package pl.touk.liero.system

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.physics.box2d.*
import pl.touk.liero.ecs.Entity
import pl.touk.liero.ecs.System
import pl.touk.liero.game.Clock

class WorldSystem(val world: World, val clock: Clock, val fixedTimeStep: Float? = null) : System, ContactListener {
    init {
        world.setContactListener(this)
        if (fixedTimeStep != null) {
            Gdx.app.log("WorldSystem", "Running world in fixed time step: $fixedTimeStep")
        }
    }

    override fun update(timeStepSec: Float) {
        if (timeStepSec == 0f) {
            return
        }
        if (fixedTimeStep != null) {
            // Na potrzeby pętli testowych (powtarzalne scenariusze)
            world.step(fixedTimeStep, 8, 3)
            clock.timeMs += (fixedTimeStep * 1000).toInt()
        } else {
            world.step(timeStepSec, 8, 3)
            clock.timeMs += (timeStepSec * 1000).toInt()
        }
    }

    override fun beginContact(contact: Contact?) {
        if (contact != null) {
            val firstEntity = contact.fixtureA.body.userData as Entity
            val secondEntity = contact.fixtureB.body.userData as Entity
            firstEntity.scripts.forEach {
                it.beginContact(firstEntity, secondEntity, contact)
            }
            secondEntity.scripts.forEach {
                it.beginContact(secondEntity, firstEntity, contact)
            }
        }
    }

    override fun endContact(contact: Contact?) {
        if (contact != null) {
            val firstEntity = contact.fixtureA.body.userData as Entity
            val secondEntity = contact.fixtureB.body.userData as Entity
            firstEntity.scripts.forEach {
                it.endContact(firstEntity, secondEntity, contact)
            }
            secondEntity.scripts.forEach {
                it.endContact(secondEntity, firstEntity, contact)
            }
        }
    }

    override fun preSolve(contact: Contact?, oldManifold: Manifold?) {
    }

    override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {
        if (contact != null && impulse != null) {
            val firstEntity = contact.fixtureA.body.userData as Entity
            val secondEntity = contact.fixtureB.body.userData as Entity
            firstEntity.scripts.forEach {
                it.postSolve(firstEntity, secondEntity, contact, impulse)
            }
            secondEntity.scripts.forEach {
                it.postSolve(secondEntity, firstEntity, contact, impulse)
            }
        }
    }
}