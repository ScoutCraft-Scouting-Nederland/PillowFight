package nl.scoutcraft.pillowfight.game.powerup;

import java.util.Random;

public final class Powerups {

    private static final Random R = new Random();

    public static final Powerup DASH = new Dash((byte) 1);
    public static final Powerup DOUBLE_JUMP = new DoubleJump((byte) 2);
    public static final Powerup EXTRA_LIFE = new ExtraLife((byte) 3);
    public static final Powerup INVINCIBLE = new Invincible((byte) 4);
    public static final Powerup PILLOW_EXPLOSION = new PillowExplosion((byte) 5);
    public static final Powerup SUPER_PILLOW = new SuperPillow((byte) 6);

    private static final Powerup[] POWERUPS = { DASH, DOUBLE_JUMP, INVINCIBLE, PILLOW_EXPLOSION, SUPER_PILLOW, EXTRA_LIFE };
    private static int POWERUP_COUNT = POWERUPS.length;
    private static int EXTRA_LIFE_COUNT = 6;

    public static Powerup random() {
        Powerup powerup = POWERUPS[R.nextInt(POWERUP_COUNT)];

        if (powerup == EXTRA_LIFE) {
            EXTRA_LIFE_COUNT--;
            if (EXTRA_LIFE_COUNT == 0) POWERUP_COUNT--;
        }

        return powerup;
    }

    private Powerups(){}
}
