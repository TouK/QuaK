package pl.touk.liero.system

import pl.touk.liero.ecs.Engine
import pl.touk.liero.ecs.Entity
import pl.touk.liero.ecs.System
import pl.touk.liero.ecs.energy

class EnergySystem(val engine: Engine<Entity>) : System {
    val family = engine.family(energy)
    override fun update(timeStepSec: Float) {
        family.foreach { ent, energy ->
            if (energy.energy <= 0f) {
                ent.dead = true
            }
        }
    }
}