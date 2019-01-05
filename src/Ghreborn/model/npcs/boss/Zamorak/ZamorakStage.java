package Ghreborn.model.npcs.boss.Zamorak;

import Ghreborn.event.CycleEvent;
import Ghreborn.model.players.Client;

public abstract class ZamorakStage extends CycleEvent {
	
	protected Zamorak zamorak;
	
	protected Client player;
	
	public ZamorakStage(Zamorak zamorak, Client player) {
		this.zamorak = zamorak;
		this.player = player;
	}

}

