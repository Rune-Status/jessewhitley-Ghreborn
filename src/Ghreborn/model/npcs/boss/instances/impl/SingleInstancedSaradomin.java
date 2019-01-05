package Ghreborn.model.npcs.boss.instances.impl;

import Ghreborn.model.npcs.NPCHandler;
import Ghreborn.model.npcs.boss.Saradomin.Saradomin;
import Ghreborn.model.npcs.boss.instances.SingleInstancedArea;
import Ghreborn.model.players.Boundary;
import Ghreborn.model.players.Client;

public class SingleInstancedSaradomin extends SingleInstancedArea {
	
	public SingleInstancedSaradomin(Client player, Boundary boundary, int height) {
		super(player, boundary, height);
	}
	
	@Override
	public void onDispose() {
		Saradomin saradomin = player.getSaradomin();
		if (player.getSaradomin().getNpc() != null) {
			NPCHandler.kill(player.getSaradomin().getNpc().npcType, height);
		}
		//Server.getGlobalObjects().remove(17000, height);
		//NPCHandler.kill(Zulrah.SNAKELING, height);
	}

}
