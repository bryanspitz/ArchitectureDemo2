package com.bryanspitz.recipes.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bryanspitz.recipes.model.recipe.RecipeSummary
import com.bryanspitz.recipes.ui.theme.RecipesTheme

@Composable
fun RecipeCard(
    modifier: Modifier = Modifier,
    data: RecipeSummary,
    imageAtEnd: Boolean = true
) {
    with(data) {
        ElevatedCard(modifier = modifier) {
            Row {
                if (!imageAtEnd) {
                    Box(
                        modifier = Modifier
                            .weight(40f)
                            .aspectRatio(1f)
                            .background(Color.White)
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(12.dp)
                        .weight(60f),
                    verticalArrangement = spacedBy(8.dp)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(text = description)
                }
                if (imageAtEnd) {
                    Box(
                        modifier = Modifier
                            .weight(40f)
                            .aspectRatio(1f)
                            .background(Color.White)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewRecipeCardRight() {
    RecipesTheme {
        RecipeCard(
            modifier = Modifier.fillMaxWidth(),
            data = RecipeSummary(
                id = "id",
                title = "Pflaumenkuchen",
                description = "A traditional German plum cake.",
                imgUrl = ""
            )
        )
    }
}

@Preview
@Composable
private fun PreviewRecipeCardLeft() {
    RecipesTheme {
        RecipeCard(
            modifier = Modifier.fillMaxWidth(),
            data = RecipeSummary(
                id = "id",
                title = "Pflaumenkuchen",
                description = "A traditional German plum cake.",
                imgUrl = ""
            ),
            imageAtEnd = false
        )
    }
}