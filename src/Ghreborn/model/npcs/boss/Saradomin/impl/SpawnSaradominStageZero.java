package Ghreborn.model.npcs.boss.Saradomin.impl;


import Ghreborn.Server;
import Ghreborn.event.CycleEventContainer;
import Ghreborn.model.npcs.NPC;
import Ghreborn.model.npcs.boss.Saradomin.Saradomin;
import Ghreborn.model.npcs.boss.Saradomin.SaradominStage;
import Ghreborn.model.players.Client;

public class SpawnSaradominStageZero extends SaradominStage {

	public static int maxNPCs = 10000;
	public static int maxListedNPCs = 10000;
	public static int maxNPCDrops = 10000;
	public static NPC npcs[] = new NPC[maxNPCs];

	public SpawnSaradominStageZero(Saradomin saradomin, Client player) {
		super(saradomin, player);
	}

	@Override
	public void execute(CycleEventContainer container) {
		try {
			if (container.getOwner() == null || saradomin == null || player == null || player.isDead
					|| saradomin.getInstancedSaradomin() == null) {
				container.stop();
				return;
			}
			int cycle = container.getTotalTicks();
			if (cycle == 8) {
				player.stopMovement();
				player.getPA().sendScreenFade("Welcome to Saradomin...", -1, 4);
				player.getPA().movePlayer(2907, 5265, saradomin.getInstancedSaradomin().getHeight() + 4);
			} else if (cycle == 13) {
				Server.npcHandler.spawnNpc(player, 2205, 2898, 5266, saradomin.getInstancedSaradomin().getHeight() + 4, 1, 255, 27, 196, 300, false, false);
				Server.npcHandler.spawnNpc(player, 2206, 2901, 5271, saradomin.getInstancedSaradomin().getHeight() + 4, 1, 100, 20, 50, 80, false, false);
				Server.npcHandler.spawnNpc(player, 2207, 2903, 5261, saradomin.getInstancedSaradomin().getHeight() + 4, 1, 100, 20, 50, 80, false, false);
				Server.npcHandler.spawnNpc(player, 2208, 2893, 5267, saradomin.getInstancedSaradomin().getHeight() + 4, 1, 100, 25, 50, 80, false, false);
				player.SARADOMIN_CLICKS = 0;
				container.stop();
				;
			}
			stop();
		} catch (Exception e) {

		}
	}
}
