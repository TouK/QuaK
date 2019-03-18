package pl.touk.liero;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import org.jetbrains.annotations.NotNull;
import pl.touk.liero.level.LevelParams;

import static pl.touk.liero.gdx.Scene2dKt.screenWidth;
import static pl.touk.liero.gdx.Scene2dKt.shorterPercent;

public class Params extends LevelParams {
    public float playerSize = 1f;
    public float playerSpeed = 10f;
    public float playerTotalHealth = 100f;


    //kaczkozooka
    public final float bazookasSize = 0.1f;
    public final float bazookaDamage = 10;
    public final float bazookaRadius = 1f;
    public final float bazookasSpeed = 1f;
    public final float bazookaCooldown = 2f;
    public final int bazookaAmmo = 5;

    //kaczkosznikov
    public final int kaczkosznikovAmmo = 40;
    public final float kaczkosznikovDamage = 2f;
    public final float kaczkosznikovSpeed = 7f;
    public final float kaczkosznikovSize = 0.2f;
    public final float kaczkosznikovCooldown = 0.25f;

    //kaczkomiecz
    public final float swordRange = 2f;
    public final float swordDamage = 10;
    public final float swordCooldown = 0.75f;
    public final float swordLifeSpan = 1f;
    
    //kaczkospluwa 
    public final float gunSize = 0.3f;
    public final float gunDamage = 5f;
    public final float gunSpeed = 7f;
    public final float gunCooldown = 1f;
    public final int gunAmmo = 6;

    //minigun
    public final float miniGunSize = 0.05f;
    public final float miniGunDamage = 0.5f;
    public final float miniGunSpeed = 10f;
    public final float miniGunCooldown = 0.05f;
    public final int miniGunAmmo = 200;

    public Color colorHud = new Color(1f, 1f, 1f, 0.4f);
    public Color colorLevelOverlay = new Color(0f, 0f, 0f, 0.9f);

    public Value pad = shorterPercent(0.05f);
    public Value buttonSize = shorterPercent(0.25f);
    public Value smallButtonSize = shorterPercent(0.2f);
    public Value logoWidth = shorterPercent(0.8f);

}
