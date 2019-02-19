package pl.touk.liero.gdx

import com.badlogic.gdx.Gdx

fun Int.pressed(): Boolean {
    return Gdx.input.isKeyPressed(this)
}

inline fun Int.ifPressed(block: () -> Unit) {
    if(Gdx.input.isKeyPressed(this)) {
        block()
    }
}

fun Int.justPressed(): Boolean {
    return Gdx.input.isKeyJustPressed(this)
}

inline fun Int.ifJustPressed(block: () -> Unit) {
    if(Gdx.input.isKeyJustPressed(this)) {
        block()
    }
}