package com.minek.kotlin.everywhere

import com.minek.kotlin.everywhere.TestCmd.CounterMsg.New
import com.minek.kotlin.everywhere.TestCmd.KeuseMsg.NewAdd
import com.minek.kotlin.everywhere.TestCmd.Outer.Container
import com.minek.kotlin.everywhere.keduct.bluebird.Bluebird
import com.minek.kotlin.everywhere.keduct.qunit.asyncTest
import com.minek.kotlin.everywhere.kelibs.result.Err
import com.minek.kotlin.everywhere.kelibs.result.Ok
import com.minek.kotlin.everywhere.kelibs.result.Result
import com.minek.kotlin.everywhere.keuix.browser.Cmd
import com.minek.kotlin.everywhere.keuix.browser.Update
import com.minek.kotlin.everywhere.keuix.browser.View
import com.minek.kotlin.everywhere.keuix.browser.common.TestCrate
import com.minek.kotlin.everywhere.keuix.browser.html.Html
import com.minek.kotlin.everywhere.keuix.browser.html.onClick
import com.minek.kotlin.everywhere.keuix.browser.testCrate
import org.junit.Test
import kotlin.test.assertEquals

class TestCmd {
    private sealed class CounterMsg {
        object Next : CounterMsg()
        data class New(val count: Int) : CounterMsg()
    }

    @Test
    fun testCounter() {
        var counter = 0

        fun <S> count(tagger: (Int) -> S): Cmd<S> {
            return Cmd.wrap { Bluebird { resolve: (S) -> Unit, _ -> resolve(tagger(++counter)) } }
        }

        val update: Update<Int, CounterMsg> = { msg, model ->
            when (msg) {
                CounterMsg.Next -> model to count(::New)
                is TestCmd.CounterMsg.New -> msg.count to null
            }
        }

        val view: View<Int, CounterMsg> = { model ->
            Html.button(onClick(CounterMsg.Next), text = "$model")
        }

        asyncSerialTest(0, update, view,
                {
                    assertEquals("0", it().text() as String)
                    it().find("button").click()
                    Unit
                },
                { assertEquals("1", it().text() as String) }
        )
    }

    private sealed class Outer {
        class Container(val inner: Inner) : Outer()
    }

    private sealed class Inner {
        object Value : Inner() {
            val value = "InnerValue"
        }
    }

    @Test
    fun testHtmlMap() {
        asyncSerialTest("",
                { msg: Outer, _: String ->
                    when (msg) {
                        is Outer.Container -> {
                            when (msg.inner) {
                                Inner.Value -> (msg.inner as Inner.Value).value
                            }
                        }
                    } to null
                },
                { model -> Html.map(::Container, Html.button(onClick(Inner.Value), text = model)) },
                {
                    assertEquals("", it().text() as String)
                    it().find("button").click()
                    Unit
                },
                { assertEquals("InnerValue", it().text() as String) }
        )
    }

    @Test
    fun testCmdValue() {
        asyncTest((Cmd.value("msg") as Cmd.Closure).body().then { assertEquals("msg", it) })
    }

    @Test
    fun testCmdMap() {
        asyncTest(((Cmd.map(Cmd.value("inner")) { "outer-$it" }) as Cmd.Closure).body().then { assertEquals("outer-inner", it) })
    }

    private sealed class KeuseMsg {
        object Add : KeuseMsg()
        data class NewAdd(val result: Result<String, Int>) : KeuseMsg()
    }

    @Test
    fun testKeuse() {
        val update: Update<String, KeuseMsg> = { msg, model ->
            when (msg) {
                KeuseMsg.Add -> model to testCrate.add(TestCrate.AddReq(1, 2), ::NewAdd)
                is KeuseMsg.NewAdd -> {
                    when (msg.result) {
                        is Ok -> "${msg.result.value}"
                        is Err -> msg.result.error
                    } to null
                }
            }
        }

        val view: View<String, KeuseMsg> = { model ->
            Html.button(onClick(KeuseMsg.Add), text = model)
        }

        asyncSerialTest("0", update, view,
                {
                    assertEquals("0", it().text() as String)
                    it().find("button").click()
                    Unit
                },
                { assertEquals("3", it().text() as String) }
        )
    }
}