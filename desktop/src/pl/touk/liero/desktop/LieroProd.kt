package pl.touk.liero.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import pl.touk.liero.Ctx
import pl.touk.liero.GamePreferences
import pl.touk.liero.LieroGame
import pl.touk.liero.gdx.DeferredApplication

fun main() {
    val config = LwjglApplicationConfiguration()
    config.title = "Quak"
    LwjglApplication(DeferredApplication { LieroGame(Ctx(GamePreferences("liero"))) }, config)
}
