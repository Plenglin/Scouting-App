package com.ironpanthers.scouting.io.client

import com.ironpanthers.scouting.common.CompetitionMatchData
import com.ironpanthers.scouting.common.MatchRobot


interface ClientStrategy {
    fun getCompetitionDescription(cb: (CompetitionMatchData) -> Unit)
    fun sendRobotPerformance(rp: MatchRobot)
}
