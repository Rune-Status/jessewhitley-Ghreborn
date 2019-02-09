package Ghreborn.model.npcs.boss.vorkath;

import Ghreborn.event.CycleEvent;
import Ghreborn.event.CycleEventContainer;
import Ghreborn.model.players.Client;

/**
 * @author Yasin
 */
public class Stage extends CycleEvent {

    protected Vorkath vorkath;

    protected Client player;

    public Stage(Vorkath vorkath, Client player) {
        this.vorkath = vorkath;
        this.player = player;
    }

    @Override
    public void execute(CycleEventContainer container) {

    }
}
