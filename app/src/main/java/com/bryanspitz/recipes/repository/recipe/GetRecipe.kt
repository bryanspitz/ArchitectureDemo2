package com.bryanspitz.recipes.repository.recipe

import com.bryanspitz.recipes.model.recipe.Ingredient
import com.bryanspitz.recipes.model.recipe.Recipe
import com.bryanspitz.recipes.model.recipe.RecipeSummary
import com.bryanspitz.recipes.repository.FetchStrategy
import com.bryanspitz.recipes.repository.get
import com.bryanspitz.recipes.repository.recipe.cache.RecipeCache
import com.bryanspitz.recipes.util.CoroutineDispatcherModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetRecipe @Inject constructor(
    private val cache: RecipeCache,
    @CoroutineDispatcherModule.IO private val ioDispatcher: CoroutineDispatcher
) {

    fun getRecipe(
        id: String,
        fetchStrategy: FetchStrategy = FetchStrategy.FETCH_THEN_CACHE
    ) = get(
        fetch = {
            withContext(ioDispatcher) {
                delay(200)
                cache.mutate {
                    it.copy(
                        recipes = it.recipes + (id to Recipe(
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
                                    preparation = "roughly chopped"
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
                                    name = "rice (preferably short-grain white)"
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
                        ))
                    )
                }
            }
        },
        cache = { cache.data.map { it.recipes[id] } },
        fetchStrategy = fetchStrategy
    )
}