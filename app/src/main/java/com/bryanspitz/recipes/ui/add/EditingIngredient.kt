package com.bryanspitz.recipes.ui.add

data class EditingIngredient(
    val index: Int,
    val amount: String,
    val unit: String,
    val name: String,
    val preparation: String
)
