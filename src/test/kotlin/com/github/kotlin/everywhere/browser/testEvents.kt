package com.github.kotlin.everywhere.browser

import org.junit.Test
import org.w3c.dom.EventInit
import org.w3c.dom.events.Event
import kotlin.test.assertEquals

class TestEvents {
    private data class Model(val clicked: Boolean = false, val inputValue: String = "")
    private sealed class Msg {
        object Clicked : Msg()
        data class NewInputValue(val inputValue: String) : Msg()
    }

    private val init = Model()

    private val update: (Msg, Model) -> Pair<Model, Cmd<Msg>> = { msg, model ->
        val newModel = when (msg) {
            Msg.Clicked -> model.copy(clicked = true)
            is Msg.NewInputValue -> model.copy(inputValue = msg.inputValue)
        }
        newModel to Cmd.none<Msg>()
    }

    private fun serialViewTests(view: (Model) -> Html<Msg>, vararg tests: (root: () -> dynamic) -> Unit) {
        asyncSerialTest(init, update, view, *tests)
    }

    @Test
    fun testOnClick() {
        val view: (Model) -> Html<Msg> = { (clicked) ->
            Html.button(onClick(Msg.Clicked)) {
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

    @Test
    fun testOnInput() {
        val view: (Model) -> Html<Msg> = { model ->
            Html.input(onInput(Msg::NewInputValue), value(model.inputValue))
        }

        serialViewTests(view,
                {
                    assertEquals("", it().children().first().`val`())
                    it().children().first().`val`("<script>alert('danger')</script>")
                    it().children().first()[0].dispatchEvent(Event("input", EventInit()))
                    Unit
                },
                {
                    assertEquals(
                            "<script>alert('danger')</script>",
                            it().children().first().`val`()
                    )
                }
        )
    }

}