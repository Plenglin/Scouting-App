package com.ironpanthers.scouting.desktop.io

import com.ironpanthers.scouting.io.server.Client

class LocalClient : Client() {
    override val displayName: String = "Local Client"

    override fun begin() {

    }

    override fun sendData(obj: String) {

    }

}