package Ghreborn.model.content;

import Ghreborn.model.players.Client;

public class PlayerContent {

	private final Client player;
	
	public PlayerContent(Client player) {
		this.player = player;
	}
	
	public Client getPlayer() {
		return player;
	}
	
}

