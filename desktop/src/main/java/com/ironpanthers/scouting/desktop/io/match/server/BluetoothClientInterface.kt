package com.ironpanthers.scouting.desktop.io.match.server

import com.ironpanthers.scouting.io.match.server.ClientInterface
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.PrintWriter
import java.util.*
import javax.microedition.io.StreamConnection
import kotlin.concurrent.thread

class BluetoothClientInterface(override val id: UUID, val rx: BufferedReader, val tx: PrintWriter, val conn: StreamConnection) : ClientInterface() {

    override val displayName: String = "asdf"

    private val logger = LoggerFactory.getLogger(javaClass)

    private var running = false

    private val streamReadingThread = thread(start = false, isDaemon = true) {
        while (true) {
            try {
                val received = rx.readLine()
                logger.debug("{} received msg: {}", id, received)
                listener?.onReceivedFromClient(this, received)
            } catch (e: InterruptedException) {
                if (!running) {
                    break
                }
                logger.warn("Interrupted without being stopped!", e)
            }
        }
    }

    init {
        logger.info("{} CliInterface created", id)
        running = true
        streamReadingThread.start()
    }

    override fun send(msg: String) {
        if (!running) {
            throw IllegalStateException("Not running! Can't send message!")
        }
        logger.trace("Sending to {}: {}", id, msg)
        tx.println(msg)
    }

    override fun close() {
        running = false
        streamReadingThread.interrupt()
        rx.close()
        tx.close()
        conn.close()
    }
}