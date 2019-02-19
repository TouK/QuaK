package pl.touk.liero.system

import pl.touk.liero.ecs.body
import pl.touk.liero.ecs.Engine
import pl.touk.liero.ecs.Entity
import pl.touk.liero.ecs.System

class BodyDisposeSystem(val engine: Engine<Entity>) : System {
    val family = engine.family(body)
    override fun update(timeStepSec: Float) {
        family.foreach {
            ent, body ->
            if (ent.dead) {
                body.world.destroyBody(body)
            }
        }
    }
}