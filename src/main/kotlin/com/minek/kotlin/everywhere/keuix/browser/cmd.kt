package com.minek.kotlin.everywhere.keuix.browser

import com.minek.kotlin.everywhere.keduct.bluebird.Bluebird
import org.w3c.dom.HTMLInputElement
import kotlin.browser.document
import kotlin.browser.window

@Suppress("unused")
sealed class Cmd<out S> {
    companion object {
        fun <S> wrap(commander: () -> Bluebird<S>): Cmd<S> {
            return Promised(commander)
        }

        fun <S : Any> value(msg: S): Cmd<S> {
            return Promised { Bluebird.resolve(msg) }
        }

        fun <S, T> map(cmd: Cmd<S>?, tagger: (S) -> T): Cmd<T>? {
            return when (cmd) {
                is Promised -> wrap { cmd.body().then(tagger) as Bluebird }
                is UiProcessor -> UiProcessor(cmd.body)
                null -> null
            }
        }

        fun <S> focus(elementId: String): Cmd<S> {
            return UiProcessor {
                (document.getElementById(elementId) as? HTMLInputElement)?.focus()
            }
        }

        fun <S> alert(message: String): Cmd<S> {
            return UiProcessor {
                window.alert(message)
            }
        }

        fun <S : Any> alerted(message: String, msg: S): Cmd<S> {
            return wrap {
                Bluebird
                        .resolve(message)
                        .then {
                            window.alert(message)
                            msg
                        }
            }
        }
    }

    internal class Promised<out S>(internal val body: () -> Bluebird<S>) : Cmd<S>()
    internal class UiProcessor<out S>(internal val body: () -> Unit) : Cmd<S>()
}