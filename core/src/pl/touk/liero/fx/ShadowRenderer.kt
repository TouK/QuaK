package pl.touk.liero.fx

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888
import com.badlogic.gdx.graphics.Texture.TextureFilter.Nearest
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.math.Vector2
import pl.touk.liero.gdx.glClear

/**
 * Używamy tego tak:
 * <pre>
 *     shadowRenderer.begin()
 *     // tutaj rysujemy
 *     // ważne! wyłączyć blending, bo w tle mamy zero alpha
 *     shadowRenderer.end()
 * </pre>
 */
class ShadowRenderer(val batch: SpriteBatch,
                     var width: Int = Gdx.graphics.width,
                     var height: Int = Gdx.graphics.height) {

    lateinit var frameBuffer: FrameBuffer
    lateinit var frameBufferTexture: TextureRegion

    val offset = Vector2(5f / 360f * Gdx.graphics.width, -5f / 360f * Gdx.graphics.width)

    init {
        createFrameBuffer()
    }

    fun begin() {
        frameBuffer.begin()
        glClear(Color(0x00000000))
    }

    fun end() {
        frameBuffer.end()
    }

    fun draw() {
        batch.begin()
        batch.setColor(0f, 0f, 0f, 0.2f)
        batch.draw(frameBufferTexture, offset.x, offset.y, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        batch.setColor(1f, 1f, 1f, 1f)
        batch.draw(frameBufferTexture, 0f, 0f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        batch.end()
    }

    fun resize(width: Int, height: Int) {
        this.width = width
        this.height = height
        dispose()
        createFrameBuffer()
    }

    fun dispose() {
        frameBuffer.dispose()
    }

    private fun createFrameBuffer() {
        frameBuffer = FrameBuffer(RGBA8888, width, height, false)
        frameBufferTexture = TextureRegion(frameBuffer.getColorBufferTexture())
        frameBufferTexture.texture.setFilter(Nearest, Nearest)
        frameBufferTexture.flip(false, true)
    }
}