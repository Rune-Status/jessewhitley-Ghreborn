package Ghreborn.model.players;

import java.util.ArrayList;

import Ghreborn.Server;
import Ghreborn.util.Misc;

/**
 * Flaxpicking. OUTDATED
 * @author Derive
 *
 */
public class Flax {
	
	public static ArrayList <int[]> flaxRemoved = new ArrayList<int[]>();
	
	public static void pickFlax(final Client c, final int x, final int y) {
		if (c.getItems().freeSlots() != 0) {
           c.getItems().addItem(1779, 1);
			//c.face(x, y);
			c.animation(827);
			c.sendMessage("You pick some flax.");
			if (Misc.random(3) == 1) {
				flaxRemoved.add(new int[] { x, y });
				Server.objectManager.removeObject(x, y);
			}
		} else {
			c.sendMessage("Not enough space in your inventory.");
			return;
		}
	
	}
}