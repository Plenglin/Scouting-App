package com.ironpanthers.scouting.io.match.server

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.ironpanthers.scouting.io.match.MSG_HANDSHAKE
import com.ironpanthers.scouting.io.match.MSG_MATCH_BEGIN
import com.ironpanthers.scouting.io.match.Message
import org.slf4j.LoggerFactory
import java.util.*
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ConcurrentHashMap
import kotlin.concurrent.thread

class MatchServerEngine : AutoCloseable, ClientInputListener {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val id = UUID.randomUUID()

    private val clientsMap = ConcurrentHashMap<UUID, ClientInterface>()
    private val msgQueue = ArrayBlockingQueue<Message>(8)
    private val mapper = jacksonObjectMapper()

    private var running = false
    private lateinit var msgProcessingThread: Thread

    fun start() {
        logger.info("Server engine initializing with UUID {}", id)
        running = true
        msgProcessingThread = thread(isDaemon = false) {
            logger.info("{} thread started", id)
            while (true) {
                try {
                    val msg = msgQueue.take()
                    logger.debug("Took message {}", msg)
                    processMessage(msg)
                } catch (e: InterruptedException) {
                    if (!running) {
                        break
                    }
                    logger.warn("{} is running but the thread got an interrupt! Continuing.", id)
                } catch (e: Exception) {
                    logger.error("Error while processing message!", e)
                }
            }
            logger.info("{} listener shutting down", id)
        }
    }

    private fun processMessage(msg: Message) {
        when (msg.type) {
            MSG_HANDSHAKE -> throw IllegalArgumentException("Should not have received a $MSG_HANDSHAKE")
        }
    }

    fun addClient(client: ClientInterface) {
        client.start()
        logger.info("Adding client with UUID ${client.id}: $client")
        client.attachClientInputListener(this)
        clientsMap[client.id] = client
    }

    fun broadcast(msg: Message) {
        val data = mapper.writeValueAsString(msg)
        logger.debug("Broadcasting {}", data)
        clientsMap.forEach { _, c ->
            c.send(data)
        }
    }

    fun broadcastBegin() {
        broadcast(Message(id, MSG_MATCH_BEGIN))
    }

    val clients get() = clientsMap.values.toList()

    override fun close() {
        running = false
        msgProcessingThread.interrupt()
    }

    override fun onReceivedFromClient(client: ClientInterface, data: String) {
        logger.debug("Received data from {}: {}", client, data)
        try {
            val parsed = mapper.readValue<Message>(data)
            logger.trace("Parsed message to {}", parsed)

            if (!client.isHandshakeCompleted && parsed.type == MSG_HANDSHAKE) {
                client.id = parsed.sender
                logger.debug("{} sent a handshake", client.id)
            } else {
                msgQueue.put(parsed)
            }
        } catch (e: JsonParseException) {
            logger.error("Parsing JSON failed!", e)
        } catch (e: JsonMappingException) {
            logger.error("Malformed JSON!", e)
        }
    }

    override fun onClientDisconnected(client: ClientInterface) {
        clientsMap.remove(client.id) ?: throw IllegalStateException("Client ${client.id} was not attached to this server!")
        logger.debug("{} disconnected", client.id)
    }

}