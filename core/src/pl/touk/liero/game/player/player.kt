package pl.touk.liero.game.player

import com.badlogic.gdx.physics.box2d.BodyDef
import ktx.box2d.body
import ktx.box2d.filter
import pl.touk.liero.Ctx
import pl.touk.liero.PlayerScript
import pl.touk.liero.ecs.Entity
import pl.touk.liero.entity.entity
import pl.touk.liero.game.PlayerControl
import pl.touk.liero.game.cat_red
import pl.touk.liero.game.joint.createWeaponJoint
import pl.touk.liero.game.gun.Bazooka
import pl.touk.liero.game.mask_red

fun createPlayer(ctx: Ctx, x: Float, y: Float, playerControl: PlayerControl, weapon: Entity) {
    val playerBody = ctx.world.body(BodyDef.BodyType.DynamicBody) {
        position.set(x, y)
        linearDamping = 0f
        fixedRotation = true
        gravityScale = 2f
        circle(radius = ctx.params.playerSize / 2f) {
            density = 1f
            restitution = 0.1f
            friction = 2f
            filter {
                categoryBits = cat_red
                maskBits = mask_red
            }
        }
    }

    ctx.engine.entity {
        body(playerBody)
        child(weapon)
        joint(ctx.world.createJoint(createWeaponJoint(ctx, playerBody, weapon[pl.touk.liero.ecs.body])))
        texture(ctx.gameAtlas.findRegion("circle"), ctx.params.playerSize, ctx.params.playerSize)
        energy(ctx.params.playerTotalHealth)
        val bazooka = Bazooka(ctx)
        script(PlayerScript(ctx, playerControl, bazooka))

        // can be only one render script per Entity
        renderScript(HealthAndAmmoBar(ctx, bazooka))
    }
}
