package Ghreborn.model.npcs.boss.Kalphite.impl;

import Ghreborn.Server;
import Ghreborn.event.CycleEventContainer;
import Ghreborn.model.npcs.NPC;
import Ghreborn.model.npcs.boss.Kalphite.Kalphite;
import Ghreborn.model.npcs.boss.Kalphite.Stage;
import Ghreborn.model.players.Client;

public class SpawnKalphite extends Stage {

	public static int maxNPCs = 10000;
	public static int maxListedNPCs = 10000;
	public static int maxNPCDrops = 10000;
	public static NPC npcs[] = new NPC[maxNPCs];

	public SpawnKalphite(Kalphite kalphite, Client player) {
		super(kalphite, player);
	}

	@Override
	public void execute(CycleEventContainer container) {
		try {
			if (container.getOwner() == null || kalphite == null || player == null || player.isDead
					|| kalphite.getInstancedKalphite() == null) {
				container.stop();
				return;
			}
			int cycle = container.getTotalTicks();
			if (cycle == 8) {
				player.stopMovement();
				player.getPA().sendScreenFade("Welcome to Kalphite...", -1, 4);
				player.getPA().movePlayer(3484, 9511, kalphite.getInstancedKalphite().getHeight());
			} else if (cycle == 13) {
				Server.npcHandler.spawnNpc(player, 963, 3484, 9511, kalphite.getInstancedKalphite().getHeight(), 1, 255, 20, 350, 350, false, false);
				player.KALPHITE_CLICKS = 0;
				container.stop();
				
			}
			stop();
		} catch (Exception e) {

		}
	}
}

