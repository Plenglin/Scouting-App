package com.ironpanthers.scouting.common

import javafx.scene.input.KeyEvent

abstract class GameDef(val name: String, val id: String, val version: Int) {
    val events: MutableList<RobotEventDef> = mutableListOf()

    fun addEventDef(id: String, name: String, stage: GameStage, maxTimes: Int = -1, icon: String? = null, listener: (KeyEvent) -> Boolean) {
        events.add(RobotEventDef("${this.id}:$version:$id", name, stage, maxTimes, icon, listener))
    }

    abstract fun upgrade(event: RobotEvent, from: Int, to: Int)
}

