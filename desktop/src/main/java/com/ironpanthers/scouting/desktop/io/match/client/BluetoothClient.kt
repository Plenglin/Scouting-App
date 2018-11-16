package com.ironpanthers.scouting.desktop.io.match.client

import com.ironpanthers.scouting.io.match.client.MatchClient
import org.slf4j.LoggerFactory
import javax.bluetooth.ServiceRecord
import javax.microedition.io.Connector
import javax.microedition.io.StreamConnection

object BluetoothClient : AutoCloseable {
    private var client: MatchClient? = null

    private val logger = LoggerFactory.getLogger(javaClass)

    fun connectTo(serviceRecord: ServiceRecord) {
        val url = serviceRecord.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false)
        logger.info("Connecting to {}", url)
        val conn = Connector.open(url) as StreamConnection

        client = MatchClient(conn.openInputStream(), conn.openDataOutputStream()).also {
            it.start()
        }
    }

    override fun close() {
        client?.close()
    }

}