package com.bryanspitz.recipes.ui.add

import androidx.compose.runtime.mutableStateOf
import com.bryanspitz.recipes.model.recipe.Ingredient
import com.bryanspitz.recipes.testutils.startAndTest
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow

internal class AddIngredientFeatureTest : BehaviorSpec({
    val ingredients = mutableStateOf(
        listOf(Ingredient(amount = 1f, name = "apple"))
    )
    val onAddIngredient = MutableSharedFlow<Any>()
    val onEditIngredient: MutableSharedFlow<Int> = mockk(relaxUnitFun = true)

    val feature = AddIngredientFeature(
        ingredients = ingredients,
        onAddIngredient = onAddIngredient,
        onEditIngredient = onEditIngredient
    )

    Given("feature is started") {
        feature.startAndTest {
            When("ingredient is added") {
                onAddIngredient.emit(Unit)

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
        }
    }
})