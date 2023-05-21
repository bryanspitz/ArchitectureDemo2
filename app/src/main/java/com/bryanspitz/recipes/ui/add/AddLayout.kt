@file:OptIn(ExperimentalMaterial3Api::class)

package com.bryanspitz.recipes.ui.add

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bryanspitz.recipes.R
import com.bryanspitz.recipes.model.recipe.Ingredient
import com.bryanspitz.recipes.ui.detail.IngredientRow
import com.bryanspitz.recipes.ui.detail.InstructionRow
import com.bryanspitz.recipes.ui.theme.RecipesTheme

@Composable
fun AddLayout(
    title: String,
    onTitleChanged: (String) -> Unit,
    description: String,
    onDescriptionChanged: (String) -> Unit,
    ingredients: List<Ingredient>,
    onAddIngredient: () -> Unit,
    onEditIngredient: (Int) -> Unit,
    editingIngredient: EditingIngredient?,
    onEditingIngredientChanged: (EditingIngredient) -> Unit,
    instructions: List<String>,
    onEditInstruction: (Int) -> Unit,
    editingInstruction: EditingInstruction?,
    onEditingInstructionChanged: (EditingInstruction) -> Unit,
    errorState: SnackbarHostState,
    onSave: () -> Unit,
    onBack: () -> Unit
) {
    val collapse = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(collapse.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                title = {
                    Text(text = stringResource(R.string.new_recipe))
                },
                actions = {
                    IconButton(onClick = onSave) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = stringResource(id = R.string.save)
                        )
                    }
                },
                scrollBehavior = collapse
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = errorState)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = onTitleChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                label = { Text(text = stringResource(R.string.title)) }
            )
            OutlinedTextField(
                value = description,
                onValueChange = onDescriptionChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                label = { Text(text = stringResource(R.string.description)) }
            )

            Text(
                text = stringResource(id = R.string.ingredients),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
            )
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                ingredients.forEachIndexed { i, item ->
                    if (editingIngredient != null && i == editingIngredient.index) {
                        Surface(
                            tonalElevation = 2.dp
                        ) {
                            AddIngredient(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                ingredient = editingIngredient,
                                onChanged = onEditingIngredientChanged
                            )
                        }
                    } else {
                        IngredientRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onEditIngredient(i) }
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            ingredient = item
                        )
                    }
                }
                TextButton(
                    onClick = onAddIngredient,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(text = stringResource(R.string.add_ingredient))
                }
            }

            Text(
                text = stringResource(id = R.string.directions),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
            )
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                instructions.forEachIndexed { i, item ->
                    if (editingInstruction != null && i == editingInstruction.index) {
                        Surface(
                            tonalElevation = 2.dp
                        ) {
                            AddInstruction(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                instruction = editingInstruction,
                                onChanged = onEditingInstructionChanged
                            )
                        }
                    } else {
                        InstructionRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp)
                                .clickable { onEditInstruction(i) },
                            number = i + 1,
                            instruction = item
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewAddLayout() {
    RecipesTheme {
        AddLayout(
            title = "Recipe",
            onTitleChanged = {},
            description = "A random combination of water and flour",
            onDescriptionChanged = {},
            ingredients = listOf(
                Ingredient(100f, "mL", "water", "heated to 60C"),
                Ingredient(null, null, "", null)
            ),
            onAddIngredient = {},
            onEditIngredient = {},
            editingIngredient = EditingIngredient(
                index = 1,
                amount = "1",
                unit = "cup",
                name = "flour",
                preparation = "divided"
            ),
            onEditingIngredientChanged = {},
            instructions = listOf(
                "",
                "Combine ingredients.",
                "Pray again."
            ),
            onEditInstruction = {},
            editingInstruction = EditingInstruction(0, "Pray."),
            onEditingInstructionChanged = {},
            errorState = SnackbarHostState(),
            onSave = { },
            onBack = {}
        )
    }
}