@file:OptIn(ExperimentalMaterial3Api::class)

package com.bryanspitz.recipes.ui.detail

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bryanspitz.recipes.R
import com.bryanspitz.recipes.ui.theme.RecipesTheme

@Composable
fun RecipeNotes(
    notes: String,
    onSave: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var editing by rememberSaveable(Unit) { mutableStateOf(false) }
    var currentNotes by rememberSaveable(notes) { mutableStateOf(notes) }

    RecipeNotesLayout(
        originalNotes = notes,
        currentNotes = currentNotes,
        isEditing = editing,
        onEdit = { editing = true },
        onSave = {
            editing = false
            onSave(currentNotes)
        },
        onCancel = {
            editing = false
            currentNotes = notes
        },
        onChangeText = { currentNotes = it },
        modifier = modifier
    )
}

@Composable
private fun RecipeNotesLayout(
    originalNotes: String,
    currentNotes: String,
    isEditing: Boolean,
    onEdit: () -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    onChangeText: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = spacedBy(4.dp)
    ) {
        if (!isEditing) {
            if (originalNotes.isNotEmpty()) {
                Text(text = originalNotes)
            }
            Button(onClick = onEdit) {
                Text(
                    text = stringResource(
                        if (originalNotes.isNotEmpty())
                            R.string.edit
                        else
                            R.string.add
                    )
                )
            }
        } else {
            OutlinedTextField(
                value = currentNotes,
                onValueChange = onChangeText,
                modifier = Modifier.fillMaxWidth()
            )

            Row(horizontalArrangement = spacedBy(16.dp)) {
                Button(onClick = onSave) {
                    Text(text = stringResource(R.string.save))
                }
                TextButton(onClick = onCancel) {
                    Text(text = stringResource(R.string.cancel))
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewRecipeNotes() {
    var text by remember { mutableStateOf("Original text") }

    RecipesTheme {
        RecipeNotes(
            notes = text,
            onSave = { text = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

@Preview
@Composable
private fun PreviewRecipeNotesLayout() {
    RecipesTheme {
        RecipeNotesLayout(
            originalNotes = "Original text",
            currentNotes = "Updated text",
            isEditing = false,
            onEdit = {},
            onSave = {},
            onCancel = {},
            onChangeText = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

@Preview
@Composable
private fun PreviewRecipeNotesLayoutEmpty() {
    RecipesTheme {
        RecipeNotesLayout(
            originalNotes = "",
            currentNotes = "Updated text",
            isEditing = false,
            onEdit = {},
            onSave = {},
            onCancel = {},
            onChangeText = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

@Preview
@Composable
private fun PreviewRecipeNotesLayoutEditing() {
    RecipesTheme {
        RecipeNotesLayout(
            originalNotes = "Original text",
            currentNotes = "Updated text",
            isEditing = true,
            onEdit = {},
            onSave = {},
            onCancel = {},
            onChangeText = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}