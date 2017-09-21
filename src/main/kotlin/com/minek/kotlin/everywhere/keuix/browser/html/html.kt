package com.minek.kotlin.everywhere.keuix.browser.html

import org.w3c.dom.events.Event

class HtmlBuilder<S> {
    internal val children = mutableListOf<Html<S>>()

    fun element(name: String, attributes: List<Attribute<S>> = listOf(), children: List<Html<S>> = listOf(), init: HtmlBuilderInit<S>? = null) {
        this.children.add(Html.element(name, attributes, children, init = init))
    }

    fun button(vararg attributes: Attribute<S>, text: String? = null, init: HtmlBuilderInit<S>? = null) {
        children.add(Html.button(*attributes, text = text, init = init))
    }

    fun div(vararg attributes: Attribute<S>, text: String? = null, init: HtmlBuilderInit<S>? = null) {
        children.add(Html.div(*attributes, text = text, init = init))
    }

    fun input(vararg attributes: Attribute<S>) {
        children.add(Html.input(*attributes))
    }

    fun header(vararg attributes: Attribute<S>, init: HtmlBuilderInit<S>? = null) {
        children.add(Html.header(*attributes, init = init))
    }

    fun footer(vararg attributes: Attribute<S>, init: HtmlBuilderInit<S>? = null) {
        children.add(Html.footer(*attributes, init = init))
    }

    fun section(vararg attributes: Attribute<S>, init: HtmlBuilderInit<S>? = null) {
        children.add(Html.section(*attributes, init = init))
    }

    fun textarea(vararg attributes: Attribute<S>, text: String? = null) {
        children.add(Html.textarea(*attributes, text = text))
    }

    fun pre(vararg attributes: Attribute<S>, text: String? = null) {
        children.add(Html.pre(*attributes, text = text))
    }

    fun span(vararg attributes: Attribute<S>, text: String? = null, init: HtmlBuilderInit<S>? = null) {
        children.add(Html.span(*attributes, text = text, init = init))
    }

    fun strong(vararg attributes: Attribute<S>, text: String? = null, init: HtmlBuilderInit<S>? = null) {
        children.add(Html.strong(*attributes, text = text, init = init))
    }

    fun label(vararg attributes: Attribute<S>, text: String? = null, init: HtmlBuilderInit<S>? = null) {
        children.add(Html.label(*attributes, text = text, init = init))
    }

    fun h1(vararg attributes: Attribute<S>, text: String? = null, init: HtmlBuilderInit<S>? = null) {
        children.add(Html.h1(*attributes, text = text, init = init))
    }

    fun h2(vararg attributes: Attribute<S>, text: String? = null, init: HtmlBuilderInit<S>? = null) {
        children.add(Html.h2(*attributes, text = text, init = init))
    }

    fun h3(vararg attributes: Attribute<S>, text: String? = null, init: HtmlBuilderInit<S>? = null) {
        children.add(Html.h3(*attributes, text = text, init = init))
    }

    fun h4(vararg attributes: Attribute<S>, text: String? = null, init: HtmlBuilderInit<S>? = null) {
        children.add(Html.h4(*attributes, text = text, init = init))
    }

    fun h5(vararg attributes: Attribute<S>, text: String? = null, init: HtmlBuilderInit<S>? = null) {
        children.add(Html.h5(*attributes, text = text, init = init))
    }

    fun h6(vararg attributes: Attribute<S>, text: String? = null, init: HtmlBuilderInit<S>? = null) {
        children.add(Html.h6(*attributes, text = text, init = init))
    }

    fun ul(vararg attributes: Attribute<S>, init: HtmlBuilderInit<S>? = null) {
        children.add(Html.ul(*attributes, init = init))
    }

    fun ol(vararg attributes: Attribute<S>, init: HtmlBuilderInit<S>? = null) {
        children.add(Html.ol(*attributes, init = init))
    }

    fun li(vararg attributes: Attribute<S>, text: String? = null, init: HtmlBuilderInit<S>? = null) {
        children.add(Html.li(*attributes, text = text, init = init))
    }

    fun a(vararg attributes: Attribute<S>, text: String? = null, init: HtmlBuilderInit<S>? = null) {
        children.add(Html.a(*attributes, text = text, init = init))
    }

    fun p(vararg attributes: Attribute<S>, text: String? = null, init: HtmlBuilderInit<S>? = null) {
        children.add(Html.p(*attributes, text = text, init = init))
    }

    fun img(vararg attributes: Attribute<S>, init: HtmlBuilderInit<S>? = null) {
        children.add(Html.img(*attributes, init = init))
    }

    fun form(vararg attributes: Attribute<S>, init: HtmlBuilderInit<S>? = null) {
        element("form", attributes.toList(), init = init)
    }

    fun fieldset(vararg attributes: Attribute<S>, init: HtmlBuilderInit<S>? = null) {
        element("fieldset", attributes.toList(), init = init)
    }

