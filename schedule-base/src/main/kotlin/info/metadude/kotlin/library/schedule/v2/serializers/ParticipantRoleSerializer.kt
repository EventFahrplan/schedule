package info.metadude.kotlin.library.schedule.v2.serializers

import info.metadude.kotlin.library.schedule.v2.models.ParticipantRole

internal object ParticipantRoleSerializer : StringEnumSerializer<ParticipantRole>("ParticipantRole") {

    override fun fromWire(value: String) = runCatching { ParticipantRole.valueOf(value) }
        .getOrDefault(ParticipantRole.UNKNOWN)

    override fun toWire(value: ParticipantRole) = value.name
}
