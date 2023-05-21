package com.bryanspitz.recipes.ui.add

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.mutableStateOf
import com.bryanspitz.recipes.R
import com.bryanspitz.recipes.model.recipe.Ingredient
import com.bryanspitz.recipes.model.recipe.Recipe
import com.bryanspitz.recipes.model.recipe.RecipeSummary
import com.bryanspitz.recipes.repository.recipe.PostNewRecipe
import com.bryanspitz.recipes.testutils.deferReturn
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
private const val EMPTY_INSTRUCTIONS = "instructions must not be empty"

private const val TITLE = "Recipe Title"
private const val RECIPE_ID = "recipeId"

internal class SaveFeatureTest : BehaviorSpec({
    val title = mutableStateOf("")
    val description = mutableStateOf("")
    val ingredients = mutableStateOf(listOf<Ingredient>())
    val ingredientSaver: IngredientSaver = mockk()
    val instructions = mutableStateOf(listOf<String>())
    val instructionSaver: InstructionSaver = mockk()
    val onSave = MutableSharedFlow<Any>()
    val activity: AddActivity = mockk(relaxUnitFun = true)
    val errorState: SnackbarHostState = mockk()
    val postRecipe: PostNewRecipe = mockk()

    val feature = SaveFeature(
        title = title,
        description = description,
        ingredients = ingredients,
        ingredientSaver = ingredientSaver,
        instructions = instructions,
        instructionSaver = instructionSaver,
        onSave = onSave,
        activity = activity,
        errorState = errorState,
        postNewRecipe = postRecipe
    )

    every { activity.getString(R.string.error_title_blank) } returns BLANK_TITLE
    every { activity.getString(R.string.error_ingredients_empty) } returns EMPTY_INGREDIENTS
    every { activity.getString(R.string.error_instructions_empty) } returns EMPTY_INSTRUCTIONS
    coEvery { errorState.showSnackbar(any<String>()) } returns SnackbarResult.Dismissed
    val ingredientResult = coEvery { ingredientSaver.saveIngredient() }.deferReturn()
    val instructionResult = coEvery { instructionSaver.saveInstruction() }.deferReturn()
    coEvery { postRecipe.post(any()) } answers {
        arg<Recipe>(0).let { it.copy(summary = it.summary.copy(id = RECIPE_ID)) }
    }

    Given("feature is started") {
        feature.startAndTest {

            And("title is empty") {
                When("save button is clicked") {
                    onSave.emit(Unit)

                    And("ingredient saver succeeds") {
                        ingredientResult.complete(true)

                        And("instruction saver succeeds") {
                            instructionResult.complete(true)

                            Then("show error message") {
                                coVerify { errorState.showSnackbar(BLANK_TITLE) }
                            }
                        }
                        And("instruction saver fails") {
                            instructionResult.complete(false)

                            Then("do not show error message") {
                                verify { errorState wasNot called }
                            }
                        }
                    }
                    And("ingredient saver fails") {
                        ingredientResult.complete(false)

                        Then("do not show error message") {
                            verify { errorState wasNot called }
                        }
                    }
                }
            }

            And("title is blank") {
                title.value = "    "
                When("save button is clicked") {
                    onSave.emit(Unit)

                    And("ingredient saver succeeds") {
                        ingredientResult.complete(true)

                        And("instruction saver succeeds") {
                            instructionResult.complete(true)

                            Then("show error message") {
                                coVerify { errorState.showSnackbar(BLANK_TITLE) }
                            }
                        }
                        And("instruction saver fails") {
                            instructionResult.complete(false)

                            Then("do not show error message") {
                                verify { errorState wasNot called }
                            }
                        }
                    }
                    And("ingredient saver fails") {
                        ingredientResult.complete(false)

                        Then("do not show error message") {
                            verify { errorState wasNot called }
                        }
                    }
                }
            }

            And("title is not blank") {
                title.value = TITLE

                And("ingredient list is empty") {
                    When("save button is clicked") {
                        onSave.emit(Unit)

                        And("ingredient saver succeeds") {
                            ingredientResult.complete(true)

                            And("instruction saver succeeds") {
                                instructionResult.complete(true)

                                Then("show error message") {
                                    coVerify { errorState.showSnackbar(EMPTY_INGREDIENTS) }
                                }
                            }
                            And("instruction saver fails") {
                                instructionResult.complete(false)

                                Then("do not show error message") {
                                    verify { errorState wasNot called }
                                }
                            }
                        }
                        And("ingredient saver fails") {
                            ingredientResult.complete(false)

                            Then("do not show error message") {
                                verify { errorState wasNot called }
                            }
                        }
                    }

                }

                And("ingredients are not empty") {
                    ingredients.value = listOf(
                        Ingredient(name = "salt")
                    )

                    And("instructions are empty") {
                        When("save button is clicked") {
                            onSave.emit(Unit)

                            And("ingredient saver succeeds") {
                                ingredientResult.complete(true)

                                And("instruction saver succeeds") {
                                    instructionResult.complete(true)

                                    Then("show error message") {
                                        coVerify { errorState.showSnackbar(EMPTY_INSTRUCTIONS) }
                                    }
                                }
                                And("instruction saver fails") {
                                    instructionResult.complete(false)

                                    Then("do not show error message") {
                                        verify { errorState wasNot called }
                                    }
                                }
                            }
                            And("ingredient saver fails") {
                                ingredientResult.complete(false)

                                Then("do not show error message") {
                                    verify { errorState wasNot called }
                                }
                            }
                        }

                    }

                    And("instructions are not empty") {
                        instructions.value = listOf("Pray.")

                        When("save button is clicked") {
                            onSave.emit(Unit)

                            And("ingredient saver succeeds") {
                                ingredientResult.complete(true)

                                And("instruction saver succeeds") {
                                    instructionResult.complete(true)

                                    Then("do not show error message") {
                                        verify { errorState wasNot called }
                                    }
                                    Then("post recipe") {
                                        coVerify {
                                            postRecipe.post(
                                                Recipe(
                                                    summary = RecipeSummary(
                                                        id = "",
                                                        title = TITLE,
                                                        description = "",
                                                        imgUrl = ""
                                                    ),
                                                    ingredients = listOf(
                                                        Ingredient(name = "salt")
                                                    ),
                                                    instructions = listOf("Pray."),
                                                    notes = ""
                                                )
                                            )
                                        }
                                    }
                                }
                                And("instruction saver fails") {
                                    instructionResult.complete(false)

                                    Then("do not show error message") {
                                        verify { errorState wasNot called }
                                    }
                                }
                            }
                            And("ingredient saver fails") {
                                ingredientResult.complete(false)

                                And("instruction saver succeeds") {
                                    instructionResult.complete(true)

                                    Then("do not show error message") {
                                        verify { errorState wasNot called }
                                    }
                                }
                                And("instruction saver fails") {
                                    instructionResult.complete(false)

                                    Then("do not show error message") {
                                        verify { errorState wasNot called }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
})