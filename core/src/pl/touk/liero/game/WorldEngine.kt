package pl.touk.liero.game

import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World
import pl.touk.liero.ecs.Actions
import pl.touk.liero.ecs.Engine
import pl.touk.liero.ecs.Entity
import pl.touk.liero.utils.FirstOrderFilter
import ktx.box2d.body
import ktx.collections.GdxArray
import ktx.math.vec2
import pl.touk.liero.game.Clock

class WorldEngine : Clock {
    val actions = Actions()
    val engine = Engine<Entity>()
    val world = World(vec2(0f, -10f), true)
    val baseBody = world.body(BodyDef.BodyType.StaticBody) {}

    var timeScale = FirstOrderFilter(0.4f, 1f, 1f)
    override var timeMs: Int = 0

    fun dispose() {
        actions.reset()
        engine.clearEntities()
        world.dispose()
    }

    fun clearBodies() {
        val bodies = GdxArray<Body>(world.bodyCount)
        world.getBodies(bodies)
        for (b in bodies) {
            if (b != baseBody) {
                world.destroyBody(b)
            }
        }
    }

    fun clear() {
        timeMs = 0
        engine.clearEntities()
        clearBodies()
        resetTimeScale()
        actions.reset()
    }

    fun resetTimeScale() {
        timeScale.state = 1f
        timeScale.target = 1f
    }

    fun update(timeStepSec: Float) {
        engine.update(timeStepSec)
    }
}