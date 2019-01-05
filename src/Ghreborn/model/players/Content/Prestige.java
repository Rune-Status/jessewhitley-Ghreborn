package Ghreborn.model.players.Content;

import Ghreborn.model.players.Client;

/**
 * 
 * @author Linus/Herpus Derpus
 *
 */

public class Prestige {

	//this initiated int is used to determine which dialogue to send
	public static int prestigeChat = -1;
	
	//sets how many times a player can prestige in total
	public static int maxPrestige = 100;

	//sets what stats you wish to reset from the player when he prestiges
	public static int[] levelsToReset = {1, 2, 3, 4, 5, 6, 7};
	
	//determines if the player is allowed to prestige or not
	public static boolean allowedToPrestige(Client c) {
		if(c.prestigeLevel >= maxPrestige){
			c.sendMessage("You can only prestige to "+maxPrestige+".");
			return false;
		}
		for (int i = 0; i < levelsToReset.length; i++) {
			if(c.getLevelForXP(c.playerXP[i]) != 99){
				c.sendMessage("You need 99 in all combat type skills before trying this.");
				c.getPA().closeAllWindows();
				return false;
			}
		}
		if(c.getPA().getWearingAmount() > 0) {
			c.sendMessage("You cannot prestige with items equipped.");
			c.getPA().closeAllWindows();
			return false;
		}
		if(getCurrentPrestige(c) > maxPrestige) {
			c.sendMessage("You're already max prestiged.");
			c.getPA().closeAllWindows();
			return false;
		}
		if(c.getItems().freeSlots() < 1){
			c.sendMessage("You need to have at-least one inventory slot to do this.");
			c.getPA().closeAllWindows();
			return false;
		}
		return true;
	}

	//determines if the player is allowed to use the prestige shop or not
	public static boolean allowedToShop(Client c) {
		if(getCurrentPrestige(c) < 1) {
			c.sendMessage("You need to prestige atleast once before you can use this shop.");
			c.getPA().closeAllWindows();
			return false;
		}
		return true;
	}
	//fetches the players current prestige level
	public static int getCurrentPrestige(Client c) {
		if(c.prestigeLevel > maxPrestige) return maxPrestige; //if somehow the player has more than max, set to max.
		return c.prestigeLevel;
	}
	
	//resets the stats for the set id's in levelsToReset
	public static void resetLevels(Client c) {
		for (int j = 0; j < levelsToReset.length; j++) {
			c.playerXP[j] = c.getPA().getXPForLevel(1)+5;
			c.playerLevel[j] = c.getPA().getLevelForXP(c.playerXP[j]);
			c.getPA().refreshSkill(j);
		}
		c.playerXP[3] = 1300+5;
		c.playerLevel[3] = c.getPA().getLevelForXP(c.playerXP[3]);
		c.getPA().refreshSkill(3); //refreshes the hp skill
		c.getPA().requestUpdates();
		c.gfx100(199);
		return;
	}
	
	//this code is initated by the player which wishes to prestige.
	public static void initiatePrestige(Client c) {
		if(allowedToPrestige(c)) {
			resetLevels(c);
			c.prestigeLevel++;
			c.sendMessage("You have now prestiged. You are now at Prestige level "+getCurrentPrestige(c)+".");
			c.prestigePoints += c.prestigeLevel*1000;
			c.    			getPA().sendFrame126("@or1@Prestige Points: <col=ffffff>"+c.prestigePoints,
					29166);
			c.sendMessage("You are rewarded "+c.prestigePoints+" for resetting your stats.");
			c.getPA().messageall("<col=ff0000>Player "+c.playerName+" Has Prestige to Level "+c.prestigeLevel+".");
			c.getPA().closeAllWindows();
		}
	}
	
	//opens the correct prestige shop. allowedToShop conditions.
	public static void openPrestigeShop(Client c) {
		if(allowedToShop(c)) {
			c.getShops().openShop(39+getCurrentPrestige(c));
			c.sendMessage("You open up the Prestige shop. Currently level: "+getCurrentPrestige(c));
		}
	}
}