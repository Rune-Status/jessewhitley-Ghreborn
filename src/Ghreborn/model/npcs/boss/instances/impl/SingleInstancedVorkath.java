package Ghreborn.model.npcs.boss.instances.impl;

import Ghreborn.model.npcs.boss.instances.SingleInstancedArea;
import Ghreborn.model.npcs.boss.vorkath.Vorkath;
import Ghreborn.model.players.Boundary;
import Ghreborn.model.players.Client;

/**
 * @author Yasin
 */
public class SingleInstancedVorkath extends SingleInstancedArea {
    public SingleInstancedVorkath(Boundary boundary, int height) {
        super(boundary, height);
    }

    public SingleInstancedVorkath(Client player, Boundary boundary, int height) {
        super(player, boundary, height);
    }

    @Override
    public void onDispose() {
        Vorkath vorkath = player.getVorkath();
        if (vorkath != null) {
            vorkath.disposeInstance();
        }
    }
}