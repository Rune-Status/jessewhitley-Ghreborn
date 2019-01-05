package Ghreborn.model.npcs.boss.Saradomin;

import Ghreborn.event.CycleEvent;
import Ghreborn.model.players.Client;

public abstract class SaradominStage extends CycleEvent {
	
	protected Saradomin saradomin;
	
	protected Client player;
	
	public SaradominStage(Saradomin saradomin, Client player) {
		this.saradomin = saradomin;
		this.player = player;
	}

}
