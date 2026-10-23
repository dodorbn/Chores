package fr.eseo.ld.e5e.android.robinedo.chores.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.eseo.ld.e5e.android.robinedo.chores.domain.model.Chore
import fr.eseo.ld.e5e.android.robinedo.chores.domain.model.ChoreStatus
import fr.eseo.ld.e5e.android.robinedo.chores.ui.theme.ChoresTheme
import java.util.UUID

@Composable
fun ChoreItemList(
    chores :List<Chore>,
    modifier: Modifier = Modifier,
    onChoreClick: (java.util.UUID?) -> Unit = {},
    onDeleteClick: (Chore) -> Unit = {}
){
    Log.d("Composition","ChoreItemList()")
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        items(
            items = chores,
            key = {chore -> chore.id}
        ){ chore ->
            ChoreItemCard(
                chore = chore,
                modifier = Modifier,
                onClick = onChoreClick,
                onDelete = onDeleteClick
            )
        }
    }
}

@Preview
@Composable
private fun PreviewChoreItemList(){
    ChoresTheme {
        ChoreItemList(
            listOf(
                Chore(
                    id = UUID.randomUUID(),
                    title = "Preview Finished Chore",
                    status = ChoreStatus.FINISHED,
                    creator = "Previewer",
                ),
                Chore(
                    id = UUID.randomUUID(),
                    title = "Preview Ongoing Chore",
                    status = ChoreStatus.ON_GOING,
                    creator = "Previewer",
                ),
                Chore(
                    id = UUID.randomUUID(),
                    title = "Preview Pending Chore",
                    status = ChoreStatus.PENDING,
                    creator = "Previewer",
                )
            )
        ) {}
    }
}