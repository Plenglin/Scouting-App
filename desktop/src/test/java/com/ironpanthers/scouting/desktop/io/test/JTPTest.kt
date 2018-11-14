package com.ironpanthers.scouting.desktop.io.test

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.treeToValue
import com.ironpanthers.scouting.desktop.io.util.JsonTransferProtocolServer
import org.apache.log4j.BasicConfigurator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.BeforeClass
import org.junit.Test
import java.io.PipedInputStream
import java.io.PipedOutputStream
import java.util.*
import java.util.concurrent.CountDownLatch

class JTPTest {

    @Test
    fun testJTPServer() {
        val server = JsonTransferProtocolServer()
        val mapper = jacksonObjectMapper()

        val latch = CountDownLatch(1)
        server.get("ep1") {
            assertEquals(mapper.treeToValue<String>(it!!), "test")
            latch.countDown()
            null
        }

        val thisTx = PipedOutputStream()
        val thisRx = PipedInputStream()

        val otherTx = PipedOutputStream()
        val otherRx = PipedInputStream()

        thisTx.connect(otherRx)
        //thisRx.connect(otherTx)

        //server.addClient(otherRx.bufferedReader(), otherTx.bufferedWriter())

        val writer = thisTx.bufferedWriter()
        val reader = otherRx.bufferedReader()
        writer.write("""{"method": "GET", "endpoint": "ep1", "uuid": "${UUID.randomUUID()}", "data": "test"}"""/* + "\n"*/)
        writer.close()
        println("asdf")
        println(reader.readLine())

        latch.await()
    }

    companion object {
        @BeforeClass
        @JvmStatic
        fun setup() {
            BasicConfigurator.configure()
        }
    }

}