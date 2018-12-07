package com.coach.walker.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.coach.walker.R
import com.coach.walker.ws.WObserverCallback
import com.coach.walker.ws.WServiceGenerator
import rx.Observer
import rx.Subscription
import rx.android.concurrency.AndroidSchedulers
import rx.concurrency.Schedulers
import com.coach.walker.ws.models.GitHubMember
import com.coach.walker.ws.services.WalkerServices
import com.coach.walker.ws.utils.WLibraryConstants
import okhttp3.ResponseBody
import retrofit2.Call
import rx.Observable
import rx.util.functions.Func1
import rx.operators.OperationMap.mapMany
import rx.operators.OperationSubscribeOn.subscribeOn
import rx.operators.OperationObserveOn.observeOn

/**
 * Created by pierre-alexandrevezinet on 10/08/2018.
 */

class WMainActivity : AppCompatActivity(), Observer<Any> {

    var mSubscription: Subscription? = null
    private val GITHUB_MEMBERS = arrayOf("mojombo", "JakeWharton", "mattt")
    var service: WalkerServices? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.w_main_activity)
        service = WalkerServices(WLibraryConstants.BASE_URL_GITHUB)

        mSubscription = Observable.from(GITHUB_MEMBERS)
                .mapMany({s -> service!!.getGitHubMember(s) })
                .subscribeOn(Schedulers.threadPoolForIO())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this)
    }

    override fun onNext(args: Any?) {
        Toast.makeText(this, args.toString(), Toast.LENGTH_LONG).show()
    }

    override fun onError(e: Throwable?) {
        Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
    }

    override fun onCompleted() {
        Toast.makeText(this, "OK OK OK", Toast.LENGTH_LONG).show()
    }

}
