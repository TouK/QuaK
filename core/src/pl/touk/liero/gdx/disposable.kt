package pl.touk.liero.gdx

import com.badlogic.gdx.utils.Disposable

inline fun <T : Disposable> T.use(block: (T) -> Unit) {
    block(this)
    this.dispose()
}