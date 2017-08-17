package com.minek.kotlin.everywhere.keuix.browser

import com.minek.kotlin.everywhere.keuix.browser.common.TestCrate
import com.minek.kotlin.everywhere.keuse.runServer
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

class CorsFilter : org.springframework.web.filter.CorsFilter(buildCorsConfigurationSource()) {
    companion object {
        private fun buildCorsConfigurationSource(): CorsConfigurationSource {
            val source = UrlBasedCorsConfigurationSource()
            val config = CorsConfiguration()
            config.addAllowedMethod("POST")
            config.addAllowedOrigin("http://localhost:8080")
            source.registerCorsConfiguration("/**", config)
            return source
        }
    }
}

fun main(args: Array<String>) {
    val testCrate = TestCrate().apply {
        add { (first, second) ->
            first + second
        }
    }

    testCrate.runServer(8000, filters = listOf(CorsFilter()))
}