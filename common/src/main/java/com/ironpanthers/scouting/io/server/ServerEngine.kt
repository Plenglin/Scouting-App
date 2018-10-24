package com.ironpanthers.scouting.io.server

import com.ironpanthers.scouting.common.Match

object ServerEngine {

    lateinit var matchList: List<Match>
    private val clients = mutableListOf<BaseClient>()

    fun attachClient(client: BaseClient) {
        clients.add(client)
        client.serverEngine = this
        client.begin()
    }

}