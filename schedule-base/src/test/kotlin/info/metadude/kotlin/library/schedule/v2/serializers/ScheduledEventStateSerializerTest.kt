package info.metadude.kotlin.library.schedule.v2.serializers

import com.google.common.truth.Truth.assertThat
import info.metadude.kotlin.library.schedule.v2.models.ScheduledEventState
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.ValueSource

internal class ScheduledEventStateSerializerTest {

    private val json = Json

    @ParameterizedTest
    @EnumSource(ScheduledEventState::class)
    fun `serialize returns corresponding string value`(value: ScheduledEventState) {
        val expected = value.name
        val actual = json.encodeToJsonElement(ScheduledEventStateSerializer, value).jsonPrimitive.content
        assertThat(actual).isEqualTo(expected)
    }

    @ParameterizedTest
    @EnumSource(ScheduledEventState::class)
    fun `deserialize returns corresponding ScheduledEventState value`(expected: ScheduledEventState) {
        val wire = expected.name
        val actual = json.decodeFromJsonElement(ScheduledEventStateSerializer, JsonPrimitive(wire))
        assertThat(actual).isEqualTo(expected)
    }

    @ParameterizedTest
    @ValueSource(strings = ["invalid", "", "talk"])
    fun `deserialize maps unknown string to UNKNOWN`(wire: String) {
        val actual = json.decodeFromJsonElement(ScheduledEventStateSerializer, JsonPrimitive(wire))
        assertThat(actual).isEqualTo(ScheduledEventState.UNKNOWN)
    }
}
