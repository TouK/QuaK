package pl.touk.liero.system

import pl.touk.liero.ecs.*
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