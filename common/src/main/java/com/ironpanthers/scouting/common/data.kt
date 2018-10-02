package com.ironpanthers.scouting.common


enum class TeamColor {
    RED, BLUE
}

enum class MatchResult {
    RED_VICTORY, BLUE_VICTORY, DRAW, OTHER
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