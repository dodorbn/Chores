package fr.eseo.ld.e5e.android.robinedo.chores.roomdb.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import fr.eseo.ld.e5e.android.robinedo.chores.domain.model.TimeLog
import java.time.LocalDateTime
import java.util.UUID

@Entity(
    tableName = "time_logs",
    foreignKeys = [
        ForeignKey(
            entity = ChoreRoomEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("choreId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TimeLogRoomEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val choreId: String,
    val startTime: String,
    val endTime: String,
    val userName: String
)

fun TimeLog.toTimeLogRoomEntity(): TimeLogRoomEntity {
    return TimeLogRoomEntity(
        choreId = this.choreId.toString(),
        startTime = this.startTime.toString(),
        endTime = this.endTime.toString(),
        userName = this.userName
    )
}

fun TimeLogRoomEntity.toTimeLog(): TimeLog {
    return TimeLog(
        choreId = UUID.fromString(this.choreId),
        startTime = LocalDateTime.parse(this.startTime),
        endTime = LocalDateTime.parse(this.endTime),
        userName = this.userName
    )
}