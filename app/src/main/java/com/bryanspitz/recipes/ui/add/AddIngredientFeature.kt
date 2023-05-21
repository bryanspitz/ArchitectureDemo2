package com.bryanspitz.recipes.ui.add

import androidx.compose.runtime.MutableState
import com.bryanspitz.recipes.architecture.Feature
import com.bryanspitz.recipes.model.recipe.Ingredient
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class AddIngredientFeature @Inject constructor(
    private val saver: IngredientSaver,
    private val ingredients: MutableState<List<Ingredient>>,
    @AddComponent.Ingredients private val onAddIngredient: MutableSharedFlow<Any>,
    @AddComponent.Ingredients private val onEditIngredient: MutableSharedFlow<Int>
) : Feature {

    override suspend fun start() {
        onAddIngredient.collectLatest {
            if (saver.saveIngredient()) {
                ingredients.value = ingredients.value + Ingredient(name = "")
                onEditIngredient.emit(ingredients.value.size - 1)
            }
        }
    }
}