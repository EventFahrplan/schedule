package info.metadude.kotlin.library.schedule.v1.serializers

import com.google.common.truth.Truth.assertThat
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonPrimitive
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
internal class UuidSerializerTest {

    private val json = Json

    @ParameterizedTest
    @ValueSource(
        strings = [
            "00000000-0000-0000-0000-000000000000",
            "550e8400-e29b-41d4-a716-446655440000",
            "f47ac10b-58cc-4372-a567-0e02b2c3d479",
        ],
    )
    fun `round trip preserves Uuid`(text: String) {
        val uuid = Uuid.parse(text)
        val encoded = json.encodeToJsonElement(UuidSerializer, uuid).jsonPrimitive.content
        assertThat(encoded).isEqualTo(text.lowercase())
        val decoded = json.decodeFromString(UuidSerializer, "\"$text\"")
        assertThat(decoded).isEqualTo(uuid)
    }
}
