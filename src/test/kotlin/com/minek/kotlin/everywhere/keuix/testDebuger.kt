package com.minek.kotlin.everywhere.keuix

import com.minek.kotlin.everywhere.keuix.browser.html.Html
import com.minek.kotlin.everywhere.keuix.browser.html.onClick
import com.minek.kotlin.everywhere.keuix.browser.runBeginnerProgramDebug
import kotlin.browser.window
import kotlin.dom.createElement

@Suppress("unused")
@JsName("testDebugger")
fun testDebugger() {
    val container = window.document.createElement("div") {}
    window.document.body!!.appendChild(container)

    runBeginnerProgramDebug(root = container, init = Model(), update = ::update, view = ::view)
}

data class Model(val count: Int = 0)

sealed class Msg

object Inc : Msg()
object Dec : Msg()

fun update(msg: Msg, model: Model): Model {
    return when (msg) {
        Inc -> model.copy(count = model.count + 1)
        Dec -> model.copy(count = model.count - 1)
    }
}

fun view(model: Model): Html<Msg> {
    return Html.div {
        button(onClick(Inc), text = "+")
        h1(text = "${model.count}")
        button(onClick(Dec), text = "-")
    }
}
