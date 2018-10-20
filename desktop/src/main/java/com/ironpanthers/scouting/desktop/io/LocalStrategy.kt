package com.ironpanthers.scouting.desktop.io

import com.ironpanthers.scouting.io.client.ClientEngine
import com.ironpanthers.scouting.io.client.CommunicationStrategy

class LocalStrategy : CommunicationStrategy {
    lateinit var boundServer: LocalClient
    override var client: ClientEngine? = null

    override fun send(data: String) {
        boundServer.onDataReceived(data)
    }

    fun onDataReceived(data: String) {
        client?.onReceived(data)
    }

}