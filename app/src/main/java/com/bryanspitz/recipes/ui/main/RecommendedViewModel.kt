package com.bryanspitz.recipes.ui.main

import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.ViewModel
import com.bryanspitz.recipes.repository.recipe.GetRecipeSummaries
import com.bryanspitz.recipes.util.CoroutineDispatcherModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlin.coroutines.CoroutineContext

/**
 * Unused class, to demo Google's recommended MVVM architecture
 */
class RecommendedViewModel(
    getRecipeSummaries: GetRecipeSummaries,
    @CoroutineDispatcherModule.SafeUiDispatcher dispatcher: CoroutineContext
) : ViewModel() {

    val recipeSummaries = getRecipeSummaries.getRecipeSummaries()
        .map { it.sortedBy { it.title.toLowerCase(Locale.current) } }
        .stateIn(CoroutineScope(dispatcher), SharingStarted.WhileSubscribed(), null)

    val startAddRecipeActivity = MutableStateFlow(false)
    val startRecipeDetailActivity = MutableStateFlow(false)

    fun onAddRecipe() {
        startAddRecipeActivity.value = true

//        Handle in view code
//        LaunchedEffect(viewModel.startAddRecipeActivity.value) {
//            if (viewModel.startAddRecipeActivity.value) {
//                activity.startActivity(Intent(activity, AddActivity::class.java))
//                viewModel.startAddRecipeActivity.value = false
//            }
//        }
    }

    fun onClickRecipe(id: String) {
        startRecipeDetailActivity.value = true

//        Handle in view code
//        LaunchedEffect(viewModel.startRecipeDetailActivity.value) {
//            if (viewModel.startRecipeDetailActivity.value) {
//                activity.startActivity(
//                    Intent(activity, DetailActivity::class.java)
//                        .putExtra(PARAM_RECIPE_ID, id)
//                )
//                viewModel.startRecipeDetailActivity.value = false
//            }
//        }
    }
}