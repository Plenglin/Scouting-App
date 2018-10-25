package com.ironpanthers.scouting.io.server

import com.ironpanthers.scouting.common.CompetitionDescription
import com.ironpanthers.scouting.common.MatchRobot

interface DatabaseBackend {

    fun initialize()

    fun close()

    fun getCompetitionDescription(id: Int, cb: (CompetitionDescription) -> Unit)

    fun updateRobotPerformance(rp: MatchRobot)

}