package Ghreborn.model.players;

import Ghreborn.core.PlayerHandler;
import Ghreborn.util.Misc;

/**
 * Request Help
 * @author I'm A Jerk
 */

public class RequestHelp {

	public static boolean requestingHelp = false;
	public static String otherPlayer = "";
	
	public static void sendOnlineStaff(Client c) {
		String[][] Staff_Config = { {"Sgsrocks", "28000", "28001"}, {"Twistndshout", "28002", "28003"}, {"Open", "28004", "28005"} , {"Lp316", "28006", "28007"}, {"Open", "28008", "28009"}, {"Open", "28010", "28011"}, {"Open", "28012", "28013"}, {"Open", "28014", "28015"}, {"Open", "28016", "28017"}   };
		for (int i = 0; i < Staff_Config.length; i++) {
			c.getPA().sendFrame126(Staff_Config[i][0], Integer.parseInt(Staff_Config[i][1]));
			if(PlayerHandler.isPlayerOn(Staff_Config[i][0])) {
				c.getPA().sendFrame126("<col=3CB71E>Online", Integer.parseInt(Staff_Config[i][2]));
			}
		}
	}
	
	public static void setInterface(Client c) {
		if (!requestingHelp) {
			sendOnlineStaff(c);
			c.setSidebarInterface(2, 24999);
			c.getPA().sendFrame106(2);
		} else if (requestingHelp) {
			c.setSidebarInterface(2, 638);
			c.getPA().sendFrame106(2);
			requestingHelp = false;
		}
	}
	
	public static void callForHelp(Client c) {
		if (System.currentTimeMillis() - c.lastRequest < 30000) {
			c.sendMessage("It has only been "+ getTimeLeft(c) +" seconds since your last request for help!");
			c.sendMessage("Please only request help from the staff every 30 seconds!");
			if (!requestingHelp) {
				c.setSidebarInterface(2, 638);
				c.getPA().sendFrame106(2);
			}
			return;
		}
		requestingHelp = true;
		otherPlayer = c.playerName;
		c.lastRequest = System.currentTimeMillis();
		setInterface(c);
		PlayerHandler.messageAllStaff("<img=5><col=255>"+getPlayer().playerName +" needs help, their coords are: "+ playerCoords() +".<img=5>", true);
	}
	
	public static long getTimeLeft(Client c) {
		return (System.currentTimeMillis() - c.lastRequest) / 1000;
	}

	public static Client getPlayer() {
		return PlayerHandler.getPlayerByName(otherPlayer);
	}
	
	public static String playerCoords() {
		return getPlayer().getX() +", "+ getPlayer().getY() +", "+ getPlayer().heightLevel;
	}

	public static void teleportToPlayer(Client c) {
		try {
			if (otherPlayer.equalsIgnoreCase(c.playerName)) {
				c.sendMessage("You can't teleport to yourself!");
				return;
			}
			if (otherPlayer != null && !otherPlayer.equalsIgnoreCase("")) {
				c.getPA().movePlayer(getPlayer().getX(), getPlayer().getY(), getPlayer().heightLevel);
				c.sendMessage("You telelported to "+ otherPlayer +".");
				otherPlayer = "";
			} else {
				c.sendMessage("There is no player to currently teleport to!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}