    fun legend(vararg attributes: Attribute<S>, text: String? = null, init: HtmlBuilderInit<S>? = null) {
        element("legend", attributes.toList(), text?.let { listOf(Html.text<S>(text)) } ?: listOf(), init)
    }

    fun nav(vararg attributes: Attribute<S>, init: HtmlBuilderInit<S>? = null) {
        element("nav", attributes.toList(), init = init)
    }

    operator fun String.unaryPlus() {
        children.add(Html.text(this))
    }

    operator fun Html<S>.unaryPlus() {
        children.add(this)
    }
}

typealias HtmlBuilderInit<S> = HtmlBuilder<S>.() -> Unit

@Suppress("unused")
sealed class Html<out S> {
    companion object {
        fun <S> element(name: String, attributes: List<Attribute<S>>? = null, children: List<Html<S>>? = null, init: HtmlBuilderInit<S>? = null): Html<S> {
            val builderChildren = if (init != null) HtmlBuilder<S>().apply(init).children else null
            return Element(name, attributes ?: listOf(), (children ?: listOf()) + (builderChildren ?: listOf()))
        }

        fun <S> button(vararg attributes: Attribute<S>, text: String? = null, init: HtmlBuilderInit<S>? = null): Html<S> {
            val children: List<Html<S>> = if (init != null) HtmlBuilder<S>().apply(init).children else listOf()
            return Element(
                    "button", attributes.asList(),
                    if (text != null) listOf(text<S>(text)) + children else children
            )
        }

        fun <S> div(vararg attributes: Attribute<S>, text: String? = null, init: HtmlBuilderInit<S>? = null): Html<S> {
            val children: List<Html<S>> = if (init != null) HtmlBuilder<S>().apply(init).children else listOf()
            return Element(
                    "div", attributes.asList(),
                    if (text != null) listOf(text<S>(text)) + children else children
            )
        }

        fun <S> ul(vararg attributes: Attribute<S>, init: HtmlBuilderInit<S>? = null): Html<S> {
            return Element(
                    "ul", attributes.asList(),
                    if (init != null)
                        HtmlBuilder<S>().apply(init).children
                    else
                        listOf()
            )
        }

        fun <S> ol(vararg attributes: Attribute<S>, init: HtmlBuilderInit<S>? = null): Html<S> {
            return Element(
                    "ol", attributes.asList(),
                    if (init != null)
                        HtmlBuilder<S>().apply(init).children
                    else
                        listOf()
            )
        }

        fun <S> li(vararg attributes: Attribute<S>, text: String? = null, init: HtmlBuilderInit<S>? = null): Html<S> {
            val children: List<Html<S>> = if (init != null) HtmlBuilder<S>().apply(init).children else listOf()
            return Element(
                    "li", attributes.asList(),
                    if (text != null) listOf(text<S>(text)) + children else children
            )
        }

        fun <S> header(vararg attributes: Attribute<S>, init: HtmlBuilderInit<S>? = null): Html<S> {
            return Element(
                    "header", attributes.asList(),
                    if (init != null)
                        HtmlBuilder<S>().apply(init).children
                    else
                        listOf()
            )
        }

        fun <S> footer(vararg attributes: Attribute<S>, init: HtmlBuilderInit<S>? = null): Html<S> {
            return Element(
                    "footer", attributes.asList(),
                    if (init != null)
                        HtmlBuilder<S>().apply(init).children
                    else
                        listOf()
            )
        }

        fun <S> section(vararg attributes: Attribute<S>, init: HtmlBuilderInit<S>? = null): Html<S> {
            return Element(
                    "section", attributes.asList(),
                    if (init != null)
                        HtmlBuilder<S>().apply(init).children
                    else
                        listOf()
            )
        }

        fun <S> span(vararg attributes: Attribute<S>, text: String? = null, init: HtmlBuilderInit<S>? = null): Html<S> {
            val children: List<Html<S>> = if (init != null) HtmlBuilder<S>().apply(init).children else listOf()
            return Element(
                    "span", attributes.asList(),
                    if (text != null) listOf(text<S>(text)) + children else children
            )
        }

        fun <S> strong(vararg attributes: Attribute<S>, text: String? = null, init: HtmlBuilderInit<S>? = null): Html<S> {
            val children: List<Html<S>> = if (init != null) HtmlBuilder<S>().apply(init).children else listOf()
            return Element(
                    "strong", attributes.asList(),
                    if (text != null) listOf(text<S>(text)) + children else children
            )
        }

        fun <S> label(vararg attributes: Attribute<S>, text: String? = null, init: HtmlBuilderInit<S>? = null): Html<S> {
            val children: List<Html<S>> = if (init != null) HtmlBuilder<S>().apply(init).children else listOf()
            return Element(
                    "label", attributes.asList(),
                    if (text != null) listOf(text<S>(text)) + children else children
            )
        }

        fun <S> h1(vararg attributes: Attribute<S>, text: String? = null, init: HtmlBuilderInit<S>? = null): Html<S> {
            val children: List<Html<S>> = if (init != null) HtmlBuilder<S>().apply(init).children else listOf()
            return Element(
                    "h1", attributes.asList(),
                    if (text != null) listOf(text<S>(text)) + children else children
            )
        }

        fun <S> h2(vararg attributes: Attribute<S>, text: String? = null, init: HtmlBuilderInit<S>? = null): Html<S> {
            val children: List<Html<S>> = if (init != null) HtmlBuilder<S>().apply(init).children else listOf()
            return Element(
                    "h2", attributes.asList(),
                    if (text != null) listOf(text<S>(text)) + children else children
            )
        }

        fun <S> h3(vararg attributes: Attribute<S>, text: String? = null, init: HtmlBuilderInit<S>? = null): Html<S> {
            val children: List<Html<S>> = if (init != null) HtmlBuilder<S>().apply(init).children else listOf()
            return Element(
                    "h3", attributes.asList(),
                    if (text != null) listOf(text<S>(text)) + children else children
            )
        }

        fun <S> h4(vararg attributes: Attribute<S>, text: String? = null, init: HtmlBuilderInit<S>? = null): Html<S> {
            val children: List<Html<S>> = if (init != null) HtmlBuilder<S>().apply(init).children else listOf()
            return Element(
                    "h4", attributes.asList(),
                    if (text != null) listOf(text<S>(text)) + children else children
            )
        }

        fun <S> h5(vararg attributes: Attribute<S>, text: String? = null, init: HtmlBuilderInit<S>? = null): Html<S> {
            val children: List<Html<S>> = if (init != null) HtmlBuilder<S>().apply(init).children else listOf()
            return Element(
                    "h5", attributes.asList(),
                    if (text != null) listOf(text<S>(text)) + children else children
            )
        }

        fun <S> h6(vararg attributes: Attribute<S>, text: String? = null, init: HtmlBuilderInit<S>? = null): Html<S> {
            val children: List<Html<S>> = if (init != null) HtmlBuilder<S>().apply(init).children else listOf()
            return Element(
                    "h6", attributes.asList(),
                    if (text != null) listOf(text<S>(text)) + children else children
            )
        }

        fun <S> a(vararg attributes: Attribute<S>, text: String? = null, init: HtmlBuilderInit<S>? = null): Html<S> {
            val children: List<Html<S>> = if (init != null) HtmlBuilder<S>().apply(init).children else listOf()
            return Element(
                    "a", attributes.asList(),
                    if (text != null) listOf(text<S>(text)) + children else children
            )
        }

        fun <S> p(vararg attributes: Attribute<S>, text: String? = null, init: HtmlBuilderInit<S>? = null): Html<S> {
            val children: List<Html<S>> = if (init != null) HtmlBuilder<S>().apply(init).children else listOf()
            return Element(
                    "p", attributes.asList(),
                    if (text != null) listOf(text<S>(text)) + children else children
            )
        }

        fun <S> img(vararg attributes: Attribute<S>, init: HtmlBuilderInit<S>? = null): Html<S> {
            val children: List<Html<S>> = if (init != null) HtmlBuilder<S>().apply(init).children else listOf()
            return Element(
                    "img", attributes.asList(),
                    children
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

        fun <S> form(vararg attributes: Attribute<S>, init: HtmlBuilderInit<S>? = null): Html<S> {
            return element("form", attributes.toList(), init = init)
        }

        fun <S> fieldset(vararg attributes: Attribute<S>, init: HtmlBuilderInit<S>? = null): Html<S> {
            return element("fieldset", attributes.toList(), init = init)
        }

        fun <S> legend(vararg attributes: Attribute<S>, text: String? = null, init: HtmlBuilderInit<S>? = null): Html<S> {
            return element("legend", attributes.asList(), text?.let { listOf(text<S>(text)) } ?: listOf(), init)
        }

        fun <S> nav(vararg attributes: Attribute<S>, init: HtmlBuilderInit<S>? = null): Html<S> {
            return element("nav", attributes.toList(), init = init)
        }

        fun <a, msg> map(tagger: (a) -> msg, html: Html<a>): Html<msg> {
            return Tagger(tagger, html)
        }
    }

    internal class Text<out S>(val text: String) : Html<S>()
    internal class Element<out S>(val tagName: String, val attributes: List<Attribute<S>>, val children: List<Html<S>>) : Html<S>()
    internal class Tagger<out S, P>(val tagger: (P) -> S, val html: Html<P>) : Html<S>()
}

@Suppress("unused")
sealed class Attribute<out S> {
    class DatasetProperty<out S>(val name: String, val value: String) : Attribute<S>()
    class TextProperty<out S>(val name: String, val value: String) : Attribute<S>()
    class BooleanProperty<out S>(val name: String, val value: Boolean) : Attribute<S>()
    class TextAttribute<out S>(val name: String, val value: String) : Attribute<S>()
    class BooleanAttribute<out S>(val name: String, val value: Boolean) : Attribute<S>()
    class EventHandler<out S>(val name: String, val value: (Event) -> S?) : Attribute<S>()
    class Key<out S>(val key: String) : Attribute<S>()
}
