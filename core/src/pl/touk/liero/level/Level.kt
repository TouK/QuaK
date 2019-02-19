package pl.touk.liero.level

import pl.touk.liero.Ctx

interface Level {
    val width: Float
    val height: Float

    fun start(ctx: Ctx)
    fun start(ctx: Ctx, wave: Int): Unit = start(ctx, 1)
    fun dispose()
}