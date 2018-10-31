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


data class RobotEvent(val id: UUID, val type: String, val time: Long, val data: JsonNode)
data class MatchRobot(val team: Int, val events: List<RobotEvent>, val endState: String?)
data class Match(val number: Int, val time: Long, val red: List<MatchRobot>, val blue: List<MatchRobot>)
data class Competition(val name: String, val date: Date, val gameType: String, val matches: List<Match>)
