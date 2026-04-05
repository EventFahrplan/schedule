package info.metadude.kotlin.library.schedule.v1.serializers

import com.google.common.truth.Truth.assertThat
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonPrimitive
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.threeten.bp.ZoneId

internal class ZoneIdSerializerTest {

    private val json = Json

    @ParameterizedTest
    @ValueSource(strings = ["UTC", "Europe/Berlin", "America/New_York"])
    fun `round trip preserves ZoneId`(id: String) {
        val zone = ZoneId.of(id)
        val encoded = json.encodeToJsonElement(ZoneIdSerializer, zone).jsonPrimitive.content
        assertThat(encoded).isEqualTo(id)
        val decoded = json.decodeFromString(ZoneIdSerializer, "\"$id\"")
        assertThat(decoded).isEqualTo(zone)
    }
}
