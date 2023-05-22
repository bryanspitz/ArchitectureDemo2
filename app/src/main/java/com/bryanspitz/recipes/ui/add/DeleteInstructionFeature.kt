package com.bryanspitz.recipes.ui.add

import androidx.compose.runtime.MutableState
import com.bryanspitz.recipes.architecture.Feature
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class DeleteInstructionFeature @Inject constructor(
    private val instructions: MutableState<List<String>>,
    private val editingInstruction: MutableState<EditingInstruction?>,
    @AddComponent.DeleteInstruction private val onDeleteInstruction: MutableSharedFlow<Any>
) : Feature {

    override suspend fun start() {
        onDeleteInstruction.collectLatest {
            instructions.value = instructions.value.filterIndexed { index, _ ->
                index != editingInstruction.value?.index
            }
            editingInstruction.value = null
        }
    }
}