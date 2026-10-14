package info.metadude.kotlin.library.schedule.v2.serializers

import com.google.common.truth.Truth.assertThat
import info.metadude.kotlin.library.schedule.v2.models.ScheduleDuration
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class ScheduleDurationSerializerTest {

    private val json = Json

    @ParameterizedTest
    @ValueSource(strings = ["PT0S", "PT90M", "P2W", "P1Y2M3DT4H5M6.5S", "P0DT00H10M00S"])
    fun `round trip preserves duration spelling`(wire: String) {
        val duration = ScheduleDuration(wire)
        val encoded = json.encodeToJsonElement(ScheduleDurationSerializer, duration).jsonPrimitive.content
        assertThat(encoded).isEqualTo(wire)
        val decoded = json.decodeFromJsonElement(ScheduleDurationSerializer, JsonPrimitive(wire))
        assertThat(decoded).isEqualTo(duration)
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "P", "PT", "P1DT", "01:30", "PT-1H", "PT1.5H", "P1H", "PT1S2H"])
    fun `deserialize rejects invalid duration`(wire: String) {
        assertThrows<IllegalArgumentException> {
            json.decodeFromJsonElement(ScheduleDurationSerializer, JsonPrimitive(wire))
        }
    }
}
