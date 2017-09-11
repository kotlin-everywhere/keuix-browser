package com.minek.kotlin.everywhere

import com.minek.kotlin.everywhere.keuix.browser.Update
import com.minek.kotlin.everywhere.keuix.browser.html.*
import org.junit.Test
import org.w3c.dom.HTMLElement
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TestAttributes {
    private data class Model(val fired: Boolean = false)
    private sealed class Msg {
        object Fire : Msg()
    }

    private val init = Model()

    private val update: Update<Model, Msg> = { msg, model ->
        when (msg) {
            Msg.Fire -> model.copy(fired = true) to null
        }
    }

    private fun serialViewTests(view: (Model) -> Html<Msg>, vararg tests: (root: () -> dynamic) -> Unit) {
        asyncSerialTest(init, update, view, *tests)
    }


    @Test
    fun testClass() {
        serialViewTests(
                { Html.div(class_("class")) },
                { assertEquals("<div class=\"class\"></div>", it().html()) }
        )
    }

    @Test
    fun testClasses() {
        serialViewTests(
                { Html.div(classes("first" to true, "second" to false, class_ = "default")) },
                { assertEquals("<div class=\"default first\"></div>", it().html()) }
        )
    }

    @Test
    fun testDisabled() {
        serialViewTests(
                {
                    Html.div {
                        input(disabled(true))
                        input(disabled(false))
                    }
                },
                {
                    assertEquals("<input disabled=\"\"><input>", it().children().first().html())
                }
        )
    }

    @Test
    fun testValue() {
        serialViewTests(
                {
                    Html.input(value("<script>alert('danger')</script>"))
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
    fun testStyle() {
        serialViewTests(
                {
                    Html.div(style("font-weight: bold;"))
                },
                {
                    assertEquals("bold", it().children().first().css("font-weight"))
                }
        )
    }

    @Test
    fun testHref() {
        serialViewTests(
                {
                    Html.a(href("https://github.com"))
                },
                {
                    assertEquals("<a href=\"https://github.com\"></a>", it().html())
                }
        )
    }

    @Test
    fun testSrc() {
        serialViewTests(
                {
                    Html.img(src("https://octodex.github.com/images/maxtocat.gif"))
                },
                {
                    assertEquals("<img src=\"https://octodex.github.com/images/maxtocat.gif\">", it().html())
                }
        )
    }

    @Test
    fun testAttribute() {
        serialViewTests(
                {
                    Html.button(attribute("type", "button"))
                },
                {
                    assertEquals("<button type=\"button\"></button>", it().html())
                }
        )
    }

    @Test
    fun testPlaceholder() {
        serialViewTests(
                {
                    Html.input(placeholder("placeholder"))
                },
                {
                    assertEquals("<input placeholder=\"placeholder\">", it().html())
                }
        )
    }

    @Test
    fun testAutoFocus() {
        serialViewTests(
                {
                    Html.input(autofocus(true))
                },
                {
                    assertEquals("<input autofocus=\"\">", it().html())
                }
        )
    }

    @Test
    fun testId() {
        serialViewTests(
                {
                    Html.div(id("test-attribute-test-id"))
                },
                {
                    assertEquals("<div id=\"test-attribute-test-id\"></div>", it().html())
                }
        )
    }


    @Test
    fun testType() {
        serialViewTests(
                {
                    Html.input(type("checkbox"))
                },
                {
                    assertEquals("<input type=\"checkbox\">", it().html())
                }
        )
    }

    @Test
    fun testChecked() {
        serialViewTests(
                {
                    Html.input(type("checkbox"), checked(true))
                },
                {
                    assertEquals(true, it().children()[0].checked)
                }
        )
    }

    @Test
    fun testFor() {
        serialViewTests(
                {
                    Html.label(for_("id-field"))
                },
                {
                    assertEquals("<label for=\"id-field\"></label>", it().html())
                }
        )
    }

    @Test
    fun testKey() {
        var second: HTMLElement? = null

        serialViewTests(
                { (fired) ->
                    Html.div {
                        ul {
                            if (!fired) {
                                li(text = "first")
                            }
                            li(key("second"), text = "second")
                        }
                        button(onClick(Msg.Fire))
                    }
                },
                {
                    second = it().find("li")[1] as? HTMLElement
                    it().find("button").click()
                    Unit
                },
                {
                    assertTrue(second === it().find("li")[0])
                }
        )
    }
}