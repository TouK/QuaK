package pl.touk.liero.script

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import pl.touk.liero.ecs.Entity

interface Script {
    fun update(me: Entity, timeStepSec: Float) {}
    fun beginContact(me: Entity, other: Entity, contact: Contact) {}
    fun endContact(me: Entity, other: Entity, contact: Contact) {}
    fun postSolve(me: Entity, other: Entity, contact: Contact, impulse: ContactImpulse) {}
    fun debugDraw(me: Entity, renderer: ShapeRenderer) {}
    fun beforeDestroy(me: Entity) {}
}

fun script(updateFun: (me: Entity, timeStepSec: Float) -> Unit) = object : Script {
    override fun update(me: Entity, timeStepSec: Float) {
        updateFun(me, timeStepSec)
    }
}

fun script(updateFun: (timeStepSec: Float) -> Unit) = object : Script {
    override fun update(me: Entity, timeStepSec: Float) {
        updateFun(timeStepSec)
    }
}