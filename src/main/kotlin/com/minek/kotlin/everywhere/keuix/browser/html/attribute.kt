package com.minek.kotlin.everywhere.keuix.browser.html


fun <S> class_(class_: String): Attribute<S> {
    return Attribute.TextAttribute("class", class_)
}

fun <S> classes(vararg pairs: Pair<String, Boolean>, class_: String = ""): Attribute<S> {
    val classes = pairs
            .filter { it.second }
            .joinToString(" ") { it.first }
            .let {
                if (class_.isNotEmpty()) "$class_ $it" else it
            }
    return com.minek.kotlin.everywhere.keuix.browser.html.class_(classes)
}

fun <S> id(id: String): Attribute<S> {
    return Attribute.TextProperty("id", id)
}

fun <S> attribute(name: String, value: String): Attribute<S> {
    return Attribute.TextProperty(name, value)
}

fun <S> href(url: String): Attribute<S> {
    return Attribute.TextProperty("href", url)
}

fun <S> src(url: String): Attribute<S> {
    return Attribute.TextProperty("src", url)
}

fun <S> disabled(disabled: Boolean): Attribute<S> {
    return Attribute.BooleanProperty("disabled", disabled)
}

fun <S> checked(checked: Boolean): Attribute<S> {
    return Attribute.BooleanProperty("checked", checked)
}

fun <S> autofocus(autofocus: Boolean): Attribute<S> {
    return Attribute.BooleanAttribute("autofocus", autofocus)
}

fun <S> value(value: String): Attribute<S> {
    return Attribute.TextProperty("value", value)
}

fun <S> type(type: String): Attribute<S> {
    return Attribute.TextProperty("type", type)
}

fun <S> style(style: String): Attribute<S> {
    return Attribute.TextAttribute("style", style)
}

fun <S> placeholder(placeholder: String): Attribute<S> {
    return Attribute.TextProperty("placeholder", placeholder)
}

fun <S> for_(for_: String): Attribute<S> {
    return Attribute.TextAttribute("for", for_)
}

fun <S> key(key: String): Attribute<S> {
    return Attribute.Key(key)
}

fun <S> innerHtml(html: String): Attribute<S> {
    return Attribute.TextProperty("innerHTML", html)
}