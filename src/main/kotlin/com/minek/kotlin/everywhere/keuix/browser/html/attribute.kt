package com.minek.kotlin.everywhere.keuix.browser.html


fun <S> class_(class_: String): Attribute<S> {
    return Attribute.TextProperty("className", class_)
}

fun <S> disabled(disabled: Boolean): Attribute<S> {
    return Attribute.BooleanProperty("disabled", disabled)
}


fun <S> value(value: String): Attribute<S> {
    return Attribute.TextProperty("value", value)
}


fun <S> style(style: String): Attribute<S> {
    return Attribute.TextProperty("style", style)
}

fun <S> dynamic(name: String, value :String): Attribute<S> {
    return Attribute.TextProperty(name, value)
}