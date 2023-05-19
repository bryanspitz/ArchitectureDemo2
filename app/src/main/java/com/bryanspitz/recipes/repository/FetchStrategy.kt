package com.bryanspitz.recipes.repository

enum class FetchStrategy {
    CACHE_ONLY,
    FETCH_THEN_CACHE,
    CACHE_THEN_FETCH
}