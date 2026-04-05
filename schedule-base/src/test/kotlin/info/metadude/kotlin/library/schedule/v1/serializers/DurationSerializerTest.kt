package info.metadude.kotlin.library.schedule.v1.serializers

import com.google.common.truth.Truth.assertThat
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonPrimitive
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import org.threeten.bp.Duration

internal class DurationSerializerTest {

    private val json = Json

    @ParameterizedTest
    @CsvSource(
        "00:00, 0",
        "01:30, 90",
        "100:05, 6005",
    )
    fun `round trip preserves Duration`(wire: String, totalMinutes: Long) {
        val duration = Duration.ofMinutes(totalMinutes)
        val encoded = json.encodeToJsonElement(DurationSerializer, duration).jsonPrimitive.content
        assertThat(encoded).isEqualTo(wire)
        val decoded = json.decodeFromString(DurationSerializer, "\"$wire\"")
        assertThat(decoded).isEqualTo(duration)
    }

    @ParameterizedTest
    @ValueSource(strings = ["1", "01:60", "x:y"])
    fun `deserialize rejects invalid duration`(invalid: String) {
        assertThrows<Exception> {
            json.decodeFromString(DurationSerializer, "\"$invalid\"")
        }
    }
}
