package me.alllex.tbot.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.StringWriter
import java.io.Writer
import java.nio.file.Files
import java.nio.file.Path

fun <T : Any> T.serialiseAsYamlInto(writer: Writer) {
    val mapper = createKotlinObjectMapper()
    mapper.writeValue(writer, this)
}

fun <T : Any> T.serialiseAsYamlIntoString(): String {
    val sw = StringWriter()
    this.serialiseAsYamlInto(sw)
    return sw.toString()
}

inline fun <reified T : Any> Path.parseYaml(): T {
    return parseYamlInto(T::class.java)
}

@PublishedApi
internal fun <T : Any> Path.parseYamlInto(target: Class<T>): T {
    val mapper = createKotlinObjectMapper()
    return Files.newBufferedReader(this).use {
        mapper.readValue(it, target)
    }
}

private fun createKotlinObjectMapper(): ObjectMapper {
    return ObjectMapper(YAMLFactory()).registerKotlinModule()
}
