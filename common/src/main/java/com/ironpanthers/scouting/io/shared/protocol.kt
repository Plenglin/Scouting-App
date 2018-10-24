package com.ironpanthers.scouting.io.shared

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.util.JSONPObject
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.lang.Exception


class NoClassNameException : Exception("No class data attached to the JSON!")

val mapper = jacksonObjectMapper()

fun marshal(obj: Any): String {
    val serializedData = mapper.valueToTree<JsonNode>(obj)
    val node = JsonNodeFactory.instance.objectNode()
            .put("className", obj.javaClass.name)
    node.set("data",  serializedData)
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