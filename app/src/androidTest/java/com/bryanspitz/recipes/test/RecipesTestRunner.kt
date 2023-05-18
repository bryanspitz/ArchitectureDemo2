package com.bryanspitz.recipes.test

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import com.bryanspitz.recipes.app.TestRecipesApplication
import com.karumi.shot.ShotTestRunner

@Suppress("unused")
class RecipesTestRunner: ShotTestRunner() {

    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(cl, TestRecipesApplication::class.qualifiedName, context)
    }
}