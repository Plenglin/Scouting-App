package com.ironpanthers.scouting.io.server

object ServerEngine {

    private val clients = mutableListOf<Client>()

    fun attachClient(client: Client) {
        clients.add(client)
    }

}