package com.ironpanthers.scouting.desktop.io.util

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.BufferedWriter
import java.lang.IllegalArgumentException
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.LinkedBlockingDeque
import kotlin.concurrent.thread

typealias EndpointListener = (JsonNode?) -> JsonNode?

class JsonTransferProtocolServer : AutoCloseable {

    internal val msgQueue = LinkedBlockingDeque<WrappedMessage>()

    private val logger = LoggerFactory.getLogger(javaClass)

    private val msgProcessingThread: Thread = thread(start = false, isDaemon = true) {
        try {
            while (true) {
                processMessage(msgQueue.take())
            }
        } catch (e: InterruptedException) {
            logger.info("Message processing thread interrupted, stopping")
        }
    }

    private val clients = CopyOnWriteArrayList<Client>()
    private val postEndpoints: MutableMap<String, EndpointListener> = mutableMapOf()
    private val getEndpoints: MutableMap<String, EndpointListener> = mutableMapOf()

    private val shutdownHook = thread(start = false) {
        logger.info("Shutdown hook reached, closing")
        close()
    }

    fun start() {
        Runtime.getRuntime().addShutdownHook(shutdownHook)
        msgProcessingThread.start()
    }

    fun post(endpoint: String, listener: EndpointListener) {
        postEndpoints[endpoint] = listener
    }

    fun get(endpoint: String, listener: EndpointListener) {
        getEndpoints[endpoint] = listener
    }

    override fun close() {
        msgProcessingThread.interrupt()
        clients.forEach { it.close() }
        if (Thread.currentThread() != shutdownHook) {
            Runtime.getRuntime().removeShutdownHook(shutdownHook)
        }
    }

    fun broadcast(msg: Message) {
        clients.forEach {
            it.send(msg)
        }
    }

    fun addClient(rx: BufferedReader, tx: BufferedWriter) {
        clients.add(Client(this, rx, tx))
    }

    private fun processMessage(wrapped: WrappedMessage) {
        val (client, msg) = wrapped
        logger.debug("received {}", msg)
        val endpointMap = when (msg.method) {
            POST -> postEndpoints
            GET -> getEndpoints
            RESPONSE -> throw IllegalArgumentException("Server should not receive RESPONSE!")
            else -> throw IllegalArgumentException("${msg.method} not a valid method!")
        }

        val response = endpointMap[msg.dest]?.invoke(msg.data)
        client.send(Message(RESPONSE, "/", msg.uuid, response))
    }
}

val RESPONSE = "RESPONSE"
val POST = "POST"
val GET = "GET"

data class Message(val method: String, val dest: String, val uuid: UUID = UUID.randomUUID(), val data: JsonNode? = null)

internal data class WrappedMessage(val from: Client, val obj: Message)

class Client(val parent: JsonTransferProtocolServer, val rx: BufferedReader, val tx: BufferedWriter) : AutoCloseable {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val mapper = jacksonObjectMapper()

    private val thread = kotlin.concurrent.thread(isDaemon = true) {
        try {
            while (true) {
                val line = rx.readLine()
                logger.debug("received line {}", line)
                try {
                    val msg = mapper.readValue<Message>(line)
                    if (msg.method != RESPONSE) {
                        parent.msgQueue.add(WrappedMessage(this, msg))
                    }
                } catch (e: JsonParseException) {
                    logger.error("Exception while parsing JSON", e)
                } catch (e: JsonMappingException) {
                    logger.error("Malformed data", e)
                }
            }
        } catch (e: InterruptedException) {
            logger.info("listening thread interrupted, stopping")
        }
    }

    fun send(msg: Message) {
        tx.write(mapper.writeValueAsString(msg))
    }

    override fun close() {
        thread.interrupt()
    }

}