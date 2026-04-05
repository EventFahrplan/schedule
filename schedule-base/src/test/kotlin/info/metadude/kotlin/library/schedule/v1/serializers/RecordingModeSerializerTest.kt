package info.metadude.kotlin.library.schedule.v1.serializers

import com.google.common.truth.Truth.assertThat
import info.metadude.kotlin.library.schedule.v1.models.RecordingMode
import info.metadude.kotlin.library.schedule.v1.models.RecordingMode.NOT_RECORDED_BY_DEFAULT
import info.metadude.kotlin.library.schedule.v1.models.RecordingMode.RECORDING_FORBIDDEN
import info.metadude.kotlin.library.schedule.v1.models.RecordingMode.RECORDING_NOT_POSSIBLE
import info.metadude.kotlin.library.schedule.v1.models.RecordingMode.RECORD_BY_DEFAULT
import info.metadude.kotlin.library.schedule.v1.models.RecordingMode.UNKNOWN
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonPrimitive
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

internal class RecordingModeSerializerTest {

    private val json = Json

    @ParameterizedTest
    @EnumSource(RecordingMode::class)
    fun `serialize returns corresponding string value`(mode: RecordingMode) {
        val expected = when (mode) {
            RECORD_BY_DEFAULT -> "record_by_default"
            NOT_RECORDED_BY_DEFAULT -> "not_recorded_by_default"
            RECORDING_FORBIDDEN -> "recording_forbidden"
            RECORDING_NOT_POSSIBLE -> "recording_not_possible"
            UNKNOWN -> "unknown"
        }
        val actual = json.encodeToJsonElement(RecordingModeSerializer, mode).jsonPrimitive.content
        assertThat(actual).isEqualTo(expected)
    }

    @ParameterizedTest
    @EnumSource(RecordingMode::class)
    fun `deserialize returns corresponding RecordingMode value`(expected: RecordingMode) {
        val wire = when (expected) {
            RECORD_BY_DEFAULT -> "record_by_default"
            NOT_RECORDED_BY_DEFAULT -> "not_recorded_by_default"
            RECORDING_FORBIDDEN -> "recording_forbidden"
            RECORDING_NOT_POSSIBLE -> "recording_not_possible"
            UNKNOWN -> "unknown"
        }
        val actual = json.decodeFromString(RecordingModeSerializer, "\"$wire\"")
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `deserialize maps unknown string to UNKNOWN`() {
        val actual = json.decodeFromString(RecordingModeSerializer, "\"invalid\"")
        assertThat(actual).isEqualTo(UNKNOWN)
    }
}
