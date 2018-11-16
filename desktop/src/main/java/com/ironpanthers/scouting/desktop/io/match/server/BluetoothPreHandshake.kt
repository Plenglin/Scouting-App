package com.ironpanthers.scouting.desktop.io.match.server

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.ironpanthers.scouting.io.match.MSG_HANDSHAKE
import com.ironpanthers.scouting.io.match.Message
import com.ironpanthers.scouting.io.match.server.BeforeHandshakeClientInterface
import com.ironpanthers.scouting.io.match.server.HandshakeCompletedListener
import org.slf4j.LoggerFactory
import java.io.PrintWriter
import java.util.*
import javax.microedition.io.StreamConnection
import kotlin.concurrent.thread


class BluetoothPreHandshake(val serverId: UUID, val conn: StreamConnection) : BeforeHandshakeClientInterface {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun initiateHandshake(listener: HandshakeCompletedListener) {
        thread {
            logger.debug("Performing handshake with client with server ID {}", serverId)

            val rx = conn.openInputStream().bufferedReader()
            val tx = PrintWriter(conn.openOutputStream().bufferedWriter())
            val mapper = jacksonObjectMapper()

            val out = mapper.writeValueAsString(Message(serverId, MSG_HANDSHAKE))
            logger.debug("Sending to client: {}", out)

            tx.println(out)

            val data = rx.readLine()
            logger.trace("Received handshake data {}", data)

            val msg = mapper.readValue<Message>(data)

            logger.debug("Successfully parsed data: {}", msg)

            listener(BluetoothClientInterface(msg.id, rx, tx, conn))
        }
    }

}