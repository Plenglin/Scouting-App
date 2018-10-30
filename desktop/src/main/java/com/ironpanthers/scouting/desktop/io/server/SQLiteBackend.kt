package com.ironpanthers.scouting.desktop.io.server

import com.ironpanthers.scouting.common.*
import com.ironpanthers.scouting.desktop.util.ioExecutor
import com.ironpanthers.scouting.io.server.DatabaseBackend
import org.slf4j.LoggerFactory
import java.sql.Connection
import java.sql.DriverManager

class SQLiteBackend(url: String) : DatabaseBackend {

    val conn: Connection

    private val log = LoggerFactory.getLogger(javaClass)

    init {
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

    override fun listCompetitions(cb: (List<CompetitionSummary>) -> Unit) {
        ioExecutor.execute {
            val results = conn.prepareStatement(STM_LIST_COMP_DESC).executeQuery()
            val out = mutableListOf<CompetitionSummary>()
            while (results.next()) {
                val id = results.getInt(1)
                val name = results.getString(2)
                val date = results.getDate(3)
                val gameDef = results.getString(4)

                log.trace("listCompetitions: id={} name={} date={} gameDef={}", id, name, date, gameDef)

                out.add(CompetitionSummary(id, name, date, gameDef, 0))
            }
            cb(out)
        }
    }

    override fun getCompetitionDescription(id: Int, cb: (CompetitionMatchData) -> Unit) {
        ioExecutor.execute {
            val st = conn.prepareStatement(STM_GET_COMP_INFO)
            st.setInt(1, id)
            st.setInt(2, id)
            //st.setInt(3, id)

            val results = st.executeQuery()
            results.next()
            val date = results.getDate(1)
            val gameDef = results.getString(2)
            val count = results.getInt(3)

            log.debug("getCompetitionDescription: date={} gameDef={} count={}", date, gameDef, count)

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
                        MatchSummary(id, desc.number, desc.alliances["RED"]!!, desc.alliances["BLUE"]!!)
                    }
                    .sortedBy { it.number }
            st.close()
            cb(CompetitionMatchData(id, date, gameDef, matches))
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
