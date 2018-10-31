package com.ironpanthers.scouting.desktop.io.test

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.util.*

data class RawRobotEvent(val id: UUID, val type: String, val time: Long, val data: JsonNode)

data class RawMatchRobot(val team: Int, val events: List<RawRobotEvent>, val endState: String?)

data class RawCompetition(val name: String, val date: Date, val gameType: String, val matches: List<RawMatch>)

data class RawMatch(val number: Int, val time: Long, val red: List<RawMatchRobot>, val blue: List<RawMatchRobot>)

class JsonSerialization

fun main(args: Array<String>) {
    val cl = JsonSerialization::class.java.classLoader
    val mapper = jacksonObjectMapper()
    val str = cl.getResource("test/test-competition.json").readText()
    val obj = mapper.readValue<RawCompetition>(str)
    println(obj)
    println(mapper.writeValueAsString(obj))
}