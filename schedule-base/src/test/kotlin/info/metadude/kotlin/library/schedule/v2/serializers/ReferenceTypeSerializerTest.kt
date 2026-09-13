package info.metadude.kotlin.library.schedule.v2.serializers

import com.google.common.truth.Truth.assertThat
import info.metadude.kotlin.library.schedule.v2.models.ReferenceType
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.ValueSource

internal class ReferenceTypeSerializerTest {

    private val json = Json

    @ParameterizedTest
    @EnumSource(ReferenceType::class)
    fun `serialize returns corresponding string value`(value: ReferenceType) {
        val expected = value.name
        val actual = json.encodeToJsonElement(ReferenceTypeSerializer, value).jsonPrimitive.content
        assertThat(actual).isEqualTo(expected)
    }

    @ParameterizedTest
    @EnumSource(ReferenceType::class)
    fun `deserialize returns corresponding ReferenceType value`(expected: ReferenceType) {
        val wire = expected.name
        val actual = json.decodeFromJsonElement(ReferenceTypeSerializer, JsonPrimitive(wire))
        assertThat(actual).isEqualTo(expected)
    }

    @ParameterizedTest
    @ValueSource(strings = ["invalid", "", "talk"])
    fun `deserialize maps unknown string to UNKNOWN`(wire: String) {
        val actual = json.decodeFromJsonElement(ReferenceTypeSerializer, JsonPrimitive(wire))
        assertThat(actual).isEqualTo(ReferenceType.UNKNOWN)
    }

    @Test
    fun `deserialize accepts legacy ACTIVITYPUB spelling`() {
        val actual = json.decodeFromJsonElement(ReferenceTypeSerializer, JsonPrimitive("ACTIVITYPUB"))
        assertThat(actual).isEqualTo(ReferenceType.ACTIVITY_PUB)
    }

}
