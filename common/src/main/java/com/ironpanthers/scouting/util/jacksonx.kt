package com.ironpanthers.scouting.util

import com.fasterxml.jackson.databind.ObjectMapper

inline fun <reified T> ObjectMapper.readValue(json: String): T {
    return this.readValue(json, T::class.java)
}