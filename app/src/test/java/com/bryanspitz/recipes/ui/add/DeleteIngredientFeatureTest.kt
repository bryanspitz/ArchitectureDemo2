package com.bryanspitz.recipes.ui.add

import androidx.compose.runtime.mutableStateOf
import com.bryanspitz.recipes.model.recipe.Ingredient
import com.bryanspitz.recipes.testutils.startAndTest
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.MutableSharedFlow

internal class DeleteIngredientFeatureTest : BehaviorSpec({
    val ingredients = mutableStateOf(
        listOf(
            Ingredient(1f, "tbsp", "honey"),
            Ingredient(2.5f, "cups", "water"),
            Ingredient(0.5f, "tsp", "salt")
        )
    )
    val editingIngredient = mutableStateOf<EditingIngredient?>(
        EditingIngredient(1, "2.5", "cups", "water", preparation = "")
    )
    val onDeleteIngredient = MutableSharedFlow<Any>()

    val feature = DeleteIngredientFeature(
        ingredients = ingredients,
        editingIngredient = editingIngredient,
        onDeleteIngredient = onDeleteIngredient
    )

    Given("feature is started") {
        feature.startAndTest {
            When("delete is requested") {
                onDeleteIngredient.emit(Unit)

                Then("remove ingredient from list") {
                    ingredients.value shouldBe listOf(
                        Ingredient(1f, "tbsp", "honey"),
                        Ingredient(0.5f, "tsp", "salt")
                    )
                }
                Then("delete editing ingredient") {
                    editingIngredient.value.shouldBeNull()
                }
            }
        }
    }
})