package com.bryanspitz.recipes.ui.add

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import com.bryanspitz.recipes.architecture.FeatureSet
import com.bryanspitz.recipes.model.recipe.Ingredient
import com.bryanspitz.recipes.repository.recipe.api.RecipeServiceSource
import com.bryanspitz.recipes.repository.recipe.cache.RecipeCacheSource
import com.bryanspitz.recipes.util.CoroutineDispatcherModule
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Qualifier

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
            @BindsInstance activity: AddActivity,
            @BindsInstance @Title title: State<String>,
            @BindsInstance @Description description: State<String>,
            @BindsInstance ingredients: MutableState<List<Ingredient>>,
            @BindsInstance editingIngredient: MutableState<EditingIngredient?>,
            @BindsInstance instructions: MutableState<List<String>>,
            @BindsInstance editingInstruction: MutableState<EditingInstruction?>,
            @BindsInstance errorState: SnackbarHostState,
            recipeCacheSource: RecipeCacheSource,
            recipeServiceSource: RecipeServiceSource
        ): AddComponent
    }

    @Save
    fun onSave(): MutableSharedFlow<Any>

    @Ingredients
    fun onAddIngredient(): MutableSharedFlow<Any>

    @Ingredients
    fun onEditIngredient(): MutableSharedFlow<Int>

    @Instructions
    fun onAddInstruction(): MutableSharedFlow<Any>

    @Instructions
    fun onEditInstruction(): MutableSharedFlow<Int>

    fun features(): FeatureSet

    @Qualifier
    annotation class Title

    @Qualifier
    annotation class Description

    @Qualifier
    annotation class Save

    @Qualifier
    annotation class Ingredients

    @Qualifier
    annotation class Instructions
}

@Module
class AddModule {
    @get:Provides
    @get:AddComponent.Save
    val onSave = MutableSharedFlow<Any>()

    @get:Provides
    @get:AddComponent.Ingredients
    val onAddIngredient = MutableSharedFlow<Any>()

    @get:Provides
    @get:AddComponent.Ingredients
    val onEditIngredient = MutableSharedFlow<Int>()

    @get:Provides
    @get:AddComponent.Instructions
    val onAddInstruction = MutableSharedFlow<Any>()

    @get:Provides
    @get:AddComponent.Instructions
    val onEditInstruction = MutableSharedFlow<Int>()

    @Provides
    fun features(
        saveFeature: SaveFeature,
        addIngredientFeature: AddIngredientFeature,
        editIngredientFeature: EditIngredientFeature,
        addInstructionFeature: AddInstructionFeature,
        editInstructionFeature: EditInstructionFeature
    ) = FeatureSet(
        saveFeature,
        addIngredientFeature,
        editIngredientFeature,
        addInstructionFeature,
        editInstructionFeature
    )
}