package com.bryanspitz.recipes.repository

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

fun <T> get(
    fetch: suspend () -> Unit,
    cache: () -> Flow<T>,
    fetchStrategy: FetchStrategy
) = flow {
    when (fetchStrategy) {
        FetchStrategy.CACHE_ONLY -> emitAll(cache())
        FetchStrategy.FETCH_THEN_CACHE -> {
            fetch()
            emitAll(cache())
        }

        FetchStrategy.CACHE_THEN_FETCH -> {
            coroutineScope {
                launch { fetch() }
                emitAll(cache())
            }
        }
    }
}
