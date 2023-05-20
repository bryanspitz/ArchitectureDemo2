package com.bryanspitz.recipes.ui.detail

import com.bryanspitz.recipes.architecture.Feature
import com.bryanspitz.recipes.repository.recipe.SaveRecipeNotes
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class SaveNotesFeature @Inject constructor(
    private val recipeId: String,
    private val saveRecipeNotes: SaveRecipeNotes,
    private val onSaveNotes: MutableSharedFlow<String>
) : Feature {

    override suspend fun start() {
        onSaveNotes.collectLatest {
            saveRecipeNotes.save(recipeId, it)
        }
    }
}