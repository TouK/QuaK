package pl.touk.liero.game.weapon

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.utils.Array
import ktx.box2d.body
import ktx.box2d.filter
import ktx.math.vec2
import pl.touk.liero.Ctx
import pl.touk.liero.ecs.*
import pl.touk.liero.entity.entity
import pl.touk.liero.game.cat_bulletRed
import pl.touk.liero.game.mask_bulletRed
import pl.touk.liero.script.Script
import pl.touk.liero.system.SoundSystem
import kotlin.random.Random.Default.nextFloat

class MiniGun(val ctx: Ctx): Weapon {
    var ammo = ctx.params.miniGunAmmo
    var overheat: Float = 0f
    var cooldown: Float = ctx.params.miniGunCooldown

    override fun update(timeStepSec: Float) {
        if( cooldown > 0) {
            cooldown -= timeStepSec
            if(cooldown < 0) {
                cooldown = 0f
            }
        }
        if(overheat > 0) {
            overheat -= timeStepSec
            if(overheat <= 0) {
                overheat = 0f
                ammo=ctx.params.miniGunAmmo
            }
        }
    }

    override fun isOverheat(): Boolean {
        return overheat > 0
    }

    override fun canAttack(): Boolean {
        if (ammo <= 0 && !isOverheat()) {
            overheat = ctx.params.miniGunOverheat
            return false
        }
        if(isOverheat()) {
            return false
        }
        if (cooldown <= 0) {
            cooldown = ctx.params.miniGunCooldown
            return true
        }
        else
            return false
    }

    override fun attack(ctx: Ctx, pos: Vector2, direction: Vector2) {
        ammo -= 1
        ctx.sound.playSoundSample(SoundSystem.SoundSample.DuckShort)
        fireMiniGun(ctx, pos, direction)
    }

    override val texture: Texture =
        Texture(TextureRegion(ctx.gameAtlas.findRegion("minigun")), 2.6f, 1f, vec2(-0.1f, -0.4f), scaleX = 0.5f, scaleY = 0.5f)

    override fun percentageCooldown(): Float {
        return overheat / ctx.params.miniGunOverheat
    }
}

fun fireMiniGun(ctx: Ctx, pos: Vector2, direction: Vector2) {
    ctx.engine.entity {
        body(ctx.world.body(BodyDef.BodyType.DynamicBody) {
            gravityScale = 0f
            linearDamping = 0f
            bullet = true
            var randomShot = Vector2(nextFloat()/ctx.params.miniGunDispersion, (nextFloat()-0.5f)/ctx.params.miniGunDispersion)
            linearVelocity.set(direction.add(randomShot.x,randomShot.y).scl(ctx.params.miniGunSpeed))
            val vec = Vector2(direction.nor()).scl(0.8f)
            position.set(pos.add(0f, -0.25f).add(vec))
            circle(ctx.params.miniGunSize) {
                filter {
                    categoryBits = cat_bulletRed
                    maskBits = mask_bulletRed
                }
            }
        })
        texture(ctx.gameAtlas.findRegion("kaczka-toukowa-sprites-01"), ctx.params.miniGunSize*11f, ctx.params.miniGunSize*11f)
        script(MiniGunScript(ctx))
    }
}

class MiniGunScript(val ctx: Ctx) : Script {

    var liveTime: Float = 0f
    var projectileAnimation: Animation<TextureRegion> = createProjectileAnimation(ctx)

    override fun beginContact(me: Entity, other: Entity, contact: Contact) {
        me.dead = true
        if (other.contains(energy)) {
            other[energy].energy -= ctx.params.miniGunDamage
        }
        ctx.actions.schedule(0) { playHitSound() }
    }

    private fun playHitSound() {
        ctx.sound.playSoundSample(SoundSystem.SoundSample.NesPew)
    }

    override fun update(me: Entity, timeStepSec: Float) {
        liveTime += timeStepSec
        val textureRegion = projectileAnimation.getKeyFrame(liveTime)
        me[texture].texture = textureRegion
        me[texture].angleDeg = me[body].linearVelocity.angle()
        return
    }

    companion object {
        private fun createProjectileAnimation(ctx: Ctx): Animation<TextureRegion> {
            val walkFrames: Array<TextureRegion> = Array()
            walkFrames.add(ctx.gameAtlas.findRegion("kaczka-toukowa-sprites-01"))
            walkFrames.add(ctx.gameAtlas.findRegion("kaczka-toukowa-sprites-02"))
            walkFrames.add(ctx.gameAtlas.findRegion("kaczka-toukowa-sprites-03"))
            walkFrames.add(ctx.gameAtlas.findRegion("kaczka-toukowa-sprites-04"))
            walkFrames.add(ctx.gameAtlas.findRegion("kaczka-toukowa-sprites-05"))
            walkFrames.add(ctx.gameAtlas.findRegion("kaczka-toukowa-sprites-06"))
            walkFrames.add(ctx.gameAtlas.findRegion("kaczka-toukowa-sprites-07"))
            walkFrames.add(ctx.gameAtlas.findRegion("kaczka-toukowa-sprites-08"))
            return Animation(0.025f, walkFrames, Animation.PlayMode.LOOP)
        }
    }

}
