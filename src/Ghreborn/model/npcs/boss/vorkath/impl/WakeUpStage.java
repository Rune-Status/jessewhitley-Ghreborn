package Ghreborn.model.npcs.boss.vorkath.impl;

import Ghreborn.Server;
import Ghreborn.event.CycleEventContainer;
import Ghreborn.model.npcs.boss.vorkath.Vorkath;
import Ghreborn.model.npcs.boss.vorkath.VorkathConstants;
import Ghreborn.model.npcs.boss.vorkath.VorkathState;
import Ghreborn.model.players.Client;

/**
 * @author Yasin
 */
public class WakeUpStage extends Stage {

    private boolean alreadyAlive;

    public WakeUpStage(Vorkath vorkath, Client player) {
        super(vorkath, player);
    }

    public WakeUpStage(Vorkath vorkath, Client player, boolean alreadyAlive) {
        super(vorkath, player);
        this.alreadyAlive = alreadyAlive;
    }

    @Override
    public void execute(CycleEventContainer container) {
        if (container.getOwner() == null || vorkath == null || player == null || player.isDead) {
            container.stop();
            return;
        }
        int cycle = container.getTotalTicks();
        //reset both combat until stage ends
        if(vorkath != null) {
        	vorkath.resetCombat();
        }
        if(alreadyAlive) {
            if(cycle == 3) {
                vorkath.getNpc().hp = VorkathConstants.VORKATH_LIFE_POINTS;
            }
            if(cycle == 6) {
                vorkath.getNpc().animation(7950);
            }
            if(cycle == 9) {
                vorkath.setAttacks(0);
                vorkath.getNpc().requestTransform(VorkathConstants.AWAKENED_VORKATH_ID);
                vorkath.setVorkath(Server.npcHandler.getNpc(VorkathConstants.AWAKENED_VORKATH_ID, VorkathConstants.X_COORDINATE, VorkathConstants.Y_COORDINATE, player.getHeight()));
                vorkath.setVorkathState(VorkathState.AWAKE);
            }
            if(cycle >= 12) {
                container.stop();
            }
        } else {
            if(cycle == 1) {
                player.startAnimation(2292);
                player.sendMessage("You poke the dragon..");
            }
            if(cycle == 2) {
                vorkath.getNpc().animation(7950);
                player.turnPlayerTo(vorkath.getNpc().getX(), vorkath.getNpc().getY() - 3);
                vorkath.getNpc().hp = VorkathConstants.VORKATH_LIFE_POINTS;
            }
            if(cycle == 9) {
                vorkath.getNpc().requestTransform(VorkathConstants.AWAKENED_VORKATH_ID);
                vorkath.setVorkathState(VorkathState.AWAKE);
                container.stop();
            }
        }


    }
}
