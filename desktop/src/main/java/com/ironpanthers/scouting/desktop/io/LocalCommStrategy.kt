package com.ironpanthers.scouting.desktop.io

import com.ironpanthers.scouting.io.client.ClientEngine
import com.ironpanthers.scouting.io.client.ServerCommStrategy

class LocalStrategy : ServerCommStrategy {
    override var client: ClientEngine? = null

    override fun send(data: String) {

    }

}