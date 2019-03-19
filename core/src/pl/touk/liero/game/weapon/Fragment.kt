package pl.touk.liero.game.weapon

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import ktx.math.vec2
import pl.touk.liero.Ctx
import pl.touk.liero.ecs.Texture
import pl.touk.liero.game.projectile.fireBazooka
import pl.touk.liero.game.projectile.fireFragments

class Fragment(val ctx: Ctx) : Weapon {
    var cooldown: Float = ctx.params.fragzookaCooldown
    val name: String = "FRAGMENT"

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
            cooldown = ctx.params.fragzookaCooldown
            return true
        }
        else
            return false
    }

    override fun attack(ctx: Ctx, pos: Vector2, direction: Vector2) {
        fireFragments(ctx, pos, direction)
    }

    override val texture: Texture =
            Texture(TextureRegion(ctx.gameAtlas.findRegion("kaczkozooka")), 2.6f, 1f, vec2(0f, -0.3f))

    override fun percentageCooldown(): Float {
        return cooldown / ctx.params.fragzookaCooldown
    }
}