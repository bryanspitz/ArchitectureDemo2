package com.bryanspitz.recipes.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.bryanspitz.recipes.ui.theme.RecipesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecipesTheme {
                MainLayout(
                    recipes = emptyList(),
                    onAdd = {}
                )
            }
        }
    }
}