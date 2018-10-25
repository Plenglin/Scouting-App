package com.ironpanthers.scouting.desktop.io.server

import com.ironpanthers.scouting.io.server.BaseClient
import com.ironpanthers.scouting.io.shared.marshal
import java.io.BufferedReader
import java.io.BufferedWriter

class BluetoothClient : BaseClient() {
    override val displayName: String = ""
    override val type = "Bluetooth"

    private lateinit var rx: BufferedReader
    private lateinit var tx: BufferedWriter

    override fun begin() {

    }

    override val connected: Boolean get() {
        return false
    }

}