package com.ironpanthers.scouting.io.match

import com.fasterxml.jackson.databind.JsonNode
import java.util.*


const val MSG_MATCH_BEGIN = "BEGIN"
const val MSG_MATCH_ASSIGN = "AS_M"
const val MSG_MATCH_UPDATE = "UP_M"
const val MSG_HANDSHAKE = "HDSH"
//val MSG_HANDSHAKE_UUID = UUID.fromString("173cda5e-c017-3231-8915-c2e0c849db86")

data class Message(val sender: UUID, val type: String, val id: UUID = UUID.randomUUID(), val data: JsonNode? = null)

