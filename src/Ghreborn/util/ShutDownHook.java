package Ghreborn.util;

import Ghreborn.core.PlayerHandler;
import Ghreborn.model.players.Client;

public class ShutDownHook extends Thread {

	@Override
	public void run() {
		System.out.println("Shutdown thread run.");
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Client c = (Client)PlayerHandler.players[j];
				Ghreborn.model.players.PlayerSave.saveGame(c);			
			}		
		}
		System.out.println("Shutting down...");
	}

}