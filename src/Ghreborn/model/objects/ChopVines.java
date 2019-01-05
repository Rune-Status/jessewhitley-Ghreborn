package Ghreborn.model.objects;

import Ghreborn.model.players.Client;

public class ChopVines {
	
	public static void walkThru(Client player, int x, int y) {
		if(x > player.getX() && y == player.getY())
	        player.getPA().movePlayer(player.absX+2, player.absY+0);
	    else if(x < player.getX() && y == player.getPosition().getY())
	        player.getPA().movePlayer(player.absX-2, player.absY+0);
	    else if(x == player.getPosition().getX() && y < player.getPosition().getY())
	        player.getPA().movePlayer(player.absX+0, player.absY-2);
	    else
	        player.getPA().movePlayer(player.absX+0, player.absY+2);
	}
}
