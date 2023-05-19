package com.bryanspitz.recipes.repository.recipe

import com.bryanspitz.recipes.repository.FetchStrategy
import com.bryanspitz.recipes.repository.get
import com.bryanspitz.recipes.repository.recipe.cache.RecipeCache
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetRecipe @Inject constructor(
    private val cache: RecipeCache
) {

    fun getRecipe(
        id: String,
        fetchStrategy: FetchStrategy = FetchStrategy.FETCH_THEN_CACHE
    ) = get(
        fetch = {},
        cache = { cache.data.map { it.recipes[id] } },
        fetchStrategy = fetchStrategy
    )
}