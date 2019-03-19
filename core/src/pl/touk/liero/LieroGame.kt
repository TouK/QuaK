package pl.touk.liero

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import pl.touk.liero.gdx.glClear
import pl.touk.liero.scene.EmptyScene
import pl.touk.liero.scene.WorldScene
import pl.touk.liero.screen.UiEvent
import pl.touk.liero.state.GameState
import pl.touk.liero.state.LevelSelectionState
import pl.touk.liero.state.MenuState
import pl.touk.liero.state.State

open class LieroGame constructor(val ctx: Ctx): Game() {

    private val menuState: State = MenuState(ctx)
    private val levelSelectionState: State = LevelSelectionState(ctx)
    private val gameState: GameState = GameState(ctx)

    private var scene: WorldScene = EmptyScene()

    override fun create() {
        ctx.mux.addProcessor(ctx.stage)
        Gdx.input.inputProcessor = ctx.mux
        ctx.music.fadeIn()

        setState(menuState)
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
            menuState.screen -> when (event) {
                UiEvent.Play -> {
                    setState(gameState)
                }
                UiEvent.Back -> {
                    Gdx.app.exit()
                }
                else -> {}
            }
            gameState.screen -> when(event) {
                UiEvent.Back -> {
                    setState(menuState)
                }
                UiEvent.Pause -> {
                    setScreen(gameState.pauseScreen)
                    ctx.music.fadeOut()
                }
                else -> {}
            }
            gameState.pauseScreen -> when(event) {
                UiEvent.Back -> {
                    setState(menuState)
                }
                UiEvent.Play -> {
                    setScreen(gameState.screen)
                    ctx.music.fadeIn()
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

    private fun setState(state: State) {
        setScene(state.scene)
        setScreen(state.screen)
        ctx.music.playTrack(state.musicTrack())
    }
}