package com.ironpanthers.scouting.desktop.io.matchmaster.server

import com.ironpanthers.scouting.desktop.io.util.JsonTransferProtocolServer

class MatchMaster {

    private val server = JsonTransferProtocolServer()

    init {
        server.post("") {
            null
        }
    }

    fun start() {
        server.start()
    }
}