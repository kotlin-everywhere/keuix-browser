package com.minek.kotlin.everywhere.keuix.browser

import com.minek.kotlin.everywhere.keduct.bluebird.Bluebird

@Suppress("unused")
sealed class Cmd<out S> {
    companion object {
        fun <S> wrap(commander: () -> Bluebird<S>): Cmd<S> {
            return Closure(commander)
        }
    }

    internal class Closure<out S>(internal val body: () -> Bluebird<S>) : Cmd<S>()
}