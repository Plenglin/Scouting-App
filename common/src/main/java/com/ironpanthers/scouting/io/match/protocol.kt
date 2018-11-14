package com.ironpanthers.scouting.io.match

import com.fasterxml.jackson.databind.JsonNode
import java.util.*


const val MSG_MATCH_BEGIN = "BEGIN_MATCH"

data class Message(val sender: UUID, val type: String, val id: UUID = UUID.randomUUID(), val data: JsonNode? = null)

