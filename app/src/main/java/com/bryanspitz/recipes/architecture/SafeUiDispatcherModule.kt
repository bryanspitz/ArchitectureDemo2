package com.bryanspitz.recipes.architecture

import android.util.Log
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import kotlin.coroutines.CoroutineContext

@Module
class SafeUiDispatcherModule {

    @Provides
    @SafeUiDispatcher
    fun safeCoroutineContext(): CoroutineContext {
        return Dispatchers.Main + CoroutineExceptionHandler { _, e ->
            Log.e("SafeCoroutineDispatcher", "error dispatching state", e)
        }
    }

    @Qualifier
    annotation class SafeUiDispatcher
}