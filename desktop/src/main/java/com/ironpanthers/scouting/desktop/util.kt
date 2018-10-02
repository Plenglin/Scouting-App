package com.ironpanthers.scouting.desktop

import com.ironpanthers.scouting.common.RobotEndState
import com.ironpanthers.scouting.common.RobotEventDef
import com.ironpanthers.scouting.util.ALT
import com.ironpanthers.scouting.util.CTRL
import com.ironpanthers.scouting.util.KeyCombo
import com.ironpanthers.scouting.util.SHIFT
import javafx.scene.control.Button
import javafx.scene.image.ImageView
import javafx.scene.input.KeyEvent

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
