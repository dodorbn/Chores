package fr.eseo.ld.e5e.android.robinedo.chores.domain.model

import java.time.Duration
import java.time.LocalDateTime
import java.util.UUID

data class TimeLog(
    val id : Int = 0,
    val choreId : UUID? = null,
    val userName : String = "",
    val startTime : LocalDateTime  = LocalDateTime.now(),
    val endTime : LocalDateTime = LocalDateTime.now()
)
{
    val duration : Int
        get() = Duration.between(startTime, endTime).seconds.toInt()
}
