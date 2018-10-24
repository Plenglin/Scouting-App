package com.ironpanthers.scouting.io.server

import java.util.*

abstract class BaseClient {

    val id = UUID.randomUUID()!!
    var serverEngine: ServerEngine? = null

    abstract val displayName: String
    abstract val connected: Boolean
    abstract val type: String

    abstract fun begin()
    abstract fun sendData(obj: Any)

    val info by lazy { ClientInfo(id, displayName) }

}

data class ClientInfo(val id: UUID, val displayName: String)
