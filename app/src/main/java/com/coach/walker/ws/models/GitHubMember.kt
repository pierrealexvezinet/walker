package com.coach.walker.ws.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by pierre-alexandrevezinet on 14/08/2018.
 */

class GitHubMember : Serializable {

    /**
     *  Return the login device passed to the constructor.
     */
    @SerializedName("login")
    @Expose
    val login: String

    /**
     *  Return the followers passed to the constructor.
     */
    @SerializedName("followers")
    @Expose
    val followers: Int

    constructor(login: String, followers: Int) {
        this.login = login
        this.followers = followers
    }
}