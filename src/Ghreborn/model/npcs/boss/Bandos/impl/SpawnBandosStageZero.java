package Ghreborn.model.npcs.boss.Bandos.impl;

import Ghreborn.Server;
import Ghreborn.event.CycleEventContainer;
import Ghreborn.model.npcs.NPC;
import Ghreborn.model.npcs.boss.Bandos.Bandos;
import Ghreborn.model.npcs.boss.Bandos.BandosStage;
import Ghreborn.model.players.Client;

public class SpawnBandosStageZero extends BandosStage {

	public static int maxNPCs = 10000;
	public static int maxListedNPCs = 10000;
	public static int maxNPCDrops = 10000;
	public static NPC npcs[] = new NPC[maxNPCs];

	public SpawnBandosStageZero(Bandos bandos, Client player) {
		super(bandos, player);
	}

	@Override
	public void execute(CycleEventContainer container) {
		try {
			if (container.getOwner() == null || bandos == null || player == null || player.isDead
					|| bandos.getInstancedBandos() == null) {
				container.stop();
				return;
			}
			int cycle = container.getTotalTicks();
			if (cycle == 8) {
				player.stopMovement();
				player.getPA().sendScreenFade("Welcome to Bandos...", -1, 4);
				player.getPA().movePlayer(2864, 5354, bandos.getInstancedBandos().getHeight() + 6);
			} else if (cycle == 13) {
				Server.npcHandler.spawnNpc(player, 2215, 2870, 5359, bandos.getInstancedBandos().getHeight() + 6, 1, 255, 20, 350, 250, false, false);
				Server.npcHandler.spawnNpc(player, 2216, 2867, 5362, bandos.getInstancedBandos().getHeight() + 6, 1, 100, 20, 80, 60, false, false);
				Server.npcHandler.spawnNpc(player, 2217, 2871, 5354, bandos.getInstancedBandos().getHeight() + 6, 1, 100, 20, 80, 60, false, false);
				Server.npcHandler.spawnNpc(player, 2218, 2874, 5360, bandos.getInstancedBandos().getHeight() + 6, 1, 100, 20, 80, 60, false, false);
				player.BANDOS_CLICKS = 0;
				container.stop();
				;
			}
			stop();
		} catch (Exception e) {

		}
	}
}
