package com.ironpanthers.scouting.io.server

import com.ironpanthers.scouting.common.Competition
import com.ironpanthers.scouting.common.CompetitionDescription
import com.ironpanthers.scouting.common.MatchRobot

interface DatabaseBackend {

    fun initialize()

    fun close()

    fun listCompetitions(cb: (List<CompetitionDescription>) -> Unit)

    fun getCompetitionDescription(id: Int, cb: (Competition) -> Unit)

    fun updateRobotPerformance(rp: MatchRobot)

}