package com.minek.kotlin.everywhere

import com.minek.kotlin.everywhere.keuix.browser.*
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
}