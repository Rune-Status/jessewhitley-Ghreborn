package Ghreborn.model.npcs.boss.Callisto;

import Ghreborn.event.CycleEvent;
import Ghreborn.event.CycleEventContainer;
import Ghreborn.event.CycleEventHandler;
import Ghreborn.model.players.Client;

/**
 * Callisto Ability class
 * @author Micheal/01053
 *
 */

public class Callisto {
	
	public static void KnockBack(Client c, int x, int y) {
		c.animation(807);
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				c.resetWalkingQueue();
				c.teleportToX = x;
				c.teleportToY = y;
				c.getPA().requestUpdates();
				container.stop();
			}
		}, 2);
	}
}
