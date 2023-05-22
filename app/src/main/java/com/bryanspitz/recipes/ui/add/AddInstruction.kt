@file:OptIn(ExperimentalMaterial3Api::class)

package com.bryanspitz.recipes.ui.add

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bryanspitz.recipes.R
import com.bryanspitz.recipes.ui.theme.RecipesTheme

@Composable
fun AddInstruction(
    modifier: Modifier = Modifier,
    instruction: EditingInstruction,
    onChanged: (EditingInstruction) -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit
) {
    Column(modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "${instruction.index + 1}.",
                modifier = Modifier
                    .width(32.dp)
                    .alignByBaseline()
            )
            OutlinedTextField(
                value = instruction.instruction,
                onValueChange = { onChanged(instruction.copy(instruction = it)) },
                modifier = Modifier
                    .weight(1f)
                    .alignByBaseline(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { onSave() })
            )
        }
        Row(modifier = Modifier.padding(start = 32.dp)) {
            IconButton(onClick = onSave) {
                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = stringResource(R.string.save)
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.delete)
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewAddInstruction() {
    RecipesTheme {
        AddInstruction(
            modifier = Modifier.fillMaxWidth(),
            instruction = EditingInstruction(
                index = 0,
                instruction = "Combine the ingredients and hope for the best."
            ),
            onChanged = {},
            onSave = {},
            onDelete = {}
        )
    }
}