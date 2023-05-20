package com.bryanspitz.recipes.ui.detail

import com.bryanspitz.recipes.model.recipe.Recipe
import com.bryanspitz.recipes.repository.recipe.api.RecipeServiceSource
import com.bryanspitz.recipes.repository.recipe.cache.RecipeCacheSource
import com.bryanspitz.recipes.util.CoroutineDispatcherModule
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.flow.StateFlow

@Component(
    dependencies = [
        RecipeCacheSource::class,
        RecipeServiceSource::class
    ],
    modules = [
        RecipeModule::class,
        CoroutineDispatcherModule::class
    ]
)
interface DetailComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance recipeId: String,
            recipeCacheSource: RecipeCacheSource,
            recipeServiceSource: RecipeServiceSource
        ): DetailComponent
    }

    fun recipe(): StateFlow<Recipe>
}