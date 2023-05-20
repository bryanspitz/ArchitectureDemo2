@file:OptIn(ExperimentalMaterial3Api::class)

package com.bryanspitz.recipes.ui.detail

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.bryanspitz.recipes.R
import com.bryanspitz.recipes.model.recipe.Ingredient
import com.bryanspitz.recipes.model.recipe.Recipe
import com.bryanspitz.recipes.model.recipe.RecipeSummary
import com.bryanspitz.recipes.ui.theme.RecipesTheme

@Composable
fun DetailLayout(
    recipe: Recipe?,
    onSaveNotes: (String) -> Unit,
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
                    Text(text = recipe?.summary?.title ?: "")
                },
                scrollBehavior = collapse
            )
        }
    ) { padding ->
        if (recipe != null) {
            with(recipe) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = summary.description,
                        modifier = Modifier.padding(16.dp)
                    )

                    Text(
                        text = stringResource(id = R.string.ingredients),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Column(
                        verticalArrangement = spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        ingredients.forEach {
                            IngredientRow(it)
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
                        verticalArrangement = spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        instructions.forEachIndexed { i, inst ->
                            InstructionRow(i, inst)
                        }
                    }

                    Text(
                        text = stringResource(id = R.string.notes),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(top = 16.dp)
                    )
                    RecipeNotes(
                        notes = notes,
                        onSave = onSaveNotes,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
private fun InstructionRow(i: Int, inst: String) {
    Row(
        horizontalArrangement = spacedBy(4.dp)
    ) {
        Text(
            text = "${i + 1}.",
            modifier = Modifier.width(32.dp)
        )
        Text(text = inst)
    }
}

@Composable
private fun IngredientRow(ingredient: Ingredient) {
    Row(
        horizontalArrangement = spacedBy(4.dp)
    ) {
        Text(
            text = ingredient.amount?.let { "$it" } ?: "",
            textAlign = TextAlign.End,
            modifier = Modifier.width(32.dp)
        )
        Text(
            text = ingredient.unit ?: "",
            modifier = Modifier.width(80.dp)
        )
        Text(
            text = buildAnnotatedString {
                append(ingredient.name)
                ingredient.preparation?.run {
                    append(", ")
                    withStyle(SpanStyle(fontSize = 0.7.em)) {
                        append(ingredient.preparation)
                    }
                }
            },
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview
@Composable
private fun PreviewDetailLayoutLoading() {
    RecipesTheme {
        DetailLayout(
            recipe = null,
            onSaveNotes = {},
            onBack = {}
        )
    }
}

@Preview
@Composable
private fun PreviewDetailLayoutPopulated() {
    RecipesTheme {
        DetailLayout(
            recipe = testRecipe,
            onSaveNotes = {},
            onBack = {}
        )
    }
}

private val testRecipe = Recipe(
    summary = RecipeSummary(
        id = "id",
        title = "Tomato and Tofu Donburi",
        description = "Steamed rice topped with a savoury tomato and tofu stew.",
        imgUrl = ""
    ),
    ingredients = listOf(
        Ingredient(
            amount = 8f,
            name = "vine-ripened tomatoes",
            preparation = "cut into chunks"
        ),
        Ingredient(
            amount = 1f,
            unit = "bunch",
            name = "spring onions",
            preparation = "(with 2 stalks reserved), roughly chopped"
        ),
        Ingredient(
            amount = 1f,
            unit = "package",
            name = "soft (silken) tofu",
            preparation = " cut into cubes"
        ),
        Ingredient(name = "soy sauce"),
        Ingredient(amount = 1f, unit = "pinch", name = "salt"),
        Ingredient(amount = 2f, unit = "tsp", name = "sugar"),
        Ingredient(name = "toasted sesame oil"),
        Ingredient(name = "olive oil"),
        Ingredient(
            amount = 2f,
            unit = "cups",
            name = "rice (preferably short-grain white rice)"
        )
    ),
    instructions = listOf(
        "Start steaming the rice.",
        "Sauté onion briefly in olive oil with a dash of sesame oil. Do not brown.",
        "Add tomatoes, stir, and cover. Allow to simmer and stew for the entire cooking time of the rice.",
        "Add a dash of soy sauce and season with salt and sugar.",
        "Add tofu and gently stir.",
        "Let simmer a few more minutes.",
        "Add more toasted sesame oil.",
        "Serve in a bowl with stew over rice. Garnish with finely chopped spring onions."
    ),
    notes = "Next time, try not to suck so much."
)
