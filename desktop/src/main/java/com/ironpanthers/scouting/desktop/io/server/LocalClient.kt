package com.ironpanthers.scouting.desktop.io.server

import com.ironpanthers.scouting.desktop.io.client.LocalStrategy
import com.ironpanthers.scouting.io.server.BaseClient
import org.slf4j.LoggerFactory

class LocalClient : BaseClient() {
    override val type: String = "Local"
    lateinit var boundClient: LocalStrategy
    override val displayName: String = "Local Client"

    override val connected: Boolean
        get() = true

    private val log = LoggerFactory.getLogger(javaClass)

    override fun begin() {
        log.info("Beginning LocalClient")
    }

}