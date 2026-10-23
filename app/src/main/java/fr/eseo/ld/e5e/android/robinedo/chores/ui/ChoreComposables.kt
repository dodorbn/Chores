package fr.eseo.ld.e5e.android.robinedo.chores.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.eseo.ld.e5e.android.robinedo.chores.domain.model.Chore
import fr.eseo.ld.e5e.android.robinedo.chores.domain.model.ChoreStatus
import fr.eseo.ld.e5e.android.robinedo.chores.ui.theme.ChoresTheme
import java.util.UUID

@Composable
fun ChoreItemCard(
    chore : Chore,
    modifier : Modifier = Modifier,
    onClick : (UUID?)-> Unit,
    onDelete : (Chore) -> Unit = {}
){
    Log.d("Composition","ChoreItemCard()")
    var showDetails by rememberSaveable {mutableStateOf(false)}
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                vertical = 4.dp,
                horizontal = 8.dp
            )
            .testTag("chore_item_card")
            .combinedClickable(
                onLongClick = {
                    showDetails = !showDetails
                },
                onClick = { onClick(chore.id) }
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
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Delete Chore",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { onDelete(chore) }
                )
                Spacer(
                    modifier = Modifier
                        .width(16.dp)
                )
                Column(
                    modifier = Modifier.Companion,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = chore.title,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.testTag("chore_title")
                    )
                    Spacer(
                        modifier = Modifier
                            .height(8.dp)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(
                            text = chore.description ?: "Aucune description",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("chore_description_preview")
                        )
                        Surface(
                            color = when (chore.status) {
                                ChoreStatus.PENDING -> Color.Gray
                                ChoreStatus.ON_GOING -> Color.Green
                                ChoreStatus.FINISHED -> Color.Red
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .width(48.dp)
                                .height(24.dp)
                                .testTag("chore_status")
                        ) {}
                    }

                }
            }
            if (showDetails) {
                chore.description?.let {
                    Spacer(
                        modifier = Modifier
                            .height(8.dp)
                    )
                    Text(
                        text = chore.description,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.testTag("chore_item_description")
                    )
                }
            } else {
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.End
                ) {
                    Spacer(
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Show Details",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewChoreDialog (
    onAdd : (Chore) -> Unit,
    onCancel : () -> Unit
) {
    Log.d("Composition","NewChoreDialog()")
    var titleText by rememberSaveable { mutableStateOf("") }
    var descriptionText by rememberSaveable { mutableStateOf("") }

    BasicAlertDialog(
        onDismissRequest = onCancel
    ) {
        Surface(
            modifier = Modifier
                .widthIn(max = 300.dp)
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surfaceContainer
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = "Add New Chore",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(
                    modifier = Modifier.height(8.dp)
                )
                OutlinedTextField(
                    value = titleText,
                    onValueChange = {titleText = it},
                    placeholder = {
                        Text(text = "New Chore's Title")
                    },
                    singleLine = true,
                    modifier = Modifier
                        .testTag("dialog_chore_title")
                )
                Spacer(
                    modifier = Modifier.height(8.dp)
                )
                OutlinedTextField(
                    value = descriptionText,
                    onValueChange = {descriptionText = it},
                    placeholder = {
                        Text(text = "New Chore's Description (Optional)")
                    },
                    singleLine = false,
                    maxLines = 5,
                    modifier = Modifier
                        .testTag("dialog_chore_desc")
                )
                Spacer(
                    modifier = Modifier.height(16.dp)
                )
                TextButton(
                    modifier = Modifier.testTag("dialog_add_chore"),
                    enabled = !titleText.isEmpty(),
                    onClick = {
                        val chore = Chore(
                            id = UUID.randomUUID(),
                            creator = "Bob",
                            title = titleText,
                            description = descriptionText.ifEmpty {null},
                            status = ChoreStatus.PENDING,
                            timeLogs = emptyList()
                        )
                        onAdd(chore)
                    }
                ){
                    Text(
                        text = "Add Chore"
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewChoreModalBottom (
    onAdd : (Chore) -> Unit,
    onCancel : () -> Unit
) {
    Log.d("Composition","NewChoreModalButtom()")
    var titleText by rememberSaveable { mutableStateOf("") }
    var descriptionText by rememberSaveable { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onCancel
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surfaceContainer
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Add New Chore",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(
                    modifier = Modifier.height(8.dp)
                )
                OutlinedTextField(
                    value = titleText,
                    onValueChange = { titleText = it },
                    placeholder = {
                        Text(text = "New Chore's Title")
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dialog_chore_title")
                )
                Spacer(
                    modifier = Modifier.height(8.dp)
                )
                OutlinedTextField(
                    value = descriptionText,
                    onValueChange = { descriptionText = it },
                    placeholder = {
                        Text(text = "New Chore's Description (Optional)")
                    },
                    singleLine = false,
                    maxLines = 5,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dialog_chore_desc")
                )
                Spacer(
                    modifier = Modifier.height(16.dp)
                )
                TextButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dialog_add_chore"),
                    enabled = titleText.isNotBlank(),
                    onClick = {
                        val chore = Chore(
                            id = UUID.randomUUID(),
                            creator = "Bob",
                            title = titleText,
                            description = descriptionText.ifBlank { null },
                            status = ChoreStatus.PENDING,
                            timeLogs = emptyList()
                        )
                        onAdd(chore)
                    }
                ){
                    Text(
                        text = "Add Chore"
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewChoreItemCardPending(){
    ChoresTheme {
        ChoreItemCard(
            chore = Chore(
                id = UUID.randomUUID(),
                title = "Preview Pending Chore",
                status = ChoreStatus.PENDING,
                creator = "Previewer",
            ),
            onClick = {},
            onDelete = {}
        )
    }
}

@Preview
@Composable
private fun PreviewChoreItemCardOnGoing(){
    ChoresTheme {
        ChoreItemCard(
            chore = Chore(
                id = UUID.randomUUID(),
                title = "Preview Ongoing Chore",
                status = ChoreStatus.ON_GOING,
                creator = "Previewer",
            ),
            onClick = {},
            onDelete = {}
        )
    }
}

@Preview
@Composable
private fun PreviewChoreItemCardFinished(){
    ChoresTheme {
        ChoreItemCard(
            chore = Chore(
                id = UUID.randomUUID(),
                title = "Preview Finished Chore",
                status = ChoreStatus.FINISHED,
                creator = "Previewer",
            ),
            onClick = {},
            onDelete = {}
        )
    }
}

@Preview
@Composable
private fun PreviewChoreItemCardOnGoingDescription(){
    ChoresTheme {
        ChoreItemCard(
            chore = Chore(
                id = UUID.randomUUID(),
                title = "Preview Ongoing Chore",
                status = ChoreStatus.ON_GOING,
                creator = "Previewer",
                description = "With a nice description that can sometimes be rather long."
                        + "Sometimes going onto multiple lines..."
            ),
            onClick = {},
            onDelete = {}
        )
    }
}
