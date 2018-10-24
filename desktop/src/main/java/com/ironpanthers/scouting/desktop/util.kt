package com.ironpanthers.scouting.desktop

import com.ironpanthers.scouting.common.GameDef
import com.ironpanthers.scouting.frc2018.GameDef2018
import com.ironpanthers.scouting.io.server.ClientInfo
import com.ironpanthers.scouting.util.ALT
import com.ironpanthers.scouting.util.CTRL
import com.ironpanthers.scouting.util.KeyCombo
import com.ironpanthers.scouting.util.SHIFT
import javafx.beans.property.BooleanProperty
import javafx.beans.property.ReadOnlyProperty
import javafx.beans.value.WritableValue
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.input.KeyEvent
import kotlin.reflect.KProperty
import javafx.scene.Parent
import javafx.scene.control.ListCell
import javafx.scene.control.TableRow
import java.util.ArrayList



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
