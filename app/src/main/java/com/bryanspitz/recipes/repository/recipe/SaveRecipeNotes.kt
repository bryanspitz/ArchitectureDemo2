package com.bryanspitz.recipes.repository.recipe

import com.bryanspitz.recipes.model.recipe.Recipe
import com.bryanspitz.recipes.repository.recipe.api.RecipeService
import com.bryanspitz.recipes.repository.recipe.cache.RecipeCache
import com.bryanspitz.recipes.util.CoroutineDispatcherModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class SaveRecipeNotes @Inject constructor(
    private val cache: RecipeCache,
    private val api: RecipeService,
    @CoroutineDispatcherModule.IO private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun save(id: String, notes: String): Recipe {
        return withContext(ioDispatcher) {
            // update optimistically
            val originalData = cache.mutate {
                it.copy(recipes = it.recipes + (id to it.recipes[id]!!.copy(notes = notes)))
            }
            try {
                val result = api.updateRecipeNotes(id, notes)
                cache.mutate {
                    it.copy(recipes = it.recipes + (id to result))
                }
                result
            } catch (ioe: IOException) {
                // roll back
                cache.mutate { originalData }
                throw ioe
            }
        }
    }
}