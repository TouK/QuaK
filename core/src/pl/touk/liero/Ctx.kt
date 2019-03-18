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
import pl.touk.liero.system.*

open class Ctx(val prefs: GamePreferences) {

    // params
    val params = Params()

    // input
    val mux = InputMultiplexer()

    // cameras, viewports
    val hudCamera = OrthographicCamera()
    val worldCamera = OrthographicCamera()
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
    val leftPlayerControl = PlayerButtonControl()
    var rightPlayerControl: PlayerControl

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
        val rightController = if (controllers.isNotEmpty()) {
            rightPlayerControl = PlayerControlSmooth()
            JoystickInputSystem(rightPlayerControl,
                    jump = Xbox.A,
                    fire = Xbox.X,
                    controller = Controllers.getControllers().first())
        } else {
            rightPlayerControl = PlayerButtonControl()
            InputSystem(rightPlayerControl,
                    left = Input.Keys.LEFT,
                    right = Input.Keys.RIGHT,
                    up = Input.Keys.UP,
                    down = Input.Keys.DOWN,
                    jump = Input.Keys.CONTROL_RIGHT,
                    fire = Input.Keys.SHIFT_RIGHT)
        }

        engine.add(
                WorldSystem(world, worldEngine, GlobalParams.fixed_time_step),
                InputSystem(leftPlayerControl,
                        left = Input.Keys.A,
                        right = Input.Keys.D,
                        up = Input.Keys.W,
                        down = Input.Keys.S,
                        jump = Input.Keys.CONTROL_LEFT,
                        fire = Input.Keys.SHIFT_LEFT),
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
    }

    fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
        cameraScript.resize(width.toFloat(), height.toFloat())
    }
}

class TextureAtlasWrapper(s: String) : TextureAtlas(s) {
    override fun findRegion(name: String?): AtlasRegion {
        return super.findRegion(name) ?: super.findRegion("default_error")
    }
}
