package com.bryanspitz.recipes.ui.add

import android.content.Intent
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import com.bryanspitz.recipes.R
import com.bryanspitz.recipes.architecture.Feature
import com.bryanspitz.recipes.model.recipe.Ingredient
import com.bryanspitz.recipes.model.recipe.Recipe
import com.bryanspitz.recipes.model.recipe.RecipeSummary
import com.bryanspitz.recipes.repository.recipe.PostNewRecipe
import com.bryanspitz.recipes.ui.detail.DetailActivity
import com.bryanspitz.recipes.ui.detail.PARAM_RECIPE_ID
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class SaveFeature @Inject constructor(
    @AddComponent.Title private val title: State<String>,
    @AddComponent.Description private val description: State<String>,
    private val ingredients: MutableState<List<Ingredient>>,
    private val ingredientSaver: IngredientSaver,
    private val instructions: MutableState<List<String>>,
    private val instructionSaver: InstructionSaver,
    @AddComponent.Save private val onSave: MutableSharedFlow<Any>,
    private val activity: AddActivity,
    private val errorState: SnackbarHostState,
    private val postNewRecipe: PostNewRecipe
) : Feature {

    override suspend fun start() {
        onSave.collectLatest {
            if (ingredientSaver.saveIngredient() && instructionSaver.saveInstruction()) {
                if (title.value.isBlank()) {
                    errorState.showSnackbar(activity.getString(R.string.error_title_blank))
                } else if (ingredients.value.isEmpty()) {
                    errorState.showSnackbar(activity.getString(R.string.error_ingredients_empty))
                } else if (instructions.value.isEmpty()) {
                    errorState.showSnackbar(activity.getString(R.string.error_instructions_empty))
                } else {
                    val saved = postNewRecipe.post(
                        Recipe(
                            summary = RecipeSummary(
                                id = "",
                                title = title.value,
                                description = description.value,
                                imgUrl = ""
                            ),
                            ingredients = ingredients.value,
                            instructions = instructions.value,
                            notes = ""
                        )
                    )
//                    activity.startActivity(
//                        Intent(activity, DetailActivity::class.java)
//                            .putExtra(PARAM_RECIPE_ID, saved.summary.id)
//                    )
                    activity.finish()
                }
            }
        }
    }
}