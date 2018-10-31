package com.ironpanthers.scouting.desktop.util

import com.ironpanthers.scouting.YEAR_TO_GAME_DEF
import com.ironpanthers.scouting.common.*
import com.ironpanthers.scouting.frc2018.GameDef2018
import com.ironpanthers.scouting.util.*
import javafx.beans.property.BooleanProperty
import javafx.beans.property.ReadOnlyProperty
import javafx.beans.value.WritableValue
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.input.KeyEvent
import org.json.JSONArray
import tornadofx.getProperty
import java.util.*
import java.util.concurrent.Executors
import kotlin.reflect.KProperty


val ioExecutor = Executors.newCachedThreadPool()

fun KeyCombo.test(event: KeyEvent): Boolean {
    return event.code == keyCode
            && (event.isShiftDown == (modifiers and SHIFT != 0))
            && (event.isAltDown == (modifiers and ALT != 0))
            && (event.isControlDown == (modifiers and CTRL != 0))
}

/**
 * Takes the number from the range [a1, b1] and scales it to the range [a2, b2]
 */
fun Double.lerp(a1: Double, b1: Double, a2: Double, b2: Double): Double {
    return (b2 - a2) / (b1 - a1) * (this - a1) + a2
}

operator fun <T> ReadOnlyProperty<T>.getValue(thisRef: Any?, property: KProperty<*>): T? {
    return value
}

operator fun <T> WritableValue<T>.setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
    this.value = value
}

operator fun BooleanProperty.getValue(thisRef: Any?, property: KProperty<*>): Boolean {
    return value
}

operator fun BooleanProperty.setValue(thisRef: Any?, property: KProperty<*>, value: Boolean) {
    this.value = value
}

fun GameDef.getFXViewData(): FXMLLoader? = when (this) {
    is GameDef2018 -> {
        val file = javaClass.classLoader.getResource("views/2018-power-up.fxml")
        val loader = FXMLLoader()
        loader.location = file
        loader
    }
    else -> null
}

fun getAllNodes(root: Parent): ArrayList<Node> {
    val nodes = ArrayList<Node>()
    addAllDescendents(root, nodes)
    return nodes
}

private fun addAllDescendents(parent: Parent, nodes: ArrayList<Node>) {
    for (node in parent.childrenUnmodifiable) {
        nodes.add(node)
        if (node is Parent)
            addAllDescendents(node, nodes)
    }
}

fun importTBAData(eventId: String, apiKey: String): Competition {
    val headers = mapOf("X-TBA-Auth-Key" to apiKey, "Content-Type" to "application/json")
    val eventData = khttp.get("https://www.thebluealliance.com/api/v3/event/$eventId", headers = headers).jsonObject
    val matchData = khttp.get("https://www.thebluealliance.com/api/v3/event/$eventId/matches", headers = headers).jsonArray


    val matches = (0 until matchData.length()).map { i ->
        val row = matchData.getJSONObject(i).getJSONObject("alliances")
        val redRaw = row.getJSONObject("red").getJSONArray("team_keys")
        val blueRaw = row.getJSONObject("blue").getJSONArray("team_keys")

        Match(i + 1, -1, extractAlliance(redRaw), extractAlliance(blueRaw))
    }

    return Competition(
            eventData.getString("name"),
            iso8601.parse(eventData.getString("start_date")),
            YEAR_TO_GAME_DEF[eventData.getInt("year")]!!.id,
            matches
    )
}

fun extractAlliance(arr: JSONArray): List<MatchRobot> {
    val list = mutableListOf(arr.getString(0), arr.getString(1), arr.getString(2))
    return list.map {
        MatchRobot(it.substring(3).toInt(), listOf())
    }
}