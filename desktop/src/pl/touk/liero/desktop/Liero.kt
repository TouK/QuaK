package pl.touk.liero.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import pl.touk.liero.Ctx
import pl.touk.liero.GamePreferences
import pl.touk.liero.LieroGame
import pl.touk.liero.common.texturePacker
import pl.touk.liero.gdx.DeferredApplication

fun main(arg: Array<String>) {
    texturePacker(arg, "../../graphics/game", "./", "game")
    texturePacker(arg, "../../graphics/menu", "./", "menu")

    val config = LwjglApplicationConfiguration()
    LwjglApplication(DeferredApplication { LieroGame(Ctx(GamePreferences("liero"))) }, config)
}
