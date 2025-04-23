package me.alllex.tbot.apigen

import java.io.File
import java.util.Map.entry

class BotApiGenerator {

    fun run(
        apiJsonFile: File,
        outputDirectory: File,
        packageName: String = "",
        clientPackageName: String = "",
    ) {
        val botApi = jsonSerialization.decodeFromString<BotApi>(apiJsonFile.readText())

        val allTypes = botApi.types
        println("Parsed ${allTypes.size} types")
        val allMethods = botApi.methods
        println("Parsed ${allMethods.size} methods")

        val unionTypes = collectUnionTypes(allTypes)
        val unionTypeParentByChild = unionTypes
            .flatMap { (parent, children) -> children.map { it to parent } }
            .toMap()

        val typesFileText = generateTypesFile(allTypes, unionTypeParentByChild, packageName)
        val requestTypesFileText = generateRequestTypesFile(allMethods, packageName)
        val listenerFileText = generateListenerFile(packageName, clientPackageName)

        val methodsSourceCodes = generateMethodsSourceCode(allMethods, packageName, clientPackageName)

        val modelDir = outputDirectory.resolve(packageName.replace(".", "/"))
        modelDir.mkdirs()

        modelDir.resolve("Types.kt").writeText(typesFileText)
        modelDir.resolve("RequestTypes.kt").writeText(requestTypesFileText)
        modelDir.resolve("TryRequestMethods.kt").writeText(methodsSourceCodes.tryRequestMethodSourceCode)
        modelDir.resolve("TryMethods.kt").writeText(methodsSourceCodes.tryMethodSourceCode)
        modelDir.resolve("TryWithContextMethods.kt").writeText(methodsSourceCodes.tryWithContextMethodSourceCode)
        modelDir.resolve("Methods.kt").writeText(methodsSourceCodes.methodSourceCode)
        modelDir.resolve("WithContextMethods.kt").writeText(methodsSourceCodes.withContextMethodSourceCode)

        val clientDir = outputDirectory.resolve(clientPackageName.replace(".", "/"))
            .apply { mkdirs() }

        clientDir.resolve("TelegramBotUpdateListener.kt").writeText(listenerFileText)
    }

