package com.ironpanthers.scouting.desktop.io.matchmaster.server

import com.ironpanthers.scouting.BLUETOOTH_MAIN_UUID_RAW
import com.ironpanthers.scouting.desktop.io.util.BluetoothServer

class MatchMaster {

    val server = BluetoothServer(BLUETOOTH_MAIN_UUID_RAW)
    init {
        server.endpoint("") {

        }
    }

    fun start() {
        server.start()
    }
}