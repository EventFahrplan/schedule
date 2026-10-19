package info.metadude.kotlin.library.schedule.v2.serializers

import kotlinx.serialization.SerializationException
import com.google.common.truth.Truth.assertThat
import info.metadude.kotlin.library.schedule.ScheduleJson
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import kotlin.uuid.Uuid

internal class UuidSerializerTest {

    private val json = ScheduleJson.create()

    @ParameterizedTest
    @ValueSource(strings = ["00000000-0000-0000-0000-000000000000", "550e8400-e29b-41d4-a716-446655440000"])
    fun `round trip preserves Uuid`(wire: String) {
        val value = Uuid.parse(wire)
        val encoded = json.encodeToJsonElement(UuidSerializer, value).jsonPrimitive.content
        assertThat(encoded).isEqualTo(wire)
        val decoded = json.decodeFromJsonElement(UuidSerializer, JsonPrimitive(wire))
        assertThat(decoded).isEqualTo(value)
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "invalid"])
    fun `deserialize rejects invalid Uuid`(wire: String) {
        assertThrows<SerializationException> {
            json.decodeFromJsonElement(UuidSerializer, JsonPrimitive(wire))
        }
    }
}
