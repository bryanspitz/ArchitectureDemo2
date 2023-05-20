package com.bryanspitz.recipes.repository.recipe

import com.bryanspitz.recipes.repository.FetchStrategy
import com.bryanspitz.recipes.repository.get
import com.bryanspitz.recipes.repository.recipe.api.RecipeService
import com.bryanspitz.recipes.repository.recipe.cache.RecipeCache
import com.bryanspitz.recipes.util.CoroutineDispatcherModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetRecipe @Inject constructor(
    private val cache: RecipeCache,
    private val api: RecipeService,
    @CoroutineDispatcherModule.IO private val ioDispatcher: CoroutineDispatcher
) {

    fun getRecipe(
        id: String,
        fetchStrategy: FetchStrategy = FetchStrategy.FETCH_THEN_CACHE
    ) = get(
        fetch = {
            withContext(ioDispatcher) {
                val result = api.getRecipe(id)
                cache.mutate {
                    it.copy(recipes = it.recipes + (id to result))
                }
            }
        },
        cache = { cache.data.map { it.recipes[id] } },
        fetchStrategy = fetchStrategy
    )
}