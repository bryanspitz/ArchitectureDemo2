@file:OptIn(ExperimentalMaterial3Api::class)

package com.bryanspitz.recipes.ui.add

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bryanspitz.recipes.R
import com.bryanspitz.recipes.ui.theme.RecipesTheme

@Composable
fun AddIngredient(
    modifier: Modifier = Modifier,
    ingredient: EditingIngredient,
    onChanged: (EditingIngredient) -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        with(ingredient) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                OutlinedTextField(
                    value = amount,
                    onValueChange = { onChanged(copy(amount = it)) },
                    modifier = Modifier.width(80.dp),
                    label = { Text(stringResource(R.string.amount)) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    )
                )
                OutlinedTextField(
                    value = unit,
                    onValueChange = { onChanged(copy(unit = it)) },
                    modifier = Modifier.width(80.dp),
                    label = { Text(stringResource(R.string.unit)) }
                )
                OutlinedTextField(
                    value = name,
                    onValueChange = { onChanged(copy(name = it)) },
                    modifier = Modifier.weight(1f),
                    label = { Text(stringResource(R.string.name)) }
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = preparation,
                    onValueChange = { onChanged(copy(preparation = it)) },
                    modifier = Modifier.weight(1f),
                    label = { Text(stringResource(R.string.preparation)) }
                )
                IconButton(onClick = onSave) {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = stringResource(id = R.string.save)
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(id = R.string.delete)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewAddIngredient() {
    RecipesTheme {
        AddIngredient(
            modifier = Modifier.fillMaxWidth(),
            ingredient = EditingIngredient(
                index = 0,
                amount = "1",
                unit = "cup",
                name = "flour",
                preparation = "divided"
            ),
            onChanged = {},
            onSave = {},
            onDelete = {}
        )
    }
}