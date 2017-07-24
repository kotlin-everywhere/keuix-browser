package com.github.kotlin.everywhere.browser

import com.github.kotlin.everywhere.browser.Snabbdom.h
import org.w3c.dom.Element
import org.w3c.dom.events.Event

sealed class Cmd<out S> {
    companion object {
        fun <S> none(): Cmd<S> {
            return None()
        }
    }

    class None<out S> : Cmd<S>()
}

sealed class Html<out S> {
    companion object {
        fun <S> text(string: String): Html<S> {
            return Text(string)
        }

        fun <S> div(attributes: List<Attribute<S>>, children: List<Html<S>>): Html<S> {
            return Element("div", attributes, children)
        }

        fun <S> input(attributes: List<Attribute<S>>, children: List<Html<S>>): Html<S> {
            return Element("input", attributes, children)
        }

        fun <S> button(attributes: List<Attribute<S>>, children: List<Html<S>>): Html<S> {
            return Element("button", attributes, children)
        }
    }

    internal class Text<out S>(val text: String) : Html<S>()
    internal class Element<out S>(val tagName: String, val attributes: List<Attribute<S>>, val children: List<Html<S>>) : Html<S>()
}

sealed class Attribute<out S> {
    class TextProperty<out S>(val name: String, val value: String) : Attribute<S>()
    class BooleanProperty<out S>(val name: String, val value: Boolean) : Attribute<S>()
    class EventHandler<out S>(val name: String, val value: (Event) -> S) : Attribute<S>()

    companion object {
        fun <S> class_(class_: String): Attribute<S> {
            return TextProperty("className", class_)
        }

        fun <S> disabled(disabled: Boolean): Attribute<S> {
            return BooleanProperty("disabled", disabled)
        }

        fun <S> onClick(msg: S): Attribute<S> {
            return EventHandler("click") { msg }
        }
    }
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

    private var model = init
    private val receiver: (S) -> Unit = { msg ->
        val (newModel, _) = update(msg, model)
        if (newModel != model) {
            model = newModel
            updateView()
        }
    }
    private var virtualNode = h("div", view(model).toVirtualNode(receiver))
    private val patch = if (onAfterRender != null) Snabbdom.init({ onAfterRender(container) }) else Snabbdom.init(null)

    init {
        patch(container, virtualNode)
    }

    private fun updateView() {
        virtualNode = patch(virtualNode, h("div", view(model).toVirtualNode(receiver)))
    }
}

internal fun <M, S> runProgram(container: Element, init: M, update: (S, M) -> Pair<M, Cmd<S>>, view: (M) -> Html<S>, onAfterRender: (Element) -> Unit): Program<M, S> {
    return Program(container, init, update, view, onAfterRender)
}

@Suppress("unused")
fun <M, S> runProgram(container: Element, init: M, update: (S, M) -> Pair<M, Cmd<S>>, view: (M) -> Html<S>): Program<M, S> {
    return Program(container, init, update, view, null)
}

