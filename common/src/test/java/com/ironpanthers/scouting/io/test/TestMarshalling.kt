package com.ironpanthers.scouting.io.test

import com.ironpanthers.scouting.io.shared.marshal
import com.ironpanthers.scouting.io.shared.unmarshal
import org.junit.Assert
import org.junit.Test
import java.util.*

data class Person (
        val name: String,
        val age: Int,
        val id: UUID = UUID.randomUUID())

data class Company(val leader: Person, val employees: Set<Person>, val properties: Map<String, String>)

class TestMarshalling {

    @Test
    fun testMarshalling() {
        val ted = Person("ted", 42)
        val bob = Person("bob", 32)
        val alice = Person("alice", 43)

        val acme = Company(ted, setOf(bob, alice), properties = mapOf("memes" to "dreams"))
        val marshaled = marshal(acme)
        println(marshaled)
        Assert.assertEquals(acme, unmarshal(marshaled))
    }

}
