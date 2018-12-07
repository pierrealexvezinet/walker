package com.coach.walker.ws.utils

import android.util.Log
import okhttp3.Interceptor
import java.io.IOException

/**
 * Created by pierre-alexandrevezinet on 14/08/2018.
 */


class WAuthenticationInterceptor : Interceptor {

    private var authToken: String? = null
    private var accessToken: String? = null

    constructor(token: String) {
        this.authToken = token
        Log.d(WLibraryConstants.UL_SDK_LOGGER, "1 Constructor authToken :" + authToken)
    }

    constructor(token: String, accessToken: String) {
        this.authToken = token
        this.accessToken = accessToken
        Log.d(WLibraryConstants.UL_SDK_LOGGER, "2 Constructor authToken :" + this.authToken + " accessToken :  " + this.accessToken)
    }

    constructor () {
        this.authToken = ""
        this.accessToken = ""
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val original = chain.request()

        val builder = original.newBuilder()

        if (!this.authToken!!.isEmpty()) {
            builder.header("content-type", "application/json")
                    .header("Authorization", "Bearer " + authToken)
                    .header("accept-language", "fr")
                    .header("cache-control", "no-cache")
            Log.d(WLibraryConstants.UL_SDK_LOGGER, "1 - Authorization Bearerrrrrrr : " + authToken)

        } else {
            builder.header("content-type", "application/json")
                    .header("accept-language", "fr")
                    .header("cache-control", "no-cache")
                    .header("access-token", accessToken)
            Log.d(WLibraryConstants.UL_SDK_LOGGER, "2 - access-tokennnnnn : " + accessToken)
        }

        val request = builder.build()
        return chain.proceed(request)
    }

}