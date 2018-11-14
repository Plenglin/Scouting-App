package com.ironpanthers.scouting.io.match.common

import com.fasterxml.jackson.databind.JsonNode
import java.util.*

data class Message(val sender: UUID, val type: String, val data: JsonNode)


