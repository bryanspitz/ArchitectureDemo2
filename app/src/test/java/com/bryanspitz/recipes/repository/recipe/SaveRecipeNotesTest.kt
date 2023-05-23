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
import java.io.IOException

private const val RECIPE_ID = "recipeId"
private const val ORIGINAL_NOTES = "Needs more salt."
private const val NOTES = "Use less salt next time."

internal class SaveRecipeNotesTest : BehaviorSpec({
    val cache = RecipeCache()
    val api: RecipeService = mockk()

    val saveNotes = SaveRecipeNotes(cache, api, UnconfinedTestDispatcher())

    val originalRecipe = Recipe(
        summary = RecipeSummary(
            RECIPE_ID,
            "A Recipe",
            "The description of the recipe.",
            ""
        ),
        ingredients = emptyList(),
        instructions = emptyList(),
        notes = ORIGINAL_NOTES
    )
    val updatedRecipe = Recipe(
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

    cache.mutate {
        it.copy(recipes = mapOf(RECIPE_ID to originalRecipe))
    }

    val apiResult = coEvery { api.updateRecipeNotes(RECIPE_ID, NOTES) }.deferReturn()

    Given("result is collected") {
        launchAndTest({ saveNotes.save(RECIPE_ID, NOTES) }) {

            Then("optimistically update cache") {
                cache.data.value.recipes[RECIPE_ID] shouldBe updatedRecipe
            }

            When("api succeeds") {
                apiResult.complete(updatedRecipe)

                Then("cache is still updated") {
                    cache.data.value.recipes[RECIPE_ID] shouldBe updatedRecipe
                }
            }
            When("api fails") {
                it.expect(IOException::class) {
                    apiResult.completeExceptionally(IOException())

                    Then("roll back cache update") {
                        cache.data.value.recipes[RECIPE_ID] shouldBe originalRecipe
                    }
                    Then("rethrow IOException") {
                        //expected above
                    }
                }
            }
        }
    }
})