package com.bryanspitz.recipes.ui.add

import com.bryanspitz.recipes.testutils.startAndTest
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow

internal class SaveInstructionFeatureTest : BehaviorSpec({
    val saver: InstructionSaver = mockk()
    val onSaveInstruction = MutableSharedFlow<Any>()

    val feature = SaveInstructionFeature(
        saver = saver,
        onSaveInstruction = onSaveInstruction
    )

    coEvery { saver.saveInstruction() } returns true

    Given("feature is started") {
        feature.startAndTest {
            When("save is requested") {
                onSaveInstruction.emit(Unit)

                Then("save current instruction") {
                    coVerify { saver.saveInstruction() }
                }
            }
        }
    }
})