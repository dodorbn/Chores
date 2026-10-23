package fr.eseo.ld.e5e.android.robinedo.chores.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import fr.eseo.ld.e5e.android.robinedo.chores.R
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.eseo.ld.e5e.android.robinedo.chores.domain.model.Chore
import fr.eseo.ld.e5e.android.robinedo.chores.domain.model.TimeLog
import fr.eseo.ld.e5e.android.robinedo.chores.domain.model.TimeLogItem
import fr.eseo.ld.e5e.android.robinedo.chores.ui.EditableText
import fr.eseo.ld.e5e.android.robinedo.chores.domain.viewmodels.ChoresViewModel
import fr.eseo.ld.e5e.android.robinedo.chores.domain.model.calculateTotalTimeForChore
import fr.eseo.ld.e5e.android.robinedo.chores.domain.model.formatSecondsToHMS
import androidx.compose.ui.text.TextStyle
import java.util.UUID

@Composable
fun ChoreTimerSection(
    totalTime: Long,
    isTimerRunning: Boolean,
    onToggleTimer: () -> Unit,
    modifier: Modifier = Modifier,
    formatSecondsToHMSFn: (Int) -> String = { formatSecondsToHMS(it) }
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = formatSecondsToHMSFn(totalTime.toInt()),
            style = MaterialTheme.typography.displayLarge,
            color = if (isTimerRunning) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onToggleTimer,
            modifier = Modifier.size(width = 200.dp, height = 60.dp)
        ) {
            Text(
                text = if (isTimerRunning) "STOP TIMER" else "START TIMER",
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChoreDetailsContent(
    chore: Chore,
    isTimerRunning: Boolean,
    currentSessionTime: Int,
    onChoreUpdated: (Chore) -> Unit,
    onToggleTimer: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier.fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text(text = stringResource(R.string.app_name)) })
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Chore Details Fields
                EditableText(
                    value = chore.title,
                    onValueChange = { newTitle -> onChoreUpdated(chore.copy(title = newTitle)) },
                    textStyle = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(16.dp))

                EditableText(
                    value = chore.description ?: "",
                    onValueChange = { newDesc ->
                        onChoreUpdated(chore.copy(description = newDesc.ifEmpty { null }))
                    },
                    textStyle = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Extracted Timer Section
                val totalTime = calculateTotalTimeForChore(chore) + currentSessionTime
                ChoreTimerSection(
                    totalTime = totalTime.toLong(),
                    isTimerRunning = isTimerRunning,
                    onToggleTimer = onToggleTimer
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Logs List
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(
                        items = chore.timeLogs,
                        key = { it.startTime }
                    ) { timeLog ->
                        TimeLogItem(timeLog)
                    }
                }
            }
        }
    }
}



@Composable
fun ChoreDetailsScreen(
    choreId: UUID?,
    choresViewModel: ChoresViewModel = viewModel()
) {
    Log.d("LIFECYCLE", "Composition - ChoreDetailsScreen")

    val choreList by choresViewModel.chores.collectAsState()
    val isTimerRunning by choresViewModel.isTimerRunning.collectAsState()
    val currentSessionTime by choresViewModel.currentSessionTime.collectAsState()

    val currentChore = choreList.find { it.id == choreId }

    if (currentChore == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    ChoreDetailsContent(
        chore = currentChore,
        isTimerRunning = isTimerRunning,
        currentSessionTime = currentSessionTime,
        onChoreUpdated = { updatedChore -> choresViewModel.addOrUpdateChore(updatedChore) },
        onToggleTimer = { choresViewModel.toggleTimer(currentChore.id) }
    )
}


