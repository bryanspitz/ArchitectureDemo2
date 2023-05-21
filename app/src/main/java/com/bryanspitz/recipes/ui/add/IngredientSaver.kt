package com.bryanspitz.recipes.ui.add

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import com.bryanspitz.recipes.R
import com.bryanspitz.recipes.model.recipe.Ingredient
import javax.inject.Inject

class IngredientSaver @Inject constructor(
    private val activity: AddActivity,
    private val ingredients: MutableState<List<Ingredient>>,
    private val editingIngredient: MutableState<EditingIngredient?>,
    private val errorState: SnackbarHostState
) {
    suspend fun saveIngredient(): Boolean {
        val editing = editingIngredient.value
        if (editing == null) {
            return true
        } else {
            val amount = editing.amount.toFloatOrNull()
            val unit = editing.unit.ifBlank { null }
            val name = editing.name
            val preparation = editing.preparation.ifBlank { null }
            if (editing.amount.isNotBlank() && amount == null) {
                errorState.showSnackbar(activity.getString(R.string.error_amount_not_number))
                return false
            } else if (name.isBlank()) {
                errorState.showSnackbar(activity.getString(R.string.error_ingredient_name_empty))
                return false
            } else {
                ingredients.value = ingredients.value.mapIndexed { index, ingredient ->
                    if (index == editing.index) {
                        Ingredient(amount, unit, name, preparation)
                    } else {
                        ingredient
                    }
                }
                editingIngredient.value = null
                return true
            }
        }
    }
}