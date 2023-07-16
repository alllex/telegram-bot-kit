package me.alllex.tbot.apigen

import java.io.File

private val unionTypesWithExplicitMarker = setOf(
    "PassportElementError"
)

private val fieldTypeSubstitutions = mapOf(
    "allowed_updates" to "List<UpdateType>",
)

data class ValueType(
    val name: String,
    val backingType: String,
    val fieldPredicate: (type: BotApiElementName, field: BotApiElement.Field) -> Boolean
)

val valueTypes = listOf(
    ValueType("ChatId", "Long") { type, field ->
        (type.value == "Chat" && field.name == "id") || field.name.endsWith("chat_id")
    },
    ValueType("UserId", "Long") { type, field ->
        (type.value == "User" && field.name == "id") || field.name.endsWith("user_id")
    },
    ValueType("MessageId", "Long") { _, field ->
        !field.name.endsWith("inline_message_id") && field.name.endsWith("message_id")
    },
    ValueType("InlineMessageId", "String") { _, field ->
        field.name.endsWith("inline_message_id")
    },
    ValueType("MessageThreadId", "Long") { _, field ->
        field.name.endsWith("message_thread_id")
    },
    ValueType("CallbackQueryId", "String") { type, field ->
        (type.value == "CallbackQuery" && field.name == "id") || field.name.endsWith("callback_query_id")
    },
    ValueType("InlineQueryId", "String") { type, field ->
        (type.value == "InlineQuery" && field.name == "id") || field.name.endsWith("inline_query_id")
    },
    ValueType("InlineQueryResultId", "String") { type, field ->
        (type.value.startsWith("InlineQuery") && field.name == "id") ||
            (type.value == "ChosenInlineResult" && field.name == "result_id")
    },
    ValueType("FileId", "String") { _, field ->
        field.name.endsWith("file_id")
    },
    ValueType("FileUniqueId", "String") { _, field ->
        field.name.endsWith("file_unique_id")
    },
    ValueType("ShippingQueryId", "String") { type, field ->
        (type.value == "ShippingQuery" && field.name == "id") || field.name.endsWith("shipping_query_id")
    },
    ValueType("WebAppQueryId", "String") { _, field ->
        field.name.endsWith("web_app_query_id")
    },
    ValueType("CustomEmojiId", "String") { _, field ->
        field.name.endsWith("custom_emoji_id")
    },
    ValueType("Seconds", "Long") { _, field ->
        field.name in setOf("cache_time", "duration", "life_period", "open_period", "timeout", "retry_after")
    },
    ValueType("UnixTimestamp", "Long") { _, field ->
        field.name in setOf("close_date", "expire_date", "until_date")
    },
)

private val unionMarkerValueInDescriptionRe = Regex("""(?:must be|always) ["“”]?([\w_]+)["“”]?""")

private val unionMarkerFieldNames = setOf("type", "status")

fun BotApiElement.Field.isUnionDiscriminator(): Boolean {
    return name in unionMarkerFieldNames
}

fun BotApiElement.getUnionDiscriminatorFieldName(): String? {
    return fields?.find { it.isUnionDiscriminator() }?.name
}

class BotApiGenerator {

    fun run(
        html: String,
        outputDirectory: File,
        packageName: String = "",
        wrapperPackageName: String = "",
    ) {
        val parser = BotApiDefinitionParser()
        val botApi = parser.run(html)

        val allTypes = botApi.types
        println("Parsed ${allTypes.size} types")
        val allMethods = botApi.methods
        println("Parsed ${allMethods.size} methods")

//        println("All unique fields and parameters:")
//        allTypes.flatMap { it.fields ?: emptyList() } + allMethods.flatMap { it.parameters }
//            .map { it.name to it.type.value }
//            .toSet()
//            .sortedBy { it.first }
//            .forEach {
//                println("${it.first}: ${it.second}")
//            }

        val unionTypes = collectUnionTypes(allTypes)
        val unionTypeParentByChild = unionTypes
            .flatMap { (parent, children) -> children.map { it to parent } }
            .toMap()

        val typesFileText = generateTypesFile(allTypes, unionTypeParentByChild, packageName)
        val methodsFileText = generateMethodsFile(allMethods, packageName, wrapperPackageName)

        val outputPackageDir = outputDirectory.resolve(packageName.replace(".", "/"))
        outputPackageDir.mkdirs()

        outputPackageDir.resolve("Types.kt").writeText(typesFileText)
        outputPackageDir.resolve("Methods.kt").writeText(methodsFileText)
    }

    private fun collectUnionTypes(elements: List<BotApiElement>): Map<BotApiElementName, List<BotApiElementName>> {
        return buildMap {
            for (el in elements) {
                if (el.unionTypes != null) {
                    put(el.name, el.unionTypes)
                }
            }
        }
    }

