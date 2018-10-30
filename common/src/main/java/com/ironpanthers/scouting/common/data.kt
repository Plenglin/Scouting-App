package com.ironpanthers.scouting.common

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


data class RobotEventDef(val id: String, val name: String, val stage: Int, val keyCombo: KeyCombo, val maxTimes: Int = -1, val icon: String? = null) {
    fun createEventInstance(team: Int): RobotEvent {
        return RobotEvent(id, team)
    }
}

data class RobotEndState(val id: String, val name: String, val icon: String? = null)

data class RobotEvent(val id: String, val team: Int) {
    val time: Long = System.currentTimeMillis()
    val extras: MutableMap<String, String> = mutableMapOf()
}

data class Team(val number: Int, val name: String)

/**
 * A description of all the things a robot did during a match.
 */
data class MatchRobot(val id: Int, val team: Int, val start: Long, val events: List<RobotEvent>, val endState: String)

data class Alliance(val color: TeamColor, val teams: List<MatchRobot>)

data class MatchDescription(val number: Int, val red: Alliance, val blue: Alliance)

data class CompetitionSummary(val id: Int, val name: String, val date: Date, val gameDef: String, val matchCount: Int)

data class CompetitionMatchData(val id: Int, val date: Date, val gameDef: String, val matches: List<MatchSummary>)

data class MatchSummary(val id: Int, val number: Int, val red: List<Int>, val blue: List<Int>)
