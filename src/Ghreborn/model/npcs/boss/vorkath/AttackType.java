package Ghreborn.model.npcs.boss.vorkath;

/**
 * @author Yasin
 */
public enum AttackType {

    MAGIC(7952, 1479, 1480),
    RANGED(7952, 1477, 1478),
    ONE_HIT(7960, 1491, 157),
    PRAYER_SNIPE(7952, 1471, 1473),
    POISON(7952, 1470, 1472),
    DRAGON_FIRE(7952, 393, 157),
    SPECIAL_1(7957, 1486, 1473), //acid
    SPECIAL_2(7952, 395, 369); //jihad

    private int animationId;

    private int projectileId;

    private int endGfx;

    AttackType(int animationId, int projectileId, int endGfx) {
        this.animationId = animationId;
        this.projectileId = projectileId;
        this.endGfx = endGfx;
    }


    public int getAnimationId() {
        return animationId;
    }

    public int getProjectileId() {
        return projectileId;
    }

    public int getEndGfx() {
        return endGfx;
    }
}