    private fun generateMethodsFile(
        methodElements: List<BotApiMethod>,
        packageName: String,
        wrapperPackageName: String
    ): String = buildString {
        if (packageName.isNotEmpty()) {
            appendLine("package $packageName")
        }
        appendLine()
        appendLine("import kotlinx.serialization.Serializable")
        appendLine("import io.ktor.client.call.*")
        appendLine("import io.ktor.client.request.*")
        appendLine("import io.ktor.http.*")
        if (wrapperPackageName.isNotEmpty() && wrapperPackageName != packageName) {
            appendLine("import $wrapperPackageName.*")
        }
        appendLine()
        appendLine()

        for (el in methodElements) {
            appendLine(generateMethodSourceCode(el.name, el.description, el.parameters, el.returnType))
        }
    }

    private fun generateMethodSourceCode(
        elementName: BotApiElementName,
        description: String,
        parameters: List<BotApiElement.Field>,
        returnType: KotlinType,
    ): String = buildString {

        val requestTypeName = "${elementName.value.toTitleCase()}Request"
        if (parameters.isNotEmpty()) {
            appendLine("/**")
            appendLine(" * Request body for [${elementName.value}]")
            parameters.filter { it.description.isNotEmpty() }.forEach {
                appendLine(" * @param ${it.name.snakeToCamelCase()} ${it.description}")
            }
            appendLine(" */")
            appendLine("@Serializable")
            appendLine("data class $requestTypeName(")
            for (parameter in parameters) {
                appendFieldLine(elementName, parameter, true)
            }
            appendLine(")")
            appendLine()
        }

        val httpMethod = if (parameters.isEmpty()) "get" else "post"
        appendLine("/**")
        description.split("\n").forEach {
            appendLine(" * $it")
        }
        appendLine(" */")
        append("suspend fun TelegramBotApiClient.${elementName.value}(")
        if (parameters.isNotEmpty())
            append("requestBody: $requestTypeName")
        appendLine("): TelegramResponse<${returnType.value}> =")
        appendLine("    httpClient.$httpMethod {")
        appendLine("        url {")
        appendLine("            protocol = apiProtocol")
        appendLine("            host = apiHost")
        appendLine("            port = apiPort")
        appendLine("            path(\"bot\$apiToken\", \"${elementName.value}\")")
        appendLine("        }")
        if (parameters.isNotEmpty()) {
            appendLine("        contentType(ContentType.Application.Json)")
            appendLine("        setBody(requestBody)")
        }
        appendLine("    }.body()")

        // convenience method
        if (parameters.isNotEmpty()) {
            appendLine()
            appendLine("/**")
            description.split("\n").forEach {
                appendLine(" * $it")
            }
            appendLine(" *")
            parameters.filter { it.description.isNotEmpty() }.forEach {
                appendLine(" * @param ${it.name.snakeToCamelCase()} ${it.description}")
            }
            appendLine(" */")
            appendLine("suspend fun TelegramBotApiClient.${elementName.value}(")
            for (parameter in parameters) {
                appendFieldLine(elementName, parameter, isProperty = false)
            }
            appendLine("): TelegramResponse<${returnType.value}> =")
            appendLine {
                append("    ${elementName.value}(${requestTypeName}(${parameters.joinToString { it.name.snakeToCamelCase() }}))")
            }
        }
    }

    private fun generateTypesFile(
        typeElements: List<BotApiElement>,
        unionTypeParentByChild: Map<BotApiElementName, BotApiElementName>,
        packageName: String = "",
    ) = buildString {

        appendLine("@file:OptIn(ExperimentalSerializationApi::class)")
        appendLine()

        if (packageName.isNotEmpty()) {
            appendLine("package $packageName")
        }
        appendLine()
        appendLine("import kotlinx.serialization.json.*")
        appendLine("import kotlinx.serialization.DeserializationStrategy")
        appendLine("import kotlinx.serialization.ExperimentalSerializationApi")
        appendLine("import kotlinx.serialization.SerialName")
        appendLine("import kotlinx.serialization.Serializable")
        appendLine()

        val specialTypes: Map<String, StringBuilder.(BotApiElement) -> Unit> = mapOf(
            "Update" to { generateSourceCodeForUpdate(it) },
        )

        val processedSpecialTypes = mutableSetOf<String>()

        for (el in typeElements) {
            val specialSourceGeneration = specialTypes[el.name.value]
            if (specialSourceGeneration != null) {
                processedSpecialTypes += el.name.value
                specialSourceGeneration(el)
            } else if (el.fields != null) {
                appendLine(generateContentfulTypeSourceCode(el.name, el.description, el.fields, unionTypeParentByChild))
            } else if (el.unionTypes != null) {
                val unionTypes = el.unionTypes.map {
                    typeElements.find { typeElement -> typeElement.name == it } ?: error("Unknown union type: $it")
                }
                appendLine(generateUnionTypeSourceCode(el.name, el.description, unionTypes))
            } else {
                error("Unknown type: $el")
            }
        }

        val unprocessedSpecialTypes = specialTypes.keys - processedSpecialTypes
        check(unprocessedSpecialTypes.isEmpty()) {
            "Special types not processed: $unprocessedSpecialTypes"
        }

        for (valueType in valueTypes) {
            appendLine(generateValueTypeSourceCode(valueType))
        }
    }

