package com.bryanspitz.recipes.ui.main

import android.content.Intent
import com.bryanspitz.recipes.architecture.Feature
import com.bryanspitz.recipes.ui.detail.DetailActivity
import com.bryanspitz.recipes.ui.detail.PARAM_RECIPE_ID
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class ClickRecipeFeature @Inject constructor(
    private val activity: MainActivity,
    private val onClick: MutableSharedFlow<String>
) : Feature {

    override suspend fun start() {
        onClick.collectLatest {
            activity.startActivity(
                Intent(activity, DetailActivity::class.java)
                    .putExtra(PARAM_RECIPE_ID, it)
            )
        }
    }
}