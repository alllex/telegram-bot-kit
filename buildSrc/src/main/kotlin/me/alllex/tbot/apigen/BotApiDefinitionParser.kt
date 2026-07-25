package me.alllex.tbot.apigen

import java.io.File
import kotlinx.serialization.Serializable
import me.alllex.parsus.parser.Grammar
import me.alllex.parsus.parser.ParseException
import me.alllex.parsus.parser.getOrElse
import me.alllex.parsus.parser.map
import me.alllex.parsus.parser.or
import me.alllex.parsus.parser.parser
import me.alllex.parsus.parser.repeatOneOrMore
import me.alllex.parsus.token.regexToken
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode
import org.slf4j.Logger
import org.slf4j.LoggerFactory


/**
 * Just use a different name for a type, because it clashes with something else
 */
private val typeRename = mapOf(
    "MessageId" to "MessageRef",
)

/**
 * Replace the original types with another type and do not generate a data class for the original type
 */
private val typeSubstitution = mapOf(
    "InaccessibleMessage" to "Message",
    "MaybeInaccessibleMessage" to "Message",
)

private val fieldTypeSubstitutionsByFieldName = mapOf(
    "allowed_updates" to "List<UpdateType>",
    "parse_mode" to "ParseMode",
)

fun resolveElementTypeName(name: String): String {
    return typeSubstitution[name] ?: typeRename[name] ?: name
}

fun resolveFieldTypeName(serialFieldName: String, serialFieldType: String): String {
    return fieldTypeSubstitutionsByFieldName[serialFieldName] ?: serialFieldType
}

data class ApiEntityDefinition(
    val nameHeaderElement: Element,
    val descriptionElements: List<Element> = emptyList(),
    val fieldsTableElement: Element? = null,
    val unionTypesListElement: Element? = null,
)

data class ApiMethodDefinition(
    val nameHeaderElement: Element,
    val descriptionElements: List<Element> = emptyList(),
    val parametersTableElement: Element? = null,
)

@Serializable
@JvmInline
value class BotApiElementName(val value: String) {
    init {
        require(regex.matches(value)) {
            "Invalid Bot API element name: '$value'. Should this section be ignored? See `availableTypesIgnoredSections`"
        }
    }

    override fun toString(): String = value

    companion object {
        private val regex = Regex("[a-zA-Z][a-zA-Z0-9]*")
    }
}

@Serializable
@JvmInline
value class KotlinType(val value: String) {
    override fun toString(): String = value
}

@Serializable
data class BotApiElement(
    val name: BotApiElementName,
    val description: String,
    val fields: List<Field>? = null,
    val unionTypes: List<BotApiElementName>? = null,
    val originalName: BotApiElementName = name,
) {
    @Serializable
    data class Field(
        val serialName: String,
        val description: String,
        val type: KotlinType,
        val isOptional: Boolean,
        val defaultValue: String?
    ) {
        val name = serialName.snakeToCamelCase()
    }
}

@Serializable
data class BotApiMethod(
    val name: BotApiElementName,
    val description: String,
    val parameters: List<BotApiElement.Field>,
    val returnType: KotlinType
)

@Serializable
data class BotApi(
    val types: List<BotApiElement>,
    val methods: List<BotApiMethod>,
)

val availableTypesIgnoredSections: Set<String> = setOf(
    "InputFile",
    "Sending files",
    "Inline mode objects",
    "Determining list of commands",
    "Formatting options",
    "Accent colors",
    "Profile accent colors",
    "Inline mode methods",
    "Paid Broadcasts",
    "Ephemeral Messages and Commands",
    "Rich Message Formatting Options"
)

val topLevelSections = listOf(
    "Getting updates",
    "Available types",
    "Available methods",
    "Updating messages",
    "Stickers",
    "Rich messages",
    "Inline mode",
    "Payments",
    "Telegram Passport",
    "Games",
)

/**
 * Top-level sections that are prose rather than type or method definitions.
 *
 * Every top-level section must appear either here or in [topLevelSections]: an unlisted section
 * is an error rather than something to skip, so that a new section in the spec cannot silently
 * drop its definitions from the generated API.
 */
val ignoredTopLevelSections = setOf(
    "Recent changes",
    "Authorizing your bot",
    "Making requests",
    "Using a Local Bot API Server",
)

/**
 * Sealed type names for unions the spec spells out as a list of members.
 *
 * Keyed by the set of member names so that ordering and separator style ("or" vs ", ... and")
 * do not matter. The name is not derivable from the members — `ReplyMarkup` never appears in
 * the union text that defines it — so it has to be stated here.
 */
