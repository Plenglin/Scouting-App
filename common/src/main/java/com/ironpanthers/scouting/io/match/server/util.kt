package com.ironpanthers.scouting.io.match.server

import com.ironpanthers.scouting.io.match.common.Message
import java.util.*

interface ServerCommStrategy : AutoCloseable {

    var queue: Deque<Message>

    abstract fun onStart(): Boolean
    abstract fun sendObject(obj: Any)
}

interface CommStrategyListener {
    fun onReceivedObject(obj: Any)
    fun onDisconnect()
}
