package info.metadude.kotlin.library.schedule

import info.metadude.kotlin.library.schedule.Logging.Companion.None
import info.metadude.kotlin.library.schedule.v1.models.Day
import info.metadude.kotlin.library.schedule.v1.serializers.DaySerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule

internal object ScheduleJson {

    val default = create()

    fun create(logging: Logging = None) = Json {
        ignoreUnknownKeys = true
        serializersModule = SerializersModule {
            contextual(Day::class, DaySerializer(logging))
        }
    }
}
