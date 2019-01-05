package Ghreborn.core.event.impl;


import Ghreborn.core.PlayerHandler;
import Ghreborn.core.World;
import Ghreborn.core.event.Event;
import Ghreborn.core.task.impl.SavePlayers;

public class SavingEvent extends Event {

	public SavingEvent() {
		super(120000);
	}

	@Override
	public void execute() {
		if (PlayerHandler.playerCount <= 0) {
			return;
		}
		World.getWorld().submit(new SavePlayers());
	}

}
