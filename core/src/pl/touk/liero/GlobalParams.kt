package pl.touk.liero

import com.badlogic.gdx.graphics.Color

object GlobalParams {
    // Na potrzeby TEST LOOP, żeby scenariusz był powtarzalny
    var fixed_time_step: Float? = null
    val buttonUpColor = Color(0xffffffaf.toInt())
    val buttonDownColor = Color(0xffffffff.toInt())
    val missingStarColor = Color(0xffffff4f.toInt())
}