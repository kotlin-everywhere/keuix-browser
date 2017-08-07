package com.minek.kotlin.everywhere

import com.minek.kotlin.everywhere.keduct.qunit.asyncTest
import com.minek.kotlin.everywhere.keduct.qunit.fixture
import com.minek.kotlin.everywhere.keuix.browser.Cmd
import com.minek.kotlin.everywhere.keuix.browser.Html
import com.minek.kotlin.everywhere.keuix.browser.runProgram
import org.w3c.dom.Element


private fun prepareFixture(): Pair<Element, () -> dynamic> {
    val fixture = q(fixture())
    val container = q("<div>").appendTo(fixture)[0] as Element
    val root = { fixture.children().first() }
    return container to root
}

internal fun <M, S> asyncSerialTest(init: M, update: (S, M) -> Pair<M, Cmd<S>>, view: (M) -> Html<S>, vararg tests: (root: () -> dynamic) -> Unit) {
    val (container, root) = prepareFixture()
    val lefts = tests.toMutableList()
    return asyncTest { resolve, _ ->
        runProgram(container, init, update, view) {
            val test = lefts[0]
            lefts.removeAt(0)
            test(root)

            if (lefts.isEmpty()) {
                resolve(Unit)
            }
        }
    }
}
