package com.ironpanthers.scouting.common

import javafx.scene.control.Button
import javafx.scene.image.ImageView
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent


enum class TeamColor {
    RED, BLUE
}

enum class MatchResult {
    RED_VICTORY, BLUE_VICTORY, DRAW, OTHER
}

enum class GameStage {
    AUTO, TELEOP, ENDGAME, ANY;

    val isAuto: Boolean get() {
        return this == ANY || this == AUTO
    }

    val isTeleop: Boolean get() {
        return this == ANY || this == TELEOP || this == ENDGAME
    }

    val isEndgame: Boolean get() {
        return this == ANY || this == ENDGAME
    }
}

data class RobotEventDef(val id: String, val name: String, val stage: GameStage, val maxTimes: Int = -1, val icon: String? = null, val listener: (KeyEvent) -> Boolean) {
    fun createButton(): Button =
        if (icon != null) {
            Button("", ImageView(icon))
        } else {
            Button(name)
        }

}

data class RobotEvent(val id: String, val team: Int) {
    val time: Long = System.currentTimeMillis()
}

data class Team(val number: Int, val name: String)

data class RobotPerformance(val team: Int) {
    val events: MutableList<RobotEvent> = mutableListOf()
}

data class Alliance(val teams: List<RobotPerformance>) {

}

data class Match(val red: Alliance, val blue: Alliance) {
    val teams: List<RobotPerformance> by lazy { red.teams + blue.teams }
}

fun main(args: Array<String>) {

}