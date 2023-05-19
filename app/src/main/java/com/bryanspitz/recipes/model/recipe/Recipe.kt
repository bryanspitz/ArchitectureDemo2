package com.bryanspitz.recipes.model.recipe

data class Recipe(
    val summary: RecipeSummary,
    val ingredients: List<Ingredient>,
    val instructions: List<String>,
    val notes: String
)