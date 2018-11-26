package com.ironpanthers.scouting.desktop.util

import com.ironpanthers.scouting.BLUETOOTH_CHAT_UUID_RAW
import com.ironpanthers.scouting.BLUETOOTH_MAIN_UUID_RAW
import javax.bluetooth.UUID

const val TBA_API_URL = "https://www.thebluealliance.com/api/v3/"

val BT_MATCH = UUID(BLUETOOTH_MAIN_UUID_RAW, false)
val BT_CHAT = UUID(BLUETOOTH_CHAT_UUID_RAW, false)
