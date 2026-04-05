package info.metadude.kotlin.library.schedule.v1.serializers

import com.google.common.truth.Truth.assertThat
import info.metadude.kotlin.library.schedule.v1.models.RoomDescription.Text
import info.metadude.kotlin.library.schedule.v1.models.RoomDescription.Translations
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.junit.jupiter.api.Test

internal class RoomDescriptionSerializerTest {

    private val json = Json

    @Test
    fun `round trip Text`() {
        val value = Text("Main hall")
        val element = json.encodeToJsonElement(RoomDescriptionSerializer, value)
        val decoded = json.decodeFromJsonElement(RoomDescriptionSerializer, element)
        assertThat(decoded).isEqualTo(value)
    }

    @Test
    fun `round trip Translations`() {
        val value = Translations(
            mapOf("en" to "Hall", "de" to "Saal"),
        )
        val element = json.encodeToJsonElement(RoomDescriptionSerializer, value)
        val decoded = json.decodeFromJsonElement(RoomDescriptionSerializer, element)
        assertThat(decoded).isEqualTo(value)
    }

    @Test
    fun `deserialize json string primitive as Text`() {
        val decoded = json.decodeFromJsonElement(RoomDescriptionSerializer, JsonPrimitive("plain"))
        assertThat(decoded).isEqualTo(Text("plain"))
    }

    @Test
    fun `deserialize json object as Translations`() {
        val obj = buildJsonObject {
            put("en", "A")
            put("de", "B")
        }
        val decoded = json.decodeFromJsonElement(RoomDescriptionSerializer, obj)
        assertThat(decoded).isEqualTo(Translations(mapOf("en" to "A", "de" to "B")))
    }
}
