package com.minek.kotlin.everywhere.keuix.browser.html


fun <S> class_(class_: String): Attribute<S> {
    return Attribute.TextProperty("class", class_)
}

fun <S> attribute(name: String, value :String): Attribute<S> {
    return Attribute.TextProperty(name, value)
}

fun <S> href(url :String): Attribute<S> {
    return Attribute.TextProperty("href", url)
}

fun <S> src(url :String): Attribute<S> {
    return Attribute.TextProperty("src", url)
}

fun <S> disabled(disabled: Boolean): Attribute<S> {
    return Attribute.BooleanProperty("disabled", disabled)
}

fun <S> autofocus(autofocus: Boolean): Attribute<S> {
    return Attribute.BooleanProperty("autofocus", autofocus)
}

fun <S> value(value: String): Attribute<S> {
    return Attribute.TextProperty("value", value)
}


fun <S> style(style: String): Attribute<S> {
    return Attribute.TextProperty("style", style)
}

fun <S> placeholder(placeholder: String): Attribute<S> {
    return Attribute.TextProperty("placeholder", placeholder)
}
