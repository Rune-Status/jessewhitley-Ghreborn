package Ghreborn.model.npcs.boss;

import Ghreborn.Config;
import Ghreborn.core.PlayerHandler;
import Ghreborn.model.npcs.NPC;
import Ghreborn.model.npcs.NPCHandler;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.Player;

/**
 * 
 * @author Micheal / 01053
 *
 */

public class Scorpia {
	
	public static int maxNPCs = 10000;
	public static int maxListedNPCs = 10000;
	public static int maxNPCDrops = 10000;
	public static NPC npcs[] = new NPC[maxNPCs];
	
	/**
	 * Healing NPC's
	 * @param i
	 * @return
	 */
	
	public static void createPlayersProjectile(int x, int y, int offX, int offY, int angle, int speed, int gfxMoving, int startHeight, int endHeight,
			int lockon, int time) {
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			Client p = (Client) PlayerHandler.players[i];
			if (p != null) {
				Client person = p;
				if (person.getOutStream() != null) {
					if (person.distanceToPoint(x, y) <= 25) {
						if (p.heightLevel == p.heightLevel)
							person.getPA().createProjectile(x, y, offX, offY, angle, speed, gfxMoving, startHeight, endHeight, lockon, time);
					}
				}
			}
		}
	}

	/**
	 * Handles the gfx moving towards scorpia to heal Scorpia
	 * @param npc
	 * @param i
	 * @param player
	 */

	public static void Healing(NPC npc, int i) {
		if (NPCHandler.npcs[i] != null) {
			int nX = NPCHandler.npcs[i].getX();
			int nY = NPCHandler.npcs[i].getY();
			int pX = NPCHandler.ScorpX;
			int pY = NPCHandler.ScorpY;
			int offX = (pX - nX) * -1;
			int offY = (pY - nY) * -1;
			createPlayersProjectile(nX, nY, offX, offY, 50, NPCHandler.getProjectileSpeed(npc.index),
					168, 15, 0, NPCHandler.npcs[i].oldIndex, 5);
		}
	}
}