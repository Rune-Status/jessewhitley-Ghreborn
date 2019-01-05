package Ghreborn.model.npcs.boss.instances.impl;

import Ghreborn.model.npcs.NPCHandler;
import Ghreborn.model.npcs.boss.Kalphite.Kalphite;
import Ghreborn.model.npcs.boss.instances.SingleInstancedArea;
import Ghreborn.model.players.Boundary;
import Ghreborn.model.players.Client;

public class SingleInstancedKalphite extends SingleInstancedArea {
	
	public SingleInstancedKalphite(Client player, Boundary boundary, int height) {
		super(player, boundary, height);
	}
	
	@Override
	public void onDispose() {
		Kalphite kalphite = player.getKalphite();
		if (player.getKalphite().getNpc() != null) {
			NPCHandler.kill(player.getKalphite().getNpc().npcType, height);
		}
	}
}
