package pl.touk.liero.level

import com.badlogic.gdx.physics.box2d.BodyDef
import ktx.box2d.body
import ktx.box2d.filter
import pl.touk.liero.Ctx
import pl.touk.liero.entity.entity
import pl.touk.liero.game.cat_ground
import pl.touk.liero.game.mask_ground

internal fun createBounds(ctx: Ctx, w: Float, h: Float) {
    // dół, góra, lewo, prawo
    createBound(ctx, w / 2, 0f, w, 0.2f)
    createBound(ctx, w / 2, h, w, 0.2f)
    createBound(ctx, 0f, h / 2, 0.2f, h)
    createBound(ctx, w, h / 2, 0.2f, h)
}

private fun createBound(ctx: Ctx, x: Float, y: Float, w: Float, h: Float) {
    ctx.engine.entity {
        body(ctx.world.body(BodyDef.BodyType.StaticBody) {
            position.set(x, y)
            box(w, h) {
                restitution = 0f
                filter {
                    categoryBits = cat_ground
                    maskBits = mask_ground
                }
            }
        })
    }
}
