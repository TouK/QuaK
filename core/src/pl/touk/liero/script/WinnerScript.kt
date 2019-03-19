package pl.touk.liero.script

import pl.touk.liero.Ctx
import pl.touk.liero.RotateScript
import pl.touk.liero.ecs.Entity
import pl.touk.liero.entity.entity

fun createWinnerScript(ctx: Ctx, left: Entity, right: Entity) {
    ctx.engine.entity {
        script(WinnerScript(ctx, left, right))
    }
}

class WinnerScript(val ctx: Ctx, val left: Entity, val right: Entity) : Script {
    override fun update(me: Entity, timeStepSec: Float) {
        if (ctx.leftFrags >= 4) {
            showWinner(left)
            me.dead = true
        }
        if (ctx.rightFrags >= 4) {
            showWinner(right)
            me.dead = true
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