package com.bryanspitz.recipes.repository.recipe.cache

import com.bryanspitz.recipes.app.AppScope
import com.bryanspitz.recipes.model.recipe.RecipeSummary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@AppScope
class RecipeCache @Inject constructor() {

    private val _data = MutableStateFlow(
        RecipeCacheData(
            summaries = listOf(
                RecipeSummary(
                    id = "id0",
                    title = "Pflaumenkuchen",
                    description = "A traditional German plum cake.",
                    imgUrl = ""
                ),
                RecipeSummary(
                    id = "id1",
                    title = "Potatoes Romanoff",
                    description = "The potatoes of Russian royalty.",
                    imgUrl = ""
                ),
                RecipeSummary(
                    id = "id2",
                    title = "Blueberry Pie",
                    description = "Flaky crust with a decadent blueberry filling, or whatever.",
                    imgUrl = ""
                )
            )
        )
    )
    val data: StateFlow<RecipeCacheData> = _data

    fun mutate(mutator: (RecipeCacheData) -> RecipeCacheData) {
        _data.value = mutator(_data.value)
    }
}

interface RecipeCacheSource {
    fun recipeCache(): RecipeCache
}