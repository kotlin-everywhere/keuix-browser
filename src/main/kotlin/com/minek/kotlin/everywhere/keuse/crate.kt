package com.minek.kotlin.everywhere.keuse

import com.minek.kotlin.everywehre.keuson.convert.Converter
import com.minek.kotlin.everywehre.keuson.convert.decoder
import com.minek.kotlin.everywehre.keuson.convert.encoder
import com.minek.kotlin.everywehre.keuson.decode.Decoder
import com.minek.kotlin.everywehre.keuson.encode.Encoder
import com.minek.kotlin.everywehre.keuson.encode.encode
import com.minek.kotlin.everywhere.keduct.bluebird.Bluebird
import com.minek.kotlin.everywhere.kelibs.result.Err
import com.minek.kotlin.everywhere.kelibs.result.Result
import com.minek.kotlin.everywhere.keuix.browser.Cmd
import kotlin.browser.window
import kotlin.js.Promise
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

private external fun fetch(url: String, options: dynamic): Promise<Any?>

abstract class Crate {
    internal var remote: String = "http://localhost:5000"
    internal val url: String
        get() = remote

    fun <P, R> e(parameterConvert: Converter<P>, resultConverter: Converter<R>): EndPoint.Delegate<P, R> {
        return EndPoint.Delegate(this, parameterConvert, resultConverter)
    }

    fun i(remote: String = "http://localhost:5000") {
        if (remote.endsWith("/")) {
            throw IllegalArgumentException("remote should not ends with /")
        }
        this.remote = remote
    }
}

class EndPoint<in P, out R>(private val crate: Crate, private val name: String, parameterConvert: Converter<P>, resultConverter: Converter<R>) {
    private val parameterEncoder: Encoder<P> = parameterConvert.encoder
    private val resultDecoder: Decoder<R> = resultConverter.decoder
    private val url: String
        get() = crate.url + '/' + name

    @Suppress("UnsafeCastFromDynamic")
    operator fun <S : Any> invoke(parameter: P, tagger: (Result<String, R>) -> S): Cmd<S> {
        val opts = js("new Object")
        opts.method = "POST"
        opts.body = encode(parameterEncoder(parameter))
        if (!url.startsWith(window.location.origin)) {
            opts.mode = "cors"
        }
        return Cmd.wrap {
            Bluebird.resolve(fetch(url, opts))
                    .then { it.asDynamic().json() }
                    .then(resultDecoder)
                    .catch { Err<String, R>(js("'' + it") as String) }
                    .then(tagger)
        }
    }

    class Delegate<in P, out R>(private val crate: Crate, private val parameterConvert: Converter<P>, private val resultConverter: Converter<R>) : ReadOnlyProperty<Crate, EndPoint<P, R>> {
        private var endpoint: EndPoint<P, R>? = null

        override fun getValue(thisRef: Crate, property: KProperty<*>): EndPoint<P, R> {
            return endpoint ?: EndPoint(crate, property.name, parameterConvert, resultConverter)
        }
    }
}