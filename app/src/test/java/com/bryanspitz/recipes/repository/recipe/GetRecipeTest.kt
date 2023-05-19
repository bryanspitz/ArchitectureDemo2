package com.bryanspitz.recipes.repository.recipe

import com.bryanspitz.recipes.model.recipe.Recipe
import com.bryanspitz.recipes.model.recipe.RecipeSummary
import com.bryanspitz.recipes.repository.recipe.cache.RecipeCache
import com.bryanspitz.recipes.repository.recipe.cache.RecipeCacheData
import com.bryanspitz.recipes.testutils.collectAndTest
import com.bryanspitz.recipes.testutils.latestValue
import com.bryanspitz.recipes.testutils.returnsStateFlow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

private const val RECIPE_ID = "recipeId"

internal class GetRecipeTest : BehaviorSpec({
    val cache: RecipeCache = mockk()

    val getRecipe = GetRecipe(cache)

    val data = every { cache.data }.returnsStateFlow(RecipeCacheData())

    Given("result is collected") {
        getRecipe.getRecipe(RECIPE_ID).collectAndTest {

            Then("emit initial cache value") {
                it.latestValue.shouldBeNull()
            }

            When("cache emits an update") {
                val value = Recipe(
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
                data.emit(
                    RecipeCacheData(
                        recipes = mapOf(
                            RECIPE_ID to value,
                            "another id" to mockk()
                        )
                    )
                )

                Then("emit update") {
                    it.latestValue shouldBe value
                }
            }
        }
    }
})