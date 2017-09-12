package com.minek.kotlin.everywhere

import com.minek.kotlin.everywhere.keuix.browser.Cmd
import com.minek.kotlin.everywhere.keuix.browser.html.Html
import com.minek.kotlin.everywhere.keuix.browser.html.class_
import com.minek.kotlin.everywhere.keuix.browser.html.src
import com.minek.kotlin.everywhere.keuix.browser.html.value
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
    fun testElement() {
        serialViewTests(
                { _ -> Html.element("div", listOf(class_("outer-div")), listOf(Html.text("division"))) { span(text = "inner") } },
                { assertEquals("<div class=\"outer-div\">division<span>inner</span></div>", it().html()) }
        )
    }

    @Test
    fun testBuilderElement() {
        serialViewTests(
                { _ ->
                    Html.div {
                        element("p", listOf(class_("outer-p")), listOf(Html.text("outer"))) {
                            span(text = "inner")
                        }
                    }
                },
                { assertEquals("<div><p class=\"outer-p\">outer<span>inner</span></p></div>", it().html()) }
        )
    }

    @Test
    fun testUnary() {
        serialViewTests(
                { _ ->
                    Html.div {
                        +Html.div<Msg> {
                            +Html.text<Msg>("text")
                        }
                    }
                },
                { assertEquals("<div><div>text</div></div>", it().html()) }
        )
    }

    @Test
    fun testDiv() {
        serialViewTests(
                { _ -> Html.div(text = "division") },
                { assertEquals("<div>division</div>", it().html()) }
        )
    }

    @Test
    fun testBuilderDiv() {
        serialViewTests(
                { _ -> Html.div { div(text = "division") } },
                { assertEquals("<div><div>division</div></div>", it().html()) }
        )
    }

    @Test
    fun testButton() {
        serialViewTests(
                { _ -> Html.button(text = "label") },
                { assertEquals("<button>label</button>", it().html()) }
        )
    }

    @Test
    fun testBuilderButton() {
        serialViewTests(
                { _ -> Html.div { button(text = "label") } },
                { assertEquals("<div><button>label</button></div>", it().html()) }
        )
    }

    @Test
    fun testTextarea() {
        serialViewTests(
                { _ ->
                    Html.textarea(text = "<script>alert('danger')</script>")
                },
                { assertEquals("<textarea>&lt;script&gt;alert('danger')&lt;/script&gt;</textarea>", it().html()) }
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
                { assertEquals("<div><textarea>&lt;script&gt;alert('danger')&lt;/script&gt;</textarea></div>", it().html()) }
        )
    }

    @Test
    fun testPre() {
        serialViewTests(
                { _ ->
                    Html.pre(text = "<script>alert('danger')</script>")
                },
                { assertEquals("<pre>&lt;script&gt;alert('danger')&lt;/script&gt;</pre>", it().html()) }
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
                { assertEquals("<div><pre>&lt;script&gt;alert('danger')&lt;/script&gt;</pre></div>", it().html()) }
        )
    }

    @Test
    fun testSpan() {
        serialViewTests(
                { _ -> Html.span(text = "label") },
                {
                    assertEquals("<span>label</span>", it().html())
                }
        )
    }

    @Test
    fun testBuilderSpan() {
        serialViewTests(
                { _ -> Html.div { span(text = "label") } },
                {
                    assertEquals("<div><span>label</span></div>", it().html())
                }
        )
    }

    @Test
    fun testStrong() {
        serialViewTests(
                { _ -> Html.strong(text = "label") },
                {
                    assertEquals("<strong>label</strong>", it().html())
                }
        )
    }

    @Test
    fun testBuilderStrong() {
        serialViewTests(
                { _ -> Html.div { strong(text = "label") } },
                {
                    assertEquals("<div><strong>label</strong></div>", it().html())
                }
        )
    }

    @Test
    fun testLabel() {
        serialViewTests(
                { _ -> Html.label(text = "label") },
                {
                    assertEquals("<label>label</label>", it().html())
                }
        )
    }

    @Test
    fun testBuilderLabel() {
        serialViewTests(
                { _ -> Html.div { label(text = "label") } },
                {
                    assertEquals("<div><label>label</label></div>", it().html())
                }
        )
    }

    @Test
    fun testH1() {
        serialViewTests(
                { _ -> Html.h1(text = "label") },
                {
                    assertEquals("<h1>label</h1>", it().html())
                }
        )
    }

    @Test
    fun testBuilderH1() {
        serialViewTests(
                { _ -> Html.div { h1(text = "label") } },
                {
                    assertEquals("<div><h1>label</h1></div>", it().html())
                }
        )
    }

    @Test
    fun testH2() {
        serialViewTests(
                { _ -> Html.h2(text = "label") },
                {
                    assertEquals("<h2>label</h2>", it().html())
                }
        )
    }

    @Test
    fun testBuilderH2() {
        serialViewTests(
                { _ -> Html.div { h2(text = "label") } },
                {
                    assertEquals("<div><h2>label</h2></div>", it().html())
                }
        )
    }

    @Test
    fun testH3() {
        serialViewTests(
                { _ -> Html.h3(text = "label") },
                {
                    assertEquals("<h3>label</h3>", it().html())
                }
        )
    }

    @Test
    fun testBuilderH3() {
        serialViewTests(
                { _ -> Html.div { h3(text = "label") } },
                {
                    assertEquals("<div><h3>label</h3></div>", it().html())
                }
        )
    }

    @Test
    fun testH4() {
        serialViewTests(
                { _ -> Html.h4(text = "label") },
                {
                    assertEquals("<h4>label</h4>", it().html())
                }
        )
    }

    @Test
    fun testBuilderH4() {
        serialViewTests(
                { _ -> Html.div { h4(text = "label") } },
                {
                    assertEquals("<div><h4>label</h4></div>", it().html())
                }
        )
    }

    @Test
    fun testH5() {
        serialViewTests(
                { _ -> Html.h5(text = "label") },
                {
                    assertEquals("<h5>label</h5>", it().html())
                }
        )
    }

    @Test
    fun testBuilderH5() {
        serialViewTests(
                { _ -> Html.div { h5(text = "label") } },
                {
                    assertEquals("<div><h5>label</h5></div>", it().html())
                }
        )
    }

    @Test
    fun testH6() {
        serialViewTests(
                { _ -> Html.h6(text = "label") },
                {
                    assertEquals("<h6>label</h6>", it().html())
                }
        )
    }

    @Test
    fun testBuilderH6() {
        serialViewTests(
                { _ -> Html.div { h6(text = "label") } },
                {
                    assertEquals("<div><h6>label</h6></div>", it().html())
                }
        )
    }

    @Test
    fun testSection() {
        serialViewTests(
                { _ -> Html.section { +"label" } },
                {
                    assertEquals("<section>label</section>", it().html())
                }
        )
    }

    @Test
    fun testBuilderSection() {
        serialViewTests(
                { _ -> Html.div { section(class_("abc")) { +"label" } } },
                {
                    assertEquals("<div><section class=\"abc\">label</section></div>", it().html())
                }
        )
    }

    @Test
    fun testHeader() {
        serialViewTests(
                { _ ->
                    Html.header {
                        div { +"header" }
                    }
                },
                {
                    assertEquals("<header><div>header</div></header>", it().html())
                }
        )
    }

    @Test
    fun testBuilderHeader() {
        serialViewTests(
                { _ ->
                    Html.div {
                        header {
                            div { +"header" }
                        }
                    }
                },
                {
                    assertEquals("<div><header><div>header</div></header></div>", it().html())
                }
        )
    }

    @Test
    fun testFooter() {
        serialViewTests(
                { _ ->
                    Html.footer {
                        div { +"footer" }
                    }
                },
                {
                    assertEquals("<footer><div>footer</div></footer>", it().html())
                }
        )
    }

    @Test
    fun testBuilderFooter() {
        serialViewTests(
                { _ ->
                    Html.div {
                        footer {
                            div { +"footer" }
                        }
                    }
                },
                {
                    assertEquals("<div><footer><div>footer</div></footer></div>", it().html())
                }
        )
    }

    @Test
    fun testUl() {
        serialViewTests(
                { _ -> Html.ul { +"nav" } },
                {
                    assertEquals("<ul>nav</ul>", it().html())
                }
        )
    }

    @Test
    fun testBuilderUl() {
        serialViewTests(
                { _ -> Html.div { ul { li(class_("abc")) { +"nav" } } } },
                {
                    assertEquals("<div><ul><li class=\"abc\">nav</li></ul></div>", it().html())
                }
        )
    }

    @Test
    fun testOl() {
        serialViewTests(
                { _ -> Html.ol { +"list" } },
                {
                    assertEquals("<ol>list</ol>", it().html())
                }
        )
    }

    @Test
    fun testBuilderOl() {
        serialViewTests(
                { _ -> Html.div { ol { li(class_("abc")) { +"list" } } } },
                {
                    assertEquals("<div><ol><li class=\"abc\">list</li></ol></div>", it().html())
                }
        )
    }

    @Test
    fun testli() {
        serialViewTests(
                { _ -> Html.li { +"list" } },
                {
                    assertEquals("<li>list</li>", it().html())
                }
        )
    }

    @Test
    fun testBuilderli() {
        serialViewTests(
                { _ -> Html.li(class_("abc")) { +"list" } },
                {
                    assertEquals("<li class=\"abc\">list</li>", it().html())
                }
        )
    }

    @Test
    fun testBuilderA() {
        serialViewTests(
                { _ -> Html.div { a(text = "label") } },
                {
                    assertEquals("<div><a>label</a></div>", it().html())
                }
        )
    }

    @Test
    fun testA() {
        serialViewTests(
                { _ -> Html.a(text = "label") },
                {
                    assertEquals("<a>label</a>", it().html())
                }
        )
    }

    @Test
    fun testBuilderP() {
        serialViewTests(
                { _ -> Html.div { p(text = "label") } },
                {
                    assertEquals("<div><p>label</p></div>", it().html())
                }
        )
    }

    @Test
    fun testP() {
        serialViewTests(
                { _ -> Html.p(text = "label") },
                {
                    assertEquals("<p>label</p>", it().html())
                }
        )
    }

    @Test
    fun testBuilderImg() {
        serialViewTests(
                { _ -> Html.div { img(src("https://github.com/favicon.ico")) } },
                {
                    assertEquals("<div><img src=\"https://github.com/favicon.ico\"></div>", it().html())
                }
        )
    }

    @Test
    fun testImg() {
        serialViewTests(
                { _ -> Html.img() },
                {
                    assertEquals("<img>", it().html())
                }
        )
    }

    @Test
    fun testForm() {
        serialViewTests(
                { _ -> Html.form(class_("form-class"))},
                {
                    assertEquals("<form class=\"form-class\"></form>", it().html())
                }
        )
    }

    @Test
    fun testBuilderForm() {
        serialViewTests(
                { _ -> Html.div { form(class_("form-class"))} },
                {
                    assertEquals("<div><form class=\"form-class\"></form></div>", it().html())
                }
        )
    }

    @Test
    fun testFieldset() {
        serialViewTests(
                { _ -> Html.fieldset(class_("fieldset-class"))},
                {
                    assertEquals("<fieldset class=\"fieldset-class\"></fieldset>", it().html())
                }
        )
    }

    @Test
    fun testBuilderFieldset() {
        serialViewTests(
                { _ -> Html.div { fieldset(class_("fieldset-class"))} },
                {
                    assertEquals("<div><fieldset class=\"fieldset-class\"></fieldset></div>", it().html())
                }
        )
    }
}