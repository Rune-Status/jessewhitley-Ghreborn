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
public class DeathStage extends Stage {
	
    public DeathStage(Vorkath vorkath, Client player) {
        super(vorkath, player);
    }

    @Override
    public void execute(CycleEventContainer container) {
        if (container.getOwner() == null || vorkath == null || player == null || player.isDead || vorkath.getVorkathInstance() == null) {
            container.stop();
            return;
        }
        int cycle = container.getTotalTicks();
        int height = vorkath.getVorkathInstance().getHeight();
        //reset both combat until stage ends
        vorkath.resetCombat();
        player.getCombat().resetPlayerAttack();
       // player.sendMessage("Death stage Cycle: " + cycle);
        if (cycle == 1) {
            vorkath.setVorkathState(VorkathState.RESTING);
            vorkath.getNpc().isDead = false;
            vorkath.getNpc().spawnedMinions = false;
            player.sendMessage("The dragon died..");
            vorkath.getNpc().animation(7949);//death animation
            vorkath.getNpc().requestTransform(8059);
            vorkath.setVorkath(Server.npcHandler.getNpc(VorkathConstants.SLEEPING_VORKATH_ID, VorkathConstants.X_COORDINATE, VorkathConstants.Y_COORDINATE, height));
            container.stop();
        }
        /*if (cycle == 10) {
            CycleEventHandler.getSingleton().addEvent(vorkath.getEventLock(), new WakeUpStage(vorkath, player, true), 1);
            container.stop();
        }*/
    }
}