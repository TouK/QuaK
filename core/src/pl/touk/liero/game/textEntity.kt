package pl.touk.liero.game

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Interpolation
import ktx.math.vec2
import pl.touk.liero.Ctx
import pl.touk.liero.ecs.Entity
import pl.touk.liero.ecs.text
import pl.touk.liero.entity.entity
import pl.touk.liero.script.Script
import pl.touk.liero.utils.rnd

fun createText(ctx: Ctx, txt: String) {
    ctx.engine.entity {
        text(txt, vec2(ctx.level.width / 2f, ctx.level.height / 2f), Color.WHITE, ctx.smallFont)
        script(TextDriftScript(0f, 0f))
    }
}

class TextDriftScript(val vx: Float = rnd(-0.5f, 0.5f),
                      val vy: Float = rnd(1f)) : Script {

    val totalLifetime = 2f
    var lifetime: Float = 0f
    val tweening = Interpolation.pow5In

    override fun update(me: Entity, timeStepSec: Float) {
        lifetime += timeStepSec
        if (lifetime > totalLifetime) {
            me.dead = true
        }
        me[text].pos.x = me[text].pos.x + vx * timeStepSec
        me[text].pos.y = me[text].pos.y + vy * timeStepSec
        me[text].color.a = 1f - tweening.apply(lifetime / totalLifetime)
    }
}