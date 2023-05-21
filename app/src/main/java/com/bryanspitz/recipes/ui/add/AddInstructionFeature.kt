package com.bryanspitz.recipes.ui.add

import androidx.compose.runtime.MutableState
import com.bryanspitz.recipes.architecture.Feature
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class AddInstructionFeature @Inject constructor(
    private val ingredientSaver: IngredientSaver,
    private val instructionSaver: InstructionSaver,
    private val instructions: MutableState<List<String>>,
    @AddComponent.Instructions private val onAddInstruction: MutableSharedFlow<Any>,
    @AddComponent.Instructions private val onEditInstruction: MutableSharedFlow<Int>
) : Feature {

    override suspend fun start() {
        onAddInstruction.collectLatest {
            if (ingredientSaver.saveIngredient() && instructionSaver.saveInstruction()) {
                instructions.value = instructions.value + ""
                onEditInstruction.emit(instructions.value.size - 1)
            }
        }
    }
}