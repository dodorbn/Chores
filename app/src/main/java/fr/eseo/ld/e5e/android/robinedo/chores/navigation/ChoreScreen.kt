package fr.eseo.ld.e5e.android.robinedo.chores.navigation
import kotlinx.serialization.Serializable
@Serializable
sealed interface ChoreScreen {

    @Serializable
    object ChoreList : ChoreScreen
    @Serializable
    data class ChoreDetail(val choreId : String) : ChoreScreen
}