    private fun generateListenerFile(modelPackageName: String, clientPackageName: String): String {
        val listenerTypeName = "TelegramBotUpdateListener"
        return buildString {
            appendLine("@file:Suppress(\"CONTEXT_RECEIVERS_DEPRECATED\")")
            appendLine()

            appendLine("package $clientPackageName")
            appendLine()

            appendLine("import $modelPackageName.*")
            appendLine()
            appendLine()

            appendLine("interface $listenerTypeName {")

            for (entry in updateListenerEntries) {
                appendLine()
                appendLine("    context(TelegramBotApiContext)")
                appendLine("    suspend fun on${entry.typeWithoutUpdate}(${entry.field}: ${entry.fieldType}) {}")
            }

            appendLine()
            appendLine("    context(TelegramBotApiContext)")
            appendLine("    suspend fun onUpdate(update: Update) {")
            appendLine("        when (update) {")
            for (entry in updateListenerEntries) {

                appendLine("            is ${entry.typeWithoutUpdate}Update -> on${entry.typeWithoutUpdate}(update.${entry.field})")
            }
            appendLine("            else -> error(\"Unknown update type: \${update::class.simpleName}\")")
            appendLine("        }")
            appendLine("    }")

            appendLine("}")

            appendLine()
            appendLine("fun $listenerTypeName(")
            for (entry in updateListenerEntries) {
                appendLine("    on${entry.typeWithoutUpdate}: TelegramBotUpdateHandler<${entry.fieldType}>? = null,")
            }
            appendLine("    @Suppress(\"UNUSED_PARAMETER\") noTrailingLambda: Unit = Unit,")
            appendLine("): $listenerTypeName {")
            appendLine("    return object : $listenerTypeName {")
            for (entry in updateListenerEntries) {
                appendLine()
                appendLine("        context(TelegramBotApiContext)")
                appendLine("        override suspend fun on${entry.typeWithoutUpdate}(${entry.field}: ${entry.fieldType}) =")
                appendLine("            on${entry.typeWithoutUpdate}?.handle(${entry.field}) ?: Unit")
            }
            appendLine("    }")
            appendLine("}")
        }
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

    private fun generateRequestTypesFile(
        methodElements: List<BotApiMethod>,
        packageName: String
    ): String = buildString {

        if (packageName.isNotEmpty()) {
            appendLine("package $packageName")
            appendLine()
        }
        appendLine("import kotlinx.serialization.Serializable")
        appendLine("import kotlinx.serialization.encodeToString")
        appendLine()
        appendLine()

        for (el in methodElements) {
            val requestTypeSourceCodeText = generateRequestTypeSourceCode(el.name, el.parameters)
            if (requestTypeSourceCodeText.isNotEmpty()) {
                appendLine(requestTypeSourceCodeText)
            }
        }
    }

    private fun generateRequestTypeSourceCode(
        elementName: BotApiElementName,
        parameters: List<BotApiElement.Field>,
    ): String = buildString {

        if (parameters.isNotEmpty()) {
            val requestTypeName = elementName.asMethodNameToRequestTypeName()

            appendLine("/**")
            appendLine(" * Request body for [${elementName.value}].")
            appendLine(" *")
            parameters.filter { it.description.isNotEmpty() }.forEach {
                appendLine(" * @param ${it.name} ${it.description}")
            }
            appendLine(" */")
            appendLine("@Serializable")
            appendLine("data class $requestTypeName(")
            for (parameter in parameters) {
                appendFieldLine(elementName, parameter, true)
            }
            appendLine(") {")
            appendLine("    ${generateDebugToString(requestTypeName, parameters)}")

            appendLine("}")
        }
    }

    private fun StringBuilder.header(packageName: String, wrapperPackageName: String, imports: List<String> = emptyList()) {
        if (packageName.isNotEmpty()) {
            appendLine("package $packageName")
        }
        appendLine()
        for (importStatement in imports) {
            appendLine(importStatement)
        }
        if (wrapperPackageName.isNotEmpty() && wrapperPackageName != packageName) {
            appendLine("import $wrapperPackageName.*")
        }
        appendLine()
        appendLine()
    }

    private fun generateMethodsSourceCode(
        methodElements: List<BotApiMethod>,
        packageName: String,
        wrapperPackageName: String
    ): MethodOverloadsSourceCode {

        val methodsSourceCodes = methodElements.flatMap { method ->
            val requestTypeName = method.name.asMethodNameToRequestTypeName()

            val variations = methodVariations.filter { it.methodName == method.name.value }

            if (variations.isEmpty()) {
                return@flatMap listOf(generateMethodSourceCode(method, method.name.value, requestTypeName, useArgNames = false))
            }

            variations.map { variation ->
                val variationMethod = method.copy(
                    parameters = method.parameters
                        .filterNot { it.serialName in variation.skipParams }
                        .map {
                            if (it.serialName in variation.requiredParams) it.copy(isOptional = false, defaultValue = null) else it
                        },
                    returnType = KotlinType(variation.returnType),
                    description = variation.descriptionSubstitutions.fold(method.description) { acc, replacement ->
                        acc.replace(replacement.first, replacement.second)
                    }
                )

                generateMethodSourceCode(variationMethod, variation.newMethodName, requestTypeName, useArgNames = true)
            }
        }

        val tryRequestMethodsSourceCode = buildString {
            header(packageName, wrapperPackageName)

            methodsSourceCodes.map { it.tryRequestMethodSourceCode }.filter { it.isNotEmpty() }
                .forEach { appendLine(it) }
        }

        val tryMethodsSourceCode = buildString {
            header(packageName, wrapperPackageName)
            methodsSourceCodes.map { it.tryMethodSourceCode }.filter { it.isNotEmpty() }
                .forEach { append(it) }
        }

        val tryWithContextMethodsSourceCode = buildString {
            appendLine("@file:Suppress(\"CONTEXT_RECEIVERS_DEPRECATED\")")
            appendLine()
            header(packageName, wrapperPackageName)
            methodsSourceCodes.map { it.tryWithContextMethodSourceCode }.filter { it.isNotEmpty() }
                .forEach { append(it) }
        }

        val methodsSourceCode = buildString {
            header(packageName, wrapperPackageName)
            methodsSourceCodes.map { it.methodSourceCode }.filter { it.isNotEmpty() }
                .forEach { append(it) }
        }

        val withContextMethodsSourceCode = buildString {
            appendLine("@file:Suppress(\"CONTEXT_RECEIVERS_DEPRECATED\")")
            appendLine()
            header(packageName, wrapperPackageName)
            methodsSourceCodes.map { it.withContextMethodSourceCode }.filter { it.isNotEmpty() }
                .forEach { append(it) }
        }

        return MethodOverloadsSourceCode(
            tryRequestMethodsSourceCode,
            tryMethodsSourceCode,
            tryWithContextMethodsSourceCode,
            methodsSourceCode,
            withContextMethodsSourceCode
        )
    }

    private fun StringBuilder.appendParameters(elementName: BotApiElementName, parameters: List<BotApiElement.Field>) {
        if (parameters.isNotEmpty()) {
            appendLine()
        }
        for (parameter in parameters) {
            appendFieldLine(elementName, parameter, isProperty = false)
        }
    }

    private fun StringBuilder.appendMethodDoc(description: String, parameters: List<BotApiElement.Field>) {
        appendLine("/**")
        appendKdocLines(description)
        if (parameters.isNotEmpty()) {
            appendLine(" *")
        }
        parameters.filter { it.description.isNotEmpty() }.forEach {
            appendLine(" * @param ${it.name} ${it.description}")
        }
        appendLine(" */")
    }

    data class MethodOverloadsSourceCode(
        val tryRequestMethodSourceCode: String,
        val tryMethodSourceCode: String,
        val tryWithContextMethodSourceCode: String,
        val methodSourceCode: String,
        val withContextMethodSourceCode: String,
    )

    private fun generateMethodSourceCode(
        method: BotApiMethod,
        methodBaseName: String,
        requestTypeName: String,
        useArgNames: Boolean
    ): MethodOverloadsSourceCode {

        val apiMethodName = method.name
        val description = method.description
        val parameters = method.parameters
        val returnType = method.returnType

        val tryMethodName = "try${methodBaseName.toTitleCase()}"

        val hasParams = parameters.isNotEmpty()

        val requestValueArg =
            if (!hasParams) ""
            else "${requestTypeName}(${parameters.joinToString { if (useArgNames) "${it.name} = ${it.name}" else it.name }})"

        // Core try-prefixed method that does the actual request
        val httpMethod = if (parameters.isEmpty()) "Get" else "Post"

        val tryRequestMethodSourceCode = buildString {
            appendLine("/**")
            appendKdocLines(description)
            appendLine(" */")
            append("suspend fun TelegramBotApiClient.$tryMethodName(")
            if (hasParams) {
                append("requestBody: $requestTypeName")
            }
            appendLine("): TelegramResponse<${returnType.value}> =")
            appendLine("    telegram$httpMethod(\"$apiMethodName\"${if (hasParams) ", requestBody" else ""})")
        }

        val tryMethodSourceCode = buildString {
            if (!hasParams) return@buildString

            appendLine()
            appendMethodDoc(description, parameters)
            append("suspend fun TelegramBotApiClient.$tryMethodName(")
            appendParameters(apiMethodName, parameters)
            appendLine("): TelegramResponse<${returnType.value}> =")
            appendLine("    $tryMethodName($requestValueArg)")
        }

        // Convenience try-prefixed method with context
        val tryWithContextMethodSourceCode = buildString {
            appendLine()
            appendMethodDoc(description, parameters)
            appendLine("context(TelegramBotApiContext)")
            append("suspend fun $tryMethodName(")
            appendParameters(apiMethodName, parameters)
            appendLine("): TelegramResponse<${returnType.value}> =")
            appendLine("    botApiClient.$tryMethodName($requestValueArg)")
        }

        // Convenience method
        val methodSourceCode = buildString {
            appendLine()
            appendMethodDoc(description, parameters)
            appendLine("@Throws(TelegramBotApiException::class)")
            append("suspend fun TelegramBotApiClient.$methodBaseName(")
            appendParameters(apiMethodName, parameters)
            appendLine("): ${returnType.value} =")
            appendLine("    $tryMethodName($requestValueArg).getResultOrThrow()")
        }

        // Convenience method with context
        val methodWithContextSourceCode = buildString {
            appendLine()
            appendMethodDoc(description, parameters)
            appendLine("context(TelegramBotApiContext)")
            appendLine("@Throws(TelegramBotApiException::class)")
            appendLine("@JvmName(\"call${methodBaseName.toTitleCase()}\")")
            append("suspend fun $methodBaseName(")
            appendParameters(apiMethodName, parameters)
            appendLine("): ${returnType.value} =")
            appendLine("    botApiClient.$tryMethodName($requestValueArg).getResultOrThrow()")
        }

        val fluentContextMethods = fluentMethods.filter { it.delegateName == methodBaseName }.map { fluent ->
            val unexpectedArgs = fluent.args.keys - parameters.map { it.name }.toSet()
            check(unexpectedArgs.isEmpty()) { "Unexpected replacement args for fluent method ${fluent.name}/$methodBaseName: $unexpectedArgs" }

            buildString {
                appendLine("context(TelegramBotApiContext)")
                appendLine("@Throws(TelegramBotApiException::class)")
                append("suspend fun ${fluent.receiver}.${fluent.name}(")
                appendParameters(apiMethodName, parameters.filter { it.name !in fluent.args })
                appendLine("): ${returnType.value} =")
                val adjustedRequestArg = parameters.map { it.name }.joinToString { fluent.args[it] ?: it }
                appendLine("    ${fluent.delegateName}($adjustedRequestArg)")
            }
        }

        val combinedMethodWithContextSourceCode = buildString {
            append(methodWithContextSourceCode)
            fluentContextMethods.forEach {
                appendLine()
                append(it)
            }
        }

        return MethodOverloadsSourceCode(
            tryRequestMethodSourceCode,
            tryMethodSourceCode,
            tryWithContextMethodSourceCode,
            methodSourceCode,
            combinedMethodWithContextSourceCode,
        )
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
            appendLine()
        }
        appendLine("import kotlinx.serialization.json.*")
        appendLine("import kotlinx.serialization.DeserializationStrategy")
        appendLine("import kotlinx.serialization.ExperimentalSerializationApi")
        appendLine("import kotlinx.serialization.SerialName")
        appendLine("import kotlinx.serialization.Serializable")
        appendLine("import kotlinx.serialization.encodeToString")
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
        valueType.docString?.let { appendKdoc(it) }
        appendLine("@Serializable")
        appendLine("@JvmInline")
        appendLine("value class ${valueType.name}(val value: ${valueType.backingType}) {")
        appendLine("    override fun toString(): String = \"${valueType.name}(\${quoteWhenWhitespace(value)})\"")
        appendLine("}")
    }

