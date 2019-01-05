package Ghreborn.model.npcs.boss.Bandos;

import Ghreborn.event.CycleEvent;
import Ghreborn.model.players.Client;

public abstract class BandosStage extends CycleEvent {

	protected Bandos bandos;

	protected Client player;

	public BandosStage(Bandos bandos, Client player) {
		this.bandos = bandos;
		this.player = player;
	}
}

