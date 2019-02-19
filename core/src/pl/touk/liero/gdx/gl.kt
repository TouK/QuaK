package pl.touk.liero.gdx

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20

fun glClear(c: Color) {
    Gdx.gl.glClearColor(c.r, c.g, c.b, c.a)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
}