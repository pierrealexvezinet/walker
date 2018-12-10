package com.coach.walker.ws.services

import com.coach.walker.ws.WServiceGenerator
import com.coach.walker.ws.interfaces.IWalkerServices
import rx.concurrency.Schedulers
import rx.subscriptions.Subscriptions
import com.coach.walker.ws.models.GitHubMember
import org.json.JSONArray
import org.json.JSONObject
import rx.Observable
import rx.Subscription
import rx.Observer

/**
 * Created by pierre-alexandrevezinet on 14/08/2018.
 */

class WalkerServices {

    var jsonObject: JSONObject? = null
    var jsonData: String? = null
    var jsonArray: JSONArray? = null
    var baseUrl: String = ""
    var WServiceGenerator: WServiceGenerator

    /**
     * @param ULSDKServiceCallback
     *
     * @param
     */
    constructor(baseUrl: String) {
        this.baseUrl = baseUrl
        WServiceGenerator = WServiceGenerator(baseUrl)
    }

    fun getGitHubMember(username: String): Observable<GitHubMember> {

        val service = WServiceGenerator.createServiceLessToken(IWalkerServices::class.java)

        return Observable.create(object : Observable.OnSubscribeFunc<GitHubMember> {
            override fun onSubscribe(observer: Observer<in GitHubMember>): Subscription {
                try {
                    val member = service.getMember(username)
                    observer.onNext(member)
                    observer.onCompleted()
                } catch (e: Exception) {
                    observer.onError(e)
                }

                return Subscriptions.empty()
            }
        }).subscribeOn(Schedulers.threadPoolForIO())
    }

}