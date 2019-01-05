package Ghreborn.model.npcs.boss.Kraken;

import Ghreborn.event.CycleEvent;
import Ghreborn.model.players.Client;

public abstract class KrakenStage extends CycleEvent {
	
	protected Kraken kraken;
	
	protected Client player;
	
	public KrakenStage(Kraken kraken, Client player) {
		this.kraken = kraken;
		this.player = player;
	}

}
