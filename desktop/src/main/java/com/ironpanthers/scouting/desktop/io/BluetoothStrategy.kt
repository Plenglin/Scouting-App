package com.ironpanthers.scouting.desktop.io

import com.ironpanthers.scouting.io.client.ClientEngine
import com.ironpanthers.scouting.io.client.CommunicationStrategy

class BluetoothStrategy : CommunicationStrategy {
    override var client: ClientEngine? = null

    override fun send(data: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}