    private fun generateValueTypeSourceCode(valueType: ValueType): String = buildString {
        appendLine("@Serializable")
        appendLine("@JvmInline")
        appendLine("value class ${valueType.name}(val value: ${valueType.backingType})")
    }

    private fun StringBuilder.generateSourceCodeForUpdate(updateTypeElement: BotApiElement) {
        val name = updateTypeElement.name.value
        val types = updateTypeElement.fields?.filter { it.isOptional } ?: emptyList()

        appendLine("/**")
        appendLine(" * Type of updates Telegram Bot can receive.")
        appendLine(" */")
        appendLine("enum class ${name}Type {")
        for (updateType in types) {
            appendLine("    @SerialName(\"${updateType.name}\") ${updateType.name.snakeToLoudSnakeCase()},")
        }
        appendLine("}")

        appendLine()
        appendLine("/**")
        updateTypeElement.description.split("\n")
            .filterNot { it.startsWith("At most one of the optional parameters") }
            .forEach {
                appendLine(" * $it")
            }
        appendLine(" *")
        appendLine(" * Sub-types:")
        for (updateType in types) {
            appendLine(" * - [${updateType.name.snakeToPascalCase()}Update]")
        }
        appendLine(" */ ")
        appendLine("@Serializable(with = ${name}Serializer::class)")
        appendLine("sealed class $name {")
        appendLine("    abstract val updateId: Long")
        appendLine("    abstract val updateType: UpdateType")
        appendLine("}")
        for (updateType in types) {
            appendLine()
            appendLine("/**")
            updateType.description.removePrefix("Optional.").split("\n").forEach {
                appendLine(" * ${it.trim()}")
            }
            appendLine(" */")
            appendLine("@Serializable")
            appendLine("data class ${updateType.name.snakeToPascalCase()}Update(")
            appendLine("    override val updateId: Long,")
            appendLine("    val ${updateType.name.snakeToCamelCase()}: ${updateType.type.value},")
            appendLine("): $name() {")
            appendLine("    override val updateType: UpdateType get() = UpdateType.${updateType.name.snakeToLoudSnakeCase()}")
            appendLine("}")
        }

        appendLine()
        appendLine("object ${name}Serializer : JsonContentPolymorphicSerializer<$name>($name::class) {")
        appendLine("    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<$name> {")
        appendLine("        val json = element.jsonObject")
        appendLine("        return when {")
        for (field in types) {
            appendLine("            \"${field.name}\" in json -> ${field.name.snakeToPascalCase()}Update.serializer()")
        }
        // TODO: do not fail here, as new update types may be added in the future, and we want the bot to gracefully ignore them
        appendLine("            else -> error(\"Failed to deserialize an update type: \$json\")")
        appendLine("        }")
        appendLine("    }")
        appendLine("}")
    }

    private fun generateUnionTypeSourceCode(
        name: BotApiElementName,
        description: String,
        unionTypes: List<BotApiElement>
    ): String = buildString {

        appendLine("/**")
        description.split("\n").forEach {
            appendLine(" * $it")
        }
        for (unionType in unionTypes) {
            appendLine(" * - [${unionType.name.value}]")
        }
        appendLine(" */ ")

        val discriminatorFieldNames = unionTypes.mapNotNull { it.getUnionDiscriminatorFieldName() }
            .toSet()

        val discriminatorFieldName = when (discriminatorFieldNames.size) {
            0 -> null
            1 -> discriminatorFieldNames.single()
            else -> error("Multiple discriminator field names found: $discriminatorFieldNames")
        }

        if (discriminatorFieldName == null) {
            appendLine("@Serializable(with = ${name}Serializer::class)")
        } else {
            appendLine("@Serializable")
            appendLine("@JsonClassDiscriminator(\"$discriminatorFieldName\")")
        }

        appendLine("sealed class ${name.value}")

        if (discriminatorFieldName == null) {
            val avoidFields = setOf("description")
            val discriminatorFieldByType = unionTypes.associate { unionType ->
                val otherFieldNames = unionTypes
                    .filter { it != unionType }
                    .flatMap { it.fields ?: emptyList() }
                    .map { it.name }
                    .toSet()
                val discriminatorField = unionType.fields?.find { it.name !in otherFieldNames && it.name !in avoidFields }
                    ?: error("Failed to find discriminator field for type ${unionType.name}, with fields: ${unionType.fields?.map { it.name }}")
                unionType.name to discriminatorField
            }

            appendLine()
            appendLine("object ${name}Serializer : JsonContentPolymorphicSerializer<$name>($name::class) {")
            appendLine("    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<$name> {")
            appendLine("        val json = element.jsonObject")
            appendLine("        return when {")
            for (unionType in unionTypes) {
                val discriminatorField = discriminatorFieldByType[unionType.name]
                    ?: error("Failed to find discriminator field for type ${unionType.name}")
                appendLine("            \"${discriminatorField.name}\" in json -> ${unionType.name.value}.serializer()")
            }
            appendLine("            else -> error(\"Failed to deserialize an update type: \$json\")")
            appendLine("        }")
            appendLine("    }")
            appendLine("}")
        }
    }

