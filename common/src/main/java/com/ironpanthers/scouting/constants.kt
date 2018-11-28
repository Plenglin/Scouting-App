package com.ironpanthers.scouting

import com.ironpanthers.scouting.common.GameDef
import com.ironpanthers.scouting.frc2018.GameDef2018
import java.util.*


val PROTOCOL_VERSION = 0

private val BLUETOOTH_MAIN_UUID_STRING = "Iron_Panthers_Scouting_App_MATCH_$PROTOCOL_VERSION"
private val BLUETOOTH_CHAT_UUID_STRING = "Iron_Panthers_Scouting_App_CHAT_$PROTOCOL_VERSION"

val BLUETOOTH_MAIN_UUID = UUID.nameUUIDFromBytes(BLUETOOTH_MAIN_UUID_STRING.toByteArray())
val BLUETOOTH_MAIN_UUID_RAW = BLUETOOTH_MAIN_UUID.toString().replace("-", "")

val BLUETOOTH_CHAT_UUID = UUID.nameUUIDFromBytes(BLUETOOTH_CHAT_UUID_STRING.toByteArray())
val BLUETOOTH_CHAT_UUID_RAW = BLUETOOTH_CHAT_UUID.toString().replace("-", "")

val BLUETOOTH_NAME = "panther-scout"

val YEAR_TO_GAME_DEF = mapOf<Int, GameDef>(
        2018 to GameDef2018
)