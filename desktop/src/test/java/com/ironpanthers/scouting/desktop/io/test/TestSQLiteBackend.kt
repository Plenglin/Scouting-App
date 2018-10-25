package com.ironpanthers.scouting.desktop.io.test

import com.ironpanthers.scouting.desktop.SqlScriptRunner
import com.ironpanthers.scouting.desktop.io.server.SQLiteBackend
import org.apache.log4j.PropertyConfigurator
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.FileReader
import java.io.InputStreamReader

class TestSQLiteBackend {

    lateinit var backend: SQLiteBackend

    @Before
    fun setup() {
        val cl = javaClass.classLoader
        PropertyConfigurator.configure(cl.getResource("log4j-test.properties"))
        backend = SQLiteBackend("jdbc:sqlite::memory:")
        backend.initialize()

        println("asdf")
        val runner = SqlScriptRunner(backend.conn, false)
        runner.runScript(InputStreamReader(cl.getResourceAsStream("sql/insert_test.sql")))
    }

    @After
    fun teardown() {
        backend.close()
    }

    @Test
    fun testCompDesc() {
        backend.getCompetitionDescription(1) {
            Assert.assertEquals(it.id, 1)

            Assert.assertEquals(it.matches[0].red[0], 1)
            Assert.assertEquals(it.matches[2].blue[2], 60)
            Assert.assertEquals(it.gameDef, "2018-power-up")
        }

    }
}