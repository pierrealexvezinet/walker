package com.coach.walker.ws

import android.text.TextUtils
import com.coach.walker.ws.utils.WAuthenticationInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import com.google.gson.GsonBuilder
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by pierre-alexandrevezinet on 14/08/2018.
 */





/**
 * Created by pierre-alexandrevezinet on 19/06/2017.
 */

class WServiceGenerator(baseUrl: String) {

    var retrofit: Retrofit? = null
    var gson = GsonBuilder()
            //.registerTypeAdapter(Date.class, new DateDeserializer())
            .setLenient()
            .create()

    private val httpClient = OkHttpClient.Builder()
    private val builder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
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

        val interceptor = WAuthenticationInterceptor()

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
            val interceptor =WAuthenticationInterceptor(authToken!!)
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
            val interceptor = WAuthenticationInterceptor(authToken!!)
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

    /**
     * @param serviceClass
     * *
     * @param authToken
     *
     * @param accessTokenWhiteLabeling
     * *
     * @param <S>
     * *
     * @return
    </S> */
    fun <S> createService(
            serviceClass: Class<S>, authToken: String?, accessTokenWhiteLabeling: String?): S {
        if (!TextUtils.isEmpty(authToken) || !TextUtils.isEmpty(accessTokenWhiteLabeling)) {
            val interceptor = WAuthenticationInterceptor(authToken!!, accessTokenWhiteLabeling!!)
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

}