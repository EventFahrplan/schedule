package info.metadude.kotlin.library.schedule.v2.serializers

import com.google.common.truth.Truth.assertThat
import info.metadude.kotlin.library.schedule.ScheduleJson
import info.metadude.kotlin.library.schedule.v2.models.EventState
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.ValueSource

internal class EventStateSerializerTest {

    private val json = ScheduleJson.create()

    @ParameterizedTest
    @EnumSource(EventState::class)
    fun `serialize returns corresponding string value`(value: EventState) {
        val expected = value.name
        val actual = json.encodeToJsonElement(EventStateSerializer, value).jsonPrimitive.content
        assertThat(actual).isEqualTo(expected)
    }

    @ParameterizedTest
    @EnumSource(EventState::class)
    fun `deserialize returns corresponding EventState value`(expected: EventState) {
        val wire = expected.name
        val actual = json.decodeFromJsonElement(EventStateSerializer, JsonPrimitive(wire))
        assertThat(actual).isEqualTo(expected)
    }

    @ParameterizedTest
    @ValueSource(strings = ["invalid", "", "talk"])
    fun `deserialize maps unknown string to UNKNOWN`(wire: String) {
        val actual = json.decodeFromJsonElement(EventStateSerializer, JsonPrimitive(wire))
        assertThat(actual).isEqualTo(EventState.UNKNOWN)
    }
}
