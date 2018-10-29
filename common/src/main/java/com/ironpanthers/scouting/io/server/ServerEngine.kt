package com.ironpanthers.scouting.io.server

import com.ironpanthers.scouting.common.MatchRobot
import org.slf4j.LoggerFactory
import java.util.concurrent.LinkedBlockingQueue
import kotlin.concurrent.thread

object ServerEngine {

    private val clients = mutableListOf<BaseClient>()
    private val queue: LinkedBlockingQueue<ServerAction> = LinkedBlockingQueue()

    private val log = LoggerFactory.getLogger(javaClass)
    private lateinit var thread: Thread

    lateinit var dbBackend: DatabaseBackend
    var compId: Int = -1

    fun start() {
        thread = thread(isDaemon = true) {
            try {
                log.info("Server thread starting")

                while (true) {
                    val action = queue.take()
                    log.debug("Processing action {}", action)
                    when (action) {
                        is UpdateRobotPerformance -> {
                            dbBackend.updateRobotPerformance(action.rp)
                        }
                    }
                }
            } catch (e: InterruptedException) {
                log.info("Received interrupt, server thread stopping")
            } finally {
                dbBackend.close()
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

data class UpdateRobotPerformance(val rp: MatchRobot) : ServerAction()
