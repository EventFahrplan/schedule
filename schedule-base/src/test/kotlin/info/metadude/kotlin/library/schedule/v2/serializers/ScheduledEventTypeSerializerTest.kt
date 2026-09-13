package info.metadude.kotlin.library.schedule.v2.serializers

import com.google.common.truth.Truth.assertThat
import info.metadude.kotlin.library.schedule.v2.models.ScheduledEventType
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.ValueSource

internal class ScheduledEventTypeSerializerTest {

    private val json = Json

    @ParameterizedTest
    @EnumSource(ScheduledEventType::class)
    fun `serialize returns corresponding string value`(value: ScheduledEventType) {
        val expected = value.name
        val actual = json.encodeToJsonElement(ScheduledEventTypeSerializer, value).jsonPrimitive.content
        assertThat(actual).isEqualTo(expected)
    }

    @ParameterizedTest
    @EnumSource(ScheduledEventType::class)
    fun `deserialize returns corresponding ScheduledEventType value`(expected: ScheduledEventType) {
        val wire = expected.name
        val actual = json.decodeFromJsonElement(ScheduledEventTypeSerializer, JsonPrimitive(wire))
        assertThat(actual).isEqualTo(expected)
    }

    @ParameterizedTest
    @ValueSource(strings = ["invalid", "", "talk"])
    fun `deserialize maps unknown string to UNKNOWN`(wire: String) {
        val actual = json.decodeFromJsonElement(ScheduledEventTypeSerializer, JsonPrimitive(wire))
        assertThat(actual).isEqualTo(ScheduledEventType.UNKNOWN)
    }
}