val unionTypeNames: Map<Set<String>, String> = mapOf(
    setOf("InlineKeyboardMarkup", "ReplyKeyboardMarkup", "ReplyKeyboardRemove", "ForceReply") to "ReplyMarkup",
    setOf("InputMediaAudio", "InputMediaDocument", "InputMediaPhoto", "InputMediaVideo") to "InputMedia",
    // Bot API 10.2 added InputMediaLivePhoto to the union.
    setOf(
        "InputMediaAudio", "InputMediaDocument", "InputMediaLivePhoto", "InputMediaPhoto", "InputMediaVideo"
    ) to "InputMedia",
    // The `media` field of InputRichMessageMedia is an anonymous union in the spec: it has no name
    // to generate a sealed type from, so one is chosen here, matching how ReplyMarkup is named.
    setOf(
        "InputMediaAnimation", "InputMediaAudio", "InputMediaPhoto", "InputMediaVideo", "InputMediaVoiceNote"
    ) to "InputRichMedia",
)

val responseTypeRegexes = listOf(
    Regex("An (.+) objects is returned"),
    Regex("Returns (\\w+) on success"),
    Regex("[Rr]eturns an? (.+?) object"),
    Regex("in form of a (\\w+) object"),
    Regex("[Oo]n success, an (.+?) of the sent messages is returned"),
    Regex("[Oo]n success, (\\w+) is returned"),
    Regex("[Oo]n success, the sent (\\w+) is returned"),
    Regex("[Oo]n success, an? (.+) objects?"),
    Regex("On success, an (.+) that were"),
    Regex("On success, the stopped (\\w+) with the final results is returned"),
    Regex("On success, if [^,]+, the (\\w+) is returned"),
    Regex("the \\w+ (\\w+) is returned"),
    Regex("Returns the (\\w+) of the"),
    Regex("returns the edited (\\w+),"),
    Regex("Returns the uploaded (\\w+) on success"),
    Regex("Returns the (?:new invite link|created invoice link) as (String) on success"),
    Regex("Returns the (?:new )?token as (String) on success"),
    Regex("Returns information about the created topic as a (\\w+) object"),
    Regex("Returns (.+) on success"),
    Regex("as (\\w+) on success"),
    Regex("invite link as (?:a )?(\\w+) object"),
)

fun findResponseTypeFromDescription(description: String): String {
    responseTypeRegexes.forEach { regex ->
        regex.find(description)?.groupValues?.get(1)?.let { return it }
    }
    error("Type not found in description:\n____\n${description}\n____")
}

fun Element.isH1() = tagName() == "h1"
fun Element.isH2() = tagName() == "h2"
fun Element.isH3() = tagName() == "h3"
fun Element.isH4() = tagName() == "h4"

private val implicitReplyMarkupElement = BotApiElement(
    BotApiElementName("ReplyMarkup"),
    description = "This object represents an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards).",
    unionTypes = listOf("InlineKeyboardMarkup", "ReplyKeyboardMarkup", "ReplyKeyboardRemove", "ForceReply").map {
        BotApiElementName(it)
    }
)

/**
 * The `media` field of `InputRichMessageMedia` is an anonymous union in the spec, so there is no
 * definition to parse it from. Declared here the same way as [implicitReplyMarkupElement].
 */
private val implicitInputRichMediaElement = BotApiElement(
    BotApiElementName("InputRichMedia"),
    description = "Media that can be embedded in an outgoing rich message.",
    unionTypes = listOf(
        "InputMediaAnimation", "InputMediaAudio", "InputMediaPhoto", "InputMediaVideo", "InputMediaVoiceNote"
    ).map { BotApiElementName(it) }
)

class BotApiDefinitionParser {

    private val logger: Logger = LoggerFactory.getLogger(BotApiDefinitionParser::class.java)

