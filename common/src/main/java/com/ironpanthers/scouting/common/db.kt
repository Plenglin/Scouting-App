package com.ironpanthers.scouting.common

const val TABLE_MATCH = "FRCMatch"
const val TABLE_ALLIANCE = "Alliance"
const val TABLE_ROBOT = "MatchRobot"
const val TABLE_COMPETITION = "Competition"
const val TABLE_ROBOT_EVENT = "RobotEvent"

const val STM_GET_COMP_DESC = "SELECT date, game_def FROM $TABLE_COMPETITION WHERE id = ?"

const val STM_GET_MATCH_DESC_FROM_COMP = "SELECT $TABLE_MATCH.id, $TABLE_MATCH.number, $TABLE_ALLIANCE.color, $TABLE_ROBOT.team FROM $TABLE_ROBOT " +
        "INNER JOIN $TABLE_ALLIANCE ON $TABLE_ROBOT.alliance = $TABLE_ALLIANCE.id " +
        "INNER JOIN $TABLE_MATCH ON $TABLE_ALLIANCE.match = $TABLE_MATCH.id " +
        "WHERE $TABLE_MATCH.competition = ? " +
        "ORDER BY $TABLE_MATCH.number ASC, $TABLE_ALLIANCE.color DESC"