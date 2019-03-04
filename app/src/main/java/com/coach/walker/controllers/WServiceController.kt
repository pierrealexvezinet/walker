package com.coach.walker.controllers

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.Log
import android.view.View
import android.widget.Toast
import com.coach.walker.R
import com.coach.walker.events.WEvent
import com.rey.material.widget.ProgressView
import org.greenrobot.eventbus.EventBus

import com.coach.walker.utils.WApplicationConstants
import com.coach.walker.utils.WPrefsManager
import com.coach.walker.ws.utils.WApplication

import com.walker.walker_android_sdk.services.ws.WSDKService
import rx.Observable
import rx.Observer
import rx.Subscription
import rx.android.concurrency.AndroidSchedulers
import rx.concurrency.Schedulers

/**
 * Created by pierre-alexandrevezinet on 14/08/2018.
 */

/**
 * Created by pierre-alexandrevezinet <pax@umanlife.com> on 09/01/2018.
 */
class WServiceController() : Observer<Any> {

    private var ulPrefsManager: WPrefsManager? = null
    private var eventName = ""
    private var progressViewCircular: ProgressView? = null
    private var context: Context? = null
    private var token: String = ""
    private var wService: WSDKService = WSDKService(WApplicationConstants.BASE_URL_WALKER_PROD)
    private val bus = EventBus.getDefault()
    private var listObjectReceived: ArrayList<Any> = ArrayList()

    var mSubscription: Subscription? = null

    constructor(mContext: Context, mULPrefsManager: WPrefsManager, mProgressViewCircular: ProgressView) : this() {
        ulPrefsManager = mULPrefsManager
        context = mContext
        if (mProgressViewCircular != null) {
            progressViewCircular = mProgressViewCircular
            progressViewCircular!!.start()
            progressViewCircular!!.visibility = View.VISIBLE
        }
    }

    constructor(mContext: Context, mULPrefsManager: WPrefsManager) : this() {
        ulPrefsManager = mULPrefsManager
        context = mContext
    }

    /**
     * @param param1
     * @param from1
     * @param wsName
     */
    fun execute(`param1`: Any, wsName: String) {
        eventName = wsName
        if (progressViewCircular != null) {
            progressViewCircular!!.visibility = View.VISIBLE
        }

        token = ulPrefsManager!!.getStringFromPreferences(WApplicationConstants.TOKEN)
        if (WApplication.isNetworkAvailable(context!!)) {
            when (wsName) {
            //GIT HUB
                WApplicationConstants.GET_CITY_WEATHER -> mSubscription = Observable.from(param1 as Array<String>)
                        .mapMany({ s -> wService.getGitHubMember(s as String) })
                        .subscribeOn(Schedulers.threadPoolForIO())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this)
            }
        } else {
            if (progressViewCircular != null) {
                progressViewCircular!!.stop()
                progressViewCircular!!.visibility = View.GONE
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
            /**
             * @param param1
             * *
             * @param param2
             *
             * @param wsName
             */
    fun execute(`param1`: Any, `param2`: Any, wsName: String) {
        eventName = wsName
        if (progressViewCircular != null) {
            progressViewCircular!!.visibility = View.VISIBLE
        }
        token = ulPrefsManager!!.getStringFromPreferences(WApplicationConstants.TOKEN)

        if (WApplication.isNetworkAvailable(context!!)) {
            when (wsName) {

            }
        } else {
            if (progressViewCircular != null) {
                progressViewCircular!!.stop()
                progressViewCircular!!.visibility = View.GONE
            }
            Toast.makeText(context!!, context!!.getString(R.string.NETWORK_connectionErrorMessage), Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * @param param1
     * *
     * @param param2
     * *
     * @param param3
     *
     * @param wsName
     */
    fun execute(`param1`: Any, `param2`: Any, `param3`: Any, wsName: String) {
        eventName = wsName
        if (progressViewCircular != null) {
            progressViewCircular!!.visibility = View.VISIBLE
        }
        token = ulPrefsManager!!.getStringFromPreferences(WApplicationConstants.TOKEN)
        if (WApplication.isNetworkAvailable(context!!)) {
            when (wsName) {
            }
        } else {
            if (progressViewCircular != null) {
                progressViewCircular!!.stop()
                progressViewCircular!!.visibility = View.GONE
            }
            Toast.makeText(context!!, context!!.getString(R.string.NETWORK_connectionErrorMessage), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onError(e: Throwable?) {
        Log.d(WApplicationConstants.LOG_WALKER, e!!.message)
    }

    override fun onCompleted() {
        bus.post(WEvent(listObjectReceived, eventName))
        if (progressViewCircular != null) {
            progressViewCircular!!.stop()
            progressViewCircular!!.visibility = View.GONE
        }
    }

    override fun onNext(response: Any?) {
        listObjectReceived.add(response!!)
    }
}