    fun run(
        originalHtml: File,
        argListsFile: File?,
    ): BotApi {

        val doc = Jsoup.parse(originalHtml.readText())

        // Telegram renders emoji as <img class="emoji" alt="🎲">. Jsoup's text() ignores the alt
        // attribute, so emoji would be dropped from descriptions entirely. Substituting the alt
        // text into the DOM before any text() call both preserves the emoji and makes the output
        // independent of how a given jsoup version spaces replaced inline elements.
        for (emojiImg in doc.select("img.emoji")) {
            emojiImg.replaceWith(TextNode(emojiImg.attr("alt")))
        }

        val devPageContent = doc.select("#dev_page_content").firstOrNull()
            ?: error("Could not find #dev_page_content")

        val argLists = argListsFile?.takeIf { it.exists() }?.let {
            jsonSerialization.decodeFromString<BotApiArgLists>(it.readText())
        }

        val contentEls = devPageContent.children()

        val h3Sections =
            contentEls.selectSections(startsSection = Element::isH3, stopsSequence = { it.isH1() || it.isH2() })

        val unknownSections = h3Sections
            .map { it.first().ownText() }
            .filter { it !in topLevelSections && it !in ignoredTopLevelSections }

        check(unknownSections.isEmpty()) {
            "Unrecognized top-level section(s) in the Bot API spec: " +
                unknownSections.joinToString { "'$it'" } + ". " +
                "Add each to `topLevelSections` if it contains type or method definitions, " +
                "or to `ignoredTopLevelSections` if it is prose. " +
                "Leaving a section unlisted would silently drop its definitions from the generated API."
        }

        val groupSections = h3Sections.filter { it.first().ownText() in topLevelSections }
        logger.info("Found ${groupSections.size} top-level sections")

        val (typeDefSections, methodDefSections) = groupSections.flatMap {
            it.selectSections(startsSection = Element::isH4)
                .filter { sectionEls -> sectionEls.first().ownText() !in availableTypesIgnoredSections }
        }.partition { sectionEls ->
            sectionEls.first().ownText().first().isUpperCase()
        }

        logger.info("Found ${typeDefSections.size} type definition sections")
        logger.info("Found ${methodDefSections.size} method definition sections")

        val typeDefinitions = typeDefSections.map(::parseElementDefinition)

        @Suppress("RemoveExplicitTypeArguments")
        val types = buildList<BotApiElement> {
            this += typeDefinitions.map(::parseElement).filter { it.originalName.value !in typeSubstitution }
            this += implicitReplyMarkupElement
            if (any { it.name.value == "InputRichMessageMedia" }) {
                this += implicitInputRichMediaElement
            }
        }

        val typesWithSortedFields = if (argLists == null) types else types.toSortedFields(argLists)

        val methodDefinitions = methodDefSections.map(::parseMethodDefinition)
        val methods = methodDefinitions.map(this::parseMethod)

        val methodsWithSortedParams = if (argLists == null) methods else methods.toSortedMethodParameters(argLists)

        return BotApi(typesWithSortedFields, methodsWithSortedParams)
    }

    private fun List<BotApiElement>.toSortedFields(argLists: BotApiArgLists) =
        map { t ->
            val prevFields = argLists.types.find { it.name == t.name.value }?.args
            val newFields = t.fields
            if (newFields == null || prevFields == null) t else t.copy(fields = newFields.toSortedFields(prevFields))
        }

    private fun List<BotApiMethod>.toSortedMethodParameters(argLists: BotApiArgLists): List<BotApiMethod> =
        map { m ->
            val prevParams = argLists.methods.find { it.name == m.name.value }?.args
            if (prevParams == null) m else m.copy(parameters = m.parameters.toSortedFields(prevParams))
        }

    private fun List<BotApiElement.Field>.toSortedFields(knownNames: List<String>) =
        sortedBy { p -> knownNames.indexOf(p.name).let { if (it == -1) knownNames.size else it } }

    private fun parseElementDefinition(elementSection: List<Element>): ApiEntityDefinition {
        // [h4, (description elements), (<table> or union types)?, (description elements)?]
        val header = elementSection.first()
        require(header.isH4()) {
            "Expected <h4>, but got ${header.tagName()}"
        }

        val tableIx = elementSection.indexOfFirst { it.tagName() == "table" }
        val unionTypesIx = elementSection.indexOfFirst { it.tagName() == "ul" }

        val firstNonDescriptionIx = listOf(tableIx, unionTypesIx, elementSection.size).filter { it >= 0 }.min()
        val descriptionEls = elementSection.subList(1, firstNonDescriptionIx)

        return ApiEntityDefinition(
            nameHeaderElement = header,
            descriptionElements = descriptionEls,
            fieldsTableElement = tableIx.takeIf { it >= 0 }?.let { elementSection[it] },
            unionTypesListElement = unionTypesIx.takeIf { it >= 0 }?.let { elementSection[it] }
        )
    }

