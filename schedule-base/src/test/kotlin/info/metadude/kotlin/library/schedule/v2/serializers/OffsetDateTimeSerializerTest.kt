package info.metadude.kotlin.library.schedule.v2.serializers

import com.google.common.truth.Truth.assertThat
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeParseException

internal class OffsetDateTimeSerializerTest {

    private val json = Json

    @ParameterizedTest
    @ValueSource(strings = ["2024-03-19T14:30+01:00", "2000-01-01T00:00Z", "2099-12-31T23:59:59.123456789-05:00"])
    fun `round trip preserves OffsetDateTime`(wire: String) {
        val value = OffsetDateTime.parse(wire)
        val encoded = json.encodeToJsonElement(OffsetDateTimeSerializer, value).jsonPrimitive.content
        assertThat(encoded).isEqualTo(wire)
        val decoded = json.decodeFromJsonElement(OffsetDateTimeSerializer, JsonPrimitive(wire))
        assertThat(decoded).isEqualTo(value)
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "invalid"])
    fun `deserialize rejects invalid OffsetDateTime`(wire: String) {
        assertThrows<DateTimeParseException> {
            json.decodeFromJsonElement(OffsetDateTimeSerializer, JsonPrimitive(wire))
        }
    }
}
