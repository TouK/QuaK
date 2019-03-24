package pl.touk.liero.system

import pl.touk.liero.ecs.Engine
import pl.touk.liero.ecs.Entity
import pl.touk.liero.ecs.System
import pl.touk.liero.ecs.lifespan
import pl.touk.liero.game.WorldEngine

class LifeSpanSystem(val engine: Engine<Entity>,
                     val worldEngine: WorldEngine) : System {
    val family = engine.family(lifespan)
    override fun update(timeStepSec: Float) {
        family.foreach { ent, lifespan ->
            if (lifespan.lifeSpan + lifespan.begin < worldEngine.timeMs) {
                ent.dead = true
            }
        }
    }
}