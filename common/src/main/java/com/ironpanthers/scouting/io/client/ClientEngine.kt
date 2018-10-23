package com.ironpanthers.scouting.io.client

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.databind.ObjectMapper
import com.ironpanthers.scouting.io.shared.NoClassNameException
import com.ironpanthers.scouting.io.shared.marshal
import com.ironpanthers.scouting.io.shared.unmarshal
import org.slf4j.LoggerFactory
import java.lang.Exception

class ClientEngine {

    var server: CommunicationStrategy? = null
    private val jsonFactory = JsonFactory()
    private val mapper = ObjectMapper()

    fun onReceived(obj: Any) {
        when (obj) {

        }
    }

    fun onDisconnected() {

    }

    fun sendObject(obj: Any) {
        val marshalled = marshal(obj)
        server?.send(marshalled)
    }

    companion object {
        val log = LoggerFactory.getLogger(ClientEngine::class.java)
    }

}
