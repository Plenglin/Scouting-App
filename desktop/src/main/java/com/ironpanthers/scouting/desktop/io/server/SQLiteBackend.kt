package com.ironpanthers.scouting.desktop.io.server

import com.ironpanthers.scouting.common.*
import com.ironpanthers.scouting.desktop.ioExecutor
import com.ironpanthers.scouting.io.server.DatabaseBackend
import org.apache.log4j.PropertyConfigurator
import org.slf4j.LoggerFactory
import java.io.File
import java.sql.Connection
import java.sql.Date
import java.sql.DriverManager
import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit

class SQLiteBackend(private val url: String) : DatabaseBackend {

    private lateinit var conn: Connection
    private val log = LoggerFactory.getLogger(javaClass)

    override fun initialize() {
        log.info("Initializing connection to {}", url)
        conn = DriverManager.getConnection(url)
    }

    override fun close() {
        log.info("Closing")
        conn.close()
    }

    override fun getCompetitionDescription(id: Int, cb: (CompetitionDescription) -> Unit) {
        val compDescCallable = Callable<Any> {
            val stm = conn.prepareStatement(STM_GET_COMP_DESC)
            stm.setInt(1, id)

            log.debug("Executing query {}", stm)

            val results = stm.executeQuery()
            results.next()
            val date = results.getDate(1)
            val gameDef = results.getString(2)

            TempCompDesc(id, date, gameDef)
        }
        val matchDescCallable = Callable<Any> {
            val stm = conn.prepareStatement(STM_GET_MATCH_DESC_FROM_COMP)
            stm.setInt(1, id)

            log.debug("Executing query {}", stm)

            val results = stm.executeQuery()
            val matches = mutableMapOf<Int, TempMatchDesc>().withDefault {
                TempMatchDesc()
            }
            while (results.next()) {
                val matchId = results.getInt(1)
                val matchNum = results.getInt(2)
                val color = results.getString(3)
                val team = results.getInt(4)

                val desc = matches[matchId]!!
                desc.number = matchNum
                desc.alliances[color]!!.add(team)
            }
            matches
        }

        val futures = ioExecutor.invokeAll(listOf(compDescCallable, matchDescCallable))
        ioExecutor.execute {
            val tcd = futures[0].get(1, TimeUnit.DAYS) as TempCompDesc
            val tmd = futures[1].get(1, TimeUnit.DAYS) as Map<Int, TempMatchDesc>

            log.debug("Results: {}; {}", tcd, tmd)

            val matches = tmd.map { (id, desc) ->
                MatchDescription(id, desc.number, desc.alliances["red"]!!, desc.alliances["blue"]!!)
            }
            cb(CompetitionDescription(tcd.id, tcd.date, tcd.gameDef, matches))
        }
    }

    override fun updateRobotPerformance(rp: MatchRobot) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}

private class TempMatchDesc {
    var number: Int = 0
    val alliances: Map<String, MutableList<Int>> = mutableMapOf(
            "RED" to mutableListOf(),
            "BLUE" to mutableListOf()
    )
}

private data class TempCompDesc(val id: Int, val date: Date, val gameDef: String)

fun main(args: Array<String>) {
    PropertyConfigurator.configure("log4j.properties")
    val path = File(".", "test.db").canonicalPath
    val b = SQLiteBackend("jdbc:sqlite:$path")
    b.initialize()
    b.getCompetitionDescription(2) {
        println(it)
    }
    b.close()
}