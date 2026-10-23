package fr.eseo.ld.e5e.android.robinedo.chores.roomdb.relations

import androidx.room.Embedded
import androidx.room.Relation
import fr.eseo.ld.e5e.android.robinedo.chores.domain.model.Chore
import fr.eseo.ld.e5e.android.robinedo.chores.roomdb.entities.ChoreRoomEntity
import fr.eseo.ld.e5e.android.robinedo.chores.roomdb.entities.TimeLogRoomEntity
import fr.eseo.ld.e5e.android.robinedo.chores.roomdb.entities.toTimeLog

data class ChoreWithTimeLogsRoomRelation(
    @Embedded val chore: ChoreRoomEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "choreId"
    )
    val timeLogs: List<TimeLogRoomEntity>
)

fun ChoreWithTimeLogsRoomRelation.toChore(): Chore {
    return Chore(
        id = java.util.UUID.fromString(this.chore.id),
        title = this.chore.title,
        description = this.chore.description,
        status = fr.eseo.ld.e5e.android.robinedo.chores.domain.model.ChoreStatus.valueOf(this.chore.status),
        creator = this.chore.creator,
        timeLogs = this.timeLogs.map { it.toTimeLog() }
    )
}