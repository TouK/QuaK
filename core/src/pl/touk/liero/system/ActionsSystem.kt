package pl.touk.liero.system

import pl.touk.liero.ecs.Actions
import pl.touk.liero.ecs.System
import pl.touk.liero.game.Clock

class ActionsSystem(val clock: Clock, val actions: Actions) : System {
    override fun update(timeStepSec: Float) {
        actions.update(clock.timeMs)
    }
}