package com.ironpanthers.scouting.common

import com.fasterxml.jackson.databind.JsonNode
import com.ironpanthers.scouting.util.KeyCombo
import java.util.*


const val AUTO = 0x1
const val TELEOP = 0x2
const val ENDGAME_ONLY = 0x4
const val ENDGAME = TELEOP or ENDGAME_ONLY
const val ANY = AUTO or TELEOP or ENDGAME_ONLY

enum class TeamColor {
    RED, BLUE
}

data class MutableRobotEvent(var type: String, var time: Long, var data: JsonNode, val id: UUID = UUID.randomUUID()) {
    fun asImmutable() = RobotEvent(type, time, data, id)
}

data class MutableMatchRobot(var team: Int, val events: MutableList<MutableRobotEvent> = mutableListOf(), var endState: String? = null) {
    fun asImmutable() = MatchRobot(team, events.map(MutableRobotEvent::asImmutable), endState)
}

data class MutableMatch(var number: Int, var time: Long, val red: List<MutableMatchRobot>, val blue: List<MutableMatchRobot>) {
    fun asImmutable() = Match(number, time, red.map(MutableMatchRobot::asImmutable), blue.map(MutableMatchRobot::asImmutable))
}

data class MutableCompetition(var name: String, var date: Date, var gameType: String, val matches: MutableList<MutableMatch>) {
    fun asImmutable() = Competition(name, date, gameType, matches.map(MutableMatch::asImmutable))
}


data class RobotEvent(val type: String, val time: Long, val data: JsonNode, val id: UUID = UUID.randomUUID()) {
    fun asMutable() = MutableRobotEvent(type, time, data, id)
}

data class MatchRobot(val team: Int, val events: List<RobotEvent>, val endState: String? = null) {
    fun asMutable() = MutableMatchRobot(team, events.asSequence().map(RobotEvent::asMutable).toMutableList(), endState)
}

data class Match(val number: Int, val time: Long, val red: List<MatchRobot>, val blue: List<MatchRobot>) {
    fun asMutable() = MutableMatch(number, time, red.map(MatchRobot::asMutable), blue.map(MatchRobot::asMutable))
}

data class Competition(val name: String, val date: Date, val gameType: String, val matches: List<Match>) {
    fun asMutable() = MutableCompetition(name, date, gameType, matches.asSequence().map(Match::asMutable).toMutableList())
}
