package pl.touk.liero.game.weapon

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils
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
import pl.touk.liero.game.cat_red
import pl.touk.liero.game.mask_playerOnly
import pl.touk.liero.script.Script
import pl.touk.liero.system.SoundSystem

class Sword(val ctx: Ctx) : Weapon {

    var cooldown: Float = 0.5f
    override val name: String = "SWORD"
    var weapon: Entity? = null

    override val texture: Texture =
            Texture(TextureRegion(ctx.gameAtlas.findRegion("kaczkomiecz")), 2.6f, 0.6f, vec2(-0.1f, -0.4f), scaleX = 0.5f, scaleY = 0.5f)


    override fun update(timeStepSec: Float) {
        if( cooldown > 0) {
            cooldown -= timeStepSec
            if(cooldown < 0) {
                cooldown = 0f
            }
        }

    }

    override fun canAttack(): Boolean {
        if (cooldown <= 0) {
            cooldown = 0.5f
            return true
        }
        else
            return false
    }

    override fun attack(ctx: Ctx, pos: Vector2, direction: Vector2) {
        val quack1Or2 = MathUtils.random.nextInt(2)
        when (quack1Or2) {
            0 -> ctx.sound.playSoundSample(SoundSystem.SoundSample.Quack1)
            1 -> ctx.sound.playSoundSample(SoundSystem.SoundSample.Quack2)
        }
        strike(ctx, pos, direction, this.weapon!!)
    }

    override fun percentageCooldown(): Float {
        return cooldown / ctx.params.bazookaCooldown
    }

    private fun strike(ctx: Ctx, pos: Vector2, direction: Vector2, weapon: Entity) {
        weapon[pl.touk.liero.ecs.texture].texture = TextureRegion(ctx.gameAtlas.findRegion("frame0000"))
        val vec: Vector2
        if (direction.x > 0) {
            vec = Vector2(1f,0f).scl(1.1f)
        }
        else {
            vec = Vector2(-1f,0f).scl(1.1f)
        }
        ctx.engine.entity {
            body(ctx.world.body(BodyDef.BodyType.DynamicBody) {
                gravityScale = 0f
                linearDamping = 0f
                bullet = true
                position.set(pos.add(vec))
                box(0.75f,1.5f) {
                    filter {
                        categoryBits = cat_red
                        maskBits = mask_playerOnly
                    }
                }
            })
            texture(ctx.gameAtlas.findRegion("kaczkomiecz0"), 2.2f, 2.2f, Vector2(-0.6f,0.2f))
            val projectileAnimation = createProjectileAnimation(ctx)
            script(SwordStrikeScript(ctx.params.bazookaDirectDamage, projectileAnimation, ctx, weapon, direction))
        }
    }

    class SwordStrikeScript(val hitPoints: Float,
                            val projectileAnimation: Animation<TextureRegion>,
                            val ctx: Ctx,
                            val weapon: Entity,
                            val direction: Vector2) : Script {

        var liveTime: Float = 0f

        override fun beginContact(me: Entity, other: Entity, contact: Contact) {
            if (me.dead) {
                return
            }
//            me.dead = true
            if (other.contains(energy)) {
                other[energy].energy -= hitPoints
            }
        }

        override fun beforeDestroy(me: Entity) {
            weapon[texture].texture = TextureRegion(ctx.gameAtlas.findRegion("kaczkomiecz"))
            super.beforeDestroy(me)
        }

        override fun update(me: Entity, timeStepSec: Float) {
            liveTime += timeStepSec
            if(liveTime > projectileAnimation.animationDuration) {
                me.dead = true
            }
            val textureRegion = TextureRegion(projectileAnimation.getKeyFrame(liveTime))
            if(direction.x < 0) {
                textureRegion.flip(true,false)
            }
            me[texture].texture = textureRegion
            if(direction.x > 0) {
                me[texture].pos = Vector2(-0.6f,0.2f)
            } else {
                me[texture].pos = Vector2(0.6f,0.2f)
            }
            me[body].linearVelocity = weapon[body].linearVelocity
            return
        }

    }

    private fun createProjectileAnimation(ctx: Ctx): Animation<TextureRegion> {
        val walkFrames: Array<TextureRegion> = Array()
        walkFrames.add(ctx.gameAtlas.findRegion("kaczkomiecz0"))
        walkFrames.add(ctx.gameAtlas.findRegion("kaczkomiecz1"))
        walkFrames.add(ctx.gameAtlas.findRegion("kaczkomiecz2"))
        walkFrames.add(ctx.gameAtlas.findRegion("kaczkomiecz3"))
        walkFrames.add(ctx.gameAtlas.findRegion("kaczkomiecz4"))
        walkFrames.add(ctx.gameAtlas.findRegion("kaczkomiecz5"))
        return Animation(0.025f, walkFrames, Animation.PlayMode.NORMAL)
    }
}
