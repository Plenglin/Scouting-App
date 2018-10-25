package com.ironpanthers.scouting.desktop.io.client

import com.ironpanthers.scouting.common.Competition
import com.ironpanthers.scouting.common.MatchRobot
import com.ironpanthers.scouting.desktop.io.server.LocalClient
import com.ironpanthers.scouting.io.client.ClientStrategy
import com.ironpanthers.scouting.io.server.ServerEngine
import com.ironpanthers.scouting.io.server.UpdateRobotPerformance
import org.slf4j.LoggerFactory

class LocalStrategy : ClientStrategy {
    override fun sendRobotPerformance(rp: MatchRobot) {
        serverEngine.scheduleAction(UpdateRobotPerformance(rp))
    }

    override fun getCompetitionDescription(cb: (Competition) -> Unit) {
        serverEngine.dbBackend.getCompetitionDescription(serverEngine.compId, cb)
    }

    lateinit var boundServer: LocalClient
    lateinit var serverEngine: ServerEngine

    private val log = LoggerFactory.getLogger(javaClass)

}