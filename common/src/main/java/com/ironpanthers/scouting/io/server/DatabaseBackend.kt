package com.ironpanthers.scouting.io.server

import com.ironpanthers.scouting.common.CompetitionMatchData
import com.ironpanthers.scouting.common.CompetitionSummary
import com.ironpanthers.scouting.common.MatchRobot

interface DatabaseBackend : AutoCloseable {

    fun listCompetitions(cb: (List<CompetitionSummary>) -> Unit)

    fun getCompetitionDescription(id: Int, cb: (CompetitionMatchData) -> Unit)

    fun updateRobotPerformance(rp: MatchRobot)

}