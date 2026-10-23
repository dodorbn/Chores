package fr.eseo.ld.e5e.android.robinedo.chores.domain.model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// Ligne vide pour garder le format
fun formatSecondsToHMS(totalSeconds : Int) : String {
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}

fun formatDateTime(dateTime : LocalDateTime) : String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss")
    return dateTime.format(formatter)
}

fun calculateTotalTimeForChore(chore : Chore) : Int {
    return chore.timeLogs.sumOf { it.duration }
}