package com.bryanspitz.recipes.ui.detail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.bryanspitz.recipes.ui.theme.RecipesTheme
import com.bryanspitz.recipes.util.appComponent
import kotlinx.coroutines.launch

const val PARAM_RECIPE_ID = "PARAM_RECIPE_ADD"

class DetailActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val appComponent = appComponent()
            val recipeId = intent.getStringExtra(PARAM_RECIPE_ID)!!

            val errorState = remember { SnackbarHostState() }

            val component = remember {
                DaggerDetailComponent.factory().create(
                    activity = this,
                    recipeId = recipeId,
                    errorState = errorState,
                    recipeCacheSource = appComponent,
                    recipeServiceSource = appComponent
                )
            }
            val recipe by remember { component.recipe() }.collectAsState()
            val scope = rememberCoroutineScope()

            LaunchedEffect(component) {
                component.features().launchAll()
            }

            RecipesTheme {
                DetailLayout(
                    recipe = recipe,
                    onSaveNotes = { scope.launch { component.onSaveNotes().emit(it) } },
                    onBack = { finish() },
                    errorState = errorState
                )
            }
        }
    }
}