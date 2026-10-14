package info.metadude.kotlin.library.schedule.v2.serializers

import com.google.common.truth.Truth.assertThat
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.threeten.bp.ZoneId
import org.threeten.bp.DateTimeException

internal class ZoneIdSerializerTest {

    private val json = Json

    @ParameterizedTest
    @ValueSource(strings = ["UTC", "Europe/Berlin", "America/New_York"])
    fun `round trip preserves ZoneId`(wire: String) {
        val value = ZoneId.of(wire)
        val encoded = json.encodeToJsonElement(ZoneIdSerializer, value).jsonPrimitive.content
        assertThat(encoded).isEqualTo(wire)
        val decoded = json.decodeFromJsonElement(ZoneIdSerializer, JsonPrimitive(wire))
        assertThat(decoded).isEqualTo(value)
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "invalid"])
    fun `deserialize rejects invalid ZoneId`(wire: String) {
        assertThrows<DateTimeException> {
            json.decodeFromJsonElement(ZoneIdSerializer, JsonPrimitive(wire))
        }
    }
}
