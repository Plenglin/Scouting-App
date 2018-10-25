package com.ironpanthers.scouting.desktop.io.client

import com.ironpanthers.scouting.common.Match
import com.ironpanthers.scouting.common.RobotPerformance
import com.ironpanthers.scouting.desktop.io.server.LocalClient
import com.ironpanthers.scouting.io.client.ClientStrategy
import com.ironpanthers.scouting.io.server.ServerEngine
import org.slf4j.LoggerFactory

class LocalStrategy : ClientStrategy {
    override fun sendRobotPerformance(rp: RobotPerformance) {

    }

    override fun getMatchList(cb: (List<Match>) -> Unit) {
        cb(serverEngine.matchList)
    }

    lateinit var boundServer: LocalClient
    lateinit var serverEngine: ServerEngine

    private val log = LoggerFactory.getLogger(javaClass)

}