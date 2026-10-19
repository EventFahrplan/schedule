package info.metadude.kotlin.library.schedule.v2.serializers

import com.google.common.truth.Truth.assertThat
import info.metadude.kotlin.library.schedule.ScheduleJson
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.JsonDecodingException
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.threeten.bp.OffsetDateTime

internal class OffsetDateTimeSerializerTest {

    private val json = ScheduleJson.create()

    @ParameterizedTest
    @ValueSource(strings = ["2024-03-19T14:30+01:00", "2000-01-01T00:00Z", "2099-12-31T23:59:59.123456789-05:00"])
    fun `round trip preserves OffsetDateTime`(wire: String) {
        val value = OffsetDateTime.parse(wire)
        val encoded = json.encodeToJsonElement(OffsetDateTimeSerializer, value).jsonPrimitive.content
        assertThat(encoded).isEqualTo(wire)
        val decoded = json.decodeFromJsonElement(OffsetDateTimeSerializer, JsonPrimitive(wire))
        assertThat(decoded).isEqualTo(value)
    }

    @OptIn(ExperimentalSerializationApi::class)
    @ParameterizedTest
    @ValueSource(strings = ["", "invalid"])
    fun `deserialize rejects invalid OffsetDateTime`(wire: String) {
        assertThrows<JsonDecodingException> {
            json.decodeFromJsonElement(OffsetDateTimeSerializer, JsonPrimitive(wire))
        }
    }
}
