package com.ironpanthers.scouting.io.client


interface CommunicationStrategy {
    var client: ClientEngine?

    fun send(obj: Any)
}