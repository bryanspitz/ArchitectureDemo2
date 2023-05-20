package com.bryanspitz.recipes.repository.recipe

import com.bryanspitz.recipes.repository.recipe.api.RecipeService
import com.bryanspitz.recipes.repository.recipe.cache.RecipeCache
import com.bryanspitz.recipes.util.CoroutineDispatcherModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SaveRecipeNotes @Inject constructor(
    private val cache: RecipeCache,
    private val api: RecipeService,
    @CoroutineDispatcherModule.IO private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun save(id: String, notes: String) {
        withContext(ioDispatcher) {
            val result = api.updateRecipeNotes(id, notes)
            cache.mutate {
                it.copy(recipes = it.recipes + (id to result))
            }
        }
    }
}