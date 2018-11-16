package com.ironpanthers.scouting.desktop.io.match.server

import com.ironpanthers.scouting.BLUETOOTH_MAIN_UUID_RAW
import com.ironpanthers.scouting.io.match.server.MatchServerEngine
import org.slf4j.LoggerFactory
import javax.bluetooth.DiscoveryAgent
import javax.bluetooth.LocalDevice
import javax.microedition.io.Connector
import javax.microedition.io.StreamConnectionNotifier
import kotlin.concurrent.thread

object BluetoothMatchServer {

    private var server = MatchServerEngine()
    private val logger = LoggerFactory.getLogger(javaClass)

    private lateinit var acceptorThread: Thread
    private var running = false

    fun start() {
        logger.info("Starting BMS")
        if (running) {
            throw IllegalStateException("Server already running!")
        }

        server.start()
        acceptorThread = thread(isDaemon = true, start = false) {
            logger.debug("listener thread starting")
            val local = LocalDevice.getLocalDevice()
            local.discoverable = DiscoveryAgent.GIAC
            val url = "btspp://localhost:$BLUETOOTH_MAIN_UUID_RAW;name=BluetoothMatchServer"
            val notifier = Connector.open(url) as StreamConnectionNotifier

            while (true) {
                val conn = notifier.acceptAndOpen()
                logger.info("received connection {}", conn)
                server.addClient(DesktopBluetoothClientInterface(conn))
            }
        }
        acceptorThread.start()
    }

    fun stop() {
        logger.info("Stopping BMS")
    }

}