package com.ironpanthers.scouting.common

import com.ironpanthers.scouting.util.KeyCombo

abstract class GameDef(val name: String, val id: String, val version: Int) {
    val events: MutableList<RobotEventDef> = mutableListOf()
    val endStates: MutableList<RobotEndState> = mutableListOf()

    fun addEventDef(id: String, name: String, stage: Int, keyCombo: KeyCombo, maxTimes: Int = -1, icon: String? = null) {
        events.add(RobotEventDef(createId(id), name, stage, keyCombo, maxTimes, icon))
    }

    private fun createId(base: String): String = "${this.id}:$version:$base"

    fun addEndState(id: String, name: String, icon: String? = null) {
        endStates.add(RobotEndState(createId(id), name, icon))
    }

}
