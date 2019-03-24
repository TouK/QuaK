package pl.touk.liero.game.weapon

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import ktx.math.vec2
import pl.touk.liero.Ctx
import pl.touk.liero.ecs.Texture
import pl.touk.liero.game.projectile.fireKaczkosznikov
import pl.touk.liero.system.SoundSystem

class Kaczkosznikov(val ctx: Ctx) : Weapon {
    var ammo = ctx.params.kaczkosznikovAmmo
    var cooldown: Float = ctx.params.kaczkosznikovCooldown

    override fun update(timeStepSec: Float) {
        if (cooldown > 0) {
            cooldown -= timeStepSec
            if (cooldown < 0) {
                cooldown = 0f
            }
        }
    }

    override fun canAttack(): Boolean {
        if (cooldown <= 0) {
            cooldown = ctx.params.kaczkosznikovCooldown
            return true
        } else
            return false
    }

    override fun attack(ctx: Ctx, pos: Vector2, direction: Vector2) {
        ammo -= ctx.params.kaczkosznikovBulletsCount
        for (i in 0..ctx.params.kaczkosznikovBulletsCount) {
            ctx.actions.schedule(i * 0.1f) {
                ctx.sound.playSoundSample(SoundSystem.SoundSample.DuckShort)
                fireKaczkosznikov(ctx, pos, direction)
            }
        }
    }

    override val texture: Texture =
            Texture(TextureRegion(ctx.gameAtlas.findRegion("kaczkorabinmaszynowy")), 2f, 1f, vec2(-0.1f, -0.4f), scaleX = 0.5f, scaleY = 0.5f)

    override fun percentageCooldown(): Float {
        return cooldown / ctx.params.kaczkosznikovCooldown
    }
}
