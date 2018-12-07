package com.coach.walker.ws.interfaces

import com.coach.walker.ws.models.GitHubMember
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import rx.Observable
import java.util.*

/**
 * Created by pierre-alexandrevezinet on 14/08/2018.
 */

interface IWalkerServices{

    @GET("/users/{username}")
    fun getMember(@Path("username") username: String): Observable<GitHubMember>

}