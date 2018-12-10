package com.coach.walker.events

/**
 * Created by pierre-alexandrevezinet on 09/01/2018.
 */

class WEvent {

    var wEvent: Any
    var eventName: String

    constructor(wEvent: Any, eventName: String) {
        this.wEvent = wEvent
        this.eventName = eventName
    }
}