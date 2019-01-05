package Ghreborn.model.npcs.boss.Zamorak.impl;

import Ghreborn.Server;
import Ghreborn.event.CycleEventContainer;
import Ghreborn.model.npcs.boss.Zamorak.Zamorak;
import Ghreborn.model.npcs.boss.Zamorak.ZamorakStage;
import Ghreborn.model.players.Client;

public class RespawnNpc extends ZamorakStage {

	public RespawnNpc(Zamorak zamorak, Client player) {
		super(zamorak, player);
	}

	@Override
	public void execute(CycleEventContainer container) {
		try {
			if (container.getOwner() == null || zamorak == null || player == null || player.isDead
					|| zamorak.getInstancedZamorak() == null) {
				container.stop();
				return;
			}
			int cycle = container.getTotalTicks();
			if (cycle == 13) {
				Server.npcHandler.spawnNpc(player, 3129, 2927, 5325, zamorak.getInstancedZamorak().getHeight() + 6, 1, 255, 25, 300, 270, false, false);
				Server.npcHandler.spawnNpc(player, 3130, 2923, 5322, zamorak.getInstancedZamorak().getHeight() + 6, 1, 100, 20, 200, 100, false, false);
				Server.npcHandler.spawnNpc(player, 3131, 2931, 5327, zamorak.getInstancedZamorak().getHeight() + 6, 1, 100, 20, 200, 100, false, false);
				Server.npcHandler.spawnNpc(player, 3132, 2932, 5322, zamorak.getInstancedZamorak().getHeight() + 6, 1, 100, 20, 200, 100, false, false);
				player.ZAMORAK_CLICKS = 0;
				container.stop();
				;
			}
			stop();
		} catch (Exception e) {

		}
	}
}
