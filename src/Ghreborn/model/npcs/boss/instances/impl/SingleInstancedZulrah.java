package Ghreborn.model.npcs.boss.instances.impl;

import Ghreborn.Server;
import Ghreborn.model.npcs.NPCHandler;
import Ghreborn.model.npcs.boss.instances.SingleInstancedArea;
import Ghreborn.model.npcs.boss.zulrah.Zulrah;
import Ghreborn.model.players.Boundary;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.Player;

public class SingleInstancedZulrah extends SingleInstancedArea {
	
	public SingleInstancedZulrah(Client player, Boundary boundary, int height) {
		super(player, boundary, height);
	}
	
	@Override
	public void onDispose() {
		Zulrah zulrah = player.getZulrahEvent();
		if (zulrah.getNpc() != null) {
			NPCHandler.kill(zulrah.getNpc().npcType, height);
		}
		Server.getGlobalObjects().remove(17000, height);
		NPCHandler.kill(Zulrah.SNAKELING, height);
	}
	}
