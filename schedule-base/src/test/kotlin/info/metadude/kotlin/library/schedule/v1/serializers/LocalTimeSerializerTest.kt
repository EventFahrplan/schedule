package info.metadude.kotlin.library.schedule.v1.serializers

import com.google.common.truth.Truth.assertThat
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonPrimitive
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter

internal class LocalTimeSerializerTest {

    private val json = Json
    private val formatter = DateTimeFormatter.ofPattern("HH:mm")

    @ParameterizedTest
    @ValueSource(strings = ["00:00", "09:30", "23:59"])
    fun `round trip preserves LocalTime`(text: String) {
        val time = LocalTime.parse(text, formatter)
        val encoded = json.encodeToJsonElement(LocalTimeSerializer, time).jsonPrimitive.content
        assertThat(encoded).isEqualTo(text)
        val decoded = json.decodeFromString(LocalTimeSerializer, "\"$text\"")
        assertThat(decoded).isEqualTo(time)
    }
}
