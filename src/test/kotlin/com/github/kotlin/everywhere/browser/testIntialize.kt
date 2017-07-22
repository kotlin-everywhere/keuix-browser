package com.github.kotlin.everywhere.browser

import com.github.kotlin.everywhere.browser.Html.Companion.text
import com.github.kotlin.everywhere.ktqunit.asyncTest
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
        val fixture = q("#qunit-fixture")
        val container = q("<div>").appendTo(fixture)[0] as Element
        asyncTest { resolve, _ ->
            val INIT = 0
            var count = 0
            runProgram(container, init, update, view) {
                when (count) {
                    INIT -> {
                        assertEquals("count = 0", fixture.text())
                        resolve(Unit)
                    }
                }
                count += 1
            }
        }
    }
}