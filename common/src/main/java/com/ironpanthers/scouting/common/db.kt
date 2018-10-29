package com.ironpanthers.scouting.common

const val TABLE_MATCH = "FRCMatch"
const val TABLE_ALLIANCE = "Alliance"
const val TABLE_ROBOT = "MatchRobot"
const val TABLE_COMPETITION = "Competition"
const val TABLE_ROBOT_EVENT = "RobotEvent"

const val STM_GET_COMP_INFO =
        "SELECT " +
                "date, " +
                "game_def, " +
                "(SELECT count() FROM $TABLE_MATCH WHERE $TABLE_MATCH.competition = $TABLE_COMPETITION.id) as count, " +
                "NULL " +
            "FROM $TABLE_COMPETITION WHERE id = ? " +
        "UNION ALL " +
        "SELECT $TABLE_MATCH.id, $TABLE_MATCH.number, $TABLE_ALLIANCE.color, $TABLE_ROBOT.team FROM $TABLE_ROBOT " +
            "INNER JOIN $TABLE_ALLIANCE ON $TABLE_ROBOT.alliance = $TABLE_ALLIANCE.id " +
            "INNER JOIN $TABLE_MATCH ON $TABLE_ALLIANCE.match = $TABLE_MATCH.id " +
                "WHERE $TABLE_MATCH.competition = ? "

const val STM_LIST_COMP_DESC =
        "SELECT id, name, date, game_def, " +
            "(SELECT count() FROM $TABLE_MATCH WHERE $TABLE_MATCH.competition = $TABLE_COMPETITION.id) AS count " +
            "FROM $TABLE_COMPETITION"

val STM_INITIALIZE = listOf(
        "PRAGMA foreign_keys = ON;",

        """
        CREATE TABLE IF NOT EXISTS `$TABLE_COMPETITION` (
        `id` INTEGER PRIMARY KEY AUTOINCREMENT,
        `name` TEXT,
        `date` TEXT,
        `game_def` TEXT
        );""",

        """
        CREATE TABLE IF NOT EXISTS `$TABLE_MATCH` (
        `id` INTEGER PRIMARY KEY AUTOINCREMENT,
        `competition` INTEGER NOT NULL,
        `number` INTEGER NOT NULL,
        `time` INTEGER,

        UNIQUE(`number`, `competition`),
        FOREIGN KEY(`competition`) REFERENCES `$TABLE_COMPETITION`(`id`) ON DELETE CASCADE ON UPDATE CASCADE
        );""",

        """
        CREATE TABLE IF NOT EXISTS `$TABLE_ALLIANCE` (
        `id`	INTEGER PRIMARY KEY AUTOINCREMENT,
        `match`	INTEGER NOT NULL,
        `color`	TEXT NOT NULL CHECK(color = 'RED' OR color = 'BLUE'),
        FOREIGN KEY(`match`) REFERENCES `$TABLE_MATCH`(`id`) ON DELETE CASCADE ON UPDATE CASCADE
        );""",

        """
        CREATE TABLE IF NOT EXISTS `$TABLE_MATCH` (
        `id` INTEGER PRIMARY KEY AUTOINCREMENT,
        `alliance` INTEGER NOT NULL,
        `team` INTEGER NOT NULL,

        FOREIGN KEY(`alliance`) REFERENCES `$TABLE_ALLIANCE`(`id`) ON DELETE CASCADE ON UPDATE CASCADE
        );""",

        """
        CREATE TABLE IF NOT EXISTS $TABLE_ROBOT_EVENT (
        `time` INTEGER NOT NULL,
        `robot` INTEGER NOT NULL,
        `event_type` TEXT NOT NULL,
        `end_state` TEXT NOT NULL,

        FOREIGN KEY(`robot`) REFERENCES `$TABLE_MATCH`(`id`) ON DELETE CASCADE ON UPDATE CASCADE
        );""",

        """
        CREATE TRIGGER IF NOT EXISTS create_alliances_when_insert_match AFTER INSERT ON $TABLE_MATCH
        BEGIN
        INSERT INTO $TABLE_ALLIANCE("match", color) VALUES (NEW.id, "RED"), (NEW.id, "BLUE");
        END;"""

).map { it.trimIndent().replace("\n", " ") }

fun main(args: Array<String>) {
    println(STM_INITIALIZE.joinToString("\n"))
}