    private fun StringBuilder.generateSourceCodeForUpdate(updateTypeElement: BotApiElement) {
        val name = updateTypeElement.name.value
        val types = updateTypeElement.fields?.filter { it.isOptional } ?: emptyList()

        fun BotApiElement.Field.enumValue() = serialName.snakeToLoudSnakeCase()
        fun BotApiElement.Field.updateTypeName() = serialName.snakeToPascalCase() + "Update"

        appendLine("/**")
        appendLine(" * Type of updates Telegram Bot can receive.")
        appendLine(" */")
        appendLine("enum class ${name}Type {")
        for (updateField in types) {
            appendLine("    @SerialName(\"${updateField.serialName}\") ${updateField.enumValue()},")
        }
        appendLine("}")

        appendLine()
        appendLine("/**")
        updateTypeElement.description.split("\n")
            .filterNot { it.startsWith("At most one of the optional parameters") }
            .let { appendKdocLines(it) }
        appendLine(" *")
        appendLine(" * Sub-types:")
        for (updateType in types) {
            appendLine(" * - [${updateType.updateTypeName()}]")
        }
        appendLine(" */")
        appendLine("@Serializable(with = ${name}Serializer::class)")
        appendLine("sealed interface $name {")
        appendLine("    val updateId: Long")
        appendLine("    val updateType: UpdateType")
        appendLine("}")
        for (updateField in types) {
            appendLine()
            appendKdoc(updateField.description.removePrefix("Optional."))
            appendLine("@Serializable")
            appendLine("data class ${updateField.updateTypeName()}(")
            appendLine("    override val updateId: Long,")
            appendLine("    val ${updateField.name}: ${updateField.type},")
            appendLine("): $name {")
            appendLine("    override val updateType: UpdateType get() = UpdateType.${updateField.enumValue()}")
            appendLine("}")
        }

        appendLine()
        appendLine("object ${name}Serializer : JsonContentPolymorphicSerializer<$name>($name::class) {")
        appendLine("    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<$name> {")
        appendLine("        val json = element.jsonObject")
        appendLine("        return when {")
        for (updateField in types) {
            appendLine("            \"${updateField.serialName}\" in json -> ${updateField.updateTypeName()}.serializer()")
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
        appendKdocLines(description)
        for (unionType in unionTypes) {
            appendLine(" * - [${unionType.name.value}]")
        }
        appendLine(" */")

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

        appendLine("sealed interface ${name.value}")

        // TODO: add value-based discrimination
        if (discriminatorFieldName == null) {
            val avoidFields = setOf("description")
            val discriminatorFieldByType = unionTypes.associate { unionType ->
                val otherFieldNames = unionTypes
                    .filter { it != unionType }
                    .flatMap { it.fields ?: emptyList() }
                    .map { it.serialName }
                    .toSet()
                val discriminatorField = unionType.fields?.find { it.serialName !in otherFieldNames && it.serialName !in avoidFields }
                    ?: error("Failed to find discriminator field for type ${unionType.name}, with fields: ${unionType.fields?.map { it.serialName }}")
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
                appendLine("            \"${discriminatorField.serialName}\" in json -> ${unionType.name.value}.serializer()")
            }
            appendLine("            else -> error(\"Failed to deserialize an update type: \$json\")")
            appendLine("        }")
            appendLine("    }")
            appendLine("}")
        }
    }

    private fun generateContentfulTypeSourceCode(
        typeName: BotApiElementName,
        description: String,
        fields: List<BotApiElement.Field>,
        unionTypeParentByChild: Map<BotApiElementName, BotApiElementName>
    ): String = buildString {

        val sealedParentName = unionTypeParentByChild[typeName]

        // For sealed classes the "type" field will be automatically added by kotlinx.serialization
        // See: https://github.com/Kotlin/kotlinx.serialization/blob/master/docs/polymorphism.md#custom-subclass-serial-name
        val trueFields = when {
            sealedParentName != null && sealedParentName.value !in unionTypesWithExplicitMarker -> {
                fields.filterNot { it.isUnionDiscriminator() }
            }

            else -> fields
        }

        appendLine("/**")
        appendKdocLines(description)
        trueFields.filter { it.description.isNotEmpty() }.forEach {
            appendLine(" * @param ${it.name} ${it.description}")
        }
        appendLine(" */")
        appendLine("@Serializable")
        if (sealedParentName != null && sealedParentName.value !in unionTypesWithExplicitMarker) {
            val markerField = fields.firstOrNull { it.isUnionDiscriminator() }
            if (markerField != null) {
                val fieldDescription = markerField.description
                val unionMarkerValue = unionMarkerValueInDescriptionRe.find(fieldDescription)
                    ?.groupValues?.get(1)
                    ?: error("Can't find union marker value for ${typeName.value} in description: '$fieldDescription'")
                appendLine("@SerialName(\"${unionMarkerValue}\")")
            }
        }

        if (trueFields.isEmpty()) {
            // https://kotlinlang.org/docs/object-declarations.html#data-objects
            append("data object ")
            append(typeName.value)
            if (sealedParentName != null) {
                append(" : ${sealedParentName.value}")
            }
            appendLine()
        } else {
            appendLine("data class ${typeName.value}(")

            for (field in trueFields) {
                appendFieldLine(typeName, field, true)
            }

            append(")")
            if (sealedParentName != null) {
                append(" : ${sealedParentName.value}")
            }
            appendLine(" {")
            appendLine("    ${generateDebugToString(typeName.value, trueFields)}")
            appendLine("}")
        }
    }

    private fun generateDebugToString(typeName: String, fields: List<BotApiElement.Field>): String = buildString {
        append("override fun toString() = DebugStringBuilder(\"$typeName\")")
        append(fields.map { it.name }.joinToString("") { ".prop(\"$it\", $it)" })
        append(".toString()")
    }

    private fun resolveFieldType(elementType: BotApiElementName, field: BotApiElement.Field): String {
        val specType = field.type.value

        valueTypes.find { it.backingType == specType && it.fieldPredicate(elementType, field) }?.let {
            return it.name
        }

        return specType
    }

    private fun StringBuilder.appendFieldLine(elementType: BotApiElementName, field: BotApiElement.Field, isProperty: Boolean) {
        appendLine {
            append("    ")
            if (isProperty) {
                append("val ")
            }
            append(field.name)
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
}
