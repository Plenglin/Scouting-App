package com.ironpanthers.scouting.io.client

import com.ironpanthers.scouting.common.Match
import com.ironpanthers.scouting.common.RobotPerformance


interface ClientStrategy {
    fun getMatchList(cb: (List<Match>) -> Unit)
    fun sendRobotPerformance(rp: RobotPerformance)
}
