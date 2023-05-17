package com.bryanspitz.recipes.app

import android.app.Application

class RecipesApplication : Application(), AppComponentSource {

    override lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.factory().create()
    }
}