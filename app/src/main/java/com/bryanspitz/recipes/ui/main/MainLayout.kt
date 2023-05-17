package com.bryanspitz.recipes.ui.main

import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bryanspitz.recipes.R
import com.bryanspitz.recipes.model.recipe.RecipeSummary
import com.bryanspitz.recipes.ui.theme.RecipesTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainLayout(
    recipes: List<RecipeSummary>,
    onAdd: () -> Unit
) {
    val collapse = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(collapse.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                title = { Text(text = stringResource(R.string.my_recipes)) },
                scrollBehavior = collapse
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAdd) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.add_recipe)
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = spacedBy(16.dp)
        ) {
            itemsIndexed(recipes, { _, item -> item.id }) { index, item ->
                RecipeCard(
                    data = item,
                    imageAtEnd = index % 2 == 0
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewMainLayout() {
    RecipesTheme {
        MainLayout(
            recipes = listOf(
                RecipeSummary(
                    id = "id0",
                    title = "Pflaumenkuchen",
                    description = "A traditional German plum cake.",
                    imgUrl = ""
                ),
                RecipeSummary(
                    id = "id1",
                    title = "Potatoes Romanoff",
                    description = "The potatoes of Russian royalty.",
                    imgUrl = ""
                ),
                RecipeSummary(
                    id = "id2",
                    title = "Blueberry Pie",
                    description = "Flaky crust with a decadent blueberry filling, or whatever.",
                    imgUrl = ""
                )
            ),
            onAdd = {}
        )
    }
}