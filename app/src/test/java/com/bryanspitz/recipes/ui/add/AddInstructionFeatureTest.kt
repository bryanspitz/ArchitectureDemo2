package com.bryanspitz.recipes.ui.add

import androidx.compose.runtime.mutableStateOf
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

internal class AddInstructionFeatureTest : BehaviorSpec({
    val ingredientSaver: IngredientSaver = mockk()
    val instructionSaver: InstructionSaver = mockk()
    val instructions = mutableStateOf(listOf("Do Step 1."))
    val onAddInstruction = MutableSharedFlow<Any>()
    val onEditInstruction: MutableSharedFlow<Int> = mockk(relaxUnitFun = true)

    val feature = AddInstructionFeature(
        ingredientSaver = ingredientSaver,
        instructionSaver = instructionSaver,
        instructions = instructions,
        onAddInstruction = onAddInstruction,
        onEditInstruction = onEditInstruction
    )

    val ingredientSuccess = coEvery { ingredientSaver.saveIngredient() }.deferReturn()
    val instructionSuccess = coEvery { instructionSaver.saveInstruction() }.deferReturn()

    Given("feature is started") {
        feature.startAndTest {
            When("instruction is added") {
                onAddInstruction.emit(Unit)

                And("saving current ingredient succeeds") {
                    ingredientSuccess.complete(true)

                    And("saving current instruction succeeds") {
                        instructionSuccess.complete(true)

                        Then("append blank instruction to the list") {
                            instructions.value shouldBe listOf(
                                "Do Step 1.",
                                ""
                            )
                        }
                        Then("trigger an instruction edit") {
                            coVerify { onEditInstruction.emit(1) }
                        }
                    }
                    And("saving current instruction fails") {
                        instructionSuccess.complete(false)

                        Then("do not change list") {
                            instructions.value shouldBe listOf(
                                "Do Step 1."
                            )
                        }
                        Then("do not edit") {
                            verify { onEditInstruction wasNot called }
                        }
                    }
                }
                And("saving current ingredient fails") {
                    ingredientSuccess.complete(false)


                    Then("do not change list") {
                        instructions.value shouldBe listOf(
                            "Do Step 1."
                        )
                    }
                    Then("do not edit") {
                        verify { onEditInstruction wasNot called }
                    }
                }
            }
        }
    }
})