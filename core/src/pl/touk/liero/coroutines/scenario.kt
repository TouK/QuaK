package pl.touk.liero.coroutines

import pl.touk.liero.game.Clock
import kotlin.coroutines.Continuation
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.createCoroutine
import kotlin.coroutines.intrinsics.COROUTINE_SUSPENDED
import kotlin.coroutines.intrinsics.suspendCoroutineUninterceptedOrReturn
import kotlin.coroutines.resume

interface ScenarioContext {
    /**
     * Returns true if scenario is finished
     */
    fun step(): Boolean
    suspend fun yield()
    suspend fun wait(delaySec: Float)
    suspend fun waitMs(delayMs: Int)
    suspend fun waitUntil(cond: () -> Boolean)
    suspend fun stopWhile(cond: () -> Boolean)
}

class ScenarioContextImpl(val clock: Clock) : ScenarioContext, Continuation<Unit> {
    var nextStep: Continuation<Unit>? = null

    override val context = EmptyCoroutineContext

    override fun resumeWith(result: Result<Unit>) {
        result.getOrThrow()
        nextStep = null
    }

    override suspend fun yield() {
        return suspendCoroutineUninterceptedOrReturn { c ->
            nextStep = c
            COROUTINE_SUSPENDED
        }
    }

    override fun step(): Boolean {
        if (nextStep != null) {
            nextStep?.resume(Unit)
            return false
        } else {
            return true
        }
    }

    override suspend fun wait(delaySec: Float) {
        val waitUntil = clock.timeMs + delaySec * 1000
        while (clock.timeMs < waitUntil)
            yield()
    }

    override suspend fun waitMs(delayMs: Int) {
        val waitUntil = clock.timeMs + delayMs
        while (clock.timeMs < waitUntil)
            yield()
    }

    override suspend fun waitUntil(cond: () -> Boolean) {
        while(!cond())
            yield()
    }

    override suspend fun stopWhile(cond: () -> Boolean) {
        while(cond())
            yield()
    }
}

fun scenario(clock: Clock, block: suspend ScenarioContext.() -> Unit): ScenarioContext {
    val ctx = ScenarioContextImpl(clock)
    val cont = block.createCoroutine(ctx, ctx)
    ctx.nextStep = cont
    return ctx
}