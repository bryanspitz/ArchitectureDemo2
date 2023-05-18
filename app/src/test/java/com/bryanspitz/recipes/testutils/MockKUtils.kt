package com.bryanspitz.recipes.testutils

import io.mockk.MockKStubScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

fun <T> MockKStubScope<T, *>.deferReturn(): CompletableDeferred<T> {
    val deferred = CompletableDeferred<T>()
    coAnswers {
        try {
            deferred.await()
        } catch (ce: CancellationException) {
            deferred.cancel(ce)
            throw ce
        }
    }
    return deferred
}

fun <T> MockKStubScope<Flow<T>, *>.returnsSharedFlow(): MutableSharedFlow<T> {
    val flow = MutableSharedFlow<T>()
    returns(flow)
    return flow
}

fun <T> MockKStubScope<StateFlow<T>, *>.returnsStateFlow(
    initialValue: T
): MutableStateFlow<T> {
    val flow = MutableStateFlow<T>(initialValue)
    returns(flow)
    return flow
}