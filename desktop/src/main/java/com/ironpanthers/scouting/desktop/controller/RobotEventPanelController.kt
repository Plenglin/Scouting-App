package com.ironpanthers.scouting.desktop.controller

import com.ironpanthers.scouting.common.RobotEvent

interface RobotEventPanelController {
    /**
     * This should get called whenever a [RobotEvent] occurs (i.e. someone presses the "vault success" button)
     */
    var onEventOccurred: (RobotEvent) -> Unit

    /**
     * The team number. It has to be settable.
     */
    var teamNumber: Int
}