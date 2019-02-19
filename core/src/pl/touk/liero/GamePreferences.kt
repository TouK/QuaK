package pl.touk.liero

import com.badlogic.gdx.Gdx

class GamePreferences(preferencesName: String) {
    private val prefs = Gdx.app.getPreferences(preferencesName)

    var sound: Boolean
        get() = prefs.getBoolean("sound", true)
        set(value) {
            prefs.putBoolean("sound", value)
            prefs.flush()
        }

    var music: Boolean
        get() = prefs.getBoolean("music", true)
        set(value) {
            prefs.putBoolean("music", value)
            prefs.flush()
        }
}