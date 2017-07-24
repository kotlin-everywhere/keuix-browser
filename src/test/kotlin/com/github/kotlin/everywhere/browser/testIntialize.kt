package com.github.kotlin.everywhere.browser

import com.github.kotlin.everywhere.browser.Html.Companion.text
import com.github.kotlin.everywhere.ktqunit.asyncTest
import com.github.kotlin.everywhere.ktqunit.fixture
import org.junit.Test
import org.w3c.dom.Element
import kotlin.test.assertEquals

private data class Model(val count: Int = 0)
private sealed class Msg

private val init = Model()

private val update: (Msg, Model) -> Pair<Model, Cmd<Msg>> = { _, model ->
    model to Cmd.none<Msg>()
}

private val view: (Model) -> Html<Msg> = { (count) ->
    text("count = $count")
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
}