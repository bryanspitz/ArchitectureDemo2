package com.bryanspitz.recipes.ui.add

import com.bryanspitz.recipes.architecture.Feature
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class SaveInstructionFeature @Inject constructor(
    private val saver: InstructionSaver,
    @AddComponent.SaveInstruction private val onSaveInstruction: MutableSharedFlow<Any>
) : Feature {

    override suspend fun start() {
        onSaveInstruction.collectLatest { saver.saveInstruction() }
    }
}