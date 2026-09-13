package info.metadude.kotlin.library.schedule.v2.serializers

import com.google.common.truth.Truth.assertThat
import info.metadude.kotlin.library.schedule.ScheduleJson
import info.metadude.kotlin.library.schedule.v2.models.ScheduleState
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.ValueSource

internal class ScheduleStateSerializerTest {

    private val json = ScheduleJson.create()

    @ParameterizedTest
    @EnumSource(ScheduleState::class)
    fun `serialize returns corresponding string value`(value: ScheduleState) {
        val expected = value.name
        val actual = json.encodeToJsonElement(ScheduleStateSerializer, value).jsonPrimitive.content
        assertThat(actual).isEqualTo(expected)
    }

    @ParameterizedTest
    @EnumSource(ScheduleState::class)
    fun `deserialize returns corresponding ScheduleState value`(expected: ScheduleState) {
        val wire = expected.name
        val actual = json.decodeFromJsonElement(ScheduleStateSerializer, JsonPrimitive(wire))
        assertThat(actual).isEqualTo(expected)
    }

    @ParameterizedTest
    @ValueSource(strings = ["invalid", "", "draft", "Published"])
    fun `deserialize maps unknown string to UNKNOWN`(wire: String) {
        val actual = json.decodeFromJsonElement(ScheduleStateSerializer, JsonPrimitive(wire))
        assertThat(actual).isEqualTo(ScheduleState.UNKNOWN)
    }
}
