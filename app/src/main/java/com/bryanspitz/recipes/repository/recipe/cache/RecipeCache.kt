package com.bryanspitz.recipes.repository.recipe.cache

import com.bryanspitz.recipes.app.AppScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@AppScope
class RecipeCache @Inject constructor() {

    private val _data = MutableStateFlow(RecipeCacheData())
    val data: StateFlow<RecipeCacheData> = _data

    fun mutate(mutator: (RecipeCacheData) -> RecipeCacheData) {
        _data.value = mutator(_data.value)
    }
}

interface RecipeCacheSource {
    fun recipeCache(): RecipeCache
}