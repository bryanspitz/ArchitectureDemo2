package com.bryanspitz.recipes.repository.recipe.cache

import com.bryanspitz.recipes.model.recipe.RecipeSummary
import com.bryanspitz.recipes.testutils.collectAndTest
import com.bryanspitz.recipes.testutils.latestValue
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

internal class RecipeCacheTest : BehaviorSpec({
    val cache = RecipeCache()

    Given("cache is initialized") {
        val initialData = RecipeCacheData(
            listOf(
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
                ),
                RecipeSummary(
                    id = "id2",
                    title = "Blueberry Pie",
                    description = "Flaky crust with a decadent blueberry filling, or whatever.",
                    imgUrl = ""
                )
            )
        )
        cache.mutate { initialData }

        And("data is collected") {
            cache.data.collectAndTest {

                Then("emit initial data") {
                    it.latestValue shouldBe initialData
                }

                When("data is cleared") {
                    cache.mutate { RecipeCacheData() }

                    Then("emit empty list") {
                        it.latestValue shouldBe RecipeCacheData()
                    }
                }
                When("data is mutated") {
                    cache.mutate {
                        it.copy(it.summaries.sortedBy { it.title })
                    }

                    Then("emit mutated data") {
                        it.latestValue shouldBe RecipeCacheData(
                            listOf(
                                RecipeSummary(
                                    id = "id2",
                                    title = "Blueberry Pie",
                                    description = "Flaky crust with a decadent blueberry filling, or whatever.",
                                    imgUrl = ""
                                ),
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
                        )
                    }
                }
            }
        }
    }
})