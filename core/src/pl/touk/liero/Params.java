package pl.touk.liero;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import pl.touk.liero.level.LevelParams;

import static pl.touk.liero.gdx.Scene2dKt.shorterPercent;

public class Params extends LevelParams {
    public float playerSize = 0.95f;
    public float playerSpeed = 10f;
    public float playerJumpSpeed = 22f;
    public float playerTotalHealth = 100f;
    public float playerGravityScale = 10f;
    public float playerGravityScaleInAir = 3f;
    public float playerPidProportional = 10f;
    public float playerMaxForce = 120f;
    public Vector2 playerAnchor = new Vector2(0f, 0f);
    public float idleVelocityLimit = 5f;
    public float hurtAnimationTime = 1.5f;

    public Vector2 weaponAnchor = new Vector2(0f, 0f);
    public float weaponLowerAngle = -MathUtils.PI * 0.25f;
    public float weaponUpperAngle = MathUtils.PI * 0.25f;
    public float weaponRotationSpeed = 3f;
    public float weaponAngularDamping = 10f;
    public float weaponBodyWidth = playerSize * 0.9f;
    public float weaponBodyHeight = playerSize * 0.4f;

    // blood
    public final float mediumBloodThreshold = 0.65f;
    public final float largeBloodThreshold = 0.25f;
    public final float bloodSpeed = 10f;
    public final float bloodSize = 0.2f;
    public final float bloodLifeSpan = 5000f;
    public final float bloodCooldown = 0.5f;

    //kaczkozooka
    public final float bazookasSize = 0.25f;
    public final float bazookaDirectDamage = 5;
    public final float bazookaExplosionDamage = 20;
    public final float bazookaRadius = 1f;
    public final float bazookasSpeed = 12f;
    public final float bazookaCooldown = 2f;
    public final int bazookaAmmo = 5;

    //fragzooka
    public final float fragzookasSize = 0.35f;
    public final float fragzookaDamage = 10;
    public final float fragzookaRadius = 1f;
    public final float fragzookasSpeed = 2f;
    public final float fragzookaCooldown = 2f;

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
    public final float gunSize = 0.5f;
    public final float gunDamage = 12f;
    public final float gunSpeed = 30f;
    public final float gunCooldown = 0.75f;
    public final int gunAmmo = 6;

    //minigun
    public final float miniGunSize = 0.05f;
    public final float miniGunDamage = 1f;
    public final float miniGunSpeed = 15f;
    public final float miniGunCooldown = 0.05f;
    public final float miniGunOverheat = 5f;
    public final float miniGunDispersion = 10f;
    public final int miniGunAmmo = 100;

    public Color colorHud = new Color(1f, 1f, 1f, 0.4f);
    public Color colorLevelOverlay = new Color(0f, 0f, 0f, 0.9f);

    public Value pad = shorterPercent(0.05f);
    public Value buttonSize = shorterPercent(0.1f);
    public Value smallButtonSize = shorterPercent(0.2f);
    public Value logoWidth = shorterPercent(0.8f);
}
