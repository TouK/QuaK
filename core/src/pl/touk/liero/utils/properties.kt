package pl.touk.liero.utils

import com.badlogic.gdx.Gdx
import java.util.*

fun Properties.getInt(key: String, default: Int): Int {
    val value = this.getProperty(key)
    if (value != null) {
        try {
            return Integer.parseInt(value)
        } catch (e: NumberFormatException) {
        }
    }
    return default
}

fun loadProperties(internalPath: String) =
        Properties().also { props ->
            val file = Gdx.files.internal(internalPath)
            if (file != null && file.exists()) {
                file.reader(1024).use { reader ->
                    props.load(reader)
                }
            }
        }
