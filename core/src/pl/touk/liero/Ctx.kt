package pl.touk.liero

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.controllers.Controllers
import com.badlogic.gdx.controllers.mappings.Xbox
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.viewport.ScreenViewport
import ktx.collections.isNotEmpty
import pl.touk.liero.ecs.Actions
import pl.touk.liero.ecs.Engine
import pl.touk.liero.ecs.Entity
import pl.touk.liero.game.PlayerButtonControl
import pl.touk.liero.game.PlayerControl
import pl.touk.liero.game.PlayerControlSmooth
import pl.touk.liero.game.WorldEngine
import pl.touk.liero.gdx.shorter
import pl.touk.liero.level.Level
import pl.touk.liero.level.LevelLoader
import pl.touk.liero.screen.UiEvent
import pl.touk.liero.script.CameraScript
import pl.touk.liero.script.Frags
import pl.touk.liero.system.*
import java.util.*

open class Ctx(val prefs: GamePreferences) {

    // params
    val params = Params()

    // input
    val mux = InputMultiplexer()

    // cameras, viewports
    val hudCamera = OrthographicCamera()
    val worldCamera = ShakyCamera()
    val viewport = ScreenViewport(hudCamera)

    // rendering
    val batch = SpriteBatch()
    val debugRenderer = Box2DDebugRenderer()

    // hud
    val stage = Stage(viewport, batch)
    val uiEvents = UiEventQueue<UiEvent>()

    // assets
    val assetManager = AssetManager()
    val font: BitmapFont
    val smallFont: BitmapFont
    val gameAtlas: TextureAtlas
    val menuAtlas: TextureAtlas
    val skin: Skin

    // music, sounds
    val music = MusicSystem(prefs.music)
    val sound = SoundSystem(prefs.sound)

    // app state
    var currentLevel = 1
    val levelLoader = LevelLoader()

    // game
    val worldEngine = WorldEngine()
    val engine: Engine<Entity>
        get() = worldEngine.engine
    val world: World
        get() = worldEngine.world
    val actions: Actions
        get() = worldEngine.actions

    lateinit var level: Level
    var cameraScript = CameraScript(worldCamera, 9f, 16f)
    val leftPlayerControl: PlayerControl
    var rightPlayerControl: PlayerControl
    var leftFrags = Frags()
    var rightFrags = Frags()


