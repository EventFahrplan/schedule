package info.metadude.kotlin.library.schedule.v2.serializers

import com.google.common.truth.Truth.assertThat
import info.metadude.kotlin.library.schedule.v2.models.ReferenceService
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.ValueSource

internal class ReferenceServiceSerializerTest {

    private val json = Json

    @ParameterizedTest
    @EnumSource(ReferenceService::class)
    fun `serialize returns corresponding string value`(value: ReferenceService) {
        val expected = value.name
        val actual = json.encodeToJsonElement(ReferenceServiceSerializer, value).jsonPrimitive.content
        assertThat(actual).isEqualTo(expected)
    }

    @ParameterizedTest
    @EnumSource(ReferenceService::class)
    fun `deserialize returns corresponding ReferenceService value`(expected: ReferenceService) {
        val wire = expected.name
        val actual = json.decodeFromJsonElement(ReferenceServiceSerializer, JsonPrimitive(wire))
        assertThat(actual).isEqualTo(expected)
    }

    @ParameterizedTest
    @ValueSource(strings = ["invalid", "", "talk"])
    fun `deserialize maps unknown string to UNKNOWN`(wire: String) {
        val actual = json.decodeFromJsonElement(ReferenceServiceSerializer, JsonPrimitive(wire))
        assertThat(actual).isEqualTo(ReferenceService.UNKNOWN)
    }

    @Test
    fun `deserialize accepts legacy MEDIACCCDE spelling`() {
        val actual = json.decodeFromJsonElement(ReferenceServiceSerializer, JsonPrimitive("MEDIACCCDE"))
        assertThat(actual).isEqualTo(ReferenceService.MEDIA_CCC_DE)
    }

}
