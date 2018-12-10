package com.coach.walker.ws.interfaces

import com.coach.walker.ws.models.GitHubMember
import retrofit2.http.GET
import retrofit2.http.Path


/**
 * Created by pierre-alexandrevezinet on 14/08/2018.
 */

interface IWalkerServices{

    @GET("/users/{username}")
    fun getMember(@Path("username") username: String): GitHubMember

}