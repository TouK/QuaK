package pl.touk.liero

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import pl.touk.liero.scene.EmptyScene
import pl.touk.liero.scene.WorldScene
import pl.touk.liero.gdx.glClear
import pl.touk.liero.scene.GameScene
import pl.touk.liero.screen.*

open class LieroGame constructor(val ctx: Ctx): Game() {

    private val menuScreen = MenuScreen(ctx)
    private val levelScreen = LevelScreen(ctx)
    private val gameScreen = GameScreen(ctx)
    private val pauseScreen = PauseScreen(ctx)

    private val emptyScene = EmptyScene()
    private val gameScene = GameScene(ctx)

    private var scene: WorldScene = emptyScene

    override fun create() {
        ctx.mux.addProcessor(ctx.stage)
        Gdx.input.inputProcessor = ctx.mux
        ctx.music.fadeIn()

        setScreen(menuScreen)
    }

    override fun render() {
        glClear(Color.BLACK)

        val deltaSec = Math.min(Gdx.graphics.rawDeltaTime, 0.033333f)
        scene.update(deltaSec)
        screen.render(deltaSec)
        ctx.music.update(Gdx.graphics.deltaTime)
        ctx.uiEvents.handle(this::handleUiEvent)
    }

    internal open fun handleUiEvent(event: UiEvent) {
        when (screen) {
            menuScreen -> when (event) {
                UiEvent.Play -> {
                    setScreen(levelScreen)
                    ctx.music.fadeOut()
                }
                UiEvent.Back -> {
                    Gdx.app.exit()
                }
                else -> {}
            }
            levelScreen -> when(event) {
                UiEvent.Back -> {
                    setScreen(menuScreen)
                }
                UiEvent.Play -> {
                    setScene(gameScene)
                    setScreen(gameScreen)
                }
                else -> {}
            }
            gameScreen -> when(event) {
                UiEvent.Back -> {
                    setScene(emptyScene)
                    setScreen(levelScreen)
                }
                UiEvent.Pause -> {
                    setScreen(pauseScreen)
                }
                else -> {}
            }
            pauseScreen -> when(event) {
                UiEvent.Back -> {
                    setScreen(levelScreen)
                    ctx.music.fadeIn()
                }
                UiEvent.Play -> {
                    setScreen(gameScreen)
                }
                else -> {}
            }
        }
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        ctx.resize(width, height)
    }

    override fun dispose() {
        ctx.dispose()
    }

    private fun setScene(scene: WorldScene) {
        this.scene.destroy()
        this.scene = scene
        this.scene.create()
    }
}