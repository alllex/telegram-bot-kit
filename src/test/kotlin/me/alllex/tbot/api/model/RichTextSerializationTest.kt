package me.alllex.tbot.api.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test

/**
 * `RichText` is a union of a bare JSON string, a JSON array and 25 object variants, which needs a
 * hand-written serializer. The object variants are covered by the generic machinery; these tests
 * exist because the string and array forms are the ones that a purely object-based serializer
 * would fail on at runtime rather than at build time.
 */
class RichTextSerializationTest {

    private val json = Json

    @Test
    fun deserializesBareStringAsPlainText() {
        val result = json.decodeFromString<RichText>("\"hello world\"")

        assertThat(result).isInstanceOf(RichTextPlain::class)
        assertThat((result as RichTextPlain).value).isEqualTo("hello world")
    }

    @Test
    fun serializesPlainTextAsBareString() {
        val encoded = json.encodeToString<RichText>(RichTextPlain("hello world"))

        assertThat(encoded).isEqualTo("\"hello world\"")
    }

    @Test
    fun roundTripsPlainText() {
        val original: RichText = RichTextPlain("plain")

        val decoded = json.decodeFromString<RichText>(json.encodeToString(original))

        assertThat(decoded).isEqualTo(original)
    }

    @Test
    fun deserializesArrayAsSequence() {
        val result = json.decodeFromString<RichText>("""["one","two"]""")

        assertThat(result).isInstanceOf(RichTextSequence::class)
        assertThat((result as RichTextSequence).value).isEqualTo(
            listOf(RichTextPlain("one"), RichTextPlain("two"))
        )
    }

    @Test
    fun serializesSequenceAsArray() {
        val value: RichText = RichTextSequence(listOf(RichTextPlain("one"), RichTextPlain("two")))

        assertThat(json.encodeToString(value)).isEqualTo("""["one","two"]""")
    }

    @Test
    fun deserializesNestedArrays() {
        val result = json.decodeFromString<RichText>("""["a",["b",["c"]]]""")

        val expected = RichTextSequence(
            listOf(
                RichTextPlain("a"),
                RichTextSequence(
                    listOf(
                        RichTextPlain("b"),
                        RichTextSequence(listOf(RichTextPlain("c"))),
                    )
                ),
            )
        )
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun roundTripsNestedArrays() {
        val original: RichText = RichTextSequence(
            listOf(RichTextPlain("a"), RichTextSequence(listOf(RichTextPlain("b"))))
        )

        val decoded = json.decodeFromString<RichText>(json.encodeToString(original))

        assertThat(decoded).isEqualTo(original)
    }

    @Test
    fun deserializesBoldObjectVariant() {
        val result = json.decodeFromString<RichText>("""{"type":"bold","text":"loud"}""")

        assertThat(result).isInstanceOf(RichTextBold::class)
        assertThat((result as RichTextBold).text).isEqualTo(RichTextPlain("loud"))
    }

    @Test
    fun deserializesUrlObjectVariant() {
        val result = json.decodeFromString<RichText>(
            """{"type":"url","text":"click","url":"https://example.com"}"""
        )

        assertThat(result).isInstanceOf(RichTextUrl::class)
        val url = result as RichTextUrl
        assertThat(url.text).isEqualTo(RichTextPlain("click"))
        assertThat(url.url).isEqualTo("https://example.com")
    }

    @Test
    fun roundTripsObjectVariant() {
        val original: RichText = RichTextBold("bold", RichTextPlain("loud"))

        val decoded = json.decodeFromString<RichText>(json.encodeToString(original))

        assertThat(decoded).isEqualTo(original)
    }

    @Test
    fun deserializesObjectVariantNestedInsideArray() {
        val result = json.decodeFromString<RichText>("""["plain",{"type":"bold","text":"loud"}]""")

        assertThat(result).isEqualTo(
            RichTextSequence(listOf(RichTextPlain("plain"), RichTextBold("bold", RichTextPlain("loud"))))
        )
    }

    @Test
    fun deserializesRecursivelyNestedObjectVariants() {
        val result = json.decodeFromString<RichText>(
            """{"type":"bold","text":{"type":"italic","text":"both"}}"""
        )

        assertThat(result).isEqualTo(RichTextBold("bold", RichTextItalic("italic", RichTextPlain("both"))))
    }
}
