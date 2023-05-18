@file:OptIn(ExperimentalCoroutinesApi::class)

package com.bryanspitz.recipes.testutils

import com.bryanspitz.recipes.architecture.Feature
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass

suspend fun Feature.startAndTest(
    context: CoroutineContext = UnconfinedTestDispatcher(),
    tests: suspend Job.(ExceptionHolder) -> Unit
) {
    launchAndTest(this::start, context = context, tests = tests)
}

suspend fun <T> Flow<T>.collectAndTest(
    context: CoroutineContext = UnconfinedTestDispatcher(),
    tests: suspend Job.(TestCollector<T>) -> Unit
) {
    val collector = TestCollector<T>()
    launchAndTest(
        underTest = { with(collector) { collect() } },
        context = context
    ) { tests(collector) }
}

suspend fun launchAndTest(
    underTest: suspend () -> Unit,
    context: CoroutineContext = UnconfinedTestDispatcher(),
    tests: suspend Job.(ExceptionHolder) -> Unit
) {
    val exceptionHolder = ExceptionHolder()

    supervisorScope {
        val job = launch(context + CoroutineExceptionHandler { _, throwable ->
            if (!exceptionHolder.onException(throwable)) {
                this.cancel("Threw unexpected exception", throwable)
            }
        }) { underTest() }
        job.tests(exceptionHolder)
        job.cancelAndJoin()
    }
}

suspend inline fun <reified R> asyncAndTest(
    crossinline underTest: suspend () -> R,
    context: CoroutineContext = UnconfinedTestDispatcher(),
    crossinline tests: suspend Deferred<R>.() -> Unit
) {
    supervisorScope {
        val deferred = async(context) { underTest() }
        deferred.tests()
        deferred.cancelAndJoin()
    }
}

class TestCollector<T> {

    val values = mutableListOf<T>()
    val errors = mutableListOf<Throwable>()

    suspend fun Flow<T>.collect() = catch { errors.add(it) }.collectLatest { values.add(it) }
}

val <T> TestCollector<T>.latestValue get() = values.last()

class ExceptionHolder {
    private var expectedClass: KClass<out Throwable>? = null
    private var exception: Throwable? = null

    /**
     * Test code should call this to expect an exception to be thrown by the code under test
     * when the test code is executed.
     *
     * Throws a [NotThrownException] if no exception was thrown, or one was thrown of an unexpected
     * type.
     */
    suspend fun expect(expectedClass: KClass<out Throwable>, tests: suspend () -> Unit) {
        this.expectedClass = expectedClass
        tests()
        if (!expectedClass.isInstance(exception)) {
            throw NotThrownException(
                message = "${expectedClass.simpleName} was expected but $exception was thrown",
                cause = exception
            )
        }
        this.expectedClass = null
    }

    /**
     * Called when an exception is caught from the code under test.
     *
     * Returns true if an exception (not necessarily of this type) was expected, false otherwise.
     */
    fun onException(t: Throwable): Boolean {
        exception = t
        return expectedClass != null
    }
}

class NotThrownException(message: String, override val cause: Throwable?) : Exception(
    message, cause
)