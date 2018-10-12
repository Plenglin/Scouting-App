package com.ironpanthers.scouting.desktop

import com.ironpanthers.scouting.common.RobotEndState
import com.ironpanthers.scouting.common.RobotEventDef
import com.ironpanthers.scouting.util.ALT
import com.ironpanthers.scouting.util.CTRL
import com.ironpanthers.scouting.util.KeyCombo
import com.ironpanthers.scouting.util.SHIFT
import javafx.beans.property.BooleanProperty
import javafx.beans.property.ReadOnlyProperty
import javafx.beans.value.WritableValue
import javafx.scene.control.Button
import javafx.scene.image.ImageView
import javafx.scene.input.KeyEvent
import kotlin.reflect.KProperty

fun KeyCombo.test(event: KeyEvent): Boolean {
    return event.code == keyCode
            && (event.isShiftDown == (modifiers and SHIFT != 0))
            && (event.isAltDown == (modifiers and ALT != 0))
            && (event.isControlDown == (modifiers and CTRL != 0))
}

fun RobotEventDef.createButton(): Button =
        if (icon != null) {
            Button("", ImageView(icon))
        } else {
            Button(name)
        }

fun RobotEndState.createButton(): Button =
        if (icon != null) {
            Button("", ImageView(icon))
        } else {
            Button(name)
        }

fun Double.lerp(a1: Double, b1: Double, a2: Double, b2: Double): Double {
    return (b2 - b1) * (this - a1) / (a2 - a1) + a2
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

