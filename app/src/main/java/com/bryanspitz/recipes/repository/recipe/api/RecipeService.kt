package com.bryanspitz.recipes.repository.recipe.api

import com.bryanspitz.recipes.model.recipe.Recipe
import com.bryanspitz.recipes.model.recipe.RecipeSummary

interface RecipeService {

    suspend fun getRecipeSummaries(): List<RecipeSummary>

    suspend fun getRecipe(id: String): Recipe

    suspend fun updateRecipeNotes(id: String, notes: String): Recipe
}

interface RecipeServiceSource {
    fun recipeService(): RecipeService
}