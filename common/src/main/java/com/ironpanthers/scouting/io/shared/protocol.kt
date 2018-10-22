package com.ironpanthers.scouting.io.shared

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.ironpanthers.scouting.io.client.ClientEngine
import java.lang.Exception


class NoClassNameException : Exception("No class data attached to the JSON!")

val mapper = ObjectMapper()

fun marshal(obj: Any): String {
    val node = JsonNodeFactory.instance.objectNode()
            .putPOJO("data", obj)
            .put("className", obj.javaClass.name)
    return mapper.writeValueAsString(node)
}

fun unmarshal(string: String): Any {
    val tree = mapper.readTree(string)

    val className = tree.get("className")?.asText() ?: throw NoClassNameException()

    val theClass: Class<*> = Class.forName(className)
    return mapper.treeToValue(tree.get("data"), theClass)
}

fun main(args: Array<String>) {
    println("asdf")
}