package com.bryanspitz.recipes.model.recipe

data class Ingredient(
    val amount: Float? = null,
    val unit: String? = null,
    val name: String,
    val preparation: String? = null
)