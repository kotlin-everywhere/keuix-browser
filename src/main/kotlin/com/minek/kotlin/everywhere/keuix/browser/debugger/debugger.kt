package com.minek.kotlin.everywhere.keuix.browser.debugger

import com.minek.kotlin.everywhere.keuix.browser.Cmd
import com.minek.kotlin.everywhere.keuix.browser.Update
import com.minek.kotlin.everywhere.keuix.browser.View
import com.minek.kotlin.everywhere.keuix.browser.html.Html
import com.minek.kotlin.everywhere.keuix.browser.html.onClick
import com.minek.kotlin.everywhere.keuix.browser.html.style


internal fun <M, S : Any> debugger(userInit: M, userUpdate: Update<M, S>, userView: View<M, S>, userCmd: Cmd<S>? = null): Debugger<M, S> {
    return Debugger(
            init = Model(user = userInit),
            update = { msg, model -> update(userUpdate, msg, model) },
            view = { view(userView, it) },
            cmd = Cmd.map(userCmd, ::UserMsg)
    )
}

data class Debugger<M, S : Any>(
        val init: Model<M, S>,
        val update: Update<Model<M, S>, Msg<S>>,
        val view: View<Model<M, S>, Msg<S>>,
        val cmd: Cmd<Msg<S>>?
)

data class Model<M, S>(
        val user: M,
        val states: List<Pair<S, M>> = listOf(),
        val currentStateIndex: Int? = null
)

sealed class Msg<S : Any>

private data class UserMsg<S : Any>(val user: S) : Msg<S>()
private data class SetCurrentStateIndex<S : Any>(val index: Int) : Msg<S>()
private class CloseNavigate<S : Any> : Msg<S>()

private fun <M, S : Any> update(userUpdate: Update<M, S>, msg: Msg<S>, model: Model<M, S>): Pair<Model<M, S>, Cmd<Msg<S>>?> {
    return when (msg) {
        is UserMsg -> {
            val (user, userCmd) = userUpdate(msg.user, model.user)
            val state = msg.user to user
            val currentStateIndex =
                    if (model.currentStateIndex == model.states.lastIndex) {
                        model.currentStateIndex + 1
                    } else {
                        model.currentStateIndex
                    }
            val newModel = model.copy(
                    user = user,
                    states = model.states + state,
                    currentStateIndex = currentStateIndex
            )
            newModel to Cmd.map(userCmd, ::UserMsg)
        }

        is SetCurrentStateIndex -> {
            model.copy(currentStateIndex = msg.index) to null
        }

        is CloseNavigate -> {
            model.copy(currentStateIndex = null, user = model.states.first().second) to null
        }
    }
}

private fun <M, S : Any> view(userView: View<M, S>, model: Model<M, S>): Html<Msg<S>> {
    val user =
            model.currentStateIndex
                    ?.let { if (it < model.states.size) model.states[it].second else null }
                    ?: model.user

    return Html.div {
        +Html.map(::UserMsg, userView(user))
        +debuggerView(model.currentStateIndex, model.states)
    }
}

private fun <M, S : Any> debuggerView(currentState: Int?, states: List<Pair<S, M>>): Html<Msg<S>> {
    return if (currentState != null) {
        viewNavigateState(currentState, states)
    } else {
        viewSimpleStates(states)
    }
}

private fun <M, S : Any> viewNavigateState(currentStateIndex: Int, states: List<Pair<S, M>>): Html<Msg<S>> {

    return Html.div {
        // blocker
        div(style("position: fixed; top: 0; left: 0; width: 100%; height: 100%; z-index: 999999999;"))
        div(style("position: fixed; bottom: 0; right: 0; width: 800px; height: 300px; border: 1px solid black; overflow: hidden; z-index: 999999999;")) {
            div(style("width: 300px; float: left; background-color: black; height: 90%; overflow: hidden scroll")) {
                states.forEachIndexed { index, it ->
                    val bStyle =
                            if (index == currentStateIndex)
                                "background-color: gray; color: white"
                            else
                                "background-color: black; color: white"
                    button(
                            onClick(SetCurrentStateIndex(index)),
                            style("width: 100%; font: normal normal normal 13px sans-serif; padding: 10px; border:none; outline:0; background-color: black; text-align: left; text-overflow: ellipsis; white-space: nowrap; overflow: hidden; $bStyle"),
                            text = it.first.dumps()
                    )
                }
            }
            div(style("width: 500px; float: right; background-color: white; height: 100%; overflow-y: scroll")) {
                val cs = states.getOrNull(currentStateIndex)
                val (msgText, modelText) = if (cs == null) {
                    "" to ""
                } else {
                    cs.first.dumps() to (cs.second?.dumps() ?: "null")
                }
                div(style("font: normal normal normal 20px sans-serif; color: black; padding: 20px;"), text = msgText)
                div(style("font: normal normal normal 13px sans-serif; padding: 20px; border-top: 1px solid #3f51b5"), text = modelText)
            }
            button(
                    onClick(CloseNavigate()),
                    style("width: 300px; font: normal normal normal 14px sans-serif; padding: 10px; position: absolute; bottom: 0px; left: 0px; border:none; background-color: #009688; color: white"),
                    text = "Close"
            )
//            button(onClick(CloseNavigate()), style("position: absolute; top: 10px;"), text = "X")
//            ol {
//                states.forEachIndexed { index, it ->
//                    val liStyle = if (index == currentStateIndex) {
//                        "color: red"
//                    } else {
//                        ""
//                    }
//                    li(style(liStyle), onClick(SetCurrentStateIndex(index)), text = "${it.first} -> ${it.second}")
//                }
//            }
        }
    }
}

fun Any.dumps(): String {
    return if (toString() == "[object Object]") this::class.simpleName ?: "Invalid Kotlin Object"
    else toString()
}


private fun <M, S : Any> viewSimpleStates(states: List<Pair<S, M>>): Html<Msg<S>> {
    val currentStateIndex = if (states.isNotEmpty()) states.lastIndex else null
    return Html.div(style("position: fixed; bottom: 0; right: 0; padding: 10px; background-color: black; border-radius: 10px 0px 0px 0px")) {
        val style = style<Msg<S>>("color: white; cursor: pointer")

        if (currentStateIndex == null) {
            h1(style, text = "Init")
        } else {
            h1(style, onClick(SetCurrentStateIndex(currentStateIndex)), text = "History (${states.size})")
        }
    }
}
