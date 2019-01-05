package Ghreborn.model.players.skills.Fishing;

import java.util.Random;

import Ghreborn.event.CycleEvent;
import Ghreborn.event.CycleEventContainer;
import Ghreborn.event.CycleEventHandler;
import Ghreborn.model.players.Client;
import Ghreborn.util.Misc;

/**
 * 
 * @author Micheal
 *
 */

public class AnglerDig {
	
	public static final Random random = new Random();
	
	private static int[] anglers = {13258, 13259, 13260, 13261
			
	};

	private static int DIG_OBJECT = 27557;
	
	/**
	 * Removes the worms object id
	 * @param c
	 */

	public static void removeWorm(Client c) {
		if (System.currentTimeMillis() - c.digging < 1800) {
			return;
		}
		if (c.hasDigged) {
			return;
		}
		c.animation(831);
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				int amount = Misc.random(1, 6);
				int chance = Misc.random(450);
				int r2 = random.nextInt(3);
				int r = random.nextInt(anglers.length);
				if (c.objectId == DIG_OBJECT && !c.hasDigged) {
					c.getPA().checkObjectSpawn(-1, c.objectX, c.objectY, 0, 10);
					c.hasDigged = true;
					c.lastX = c.objectX;
					c.lastY = c.objectY;
					c.sendMessage("You dig the up the sandworm.");
					if(chance == 1) {
						c.getItems().addItem(anglers[r], 1);
						c.sendMessage("You have found a mysterious piece of gear!");
					}
					c.getItems().addItem(13431, amount);
					container.stop();
				}
			}

			public void stop() {
				c.stopAnimation();
				c.alreadySpawned = false;
				c.hasDigged = false;
				spawn(c);
				c.digging = System.currentTimeMillis();
			}
		}, 1);
	}
	
	/**
	 * Respawns the worms object id
	 * @param c
	 */

	private static void spawn(Client c) {
		if (c.alreadySpawned) {
			return;
		}
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				c.getPA().checkObjectSpawn(DIG_OBJECT, c.lastX, c.lastY, 0, 10);
				container.stop();
			}

			public void stop() {
				c.alreadySpawned = true;
			}
		}, 3);
	}
}
