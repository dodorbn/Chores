package fr.eseo.ld.e5e.android.robinedo.chores.domain.model

import java.util.UUID

data class Chore(
    val id : UUID = UUID.randomUUID(),
    val creator : String = "",
    val title : String = "",
    val description : String? = null,
    val status : ChoreStatus = ChoreStatus.PENDING,
    val timeLogs : List<TimeLog> = emptyList()
)
