package Ghreborn.model.npcs.boss.zulrah;

import Ghreborn.event.CycleEvent;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.Player;

public abstract class ZulrahStage extends CycleEvent {
	
	protected Zulrah zulrah;
	
	protected Client player;
	
	public ZulrahStage(Zulrah zulrah, Client player) {
		this.zulrah = zulrah;
		this.player = player;
	}

}
