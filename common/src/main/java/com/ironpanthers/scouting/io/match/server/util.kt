package com.ironpanthers.scouting.io.match.server

import java.util.*

abstract class ClientInterface : AutoCloseable {

    var isHandshakeCompleted = false
    lateinit var id: UUID
    protected var listener: ClientInputListener? = null

    abstract val displayName: String

    abstract fun start()
    abstract fun send(msg: String)

    fun attachClientInputListener(listener: ClientInputListener) {
        this.listener = listener
    }

    val clientInfo get() = ClientInfo(id, displayName)

}

interface ClientInputListener {

    /**
     * Called when the client sends over an object.
     */
    fun onReceivedFromClient(client: ClientInterface, data: String)

    /**
     * Called when the client socket is closed, either intentionally or not.
     */
    fun onClientDisconnected(client: ClientInterface)

}

data class ClientInfo(val id: UUID, val displayName: String)
