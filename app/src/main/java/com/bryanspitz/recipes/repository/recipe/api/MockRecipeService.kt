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
import java.io.IOException
import java.util.UUID
import javax.inject.Inject

@AppScope
class MockRecipeService @Inject constructor() : RecipeService {
    private val internalCache = RecipeCache().apply {
        mutate {
            RecipeCacheData(
                summaries = listOf(
                    RecipeSummary(
                        id = "donburi",
                        title = "Tomato and Tofu Donburi",
                        description = "Steamed rice topped with stewed tomatoes and tofu.",
                        imgUrl = ""
                    ),
                    RecipeSummary(
                        id = "coleslaw",
                        title = "Coleslaw",
                        description = "Cabbage salad with a mayonnaise-based dressing.",
                        imgUrl = ""
                    ),
                    RecipeSummary(
                        id = "lasagna",
                        title = "Vegetarian Lasagna",
                        description = "No, seriously, it's just vegetarian lasagna.",
                        imgUrl = ""
                    )
                ),
                recipes = mapOf(
                    "donburi" to Recipe(
                        summary = RecipeSummary(
                            id = "donburi",
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
                    ),
                    "coleslaw" to Recipe(
                        summary = RecipeSummary(
                            id = "coleslaw",
                            title = "Coleslaw",
                            description = "Shredded cabbage salad with a rich mayonnaise-based dressing.",
                            imgUrl = ""
                        ),
                        ingredients = listOf(
                            Ingredient(
                                amount = 1f,
                                name = "cabbage",
                                preparation = "outer leaves removed"
                            ),
                            Ingredient(
                                amount = 3f,
                                name = "medium carrots",
                                preparation = "peeled and shredded"
                            ),
                            Ingredient(amount = 1f, unit = "cup", name = "mayonnaise"),
                            Ingredient(amount = 2f, unit = "tbsp", name = "apple cider vinegar"),
                            Ingredient(amount = 2f, unit = "tbsp", name = "Dijon mustard"),
                            Ingredient(amount = 1f, unit = "tsp", name = "celery seeds"),
                            Ingredient(amount = 0.25f, unit = "tsp", name = "salt"),
                            Ingredient(amount = 0.25f, unit = "tsp", name = "pepper")
                        ),
                        instructions = listOf(
                            "Quarter the cabbage through the core, cut out the core, cut each quarter crosswise, and shred.",
                            "Add shredded carrot and toss.",
                            "In a separate bowl, mix mayo, vinegar, mustard, celery seeds, salt, and pepper.",
                            "Combine vegetables and dressing and toss to coat. Do it immediately before serving to retain crunch."
                        ),
                        notes = ""
                    ),
                    "lasagna" to Recipe(
                        summary = RecipeSummary(
                            id = "lasagna",
                            title = "Vegetarian Lasagna",
                            description = "No, seriously, it's just vegetarian lasagna.",
                            imgUrl = ""
                        ),
                        ingredients = listOf(
                            Ingredient(amount = 9f, name = "lasagna noodles"),
                            Ingredient(amount = 2f, unit = "tbsp", name = "olive oil"),
                            Ingredient(amount = 1f, name = "medium onion", preparation = "chopped"),
                            Ingredient(
                                amount = 2f,
                                unit = "cloves",
                                name = "garlic",
                                preparation = "crushed"
                            ),
                            Ingredient(amount = 2f, name = "zucchini", preparation = "diced"),
                            Ingredient(amount = 1f, name = "yellow squash", preparation = "diced"),
                            Ingredient(
                                amount = 1f,
                                unit = "package",
                                name = "cremini mushrooms",
                                preparation = "diced"
                            ),
                            Ingredient(
                                1f,
                                unit = "jar",
                                name = "roasted red pepper",
                                preparation = "diced"
                            ),
                            Ingredient(1f, unit = "can", name = "crushed tomatoes"),
                            Ingredient(1f, unit = "container", name = "ricotta"),
                            Ingredient(2f, name = "eggs"),
                            Ingredient(60f, unit = "g", name = "parmesan"),
                            Ingredient(230f, unit = "g", name = "mozzarella"),
                            Ingredient(name = "salt and pepper")
                        ),
                        instructions = listOf(
                            "Cook noodles according to directions, then lay flat on aluminum foil.",
                            "Preheat oven to 350F.",
                            "Heat olive oil in large pot.",
                            "Cook onion and mushrooms in oil about 5 minutes.",
                            "Add garlic, zucchini, squash, a pinch of salt, and cook 5-8 minutes.",
                            "Add red peppers and tomatoes and cook another 5 minutes. Season to taste.",
                            "While sauce cooks, stir ricotta with eggs and 1/2 tsp salt until blended.",
                            "Assemble layers: sauce, noodles; ricotta, sauce, cheese, noodles; ricotta, cheese, noodles; sauce, cheese.",
                            "Cover with foil and bake 20 minutes. Uncover and bake 15 minutes. Broil 1-2 minutes.",
                            "Let rest 10-15 minutes before serving.",
                        ),
                        notes = ""
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
        return internalCache.data.value.recipes[id] ?: throw IOException("no recipe for id $id")
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