package pl.touk.liero.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Pixmap

fun loadTxt(lines: List<String>, width: Int, height: Int) =
        IntArray2d(width, height, ' '.toInt()).also { map ->
            for (y in 0 until Math.min(map.h, lines.size)) {
                for (x in 0 until Math.min(map.w, lines[y].length)) {
                    map[x, y] = lines[y][x].toInt()
                }
            }
        }

fun loadPng(internalPath: String): IntArray2d? =
        loadPng(Gdx.files.internal(internalPath))

fun loadPng(fileHandle: FileHandle): IntArray2d? =
        loadPixmapRgba8888(fileHandle)?.let { pixmap ->
            IntArray2d(pixmap.width, pixmap.height).also {
                pixmap.forEach { x, y, color ->
                    it[x, y] = color
                }
                pixmap.dispose()
            }
        }

fun IntArray2d.toPixmap(): Pixmap {
    val pixmap = Pixmap(w, h, Pixmap.Format.RGBA8888)
    // pewnie da się jakoś optymalniej - tzn. przekazać ByteArray?
    forEach { x, y, value -> pixmap[x, y] = value }
    return pixmap
}

fun IntArray2d.savePng(file: FileHandle) {
    val pixmap = toPixmap()
    pixmap.savePng(file, false)
    pixmap.dispose()
}