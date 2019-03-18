package pl.touk.liero.game.weapon

import com.badlogic.gdx.physics.box2d.BodyDef
import ktx.box2d.body
import ktx.box2d.filter
import pl.touk.liero.Ctx
import pl.touk.liero.ecs.Entity
import pl.touk.liero.entity.entity
import pl.touk.liero.game.cat_red
import pl.touk.liero.game.mask_red

fun createWeapon(ctx: Ctx, x: Float, y: Float): Entity {
    val weaponBody = ctx.world.body(BodyDef.BodyType.DynamicBody) {
        position.set(x, y)
        angularDamping = ctx.params.weaponAngularDamping
        fixedRotation = true
        box(ctx.params.weaponBodyWidth, ctx.params.weaponBodyHeight) {
            density = 1f
            restitution = 0.1f
            friction = 2f
            isSensor = true
            filter {
                categoryBits = cat_red
                maskBits = mask_red
            }
        }
    }

    return ctx.engine.entity {
        body(weaponBody)
    }
}