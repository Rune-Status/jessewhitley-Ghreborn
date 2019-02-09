package Ghreborn.model.npcs.boss.Kraken.impl;

import Ghreborn.Server;
import Ghreborn.event.CycleEventContainer;
import Ghreborn.model.npcs.boss.Kraken.Kraken;
import Ghreborn.model.npcs.boss.Kraken.KrakenStage;
import Ghreborn.model.players.Client;

public class SpawnKrakenStageZero extends KrakenStage {

	public SpawnKrakenStageZero(Kraken kraken, Client player) {
		super(kraken, player);
	}
		
	@Override
	public void execute(CycleEventContainer container) {
		if (container.getOwner() == null || kraken == null || player == null || player.isDead || kraken.getInstancedKraken() == null) {
			container.stop();
			return;
		}
		int cycle = container.getTotalTicks();
		if (cycle == 8) {
			player.stopMovement();
			player.getPA().sendScreenFade("Welcome to Kraken's Cave...", -1, 4);
			player.getPA().movePlayer(2278, 10024, kraken.getInstancedKraken().getHeight());
		}
		else if (cycle == 13) {
			Server.npcHandler.spawnNpc(player, 496, 2278, 10034, kraken.getInstancedKraken().getHeight(), -1, 1, 41, 500, 500, false, false);
			Server.npcHandler.spawnNpc(player, 493, 2275, 10034, kraken.getInstancedKraken().getHeight(), -1, 1, 41, 500, 500, false, false);
			Server.npcHandler.spawnNpc(player, 493, 2284, 10034, kraken.getInstancedKraken().getHeight(), -1, 1, 41, 500, 500, false, false);
			Server.npcHandler.spawnNpc(player, 493, 2275, 10038, kraken.getInstancedKraken().getHeight(), -1, 1, 41, 500, 500, false, false);
			Server.npcHandler.spawnNpc(player, 493, 2284, 10038, kraken.getInstancedKraken().getHeight(), -1, 1, 41, 500, 500, false, false);
			player.KRAKEN_CLICKS = 0;
			container.stop();;
			}
			stop();
		}
	}


