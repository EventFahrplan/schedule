package info.metadude.kotlin.library.schedule.v2.serializers

import com.google.common.truth.Truth.assertThat
import info.metadude.kotlin.library.schedule.ScheduleJson
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.JsonDecodingException
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.threeten.bp.ZoneId

internal class ZoneIdSerializerTest {

    private val json = ScheduleJson.create()

    @ParameterizedTest
    @ValueSource(strings = ["UTC", "Europe/Berlin", "America/New_York"])
    fun `round trip preserves ZoneId`(wire: String) {
        val value = ZoneId.of(wire)
        val encoded = json.encodeToJsonElement(ZoneIdSerializer, value).jsonPrimitive.content
        assertThat(encoded).isEqualTo(wire)
        val decoded = json.decodeFromJsonElement(ZoneIdSerializer, JsonPrimitive(wire))
        assertThat(decoded).isEqualTo(value)
    }

    @OptIn(ExperimentalSerializationApi::class)
    @ParameterizedTest
    @ValueSource(strings = ["", "invalid"])
    fun `deserialize rejects invalid ZoneId`(wire: String) {
        assertThrows<JsonDecodingException> {
            json.decodeFromJsonElement(ZoneIdSerializer, JsonPrimitive(wire))
        }
    }
}
