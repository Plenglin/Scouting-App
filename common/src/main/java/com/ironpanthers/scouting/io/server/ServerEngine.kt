package com.ironpanthers.scouting.io.server

import com.ironpanthers.scouting.common.Match
import com.ironpanthers.scouting.common.RobotPerformance
import org.slf4j.LoggerFactory
import java.sql.DriverManager
import java.util.concurrent.LinkedBlockingQueue
import kotlin.concurrent.thread

object ServerEngine {

    lateinit var matchList: List<Match>
    private val clients = mutableListOf<BaseClient>()
    private val queue: LinkedBlockingQueue<ServerAction> = LinkedBlockingQueue()

    private val log = LoggerFactory.getLogger(javaClass)
    private val db = DriverManager.getConnection("jdbc:sqlite:data.db")
    private lateinit var thread: Thread

    var running: Boolean = false
        set(value) {
            if (value) {
                start()
            } else {
                stop()
            }
            field = value
        }

    private fun start() {
        thread = thread(isDaemon = true) {
            try {
                log.info("Server thread starting")
                while (true) {
                    val action = queue.take()
                    log.debug("Processing action {}", action)
                    when (action) {
                        is UpdateRobotPerformance -> {

                        }
                    }
                }
            } catch (e: InterruptedException) {
                log.info("Received interrupt, server thread stopping")
            }
        }
    }

    private fun stop() {
        thread.interrupt()
    }

    fun scheduleAction(action: ServerAction) {
        queue.put(action)
    }

    fun attachClient(client: BaseClient) {
        clients.add(client)
        client.serverEngine = this
        client.begin()
    }

}

sealed class ServerAction

data class UpdateRobotPerformance(val rp: RobotPerformance) : ServerAction()
