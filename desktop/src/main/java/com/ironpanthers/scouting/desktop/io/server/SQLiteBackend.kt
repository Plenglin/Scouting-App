package com.ironpanthers.scouting.desktop.io.server

import com.ironpanthers.scouting.common.*
import com.ironpanthers.scouting.desktop.ioExecutor
import com.ironpanthers.scouting.io.server.DatabaseBackend
import org.apache.log4j.PropertyConfigurator
import org.slf4j.LoggerFactory
import tornadofx.multi
import java.io.File
import java.sql.Connection
import java.sql.DriverManager

class SQLiteBackend(private val url: String) : DatabaseBackend {

    lateinit var conn: Connection
        private set

    private val log = LoggerFactory.getLogger(javaClass)

    override fun initialize() {
        log.info("Initializing connection to {}", url)
        conn = DriverManager.getConnection(url)

        log.debug("creating tables")
        STM_INITIALIZE.forEach {
            log.debug("execute: {}", it)
            conn.createStatement().execute(it)
        }
    }

    override fun close() {
        log.info("Closing")
        conn.close()
    }

    override fun listCompetitions(cb: (List<CompetitionDescription>) -> Unit) {
        ioExecutor.execute {
            val results = conn.prepareStatement(STM_LIST_COMP_DESC).executeQuery()
            val out = mutableListOf<CompetitionDescription>()
            while (results.next()) {
                val id = results.getInt(1)
                val name = results.getString(2)
                val date = results.getDate(3)
                val gameDef = results.getString(4)

                log.trace("listCompetitions: id={} name={} date={} gameDef={}", id, name, date, gameDef)

                out.add(CompetitionDescription(id, name, date, gameDef))
            }
            cb(out)
        }
    }

    override fun getCompetitionDescription(id: Int, cb: (Competition) -> Unit) {
        ioExecutor.execute {
            val st = conn.prepareStatement(STM_GET_COMP_INFO)
            st.setInt(1, id)
            st.setInt(2, id)

            log.debug("getCompetitionDescription execute: {}", STM_GET_COMP_INFO)

            val results = st.executeQuery()
            results.next()
            val date = results.getDate(1)
            val gameDef = results.getString(2)

            log.debug("getCompetitionDescription: date={} gameDef={}", date, gameDef)

            val matchMap = mutableMapOf<Int, TempMatchDesc>()

            while (results.next()) {
                val matchId = results.getInt(1)
                val matchNum = results.getInt(2)
                val color = results.getString(3)
                val team = results.getInt(4)

                log.trace("getCompetitionDescription: matchId={} matchNum={} color={} team={}", matchId, matchNum, color, team)

                val desc = matchMap.getOrPut(matchId) { TempMatchDesc(matchNum) }
                desc.alliances[color]!!.add(team)
            }

            log.debug("getCompetitionDescription: matchMap={}", matchMap)

            val matches = matchMap
                    .map { (id, desc) ->
                        Match(id, desc.number, desc.alliances["RED"]!!, desc.alliances["BLUE"]!!)
                    }
                    .sortedBy { it.number }
            st.close()
            cb(Competition(id, date, gameDef, matches))
        }
    }

    override fun updateRobotPerformance(rp: MatchRobot) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}

private class TempMatchDesc(val number: Int) {
    val alliances: Map<String, MutableList<Int>> = mutableMapOf(
            "RED" to mutableListOf(),
            "BLUE" to mutableListOf()
    )
}

fun main(args: Array<String>) {
    PropertyConfigurator.configure("log4j.properties")
    val path = File(".", "test.sqlite3").canonicalPath
    val b = SQLiteBackend("jdbc:sqlite:$path")
    b.initialize()

}