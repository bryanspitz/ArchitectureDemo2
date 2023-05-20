package com.bryanspitz.recipes.ui.main

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ActivityScenario
import com.karumi.shot.ActivityScenarioUtils.waitForActivity
import com.karumi.shot.ScreenshotTest
import org.junit.Rule
import org.junit.Test

internal class MainActivityTest : ScreenshotTest {

    @Rule
    @JvmField
    val composeRule = createComposeRule()

    @Test
    fun rendersTheDefaultActivityState() {
        val activity = ActivityScenario.launch(MainActivity::class.java)

        compareScreenshot(activity.waitForActivity())
    }

    @Test
    fun composeTest() {
        composeRule.setContent {
            MainLayout(
                recipes = emptyList(),
                onAdd = {},
                onClick = {}
            )
        }

        compareScreenshot(composeRule)
    }
}