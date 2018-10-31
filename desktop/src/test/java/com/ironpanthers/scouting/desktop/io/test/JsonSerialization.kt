package com.ironpanthers.scouting.desktop.io.test

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.util.*

data class RawRobotEvent(val id: String, val type: String, val time: Long, val data: JsonNode)

data class RawMatchRobot(val team: Int, val events: List<RawRobotEvent>, val endState: String?)

data class RawCompetition(val name: String, val date: Date, val gameType: String, val matches: List<RawMatch>)

data class RawMatch(val number: Int, val time: Long, val red: List<RawMatchRobot>, val blue: List<RawMatchRobot>)

class JsonSerialization

/*val dateConverter = object : Converter {
    override fun canConvert(cls: Class<*>): Boolean = cls == LocalDate::class.java

    override fun fromJson(jv: JsonValue): Any {
        return LocalDate.parse(jv.string!!)
    }

    override fun toJson(value: Any): String {
        return (value as LocalDate).toString()
    }

}*/

fun main(args: Array<String>) {
    val cl = JsonSerialization::class.java.classLoader
    val mapper = jacksonObjectMapper()
    val str = cl.getResource("test/test-competition.json").readText()
    val obj = mapper.readValue<RawCompetition>(str)
    println(obj)
    println(mapper.writeValueAsString(obj))
}