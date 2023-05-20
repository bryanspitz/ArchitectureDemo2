package com.bryanspitz.recipes.util

import android.util.Log
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import kotlin.coroutines.CoroutineContext

@Module
class CoroutineDispatcherModule {

    @Provides
    @UI
    fun uiDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @IO
    fun ioDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @SafeUiDispatcher
    fun safeCoroutineContext(
        @UI uiDispatcher: CoroutineDispatcher
    ): CoroutineContext {
        return uiDispatcher + CoroutineExceptionHandler { _, e ->
            Log.e("SafeCoroutineDispatcher", "error dispatching state", e)
        }
    }

    @Qualifier
    annotation class SafeUiDispatcher

    @Qualifier
    annotation class UI

    @Qualifier
    annotation class IO
}