package com.ironpanthers.scouting

import java.util.*


val PROTOCOL_VERSION = 0

private val BLUETOOTH_SERVER_UUID_STRING = "Iron_Panthers_Scouting_App_$PROTOCOL_VERSION"

val BLUETOOTH_SERVER_UUID = UUID.nameUUIDFromBytes(BLUETOOTH_SERVER_UUID_STRING.toByteArray())