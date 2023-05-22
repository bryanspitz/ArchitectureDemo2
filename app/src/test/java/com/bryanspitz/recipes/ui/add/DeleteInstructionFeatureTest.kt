package com.bryanspitz.recipes.ui.add

import androidx.compose.runtime.mutableStateOf
import com.bryanspitz.recipes.testutils.startAndTest
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.MutableSharedFlow

internal class DeleteInstructionFeatureTest : BehaviorSpec({
    val instructions = mutableStateOf(
        listOf(
            "Combine ingredients.",
            "Heat mixture.",
            "Dump mixture on head of enemy."
        )
    )
    val editingInstruction = mutableStateOf<EditingInstruction?>(
        EditingInstruction(1, "Heat mixture.")
    )
    val onDeleteInstruction = MutableSharedFlow<Any>()

    val feature = DeleteInstructionFeature(
        instructions = instructions,
        editingInstruction = editingInstruction,
        onDeleteInstruction = onDeleteInstruction
    )

    Given("feature is started") {
        feature.startAndTest {
            When("delete is requested") {
                onDeleteInstruction.emit(Unit)

                Then("remove instruction from list") {
                    instructions.value shouldBe listOf(
                        "Combine ingredients.",
                        "Dump mixture on head of enemy."
                    )
                }
                Then("delete editing instruction") {
                    editingInstruction.value.shouldBeNull()
                }
            }
        }
    }
})