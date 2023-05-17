package com.bryanspitz.recipes.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.bryanspitz.recipes.ui.theme.RecipesTheme
import com.bryanspitz.recipes.util.appComponent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val appComponent = appComponent()
            val component = remember {
                DaggerMainComponent.factory().create(
                    recipeCacheSource = appComponent
                )
            }
            val recipeSummaries by remember { component.recipeSummaries() }.collectAsState()

            RecipesTheme {
                MainLayout(
                    recipes = recipeSummaries,
                    onAdd = {}
                )
            }
        }
    }
}