package com.minek.kotlin.everywhere

import com.minek.kotlin.everywhere.keuix.browser.Cmd
import com.minek.kotlin.everywhere.keuix.browser.html.Html
import org.junit.Test
import kotlin.test.assertEquals


class TestView {
    private data class Model(val clicked: Boolean = false)
    private sealed class Msg

    private val init = Model()

    private val update: (Msg, Model) -> Pair<Model, Cmd<Msg>?> = { _, model ->
        model to null
    }

    private fun serialViewTests(view: (Model) -> Html<Msg>, vararg tests: (root: () -> dynamic) -> Unit) {
        asyncSerialTest(init, update, view, *tests)
    }

    @Test
    fun testBuilderDiv() {
        serialViewTests(
                { _ ->
                    Html.div {
                        div { +"division" }
                    }
                },
                {
                    assertEquals("<div><div>division</div></div>", it().html())
                }
        )
    }

    @Test
    fun testButton() {
        serialViewTests(
                { _ -> Html.button(text = "label") },
                {
                    assertEquals("<button>label</button>", it().html())
                }
        )
    }

    @Test
    fun testBuilderButton() {
        serialViewTests(
                { _ -> Html.div { button(text = "label") } },
                {
                    assertEquals("<div><button>label</button></div>", it().html())
                }
        )
    }

    @Test
    fun testTextarea() {
        serialViewTests(
                { _ ->
                    Html.textarea(text = "<script>alert('danger')</script>")
                },
                {
                    assertEquals("<textarea>&lt;script&gt;alert('danger')&lt;/script&gt;</textarea>", it().html())
                }
        )
    }

    @Test
    fun testBuilderTextarea() {
        serialViewTests(
                { _ ->
                    Html.div {
                        textarea(text = "<script>alert('danger')</script>")
                    }
                },
                {
                    assertEquals("<div><textarea>&lt;script&gt;alert('danger')&lt;/script&gt;</textarea></div>", it().html())
                }
        )
    }

    @Test
    fun testPre() {
        serialViewTests(
                { _ ->
                    Html.pre(text = "<script>alert('danger')</script>")
                },
                {
                    assertEquals("<pre>&lt;script&gt;alert('danger')&lt;/script&gt;</pre>", it().html())
                }
        )
    }

    @Test
    fun testBuilderPre() {
        serialViewTests(
                { _ ->
                    Html.div {
                        pre(text = "<script>alert('danger')</script>")
                    }
                },
                {
                    assertEquals("<div><pre>&lt;script&gt;alert('danger')&lt;/script&gt;</pre></div>", it().html())
                }
        )
    }


}