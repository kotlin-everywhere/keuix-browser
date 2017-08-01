package com.github.kotlin.everywhere.browser

import org.junit.Test
import kotlin.test.assertEquals

class TestAttributes {
    private data class Model(val clicked: Boolean = false)
    private sealed class Msg

    private val init = Model()

    private val update: (Msg, Model) -> Pair<Model, Cmd<Msg>> = { _, model ->
        model to Cmd.none<Msg>()
    }

    private fun serialViewTests(view: (Model) -> Html<Msg>, vararg tests: (root: () -> dynamic) -> Unit) {
        asyncSerialTest(init, update, view, *tests)
    }


    @Test
    fun testClass() {
        val view: (Model) -> Html<Msg> = { _ ->
            Html.div(Attribute.class_("class"))
        }

        serialViewTests(view,
                {
                    assertEquals("<div class=\"class\"></div>", it().html())
                }
        )
    }

    @Test
    fun testDisabled() {
        val view: (Model) -> Html<Msg> = { _ ->
            Html.div {
                input(Attribute.disabled(true))
                input(Attribute.disabled(false))
            }
        }

        serialViewTests(view,
                {
                    assertEquals("<input disabled=\"\"><input>", it().children().first().html())
                }
        )
    }
}