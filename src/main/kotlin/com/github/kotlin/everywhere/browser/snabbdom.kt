package com.github.kotlin.everywhere.browser

@JsModule("snabbdom")
private external object SnabbdomJs {
    fun init(modules: Array<dynamic>): dynamic
}

@JsModule("snabbdom/modules/props")
private external object SnabbdomJsModulesProps {
    val default: dynamic
}

@JsModule("snabbdom/modules/eventlisteners")
private external object SnabbdomJsModulesEventlisteners {
    val default: dynamic
}

@JsModule("snabbdom/h")
private external object SnabbdomJsHelper {
    val default: dynamic
}

internal object Snabbdom {
    val h = SnabbdomJsHelper.default
    fun init(onPost: () -> Unit): dynamic {
        return SnabbdomJs.init(arrayOf(
                SnabbdomJsModulesProps.default, SnabbdomJsModulesEventlisteners.default,
                object {
                    val post = onPost
                }
        ))
    }
}
