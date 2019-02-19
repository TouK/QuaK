package pl.touk.liero.gdx

import com.badlogic.gdx.graphics.Color

// HSV = HSB (Hue Saturation Value, Hue Saturation Brightness)
fun hsv(hue: Float, saturation: Float, value: Float): Color {

    val h = (hue * 6).toInt()
    val f = hue * 6 - h
    val p = value * (1 - saturation)
    val q = value * (1 - f * saturation)
    val t = value * (1 - (1 - f) * saturation)

    when (h) {
        0 -> return Color(value, t, p, 1f)
        1 -> return Color(q, value, p, 1f)
        2 -> return Color(p, value, t, 1f)
        3 -> return Color(p, q, value, 1f)
        4 -> return Color(t, p, value, 1f)
        5 -> return Color(value, p, q, 1f)
        else -> throw RuntimeException("Something went wrong when converting from HSV to RGB. Input was $hue, $saturation, $value")
    }
}

fun Color.luminance() = (r + g + b) / 3f

fun Color.rgba() = (255 * r).toInt() shl 24 or ((255 * g).toInt() shl 16) or ((255 * b).toInt() shl 8) or (255 * a).toInt()