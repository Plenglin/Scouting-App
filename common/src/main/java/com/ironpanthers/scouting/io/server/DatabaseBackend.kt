package com.ironpanthers.scouting.io.server

import com.ironpanthers.scouting.common.Competition
import com.ironpanthers.scouting.common.CompetitionDescription
import com.ironpanthers.scouting.common.MatchRobot

interface DatabaseBackend : AutoCloseable {

    fun listCompetitions(cb: (List<CompetitionDescription>) -> Unit)

    fun getCompetitionDescription(id: Int, cb: (Competition) -> Unit)

    fun updateRobotPerformance(rp: MatchRobot)

}