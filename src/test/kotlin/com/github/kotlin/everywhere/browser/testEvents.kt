package com.github.kotlin.everywhere.browser

import org.junit.Test
import kotlin.test.assertEquals

class TestEvents {
    private data class Model(val clicked: Boolean = false)
    private sealed class Msg {
        object Clicked : Msg()
    }

    private val init = Model()

    private val update: (Msg, Model) -> Pair<Model, Cmd<Msg>> = { msg, model ->
        val newModel = when (msg) {
            Msg.Clicked -> model.copy(clicked = true)
        }
        newModel to Cmd.none<Msg>()
    }

    private fun serialViewTests(view: (Model) -> Html<Msg>, vararg tests: (root: () -> dynamic) -> Unit) {
        asyncSerialTest(init, update, view, *tests)
    }

    @Test
    fun testOnClick() {
        val view: (Model) -> Html<Msg> = { (clicked) ->
            Html.button(Attribute.onClick(Msg.Clicked)) {
                +(if (clicked) "clicked" else "")
            }
        }

        serialViewTests(view,
                {
                    assertEquals("<button></button>", it().html())
                    it().children().first().click()
                    Unit
                },
                {
                    assertEquals("<button>clicked</button>", it().html())
                }
        )
    }
}