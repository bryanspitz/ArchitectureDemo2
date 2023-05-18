package com.bryanspitz.recipes.architecture

import com.bryanspitz.recipes.testutils.deferReturn
import com.bryanspitz.recipes.testutils.launchAndTest
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk

internal class FeatureSetTest : BehaviorSpec({

    val feature1: Feature = mockk()
    val feature2: Feature = mockk()

    val set = FeatureSet(feature1, feature2)

    Given("features will run indefinitely") {
        val job1 = coEvery { feature1.start() }.deferReturn()
        val job2 = coEvery { feature2.start() }.deferReturn()

        When("feature set is launched") {
            launchAndTest(set::launchAll) {
                Then("launch all features") {
                    coVerify { feature1.start() }
                    coVerify { feature2.start() }
                }

                And("feature set job is cancelled") {
                    cancel()

                    Then("cancel all features") {
                        job1.isCancelled.shouldBeTrue()
                        job2.isCancelled.shouldBeTrue()
                    }
                }

                And("one feature fails") {
                    job1.completeExceptionally(IllegalStateException())

                    Then("do not cancel remaining features") {
                        job2.isCancelled.shouldBeFalse()
                    }
                }
            }
        }
    }
})