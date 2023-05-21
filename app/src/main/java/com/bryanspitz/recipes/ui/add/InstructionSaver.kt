package com.bryanspitz.recipes.ui.add

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import com.bryanspitz.recipes.R
import javax.inject.Inject

class InstructionSaver @Inject constructor(
    private val activity: AddActivity,
    private val instructions: MutableState<List<String>>,
    private val editingInstruction: MutableState<EditingInstruction?>,
    private val errorState: SnackbarHostState
) {
    suspend fun saveInstruction(): Boolean {
        val editing = editingInstruction.value
        if (editing == null) {
            return true
        } else {
            if (editing.instruction.isBlank()) {
                errorState.showSnackbar(activity.getString(R.string.error_instruction_blank))
                return false
            } else {
                instructions.value = instructions.value.mapIndexed { index, instruction ->
                    if (index == editing.index) {
                        editing.instruction
                    } else {
                        instruction
                    }
                }
                editingInstruction.value = null
                return true
            }
        }
    }
}