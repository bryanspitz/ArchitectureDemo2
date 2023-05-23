package com.bryanspitz.recipes.repository.recipe.cache

import com.bryanspitz.recipes.app.AppScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@AppScope
class RecipeCache @Inject constructor() {

    private val _data = MutableStateFlow(RecipeCacheData())
    val data: StateFlow<RecipeCacheData> = _data

    /**
     * returns original model, to roll back cache updates
     */
    fun mutate(mutator: (RecipeCacheData) -> RecipeCacheData): RecipeCacheData {
        val oldData = _data.value
        _data.value = mutator(_data.value)
        return oldData
    }
}

interface RecipeCacheSource {
    fun recipeCache(): RecipeCache
}