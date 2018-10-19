package com.ironpanthers.scouting.io.client

interface ServerCommStrategy {
    var client: ClientEngine?

    fun send(data: String)
}