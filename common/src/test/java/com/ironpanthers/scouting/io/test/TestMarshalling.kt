package com.ironpanthers.scouting.io.test

import com.ironpanthers.scouting.io.shared.marshal
import com.ironpanthers.scouting.io.shared.unmarshal
import java.util.*

data class Person(var name: String, var age: Int, var id: UUID = UUID.randomUUID()) {
    constructor() : this("", 0)
}

data class Company(var leader: Person?, var employees: List<Person>, var properties: Map<String, String>) {
    constructor() : this(null, emptyList(), emptyMap())
}

fun main(args: Array<String>) {
    val obj = Company(Person("ted", 42), listOf(Person("bob", 32)), properties = mapOf("memes" to "dreams"))
    println(obj)
    val marshalled = marshal(obj)
    println(marshalled)
    val unmarshalled = unmarshal(marshalled)
    println(unmarshalled)
}