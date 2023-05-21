package com.bryanspitz.recipes.repository.recipe.api

import com.bryanspitz.recipes.app.AppScope
import com.bryanspitz.recipes.model.recipe.Ingredient
import com.bryanspitz.recipes.model.recipe.Recipe
import com.bryanspitz.recipes.model.recipe.RecipeSummary
import com.bryanspitz.recipes.repository.recipe.cache.RecipeCache
import com.bryanspitz.recipes.repository.recipe.cache.RecipeCacheData
import dagger.Binds
import dagger.Module
import kotlinx.coroutines.delay
import java.util.UUID
import javax.inject.Inject

@AppScope
class MockRecipeService @Inject constructor() : RecipeService {
    private val internalCache = RecipeCache().apply {
        mutate {
            RecipeCacheData(
                summaries = listOf(
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
                )
            )
        }
    }

    override suspend fun getRecipeSummaries(): List<RecipeSummary> {
        delay(1000)
        return internalCache.data.value.summaries
    }

    override suspend fun getRecipe(id: String): Recipe {
        delay(1000)
        return internalCache.data.value.recipes[id]
            ?: internalCache.data.value.summaries.firstOrNull { it.id == id }?.let {
                Recipe(
                    summary = it,
                    ingredients = listOf(
                        Ingredient(1f, "unit", "appropriate ingredient", "chopped")
                    ),
                    instructions = listOf(
                        "Eat all the ingredients."
                    ),
                    notes = ""
                )
            } ?: Recipe(
                summary = RecipeSummary(
                    id = id,
                    title = "Tomato and Tofu Donburi ($id)",
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
    }

    override suspend fun updateRecipeNotes(id: String, notes: String): Recipe {
        delay(1000)
        internalCache.mutate {
            it.copy(recipes = it.recipes + (id to it.recipes[id]!!.copy(notes = notes)))
        }
        return internalCache.data.value.recipes[id]!!
    }

    override suspend fun addNewRecipe(recipe: Recipe): Recipe {
        delay(1000)
        val newRecipe = recipe.copy(
            summary = recipe.summary.copy(id = UUID.randomUUID().toString())
        )
        internalCache.mutate {
            it.copy(
                summaries = it.summaries + newRecipe.summary,
                recipes = it.recipes + (newRecipe.summary.id to newRecipe)
            )

        }
        return newRecipe
    }
}

@Module
interface MockRecipeServiceModule {

    @Binds
    fun service(mockService: MockRecipeService): RecipeService
}