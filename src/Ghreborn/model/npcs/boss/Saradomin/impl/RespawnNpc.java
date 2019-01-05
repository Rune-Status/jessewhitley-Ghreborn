package Ghreborn.model.npcs.boss.Saradomin.impl;

import Ghreborn.Server;
import Ghreborn.event.CycleEventContainer;
import Ghreborn.model.npcs.boss.Saradomin.Saradomin;
import Ghreborn.model.npcs.boss.Saradomin.SaradominStage;
import Ghreborn.model.players.Client;

public class RespawnNpc extends SaradominStage {

	public RespawnNpc(Saradomin saradomin, Client player) {
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
			if (cycle == 13) {
				Server.npcHandler.spawnNpc(player, 2205, 2898, 5266,  saradomin.getInstancedSaradomin().getHeight()+4, 1, 255, 20, 350, 250, false, false);
				Server.npcHandler.spawnNpc(player, 2206, 2901, 5271, saradomin.getInstancedSaradomin().getHeight()+4, 1, 100, 20, 80, 60, false, false);
				Server.npcHandler.spawnNpc(player, 2207, 2903, 5261, saradomin.getInstancedSaradomin().getHeight()+4, 1, 100, 20, 80, 60, false, false);
				Server.npcHandler.spawnNpc(player, 2208, 2893, 5267, saradomin.getInstancedSaradomin().getHeight()+4, 1, 100, 20, 80, 60, false, false);
				player.SARADOMIN_CLICKS = 0;
				container.stop();
				;
			}
			stop();
		} catch (Exception e) {

		}
	}
}