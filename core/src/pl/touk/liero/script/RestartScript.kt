package pl.touk.liero.script

import pl.touk.liero.Ctx
import pl.touk.liero.ecs.Entity
import pl.touk.liero.ecs.text
import pl.touk.liero.screen.UiEvent

class RestartScript(val ctx: Ctx) : Script {
    val restartAt = ctx.worldEngine.timeMs + 6000

    override fun update(me: Entity, timeStepSec: Float) {
        val secondsLeft = (restartAt - ctx.worldEngine.timeMs) / 1000
        me[text].text = "Restart in $secondsLeft sec"
        if(secondsLeft <= 0) {
            ctx.uiEvents += UiEvent.Restart
        }
    }
}