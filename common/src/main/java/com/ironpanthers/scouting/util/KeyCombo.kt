package com.ironpanthers.scouting.util


const val SHIFT = 0x1
const val ALT = 0x2
const val CTRL = 0x4
val UNDO = KeyCombo("Z", CTRL)

data class KeyCombo(val keyCode: String, val modifiers: Int = 0)