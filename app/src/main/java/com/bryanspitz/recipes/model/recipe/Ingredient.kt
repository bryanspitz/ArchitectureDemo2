package com.bryanspitz.recipes.model.recipe

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Ingredient(
    val amount: Float? = null,
    val unit: String? = null,
    val name: String,
    val preparation: String? = null
) : Parcelable