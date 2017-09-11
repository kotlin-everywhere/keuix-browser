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
    private data class Model(val clicked: Boolean = false, val inputValue: String = "", val checked: Boolean = false)
    private sealed class Msg {
        object Clicked : Msg()
        object Entered : Msg()
        class Checked(val checked: Boolean) : Msg()
        class NewInputValue(val inputValue: String) : Msg()
    }

    private val init = Model()

    private val update: Update<Model, Msg> = { msg, model ->
        val newModel = when (msg) {
            Msg.Clicked -> model.copy(clicked = true)
            Msg.Entered -> model.copy(inputValue = "entered")
            is Msg.NewInputValue -> model.copy(inputValue = msg.inputValue)
            is TestEvents.Msg.Checked -> model.copy(checked = msg.checked)
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

    @Test
    fun testOnDblclick() {
        serialViewTests(
                { (clicked) ->
                    Html.button(onDblclick(Msg.Clicked)) {
                        +(if (clicked) "clicked" else "")
                    }
                },
                {
                    assertEquals("<button></button>", it().html())
                    it().children().first()[0].dispatchEvent(Event("dblclick", EventInit()))
                    Unit
                },
                {
                    assertEquals("<button>clicked</button>", it().html())
                }
        )
    }

    @Test
    fun testOnBlur() {
        serialViewTests(
                { (clicked) ->
                    Html.button(onBlur(Msg.Clicked)) {
                        +(if (clicked) "clicked" else "")
                    }
                },
                {
                    assertEquals("<button></button>", it().html())
                    it().children().first()[0].dispatchEvent(Event("blur", EventInit()))
                    Unit
                },
                {
                    assertEquals("<button>clicked</button>", it().html())
                }
        )
    }

    @Test
    fun testOnChecked() {
        serialViewTests(
                { model ->
                    Html.div {
                        input(type("checkbox"), checked(model.checked), onCheck { Msg.Checked(it) })
                        +(if (model.checked) "checked" else "")
                    }

                },
                {
                    assertEquals("<div><input type=\"checkbox\"></div>", it().html())
                    it().find("input").click()
                    Unit
                },
                {
                    assertEquals("<div><input type=\"checkbox\" checked=\"\">checked</div>", it().html())
                    it().find("input").click()
                    Unit
                },
                {
                    assertEquals("<div><input type=\"checkbox\"></div>", it().html())
                }
        )
    }
}