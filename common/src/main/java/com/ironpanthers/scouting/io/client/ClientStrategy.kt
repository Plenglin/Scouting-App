package com.ironpanthers.scouting.io.client

import com.ironpanthers.scouting.common.CompetitionDescription
import com.ironpanthers.scouting.common.MatchRobot


interface ClientStrategy {
    fun getCompetitionDescription(cb: (CompetitionDescription) -> Unit)
    fun sendRobotPerformance(rp: MatchRobot)
}
