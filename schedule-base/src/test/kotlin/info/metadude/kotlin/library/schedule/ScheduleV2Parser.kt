package info.metadude.kotlin.library.schedule

import info.metadude.kotlin.library.schedule.v2.models.ScheduleV2

internal object ScheduleV2Parser {

    fun parse(json: String): ScheduleV2 = ScheduleJson.default.decodeFromString(json)

}
