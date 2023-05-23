package com.bryanspitz.recipes.ui.detail

import androidx.compose.material3.SnackbarHostState
import com.bryanspitz.recipes.architecture.FeatureSet
import com.bryanspitz.recipes.model.recipe.Recipe
import com.bryanspitz.recipes.repository.recipe.api.RecipeServiceSource
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
        RecipeCacheSource::class,
        RecipeServiceSource::class
    ],
    modules = [
        RecipeViewModel::class,
        DetailModule::class,
        CoroutineDispatcherModule::class
    ]
)
interface DetailComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance activity: DetailActivity,
            @BindsInstance recipeId: String,
            @BindsInstance errorState: SnackbarHostState,
            recipeCacheSource: RecipeCacheSource,
            recipeServiceSource: RecipeServiceSource
        ): DetailComponent
    }

    fun recipe(): StateFlow<Recipe>

    fun onSaveNotes(): MutableSharedFlow<String>

    fun features(): FeatureSet
}

@Module
class DetailModule {
    @get:Provides
    val onSaveNotes = MutableSharedFlow<String>()

    @Provides
    fun features(
        saveNotesFeature: SaveNotesFeature
    ) = FeatureSet(
        saveNotesFeature
    )
}