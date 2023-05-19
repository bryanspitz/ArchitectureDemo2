package com.bryanspitz.recipes.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.bryanspitz.recipes.ui.theme.RecipesTheme
import com.bryanspitz.recipes.util.appComponent
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val appComponent = appComponent()
            val component = remember {
                DaggerMainComponent.factory().create(
                    activity = this,
                    recipeCacheSource = appComponent
                )
            }
            val recipeSummaries by remember { component.recipeSummaries() }.collectAsState()
            val scope = rememberCoroutineScope()

            LaunchedEffect(component) {
                component.features().launchAll()
            }

            RecipesTheme {
                MainLayout(
                    recipes = recipeSummaries,
                    onAdd = { scope.launch { component.onAdd().emit(Unit) } },
                    onClick = { scope.launch { component.onClickRecipe().emit(it) } }
                )
            }
        }
    }
}