package com.bryanspitz.recipes.ui.add

import androidx.compose.runtime.MutableState
import com.bryanspitz.recipes.architecture.Feature
import com.bryanspitz.recipes.model.recipe.Ingredient
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class EditIngredientFeature @Inject constructor(
    private val ingredientSaver: IngredientSaver,
    private val instructionSaver: InstructionSaver,
    private val ingredients: MutableState<List<Ingredient>>,
    @AddComponent.Ingredients private val onEditIngredient: MutableSharedFlow<Int>,
    private val editingIngredient: MutableState<EditingIngredient?>
) : Feature {

    override suspend fun start() {
        onEditIngredient.collectLatest { index ->
            if (ingredientSaver.saveIngredient() && instructionSaver.saveInstruction()) {
                val ingredient = ingredients.value[index]
                editingIngredient.value = EditingIngredient(
                    index = index,
                    amount = ingredient.amount?.toString() ?: "",
                    unit = ingredient.unit ?: "",
                    name = ingredient.name,
                    preparation = ingredient.preparation ?: ""
                )
            }
        }
    }
}