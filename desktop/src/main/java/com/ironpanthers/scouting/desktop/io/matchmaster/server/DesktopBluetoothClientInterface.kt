package com.ironpanthers.scouting.desktop.io.matchmaster.server

import com.ironpanthers.scouting.io.match.server.ClientInterface
import org.slf4j.LoggerFactory
import java.io.PrintWriter
import javax.bluetooth.RemoteDevice
import javax.microedition.io.StreamConnection
import kotlin.concurrent.thread

class DesktopBluetoothClientInterface(val device: RemoteDevice, val conn: StreamConnection) : ClientInterface() {

    override val displayName: String = device.getFriendlyName(true)

    private val logger = LoggerFactory.getLogger(javaClass)

    private val rx = conn.openInputStream().bufferedReader()
    private val tx = PrintWriter(conn.openOutputStream().bufferedWriter())
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

    override fun start() {
        running = true
        streamReadingThread.start()
    }

    override fun send(msg: String) {
        logger.trace("Sending to {}: {}", id, msg)
        tx.println(msg)
    }

    override fun close() {
        running = false
        streamReadingThread.interrupt()
    }
}