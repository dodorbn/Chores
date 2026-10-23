package fr.eseo.ld.e5e.android.robinedo.chores.roomdb.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import fr.eseo.ld.e5e.android.robinedo.chores.domain.model.Chore
import fr.eseo.ld.e5e.android.robinedo.chores.domain.model.ChoreStatus
import java.util.UUID

@Entity(tableName = "chores")
data class ChoreRoomEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String?,
    val status: String,
    val creator: String
)

fun Chore.toChoreRoomEntity(): ChoreRoomEntity {
    return ChoreRoomEntity(
        id = this.id.toString(),
        title = this.title,
        description = this.description,
        status = this.status.name,
        creator = this.creator
    )
}

fun ChoreRoomEntity.toChore(): Chore {
    return Chore(
        id = UUID.fromString(this.id),
        title = this.title,
        description = this.description,
        status = ChoreStatus.valueOf(this.status),
        creator = this.creator,
        timeLogs = emptyList()
    )
}