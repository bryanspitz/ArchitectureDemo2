package com.bryanspitz.recipes.ui.add

import androidx.compose.runtime.MutableState
import com.bryanspitz.recipes.architecture.Feature
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class EditInstructionFeature @Inject constructor(
    private val ingredientSaver: IngredientSaver,
    private val instructionSaver: InstructionSaver,
    private val instructions: MutableState<List<String>>,
    @AddComponent.Instructions private val onEditInstruction: MutableSharedFlow<Int>,
    private val editingInstruction: MutableState<EditingInstruction?>
) : Feature {

    override suspend fun start() {
        onEditInstruction.collectLatest { index ->
            if (ingredientSaver.saveIngredient() && instructionSaver.saveInstruction()) {
                val instruction = instructions.value[index]
                editingInstruction.value = EditingInstruction(
                    index = index,
                    instruction = instruction
                )
            }
        }
    }
}