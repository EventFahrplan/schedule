package info.metadude.kotlin.library.schedule

import info.metadude.kotlin.library.schedule.Logging.Companion.None
import info.metadude.kotlin.library.schedule.v1.models.Day
import info.metadude.kotlin.library.schedule.v1.serializers.DaySerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import org.jetbrains.annotations.VisibleForTesting
import retrofit2.Converter.Factory
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object Api : ScheduleApi {

    private val CONTENT_TYPE = "application/json; charset=UTF-8".toMediaType()

    override fun provideScheduleService(
        baseUrl: String,
        callFactory: Call.Factory,
        logging: Logging,
    ): ScheduleService {
        require(baseUrl.isNotEmpty()) { "baseUrl is empty." }
        val retrofit = createRetrofit(baseUrl, callFactory, logging)
        return retrofit.create(ScheduleService::class.java)
    }

    @VisibleForTesting
    fun createRetrofit(
        baseUrl: String,
        callFactory: Call.Factory,
        logging: Logging = None,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(createJsonConverterFactory(logging))
        .callFactory(callFactory)
        .build()

    private fun createJsonConverterFactory(logging: Logging): Factory {
        val json = Json {
            ignoreUnknownKeys = true
            serializersModule = SerializersModule {
                contextual(Day::class, DaySerializer(logging))
            }
        }
        return json.asConverterFactory(CONTENT_TYPE)
    }
}
