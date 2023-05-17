package com.bryanspitz.recipes.repository.recipe

import com.bryanspitz.recipes.repository.recipe.cache.RecipeCache
import javax.inject.Inject

class GetRecipeSummaries @Inject constructor(
    private val cache: RecipeCache
) {

    fun get() = cache.data
}