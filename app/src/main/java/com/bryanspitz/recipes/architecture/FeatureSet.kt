package com.bryanspitz.recipes.architecture

import android.util.Log
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineStart.UNDISPATCHED
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class FeatureSet(private vararg val features: Feature) {

    suspend fun launchAll() {
        supervisorScope {
            features.forEach {
                launch(
                    CoroutineExceptionHandler { _, e ->
                        Log.e(it::class.simpleName!!, "feature crashed", e)
                    },
                    start = UNDISPATCHED,
                ) { it.start() }
            }
        }
    }
}