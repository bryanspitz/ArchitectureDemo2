package com.bryanspitz.recipes.ui.add

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import com.bryanspitz.recipes.model.recipe.Ingredient
import com.bryanspitz.recipes.ui.theme.RecipesTheme
import com.bryanspitz.recipes.util.appComponent
import kotlinx.coroutines.launch

class AddActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val appComponent = appComponent()

            val title = rememberSaveable(Unit) { mutableStateOf("") }
            val description = rememberSaveable(Unit) { mutableStateOf("") }
            val ingredients = rememberSaveable(Unit) { mutableStateOf(listOf<Ingredient>()) }
            val editingIngredient = rememberSaveable(Unit) {
                mutableStateOf<EditingIngredient?>(null)
            }
            val instructions = rememberSaveable(Unit) { mutableStateOf(listOf<String>()) }
            val editingInstruction = rememberSaveable(Unit) {
                mutableStateOf<EditingInstruction?>(null)
            }
            val errorState = remember { SnackbarHostState() }

            val component = remember {
                DaggerAddComponent.factory().create(
                    activity = this,
                    title = title,
                    description = description,
                    ingredients = ingredients,
                    editingIngredient = editingIngredient,
                    instructions = instructions,
                    editingInstruction = editingInstruction,
                    errorState = errorState,
                    recipeCacheSource = appComponent,
                    recipeServiceSource = appComponent
                )
            }
            val scope = rememberCoroutineScope()

            LaunchedEffect(Unit) {
                component.features().launchAll()
            }

            RecipesTheme {
                AddLayout(
                    title = title.value,
                    onTitleChanged = { title.value = it },
                    description = description.value,
                    onDescriptionChanged = { description.value = it },
                    ingredients = ingredients.value,
                    onAddIngredient = { scope.launch { component.onAddIngredient().emit(Unit) } },
                    onEditIngredient = { scope.launch { component.onEditIngredient().emit(it) } },
                    onSaveIngredient = { scope.launch { component.onSaveIngredient().emit(Unit) } },
                    onDeleteIngredient = {
                        scope.launch {
                            component.onDeleteIngredient().emit(Unit)
                        }
                    },
                    editingIngredient = editingIngredient.value,
                    onEditingIngredientChanged = { editingIngredient.value = it },
                    instructions = instructions.value,
                    onAddInstruction = { scope.launch { component.onAddInstruction().emit(Unit) } },
                    onEditInstruction = { scope.launch { component.onEditInstruction().emit(it) } },
                    editingInstruction = editingInstruction.value,
                    onEditingInstructionChanged = { editingInstruction.value = it },
                    errorState = errorState,
                    onSave = { scope.launch { component.onSave().emit(Unit) } },
                    onBack = { finish() }
                )
            }
        }
    }
}