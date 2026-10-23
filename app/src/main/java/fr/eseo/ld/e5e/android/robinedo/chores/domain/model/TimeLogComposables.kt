package fr.eseo.ld.e5e.android.robinedo.chores.domain.model


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


import java.time.LocalDateTime
import java.util.UUID

    @Composable
    fun TimeLogItem(
        timeLog: TimeLog
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 4.dp,
                    horizontal = 8.dp
                ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        all = 16.dp
                    )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Time Log",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(48.dp)
                    )
                    Spacer(
                        modifier = Modifier
                            .width(16.dp)
                    )
                    Column(
                        modifier = Modifier,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = formatDateTime(timeLog.startTime),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )
                        Text(
                            text = formatDateTime(timeLog.endTime),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )
                        Text(
                            text = timeLog.userName,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    private fun PreviewTimeLogCard() {
        fr.eseo.ld.e5e.android.robinedo.chores.ui.theme.ChoresTheme {
            TimeLogItem(
                TimeLog(
                    id = 1,
                    choreId = UUID.randomUUID(),
                    userName = "Preview",
                    startTime = LocalDateTime.of(2026, 2, 25, 10, 15),
                    endTime = LocalDateTime.of(2026, 2, 25, 14, 37)
                )
            )
        }
    }