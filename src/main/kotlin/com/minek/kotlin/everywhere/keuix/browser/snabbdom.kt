package com.minek.kotlin.everywhere.keuix.browser

@JsModule("snabbdom")
private external object SnabbdomJs {
    fun init(modules: Array<dynamic>): dynamic
}

@JsModule("snabbdom/modules/props")
private external object SnabbdomJsModulesProps {
    val default: dynamic
}

@JsModule("snabbdom/modules/attributes")
private external object SnabbdomJsModulesAttributes {
    val default: dynamic
}

@JsModule("snabbdom/modules/eventlisteners")
private external object SnabbdomJsModulesEventListeners {
    val default: dynamic
}

@JsModule("snabbdom/h")
private external object SnabbdomJsHelper {
    val default: dynamic
}

internal object Snabbdom {
    @Suppress("HasPlatformType")
    val h = SnabbdomJsHelper.default

    fun init(onPost: (() -> Unit)?): dynamic {
        val hookModule: dynamic = if (onPost != null) {
            object {
                @Suppress("unused")
                val post = onPost
            }
        } else {
            null
        }

        return SnabbdomJs.init(
                arrayOf(SnabbdomJsModulesProps.default, SnabbdomJsModulesAttributes.default, SnabbdomJsModulesEventListeners.default, hookModule)
                        .filterNotNull()
                        .toTypedArray()
        )
    }
}