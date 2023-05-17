package com.bryanspitz.recipes.ui.main

import com.bryanspitz.recipes.architecture.SafeUiDispatcherModule
import com.bryanspitz.recipes.model.recipe.RecipeSummary
import com.bryanspitz.recipes.repository.recipe.GetRecipeSummaries
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlin.coroutines.CoroutineContext

@Module
class RecipeSummariesViewModel {

    @Provides
    fun recipeSummaries(
        getRecipeSummaries: GetRecipeSummaries,
        @SafeUiDispatcherModule.SafeUiDispatcher dispatcher: CoroutineContext
    ): StateFlow<List<RecipeSummary>> {
        return getRecipeSummaries.get()
            .map { it.sortedBy { it.title } }
            .stateIn(CoroutineScope(dispatcher), SharingStarted.WhileSubscribed(), emptyList())
    }
}