package com.bryanspitz.recipes.ui.add

import com.bryanspitz.recipes.repository.recipe.api.RecipeServiceSource
import com.bryanspitz.recipes.repository.recipe.cache.RecipeCacheSource
import com.bryanspitz.recipes.util.CoroutineDispatcherModule
import dagger.Component
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.flow.MutableSharedFlow

@Component(
    dependencies = [
        RecipeCacheSource::class,
        RecipeServiceSource::class
    ],
    modules = [
        CoroutineDispatcherModule::class,
        AddModule::class
    ]
)
interface AddComponent {

    @Component.Factory
    interface Factory {
        fun create(
            recipeCacheSource: RecipeCacheSource,
            recipeServiceSource: RecipeServiceSource
        ): AddComponent
    }

    fun onSave(): MutableSharedFlow<Any>
}

@Module
class AddModule {
    @get:Provides
    val onSave = MutableSharedFlow<Any>()
}