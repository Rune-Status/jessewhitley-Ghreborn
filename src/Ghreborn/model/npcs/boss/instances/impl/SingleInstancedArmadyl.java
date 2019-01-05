package Ghreborn.model.npcs.boss.instances.impl;

import Ghreborn.model.npcs.NPCHandler;
import Ghreborn.model.npcs.boss.Armadyl.Armadyl;
import Ghreborn.model.npcs.boss.instances.SingleInstancedArea;
import Ghreborn.model.players.Boundary;
import Ghreborn.model.players.Client;

public class SingleInstancedArmadyl extends SingleInstancedArea {
	
	public SingleInstancedArmadyl(Client player, Boundary boundary, int height) {
		super(player, boundary, height);
	}
	
	@Override
	public void onDispose() {
		Armadyl armadyl = player.getArmadyl();
		if (player.getArmadyl().getNpc() != null) {
			NPCHandler.kill(player.getArmadyl().getNpc().npcType, height);
		}
		//Server.getGlobalObjects().remove(17000, height);
		//NPCHandler.kill(Zulrah.SNAKELING, height);
	}
}