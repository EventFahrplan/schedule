package info.metadude.kotlin.library.schedule.v2.serializers

import com.google.common.truth.Truth.assertThat
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeParseException

internal class LocalDateSerializerTest {

    private val json = Json

    @ParameterizedTest
    @ValueSource(strings = ["2024-03-19", "2000-02-29", "2099-12-31"])
    fun `round trip preserves LocalDate`(wire: String) {
        val value = LocalDate.parse(wire)
        val encoded = json.encodeToJsonElement(LocalDateSerializer, value).jsonPrimitive.content
        assertThat(encoded).isEqualTo(wire)
        val decoded = json.decodeFromJsonElement(LocalDateSerializer, JsonPrimitive(wire))
        assertThat(decoded).isEqualTo(value)
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "invalid"])
    fun `deserialize rejects invalid LocalDate`(wire: String) {
        assertThrows<DateTimeParseException> {
            json.decodeFromJsonElement(LocalDateSerializer, JsonPrimitive(wire))
        }
    }
}
