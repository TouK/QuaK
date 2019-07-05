package pl.touk.liero.script

import com.badlogic.gdx.graphics.Color
import ktx.math.vec2
import pl.touk.liero.Ctx
import pl.touk.liero.RotateScript
import pl.touk.liero.ecs.Entity
import pl.touk.liero.entity.entity

class WinnerScript(val ctx: Ctx, val frags: Frags) : Script {
    override fun update(me: Entity, timeStepSec: Float) {
        if (frags.frags >= ctx.params.fragsLimit) {
            showWinner(me)
            me.scriptsToRemove += this
            ctx.engine.entity {
                script(RestartScript(ctx))
                text("", vec2(ctx.level.width/2f, ctx.level.height/4f), Color.WHITE, ctx.smallFont)
            }
        }
    }

    private fun showWinner(winner: Entity) {
        ctx.engine.entity {
            parent(winner)
            texture(ctx.gameAtlas.findRegion("winner"), 10f, 10f)
            script(RotateScript(30f))
        }
    }
}