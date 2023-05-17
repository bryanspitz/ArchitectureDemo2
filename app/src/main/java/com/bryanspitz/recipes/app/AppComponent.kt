package com.bryanspitz.recipes.app

import dagger.Component
import javax.inject.Scope

@Scope
annotation class AppScope

@AppScope
@Component
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(): AppComponent
    }
}