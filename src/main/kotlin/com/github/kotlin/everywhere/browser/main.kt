package com.github.kotlin.everywhere.browser

import com.github.kotlin.everywhere.browser.Snabbdom.h
import org.w3c.dom.Element

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

        fun <S> div(children: List<Html<S>>): Html<S> {
            return Element("div", children)
        }
    }

    internal class Text<out S>(val text: String) : Html<S>()
    internal class Element<out S>(val tagName: String, val children: List<Html<S>>) : Html<S>()
}

private fun <S> Html<S>.toVirtualNode(): dynamic {
    return when (this) {
        is Html.Text -> this.text
        is Html.Element -> h(
                this.tagName,
                this.children
                        .map {
                            @Suppress("UnsafeCastFromDynamic")
                            it.toVirtualNode()
                        }
                        .toTypedArray()
        )
    }
}

class Program<M, S>(private val container: Element, init: M,
                    private val update: (S, M) -> Pair<M, Cmd<S>>, private val view: (M) -> Html<S>, onAfterRender: (Element) -> Unit) {

    private var model = init
    private var vNode = h("div", view(model).toVirtualNode())
    private val patch = Snabbdom.init({ onAfterRender(container) })

    init {
        patch(container, vNode)
    }
}

fun <M, S> runProgram(container: Element, init: M, update: (S, M) -> Pair<M, Cmd<S>>, view: (M) -> Html<S>, onAfterRender: (Element) -> Unit): Program<M, S> {
    return Program(container, init, update, view, onAfterRender)
}

