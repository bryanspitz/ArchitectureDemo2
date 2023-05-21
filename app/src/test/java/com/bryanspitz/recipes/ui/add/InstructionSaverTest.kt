package com.bryanspitz.recipes.ui.add

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.mutableStateOf
import com.bryanspitz.recipes.R
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

private const val EMPTY_INSTRUCTION = "empty instruction"

internal class InstructionSaverTest : BehaviorSpec({
    val activity: AddActivity = mockk()
    val instructions = mutableStateOf(listOf(""))
    val editingInstruction = mutableStateOf<EditingInstruction?>(null)
    val errorState: SnackbarHostState = mockk()

    val saver = InstructionSaver(
        activity = activity,
        instructions = instructions,
        editingInstruction = editingInstruction,
        errorState = errorState
    )

    every { activity.getString(R.string.error_instruction_blank) } returns EMPTY_INSTRUCTION
    coEvery { errorState.showSnackbar(any<String>()) } returns SnackbarResult.Dismissed

    Given("instruction is null") {
        When("instruction is saved") {
            asyncAndTest({ saver.saveInstruction() }) {
                Then("return true") {
                    await().shouldBeTrue()
                }
            }
        }
    }

    Given("instruction is empty") {
        editingInstruction.value = EditingInstruction(
            index = 0,
            instruction = ""
        )

        When("instruction is saved") {
            asyncAndTest({ saver.saveInstruction() }) {
                Then("show error message") {
                    coVerify { errorState.showSnackbar(EMPTY_INSTRUCTION) }
                }

                Then("return false") {
                    await().shouldBeFalse()
                }
            }
        }
    }
    Given("name is not empty") {
        editingInstruction.value = EditingInstruction(
            index = 0,
            instruction = "Eat all the ingredients."
        )

        When("instruction is saved") {
            asyncAndTest({ saver.saveInstruction() }) {
                Then("do not show error message") {
                    verify { errorState wasNot called }
                }
                Then("replace ingredient in list") {
                    instructions.value shouldBe listOf(
                        "Eat all the ingredients."
                    )
                }
                Then("clear editing instruction") {
                    editingInstruction.value.shouldBeNull()
                }
                Then("return true") {
                    await().shouldBeTrue()
                }
            }
        }
    }
})