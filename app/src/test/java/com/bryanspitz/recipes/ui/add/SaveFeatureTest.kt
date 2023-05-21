package com.bryanspitz.recipes.ui.add

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.mutableStateOf
import com.bryanspitz.recipes.R
import com.bryanspitz.recipes.model.recipe.Ingredient
import com.bryanspitz.recipes.testutils.startAndTest
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableSharedFlow

private const val BLANK_TITLE = "title must not be blank"
private const val EMPTY_INGREDIENTS = "ingredients must not be empty"

private const val TITLE = "Recipe Title"

internal class SaveFeatureTest : BehaviorSpec({
    val title = mutableStateOf("")
    val ingredients = mutableStateOf(listOf<Ingredient>())
    val onSave = MutableSharedFlow<Any>()
    val activity: AddActivity = mockk()
    val errorState: SnackbarHostState = mockk()

    val feature = SaveFeature(
        title = title,
        ingredients = ingredients,
        onSave = onSave,
        activity = activity,
        errorState = errorState
    )

    every { activity.getString(R.string.error_title_blank) } returns BLANK_TITLE
    every { activity.getString(R.string.error_ingredients_empty) } returns EMPTY_INGREDIENTS
    coEvery { errorState.showSnackbar(any<String>()) } returns SnackbarResult.Dismissed

    Given("feature is started") {
        feature.startAndTest {
            And("title is empty") {
                When("save button is clicked") {
                    onSave.emit(Unit)

                    Then("show error message") {
                        coVerify { errorState.showSnackbar(BLANK_TITLE) }
                    }
                }
            }

            And("title is blank") {
                title.value = "    "
                When("save button is clicked") {
                    onSave.emit(Unit)

                    Then("show error message") {
                        coVerify { errorState.showSnackbar(BLANK_TITLE) }
                    }
                }
            }

            And("title is not blank") {
                title.value = TITLE

                And("ingredient list is empty") {
                    When("save button is clicked") {
                        onSave.emit(Unit)

                        Then("show error message") {
                            coVerify { errorState.showSnackbar(EMPTY_INGREDIENTS) }
                        }
                    }

                }

                And("ingredients are not empty") {
                    ingredients.value = listOf(
                        Ingredient(name = "salt")
                    )

                    When("save button is clicked") {
                        onSave.emit(Unit)

                        Then("do not show error") {
                            verify { errorState wasNot called }
                        }
                    }
                }
            }
        }
    }
})