    private fun generateContentfulTypeSourceCode(
        name: BotApiElementName,
        description: String,
        fields: List<BotApiElement.Field>,
        unionTypeParentByChild: Map<BotApiElementName, BotApiElementName>
    ): String = buildString {

        val sealedParentName = unionTypeParentByChild[name]

        // For sealed classes the "type" field will be automatically added by kotlinx.serialization
        // See: https://github.com/Kotlin/kotlinx.serialization/blob/master/docs/polymorphism.md#custom-subclass-serial-name
        val trueFields = when {
            sealedParentName != null && sealedParentName.value !in unionTypesWithExplicitMarker -> {
                fields.filterNot { it.isUnionDiscriminator() }
            }

            else -> fields
        }

        appendLine("/**")
        description.split("\n").forEach {
            appendLine(" * $it")
        }
        trueFields.filter { it.description.isNotEmpty() }.forEach {
            appendLine(" * @param ${it.name.snakeToCamelCase()} ${it.description}")
        }
        appendLine(" */")
        appendLine("@Serializable")
        if (sealedParentName != null && sealedParentName.value !in unionTypesWithExplicitMarker) {
            val markerField = fields.firstOrNull { it.isUnionDiscriminator() }
            if (markerField != null) {
                val fieldDescription = markerField.description
                val unionMarkerValue = unionMarkerValueInDescriptionRe.find(fieldDescription)
                    ?.groupValues?.get(1)
                    ?: error("Can't find union marker value for ${name.value} in description: '$fieldDescription'")
                appendLine("@SerialName(\"${unionMarkerValue}\")")
            }
        }

        if (trueFields.isEmpty()) {
            appendLine {
                if (sealedParentName != null) {
                    append("data ") // https://kotlinlang.org/docs/object-declarations.html#data-objects
                }
                append("object ")
                append(name.value)
                if (sealedParentName != null) {
                    append(" : ${sealedParentName.value}()")
                }
            }
        } else {
            appendLine("data class ${name.value}(")

            for (field in trueFields) {
                appendFieldLine(name, field, true)
            }

            appendLine {
                append(")")
                if (sealedParentName != null) {
                    append(" : ${sealedParentName.value}()")
                }
            }
        }
    }

    private fun resolveFieldType(elementType: BotApiElementName, field: BotApiElement.Field): String {
        val specType = field.type.value
        val valueTyped = valueTypes.find { it.backingType == specType && it.fieldPredicate(elementType, field) }?.name ?: specType
        return fieldTypeSubstitutions[field.name] ?: valueTyped
    }

    private fun StringBuilder.appendFieldLine(elementType: BotApiElementName, field: BotApiElement.Field, isProperty: Boolean) {
        appendLine {
            append("    ")
            if (isProperty) {
                append("val ")
            }
            append(field.name.snakeToCamelCase())
            append(": ")
            val type = resolveFieldType(elementType, field)
            append(type)
            if (field.isOptional) {
                append("?")
            }
            if (field.defaultValue != null) {
                append(" = ${field.defaultValue}")
            }
            append(",")
        }
    }

    companion object {

        fun generateFromSpec(
            telegramApiHtmlSpec: String,
            outputDirectory: File,
            packageName: String,
            wrapperPackageName: String,
        ) {
            BotApiGenerator().run(
                telegramApiHtmlSpec,
                outputDirectory,
                packageName,
                wrapperPackageName,
            )
        }

        @JvmStatic
        fun main(args: Array<String>) {
            val specFile = args.firstOrNull()
                ?: error("Telegram Bot API HTML spec file is not specified as the first argument")
            val outputDirectory = args.getOrNull(1)
                ?: error("Output directory is not specified as the second argument")

            generateFromSpec(
                telegramApiHtmlSpec = File(specFile).readText(),
                outputDirectory = File(outputDirectory),
                packageName = "me.alllex.tbot.api.model",
                wrapperPackageName = "me.alllex.tbot.api.client",
            )
        }
    }

}
