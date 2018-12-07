package com.coach.walker.ws

import rx.Observer

/**
 * Created by pierre-alexandrevezinet on 14/08/2018.
 */

interface WObserverCallback : Observer<Any> {

    /**
     * Return a response when the service has started.
     */
    fun onServiceCompleted(response: Any)

    /**
     * Return a response when the service has failed.
     */
    fun onServiceFailed(response: Any)

    /**
     * Return a response when the service has succeeded.
     */
    fun onServiceNext(response: Any)
}

