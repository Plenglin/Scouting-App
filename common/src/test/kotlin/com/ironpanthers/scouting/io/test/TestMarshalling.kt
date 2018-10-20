package com.ironpanthers.scouting.io.test

import com.ironpanthers.scouting.io.shared.marshal

data class Person(val name: String, val age: Int)

fun main(args: Array<String>) {
    val obj = Person("bob", 32)
    println(obj)
    val marshalled = marshal(obj)
    println(marshalled)
}