package com.bryanspitz.recipes.ui.main

import android.content.Intent
import com.bryanspitz.recipes.testutils.startAndTest
import com.bryanspitz.recipes.ui.add.AddActivity
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.verify
import kotlinx.coroutines.flow.MutableSharedFlow

internal class AddRecipeFeatureTest : BehaviorSpec({
    val activity: MainActivity = mockk(relaxUnitFun = true)
    val onAdd = MutableSharedFlow<Any>()

    val feature = AddRecipeFeature(
        activity = activity,
        onAdd = onAdd
    )

    Given("Intent is mocked") {
        mockkConstructor(Intent::class) {

            And("feature is started") {
                feature.startAndTest {

                    When("add button is clicked") {
                        onAdd.emit(Unit)

                        Then("start AddActivity") {
                            verify {
                                activity.startActivity(match {
                                    // Weird way of comparing expected with actual Intent, but
                                    // I couldn't come up with any other way that worked.
                                    it.toString() == Intent(
                                        activity,
                                        AddActivity::class.java
                                    ).toString()
                                })
                            }
                        }
                    }
                }
            }
        }
    }
})