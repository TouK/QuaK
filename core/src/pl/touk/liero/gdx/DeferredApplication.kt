package pl.touk.liero.gdx

import com.badlogic.gdx.ApplicationListener

/**
 * Chodzi tylko o to, żeby opóźnić tworzenie docelowego ApplicationListener'a do metody create (kiedy już cały kontekst Gdx istnieje)
 * Dzięki temu możemy inicjalizować aplikację w konstruktorze, a nie w create (w Kotlinie jest to szczególnie porządane)
 */
class DeferredApplication(val builder: () -> ApplicationListener) : ApplicationListener {
    var app: ApplicationListener? = null

    override fun create() {
        app = builder()
        app!!.create()
    }

    override fun render() = app!!.render()
    override fun resize(width: Int, height: Int) = app!!.resize(width, height)
    override fun pause() = app!!.pause()
    override fun resume() = app!!.resume()
    override fun dispose() = app!!.dispose()
}