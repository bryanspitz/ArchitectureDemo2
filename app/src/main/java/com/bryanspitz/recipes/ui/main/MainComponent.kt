package com.bryanspitz.recipes.ui.main

import com.bryanspitz.recipes.architecture.SafeUiDispatcherModule
import com.bryanspitz.recipes.model.recipe.RecipeSummary
import com.bryanspitz.recipes.repository.recipe.cache.RecipeCacheSource
import dagger.Component
import kotlinx.coroutines.flow.StateFlow

@Component(
    dependencies = [
        RecipeCacheSource::class
    ],
    modules = [
        SafeUiDispatcherModule::class,
        RecipeSummariesViewModel::class
    ]
)
interface MainComponent {

    @Component.Factory
    interface Factory {
        fun create(
            recipeCacheSource: RecipeCacheSource
        ): MainComponent
    }

    fun recipeSummaries(): StateFlow<List<RecipeSummary>>
}