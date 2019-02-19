package pl.touk.liero

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color

class LieroGameDev(ctx: Ctx) : LieroGame(ctx) {

    override fun render() {
        super.render()
        ctx.batch.projectionMatrix.set(ctx.hudCamera.combined)
        ctx.batch.begin()
        ctx.smallFont.setColor(Color.WHITE)
        ctx.smallFont.draw(ctx.batch, Gdx.graphics.framesPerSecond.toString(), 50f, 50f)
        ctx.batch.end()
    }
}