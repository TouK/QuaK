package pl.touk.liero;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import pl.touk.liero.level.LevelParams;

import static pl.touk.liero.gdx.Scene2dKt.screenWidth;
import static pl.touk.liero.gdx.Scene2dKt.shorterPercent;

public class Params extends LevelParams {
    public float playerScale = 1f;

    public Color colorHud = new Color(1f, 1f, 1f, 0.4f);
    public Color colorLevelOverlay = new Color(0f, 0f, 0f, 0.9f);

    public Value pad = shorterPercent(0.05f);
    public Value buttonSize = shorterPercent(0.25f);
    public Value smallButtonSize = shorterPercent(0.2f);
    public Value logoWidth = shorterPercent(0.8f);
}
