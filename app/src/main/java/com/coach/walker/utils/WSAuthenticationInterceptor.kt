package com.coach.walker.utils

import okhttp3.Interceptor
import java.io.IOException

/**
 * Created by pierre-alexandrevezinet on 20/02/2019.
 *
 */

class WSAuthenticationInterceptor : Interceptor {

    private var authToken: String? = null
    private var accessToken: String? = null

    constructor(token: String) {
        this.authToken = token
    }

    constructor () {
        this.accessToken = ""
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val original = chain.request()
        val builder = original.newBuilder()

        builder.header("content-type", "application/json")
                .header("accept-language", "fr")
                .header("cache-control", "no-cache")
                .header("access-token", accessToken)

        val request = builder.build()
        return chain.proceed(request)
    }

}