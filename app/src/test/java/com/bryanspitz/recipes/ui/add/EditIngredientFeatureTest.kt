package com.bryanspitz.recipes.ui.add

import androidx.compose.runtime.mutableStateOf
import com.bryanspitz.recipes.model.recipe.Ingredient
import com.bryanspitz.recipes.testutils.deferReturn
import com.bryanspitz.recipes.testutils.startAndTest
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow

internal class EditIngredientFeatureTest : BehaviorSpec({
    val ingredientSaver: IngredientSaver = mockk()
    val instructionSaver: InstructionSaver = mockk()
    val ingredients = mutableStateOf(
        listOf(Ingredient(amount = 1f, unit = "cup", name = "apple", preparation = "diced"))
    )
    val onEditIngredient = MutableSharedFlow<Int>()
    val editingIngredient = mutableStateOf<EditingIngredient?>(null)

    val feature = EditIngredientFeature(
        ingredientSaver = ingredientSaver,
        instructionSaver = instructionSaver,
        ingredients = ingredients,
        onEditIngredient = onEditIngredient,
        editingIngredient = editingIngredient
    )

    val ingredientSuccess = coEvery { ingredientSaver.saveIngredient() }.deferReturn()
    val instructionSuccess = coEvery { instructionSaver.saveInstruction() }.deferReturn()

    Given("feature is started") {
        feature.startAndTest {
            When("ingredient is edited") {
                onEditIngredient.emit(0)

                And("saving current ingredient succeeds") {
                    ingredientSuccess.complete(true)

                    And("saving current instruction succeeds") {
                        instructionSuccess.complete(true)

                        Then("create ingredient scratch pad") {
                            editingIngredient.value shouldBe EditingIngredient(
                                index = 0,
                                amount = "1.0",
                                unit = "cup",
                                name = "apple",
                                preparation = "diced"
                            )
                        }
                    }
                    And("saving current instruction fails") {
                        instructionSuccess.complete(false)

                        Then("do not change editing ingredient") {
                            editingIngredient.value.shouldBeNull()
                        }
                    }
                }
                And("saving current ingredient fails") {
                    ingredientSuccess.complete(false)

                    Then("do not change editing ingredient") {
                        editingIngredient.value.shouldBeNull()
                    }
                }
            }
        }
    }
})