    private fun parseMethodDefinition(methodSection: List<Element>): ApiMethodDefinition {
        // [h4, (description elements), (<table> or union types)?, (description elements)?]
        val header = methodSection.first()
        require(header.isH4()) {
            "Expected <h4>, but got ${header.tagName()}"
        }

        val tableIx = methodSection.indexOfFirst { it.tagName() == "table" }

        val firstNonDescriptionIx = listOf(tableIx, methodSection.size).filter { it >= 0 }.min()
        val descriptionEls = methodSection.subList(1, firstNonDescriptionIx)

        return ApiMethodDefinition(
            nameHeaderElement = header,
            descriptionElements = descriptionEls,
            parametersTableElement = tableIx.takeIf { it >= 0 }?.let { methodSection[it] },
        )
    }

    private fun parseElement(elementDef: ApiEntityDefinition): BotApiElement {
        val initialName = elementDef.nameHeaderElement.ownText()
        val name = BotApiElementName(resolveElementTypeName(initialName))
        val description = elementDef.descriptionElements.joinToString("\n\n") { it.text() }
        val fields = elementDef.fieldsTableElement?.let { parseElementFields(it) }
            ?.sortedBy { it.isOptional }
        val unionTypes = elementDef.unionTypesListElement?.let { parseUnionTypes(it) }
        val finalFields = if (unionTypes == null && fields == null) emptyList() else fields
        return BotApiElement(name, description, finalFields, unionTypes, BotApiElementName(initialName))
    }

    private fun parseMethod(methodDef: ApiMethodDefinition): BotApiMethod {
        val initialName = methodDef.nameHeaderElement.ownText()
        val name = BotApiElementName(resolveElementTypeName(initialName))
        val description = methodDef.descriptionElements.joinToString("\n\n") { it.text() }
        val parameters = (methodDef.parametersTableElement?.let { parseMethodParameters(it) } ?: emptyList())
            .sortedBy { it.isOptional }
        val returnType = parseMethodReturnType(description)
        return BotApiMethod(name, description, parameters, returnType)
    }

    private fun parseMethodReturnType(methodDescriptionText: String): KotlinType {
        val returnTypeText = findResponseTypeFromDescription(methodDescriptionText)
        val typeFieldInfo = try {
            serialTypeToKotlinTypeString(returnTypeText, isOptional = false)
        } catch (e: Exception) {
            throw RuntimeException(
                "Failed to parse return type from '$returnTypeText' in description:\n---\n$methodDescriptionText\n---\n",
                e
            )
        }
        return typeFieldInfo.kotlinType
    }

    private fun parseUnionTypes(unionTypesEl: Element): List<BotApiElementName> {
        require(unionTypesEl.tagName() == "ul") {
            "Expected <ul>, but got ${unionTypesEl.tagName()}"
        }

        val childrenClasses = unionTypesEl.children().map {
            BotApiElementName(resolveElementTypeName(it.text()))
        }
        check(childrenClasses.all { it.value.isNotEmpty() }) {
            "Got empty union type name for ${unionTypesEl.html()}"
        }

        return childrenClasses
    }

    private fun parseElementFields(tableEl: Element): List<BotApiElement.Field> {
        require(tableEl.tagName() == "table") {
            "Expected <table>, but got ${tableEl.tagName()}"
        }

        val header = tableEl.firstElementChild()!!
        val headerRow = header.firstElementChild()!!
        check(headerRow.childrenSize() == 3) {
            "Expected 3 columns in table header, but got ${headerRow.childrenSize()}"
        }
        check(headerRow.firstElementChild()!!.text() == "Field") {
            "Expected first column in table header to be 'Field', but got ${headerRow.firstElementChild()!!.text()}"
        }

        val body = tableEl.child(1)

        val fields = mutableListOf<BotApiElement.Field>()

        for (row in body.children()) {
            val nameEl = row.child(0)
            val typeEl = row.child(1)
            val descriptionEl = row.child(2)
            val description = descriptionEl.text().trim()
            val isOptional = description.startsWith("Optional")
            val serialFieldName = nameEl.text().trim()
            val typeText = typeEl.text().trim()
            val typeFieldInfo = serialTypeToKotlinTypeString(typeText, isOptional, serialFieldName)

            fields.add(
                BotApiElement.Field(
                    serialFieldName,
                    description,
                    typeFieldInfo.kotlinType,
                    isOptional,
                    typeFieldInfo.defaultValue
                )
            )
        }

        return fields
    }

