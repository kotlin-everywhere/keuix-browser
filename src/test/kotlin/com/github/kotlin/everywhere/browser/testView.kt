package com.github.kotlin.everywhere.browser

import com.github.kotlin.everywhere.browser.Attribute.Companion.class_
import com.github.kotlin.everywhere.browser.Attribute.Companion.disabled
import com.github.kotlin.everywhere.browser.Attribute.Companion.onClick
import com.github.kotlin.everywhere.ktqunit.asyncTest
import com.github.kotlin.everywhere.ktqunit.fixture
import org.junit.Test
import org.w3c.dom.Element
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

    @Test
    fun testTextProperty() {
        val view: (Model) -> Html<Msg> = { _ ->
            Html.div(class_("class"))
        }

        val (container, root) = prepareFixture()

        asyncTest { resolve, _ ->
            runProgram(container, init, update, view) {
                assertEquals("<div class=\"class\"></div>", root().html())
                resolve(Unit)
            }
        }
    }

    @Test
    fun testBooleanProperty() {
        val view: (Model) -> Html<Msg> = { _ ->
            Html.div {
                input(disabled(true))
                input(disabled(false))
            }
        }

        val (container, root) = prepareFixture()

        asyncTest { resolve, _ ->
            runProgram(container, init, update, view) {
                assertEquals("<input disabled=\"\"><input>", root().children().first().html())
                resolve(Unit)
            }
        }
    }

    @Test
    fun testEventHandler() {
        val view: (Model) -> Html<Msg> = { (clicked) ->
            Html.button(onClick(Msg.Clicked)) {
                +(if (clicked) "clicked" else "")
            }
        }

        val (container, root) = prepareFixture()

        asyncSerialTest(container, view,
                {
                    assertEquals("<button></button>", root().html())
                    root().children().first().click()
                    Unit
                },
                {
                    assertEquals("<button>clicked</button>", root().html())
                }
        )
    }

    private fun asyncSerialTest(container: Element, view: (Model) -> Html<Msg>, vararg tests: () -> Unit) {
        val lefts = tests.toMutableList()
        return asyncTest { resolve, _ ->
            runProgram(container, init, update, view) {
                val test = lefts[0]
                lefts.removeAt(0)
                test()

                if (lefts.isEmpty()) {
                    resolve(Unit)
                }
            }
        }
    }

    private fun prepareFixture(): Pair<Element, () -> dynamic> {
        val fixture = q(fixture())
        val container = q("<div>").appendTo(fixture)[0] as Element
        val root = { fixture.children().first() }
        return container to root
    }
}