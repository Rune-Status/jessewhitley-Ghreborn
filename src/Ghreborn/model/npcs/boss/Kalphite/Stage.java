package Ghreborn.model.npcs.boss.Kalphite;

import Ghreborn.event.CycleEvent;
import Ghreborn.model.players.Client;

public abstract class Stage extends CycleEvent {

	protected Kalphite kalphite;

	protected Client player;

	public Stage(Kalphite kalphite, Client player) {
		this.kalphite = kalphite;
		this.player = player;
	}
}

