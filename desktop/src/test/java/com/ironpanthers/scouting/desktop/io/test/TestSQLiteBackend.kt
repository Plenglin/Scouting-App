package com.ironpanthers.scouting.desktop.io.test

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

    lateinit var backend: SQLiteBackend

    @Before
    fun setUp() {
        val cl = javaClass.classLoader
        PropertyConfigurator.configure(cl.getResource("log4j-test.properties"))
        backend = SQLiteBackend("jdbc:sqlite::memory:")
        backend.initialize()

        val runner = SqlScriptRunner(backend.conn, false)
        runner.runScript(InputStreamReader(cl.getResourceAsStream("sql/insert_test.sql")))
    }

    @After
    fun tearDown() {
        backend.close()
    }

    @Test
    fun testCompDesc() {
        val latch = CountDownLatch(2)
        backend.getCompetitionDescription(1) {
            Assert.assertEquals(it.id, 1)

            Assert.assertTrue(it.matches[0].red.contains(1))
            Assert.assertTrue(it.matches[2].blue.contains(60))
            Assert.assertEquals(it.gameDef, "2018-power-up")

            latch.countDown()
            latch.await()
        }
        latch.countDown()
        latch.await()
    }
}