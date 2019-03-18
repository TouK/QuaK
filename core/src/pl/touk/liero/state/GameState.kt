package pl.touk.liero.state

import pl.touk.liero.Ctx
import pl.touk.liero.scene.GameScene
import pl.touk.liero.scene.WorldScene
import pl.touk.liero.screen.GameScreen
import pl.touk.liero.screen.PauseScreen
import pl.touk.liero.screen.UiScreen
import pl.touk.liero.system.MusicSystem

class GameState(ctx: Ctx) : State {
    override val scene: WorldScene = GameScene(ctx)
    override val screen: UiScreen = GameScreen(ctx)
    override val musicTrack: MusicSystem.MusicTrack = MusicSystem.MusicTrack.Battle
    val pauseScreen: UiScreen = PauseScreen(ctx)
}