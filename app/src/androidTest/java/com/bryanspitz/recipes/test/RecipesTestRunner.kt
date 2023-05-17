package com.bryanspitz.recipes.test

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import com.bryanspitz.recipes.app.TestRecipesApplication

@Suppress("unused")
class RecipesTestRunner: AndroidJUnitRunner() {

    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(cl, TestRecipesApplication::class.qualifiedName, context)
    }
}