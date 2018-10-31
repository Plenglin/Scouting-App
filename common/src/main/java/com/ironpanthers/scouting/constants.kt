package com.ironpanthers.scouting

import com.ironpanthers.scouting.common.GameDef
import com.ironpanthers.scouting.frc2018.GameDef2018
import java.util.*


val PROTOCOL_VERSION = 0

private val BLUETOOTH_SERVER_UUID_STRING = "Iron_Panthers_Scouting_App_$PROTOCOL_VERSION"

val BLUETOOTH_SERVER_UUID = UUID.nameUUIDFromBytes(BLUETOOTH_SERVER_UUID_STRING.toByteArray())

val YEAR_TO_GAME_DEF = mapOf<Int, GameDef>(
        2018 to GameDef2018
)