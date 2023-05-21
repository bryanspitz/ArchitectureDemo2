package com.bryanspitz.recipes.repository.recipe

import com.bryanspitz.recipes.model.recipe.Recipe
import com.bryanspitz.recipes.repository.recipe.api.RecipeService
import com.bryanspitz.recipes.repository.recipe.cache.RecipeCache
import com.bryanspitz.recipes.util.CoroutineDispatcherModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PostNewRecipe @Inject constructor(
    private val cache: RecipeCache,
    private val api: RecipeService,
    @CoroutineDispatcherModule.IO private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun post(recipe: Recipe): Recipe {
        return withContext(ioDispatcher) {
            coroutineScope {
                val result = api.addNewRecipe(recipe)
                val summaries = api.getRecipeSummaries()

                cache.mutate {
                    it.copy(
                        summaries = summaries,
                        recipes = it.recipes + (result.summary.id to result)
                    )
                }
                result
            }
        }
    }
}