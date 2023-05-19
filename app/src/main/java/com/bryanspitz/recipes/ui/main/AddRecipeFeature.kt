package com.bryanspitz.recipes.ui.main

import android.content.Intent
import com.bryanspitz.recipes.architecture.Feature
import com.bryanspitz.recipes.ui.add.AddActivity
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class AddRecipeFeature @Inject constructor(
    private val activity: MainActivity,
    private val onAdd: MutableSharedFlow<Any>
) : Feature {

    override suspend fun start() {
        onAdd.collectLatest {
            activity.startActivity(Intent(activity, AddActivity::class.java))
        }
    }
}