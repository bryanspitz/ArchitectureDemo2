@file:OptIn(ExperimentalCoroutinesApi::class)

package com.bryanspitz.recipes.repository.recipe

import com.bryanspitz.recipes.model.recipe.Recipe
import com.bryanspitz.recipes.model.recipe.RecipeSummary
import com.bryanspitz.recipes.repository.recipe.api.RecipeService
import com.bryanspitz.recipes.repository.recipe.cache.RecipeCache
import com.bryanspitz.recipes.repository.recipe.cache.RecipeCacheData
import com.bryanspitz.recipes.testutils.collectAndTest
import com.bryanspitz.recipes.testutils.deferReturn
import com.bryanspitz.recipes.testutils.latestValue
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher

private const val RECIPE_ID = "recipeId"

internal class GetRecipeTest : BehaviorSpec({
    val cache = RecipeCache()
    val api: RecipeService = mockk()

    val getRecipe = GetRecipe(cache, api, UnconfinedTestDispatcher())

    val apiResult = coEvery { api.getRecipe(RECIPE_ID) }.deferReturn()

    Given("result is collected") {
        getRecipe.getRecipe(RECIPE_ID).collectAndTest {

            When("api succeeds") {
                val fetchedRecipe = Recipe(
                    summary = RecipeSummary(
                        id = RECIPE_ID,
                        title = "A Recipe",
                        description = "The description of the recipe.",
                        imgUrl = ""
                    ),
                    ingredients = emptyList(),
                    instructions = emptyList(),
                    notes = ""
                )
                apiResult.complete(fetchedRecipe)

                Then("update the cache") {
                    cache.data.value.recipes[RECIPE_ID] shouldBe fetchedRecipe
                }
                Then("emit the result") {
                    it.latestValue shouldBe fetchedRecipe
                }

                And("the cache is updated externally") {
                    val value = Recipe(
                        summary = RecipeSummary(
                            id = RECIPE_ID,
                            title = "Updated Recipe",
                            description = "The description of the recipe.",
                            imgUrl = ""
                        ),
                        ingredients = emptyList(),
                        instructions = emptyList(),
                        notes = ""
                    )
                    cache.mutate {
                        RecipeCacheData(
                            recipes = mapOf(
                                RECIPE_ID to value,
                                "another id" to mockk()
                            )
                        )
                    }

                    Then("emit update") {
                        it.latestValue shouldBe value
                    }
                }
            }
        }
    }
})