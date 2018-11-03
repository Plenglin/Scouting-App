package com.ironpanthers.scouting.common

import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.ironpanthers.scouting.util.Color
import com.ironpanthers.scouting.util.KeyCombo
import java.util.*

abstract class GameDef(val name: String, val id: String, val version: Int) {
    val events: MutableList<RobotEventDef> = mutableListOf()
    val endStates: MutableList<RobotEndState> = mutableListOf()

    protected fun addEventDef(id: String, name: String, stage: Int, keyCombo: KeyCombo, color: Color = Color(0.0, 0.0, 0.0), maxTimes: Int = -1, icon: String? = null) {
        events.add(RobotEventDef(createId(id), name, stage, keyCombo, color, maxTimes, icon))
    }

    fun createId(base: String): String = "${this.id}:$version:$base"

    protected fun addEndState(id: String, name: String, icon: String? = null) {
        endStates.add(RobotEndState(createId(id), name, icon))
    }

}

data class RobotEventDef(val id: String, val name: String, val stage: Int, val keyCombo: KeyCombo, val color: Color, val maxTimes: Int = -1, val icon: String? = null) {
    fun createEventInstance(team: Int): RobotEvent {
        return RobotEvent(id, System.currentTimeMillis(), JsonNodeFactory.instance.objectNode())
    }
}

data class RobotEndState(val id: String, val name: String, val icon: String? = null)

