package com.bryanspitz.recipes.ui.detail

import com.bryanspitz.recipes.model.recipe.Recipe
import com.bryanspitz.recipes.repository.FetchStrategy
import com.bryanspitz.recipes.repository.recipe.GetRecipe
import com.bryanspitz.recipes.util.CoroutineDispatcherModule
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlin.coroutines.CoroutineContext

@Module
class RecipeModule {

    @Provides
    fun recipe(
        recipeId: String,
        getRecipe: GetRecipe,
        @CoroutineDispatcherModule.SafeUiDispatcher dispatcher: CoroutineContext
    ): StateFlow<Recipe?> = getRecipe.getRecipe(recipeId, FetchStrategy.CACHE_THEN_FETCH)
        .stateIn(CoroutineScope(dispatcher), SharingStarted.WhileSubscribed(), null)
}