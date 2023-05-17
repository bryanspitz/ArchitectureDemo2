package com.bryanspitz.recipes.app

import android.app.Application

class RecipesApplication : Application() {

    lateinit var component: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()

        component = DaggerAppComponent.factory().create()
    }
}