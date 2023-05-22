package com.bryanspitz.recipes.ui.add

import com.bryanspitz.recipes.architecture.Feature
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class SaveIngredientFeature @Inject constructor(
    private val saver: IngredientSaver,
    @AddComponent.SaveIngredient private val onSaveIngredient: MutableSharedFlow<Any>
) : Feature {

    override suspend fun start() {
        onSaveIngredient.collectLatest { saver.saveIngredient() }
    }
}