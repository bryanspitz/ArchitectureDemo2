package com.bryanspitz.recipes.ui.main

import com.bryanspitz.recipes.architecture.FeatureSet
import com.bryanspitz.recipes.model.recipe.RecipeSummary
import com.bryanspitz.recipes.repository.recipe.cache.RecipeCacheSource
import com.bryanspitz.recipes.util.CoroutineDispatcherModule
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow

@Component(
    dependencies = [
        RecipeCacheSource::class
    ],
    modules = [
        CoroutineDispatcherModule::class,
        RecipeSummariesViewModel::class,
        MainModule::class
    ]
)
interface MainComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance activity: MainActivity,
            recipeCacheSource: RecipeCacheSource
        ): MainComponent
    }

    fun recipeSummaries(): StateFlow<List<RecipeSummary>>

    fun onAdd(): MutableSharedFlow<Any>
    fun onClickRecipe(): MutableSharedFlow<String>

    fun features(): FeatureSet
}

@Module
class MainModule {
    @get:Provides
    val onAdd = MutableSharedFlow<Any>()

    @get:Provides
    val onClickRecipe = MutableSharedFlow<String>()

    @Provides
    fun features(
        addRecipeFeature: AddRecipeFeature,
        clickRecipeFeature: ClickRecipeFeature
    ) = FeatureSet(
        addRecipeFeature,
        clickRecipeFeature
    )
}