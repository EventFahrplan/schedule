package info.metadude.kotlin.library.schedule.v1.serializers

import com.google.common.truth.Truth.assertThat
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import kotlin.uuid.Uuid

internal class RoomsSerializerTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun `deserialize skips events that fail decoding`() {
        val rooms = json.decodeFromString(
            RoomsSerializer(),
            """
                {
                  "One": [
                    {
                      "guid": "bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb",
                      "id": 1,
                      "date": "2025-01-01T10:00:00+00:00",
                      "start": "10:00",
                      "duration": "00:30",
                      "room": "One",
                      "slug": "valid",
                      "title": "Valid",
                      "type": "talk",
                      "url": "https://example.com/valid",
                      "extra": "ignored"
                    },
                    {
                      "id": 2,
                      "date": "2025-01-01T11:00:00+00:00",
                      "start": "11:00",
                      "duration": "00:30",
                      "room": "One",
                      "slug": "missing-guid",
                      "title": "Missing guid",
                      "type": "talk",
                      "url": "https://example.com/missing-guid"
                    },
                    {
                      "guid": "not-a-uuid",
                      "id": 3,
                      "date": "2025-01-01T12:00:00+00:00",
                      "start": "12:00",
                      "duration": "00:30",
                      "room": "One",
                      "slug": "invalid-guid",
                      "title": "Invalid guid",
                      "type": "talk",
                      "url": "https://example.com/invalid-guid"
                    }
                  ],
                  "Two": []
                }
            """.trimIndent()
        )

        assertThat(rooms.keys).containsExactly("One", "Two").inOrder()
        assertThat(rooms.getValue("One")).hasSize(1)
        assertThat(rooms.getValue("One").single().guid).isEqualTo(
            Uuid.parse("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb")
        )
        assertThat(rooms.getValue("Two")).isEmpty()
    }
}