    init {
        val resolver = InternalFileHandleResolver()
        assetManager.setLoader(FreeTypeFontGenerator::class.java, FreeTypeFontGeneratorLoader(resolver))
        assetManager.setLoader(BitmapFont::class.java, ".ttf", FreetypeFontLoader(resolver))

        assetManager.load("font.ttf", BitmapFont::class.java,
                FreetypeFontLoader.FreeTypeFontLoaderParameter().also {
                    it.fontFileName = "fonts/century-gothic.ttf"
                    it.fontParameters.size = Gdx.graphics.shorter() / 10
                })
        assetManager.load("small-font.ttf", BitmapFont::class.java,
                FreetypeFontLoader.FreeTypeFontLoaderParameter().also {
                    it.fontFileName = "fonts/century-gothic.ttf"
                    it.fontParameters.size = Gdx.graphics.shorter() / 16
                })
        assetManager.load("game.atlas", TextureAtlas::class.java)
        assetManager.load("menu.atlas", TextureAtlas::class.java)

        assetManager.finishLoading()

        font = assetManager.get("font.ttf")
        smallFont = assetManager.get("small-font.ttf")
        gameAtlas = TextureAtlasWrapper("game.atlas")
        menuAtlas = assetManager.get("menu.atlas")
        skin = createSkin(smallFont, font, gameAtlas, menuAtlas)

        val controllers = Controllers.getControllers()
        val psController = if (controllers.isNotEmpty()) controllers.find { it.name.contains("PLAYSTATION") } else null
        val otherController = if (controllers.isNotEmpty()) controllers.find { !it.name.contains("PLAYSTATION") } else null

        val leftController = if (psController != null) {
            leftPlayerControl = PlayerControlSmooth()
            JoystickInputSystem(leftPlayerControl,
                    jump = Xbox.A,
                    fire = Xbox.Y,
                    changeWeapon = Xbox.R_BUMPER,
                    changeWeaponBackwards = Xbox.L_BUMPER,
                    controller = psController)
        } else {
            leftPlayerControl = PlayerButtonControl()
            InputSystem(leftPlayerControl,
                    left = Input.Keys.A,
                    right = Input.Keys.D,
                    up = Input.Keys.W,
                    down = Input.Keys.S,
                    jump = Input.Keys.CONTROL_LEFT,
                    fire = Input.Keys.ALT_LEFT,
                    changeWeaponBackwards = Input.Keys.Z,
                    changeWeapon = Input.Keys.X)
        }
        val rightController = if (otherController != null) {
            rightPlayerControl = PlayerControlSmooth()
            JoystickInputSystem(rightPlayerControl,
                    jump = Xbox.A,
                    fire = Xbox.X,
                    changeWeapon = Xbox.R_BUMPER,
                    changeWeaponBackwards = Xbox.L_BUMPER,
                    controller = otherController)
        } else {
            rightPlayerControl = PlayerButtonControl()
            InputSystem(rightPlayerControl,
                    left = Input.Keys.LEFT,
                    right = Input.Keys.RIGHT,
                    up = Input.Keys.UP,
                    down = Input.Keys.DOWN,
                    jump = Input.Keys.CONTROL_RIGHT,
                    fire = Input.Keys.ALT_RIGHT,
                    changeWeapon = Input.Keys.COMMA,
                    changeWeaponBackwards = Input.Keys.COLON)
        }

        engine.add(
                WorldSystem(world, worldEngine, GlobalParams.fixed_time_step),
                leftController,
                rightController,
                ScriptUpdateSystem(engine),
                ActionsSystem(worldEngine, actions),
                SpriteRenderSystem(engine, batch, worldCamera),
                WorldRenderSystem(debugRenderer, world, worldCamera),
                TextSystem(engine, batch, worldCamera, hudCamera),
                EnergySystem(engine),
                LifeSpanSystem(engine, worldEngine),
                ScriptBeforeDestroySystem(engine),
                ParentChildSystem(engine),
                BodyDisposeSystem(engine))
    }

    fun dispose() {
        batch.dispose()
        skin.dispose()
        stage.dispose()
        music.dispose()
        sound.dispose()
        worldEngine.dispose()
    }

    fun clearWorld() {
        worldEngine.clear()
        leftFrags = Frags()
        rightFrags = Frags()
    }

    fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
        cameraScript.resize(width.toFloat(), height.toFloat())
    }
}

class ShakyCamera: OrthographicCamera() {

    private lateinit var samples: FloatArray

    private var timer = 0f
    private var duration = 0f

    private var amplitude = 0
    private var frequency = 0
    private var isFading = true

    private var shake = false

    private fun ClosedRange<Int>.random() = Random().nextInt((endInclusive + 1) - start) +  start

    fun shake(time: Float = 1f, amp: Int = 1, freq: Int = 15, fade: Boolean = true) {
        shake = true
        timer = 0f
        duration = time
        amplitude = amp
        frequency = freq
        isFading = fade
        samples = FloatArray(frequency)
        for (i in 0 until frequency) {
//            samples[i] = Random().nextFloat() * 2f - 1f
            samples[i] = (-1..1).random().toFloat() // only 3 variants (-1, 0, 1) and same visible effect as function above, lol
        }
    }

    override fun update() {
        if (shake) {
            if (timer > duration) shake = false
            val dt = Gdx.graphics.deltaTime
            timer += dt
            if (duration > 0) {
                duration -= dt
                val shakeTime = timer * frequency
                val first = shakeTime.toInt()
                val second = (first + 1) % frequency
                val third = (first + 2) % frequency
                val deltaT = shakeTime - shakeTime.toInt()
                val deltaX = samples[first] * deltaT + samples[second] * (1f - deltaT)
                val deltaY = samples[second] * deltaT + samples[third] * (1f - deltaT)

                position.x += deltaX * amplitude * if (isFading) Math.min(duration, 1f) else 1f
                position.y += deltaY * amplitude * if (isFading) Math.min(duration, 1f) else 1f
            }
        }
        super.update()
    }
}

class TextureAtlasWrapper(s: String) : TextureAtlas(s) {
    override fun findRegion(name: String?): AtlasRegion {
        return super.findRegion(name) ?: super.findRegion("default_error")
    }
}
