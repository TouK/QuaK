package pl.touk.liero.common

import com.badlogic.gdx.graphics.Texture.TextureFilter.Linear
import com.badlogic.gdx.graphics.Texture.TextureFilter.MipMapLinearNearest
import com.badlogic.gdx.tools.texturepacker.TexturePacker
import java.io.File

private fun doPack(args: Array<String>, sourceDir: File, targetDir: File, atlasFileName: String): Boolean {
    if(args.contains("-f")) {
        println("force = true (flaga '-f'), pakujemy")
        return true
    }
    val target = File(targetDir, atlasFileName + ".atlas")
    if (!target.exists()) {
        println("Nie ma pliku '" + target.name + "', pakujemy")
        return true
    }

    if (sourceDir.listFiles().isEmpty()) {
        println("Katalog " + sourceDir.path + " jest pusty, pomijam")
        return false
    }

    if (sourceDir.lastModified() > target.lastModified()) {
        println("Tekstury są nowsze od atlasu, pakujemy")
        return true
    } else {
        println("Tekstury są starsze od atlasu, pomijam")
        return false
    }
}

fun texturePacker(args: Array<String>, sourceDir: String, targetDir: String, atlasFileName: String) {
    if (doPack(args, File(sourceDir), File(targetDir), atlasFileName)) {
        val settings = TexturePacker.Settings()
        settings.maxWidth = 2048
        settings.maxHeight = 2048
        settings.filterMag = Linear
        // Aka. GL_LINEAR_MIPMAP_NEAREST - Chooses the mipmap that most closely matches the size of the pixel being
        // textured and uses the GL_LINEAR criterion (a weighted average of the four texture elements that are closest
        // to the center of the pixel) to produce a texture value.
        settings.filterMin = MipMapLinearNearest
        settings.paddingX = 4
        settings.paddingY = 4
        TexturePacker.process(settings, sourceDir, targetDir, atlasFileName)
    }
}