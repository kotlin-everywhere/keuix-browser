package com.minek.kotlin.everywhere

import com.minek.kotlin.everywhere.keuix.browser.Update
import com.minek.kotlin.everywhere.keuix.browser.html.*
import org.junit.Test
import kotlin.test.assertEquals

class TestAttributes {
    private data class Model(val clicked: Boolean = false)
    private sealed class Msg

    private val init = Model()

    private val update: Update<Model, Msg> = { _, model ->
        model to null
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
                    Html.a(href( "https://github.com"))
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
                    Html.img(src( "https://octodex.github.com/images/maxtocat.gif"))
                },
                {
                    assertEquals("<img src=\"https://octodex.github.com/images/maxtocat.gif\">", it().html())
                }
        )
    }

    @Test
    fun testAttrubute() {
        serialViewTests(
                {
                    Html.button(attribute("type","button"))
                },
                {
                    assertEquals("<button type=\"button\"></button>", it().html())
                }
        )
    }
}