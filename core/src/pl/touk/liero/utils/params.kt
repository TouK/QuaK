package pl.touk.liero.utils

import com.badlogic.gdx.graphics.Color
import java.util.*

/*

Przydatne funkcje do globalnych parametrów (params).
Pomysł jest taki, że najpierw tworzymy obiekt z wartościami domyślnymi,
a później nadpisujemy wartościami specyficznymi dla level'u, poziomu trudności itp.
Można też nadpisywać w czasie gry (np. cyklicznie z pliku)

class Params {
    @JvmField var speed = 1f
    @JvmField var debug = "yes"
    @JvmField var color = Color.WHITE.cpy()
}
val props = with(Properties()) {
    load(FileReader("params"))
    this
}
val params = Params()
        .overwrite(props)
        .overwrite(mapOf("speed" to "2.4"))
 */

internal val truthy = setOf("true", "yes", "on")

/**
 * Nadpisuje pola w obiekcie wartościami z properties.
 * <p> Dostaje się bezpośrednio do pola za pomocą refleksji, dlatego dwa wyjścia:
 * <pre>
 * public class ParamsJava {
 *     public float speed = 1f
 * }
 * class ParamsKotlin {
 *     @JvmField var speed = 1f
 * }
 * </pre>
 */
fun Any.overwrite(props: Properties) {
    this.overwrite(props.mapKeys { e -> e.key.toString() }.mapValues { e -> e.value.toString() })
}

/**
 * Nadpisuje pola w obiekcie wartościami z mapy
 */
fun Any.overwrite(props: Map<String, String>) {
    val clazz = this.javaClass
    for (key in props.keys) {
        props[key]?.let { value ->
            val field = clazz.getField(key)
                    ?: throw IllegalArgumentException("Field not found: $key, target class: $clazz")
            when (field.type) {
                Color::class.java -> (field.get(this) as Color).set(Color.valueOf(value))
                String::class.java -> field.set(this, value)
                Int::class.java -> field.setInt(this, Integer.parseInt(value))
                Float::class.java -> field.setFloat(this, java.lang.Float.parseFloat(value))
                Boolean::class.java -> field.setBoolean(this, truthy.contains(value.toLowerCase()))
                else -> throw IllegalArgumentException("Unsupported type: " + field.type)
            }
        }
    }
}

/**
 * Nadpisuje pola w obiekcie wartościami z drugiego obiektu
 */
fun Any.overwrite(source: Any) {
    val clazz = this.javaClass
    val sourceClass = source.javaClass
    for (key in sourceClass.declaredFields) {
        val value = key.get(source)
        val field = clazz.getField(key.name)
                ?: throw IllegalArgumentException("Field not found: $key, source class: $source, target class: $clazz")
        when (value) {
            is Color -> (field.get(this) as Color).set(value)
            else -> field.set(this, value)
        }
    }
}