package Ghreborn.model.npcs.boss.instances.impl;

import Ghreborn.model.npcs.NPCHandler;
import Ghreborn.model.npcs.boss.Zamorak.Zamorak;
import Ghreborn.model.npcs.boss.instances.SingleInstancedArea;
import Ghreborn.model.players.Boundary;
import Ghreborn.model.players.Client;

public class SingleInstancedZamorak extends SingleInstancedArea {
	
	public SingleInstancedZamorak(Client player, Boundary boundary, int height) {
		super(player, boundary, height);
	}
	
	@Override
	public void onDispose() {
		Zamorak zamorak = player.getZamorak();
		if (player.getZamorak().getNpc() != null) {
			NPCHandler.kill(player.getZamorak().getNpc().npcType, height);
		}
		//Server.getGlobalObjects().remove(17000, height);
		//NPCHandler.kill(Zulrah.SNAKELING, height);
	}

}
