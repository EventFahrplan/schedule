package info.metadude.kotlin.library.schedule.v2.serializers

import com.google.common.truth.Truth.assertThat
import info.metadude.kotlin.library.schedule.v2.models.MetaEventType
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.ValueSource

internal class MetaEventTypeSerializerTest {

    private val json = Json

    @ParameterizedTest
    @EnumSource(MetaEventType::class)
    fun `serialize returns corresponding string value`(value: MetaEventType) {
        val expected = value.name
        val actual = json.encodeToJsonElement(MetaEventTypeSerializer, value).jsonPrimitive.content
        assertThat(actual).isEqualTo(expected)
    }

    @ParameterizedTest
    @EnumSource(MetaEventType::class)
    fun `deserialize returns corresponding MetaEventType value`(expected: MetaEventType) {
        val wire = expected.name
        val actual = json.decodeFromJsonElement(MetaEventTypeSerializer, JsonPrimitive(wire))
        assertThat(actual).isEqualTo(expected)
    }

    @ParameterizedTest
    @ValueSource(strings = ["invalid", "", "talk"])
    fun `deserialize maps unknown string to UNKNOWN`(wire: String) {
        val actual = json.decodeFromJsonElement(MetaEventTypeSerializer, JsonPrimitive(wire))
        assertThat(actual).isEqualTo(MetaEventType.UNKNOWN)
    }
}
