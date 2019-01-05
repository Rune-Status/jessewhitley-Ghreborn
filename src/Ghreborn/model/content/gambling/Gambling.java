package Ghreborn.model.content.gambling;

import Ghreborn.model.content.dialogue.DialogueManager;
import Ghreborn.model.content.dialogue.Emotion;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.PlayerSave;
import Ghreborn.util.Misc;

public class Gambling {

    private static final int CHANCE_OF_WINNING = 55;
    
    private static final int MAXIMUM_AMOUNT = 15000000;
    
    private static final int MINIMUM_AMOUNT = 500000;
    
    public static long MONEY_TRACKER;
    
    public static boolean calculateWin() {
    	return Misc.random(100) >= CHANCE_OF_WINNING;
    }
    
    public static boolean canPlay(Client player, int amount) {
    	if (player.getRights().isStaff()) {
    		DialogueManager.sendNpcChat(player, 1011, Emotion.DEFAULT, "Sorry, but Daniel has forbidden you from gambling.");
    		return false;
    	}
		if (amount > MAXIMUM_AMOUNT) {
			DialogueManager.sendNpcChat(player, 1011, Emotion.DEFAULT, "Woah there fella!", "The maximum bet allowed is " + Misc.format(MAXIMUM_AMOUNT) + "!");
			return false;
		}	
		if (amount < MINIMUM_AMOUNT) {
			DialogueManager.sendNpcChat(player, 1011, Emotion.DEFAULT, "Sorry buddy, bets have to be more than " + Misc.format(MINIMUM_AMOUNT) +".");
			return false;
		}
			if (!player.getItems().hasItemAmount(995, amount)) {
				DialogueManager.sendNpcChat(player, 1011, Emotion.DEFAULT, "You don't have that much money to bet!");
				return false;
			}			
    	return true;
    }

    public static void play(Client player, int amount) {
    	if (!canPlay(player, amount)) {
    		return;
    	}
		if (calculateWin()) {
			results(player, amount, true);
		} else {
			results(player, amount, false);
		}
    }
    
    public static void results(Client player, int amount, boolean win) {
    	String bet = Misc.format(amount);
	    if (win) {
	    		player.getItems().addItem(995, amount);	    		
			if (amount >= 10_000_000) {
				player.getPA().messageall("<img=8> <col=C42BAD>" + player.getUsername() + " has just won " + Misc.format(amount) + " from the Gambler!");
			}
			save(+amount);
			DialogueManager.sendNpcChat(player, 1011, Emotion.DEFAULT, "Congratulations! You have won " + bet + ".");
			return;
    }
			player.getItems().deleteItem2(995, amount);  		
		DialogueManager.sendNpcChat(player, 1011, Emotion.DEFAULT, "Sorry! You have lost " + bet + "!"); 
		save(-amount);
    }
    
    public static void save(long amount) {
    	MONEY_TRACKER += amount;
    	PlayerSave.saveGambling();
    }
	
}