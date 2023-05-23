package com.bryanspitz.recipes.ui.detail

import androidx.compose.material3.SnackbarHostState
import com.bryanspitz.recipes.R
import com.bryanspitz.recipes.architecture.Feature
import com.bryanspitz.recipes.repository.recipe.SaveRecipeNotes
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import java.io.IOException
import javax.inject.Inject

class SaveNotesFeature @Inject constructor(
    private val recipeId: String,
    private val saveRecipeNotes: SaveRecipeNotes,
    private val onSaveNotes: MutableSharedFlow<String>,
    private val activity: DetailActivity,
    private val errorState: SnackbarHostState
) : Feature {

    override suspend fun start() {
        onSaveNotes.collectLatest {
            try {
                saveRecipeNotes.save(recipeId, it)
            } catch (ioe: IOException) {
                errorState.showSnackbar(activity.getString(R.string.error_notes_failed))
            }
        }
    }
}