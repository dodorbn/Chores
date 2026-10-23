package fr.eseo.ld.e5e.android.robinedo.chores.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import fr.eseo.ld.e5e.android.robinedo.chores.domain.viewmodels.ChoresViewModel
import fr.eseo.ld.e5e.android.robinedo.chores.ui.ChoreDetailsScreen
import fr.eseo.ld.e5e.android.robinedo.chores.ui.ChoreListScreen
import java.util.UUID

class ChoreNavigator(private val navController: NavController) {
    fun navigateToScreen(screen : ChoreScreen) {
        navController.navigate(screen)
    }
    fun navigateBack() {
        navController.popBackStack()
    }
    fun navigateAndClearBackStack(screen : ChoreScreen) {
        navController.navigate(screen){
            popUpTo(navController.graph.startDestinationId) {
                inclusive = true
            }
        }
    }
}

@Composable
fun ChoreAppNavigation() {
    val choresViewModel : ChoresViewModel = hiltViewModel()
    val navController = rememberNavController()
    val navigator = remember { ChoreNavigator(navController) }

    NavHost(
        navController = navController,
        startDestination = ChoreScreen.ChoreList
    ) {
        composable<ChoreScreen.ChoreList> {
            ChoreListScreen(
                choresViewModel = choresViewModel,
                onChoreClick = { uuid ->
                    if (uuid != null) {
                        navigator.navigateToScreen(ChoreScreen.ChoreDetail(choreId = uuid.toString()))
                    }
                }
            )
        }
        composable<ChoreScreen.ChoreDetail> { backStackEntry ->
            val args = backStackEntry.toRoute<ChoreScreen.ChoreDetail>()
            val choreId = UUID.fromString(args.choreId)
            ChoreDetailsScreen(choreId, choresViewModel)

        }
    }
}
