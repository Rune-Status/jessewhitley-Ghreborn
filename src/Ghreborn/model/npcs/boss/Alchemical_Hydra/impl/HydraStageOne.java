package Ghreborn.model.npcs.boss.Alchemical_Hydra.impl;

import Ghreborn.Server;
import Ghreborn.event.CycleEventContainer;
import Ghreborn.model.npcs.boss.Alchemical_Hydra.AlchemicalHydraStage;
import Ghreborn.model.npcs.boss.Alchemical_Hydra.Alchemical_Hydra;
import Ghreborn.model.players.Client;

public class HydraStageOne extends AlchemicalHydraStage {

	public HydraStageOne(Alchemical_Hydra hydra, Client player) {
		super(hydra, player);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(CycleEventContainer container) {
		try {
			if (container.getOwner() == null || hydra == null || player == null || player.isDead
					|| hydra.getInstancedAlchemicalHydra() == null) {
				container.stop();
				return;
			}
			int cycle = container.getTotalTicks();
			if (cycle == 8) {
				player.stopMovement();
				player.getPA().sendScreenFade("Welcome to Alchemical Hydra...", -1, 4);
				player.getPA().movePlayer(1357, 10259, hydra.getInstancedAlchemicalHydra().getHeight());
			} else if (cycle == 13) {
				Server.npcHandler.spawnNpc(player, 8615, 1366, 10267, hydra.getInstancedAlchemicalHydra().getHeight(), 1, 1100, 35, 100, 150, false, false);
				player.ARMADYL_CLICKS = 0;
				container.stop();
				;
			}
			stop();
		} catch (Exception e) {

		}

	}
	

}
