package com.github.kotlin.everywhere.browser

import com.github.kotlin.everywhere.browser.Attribute.Companion.class_
import com.github.kotlin.everywhere.browser.Html.Companion.div
import com.github.kotlin.everywhere.ktqunit.asyncTest
import com.github.kotlin.everywhere.ktqunit.fixture
import org.junit.Test
import org.w3c.dom.Element
import kotlin.test.assertEquals


class TestView {
    private class Model
    private sealed class Msg

    private val init = Model()

    private val update: (Msg, Model) -> Pair<Model, Cmd<Msg>> = { _, model ->
        model to Cmd.none<Msg>()
    }


    @Test
    fun testTextProperty() {
        val view: (Model) -> Html<Msg> = { _ ->
            div(listOf(class_("class")), listOf())
        }

        val (container, root) = prepareFixture()

        asyncTest { resolve, _ ->
            runProgram(container, init, update, view) {
                assertEquals("<div class=\"class\"></div>", root().html())
                resolve(Unit)
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