package com.bryanspitz.recipes.ui.add

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EditingIngredient(
    val index: Int,
    val amount: String,
    val unit: String,
    val name: String,
    val preparation: String
) : Parcelable
