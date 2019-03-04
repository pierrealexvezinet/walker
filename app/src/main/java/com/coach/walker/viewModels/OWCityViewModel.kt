package com.coach.walker.viewmodels

import android.arch.lifecycle.ViewModel
import android.content.Context
import android.util.Log
import com.coach.walker.events.WEvent
import com.coach.walker.utils.WApplicationConstants
import com.rey.material.widget.ProgressView
import com.walker.services.ws.WServiceRX
import rx.Observable
import rx.Observer
import rx.Subscription
import rx.android.concurrency.AndroidSchedulers
import rx.concurrency.Schedulers
import org.greenrobot.eventbus.EventBus

/**
 * Created by pierre-alexandrevezinet on 20/02/2019.
 *
 */

class OWCityViewModel : ViewModel {

    private var mSubscription: Subscription? = null
    private var mContext: Context? = null
    private var wService: WServiceRX = WServiceRX(WApplicationConstants.BASE_URL_WALKER_PROD)
    private var mProgress: ProgressView? = null
    private val bus = EventBus.getDefault()
    private var city: Any? = null
    private var listCity: ArrayList<Any> = ArrayList()
    private var listNCity: ArrayList<Any> = ArrayList()

    constructor() : super() {
    }

    constructor(context: Context, progressView: ProgressView) : super() {
        mContext = context
        mProgress = progressView
    }

    fun getListCityWeather(pListCity: Array<String>, appId: String) {
        listCity = ArrayList()
        mSubscription = Observable.from(pListCity)
                .mapMany({ s -> wService.getWeather(appId, s) })
                .subscribeOn(Schedulers.threadPoolForIO())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Any> {
                    override fun onError(e: Throwable?) {
                        Log.d(WApplicationConstants.LOG_WALKER, e!!.message)
                        bus.post(WEvent(WApplicationConstants.ERROR_WALKER, WApplicationConstants.ERROR_WALKER))
                    }

                    override fun onNext(response: Any?) {
                        if (response != null) {
                            city = response
                            listCity.add(city!!)
                        }
                    }

                    override fun onCompleted() {
                        bus.post(WEvent(listCity, WApplicationConstants.GET_CITY_WEATHER))
                        listCity.clear()
                    }

                })

    }

    fun getNCityWeather(city1: String, city2: String, appId: String) {
        listNCity = ArrayList()
        mSubscription = Observable
                .merge(wService.getWeather(appId, city1), wService.getWeather(appId, city2))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Any> {
                    override fun onNext(args: Any?) {
                        listNCity.add(args!!)
                    }

                    override fun onError(e: Throwable?) {
                        Log.d(WApplicationConstants.LOG_WALKER, e!!.message)
                        bus.post(WEvent(WApplicationConstants.ERROR_WALKER, WApplicationConstants.ERROR_WALKER))
                    }

                    override fun onCompleted() {
                        bus.post(WEvent(listNCity, WApplicationConstants.GET_N_CITY_WEATHER))
                        listNCity.clear()
                    }
                })
    }

}