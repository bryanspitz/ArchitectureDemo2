package com.bryanspitz.recipes.repository

import com.bryanspitz.recipes.testutils.collectAndTest
import com.bryanspitz.recipes.testutils.latestValue
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.MutableStateFlow

internal class GetTest : BehaviorSpec({
    val fetch = CompletableDeferred<Int>()
    val cache = MutableStateFlow(0)

    Given("get is called with CACHE_ONLY") {
        get(
            { cache.value = fetch.await() },
            { cache },
            FetchStrategy.CACHE_ONLY
        ).collectAndTest {

            Then("emit cache value") {
                it.latestValue shouldBe 0
            }

            And("cache is updated") {
                cache.value = 1

                Then("emit latest cache value") {
                    it.latestValue shouldBe 1
                }
            }

            When("fetch succeeds") {
                fetch.complete(2)

                Then("do not emit") {
                    it.latestValue shouldNotBe 2
                }
            }

            When("fetch fails") {
                fetch.completeExceptionally(IllegalStateException())

                Then("do not throw") {
                    it.errors.shouldBeEmpty()
                }
            }
        }
    }
    Given("get is called with FETCH_THEN_CACHE") {
        get(
            { cache.value = fetch.await() },
            { cache },
            FetchStrategy.FETCH_THEN_CACHE
        ).collectAndTest {

            Then("do not emit cache value") {
                it.values.shouldBeEmpty()
            }

            And("cache is updated") {
                cache.value = 1

                Then("do not emit") {
                    it.values.shouldBeEmpty()
                }
            }

            When("fetch succeeds") {
                fetch.complete(2)

                Then("emit fetched value") {
                    it.latestValue shouldBe 2
                }

                And("cache is updated") {
                    cache.value = 3

                    Then("emit latest cache value") {
                        it.latestValue shouldBe 3
                    }
                }
            }

            When("fetch fails") {
                fetch.completeExceptionally(IllegalStateException())

                Then("throw an exception") {
                    it.errors.any { it is IllegalStateException }
                }
            }
        }
    }
    Given("get is called with CACHE_THEN_FETCH") {
        get(
            { cache.value = fetch.await() },
            { cache },
            FetchStrategy.CACHE_THEN_FETCH
        ).collectAndTest {

            Then("emit cache value") {
                it.latestValue shouldBe 0
            }

            And("cache is updated") {
                cache.value = 1

                Then("emit latest value") {
                    it.latestValue shouldBe 1
                }
            }

            When("fetch succeeds") {
                fetch.complete(2)

                Then("emit fetched value") {
                    it.latestValue shouldBe 2
                }

                And("cache is updated") {
                    cache.value = 3

                    Then("emit latest cache value") {
                        it.latestValue shouldBe 3
                    }
                }
            }

            When("fetch fails") {
                fetch.completeExceptionally(IllegalStateException())

                Then("throw an exception") {
                    it.errors.any { it is IllegalStateException }
                }
            }
        }
    }
})