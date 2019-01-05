package Ghreborn.model.npcs.boss.zulrah.impl;

import Ghreborn.event.CycleEventContainer;
import Ghreborn.model.npcs.boss.zulrah.Zulrah;
import Ghreborn.model.npcs.boss.zulrah.ZulrahLocation;
import Ghreborn.model.npcs.boss.zulrah.ZulrahStage;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.combat.CombatType;

public class MageStageThree extends ZulrahStage {

	public MageStageThree(Zulrah zulrah, Client player) {
		super(zulrah, player);
	}

	@Override
	public void execute(CycleEventContainer container) {
		if (container.getOwner() == null || zulrah == null || zulrah.getNpc() == null || zulrah.getNpc().isDead
				|| player == null || player.isDead || zulrah.getInstancedZulrah() == null) {
			container.stop();
			return;
		}
		zulrah.getNpc().setFacePlayer(true);
		if (zulrah.getNpc().totalAttacks > 5) {
			player.getZulrahEvent().changeStage(4, CombatType.RANGE, ZulrahLocation.WEST);
			zulrah.getNpc().totalAttacks = 0;
			container.stop();
			return;
		}
	}


}
