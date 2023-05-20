@file:OptIn(ExperimentalCoroutinesApi::class)

package com.bryanspitz.recipes.repository.recipe

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

internal class GetRecipeSummariesTest : BehaviorSpec({
    val cache = RecipeCache()
    val api: RecipeService = mockk()

    val getSummaries = GetRecipeSummaries(cache, api, UnconfinedTestDispatcher())

    val apiResult = coEvery { api.getRecipeSummaries() }.deferReturn()

    Given("result is collected") {
        getSummaries.getRecipeSummaries().collectAndTest {

            When("api succeeds") {
                val fetchedSummaries = listOf(
                    RecipeSummary(
                        id = "id0",
                        title = "Pflaumenkuchen",
                        description = "A traditional German plum cake.",
                        imgUrl = ""
                    )
                )
                apiResult.complete(fetchedSummaries)

                Then("update the cache") {
                    cache.data.value.summaries shouldBe fetchedSummaries
                }
                Then("emit the result") {
                    it.latestValue shouldBe fetchedSummaries
                }

                And("the cache is updated externally") {
                    val value = listOf(
                        RecipeSummary(
                            id = "id0",
                            title = "Pflaumenkuchen",
                            description = "A traditional German plum cake.",
                            imgUrl = ""
                        ),
                        RecipeSummary(
                            id = "id1",
                            title = "Potatoes Romanoff",
                            description = "The potatoes of Russian royalty.",
                            imgUrl = ""
                        )
                    )
                    cache.mutate {
                        RecipeCacheData(summaries = value)
                    }

                    Then("emit update") {
                        it.latestValue shouldBe value
                    }
                }
            }
        }
    }
})