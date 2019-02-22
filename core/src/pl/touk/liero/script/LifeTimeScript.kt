package pl.touk.liero.script

import pl.touk.liero.ecs.Entity

class LifeTimeScript(var timeLeftSec: Float) : Script {
    override fun update(me: Entity, timeStepSec: Float) {
        timeLeftSec -= timeStepSec
        if (timeLeftSec < 0) {
            me.dead = true
        }
    }
}