package com.ironpanthers.scouting.desktop.io.test

import com.ironpanthers.scouting.desktop.util.importTBAData
import org.junit.Test

class TestTBAImport {

    @Test
    fun testTBA() {
        val key = System.getProperty("tba-api-key")
        println(key)
        val dat = importTBAData("2018mxmo", key)
        println(dat)
    }

}