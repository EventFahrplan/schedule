package info.metadude.kotlin.library.schedule.v2.serializers

import com.google.common.truth.Truth.assertThat
import info.metadude.kotlin.library.schedule.v2.models.RecordingMode
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.ValueSource

internal class RecordingModeSerializerTest {

    private val json = Json

    @ParameterizedTest
    @EnumSource(RecordingMode::class)
    fun `serialize returns corresponding string value`(value: RecordingMode) {
        val expected = value.name
        val actual = json.encodeToJsonElement(RecordingModeSerializer, value).jsonPrimitive.content
        assertThat(actual).isEqualTo(expected)
    }

    @ParameterizedTest
    @EnumSource(RecordingMode::class)
    fun `deserialize returns corresponding RecordingMode value`(expected: RecordingMode) {
        val wire = expected.name
        val actual = json.decodeFromJsonElement(RecordingModeSerializer, JsonPrimitive(wire))
        assertThat(actual).isEqualTo(expected)
    }

    @ParameterizedTest
    @ValueSource(strings = ["invalid", "", "talk"])
    fun `deserialize maps unknown string to UNKNOWN`(wire: String) {
        val actual = json.decodeFromJsonElement(RecordingModeSerializer, JsonPrimitive(wire))
        assertThat(actual).isEqualTo(RecordingMode.UNKNOWN)
    }
}
