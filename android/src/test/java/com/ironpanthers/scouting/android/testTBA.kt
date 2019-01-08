package com.ironpanthers.scouting.android

import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) {
    println(runBlocking {
        fetchTBAData("eeszr95mLfyCJkDqDrXAxsW93MgRYrmn8lLqrUJ76GfSYIWNxA8N5mCUILlX3dFN", "2018ohmv")
    })
}