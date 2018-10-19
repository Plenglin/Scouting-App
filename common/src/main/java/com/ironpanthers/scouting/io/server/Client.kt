package com.ironpanthers.scouting.io.server

import java.util.*

abstract class Client {

    val id = UUID.randomUUID()
    abstract val displayName: String

    var listener: ClientListener? = null

    abstract fun begin()
    abstract fun sendData(obj: String)

    fun getInfo() = ClientInfo(id, displayName)

}

interface ClientListener {

    /**
     * Called when the client sends over an object.
     */
    fun onReceivedFromClient(client: Client, obj: Any)

    /**
     * Called when the client socket is closed, either intentionally or not.
     */
    fun onClientDisconnected(client: Client)

}

data class ClientInfo(val id: UUID, val displayName: String)
