package com.walker.ws.services

import android.text.TextUtils
import com.coach.walker.utils.WSAuthenticationInterceptor
import com.coach.walker.utils.WSynchronousCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import com.google.gson.GsonBuilder
import java.util.concurrent.TimeUnit

/**
 * Created by pierre-alexandrevezinet on 20/02/2019.
 *
 */

class OWServiceGenerator(baseUrl: String) {

    var retrofit: Retrofit? = null
    var gson = GsonBuilder()
            .setLenient()
            .create()

    private val httpClient = OkHttpClient.Builder()
    private val builder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(WSynchronousCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))

    /**

     * @param serviceClass
     * *
     * @param <S>
     * *
     * @return
    </S> */
    fun <S> createServiceLessToken(serviceClass: Class<S>): S {
        var retrofit = builder.build()

        val interceptor = WSAuthenticationInterceptor()

        if (!httpClient.interceptors().contains(interceptor)) {
            httpClient.addInterceptor(interceptor)
            httpClient.readTimeout(10, TimeUnit.SECONDS)
            httpClient.connectTimeout(10, TimeUnit.SECONDS)
            httpClient.writeTimeout(10, TimeUnit.SECONDS)
            builder.client(httpClient.build())
            retrofit = builder.build()
        }
        return retrofit.create(serviceClass)
    }

    /**
     * @param serviceClass
     * *
     * @param authToken
     * *
     * @param <S>
     * *
     * @return
    </S> */
    fun <S> createService(
            serviceClass: Class<S>, authToken: String?): S {
        if (!TextUtils.isEmpty(authToken)) {
            val interceptor = WSAuthenticationInterceptor(authToken!!)
            if (!httpClient.interceptors().contains(interceptor)) {
                httpClient.addInterceptor(interceptor)
                httpClient.readTimeout(10, TimeUnit.SECONDS)
                httpClient.connectTimeout(10, TimeUnit.SECONDS)
                httpClient.writeTimeout(10, TimeUnit.SECONDS)
                builder.client(httpClient.build())
                retrofit = builder.build()
            }
        }

        return builder.build().create(serviceClass)
    }

    /**
     * @param serviceClass
     * *
     * @param authToken
     * *
     * @param <S>
     * *
     * @return
    </S> */
    fun <S> createServiceGetUserContents(
            serviceClass: Class<S>, authToken: String?): S {
        if (!TextUtils.isEmpty(authToken)) {
            val interceptor = WSAuthenticationInterceptor(authToken!!)
            if (!httpClient.interceptors().contains(interceptor)) {
                httpClient.addInterceptor(interceptor)
                httpClient.readTimeout(60, TimeUnit.SECONDS)
                httpClient.connectTimeout(60, TimeUnit.SECONDS)
                httpClient.writeTimeout(60, TimeUnit.SECONDS)
                builder.client(httpClient.build())
                retrofit = builder.build()
            }
        }

        return builder.build().create(serviceClass)
    }

}