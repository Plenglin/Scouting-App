package com.ironpanthers.scouting.desktop.io.server

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.intel.bluetooth.MicroeditionConnector
import com.ironpanthers.scouting.BLUETOOTH_NAME
import com.ironpanthers.scouting.BLUETOOTH_SERVER_UUID_RAW
import org.slf4j.LoggerFactory
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.LinkedBlockingDeque
import javax.bluetooth.DeviceClass
import javax.bluetooth.DiscoveryListener
import javax.bluetooth.RemoteDevice
import javax.bluetooth.ServiceRecord
import javax.microedition.io.Connector
import javax.microedition.io.StreamConnection
import javax.microedition.io.StreamConnectionNotifier
import kotlin.concurrent.thread


typealias DataListener = (JsonNode?) -> Unit

object BluetoothServer : DiscoveryListener {

    internal val msgQueue = LinkedBlockingDeque<JsonNode>()

    private val logger = LoggerFactory.getLogger(javaClass)
    private val connectionAcceptorThread: Thread = thread(start = false, isDaemon = true) {
        val url = "btspp://localhost:$BLUETOOTH_SERVER_UUID_RAW;name=$BLUETOOTH_NAME"
        val c = MicroeditionConnector.open(url, Connector.READ_WRITE, false) as StreamConnectionNotifier
        logger.info("Waiting for client connections on $url")
        try {
            while (true) {
                val sc = c.acceptAndOpen()
                val client = Client(this, sc)
                logger.info("Client connected: {}", client)
                listenerThreads += client
            }
        } catch (e: InterruptedException) {
            logger.info("Connection accepting thread interrupted, stopping")
        }
    }
    private val msgProcessingThread: Thread = thread(start = false, isDaemon = true) {
        try {
            while (true) {
                val obj = msgQueue.take()
                logger.debug("received {}", obj)
                if (obj.get("destination").isTextual) {
                    val data: JsonNode? = obj.get("data")
                    val dest = obj["destination"].asText()
                    endpoints[dest]?.invoke(data)
                } else {
                    logger.error("Invalid header key 'destination'! Ignoring message {}", obj)
                }
            }
        } catch (e: InterruptedException) {
            logger.info("Message processing thread interrupted, stopping")
        }
    }

    private val listenerThreads = CopyOnWriteArrayList<Client>()
    private val endpoints: MutableMap<String, DataListener> = mutableMapOf()

    init {
        Runtime.getRuntime().addShutdownHook(thread(start = false) {
            logger.info("Shutdown hook reached, closing")
            close()
        })
    }

    fun start() {
        connectionAcceptorThread.start()
        msgProcessingThread.start()
    }

    fun endpoint(endpoint: String, listener: DataListener) {
        endpoints[endpoint] = listener
    }

    fun close() {
        connectionAcceptorThread.interrupt()
        msgProcessingThread.interrupt()
        listenerThreads.forEach { it.close() }
    }

    override fun serviceSearchCompleted(p0: Int, p1: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deviceDiscovered(p0: RemoteDevice?, p1: DeviceClass?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun servicesDiscovered(p0: Int, p1: Array<out ServiceRecord>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun inquiryCompleted(p0: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}

private class Client(val parent: BluetoothServer, val conn: StreamConnection) : AutoCloseable {
    val rx = conn.openInputStream().bufferedReader()
    val tx = conn.openOutputStream().bufferedWriter()

    private val logger = LoggerFactory.getLogger(javaClass)

    val thread = kotlin.concurrent.thread(isDaemon = true) {
        val mapper = jacksonObjectMapper()
        try {
            while (true) {
                val line = rx.readLine()
                logger.debug("{} received line {}", conn, line)
                try {
                    val json = mapper.readTree(line)
                    parent.msgQueue.add(json)
                } catch (e: JsonParseException) {
                    logger.error("Exception while parsing JSON", e)
                }
            }
        } catch (e: InterruptedException) {
            logger.info("{} listening thread interrupted, stopping", conn)
        }
    }

    override fun close() {
        thread.interrupt()
        conn.close()
    }

}