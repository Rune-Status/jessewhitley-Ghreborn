package Ghreborn.model.npcs.boss.Armadyl.impl;

import Ghreborn.Server;
import Ghreborn.event.CycleEventContainer;
import Ghreborn.model.npcs.NPC;
import Ghreborn.model.npcs.boss.Armadyl.Armadyl;
import Ghreborn.model.npcs.boss.Armadyl.ArmadylStage;
import Ghreborn.model.players.Client;

public class SpawnArmadylStageZero extends ArmadylStage {

	public static int maxNPCs = 10000;
	public static int maxListedNPCs = 10000;
	public static int maxNPCDrops = 10000;
	public static NPC npcs[] = new NPC[maxNPCs];

	public SpawnArmadylStageZero(Armadyl armadyl, Client player) {
		super(armadyl, player);
	}

	@Override
	public void execute(CycleEventContainer container) {
		try {
			if (container.getOwner() == null || armadyl == null || player == null || player.isDead
					|| armadyl.getInstancedArmadyl() == null) {
				container.stop();
				return;
			}
			int cycle = container.getTotalTicks();
			if (cycle == 8) {
				player.stopMovement();
				player.getPA().sendScreenFade("Welcome to Armadyl...", -1, 4);
				player.getPA().movePlayer(2839, 5296, armadyl.getInstancedArmadyl().getHeight() + 6);
			} else if (cycle == 13) {
				Server.npcHandler.spawnNpc(player, 3162, 2833, 5303, armadyl.getInstancedArmadyl().getHeight() + 6, 1, 255, 23, 200, 300, false, false);
				Server.npcHandler.spawnNpc(player, 3163, 2830, 5304, armadyl.getInstancedArmadyl().getHeight() + 6, 1, 100, 13, 80, 60, false, false);
				Server.npcHandler.spawnNpc(player, 3164, 2830, 5300, armadyl.getInstancedArmadyl().getHeight() + 6, 1, 100, 13, 80, 60, false, false);
				Server.npcHandler.spawnNpc(player, 3165, 2839, 5301, armadyl.getInstancedArmadyl().getHeight() + 6, 1, 100, 13, 80, 60, false, false);
				player.ARMADYL_CLICKS = 0;
				container.stop();
				;
			}
			stop();
		} catch (Exception e) {

		}
	}
}