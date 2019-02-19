package pl.touk.liero.gdx

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Value
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.Layout

fun Stage.actor(block: Actor.() -> Unit): Actor {
    val actor = Actor()
    actor.block()
    addActor(actor)
    return actor
}

fun Stage.group(block: Group.() -> Unit): Group {
    val group = Group()
    group.block()
    addActor(group)
    return group
}

fun Actor.onTap(action: () -> Unit) {
    addListener(object : ActorGestureListener() {
        override fun tap(event: InputEvent, x: Float, y: Float, count: Int, button: Int) {
            action()
        }
    })
}

fun Actor.onFling(capture: Boolean = false, action: (event: InputEvent, vx: Float, vy: Float) -> Unit) {
    val listener = object : ActorGestureListener() {
        override fun fling(event: InputEvent, vx: Float, vy: Float, button: Int) {
            action(event, vx, vy)
        }
    }
    if(capture) {
        addCaptureListener(listener)
    } else {
        addListener(listener)
    }
}

/**
 * Ten Click Listener nie pozwoli na pączkowanie eventu do rodzica,
 * np.:
 * table - reaguje na tap event
 * button - reaguje na kliknięcie
 * Dzięki temu, kliknięcie przycisku nie wyzwoli tap event na tabeli
 *
 *  +-table-------+
 *  | +--------+  |
 *  | | button |  |
 *  | +--------+  |
 *  +-------------+
 *
 */
inline fun Actor.onClicked(crossinline action: () -> Unit) {
    addListener(object : ClickListener() {
        override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
            val res = super.touchDown(event, x, y, pointer, button)
            event.stop()
            return res
        }
        override fun clicked(event: InputEvent, x: Float, y: Float) {
            action()
        }
    })
}

/**
 * Jedyny sposób jaki znalazłem, żeby wymusić aktualizację
 * rozmiarów gdy używam Value.precentOf
 * <pre>
 * root.setSize(w, h)
 * root.invalidateAll()
 * root.layout()
 * </pre>
 */
fun Group.invalidateAll() {
    if (this is Layout)
        invalidate()
    for(c in children) {
        when(c) {
            is Group -> c.invalidateAll()
            is Layout -> c.invalidate()
        }
    }
}

fun screenWidth(ratio: Float = 1f): Value = object : Value() {
    override fun get(context: Actor?): Float = Gdx.graphics.width * ratio
}

fun screenHeight(ratio: Float = 1f): Value = object : Value() {
    override fun get(context: Actor?): Float = Gdx.graphics.height * ratio
}

fun shorterPercent(ratio: Float = 1f): Value = object : Value() {
    override fun get(context: Actor?): Float = Gdx.graphics.shorter() * ratio
}

fun Actor.setSize(w: Int, h: Int) = setSize(w.toFloat(), h.toFloat())