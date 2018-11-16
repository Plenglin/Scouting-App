package com.ironpanthers.scouting.io.match.server

import java.util.*

typealias HandshakeCompletedListener = (ClientInterface) -> Unit

interface BeforeHandshakeClientInterface {
    fun initiateHandshake(listener: HandshakeCompletedListener)
}

abstract class ClientInterface : AutoCloseable {

    abstract val displayName: String
    abstract val id: UUID

    protected var listener: ClientConnectionListener? = null

    fun attachClientInputListener(listener: ClientConnectionListener) {
        this.listener = listener
    }

    abstract fun send(msg: String)

    val clientInfo by lazy { ClientInfo(id, displayName) }

}

interface ClientConnectionListener {

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
