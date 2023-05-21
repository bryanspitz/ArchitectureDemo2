package com.bryanspitz.recipes.ui.add

import androidx.compose.runtime.mutableStateOf
import com.bryanspitz.recipes.testutils.deferReturn
import com.bryanspitz.recipes.testutils.startAndTest
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow

internal class EditInstructionFeatureTest : BehaviorSpec({
    val ingredientSaver: IngredientSaver = mockk()
    val instructionSaver: InstructionSaver = mockk()
    val instructions = mutableStateOf(
        listOf("Do Step 1.")
    )
    val onEditInstruction = MutableSharedFlow<Int>()
    val editingInstruction = mutableStateOf<EditingInstruction?>(null)

    val feature = EditInstructionFeature(
        ingredientSaver = ingredientSaver,
        instructionSaver = instructionSaver,
        instructions = instructions,
        onEditInstruction = onEditInstruction,
        editingInstruction = editingInstruction
    )

    val ingredientSuccess = coEvery { ingredientSaver.saveIngredient() }.deferReturn()
    val instructionSuccess = coEvery { instructionSaver.saveInstruction() }.deferReturn()

    Given("feature is started") {
        feature.startAndTest {
            When("ingredient is edited") {
                onEditInstruction.emit(0)

                And("saving current ingredient succeeds") {
                    ingredientSuccess.complete(true)

                    And("saving current instruction succeeds") {
                        instructionSuccess.complete(true)

                        Then("create instruction scratch pad") {
                            editingInstruction.value shouldBe EditingInstruction(
                                index = 0,
                                instruction = "Do Step 1."
                            )
                        }
                    }
                    And("saving current instruction fails") {
                        instructionSuccess.complete(false)

                        Then("do not change editing ingredient") {
                            editingInstruction.value.shouldBeNull()
                        }
                    }
                }
                And("saving current ingredient fails") {
                    ingredientSuccess.complete(false)

                    Then("do not change editing ingredient") {
                        editingInstruction.value.shouldBeNull()
                    }
                }
            }
        }
    }
})