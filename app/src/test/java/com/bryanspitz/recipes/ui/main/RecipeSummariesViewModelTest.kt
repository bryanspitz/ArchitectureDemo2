@file:OptIn(ExperimentalCoroutinesApi::class)

package com.bryanspitz.recipes.ui.main

import com.bryanspitz.recipes.model.recipe.RecipeSummary
import com.bryanspitz.recipes.repository.recipe.GetRecipeSummaries
import com.bryanspitz.recipes.testutils.collectAndTest
import com.bryanspitz.recipes.testutils.latestValue
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.jupiter.api.Assertions.*

internal class RecipeSummariesViewModelTest : BehaviorSpec({
    val getRecipeSummaries: GetRecipeSummaries = mockk()

    every { getRecipeSummaries.getRecipeSummaries() } returns flowOf(
        listOf(
            RecipeSummary(
                id = "b",
                title = "B",
                description = "whatever",
                imgUrl = "src"
            ),
            RecipeSummary(
                id = "a",
                title = "A",
                description = "whatever",
                imgUrl = "src"
            )
        )
    )

    val flow = RecipeSummariesViewModel().recipeSummaries(
        getRecipeSummaries = getRecipeSummaries,
        dispatcher = UnconfinedTestDispatcher()
    )

    Given("flow is collected") {
        flow.collectAndTest {
            Then("emit results sorted alphabetically") {
                it.latestValue shouldBe listOf(
                    RecipeSummary(
                        id = "a",
                        title = "A",
                        description = "whatever",
                        imgUrl = "src"
                    ),
                    RecipeSummary(
                        id = "b",
                        title = "B",
                        description = "whatever",
                        imgUrl = "src"
                    )
                )
            }
        }
    }
})