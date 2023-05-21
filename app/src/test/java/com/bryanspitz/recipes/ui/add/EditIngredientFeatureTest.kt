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
    val saver: IngredientSaver = mockk()
    val ingredients = mutableStateOf(
        listOf(Ingredient(amount = 1f, unit = "cup", name = "apple", preparation = "diced"))
    )
    val onEditIngredient = MutableSharedFlow<Int>()
    val editingIngredient = mutableStateOf<EditingIngredient?>(null)

    val feature = EditIngredientFeature(
        saver = saver,
        ingredients = ingredients,
        onEditIngredient = onEditIngredient,
        editingIngredient = editingIngredient
    )

    Given("feature is started") {
        feature.startAndTest {
            When("ingredient is edited") {
                val saveSuccess = coEvery { saver.saveIngredient() }.deferReturn()
                onEditIngredient.emit(0)

                And("saving current ingredient succeeds") {
                    saveSuccess.complete(true)

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
                And("saving current ingredient fails") {
                    saveSuccess.complete(false)

                    Then("do not change editing ingredient") {
                        editingIngredient.value.shouldBeNull()
                    }
                }
            }
        }
    }
})