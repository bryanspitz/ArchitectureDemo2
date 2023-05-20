package com.bryanspitz.recipes.ui.detail

import com.bryanspitz.recipes.repository.recipe.SaveRecipeNotes
import com.bryanspitz.recipes.testutils.startAndTest
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow

private const val RECIPE_ID = "recipeId"
private const val NOTES = "The new notes."

internal class SaveNotesFeatureTest : BehaviorSpec({
    val saveRecipeNotes: SaveRecipeNotes = mockk(relaxUnitFun = true)
    val onSaveNotes = MutableSharedFlow<String>()

    val feature = SaveNotesFeature(
        recipeId = RECIPE_ID,
        saveRecipeNotes = saveRecipeNotes,
        onSaveNotes = onSaveNotes
    )

    Given("feature is started") {
        feature.startAndTest {

            When("notes are saved") {
                onSaveNotes.emit(NOTES)

                Then("save to repository") {
                    coVerify { saveRecipeNotes.save(RECIPE_ID, NOTES) }
                }
            }
        }
    }
})