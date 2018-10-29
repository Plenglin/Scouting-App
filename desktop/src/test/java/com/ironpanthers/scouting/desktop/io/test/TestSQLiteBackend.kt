package com.ironpanthers.scouting.desktop.io.test

import com.ironpanthers.scouting.common.CompetitionDescription
import com.ironpanthers.scouting.desktop.SqlScriptRunner
import com.ironpanthers.scouting.desktop.io.server.SQLiteBackend
import org.apache.log4j.PropertyConfigurator
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.InputStreamReader
import java.util.concurrent.CountDownLatch

class TestSQLiteBackend {

    lateinit var db: SQLiteBackend

    @Before
    fun setUp() {
        val cl = javaClass.classLoader
        PropertyConfigurator.configure(cl.getResource("log4j-test.properties"))
        db = SQLiteBackend("jdbc:sqlite::memory:")

        val runner = SqlScriptRunner(db.conn, false)
        runner.runScript(InputStreamReader(cl.getResourceAsStream("sql/insert_test.sql")))
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun listCompetitions() {
        val latch = CountDownLatch(1)
        db.listCompetitions {
            Assert.assertEquals(
                    CompetitionDescription(1, "test competition", java.sql.Date.valueOf("2018-10-24"), "2018-power-up", 4),
                    it[0]
            )
            latch.countDown()
        }
        latch.await()
    }

    @Test
    fun compMatchList() {
        val latch = CountDownLatch(1)
        db.getCompetitionDescription(1) {
            Assert.assertEquals(it.id, 1)

            Assert.assertTrue(it.matches[0].red.contains(1))
            Assert.assertTrue(it.matches[2].blue.contains(60))
            Assert.assertEquals(it.gameDef, "2018-power-up")

            latch.countDown()
        }
        latch.await()
    }
}