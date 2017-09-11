package com.minek.kotlin.everywhere.keuix.browser.html


fun <S> onClick(msg: S): Attribute<S> {
    return Attribute.EventHandler("click") { msg }
}

fun <S> onInput(tagger: (String) -> S): Attribute<S> {
    return Attribute.EventHandler("input") {
        val target: dynamic = it.target
        tagger(target.value as String)
    }
}

fun <S> onEnter(msg: S): Attribute<S> {
    return Attribute.EventHandler("keydown") {
        if (it.asDynamic().key == "enter" || it.asDynamic().keyCode == 13) {
            it.preventDefault()
            msg
        } else {
            null
        }
    }
}

fun <S> onDblclick(msg: S): Attribute<S> {
    return Attribute.EventHandler("dblclick") { msg }
}