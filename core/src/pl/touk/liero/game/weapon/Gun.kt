package pl.touk.liero.game.weapon

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import ktx.math.vec2
import pl.touk.liero.Ctx
import pl.touk.liero.ecs.Texture
import pl.touk.liero.game.projectile.fireBazooka
import pl.touk.liero.game.projectile.fireGun

class Gun(val ctx: Ctx) : Weapon {
    var cooldown: Float = ctx.params.gunCooldown
    val name: String = "GUN"

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
            cooldown = ctx.params.gunCooldown
            return true
        }
        else
            return false
    }

    override fun attack(ctx: Ctx, pos: Vector2, direction: Vector2) {
        fireGun(ctx, pos, direction)
    }

    override val texture: Texture =
            Texture(TextureRegion(ctx.gameAtlas.findRegion("kaczkospluwa")), 1.27f, 1f, vec2(0f, -0.3f))

    override fun percentageCooldown(): Float {
        return cooldown / ctx.params.gunCooldown
    }
}
