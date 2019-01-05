package Ghreborn.model.npcs.boss.zulrah.impl;

import java.util.Arrays;

import Ghreborn.event.CycleEventContainer;
import Ghreborn.event.CycleEventHandler;
import Ghreborn.model.npcs.boss.zulrah.DangerousEntity;
import Ghreborn.model.npcs.boss.zulrah.DangerousLocation;
import Ghreborn.model.npcs.boss.zulrah.SpawnDangerousEntity;
import Ghreborn.model.npcs.boss.zulrah.Zulrah;
import Ghreborn.model.npcs.boss.zulrah.ZulrahLocation;
import Ghreborn.model.npcs.boss.zulrah.ZulrahStage;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.combat.CombatType;


public class RangeStageNine extends ZulrahStage {
	
	private int finishedAttack;

	public RangeStageNine(Zulrah zulrah, Client player) {
		super(zulrah, player);
	}

	@Override
	public void execute(CycleEventContainer container) {
		if (container.getOwner() == null || zulrah == null || zulrah.getNpc() == null || zulrah.getNpc().isDead
				|| player == null || player.isDead || zulrah.getInstancedZulrah() == null) {
			container.stop();
			return;
		}
		int ticks = container.getTotalTicks();
		if (zulrah.getNpc().totalAttacks >= 20 && finishedAttack == 0) {
			finishedAttack = ticks;
			zulrah.getNpc().attackTimer = 20;
			zulrah.getNpc().setFacePlayer(false);
			CycleEventHandler.getSingleton().addEvent(player, new SpawnDangerousEntity(zulrah, player, Arrays.asList(
					DangerousLocation.SOUTH_EAST, DangerousLocation.SOUTH_WEST), DangerousEntity.MINION_NPC), 1);
		}
		if (finishedAttack > 0) {
			if (ticks - finishedAttack == 2) {
				CycleEventHandler.getSingleton().addEvent(player, new SpawnDangerousEntity(zulrah, player, Arrays.asList(
						DangerousLocation.values()), DangerousEntity.TOXIC_SMOKE, 35), 1);
			} else if (ticks - finishedAttack == 18) {
				zulrah.getNpc().setFacePlayer(false);
				zulrah.getNpc().totalAttacks = 0;
				zulrah.changeStage(10, CombatType.MELEE, ZulrahLocation.NORTH);
				container.stop();
			}
		}
	}

}
