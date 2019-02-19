package pl.touk.liero.system

import pl.touk.liero.ecs.Engine
import pl.touk.liero.ecs.Entity
import pl.touk.liero.ecs.System

class ScriptUpdateSystem(val engine: Engine<Entity>) : System {
    override fun update(timeStepSec: Float) {
        engine.ents.forEach { e ->
            e.scripts.forEach { s ->
                s.update(e, timeStepSec)
            }
            for (s in e.scriptsToRemove) {
                e.scripts.remove(s)
            }
            e.scriptsToRemove.clear()
            for (s in e.scriptsToAdd) {
                e.scripts.add(s)
            }
            e.scriptsToAdd.clear()
        }
    }
}

class ScriptBeforeDestroySystem(val engine: Engine<Entity>) : System {
    override fun update(timeStepSec: Float) {
        engine.ents.forEach { e ->
            if (e.dead) {
                e.scripts.forEach { s ->
                    s.beforeDestroy(e)
                }
            }
        }
    }
}