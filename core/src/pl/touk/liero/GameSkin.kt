package pl.touk.liero

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import ktx.scene2d.Scene2DSkin
import ktx.style.button
import ktx.style.label
import ktx.style.textButton

fun createSkin(smallFont: BitmapFont, font: BitmapFont, vararg atlases: TextureAtlas): Skin {
    val skin = ktx.style.skin {
        for (atlas in atlases) {
            addRegions(atlas)
        }
        add("small-font", smallFont)
        add("default-font", font)

        val skin = this
        val upColor = GlobalParams.buttonUpColor
        val downColor = GlobalParams.buttonDownColor
        val disabledColor = Color(0x00000000)

        textButton {
            this.font = smallFont
            fontColor = upColor
            downFontColor = downColor
        }
        label("default") {
            this.font = font
        }
        label("small") {
            this.font = smallFont
        }
        button("back") {
            up = skin.newDrawable("left-arrow", upColor)
            down = skin.newDrawable("left-arrow", downColor)
        }
        button("play") {
            up = skin.newDrawable("play", upColor)
            down = skin.newDrawable("play", downColor)
            disabled = skin.newDrawable("play", disabledColor)
        }
        button("pause") {
            up = skin.newDrawable("pause", upColor)
            down = skin.newDrawable("pause", downColor)
        }
        button("music") {
            up = skin.newDrawable("music-off", upColor)
            down = skin.newDrawable("music-on", downColor)
            checked = skin.newDrawable("music-on", upColor)
        }
        button("sound") {
            up = skin.newDrawable("sound-off", upColor)
            down = skin.newDrawable("sound-on", downColor)
            checked = skin.newDrawable("sound-on", upColor)
        }
    }

    Scene2DSkin.defaultSkin = skin

    return skin
}