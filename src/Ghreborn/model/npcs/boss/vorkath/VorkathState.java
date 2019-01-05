package Ghreborn.model.npcs.boss.vorkath;

/**
 * @author Yasin
 */
public enum VorkathState {
    SLEEPING(-1, -1),
    RESTING(-1, -1),
    AWAKE(-1, -1);

    private int animation;

    private int npcId;

    VorkathState(int animation, int npcId) {
        this.animation = animation;
        this.npcId = npcId;
    }

    public int getAnimation() {
        return animation;
    }

    public int getNpcId() {
        return npcId;
    }
}
