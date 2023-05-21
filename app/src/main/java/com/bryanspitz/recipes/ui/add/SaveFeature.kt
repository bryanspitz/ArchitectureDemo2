package com.bryanspitz.recipes.ui.add

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import com.bryanspitz.recipes.R
import com.bryanspitz.recipes.architecture.Feature
import com.bryanspitz.recipes.model.recipe.Ingredient
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class SaveFeature @Inject constructor(
    @AddComponent.Title private val title: State<String>,
    private val ingredients: MutableState<List<Ingredient>>,
    private val saver: IngredientSaver,
    @AddComponent.Save private val onSave: MutableSharedFlow<Any>,
    private val activity: AddActivity,
    private val errorState: SnackbarHostState
) : Feature {

    override suspend fun start() {
        onSave.collectLatest {
            if (saver.saveIngredient()) {
                if (title.value.isBlank()) {
                    errorState.showSnackbar(activity.getString(R.string.error_title_blank))
                } else if (ingredients.value.isEmpty()) {
                    errorState.showSnackbar(activity.getString(R.string.error_ingredients_empty))
                }
            }
        }
    }
}