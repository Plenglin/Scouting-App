package com.ironpanthers.scouting.desktop.io.util

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.intel.bluetooth.MicroeditionConnector
import com.ironpanthers.scouting.BLUETOOTH_MAIN_UUID_RAW
import com.ironpanthers.scouting.BLUETOOTH_NAME
import org.slf4j.LoggerFactory
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.LinkedBlockingDeque
import javax.microedition.io.Connector
import javax.microedition.io.StreamConnection
import javax.microedition.io.StreamConnectionNotifier
import kotlin.concurrent.thread


typealias DataListener = (JsonNode?) -> Unit

class BluetoothServer : AutoCloseable {

    internal val msgQueue = LinkedBlockingDeque<JsonNode>()

    private val logger = LoggerFactory.getLogger(javaClass)
    private val connectionAcceptorThread: Thread = thread(start = false, isDaemon = true) {
        val url = "btspp://localhost:$BLUETOOTH_MAIN_UUID_RAW;name=$BLUETOOTH_NAME"
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
                if (obj.get("dest").isTextual) {
                    val data: JsonNode? = obj.get("data")
                    val dest = obj["dest"].asText()
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
    private val shutdownHook = thread(start = false) {
        logger.info("Shutdown hook reached, closing")
        close()
    }

    fun start() {
        Runtime.getRuntime().addShutdownHook(shutdownHook)
        connectionAcceptorThread.start()
        msgProcessingThread.start()
    }

    fun endpoint(endpoint: String, listener: DataListener) {
        endpoints[endpoint] = listener
    }

    override fun close() {
        connectionAcceptorThread.interrupt()
        msgProcessingThread.interrupt()
        listenerThreads.forEach { it.close() }
        if (Thread.currentThread() != shutdownHook) {
            Runtime.getRuntime().removeShutdownHook(shutdownHook)
        }
    }

    fun broadcast(endpoint: String, data: JsonNode) {
        listenerThreads.forEach {
            it.send(endpoint, data)
        }
    }

}

private class Client(val parent: BluetoothServer, val conn: StreamConnection) : AutoCloseable {
    val rx = conn.openInputStream().bufferedReader()
    val tx = conn.openOutputStream().bufferedWriter()

    private val logger = LoggerFactory.getLogger(javaClass)
    private val mapper = jacksonObjectMapper()

    val thread = kotlin.concurrent.thread(isDaemon = true) {
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

    fun send(endpoint: String, data: JsonNode) {
        val obj = JsonNodeFactory.instance.objectNode()
        obj["dest"] = JsonNodeFactory.instance.textNode(endpoint)
        obj["data"] = data
    }

    override fun close() {
        thread.interrupt()
        conn.close()
    }

}