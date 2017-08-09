package com.minek.kotlin.everywhere.keuix.browser

import com.minek.kotlin.everywhere.keuix.browser.Snabbdom.h
import com.minek.kotlin.everywhere.keuix.browser.html.Attribute
import com.minek.kotlin.everywhere.keuix.browser.html.Html
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

internal fun runBeginnerProgram(container: Element, view: Html<Unit>, onAfterRender: (Element) -> Unit): Program<Unit, Unit> {
    return Program(container, Unit, { _, _ -> Unit to Cmd.none() }, { view }, onAfterRender)
}

@Suppress("unused")
internal fun runBeginnerProgram(container: Element, view: Html<Unit>): Program<Unit, Unit> {
    return Program(container, Unit, { _, _ -> Unit to Cmd.none() }, { view }, null)
}