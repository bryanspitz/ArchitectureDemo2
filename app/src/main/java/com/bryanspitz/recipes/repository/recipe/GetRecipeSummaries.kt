package com.bryanspitz.recipes.repository.recipe

import com.bryanspitz.recipes.model.recipe.RecipeSummary
import com.bryanspitz.recipes.repository.FetchStrategy
import com.bryanspitz.recipes.repository.FetchStrategy.FETCH_THEN_CACHE
import com.bryanspitz.recipes.repository.get
import com.bryanspitz.recipes.repository.recipe.api.RecipeService
import com.bryanspitz.recipes.repository.recipe.cache.RecipeCache
import com.bryanspitz.recipes.util.CoroutineDispatcherModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetRecipeSummaries @Inject constructor(
    private val cache: RecipeCache,
    private val api: RecipeService,
    @CoroutineDispatcherModule.IO private val dispatcher: CoroutineDispatcher
) {

    fun getRecipeSummaries(fetchStrategy: FetchStrategy = FETCH_THEN_CACHE) = get(
        ::fetch,
        ::getFromCache,
        fetchStrategy
    )


    private suspend fun fetch() {
        withContext(dispatcher) {
            val result = api.getRecipeSummaries()
            cache.mutate { it.copy(summaries = result) }
        }
    }

    private fun getFromCache(): Flow<List<RecipeSummary>> {
        return cache.data.map { it.summaries }
    }
}