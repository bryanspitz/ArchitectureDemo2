package com.bryanspitz.recipes.app

import android.app.Application
import io.mockk.mockk

class TestRecipesApplication : Application(), AppComponentSource {

    override val appComponent: AppComponent = mockk(relaxed = true)
}