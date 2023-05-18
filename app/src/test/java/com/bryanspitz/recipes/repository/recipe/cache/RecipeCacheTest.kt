package com.bryanspitz.recipes.repository.recipe.cache

import com.bryanspitz.recipes.model.recipe.RecipeSummary
import com.bryanspitz.recipes.testutils.collectAndTest
import com.bryanspitz.recipes.testutils.latestValue
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe

internal class RecipeCacheTest : BehaviorSpec({
    val cache = RecipeCache()

    Given("data is collected") {
        cache.data.collectAndTest {

            Then("emit initial data") {
                it.latestValue shouldBe listOf(
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
            }

            When("data is cleared") {
                cache.mutate { emptyList() }

                Then("emit empty list") {
                    it.latestValue.shouldBeEmpty()
                }
            }
            When("data is mutated") {
                cache.mutate {
                    it.sortedBy { it.title }
                }

                Then("emit mutated data") {
                    it.latestValue shouldBe listOf(
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
                }
            }
        }
    }
})