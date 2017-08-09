package com.minek.kotlin.everywhere.keuix.browser.html

import org.w3c.dom.events.Event

class HtmlBuilder<S> {
    internal val children = mutableListOf<Html<S>>()

    fun button(vararg attributes: Attribute<S>, init: HtmlBuilderInit<S>? = null) {
        children.add(Html.button(*attributes, init = init))
    }

    fun div(vararg attributes: Attribute<S>, init: HtmlBuilderInit<S>? = null) {
        children.add(Html.div(*attributes, init = init))
    }

    fun input(vararg attributes: Attribute<S>) {
        children.add(Html.input(*attributes))
    }

    fun textarea(vararg attributes: Attribute<S>, text: String? = null) {
        children.add(Html.textarea(*attributes, text = text))
    }

    fun pre(vararg attributes: Attribute<S>, text: String? = null) {
        children.add(Html.pre(*attributes, text = text))
    }

    operator fun String.unaryPlus() {
        children.add(Html.text(this))
    }
}

typealias HtmlBuilderInit<S> = HtmlBuilder<S>.() -> Unit

@Suppress("unused")
sealed class Html<out S> {
    companion object {
        fun <S> button(vararg attributes: Attribute<S>, init: HtmlBuilderInit<S>? = null): Html<S> {
            return Element(
                    "button", attributes.asList(),
                    if (init != null)
                        HtmlBuilder<S>().apply(init).children
                    else
                        listOf()
            )
        }

        fun <S> div(vararg attributes: Attribute<S>, init: HtmlBuilderInit<S>? = null): Html<S> {
            return Element(
                    "div", attributes.asList(),
                    if (init != null)
                        HtmlBuilder<S>().apply(init).children
                    else
                        listOf()
            )
        }

        fun <S> input(vararg attributes: Attribute<S>): Html<S> {
            return Element("input", attributes.asList(), listOf())
        }

        fun <S> pre(vararg attributes: Attribute<S>, text: String? = null): Html<S> {
            return Element(
                    "pre",
                    attributes.asList(),
                    if (text == null) listOf() else listOf(text(text))
            )
        }

        fun <S> text(string: String): Html<S> {
            return Text(string)
        }

        fun <S> textarea(vararg attributes: Attribute<S>, text: String? = null): Html<S> {
            return Element(
                    "textarea",
                    attributes.asList(),
                    if (text == null) listOf() else listOf(text(text))
            )
        }
    }

    internal class Text<out S>(val text: String) : Html<S>()
    internal class Element<out S>(val tagName: String, val attributes: List<Attribute<S>>, val children: List<Html<S>>) : Html<S>()
}

@Suppress("unused")
sealed class Attribute<out S> {
    class TextProperty<out S>(val name: String, val value: String) : Attribute<S>()
    class BooleanProperty<out S>(val name: String, val value: Boolean) : Attribute<S>()
    class EventHandler<out S>(val name: String, val value: (Event) -> S) : Attribute<S>()
}
