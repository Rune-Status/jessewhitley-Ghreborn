package Ghreborn.model.npcs.boss.Alchemical_Hydra;

import Ghreborn.event.CycleEvent;
import Ghreborn.model.players.Client;

public abstract class AlchemicalHydraStage extends CycleEvent {
	
	protected Alchemical_Hydra hydra;
	
	protected Client player;
	
	public AlchemicalHydraStage(Alchemical_Hydra hydra, Client player) {
		this.hydra = hydra;
		this.player = player;
	}

}
