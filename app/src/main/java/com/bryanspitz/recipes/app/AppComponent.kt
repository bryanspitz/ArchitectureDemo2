package com.bryanspitz.recipes.app

import com.bryanspitz.recipes.repository.recipe.api.MockRecipeServiceModule
import com.bryanspitz.recipes.repository.recipe.api.RecipeServiceSource
import com.bryanspitz.recipes.repository.recipe.cache.RecipeCacheSource
import dagger.Component
import javax.inject.Scope

@Scope
annotation class AppScope

@AppScope
@Component(modules = [MockRecipeServiceModule::class])
interface AppComponent : RecipeCacheSource, RecipeServiceSource {

    @Component.Factory
    interface Factory {
        fun create(): AppComponent
    }
}

interface AppComponentSource {
    val appComponent: AppComponent
}