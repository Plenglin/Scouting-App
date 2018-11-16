package com.ironpanthers.scouting.io.chat

import org.slf4j.LoggerFactory
import java.time.Instant
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.concurrent.PriorityBlockingQueue
import kotlin.collections.HashSet
import kotlin.concurrent.thread

const val UUID_RETENTION_TIME = 10000L

class ChatNode {
    private val peers = ConcurrentLinkedDeque<ChatPeer>()

    // To prevent receiving a message twice
    private val receivedMessageIds = ConcurrentHashMap.newKeySet<UUID>()

    // To prevent UUIDs from clogging up memory
    private val uuidRemovalQueue = PriorityBlockingQueue<ReceivedMessage>()

    private val logger = LoggerFactory.getLogger(javaClass)

    val id: UUID = UUID.randomUUID()
    var name: String = id.toString()

    fun onReceivedMessage(message: Message) {
        if (receivedMessageIds.contains(message.id)) {
            logger.debug("Received a message {} that was already received, dropping", message.id)
            return
        }

        val now = Instant.now()
        expireUUIDs(now)

        uuidRemovalQueue.add(ReceivedMessage(message.id, now.plusMillis(UUID_RETENTION_TIME)))
        receivedMessageIds.add(message.id)
    }

    fun sendMessage(msg: Message) {
        peers.forEach {
            it.send(msg)
        }
    }

    fun expireUUIDs(currentTime: Instant) {
        // Expire UUIDs until we either run out of UUIDs or there are no more to expire
        while (uuidRemovalQueue.peek()?.expiryTime?.isBefore(currentTime) == true) {
            val removed = uuidRemovalQueue.remove()
            logger.trace("Expiring {}", removed)
            receivedMessageIds.remove(removed.id)
        }
    }
}

class ChatPeer(val parent: ChatNode) {

    private val receiver = thread(isDaemon = true) {

    }

    fun send(msg: Message) {

    }
}

private data class ReceivedMessage(val id: UUID, val expiryTime: Instant): Comparable<ReceivedMessage> {
    override fun compareTo(other: ReceivedMessage): Int {
        return expiryTime.compareTo(other.expiryTime)
    }

}

data class Sender(val id: UUID, val name: String)

data class Message(val id: UUID, val sender: Sender, val sent: Instant, val content: String)
