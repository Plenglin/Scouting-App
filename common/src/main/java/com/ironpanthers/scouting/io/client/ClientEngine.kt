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

    fun onReceived(data: String) {
        log.trace("received %s", data)

        val obj: Any
        try {
            obj = unmarshal(data)
        } catch (e: Exception) {
            when (e) {
                is NoClassNameException -> log.error("Didn't receive a className!", e)
                is ClassNotFoundException -> log.error("Unable to find class!", e)
            }
            return
        }
        log.trace("created object {}", obj)

        when (obj) {

        }
    }

    fun sendObject(obj: Any) {
        val marshalled = marshal(obj)
        server?.send(marshalled)
    }

    companion object {
        val log = LoggerFactory.getLogger(ClientEngine::class.java)
    }

}
