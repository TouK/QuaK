package pl.touk.liero.game.weapon

import com.badlogic.gdx.math.Vector2
import pl.touk.liero.Ctx
import pl.touk.liero.ecs.Texture

interface Weapon {
    val texture: Texture
    fun update(timeStepSec: Float)
    fun canAttack(): Boolean
    fun attack(ctx: Ctx, pos: Vector2, direction: Vector2)
    fun percentageCooldown(): Float
}
