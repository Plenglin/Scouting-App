package com.ironpanthers.scouting.android


import awaitStringResponse
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.treeToValue
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.ironpanthers.scouting.YEAR_TO_GAME_DEF
import com.ironpanthers.scouting.common.Competition
import com.ironpanthers.scouting.common.CompetitionFileSummary
import com.ironpanthers.scouting.common.Match
import com.ironpanthers.scouting.common.MatchRobot
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.io.File
import java.text.SimpleDateFormat

fun loadCompetitionFileData(file: File, mapper: ObjectMapper): CompetitionFileSummary {
    return mapper.treeToValue<Competition>(mapper.readTree(file)).createFileSummary(file)
}

fun writeCompetitionFileData(file: File, competition: Competition, mapper: ObjectMapper) {
    return mapper.writeValue(file, competition)
}

val iso8601 = SimpleDateFormat("yyyy-MM-dd")

suspend fun fetchTBAData(eventId: String, apiKey: String): Competition {
    val mapper = jacksonObjectMapper()
    val jobEventData = GlobalScope.async {
        "https://www.thebluealliance.com/api/v3/event/$eventId".httpGet()
            .header("X-TBA-Auth-Key" to apiKey, "Content-Type" to "application/json")
            .awaitStringResponse()
    }

    val jobMatchData = GlobalScope.async {
        "https://www.thebluealliance.com/api/v3/event/$eventId/matches".httpGet()
            .header("X-TBA-Auth-Key" to apiKey, "Content-Type" to "application/json")
            .awaitStringResponse()
    }

    val eventData = jobEventData.await().let { (_, _, result) ->
        when (result) {
            is Result.Success ->mapper.readTree(result.value)
            is Result.Failure -> throw result.error
        }
    }
    val matchData = jobMatchData.await().let { (_, _, result) ->
        when (result) {
            is Result.Success ->mapper.readTree(result.value)
            is Result.Failure -> throw result.error
        }
    }

    var n = 0
    val matches = matchData.elements().asSequence().mapNotNull { obj ->

        if (obj.get("comp_level").asText() != "qm") {
            null
        } else {
            val row = obj.get("alliances")
            val redRaw = row.get("red").get("team_keys")
            val blueRaw = row.get("blue").get("team_keys")

            n += 1
            Match(n, -1, extractAlliance(redRaw), extractAlliance(blueRaw))
        }
    }.toList()

    return Competition(
            eventData.get("name").asText(),
            iso8601.parse(eventData.get("start_date").asText()),
            YEAR_TO_GAME_DEF[eventData.get("year").asInt()]!!.id,
            matches
    )


}

fun extractAlliance(arr: JsonNode): List<MatchRobot> {
    return arr.elements().asSequence().map { MatchRobot(it.asInt(), emptyList()) }.toList()
}
