package info.metadude.kotlin.library.schedule

import info.metadude.kotlin.library.schedule.Logging.Companion.None
import okhttp3.Call
import okhttp3.OkHttpClient

interface ScheduleApi {

    fun provideScheduleService(
        baseUrl: String,
        callFactory: Call.Factory = OkHttpClient.Builder().build(),
        logging: Logging = None,
    ): ScheduleService
}
