package com.minek.kotlin.everywhere.keuix.browser

import com.minek.kotlin.everywhere.keduct.bluebird.Bluebird

@Suppress("unused")
sealed class Cmd<out S> {
    companion object {
        fun <S> wrap(commander: () -> Bluebird<S>): Cmd<S> {
            return Closure(commander)
        }

        fun <S : Any> value(msg: S): Cmd<S> {
            return Closure { Bluebird.Companion.resolve(msg) }
        }

        fun <S, T> map(cmd: Cmd<S>?, tagger: (S) -> T): Cmd<T>? {
            return when (cmd) {
                is Closure -> wrap { cmd.body().then(tagger) as Bluebird }
                null -> null
            }
        }
    }

    internal class Closure<out S>(internal val body: () -> Bluebird<S>) : Cmd<S>()
}