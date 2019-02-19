package pl.touk.liero.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.PixmapIO
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap

/**
 * Wczytaj png do Pixmap'y (bufor w natywnej pamięci)
 */
fun loadPixmapRgba8888(internalPath: String) =
        loadPixmapRgba8888(Gdx.files.internal(internalPath))

fun loadPixmapRgba8888(file: FileHandle) =
    file.takeIf {it.exists()}?.let {
        val input = it.read()
        val pixmap = Gdx2DPixmap(input, Gdx2DPixmap.GDX2D_FORMAT_RGBA8888)
        input.close()
        Pixmap(pixmap)
    }

/**
 * Nie działa jak powinno, tzn. podaję jako oczekiwany format RGB888, dostaję pixmap'ę z formatem RGB888,
 * ale ewidentnie format jest RGBA (tzn. jest też kanał alpha), zawsze wartość 0xff
 * Nie używać - do sprawdzenia
 */
fun loadPixmapRgb888(internalPath: String) =
    Gdx.files.internal(internalPath)?.let {
        val input = it.read()
        val pixmap = Gdx2DPixmap(input, Gdx2DPixmap.GDX2D_FORMAT_RGB888)
        input.close()
        Pixmap(pixmap)
    }

fun Pixmap.savePng(file: FileHandle, flipY: Boolean = false): Pixmap {
    val png = PixmapIO.PNG(width * height * 2)
    png.setFlipY(flipY)
    png.write(file, this)
    png.dispose()
    return this
}

operator fun Pixmap.get(x: Int, y: Int) = this.getPixel(x, y)
operator fun Pixmap.set(x: Int, y: Int, color: Int) = this.drawPixel(x, y, color)
operator fun Pixmap.set(x: Int, y: Int, color: UInt) = this.drawPixel(x, y, color.toInt())

/**
 * For each x, y in row order
 */
fun Pixmap.forEach(action: (x: Int, y: Int, color: Int) -> Unit) {
    for (y in 0 until height) {
        for (x in 0 until width) {
            action(x, y, this[x, y])
        }
    }
}

fun Pixmap.subPixmap(x: Int, y: Int, xend: Int, yend: Int): Pixmap {
    val w = xend - x
    val h = yend - y
    val pixmap = Pixmap(w, h, this.format)
    pixmap.drawPixmap(this, 0, 0, x, y, w, h)
    return pixmap
}