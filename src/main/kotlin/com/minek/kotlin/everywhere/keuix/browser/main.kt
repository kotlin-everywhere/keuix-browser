package com.minek.kotlin.everywhere.keuix.browser

import com.minek.kotlin.everywhere.keuix.browser.Snabbdom.h
import org.w3c.dom.Element
import org.w3c.dom.events.Event
import kotlin.browser.window

@Suppress("unused")
sealed class Cmd<out S> {
    companion object {
        fun <S> none(): Cmd<S> {
            return None()
        }
    }

    class None<out S> : Cmd<S>()
}

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

fun <S> class_(class_: String): Attribute<S> {
    return Attribute.TextProperty("className", class_)
}

fun <S> disabled(disabled: Boolean): Attribute<S> {
    return Attribute.BooleanProperty("disabled", disabled)
}

fun <S> onClick(msg: S): Attribute<S> {
    return Attribute.EventHandler("click") { msg }
}

fun <S> onInput(tagger: (String) -> S): Attribute<S> {
    return Attribute.EventHandler("input") {
        val target: dynamic = it.target
        tagger(target.value as String)
    }
}

fun <S> value(value: String): Attribute<S> {
    return Attribute.TextProperty("value", value)
}


fun <S> style(style: String): Attribute<S> {
    return Attribute.TextProperty("style", style)
}

private fun <S> List<Attribute<S>>.toProps(receiver: (S) -> Unit): Pair<dynamic, dynamic> {
    val props: dynamic = object {}
    val on: dynamic = object {}

    this.forEach { attr ->
        when (attr) {
            is Attribute.TextProperty -> props[attr.name] = attr.value
            is Attribute.BooleanProperty -> props[attr.name] = attr.value
            is Attribute.EventHandler -> on[attr.name] = { event: Event -> receiver(attr.value(event)) }
        }
    }

    return Pair(props, on)
}

private fun <S> Html<S>.toVirtualNode(receiver: (S) -> Unit): dynamic {
    return when (this) {
        is Html.Text -> this.text
        is Html.Element -> {
            val data: dynamic = object {}
            val (props, on) = this.attributes.toProps(receiver)
            data["props"] = props
            data["on"] = on
            h(
                    this.tagName,
                    data,
                    this.children
                            .map {
                                @Suppress("UnsafeCastFromDynamic")
                                it.toVirtualNode(receiver)
                            }
                            .toTypedArray()
            )
        }
    }
}

class Program<M, S>(private val container: Element, init: M,
                    private val update: (S, M) -> Pair<M, Cmd<S>>, private val view: (M) -> Html<S>, onAfterRender: ((Element) -> Unit)?) {

    private var requestAnimationFrameId: Int? = null
    private var model = init
    private var previousModel = init

    private val _updateView: (Double) -> Unit = { updateView() }
    private val receiver: (S) -> Unit = { msg ->
        val (newModel, _) = update(msg, model)
        if (requestAnimationFrameId == null && model != newModel) {
            model = newModel
            requestAnimationFrameId = window.requestAnimationFrame(_updateView)
        }
    }
    private var virtualNode = h("div", view(model).toVirtualNode(receiver))
    private val patch = if (onAfterRender != null) Snabbdom.init({ onAfterRender(container) }) else Snabbdom.init(null)

    init {
        patch(container, virtualNode)
    }

    private fun updateView() {
        requestAnimationFrameId = null
        if (previousModel != model) {
            previousModel = model
            virtualNode = patch(virtualNode, h("div", view(model).toVirtualNode(receiver)))
        }
    }
}

internal fun <M, S> runProgram(container: Element, init: M, update: (S, M) -> Pair<M, Cmd<S>>, view: (M) -> Html<S>, onAfterRender: (Element) -> Unit): Program<M, S> {
    return Program(container, init, update, view, onAfterRender)
}

@Suppress("unused")
fun <M, S> runProgram(container: Element, init: M, update: (S, M) -> Pair<M, Cmd<S>>, view: (M) -> Html<S>): Program<M, S> {
    return Program(container, init, update, view, null)
}

internal fun <M, S> runBeginnerProgram(container: Element, init: M, update: (S, M) -> M, view: (M) -> Html<S>, onAfterRender: (Element) -> Unit): Program<M, S> {
    return Program(container, init, { s, m -> update(s, m) to Cmd.none() }, view, onAfterRender)
}

@Suppress("unused")
fun <M, S> runBeginnerProgram(container: Element, init: M, update: (S, M) -> M, view: (M) -> Html<S>): Program<M, S> {
    return Program(container, init, { s, m -> update(s, m) to Cmd.none() }, view, null)
}

internal fun runBeginnerProgram(container: Element, view: () -> Html<Unit>, onAfterRender: (Element) -> Unit): Program<Unit, Unit> {
    return Program(container, Unit, { s, m -> Unit to Cmd.none() }, { view() }, onAfterRender)
}

@Suppress("unused")
internal fun runBeginnerProgram(container: Element, view: () -> Html<Unit>): Program<Unit, Unit> {
    return Program(container, Unit, { s, m -> Unit to Cmd.none() }, { view() }, null)
}