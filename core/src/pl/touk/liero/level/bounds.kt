package pl.touk.liero.level

import com.badlogic.gdx.physics.box2d.BodyDef
import pl.touk.liero.entity.entity
import pl.touk.liero.Ctx
import ktx.box2d.body

internal fun createBounds(ctx: Ctx, w: Float, h: Float) {
    // dół, góra, lewo, prawo
    createBound(ctx, w / 2, -0.5f * h, 2 * w, 2f)
    createBound(ctx, w / 2, 1.5f * h, 2 * w, 2f)
    createBound(ctx, -0.5f * w, h / 2, 2f, 2 * h)
    createBound(ctx, 1.5f * w, h / 2, 2f, 2 * h)
}

private fun createBound(ctx: Ctx, x: Float, y: Float, w: Float, h: Float) {
    ctx.engine.entity {
        body(ctx.world.body(BodyDef.BodyType.StaticBody) {
            position.set(x, y)
            box(w, h) {
                restitution = 0f
            }
        })
    }
}
