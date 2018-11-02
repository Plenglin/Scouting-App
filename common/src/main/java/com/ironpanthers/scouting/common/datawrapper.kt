package com.ironpanthers.scouting.common


data class MatchRobotWrapper(val color: TeamColor, val robot: MutableMatchRobot, val parent: MutableMatch)

fun MutableMatch.getRedWrapper(i: Int) = MatchRobotWrapper(TeamColor.RED, red[i], this)
fun MutableMatch.getBlueWrapper(i: Int) = MatchRobotWrapper(TeamColor.BLUE, blue[i], this)
