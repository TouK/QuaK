package pl.touk.liero.script

import com.badlogic.gdx.Input
import pl.touk.liero.Ctx
import pl.touk.liero.RotateScript
import pl.touk.liero.ecs.Entity
import pl.touk.liero.entity.entity
import pl.touk.liero.gdx.ifJustPressed

class WinnerScript(val ctx: Ctx, val frags: Frags) : Script {
    override fun update(me: Entity, timeStepSec: Float) {
        Input.Keys.F3.ifJustPressed {
            ctx.rightFrags.frags++
        }
        Input.Keys.F4.ifJustPressed {
            ctx.rightFrags.frags++
        }

        if (frags.frags >= ctx.params.fragsLimit) {
            showWinner(me)
            me.scriptsToRemove += this
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