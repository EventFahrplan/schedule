package info.metadude.kotlin.library.schedule.v2.models

import info.metadude.kotlin.library.schedule.v2.serializers.ParticipantRoleSerializer
import info.metadude.kotlin.library.schedule.v2.serializers.UuidSerializer
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class EventParticipant(
    @Serializable(with = UuidSerializer::class)
    val guid: Uuid,
    val name: String,
    @Serializable(with = ParticipantRoleSerializer::class) val role: ParticipantRole = ParticipantRole.SPEAKER,
)
