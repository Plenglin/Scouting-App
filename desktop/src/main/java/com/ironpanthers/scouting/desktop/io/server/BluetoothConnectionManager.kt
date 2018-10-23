package com.ironpanthers.scouting.desktop.io.server

import com.ironpanthers.scouting.BLUETOOTH_SERVER_UUID
import javax.bluetooth.DiscoveryAgent
import javax.bluetooth.LocalDevice
import javax.microedition.io.Connector
import javax.obex.HeaderSet
import javax.obex.Operation
import javax.obex.ServerRequestHandler
import javax.obex.SessionNotifier

class BluetoothConnectionManager : ServerRequestHandler() {

    init {
        LocalDevice.getLocalDevice().discoverable = DiscoveryAgent.GIAC

        val connection = Connector.open("btgoep://localhost:$BLUETOOTH_SERVER_UUID;name=ObexExample")
        (connection as SessionNotifier).acceptAndOpen(this)

    }

    override fun onPut(op: Operation): Int {
        op.receivedHeaders.getHeader(HeaderSet.NAME)
        return super.onPut(op)
    }

    override fun onGet(op: Operation): Int {
        return super.onGet(op)
    }
}

fun main(args: Array<String>) {
    BluetoothConnectionManager()
}