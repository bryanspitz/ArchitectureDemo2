package com.bryanspitz.recipes.repository.recipe.cache

import com.bryanspitz.recipes.model.recipe.Recipe
import com.bryanspitz.recipes.model.recipe.RecipeSummary

data class RecipeCacheData(
    val summaries: List<RecipeSummary> = emptyList(),
    val recipes: Map<String, Recipe> = emptyMap()
)
