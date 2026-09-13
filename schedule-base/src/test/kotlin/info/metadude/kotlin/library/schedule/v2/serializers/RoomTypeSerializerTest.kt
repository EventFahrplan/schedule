package info.metadude.kotlin.library.schedule.v2.serializers

import com.google.common.truth.Truth.assertThat
import info.metadude.kotlin.library.schedule.v2.models.RoomType
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.ValueSource
import org.junit.jupiter.api.Test

internal class RoomTypeSerializerTest {

    private val json = Json

    @ParameterizedTest
    @EnumSource(RoomType::class)
    fun `serialize returns corresponding string value`(value: RoomType) {
        val expected = value.name
        val actual = json.encodeToJsonElement(RoomTypeSerializer, value).jsonPrimitive.content
        assertThat(actual).isEqualTo(expected)
    }

    @ParameterizedTest
    @EnumSource(RoomType::class)
    fun `deserialize returns corresponding RoomType value`(expected: RoomType) {
        val wire = expected.name
        val actual = json.decodeFromJsonElement(RoomTypeSerializer, JsonPrimitive(wire))
        assertThat(actual).isEqualTo(expected)
    }

    @ParameterizedTest
    @ValueSource(strings = ["invalid", "", "talk"])
    fun `deserialize maps unknown string to UNKNOWN`(wire: String) {
        val actual = json.decodeFromJsonElement(RoomTypeSerializer, JsonPrimitive(wire))
        assertThat(actual).isEqualTo(RoomType.UNKNOWN)
    }

    @Test
    fun `deserialize accepts legacy LECTUREHALL spelling`() {
        val actual = json.decodeFromJsonElement(RoomTypeSerializer, JsonPrimitive("LECTUREHALL"))
        assertThat(actual).isEqualTo(RoomType.LECTURE_HALL)
    }

}
