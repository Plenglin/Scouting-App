package com.ironpanthers.scouting.desktop.io.matchmaster.client

import com.intel.bluetooth.MicroeditionConnector
import java.io.InputStream
import javax.microedition.io.StreamConnection
import javax.microedition.io.StreamConnectionNotifier
import kotlin.concurrent.thread

class MatchSlave(val conn: StreamConnection) {

    val rx = conn.openInputStream().bufferedReader()
    val tx = conn.openOutputStream().bufferedWriter()

    val listener = thread(start = true, isDaemon = true) {
        while (true) {
            val read = rx.readLine()
        }
    }

}