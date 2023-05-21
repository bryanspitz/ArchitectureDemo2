package com.bryanspitz.recipes.ui.add

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.bryanspitz.recipes.ui.theme.RecipesTheme
import com.bryanspitz.recipes.util.appComponent
import kotlinx.coroutines.launch

class AddActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val appComponent = appComponent()
            val component = remember {
                DaggerAddComponent.factory().create(
                    recipeCacheSource = appComponent,
                    recipeServiceSource = appComponent
                )
            }
            val scope = rememberCoroutineScope()

            RecipesTheme {
                AddLayout(
                    title = "",
                    onTitleChanged = {},
                    description = "",
                    onDescriptionChanged = {},
                    ingredients = emptyList(),
                    onEditIngredient = {},
                    editingIngredient = null,
                    onEditingIngredientChanged = {},
                    instructions = emptyList(),
                    onEditInstruction = {},
                    editingInstruction = null,
                    onEditingInstructionChanged = {},
                    onSave = { scope.launch { component.onSave().emit(Unit) } },
                    onBack = { finish() }
                )
            }
        }
    }
}