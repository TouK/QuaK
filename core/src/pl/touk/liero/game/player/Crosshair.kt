package pl.touk.liero.game.player

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import pl.touk.liero.Ctx
import pl.touk.liero.ecs.Entity
import pl.touk.liero.ecs.SpriteRenderScript
import pl.touk.liero.ecs.body
import kotlin.math.cos
import kotlin.math.sin


class Crosshair(val ctx: Ctx) : SpriteRenderScript {

    private val yellow = ctx.gameAtlas.findRegion("ammo")


    override fun render(self: Entity, batch: SpriteBatch, timeStepSec: Float) {
        val pos = self[body].position
        val angle = self[body].angle
        val r = 1.0f

        batch.draw(yellow, pos.x + (r * cos(angle)), pos.y + (r * sin(angle)), 0.5f, 0.5f)

    }


}
