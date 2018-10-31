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

    //lateinit var dbBackend: DatabaseBackend
    var compId: Int = -1
        private set

    fun start(id: Int) {
        compId = id
        thread = thread(isDaemon = true) {
            try {
                log.info("Server thread starting")

                while (true) {
                    val action = queue.take()
                    log.debug("Processing action {}", action)
                    when (action) {
                        is StopServer -> {
                            log.info("Received request to stop server thread, halting")
                            return@thread
                        }
                        is UpdateRobotPerformance -> {
                            //dbBackend.updateRobotPerformance(action.rp)
                        }
                    }
                }
            } catch (e: InterruptedException) {
                log.warn("Received interrupt, server thread stopping")
            } finally {
                //dbBackend.close()
            }
        }
    }

    fun stop() {
        log.info("Sent request to stop server")
        scheduleAction(StopServer)
    }

    fun scheduleAction(action: ServerAction) {
        log.debug("Adding {} to queue", action)
        queue.put(action)
    }

    fun attachClient(client: BaseClient) {
        clients.add(client)
        client.serverEngine = this
        client.begin()
    }

}

sealed class ServerAction
object StopServer : ServerAction()
data class UpdateRobotPerformance(val rp: MatchRobot) : ServerAction()
