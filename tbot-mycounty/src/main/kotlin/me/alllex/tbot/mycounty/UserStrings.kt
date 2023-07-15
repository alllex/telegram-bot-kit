package me.alllex.tbot.mycounty

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

object UserStrings {

    private val strings: Map<String, String> by lazy { readResource() }

    operator fun get(key: String): String {
        return strings[key] ?: error("Unknown key for user strings: $key")
    }

    private fun readResource(): Map<String, String> {
        val resourceName = "/mycounty/strings.yaml"
        val resourceStream = UserStrings::class.java.getResourceAsStream(resourceName)
            ?: error("Resource not found: $resourceName")
        val resourceText = resourceStream.bufferedReader().use {
            it.readText()
        }

        val mapper = ObjectMapper(YAMLFactory()).registerKotlinModule()
        val export = mapper.readValue(resourceText, Export::class.java)

        return export.strings
    }

    private data class Export(
        val strings: Map<String, String>
    )
}
