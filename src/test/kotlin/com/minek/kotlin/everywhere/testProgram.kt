package com.minek.kotlin.everywhere

import com.minek.kotlin.everywhere.keduct.bluebird.Bluebird
import com.minek.kotlin.everywhere.keduct.qunit.asyncTest
import com.minek.kotlin.everywhere.keduct.qunit.fixture
import com.minek.kotlin.everywhere.keuix.browser.*
import com.minek.kotlin.everywhere.keuix.browser.html.Html
import com.minek.kotlin.everywhere.keuix.browser.html.onClick
import kotlin.test.Test
import org.w3c.dom.Element
import kotlin.browser.window
import kotlin.test.assertEquals

private data class Model(val count: Int = 0)
private sealed class Msg

private val init = Model()

private val update: Update<Model, Msg> = { _, model ->
    model to null
}

private val view: (Model) -> Html<Msg> = { (count) ->
    Html.text("count = $count")
}

@JsModule("jquery")
external val q: dynamic

class TestProgram {
    @Test
    fun testProgram() {
        val fixture = q(fixture())
        val container = q("<div>").appendTo(fixture)[0] as Element

        asyncTest { resolve, _ ->
            runProgram(container, init, update, view) {
                assertEquals("count = 0", fixture.text())
                resolve(Unit)
            }
        }
    }

    @Test
    fun testProgramInitialCmd() {
        asyncSerialTest(
                0,
                { _: Unit, model: Int -> (model + 1) to null },
                { Html.div(text = "$it") },
                Cmd.value(Unit),
                {
                    assertEquals("0", it().text())
                },
                {
                    assertEquals("1", it().text())
                }
        )
    }

    @Test
    fun testBeginnerProgram() {
        val fixture = q(fixture())
        val container = q("<div>").appendTo(fixture)[0] as Element

        val update: (Msg, Model) -> Model = { _, model ->
            model
        }

        asyncTest { resolve, _ ->
            runBeginnerProgram(container, init, update, view) {
                assertEquals("count = 0", fixture.text())
                resolve(Unit)
            }
        }
    }

    @Test
    fun testBeginnerViewProgram() {
        val fixture = q(fixture())
        val container = q("<div>").appendTo(fixture)[0] as Element

        asyncTest { resolve, _ ->
            runBeginnerProgram(container, Html.text("beginnerViewProgram")) {
                assertEquals("beginnerViewProgram", fixture.text())
                resolve(Unit)
            }
        }
    }

    @Test
    fun testProgramRenderTiming() {
        val update: Update<Int, Unit> = { _, model ->
            model + 1 to null
        }
        val view: View<Int, Unit> = {
            Html.button(onClick(Unit), text = "$it")
        }
        asyncSerialTest(
                0, update, view,
                {
                    assertEquals("0", it().text())
                    window.setTimeout({ it().find("button").click() }, 1)
                    window.setTimeout({ it().find("button").click() }, 2)
                },
                {
                    assertEquals("2", it().text())
                }
        )
    }

    @Test
    fun testUnmount() {
        val update = { _: Unit, model: Int -> model to null }
        val view: (Int) -> Html<Unit> = { model: Int -> Html.text("$model") }
        val tests = Bluebird.resolve(Unit)
                .andThen { serialTest(0, update, view, null, { assertEquals("0", it().text()) }) }
                .then { assertEquals("", q(fixture()).html()) }
                .andThen { serialTest(1, update, view, null, { assertEquals("1", it().text()) }) }
        asyncTest(tests)
    }
}