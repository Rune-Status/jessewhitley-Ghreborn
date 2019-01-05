package Ghreborn.core.task.impl;

import Ghreborn.core.GameEngine;
import Ghreborn.core.PlayerHandler;
import Ghreborn.core.World;
import Ghreborn.core.task.Task;
import Ghreborn.model.players.Client;

/**
 * 
 * @author Jinrake
 * PvP Planet
 *
 */

public class SavePlayers implements Task {

	@Override
	public void execute(GameEngine context) {
		if (PlayerHandler.players.length == 0)
			return;
		try {
			for (int j = 0; j < PlayerHandler.players.length; j++) {
				if (PlayerHandler.players[j] != null) {
					Client c = (Client)PlayerHandler.players[j];
					Ghreborn.model.players.PlayerSave.saveGame(c);			
				}
			}
		} catch (Exception e) {
			World.getWorld().handleError(e);
		}		
	}	

}
