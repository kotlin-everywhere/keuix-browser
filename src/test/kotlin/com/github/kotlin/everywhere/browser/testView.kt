package com.github.kotlin.everywhere.browser

import com.github.kotlin.everywhere.browser.Attribute.Companion.class_
import com.github.kotlin.everywhere.browser.Attribute.Companion.disabled
import com.github.kotlin.everywhere.browser.Attribute.Companion.onClick
import org.junit.Test
import kotlin.test.assertEquals


class TestView {
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
    fun testTextProperty() {
        val view: (Model) -> Html<Msg> = { _ ->
            Html.div(class_("class"))
        }

        serialViewTests(view,
                {
                    assertEquals("<div class=\"class\"></div>", it().html())
                }
        )
    }

    @Test
    fun testBooleanProperty() {
        val view: (Model) -> Html<Msg> = { _ ->
            Html.div {
                input(disabled(true))
                input(disabled(false))
            }
        }

        serialViewTests(view,
                {
                    assertEquals("<input disabled=\"\"><input>", it().children().first().html())
                }
        )
    }

    @Test
    fun testEventHandler() {
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
    fun testBuilderDiv() {
        val view: (Model) -> Html<Msg> = { _ ->
            Html.div {
                div { +"division" }
            }
        }

        serialViewTests(view,
                {
                    assertEquals("<div><div>division</div></div>", it().html())
                }
        )
    }

    @Test
    fun testBuilderButton() {
        val view: (Model) -> Html<Msg> = { _ ->
            Html.div {
                button { +"label" }
            }
        }

        serialViewTests(view,
                {
                    assertEquals("<div><button>label</button></div>", it().html())
                }
        )
    }

    @Test
    fun testTextarea() {
        val view: (Model) -> Html<Msg> = { _ ->
            Html.textarea(text = "<script>alert('danger')</script>")
        }

        serialViewTests(view,
                {
                    assertEquals("<textarea>&lt;script&gt;alert('danger')&lt;/script&gt;</textarea>", it().html())
                }
        )
    }

    @Test
    fun testBuilderTextarea() {
        val view: (Model) -> Html<Msg> = { _ ->
            Html.div {
                textarea(text = "<script>alert('danger')</script>")
            }
        }

        serialViewTests(view,
                {
                    assertEquals("<div><textarea>&lt;script&gt;alert('danger')&lt;/script&gt;</textarea></div>", it().html())
                }
        )
    }

    @Test
    fun testPre() {
        val view: (Model) -> Html<Msg> = { _ ->
            Html.pre(text = "<script>alert('danger')</script>")
        }

        serialViewTests(view,
                {
                    assertEquals("<pre>&lt;script&gt;alert('danger')&lt;/script&gt;</pre>", it().html())
                }
        )
    }

    @Test
    fun testBuilderPre() {
        val view: (Model) -> Html<Msg> = { _ ->
            Html.div {
                pre(text = "<script>alert('danger')</script>")
            }
        }

        serialViewTests(view,
                {
                    assertEquals("<div><pre>&lt;script&gt;alert('danger')&lt;/script&gt;</pre></div>", it().html())
                }
        )
    }


}