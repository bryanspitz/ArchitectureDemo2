package com.bryanspitz.recipes.ui.add

import androidx.compose.runtime.mutableStateOf
import com.bryanspitz.recipes.model.recipe.Ingredient
import com.bryanspitz.recipes.testutils.deferReturn
import com.bryanspitz.recipes.testutils.startAndTest
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableSharedFlow

internal class AddIngredientFeatureTest : BehaviorSpec({
    val ingredientSaver: IngredientSaver = mockk()
    val instructionSaver: InstructionSaver = mockk()
    val ingredients = mutableStateOf(
        listOf(Ingredient(amount = 1f, name = "apple"))
    )
    val onAddIngredient = MutableSharedFlow<Any>()
    val onEditIngredient: MutableSharedFlow<Int> = mockk(relaxUnitFun = true)

    val feature = AddIngredientFeature(
        ingredientSaver = ingredientSaver,
        instructionSaver = instructionSaver,
        ingredients = ingredients,
        onAddIngredient = onAddIngredient,
        onEditIngredient = onEditIngredient
    )

    val ingredientSuccess = coEvery { ingredientSaver.saveIngredient() }.deferReturn()
    val instructionSuccess = coEvery { instructionSaver.saveInstruction() }.deferReturn()

    Given("feature is started") {
        feature.startAndTest {
            When("ingredient is added") {
                onAddIngredient.emit(Unit)

                And("saving current ingredient succeeds") {
                    ingredientSuccess.complete(true)

                    And("saving current instruction succeeds") {
                        instructionSuccess.complete(true)

                        Then("append blank ingredient to the list") {
                            ingredients.value shouldBe listOf(
                                Ingredient(amount = 1f, name = "apple"),
                                Ingredient(name = "")
                            )
                        }
                        Then("trigger an ingredient edit") {
                            coVerify { onEditIngredient.emit(1) }
                        }
                    }
                    And("saving current instruction fails") {
                        instructionSuccess.complete(false)

                        Then("do not change list") {
                            ingredients.value shouldBe listOf(
                                Ingredient(amount = 1f, name = "apple")
                            )
                        }
                        Then("do not edit") {
                            verify { onEditIngredient wasNot called }
                        }
                    }
                }
                And("saving current ingredient fails") {
                    ingredientSuccess.complete(false)

                    Then("do not change list") {
                        ingredients.value shouldBe listOf(Ingredient(amount = 1f, name = "apple"))
                    }
                    Then("do not edit") {
                        verify { onEditIngredient wasNot called }
                    }
                }
            }
        }
    }
})