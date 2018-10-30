package com.ironpanthers.scouting.desktop.io.client

import com.ironpanthers.scouting.common.CompetitionMatchData
import com.ironpanthers.scouting.common.MatchRobot
import com.ironpanthers.scouting.io.client.ClientStrategy
import com.ironpanthers.scouting.io.shared.NoClassNameException
import com.ironpanthers.scouting.io.shared.unmarshal
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.BufferedWriter
import kotlin.concurrent.thread

class BluetoothStrategy : ClientStrategy {
    override fun getCompetitionDescription(cb: (CompetitionMatchData) -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun sendRobotPerformance(rp: MatchRobot) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    private val log = LoggerFactory.getLogger(javaClass)

    private lateinit var rx: BufferedReader
    private lateinit var tx: BufferedWriter

    init {
        thread(isDaemon = true) {
            while (true) {
                val data = rx.readLine()
                log.trace("received %s", data)

                val obj: Any
                try {
                    obj = unmarshal(data)
                } catch (e: Exception) {
                    when (e) {
                        is NoClassNameException -> log.error("Didn't receive a className!", e)
                        is ClassNotFoundException -> log.error("Unable to find class!", e)
                    }
                    continue
                }
                log.trace("created object {}", obj)
                //client!!.onReceived(obj)
            }
        }
    }

}