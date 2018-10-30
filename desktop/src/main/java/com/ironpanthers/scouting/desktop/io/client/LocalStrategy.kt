package com.ironpanthers.scouting.desktop.io.client

import com.ironpanthers.scouting.common.CompetitionMatchData
import com.ironpanthers.scouting.common.MatchRobot
import com.ironpanthers.scouting.desktop.io.server.LocalClient
import com.ironpanthers.scouting.io.client.ClientStrategy
import com.ironpanthers.scouting.io.server.ServerEngine
import com.ironpanthers.scouting.io.server.UpdateRobotPerformance
import org.slf4j.LoggerFactory

class LocalStrategy(private val serverEngine: ServerEngine) : ClientStrategy {

    private val boundServer: LocalClient = LocalClient()

    init {
        boundServer.boundClient = this
        serverEngine.attachClient(boundServer)
    }

    override fun sendRobotPerformance(rp: MatchRobot) {
        log.debug("Posting MR {}", rp)
        serverEngine.scheduleAction(UpdateRobotPerformance(rp))
    }

    override fun getCompetitionDescription(cb: (CompetitionMatchData) -> Unit) {
        log.debug("Retrieving competition descriptions")
        serverEngine.dbBackend.getCompetitionDescription(serverEngine.compId, cb)
    }

    private val log = LoggerFactory.getLogger(javaClass)

}