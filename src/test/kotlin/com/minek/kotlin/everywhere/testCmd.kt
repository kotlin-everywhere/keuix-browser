package com.minek.kotlin.everywhere

import com.minek.kotlin.everywhere.TestCmd.CounterMsg.New
import com.minek.kotlin.everywhere.keduct.bluebird.Bluebird
import com.minek.kotlin.everywhere.keuix.browser.Cmd
import com.minek.kotlin.everywhere.keuix.browser.Update
import com.minek.kotlin.everywhere.keuix.browser.View
import com.minek.kotlin.everywhere.keuix.browser.html.Html
import com.minek.kotlin.everywhere.keuix.browser.html.onClick
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
            console.info(msg, model)
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
}