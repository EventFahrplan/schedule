package info.metadude.kotlin.library.schedule.v2.models

/**
 * Inspired by https://docs.pretalx.org/user/sessions/#session-lifecycle
 */
enum class EventState {
    ACCEPTED,
    CANCELED,
    CONFIRMED,
    DELAYED,
    DELETED,
    DRAFT,
    REJECTED,
    SUBMITTED,
    WITHDRAWN,
    UNKNOWN,
}
