package com.bryanspitz.recipes.repository.recipe

import com.bryanspitz.recipes.model.recipe.RecipeSummary
import com.bryanspitz.recipes.repository.recipe.cache.RecipeCache
import com.bryanspitz.recipes.repository.recipe.cache.RecipeCacheData
import com.bryanspitz.recipes.testutils.collectAndTest
import com.bryanspitz.recipes.testutils.latestValue
import com.bryanspitz.recipes.testutils.returnsStateFlow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

internal class GetRecipeSummariesTest : BehaviorSpec({
    val cache: RecipeCache = mockk()

    val get = GetRecipeSummaries(cache)

    val data = every { cache.data }.returnsStateFlow(RecipeCacheData())

    Given("result is collected") {
        get.getRecipeSummaries().collectAndTest {

            Then("emit initial cache value") {
                it.latestValue.shouldBeEmpty()
            }

            When("cache emits an update") {
                val value = listOf(
                    RecipeSummary(
                        id = "id",
                        title = "A Recipe",
                        description = "The description of the recipe.",
                        imgUrl = ""
                    )
                )
                data.emit(RecipeCacheData(summaries = value))

                Then("emit update") {
                    it.latestValue shouldBe value
                }
            }
        }
    }
})