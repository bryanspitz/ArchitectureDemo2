package com.bryanspitz.recipes.ui.add

import androidx.compose.runtime.mutableStateOf
import com.bryanspitz.recipes.model.recipe.Ingredient
import com.bryanspitz.recipes.testutils.startAndTest
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.MutableSharedFlow

internal class EditIngredientFeatureTest : BehaviorSpec({
    val ingredients = mutableStateOf(
        listOf(Ingredient(amount = 1f, unit = "cup", name = "apple", preparation = "diced"))
    )
    val onEditIngredient = MutableSharedFlow<Int>()
    val editingIngredient = mutableStateOf<EditingIngredient?>(null)

    val feature = EditIngredientFeature(
        ingredients = ingredients,
        onEditIngredient = onEditIngredient,
        editingIngredient = editingIngredient
    )

    Given("feature is started") {
        feature.startAndTest {
            When("ingredient is edited") {
                onEditIngredient.emit(0)

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
        }
    }
})