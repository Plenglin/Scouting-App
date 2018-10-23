package com.ironpanthers.scouting.desktop.io.client

import com.ironpanthers.scouting.desktop.io.server.LocalClient
import com.ironpanthers.scouting.io.client.ClientEngine
import com.ironpanthers.scouting.io.client.CommunicationStrategy
import org.slf4j.LoggerFactory

class LocalStrategy : CommunicationStrategy {
    lateinit var boundServer: LocalClient
    override var client: ClientEngine? = null

    private val log = LoggerFactory.getLogger(javaClass)

    override fun send(obj: Any) {
        log.trace("Sending data %s", obj)
        boundServer.onDataReceived(obj)
    }

}