    private fun parseMethodParameters(tableEl: Element): List<BotApiElement.Field> {
        require(tableEl.tagName() == "table") {
            "Expected <table>, but got ${tableEl.tagName()}"
        }

        val header = tableEl.firstElementChild()!!
        val headerRow = header.firstElementChild()!!
        require(headerRow.childrenSize() == 4) {
            "Expected 4 columns in table header, but got ${headerRow.childrenSize()}"
        }
        check(headerRow.firstElementChild()!!.text() == "Parameter") {
            "Expected first column in table header to be 'Parameter', but got ${headerRow.firstElementChild()!!.text()}"
        }

        val body = tableEl.child(1)

        val fields = mutableListOf<BotApiElement.Field>()

        for (row in body.children()) {
            val nameEl = row.child(0)
            val typeEl = row.child(1)
            val requiredEl = row.child(2)
            val descriptionEl = row.child(3)
            val isOptional = requiredEl.text().startsWith("Optional")
            val serialFieldName = nameEl.text().trim()
            val typeText = typeEl.text().trim()
            val typeFieldInfo = serialTypeToKotlinTypeString(typeText, isOptional, serialFieldName)
            val description = descriptionEl.text().trim()

            fields.add(
                BotApiElement.Field(
                    serialFieldName,
                    description,
                    typeFieldInfo.kotlinType,
                    isOptional,
                    typeFieldInfo.defaultValue
                )
            )
        }

        return fields
    }

    private data class ResolvedTypeInfo(val kotlinType: KotlinType, val defaultValue: String?)

    private fun serialTypeToKotlinTypeString(
        serialType: String,
        isOptional: Boolean,
        serialFieldName: String? = null
    ): ResolvedTypeInfo {
        val result = FieldTypeGrammar.parse(serialType)

        val parsedType = result.getOrElse {
            throw RuntimeException("Could not parse field type: '$serialType'", ParseException(it))
        }

        val resolvedType = serialFieldName?.let { resolveFieldTypeName(it, parsedType) } ?: parsedType

        val defaultValue = if (isOptional) "null" else null
        return ResolvedTypeInfo(KotlinType(resolvedType), defaultValue)
    }

}

@Suppress("MemberVisibilityCanBePrivate")
object FieldTypeGrammar : Grammar<String>(ignoreCase = true) {
    init {
        regexToken("\\s+", ignored = true)
    }

    val arrayOf by regexToken("Array of")
    val int by regexToken("Integer or String|Integer") map { "Long" }
    val double by regexToken("Float number|Float") map { "Double" }
    val boolean by regexToken("Boolean|False|True") map { "Boolean" }
    val string by regexToken("InputFile or String|InputFile") map { "String" }
    val message by regexToken("Messages") map { "Message" } // typo in the original HTML spec

    // Unions are matched structurally ("A or B or C", "A, B and C") rather than as fixed literals,
    // so adding a member to an existing union no longer breaks parsing. The resulting sealed type
    // name cannot be derived from the members, though, so it is looked up in [unionTypeNames]:
    // "InlineKeyboardMarkup or ReplyKeyboardMarkup or ..." has to become `ReplyMarkup`, a name that
    // appears nowhere in the spec text. An unknown combination is an error rather than a guess.
    val unionType by regexToken("\\w+(?:(?:,| and| or) \\w+)+") map { token ->
        val members = token.text.split(",", " and ", " or ")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
        unionTypeNames[members.toSet()]
            ?: error(
                "Unrecognized union type: '${token.text}'. " +
                    "Add its members to `unionTypeNames` mapped to the sealed type they represent."
            )
    }

    val apiType by regexToken("\\w+") map { resolveElementTypeName(it.text) }

    val simpleType by int or double or boolean or string or message or unionType or apiType
    val listType by parser {
        val listDepth = repeatOneOrMore(arrayOf).size
        val atom = simpleType()
        "List<".repeat(listDepth) + atom + ">".repeat(listDepth)
    }

    val fieldType by listType or simpleType

    override val root by fieldType
}

private fun <T> List<T>.selectSections(
    startsSection: (T) -> Boolean,
    stopsSequence: (T) -> Boolean = { false }
): List<List<T>> {
    val result = mutableListOf<MutableList<T>>()
    var subList: MutableList<T>? = null

    for (item in this) {
        when {
            stopsSequence(item) -> break
            startsSection(item) -> {
                subList?.let(result::add)
                subList = mutableListOf(item)
            }

            else -> subList?.add(item)
        }
    }

    subList?.let(result::add)

    return result
}
