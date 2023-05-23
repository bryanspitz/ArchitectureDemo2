package com.bryanspitz.recipes.ui.detail

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import com.bryanspitz.recipes.R
import com.bryanspitz.recipes.repository.recipe.SaveRecipeNotes
import com.bryanspitz.recipes.testutils.deferReturn
import com.bryanspitz.recipes.testutils.startAndTest
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableSharedFlow
import java.io.IOException

private const val RECIPE_ID = "recipeId"
private const val NOTES = "The new notes."
private const val ERROR = "error"

internal class SaveNotesFeatureTest : BehaviorSpec({
    val saveRecipeNotes: SaveRecipeNotes = mockk(relaxUnitFun = true)
    val onSaveNotes = MutableSharedFlow<String>()
    val activity: DetailActivity = mockk()
    val errorState: SnackbarHostState = mockk()

    val feature = SaveNotesFeature(
        recipeId = RECIPE_ID,
        saveRecipeNotes = saveRecipeNotes,
        onSaveNotes = onSaveNotes,
        activity = activity,
        errorState = errorState
    )

    every { activity.getString(R.string.error_notes_failed) } returns ERROR
    val saveResult = coEvery { saveRecipeNotes.save(RECIPE_ID, any()) }.deferReturn()
    coEvery { errorState.showSnackbar(any<String>()) } returns SnackbarResult.Dismissed

    Given("feature is started") {
        feature.startAndTest {

            When("notes are saved") {
                onSaveNotes.emit(NOTES)

                Then("save to repository") {
                    coVerify { saveRecipeNotes.save(RECIPE_ID, NOTES) }
                }

                And("saving succeeds") {
                    saveResult.complete(mockk())

                    Then("do not show error") {
                        verify { errorState wasNot called }
                    }
                }

                And("saving fails") {
                    saveResult.completeExceptionally(IOException())

                    Then("show error") {
                        coVerify { errorState.showSnackbar(ERROR) }
                    }
                }
            }
        }
    }
})