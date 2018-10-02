package com.ironpanthers.scouting.util

import javafx.scene.input.KeyCode


const val SHIFT = 0x1
const val ALT = 0x2
const val CTRL = 0x4

data class KeyCombo(val keyCode: KeyCode, val modifiers: Int = 0)