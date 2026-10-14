package info.metadude.kotlin.library.schedule.v2.serializers

import com.google.common.truth.Truth.assertThat
import info.metadude.kotlin.library.schedule.v2.models.LocalizableString.Text
import info.metadude.kotlin.library.schedule.v2.models.LocalizableString.Translations
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class LocalizableStringSerializerTest {

    private val json = Json

    @ParameterizedTest
    @ValueSource(strings = ["", "Hello", "Grüße", "A \"quote\"\nwith a newline"])
    fun `round trip preserves plain text`(text: String) {
        val value = Text(text)
        val wire = JsonPrimitive(text)
        assertThat(json.encodeToJsonElement(LocalizableStringSerializer, value)).isEqualTo(wire)
        assertThat(json.decodeFromJsonElement(LocalizableStringSerializer, wire)).isEqualTo(value)
    }

    @Test
    fun `round trip preserves translations`() {
        val value = Translations(mapOf("en" to "Hello", "de" to "Grüße"))
        val wire = json.parseToJsonElement("""{"en":"Hello","de":"Grüße"}""")
        assertThat(json.encodeToJsonElement(LocalizableStringSerializer, value)).isEqualTo(wire)
        assertThat(json.decodeFromJsonElement(LocalizableStringSerializer, wire)).isEqualTo(value)
    }

    @Test
    fun `round trip preserves empty translation map`() {
        val value = Translations(emptyMap())
        val wire = json.parseToJsonElement("{}")
        assertThat(json.encodeToJsonElement(LocalizableStringSerializer, value)).isEqualTo(wire)
        assertThat(json.decodeFromJsonElement(LocalizableStringSerializer, wire)).isEqualTo(value)
    }

    @ParameterizedTest
    @ValueSource(strings = ["[]", "[\"Hello\"]"])
    fun `deserialize rejects arrays`(wire: String) {
        assertThrows<SerializationException> {
            json.decodeFromString(LocalizableStringSerializer, wire)
        }
    }
}
