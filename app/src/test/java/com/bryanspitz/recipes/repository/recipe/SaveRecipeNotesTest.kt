@file:OptIn(ExperimentalCoroutinesApi::class)

package com.bryanspitz.recipes.repository.recipe

import com.bryanspitz.recipes.model.recipe.Recipe
import com.bryanspitz.recipes.model.recipe.RecipeSummary
import com.bryanspitz.recipes.repository.recipe.api.RecipeService
import com.bryanspitz.recipes.repository.recipe.cache.RecipeCache
import com.bryanspitz.recipes.testutils.deferReturn
import com.bryanspitz.recipes.testutils.launchAndTest
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher

private const val RECIPE_ID = "recipeId"
private const val NOTES = "Use less salt next time."

internal class SaveRecipeNotesTest : BehaviorSpec({
    val cache = RecipeCache()
    val api: RecipeService = mockk()

    val saveNotes = SaveRecipeNotes(cache, api, UnconfinedTestDispatcher())

    val apiResult = coEvery { api.updateRecipeNotes(RECIPE_ID, NOTES) }.deferReturn()

    Given("result is collected") {
        launchAndTest({ saveNotes.save(RECIPE_ID, NOTES) }) {

            When("api succeeds") {
                val savedRecipe = Recipe(
                    summary = RecipeSummary(
                        id = RECIPE_ID,
                        title = "A Recipe",
                        description = "The description of the recipe.",
                        imgUrl = ""
                    ),
                    ingredients = emptyList(),
                    instructions = emptyList(),
                    notes = NOTES
                )
                apiResult.complete(savedRecipe)

                Then("update the cache") {
                    cache.data.value.recipes[RECIPE_ID] shouldBe savedRecipe
                }
            }
        }
    }
})