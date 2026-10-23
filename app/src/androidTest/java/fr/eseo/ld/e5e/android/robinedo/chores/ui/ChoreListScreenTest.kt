package fr.eseo.ld.e5e.android.robinedo.chores.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import de.mannodermaus.junit5.compose.ComposeExtension
import de.mannodermaus.junit5.compose.createComposeExtension
import fr.eseo.ld.e5e.android.robinedo.chores.domain.model.Chore
import fr.eseo.ld.e5e.android.robinedo.chores.domain.model.ChoreStatus
import fr.eseo.ld.e5e.android.robinedo.chores.domain.viewmodels.ChoresViewModel
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import java.util.UUID

@OptIn(ExperimentalTestApi::class)
class ChoreListScreenTest(@JvmField @RegisterExtension
                          val composeTestExtension: ComposeExtension = createComposeExtension()) {
    companion object {
        private val chores = listOf(
            Chore(
                id = UUID.randomUUID(),
                title = "Test a Finished Chore",
                status = ChoreStatus.FINISHED,
                creator = "Tester 2",
            ),
            Chore(
                id = UUID.randomUUID(),
                title = "Test an Ongoing Chore",
                status = ChoreStatus.ON_GOING,
                creator = "Tester 1",
            ),
            Chore(
                id = UUID.randomUUID(),
                title = "Preview Pending Chore",
                status = ChoreStatus.PENDING,
                creator = "Previewer",
            ),
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
                title = "Test a Pending Chore",
                status = ChoreStatus.PENDING,
                creator = "Tester 3",
            )
        )
    }

    private fun assertChoreCard(
        expectedTitle: String,
        expectedColour: Color,
        expectedCreator: String
    ) {
        val choresViewModel = ChoresViewModel()
        for(chore in chores) {
            choresViewModel.addOrUpdateChore(chore)
        }
        composeTestExtension.use{
            setContent {
                ChoreListScreen(choresViewModel)
            }
            val choreCard = onAllNodesWithTag("chore_item_card", true)
                .filterToOne(hasAnyDescendant(hasText(expectedTitle)))
            choreCard.assertIsDisplayed()
            choreCard.onChildren()
                .filterToOne(hasTestTag("chore_status"))
                .assertStatusColour(expectedColour)
            choreCard.onChildren()
                .filterToOne(hasTestTag("chore_creator"))
                .assertTextEquals(expectedCreator)
        }
    }

    @Test
    fun testPendingChoreCard() {
        assertChoreCard("Test a Pending Chore", Color.Gray, "Tester 3")
    }

    @Test
    fun testOngoingChoreCard() {
        assertChoreCard("Test an Ongoing Chore", Color.Green, "Tester 1")
    }

    @Test
    fun testFinishedChoreCard() {
        assertChoreCard("Test a Finished Chore", Color.Red, "Tester 2")
    }

    @Test
    fun testAddChoreDialog() {//Will fail when using BottomModalPanel
        val choresViewModel = ChoresViewModel()
        for(chore in chores) {
            choresViewModel.addOrUpdateChore(chore)
        }
        composeTestExtension.use {
            setContent {
                ChoreListScreen(choresViewModel)
            }
            val testTitle = "Test Chore"
            val testDesc = "Test Chore Description"
            val choreList = onNodeWithTag("chore_list", true)
            choreList.assertExists("No chore list found")
            val fab = onNodeWithTag("fab_new_chore", true)
            fab.assertExists("No chore list found")
            fab.performClick()
            val choreTitle = onNodeWithTag("dialog_chore_title", true)
            choreTitle.assertExists("Should be displaying dialog with a textfield for the Chore's title")
            choreTitle.performTextInput(testTitle)
            val choreDesc = onNodeWithTag("dialog_chore_desc", true)
            choreDesc.assertExists("Should be displaying dialog with a textfield for the Chore's description")
            choreDesc.performTextInput(testDesc)
            val valid = onNodeWithTag("dialog_add_chore", true)
            valid.assertExists("No add chore button found")
            valid.performClick()
            waitForIdle()
            onNodeWithTag("chore_list").performScrollToNode(hasText(testTitle))
            val choreCard = onAllNodesWithTag("chore_item_card", useUnmergedTree = true)
                .filterToOne(hasAnyDescendant(hasText(testTitle)))
            choreCard.performScrollTo()
            choreCard.performTouchInput { longClick() }
            choreCard.onChildren()
                .filterToOne(hasTestTag("chore_item_description"))
                .assertTextEquals(testDesc)

        }
    }
}
