package com.ironpanthers.scouting.io.test

import com.ironpanthers.scouting.io.shared.marshal
import com.ironpanthers.scouting.io.shared.unmarshal
import org.junit.Assert
import org.junit.Test
import java.util.*

data class Person(var name: String, var age: Int, var id: UUID = UUID.randomUUID()) {
    constructor() : this("", 0)
}

data class Company(var leader: Person?, var employees: Set<Person>, var properties: Map<String, String>) {
    constructor() : this(null, emptySet(), emptyMap())
}

class TestMarshalling {

    @Test
    fun testMarshalling() {
        val ted = Person("ted", 42)
        val bob = Person("bob", 32)
        val alice = Person("alice", 43)

        val acme = Company(ted, setOf(bob, alice), properties = mapOf("memes" to "dreams"))
        Assert.assertEquals(acme, unmarshal(marshal(acme)))
    }

}
