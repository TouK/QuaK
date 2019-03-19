package pl.touk.liero.game.player

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.utils.Array
import ktx.box2d.body
import ktx.box2d.filter
import ktx.math.vec2
import pl.touk.liero.Ctx
import pl.touk.liero.PlayerScript
import pl.touk.liero.entity.entity
import pl.touk.liero.game.PlayerControl
import pl.touk.liero.game.cat_red
import pl.touk.liero.game.weapon.Bazooka
import pl.touk.liero.game.joint.createWeaponJoint
import pl.touk.liero.game.mask_red
import pl.touk.liero.system.BloodScript


fun createPlayer(ctx: Ctx, x: Float, y: Float, playerControl: PlayerControl) {
    val playerBody = ctx.world.body(BodyDef.BodyType.DynamicBody) {
        position.set(x, y)
        linearDamping = 0f
        fixedRotation = true
        gravityScale = ctx.params.playerGravityScale
        circle(radius = ctx.params.playerSize / 2f) {
            density = 1f
            restitution = 0.1f
            friction = 0.1f
            filter {
                categoryBits = cat_red
                maskBits = mask_red
            }
        }
    }

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

    val bazooka = Bazooka(ctx)
    val movementAnimation = createMovementAnimation(ctx)
    val idleAnimation = createStandAnimation(ctx)
    val state = PlayerState(bazooka, mutableListOf(bazooka))

    ctx.engine.entity {
        body(playerBody)
        joint(ctx.world.createJoint(createWeaponJoint(ctx, playerBody, weaponBody)))
        texture(ctx.gameAtlas.findRegion("circle"), ctx.params.playerSize, ctx.params.playerSize, scale = 1.4f)
        energy(ctx.params.playerTotalHealth)
        script(PlayerScript(ctx, playerControl, state, movementAnimation, idleAnimation))
        script(BloodScript(ctx))

        // can be only one render script per Entity
        renderScript(HealthAndAmmoBar(ctx, state))
    }

    ctx.engine.entity {
        body(weaponBody)
        texture(bazooka.texture)
    }
}

private fun createMovementAnimation(ctx: Ctx): Animation<TextureRegion> {
    val walkFrames: Array<TextureRegion> = Array()
    walkFrames.add(ctx.gameAtlas.findRegion("blobAnimation0"))
    walkFrames.add(ctx.gameAtlas.findRegion("blobAnimation1"))
    walkFrames.add(ctx.gameAtlas.findRegion("blobAnimation2"))
    walkFrames.add(ctx.gameAtlas.findRegion("blobAnimation3"))
    walkFrames.add(ctx.gameAtlas.findRegion("blobAnimation4"))
    walkFrames.add(ctx.gameAtlas.findRegion("blobAnimation5"))
    walkFrames.add(ctx.gameAtlas.findRegion("blobAnimation6"))
    walkFrames.add(ctx.gameAtlas.findRegion("blobAnimation7"))
    walkFrames.add(ctx.gameAtlas.findRegion("blobAnimation8"))
    walkFrames.add(ctx.gameAtlas.findRegion("blobAnimation9"))
    walkFrames.add(ctx.gameAtlas.findRegion("blobAnimation10"))
    walkFrames.add(ctx.gameAtlas.findRegion("blobAnimation11"))
    walkFrames.add(ctx.gameAtlas.findRegion("blobAnimation12"))
    walkFrames.add(ctx.gameAtlas.findRegion("blobAnimation13"))
    walkFrames.add(ctx.gameAtlas.findRegion("blobAnimation14"))
    return Animation(0.025f, walkFrames, Animation.PlayMode.LOOP)
}
private fun createStandAnimation(ctx: Ctx): Animation<TextureRegion> {
    val walkFrames: Array<TextureRegion> = Array()
    walkFrames.add(ctx.gameAtlas.findRegion("blobIdle0"))
    walkFrames.add(ctx.gameAtlas.findRegion("blobIdle1"))
    walkFrames.add(ctx.gameAtlas.findRegion("blobIdle2"))
    walkFrames.add(ctx.gameAtlas.findRegion("blobIdle3"))
    walkFrames.add(ctx.gameAtlas.findRegion("blobIdle4"))
    walkFrames.add(ctx.gameAtlas.findRegion("blobIdle5"))
    return Animation(0.05f, walkFrames, Animation.PlayMode.LOOP)
}
