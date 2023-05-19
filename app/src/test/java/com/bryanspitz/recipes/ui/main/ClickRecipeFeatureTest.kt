package com.bryanspitz.recipes.ui.main

import android.content.Intent
import com.bryanspitz.recipes.testutils.startAndTest
import com.bryanspitz.recipes.ui.detail.DetailActivity
import com.bryanspitz.recipes.ui.detail.PARAM_RECIPE_ID
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.EqMatcher
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.verify
import kotlinx.coroutines.flow.MutableSharedFlow

private const val RECIPE_ID = "recipeId"

internal class ClickRecipeFeatureTest : BehaviorSpec({
    val activity: MainActivity = mockk(relaxUnitFun = true)
    val onClick = MutableSharedFlow<String>()
    val intent: Intent = mockk()

    val feature = ClickRecipeFeature(
        activity = activity,
        onClick = onClick
    )

    Given("Intent is mocked") {
        mockkConstructor(Intent::class) {
            every {
                constructedWith<Intent>(EqMatcher(activity), EqMatcher(DetailActivity::class.java))
                    .putExtra(PARAM_RECIPE_ID, RECIPE_ID)
            } returns intent

            And("feature is started") {
                feature.startAndTest {

                    When("recipe card is clicked") {
                        onClick.emit(RECIPE_ID)

                        Then("start DetailActivity") {
                            verify { activity.startActivity(intent) }
                        }
                    }
                }
            }
        }
    }
})