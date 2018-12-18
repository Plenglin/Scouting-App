package com.ironpanthers.scouting.common

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import java.io.Serializable
import java.util.*


typealias Team = Int

const val AUTO_ONLY: Int = 0x1
const val TELEOP_ONLY: Int = 0x2
const val ENDGAME_ONLY: Int = 0x4
const val ENTIRE_TELEOP: Int = TELEOP_ONLY or ENDGAME_ONLY
const val ANY_GAME_PHASE: Int = AUTO_ONLY or TELEOP_ONLY or ENDGAME_ONLY

enum class TeamColor {
    RED, BLUE
}

data class MutableRobotEvent(var type: String, var time: Long, var data: JsonNode, val id: UUID = UUID.randomUUID()) {
    fun asImmutable() = RobotEvent(type, time, data, id)
}

data class MutableMatchRobot(var team: Team, val events: MutableList<MutableRobotEvent> = mutableListOf(), var endState: String? = null) {
    fun asImmutable() = MatchRobot(team, events.map(MutableRobotEvent::asImmutable), endState)
}

data class MutableMatch(var number: Int, var time: Long, val red: List<MutableMatchRobot>, val blue: List<MutableMatchRobot>) {
    fun asImmutable() = Match(number, time, red.map(MutableMatchRobot::asImmutable), blue.map(MutableMatchRobot::asImmutable))
}

data class MutableCompetition(var name: String, var date: Date, var gameType: String, val matches: MutableList<MutableMatch>) {
    fun asImmutable() = Competition(name, date, gameType, matches.map(MutableMatch::asImmutable))
}


data class RobotEvent(val type: String, val time: Long, val data: JsonNode = JsonNodeFactory.instance.objectNode(), val id: UUID = UUID.randomUUID()) : Serializable {
    fun asMutable() = MutableRobotEvent(type, time, data, id)
}

data class MatchRobot(val team: Team, val events: List<RobotEvent>, val endState: String? = null) : Serializable {
    fun asMutable() = MutableMatchRobot(team, events.asSequence().map(RobotEvent::asMutable).toMutableList(), endState)
}

data class Match(val number: Int, val time: Long, val red: List<MatchRobot>, val blue: List<MatchRobot>) : Serializable {
    fun asMutable() = MutableMatch(number, time, red.map(MatchRobot::asMutable), blue.map(MatchRobot::asMutable))
}

data class Competition(val name: String, val date: Date, val gameType: String, val matches: List<Match>) : Serializable {
    fun asMutable() = MutableCompetition(name, date, gameType, matches.asSequence().map(Match::asMutable).toMutableList())
}
