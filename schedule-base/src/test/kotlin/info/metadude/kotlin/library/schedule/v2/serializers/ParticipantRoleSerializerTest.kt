package info.metadude.kotlin.library.schedule.v2.serializers

import com.google.common.truth.Truth.assertThat
import info.metadude.kotlin.library.schedule.v2.models.ParticipantRole
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.ValueSource

internal class ParticipantRoleSerializerTest {

    private val json = Json

    @ParameterizedTest
    @EnumSource(ParticipantRole::class)
    fun `serialize returns corresponding string value`(value: ParticipantRole) {
        val expected = value.name
        val actual = json.encodeToJsonElement(ParticipantRoleSerializer, value).jsonPrimitive.content
        assertThat(actual).isEqualTo(expected)
    }

    @ParameterizedTest
    @EnumSource(ParticipantRole::class)
    fun `deserialize returns corresponding ParticipantRole value`(expected: ParticipantRole) {
        val wire = expected.name
        val actual = json.decodeFromJsonElement(ParticipantRoleSerializer, JsonPrimitive(wire))
        assertThat(actual).isEqualTo(expected)
    }

    @ParameterizedTest
    @ValueSource(strings = ["invalid", "", "talk"])
    fun `deserialize maps unknown string to UNKNOWN`(wire: String) {
        val actual = json.decodeFromJsonElement(ParticipantRoleSerializer, JsonPrimitive(wire))
        assertThat(actual).isEqualTo(ParticipantRole.UNKNOWN)
    }
}
