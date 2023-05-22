package com.bryanspitz.recipes.ui.add

import com.bryanspitz.recipes.testutils.startAndTest
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow

internal class SaveIngredientFeatureTest : BehaviorSpec({
    val saver: IngredientSaver = mockk()
    val onSaveIngredient = MutableSharedFlow<Any>()

    val feature = SaveIngredientFeature(
        saver = saver,
        onSaveIngredient = onSaveIngredient
    )

    coEvery { saver.saveIngredient() } returns true

    Given("feature is started") {
        feature.startAndTest {
            When("save is requested") {
                onSaveIngredient.emit(Unit)

                Then("save current ingredient") {
                    coVerify { saver.saveIngredient() }
                }
            }
        }
    }
})