package com.bryanspitz.recipes.repository.recipe

import com.bryanspitz.recipes.model.recipe.RecipeSummary
import com.bryanspitz.recipes.repository.FetchStrategy
import com.bryanspitz.recipes.repository.FetchStrategy.FETCH_THEN_CACHE
import com.bryanspitz.recipes.repository.get
import com.bryanspitz.recipes.repository.recipe.cache.RecipeCache
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetRecipeSummaries @Inject constructor(
    private val cache: RecipeCache
) {

    fun getRecipeSummaries(fetchStrategy: FetchStrategy = FETCH_THEN_CACHE) = get(
        ::fetch,
        ::getFromCache,
        fetchStrategy
    )


    private suspend fun fetch() {
        // withContext(IO) {api.fetchRecipeSummaries()}
        // cache.mutate{}
    }

    private fun getFromCache(): Flow<List<RecipeSummary>> {
        return cache.data.map { it.summaries }
    }
}