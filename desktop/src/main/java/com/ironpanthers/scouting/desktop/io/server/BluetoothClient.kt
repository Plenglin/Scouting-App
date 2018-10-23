package com.ironpanthers.scouting.desktop.io.server

import com.ironpanthers.scouting.io.server.Client
import com.ironpanthers.scouting.io.shared.marshal
import java.io.BufferedReader
import java.io.BufferedWriter

class BluetoothClient : Client() {
    override val displayName: String = ""

    private lateinit var rx: BufferedReader
    private lateinit var tx: BufferedWriter

    override fun begin() {

    }

    override val connected: Boolean get() {
        return false
    }

    override fun sendData(obj: Any) {
        tx.write(marshal(obj))
    }

}