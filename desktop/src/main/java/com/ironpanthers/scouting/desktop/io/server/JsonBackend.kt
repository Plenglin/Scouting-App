package com.ironpanthers.scouting.desktop.io.server

import com.ironpanthers.scouting.common.CompetitionMatchData
import com.ironpanthers.scouting.common.CompetitionSummary
import com.ironpanthers.scouting.common.MatchRobot
import com.ironpanthers.scouting.io.server.DatabaseBackend
import java.io.File
import java.net.URL

class JsonBackend(file: File) : DatabaseBackend {


    override fun listCompetitions(cb: (List<CompetitionSummary>) -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCompetitionDescription(id: Int, cb: (CompetitionMatchData) -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateRobotPerformance(rp: MatchRobot) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun close() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}