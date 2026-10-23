package fr.eseo.ld.e5e.android.robinedo.chores.ui

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.eseo.ld.e5e.android.robinedo.chores.R
import fr.eseo.ld.e5e.android.robinedo.chores.domain.viewmodels.ChoresViewModel
import fr.eseo.ld.e5e.android.robinedo.chores.ui.theme.ChoresTheme
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChoreListScreen(
    choresViewModel: ChoresViewModel = viewModel(),
    modifier: Modifier = Modifier,
    onChoreClick : (UUID?) -> Unit
){
    Log.d("Composition","ChoreListScreen()")
    val chores by choresViewModel.chores.collectAsState()
    var showNewChore by rememberSaveable { mutableStateOf(false) }

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier
            .fillMaxSize()
    ){
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.app_name)
                        )
                    }
                )
            },
            content = { innerPadding ->
                ChoreItemList(
                    chores = chores,
                    modifier = modifier
                        .testTag("chore_list")
                        .padding(innerPadding)
                        .imePadding(),
                    onChoreClick = onChoreClick,
                    onDeleteClick = { chore -> choresViewModel.deleteChore(chore) }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    modifier = Modifier.testTag("fab_new_chore"),
                    onClick = {
                        if(!showNewChore) {
                            showNewChore = true
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = "Add chore",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            }
        )
        if(showNewChore) {
            NewChoreModalBottom (
                onAdd = {newChore ->
                    choresViewModel.addOrUpdateChore(newChore)
                    showNewChore = false
                },
                onCancel = {showNewChore = false}
            )
        }

    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun PreviewChoreItemList(){
    ChoresTheme {
        ChoreListScreen(onChoreClick = {})
    }
}
