package Ghreborn.model.npcs.boss.instances.impl;

import Ghreborn.model.npcs.NPCHandler;
import Ghreborn.model.npcs.boss.Kraken.Kraken;
import Ghreborn.model.npcs.boss.instances.SingleInstancedArea;
import Ghreborn.model.players.Boundary;
import Ghreborn.model.players.Client;

public class SingleInstancedKraken extends SingleInstancedArea {
	
	public SingleInstancedKraken(Client player, Boundary boundary, int height) {
		super(player, boundary, height);
	}
	
	@Override
	public void onDispose() {
		Kraken kraken = player.getKraken();
		if (player.getKraken().getNpc() != null) {
			NPCHandler.kill(player.getKraken().getNpc().npcType, height);
		}
		//Server.getGlobalObjects().remove(17000, height);
		//NPCHandler.kill(Zulrah.SNAKELING, height);
	}

}
