package pl.touk.liero.gdx

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.Viewport

inline fun SpriteBatch.render(camera: Camera, color: Color, body: SpriteBatch.() -> Unit) {
    this.projectionMatrix.set(camera.combined)
    this.begin()
    this.setColor(color)
    body()
    this.end()
}

inline fun SpriteBatch.render(viewport: Viewport, color: Color, body: SpriteBatch.() -> Unit) = this.render(viewport.camera, color, body)

inline fun ShapeRenderer.render(type : ShapeRenderer.ShapeType, camera: Camera, color: Color, body: ShapeRenderer.() -> Unit) {
    this.projectionMatrix.set(camera.combined)
    this.begin(type)
    this.setColor(color)
    body()
    this.end()
}

inline fun ShapeRenderer.render(type: ShapeRenderer.ShapeType, viewport: Viewport, color: Color, body: ShapeRenderer.() -> Unit) =
        this.render(type, viewport.camera, color, body)

inline fun ShapeRenderer.render(type : ShapeRenderer.ShapeType, camera: Camera, body: ShapeRenderer.() -> Unit) {
    this.projectionMatrix.set(camera.combined)
    this.begin(type)
    body()
    this.end()
}

inline fun ShapeRenderer.render(type: ShapeRenderer.ShapeType, viewport: Viewport, body: ShapeRenderer.() -> Unit) =
        this.render(type, viewport.camera, body)

inline fun FrameBuffer.render(body: FrameBuffer.() -> Unit) {
    this.begin()
    body()
    this.end()
}