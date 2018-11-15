package com.ironpanthers.scouting.io.match.client

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.ironpanthers.scouting.io.match.MSG_HANDSHAKE
import com.ironpanthers.scouting.io.match.Message
import org.slf4j.LoggerFactory
import java.io.InputStream
import java.io.OutputStream
import java.io.PrintWriter
import java.util.*
import kotlin.concurrent.thread

typealias MessageListener = (Message) -> Unit

class MatchClient(input: InputStream, output: OutputStream) : AutoCloseable {
    val id = UUID.randomUUID()!!
    var listener: MessageListener? = null

    private val rx = input.bufferedReader()
    private val tx = PrintWriter(output.bufferedWriter())

    private val mapper = jacksonObjectMapper()
    private val logger = LoggerFactory.getLogger(javaClass)

    private var serverId: UUID? = null

    private var running = true
    private var isHandshakeCompleted = false

    private lateinit var receiver: Thread

    fun start() {
        send(MSG_HANDSHAKE)
        receiver = thread(isDaemon = true) {
            logger.info("{} thread started", id)
            while (true) {
                try {
                    val data = rx.readLine()
                    logger.trace("{} got data: {}", id, data)
                    val deserialized = mapper.readValue<Message>(data)
                    onMessageReceived(deserialized)
                } catch (e: InterruptedException) {
                    if (running) {
                        logger.warn("{} is running but the thread got an interrupt! Continuing.", id)
                    } else {
                        break
                    }
                }
            }
            logger.info("{} listener shutting down", id)
        }
    }

    private fun send(msg: Message) {
        val serialized = mapper.writeValueAsString(msg)
        logger.trace("{} sending: {}", id, serialized)
        tx.println(serialized)
    }

    fun send(type: String, data: Any? = null) {
        send(Message(id, type, data = mapper.valueToTree(data)))
    }
    private fun onMessageReceived(msg: Message) {
        logger.debug("{} got message: {}", id, msg)
        if (!isHandshakeCompleted && msg.type == MSG_HANDSHAKE) {
            logger.info("{} received handshake: {}")
            serverId = msg.sender
        } else {
            listener?.invoke(msg)
        }
    }

    override fun close() {
        running = false
        receiver.interrupt()
    }

}
