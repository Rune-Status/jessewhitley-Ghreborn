package Ghreborn.model.npcs.boss.Armadyl;

import Ghreborn.event.CycleEvent;
import Ghreborn.model.players.Client;

public abstract class ArmadylStage extends CycleEvent {
	
	protected Armadyl armadyl;
	
	protected Client player;
	
	public ArmadylStage(Armadyl armadyl, Client player) {
		this.armadyl = armadyl;
		this.player = player;
	}

}
