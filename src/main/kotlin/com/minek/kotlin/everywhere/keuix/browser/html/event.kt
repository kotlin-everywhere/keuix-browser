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
