package com.bryanspitz.recipes.ui.add

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.mutableStateOf
import com.bryanspitz.recipes.R
import com.bryanspitz.recipes.testutils.startAndTest
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableSharedFlow

private const val BLANK_TITLE = "title must not be blank"

private const val TITLE = "Recipe Title"

internal class SaveFeatureTest : BehaviorSpec({
    val title = mutableStateOf("")
    val onSave = MutableSharedFlow<Any>()
    val activity: AddActivity = mockk()
    val errorState: SnackbarHostState = mockk()

    val feature = SaveFeature(
        title = title,
        onSave = onSave,
        activity = activity,
        errorState = errorState
    )

    every { activity.getString(R.string.error_title_blank) } returns BLANK_TITLE
    coEvery { errorState.showSnackbar(any<String>()) } returns SnackbarResult.Dismissed

    Given("feature is started") {
        feature.startAndTest {
            And("title is empty") {
                When("save button is clicked") {
                    onSave.emit(Unit)

                    Then("show error message") {
                        coVerify { errorState.showSnackbar(BLANK_TITLE) }
                    }
                }
            }

            And("title is blank") {
                title.value = "    "
                When("save button is clicked") {
                    onSave.emit(Unit)

                    Then("show error message") {
                        coVerify { errorState.showSnackbar(BLANK_TITLE) }
                    }
                }
            }

            And("all fields are acceptable") {
                title.value = TITLE

                When("save button is clicked") {
                    onSave.emit(Unit)

                    Then("do not show error") {
                        verify { errorState wasNot called }
                    }
                }
            }
        }
    }
})