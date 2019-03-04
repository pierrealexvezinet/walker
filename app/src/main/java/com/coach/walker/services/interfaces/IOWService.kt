package com.coach.walker.services.interfaces

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by pierre-alexandrevezinet on 20/02/2019.
 *
 */

interface IOWService {

    //****************************************************************************************//
    //       *****************************OPEN WEATHER MAP*******************************//
    //****************************************************************************************//

    /**
     * Retrieve weather information for a passed city string parameter
     * @return
     */
    @GET("/data/2.5/weather")
    fun getWeather(@Query("q") city: String, @Query("appid") appId: String): Any

}