package com.walker.services.ws

import com.coach.walker.services.interfaces.IOWService
import com.coach.walker.ws.WServiceGenerator
import rx.Observable
import rx.Observer
import rx.Subscription
import rx.concurrency.Schedulers
import rx.subscriptions.Subscriptions

/**
 * Created by pierre-alexandrevezinet on 20/02/2019.
 *
 */
class WServiceRX {

    var baseUrl: String = ""
    var owServiceGenerator: WServiceGenerator

    /**
     * @param baseUrl
     */
    constructor(baseUrl: String) {
        this.baseUrl = baseUrl
        owServiceGenerator = WServiceGenerator(baseUrl)
    }

    //****************************************************************************************//
    //*****************************WALKER INTERFACE ROOTS IMPLEMENTATION*******************************//
    //****************************************************************************************//

    fun getWeather(appId : String, cityName: String): Observable<Any> {

        val service = owServiceGenerator.createServiceLessToken(IOWService::class.java)

        return Observable.create(object : Observable.OnSubscribeFunc<Any> {
            override fun onSubscribe(observer: Observer<in Any>?): Subscription {
                try {
                    val city = service.getWeather(cityName, appId)
                    observer!!.onNext(city)
                    observer.onCompleted()
                } catch (e: Exception) {
                    observer!!.onError(e)
                }
                return Subscriptions.empty()
            }
        }).subscribeOn(Schedulers.threadPoolForIO())
    }
}


