package com.bryanspitz.recipes.ui.add

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.mutableStateOf
import com.bryanspitz.recipes.R
import com.bryanspitz.recipes.model.recipe.Ingredient
import com.bryanspitz.recipes.testutils.asyncAndTest
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.mockk.called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

private const val NOT_NUMBER = "not a number"
private const val EMPTY_NAME = "empty name"

internal class IngredientSaverTest : BehaviorSpec({
    val activity: AddActivity = mockk()
    val ingredients = mutableStateOf(listOf(Ingredient(name = "")))
    val editingIngredient = mutableStateOf<EditingIngredient?>(null)
    val errorState: SnackbarHostState = mockk()

    val saver = IngredientSaver(
        activity = activity,
        ingredients = ingredients,
        editingIngredient = editingIngredient,
        errorState = errorState
    )

    every { activity.getString(R.string.error_amount_not_number) } returns NOT_NUMBER
    every { activity.getString(R.string.error_ingredient_name_empty) } returns EMPTY_NAME
    coEvery { errorState.showSnackbar(any<String>()) } returns SnackbarResult.Dismissed

    Given("ingredient is null") {
        When("ingredient is saved") {
            asyncAndTest({ saver.saveIngredient() }) {
                Then("return true") {
                    await().shouldBeTrue()
                }
            }
        }
    }

    Given("amount is empty") {
        And("name is empty") {
            editingIngredient.value = EditingIngredient(
                index = 0,
                amount = "",
                unit = "unit",
                name = "",
                preparation = "preparation"
            )

            When("ingredient is saved") {
                asyncAndTest({ saver.saveIngredient() }) {
                    Then("show error message") {
                        coVerify { errorState.showSnackbar(EMPTY_NAME) }
                    }

                    Then("return false") {
                        await().shouldBeFalse()
                    }
                }
            }
        }
        And("name is not empty") {
            editingIngredient.value = EditingIngredient(
                index = 0,
                amount = "",
                unit = "unit",
                name = "name",
                preparation = "preparation"
            )

            When("ingredient is saved") {
                asyncAndTest({ saver.saveIngredient() }) {
                    Then("do not show error message") {
                        verify { errorState wasNot called }
                    }
                    Then("replace ingredient in list") {
                        ingredients.value shouldBe listOf(
                            Ingredient(null, "unit", "name", "preparation")
                        )
                    }
                    Then("clear editing ingredient") {
                        editingIngredient.value.shouldBeNull()
                    }
                    Then("return true") {
                        await().shouldBeTrue()
                    }
                }
            }
        }
    }

    Given("amount is not a valid number") {
        editingIngredient.value = EditingIngredient(
            index = 0,
            amount = "blah",
            unit = "",
            name = "",
            preparation = ""
        )

        When("ingredient is saved") {
            asyncAndTest({ saver.saveIngredient() }) {
                Then("show error message") {
                    coVerify { errorState.showSnackbar(NOT_NUMBER) }
                }

                Then("return false") {
                    await().shouldBeFalse()
                }
            }
        }
    }

    Given("amount is valid") {
        And("name is empty") {
            editingIngredient.value = EditingIngredient(
                index = 0,
                amount = "2",
                unit = "unit",
                name = "",
                preparation = "preparation"
            )

            When("ingredient is saved") {
                asyncAndTest({ saver.saveIngredient() }) {
                    Then("show error message") {
                        coVerify { errorState.showSnackbar(EMPTY_NAME) }
                    }

                    Then("return false") {
                        await().shouldBeFalse()
                    }
                }
            }
        }
        And("name is not empty") {
            editingIngredient.value = EditingIngredient(
                index = 0,
                amount = "2",
                unit = "unit",
                name = "name",
                preparation = "preparation"
            )

            When("ingredient is saved") {
                asyncAndTest({ saver.saveIngredient() }) {
                    Then("do not show error message") {
                        verify { errorState wasNot called }
                    }
                    Then("replace ingredient in list") {
                        ingredients.value shouldBe listOf(
                            Ingredient(2f, "unit", "name", "preparation")
                        )
                    }
                    Then("clear editing ingredient") {
                        editingIngredient.value.shouldBeNull()
                    }
                    Then("return true") {
                        await().shouldBeTrue()
                    }
                }
            }
        }
    }
})