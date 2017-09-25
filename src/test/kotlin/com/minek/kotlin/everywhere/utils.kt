package com.minek.kotlin.everywhere

import com.minek.kotlin.everywhere.keduct.bluebird.Bluebird
import com.minek.kotlin.everywhere.keduct.qunit.asyncTest
import com.minek.kotlin.everywhere.keduct.qunit.fixture
import com.minek.kotlin.everywhere.keuix.browser.*
import org.w3c.dom.Element


private fun prepareFixture(): Pair<Element, () -> dynamic> {
    val fixture = q(fixture())
    val container = q("<div>").appendTo(fixture)[0] as Element
    val root = { fixture.children().first() }
    return container to root
}

internal fun <M, S> serialTest(init: M, update: Update<M, S>, view: View<M, S>, cmd: Cmd<S>? = null, vararg tests: (root: () -> dynamic) -> dynamic): Bluebird<Unit> {
    var program: Program<M, S>? = null
    return Bluebird<Unit>({ resolve, reject ->
        val (container, root) = prepareFixture()
        val lefts = tests.toMutableList()
        program = runProgram(container, init, update, view, cmd) {
            try {
                if (lefts.isNotEmpty()) {
                    val test = lefts[0]
                    lefts.removeAt(0)
                    test(root)
                }

                if (lefts.isEmpty()) {
                    resolve(Unit)
                }
            } catch (e: dynamic) {
                reject(e)
            }
        }
    }).finally {
        program?.stop()
        q(fixture()).html("")
        Unit
    }
}

internal fun <M, S> asyncSerialTest(init: M, update: Update<M, S>, view: View<M, S>, vararg tests: (root: () -> dynamic) -> dynamic) {
    asyncSerialTest(init, update, view, null as Cmd<S>?, *tests)
}

internal fun <M, S> asyncSerialTest(init: M, update: Update<M, S>, view: View<M, S>, cmd: Cmd<S>?, vararg tests: (root: () -> dynamic) -> dynamic) {
    asyncTest(serialTest(init, update, view, cmd, *tests))
}
