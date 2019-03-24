package pl.touk.liero.system

import pl.touk.liero.ecs.Engine
import pl.touk.liero.ecs.Entity
import pl.touk.liero.ecs.System
import pl.touk.liero.ecs.children

class ParentChildSystem(engine: Engine<Entity>) : System {
    val family = engine.family(children)

    override fun update(timeStepSec: Float) {
        family.foreach { entity, children ->
            if (entity.dead) {
                for (c in children.children) {
                    c.dead = true
                }
            }
        }
    }
}