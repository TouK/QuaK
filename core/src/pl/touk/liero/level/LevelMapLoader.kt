package pl.touk.liero.level

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import ktx.box2d.body
import ktx.box2d.filter
import ktx.math.vec2
import pl.touk.liero.Ctx
import pl.touk.liero.entity.entity
import pl.touk.liero.game.cat_bulletBlue
import pl.touk.liero.game.cat_bulletRed
import pl.touk.liero.game.cat_ground
import pl.touk.liero.game.mask_ground
import pl.touk.liero.utils.get
import pl.touk.liero.utils.loadPixmapRgba8888
import kotlin.experimental.xor

class LevelMapLoader(val ctx: Ctx) {

    private val valueThreshold = 0.2f

    var width = 0f
    var height = 0f

    fun loadMap(mapName: String) {
        val bgPath = "level/$mapName/bg.png"
        val texPath = "level/$mapName/tex.png"
        val mapPath = "level/$mapName/map.png"
        val bgTex = Texture(bgPath)
        val texture = Texture(texPath)
        val map = loadPixmapRgba8888(mapPath) ?: throw IllegalArgumentException("File not found: $mapPath")
        val gridSizePx = 8
        val tileSizeMeters = 0.25f

        val tilesX = map.width / gridSizePx
        val tilesY = map.height / gridSizePx
        /*val worldWidth = (map.width / gridSizePx * tileSizeMeters).toInt()
        val worldHeight = (map.height / gridSizePx * tileSizeMeters).toInt()*/
        width = tilesX * tileSizeMeters
        height = tilesY * tileSizeMeters

        ctx.engine.entity {
            body(ctx.worldEngine.baseBody)
            texture(TextureRegion(bgTex), width, height, vec2(width / 2, height / 2))
        }

        val color = Color()

        for (wx in 0 until tilesX) {
            for (wy in 0 until tilesY) {

                val tx = wx * gridSizePx
                val ty = (tilesY - wy) * gridSizePx

                Color.rgba8888ToColor(color, map[tx, ty])

                var count = 0
                var redCount = 0
                var blueCount = 0
                for (x in tx until (tx + gridSizePx)) {
                    for (y in ty until (ty + gridSizePx)) {
                        Color.rgba8888ToColor(color, map[x, y])
                        if (color.r * color.a > valueThreshold || color.g * color.a > valueThreshold || color.b * color.a > valueThreshold)
                            count++
                        if (color.r > 0.5f)
                            redCount++
                        if (color.b > 0.5f)
                            blueCount++
                    }
                }
                val fillRatio = count.toFloat() / (gridSizePx * gridSizePx)
                val redFillRatio = redCount.toFloat() / (gridSizePx * gridSizePx)
                val blueFillRatio = blueCount.toFloat() / (gridSizePx * gridSizePx)

                if (fillRatio > 0.2f) {
                    // static
                    ctx.engine.entity {
                        body(ctx.world.body {
                            position.set((wx + 0.5f) * tileSizeMeters, (wy + 0.5f) * tileSizeMeters)
                            box(width = tileSizeMeters, height = tileSizeMeters) {
                                density = 1f
                                filter {
                                    if (blueFillRatio > 0.5f) {
                                        categoryBits = cat_ground
                                        maskBits = mask_ground.xor(cat_bulletRed).xor(cat_bulletBlue)
                                    } else {
                                        categoryBits = cat_ground
                                        maskBits = mask_ground
                                        maskBits = mask_ground
                                    }
                                }
                            }
                        })
                        texture(TextureRegion(texture, tx, ty, gridSizePx, gridSizePx), tileSizeMeters, tileSizeMeters)
                        //if (redFillRatio > 0.5f) {
                            energy(5f)
                        //}
                    }
                }
            }
        }
    }
}