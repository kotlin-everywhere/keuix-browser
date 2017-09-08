package com.minek.kotlin.everywhere

import com.minek.kotlin.everywhere.keuix.browser.Update
import com.minek.kotlin.everywhere.keuix.browser.html.*
import org.junit.Test
import org.w3c.dom.EventInit
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.events.KeyboardEventInit
import kotlin.test.assertEquals

class TestEvents {
    private data class Model(val clicked: Boolean = false, val inputValue: String = "")
    private sealed class Msg {
        object Clicked : Msg()
        object Entered : Msg()
        data class NewInputValue(val inputValue: String) : Msg()
    }

    private val init = Model()

    private val update: Update<Model, Msg> = { msg, model ->
        val newModel = when (msg) {
            Msg.Clicked -> model.copy(clicked = true)
            Msg.Entered -> model.copy(inputValue = "entered")
            is Msg.NewInputValue -> model.copy(inputValue = msg.inputValue)
        }
        newModel to null
    }

    private fun serialViewTests(view: (Model) -> Html<Msg>, vararg tests: (root: () -> dynamic) -> Unit) {
        asyncSerialTest(init, update, view, *tests)
    }

    @Test
    fun testOnClick() {
        serialViewTests(
                { (clicked) ->
                    Html.button(onClick(Msg.Clicked)) {
                        +(if (clicked) "clicked" else "")
                    }
                },
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
        serialViewTests(
                { model ->
                    Html.input(onInput(Msg::NewInputValue), value(model.inputValue))
                },
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

    @Test
    fun testOnEnter() {
        serialViewTests(
                { model ->
                    Html.input(onEnter(Msg.Entered), value(model.inputValue))
                },
                {
                    assertEquals("", it().children().first().`val`())
                    it().children().first()[0].dispatchEvent(KeyboardEvent("keydown", KeyboardEventInit(key = "enter", code = "13")))
                    Unit
                },
                {
                    assertEquals(
                            "entered",
                            it().children().first().`val`()
                    )
                }
        )
    }

}