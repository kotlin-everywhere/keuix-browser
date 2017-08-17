package com.minek.kotlin.everywhere.keuix.browser

import com.minek.kotlin.everywhere.keduct.bluebird.Bluebird
import com.minek.kotlin.everywhere.keduct.qunit.asyncTest
import com.minek.kotlin.everywhere.kelibs.result.Ok
import com.minek.kotlin.everywhere.keuix.browser.common.TestCrate
import org.junit.Test
import kotlin.test.assertEquals

val testCrate = TestCrate().apply { i(remote = "http://localhost:8000") }

class TestKeuse {
    private fun <T : Cmd<S>, S> T.fetch(): Bluebird<S> {
        @Suppress("UNCHECKED_CAST")
        return (this as Cmd.Closure<S>).body()
    }

    @Test
    fun testSimple() {
        asyncTest(testCrate.add(TestCrate.AddReq(1, 2), { it }).fetch().then {
            assertEquals(Ok(3), it)
        })
    }

    @Test
    fun testNested() {
        asyncTest(testCrate.inner.flip(false, { it }).fetch().then {
            assertEquals(Ok(true), it)
        })
    }
}