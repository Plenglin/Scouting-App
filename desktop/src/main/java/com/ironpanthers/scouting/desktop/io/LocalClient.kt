package com.ironpanthers.scouting.desktop.io

import com.ironpanthers.scouting.io.server.Client

class LocalClient : Client() {
    lateinit var boundClient: LocalStrategy
    override val displayName: String = "Local Client"

    override fun begin() {

    }

    override fun sendData(obj: String) {
        boundClient.onDataReceived(obj)
    }

    fun onDataReceived(data: String) {
        listener?.onReceivedFromClient(this, data)
    }

}