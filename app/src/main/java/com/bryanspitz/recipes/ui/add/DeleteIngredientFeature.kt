package com.bryanspitz.recipes.ui.add

import androidx.compose.runtime.MutableState
import com.bryanspitz.recipes.architecture.Feature
import com.bryanspitz.recipes.model.recipe.Ingredient
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class DeleteIngredientFeature @Inject constructor(
    private val ingredients: MutableState<List<Ingredient>>,
    private val editingIngredient: MutableState<EditingIngredient?>,
    @AddComponent.DeleteIngredient private val onDeleteIngredient: MutableSharedFlow<Any>
) : Feature {

    override suspend fun start() {
        onDeleteIngredient.collectLatest {
            ingredients.value = ingredients.value.filterIndexed { index, _ ->
                index != editingIngredient.value?.index
            }
            editingIngredient.value = null
        }
    }
}