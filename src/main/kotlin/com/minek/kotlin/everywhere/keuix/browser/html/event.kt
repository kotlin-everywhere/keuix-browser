package com.minek.kotlin.everywhere.keuix.browser.html

import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.InputEvent
import org.w3c.dom.events.KeyboardEvent


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
        if (it.asDynamic().key == "Enter" || it.asDynamic().keyCode == 13) {
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

fun <S> onBlur(msg: S): Attribute<S> {
    return Attribute.EventHandler("blur") { msg }
}

fun <S> onCheck(tagger: (Boolean) -> S): Attribute<S> {
    return Attribute.EventHandler("change") {
        (it.target as? HTMLInputElement)?.let { tagger(it.checked) }
    }
}