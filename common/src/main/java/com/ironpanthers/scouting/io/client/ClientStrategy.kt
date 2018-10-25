package com.ironpanthers.scouting.io.client

import com.ironpanthers.scouting.common.Competition
import com.ironpanthers.scouting.common.MatchRobot


interface ClientStrategy {
    fun getCompetitionDescription(cb: (Competition) -> Unit)
    fun sendRobotPerformance(rp: MatchRobot)
}
