package Ghreborn.model.minigames.warriors_guild;


import java.util.Arrays;
import java.util.Optional;

import Ghreborn.Server;
import Ghreborn.event.CycleEvent;
import Ghreborn.event.CycleEventContainer;
import Ghreborn.event.CycleEventHandler;
import Ghreborn.model.content.dialogue.Dialogue;
import Ghreborn.model.content.dialogue.DialogueManager;
import Ghreborn.model.content.dialogue.Emotion;
import Ghreborn.model.content.dialogue.impl.LorelaiDialogue;
import Ghreborn.model.players.Boundary;
import Ghreborn.model.players.Client;
import Ghreborn.util.Misc;


/**
 * 
 * @author Jason http://www.rune-server.org/members/jason
 * @date Oct 20, 2013
 */
public class WarriorsGuildBasement {
	
	public static final Boundary[] CYCLOPS_BOUNDARY = new Boundary[] {
			new Boundary(2905, 9957, 2911, 9967, 0),
			new Boundary(2912, 9957, 2940, 9973, 0),
	};
	
	public static final Boundary[] WAITING_ROOM_BOUNDARY = new Boundary[] {
		new Boundary(2905, 9966, 2911, 9973, 0)
	};
	
	private Client player;
	
	private boolean active;
	
	public static final int[][] DEFENDER_DATA = {
		{12954, 50}
	};
	
	public WarriorsGuildBasement(Client player) {
		this.player = player;
	}
	
	public void cycle() {
		CycleEventHandler.getSingleton().stopEvents(this);
		setActive(true);
		CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer event) {
				if(player == null || player.disconnected) {
					event.stop();
					return;
				}
				if(!player.getItems().playerHasItem(8851, 10)) {
					removeFromRoom();
					setActive(false);
					event.stop();
					return;
				}
				if(!Boundary.isIn(player, CYCLOPS_BOUNDARY) || Boundary.isIn(player, WAITING_ROOM_BOUNDARY)) {
					setActive(false);
					event.stop();
					return;
				}
				player.getItems().deleteItem2(8851, 20);
				player.sendMessage("You notice some of your warrior guild tokens dissapear..", 255);
			}

			@Override
			public void stop() {
				
			}
			
		}, 100);
	}
	
	public void handleDoor() {
		if(player.absX == 2912 && player.absY == 9968) {
			CycleEventHandler.getSingleton().stopEvents(this);
			player.getPA().movePlayer(player.absX - 1, player.absY, 2);
		} else if(player.absX == 2911 && player.absY == 9968 || Boundary.isIn(player, WAITING_ROOM_BOUNDARY)) {
			if(player.getItems().playerHasItem(8851, 100)) {
				int current = currentDefender();
				if (current == -1) {
					player.start(new LorelaiDialogue());
				} else {
					DialogueManager.sendNpcChat(player, 2135, Emotion.CALM,"You are currently in posession of a "+player.getItems().getItemName(current)+".",
							"The fee for re-entering the area is 100 tokens.");
							//player.nextChat = 627;
				}
			} else {
				DialogueManager.sendNpcChat(player, 2135, Emotion.CALM,"You need atleast 100 warrior guild tokens.", "You can get some by operating the armour animator.");
				//player.nextChat = 0;
			}
		}
	}
	
	/**
	 * Attempts to return the value of the defender the player is wearing or is in posession of
	 * in their inventory. 
	 * @return	-1 will be returned in the case that the player does not have a defender
	 */
	private int currentDefender() {
		for(int index = DEFENDER_DATA.length - 1; index > -1; index--) {
			int[] defender = DEFENDER_DATA[index];
			if (player.getItems().playerHasItem(defender[0])
					|| player.getItems().isWearingItem(defender[0]) || player.getItems().bankContains(defender[0])) {
				return defender[0];
			}
		}
		return -1;
	}
	
	/**
	 * Attempts to return the next best defender.
	 * @return	The first defender, bronze, if the player doesnt have a defender.
	 * If the player has the best it will return the best. If either of the afforementioned
	 * conditions are not met, the next best defender is returned. 
	 */
	private int nextDefender() {
		int defender = currentDefender();
		if (defender == -1) {
			return DEFENDER_DATA[0][0];
		}
		int best = DEFENDER_DATA[DEFENDER_DATA.length - 1][0];
		if (best == defender) {
			return best;
		}
		int index = indexOf(defender);
		if (index != -1) {
			defender = DEFENDER_DATA[index + 1][0];
		}
		return defender;
	}
	
	/**
	 * Attempts to retrieve the index in the array of the defender
	 * @param defender	the defender
	 * @return	-1 will be returned if the defender cannot be found
	 */
	private int indexOf(int defender) {
		for (int index = 0; index < DEFENDER_DATA.length; index++) {
			if (defender == DEFENDER_DATA[index][0]) {
				return index;
			}
		}
		return -1;
	}
	
	/**
	 * Retrieves the drop chance of the next best defender the player
	 * can receive.
	 * @return	the chance of the dropped dagger.
	 */
	private int chance() {
		Optional<int[]> defender = Arrays.asList(DEFENDER_DATA).stream().filter(
				data -> data[0] == nextDefender()).findFirst();
		return defender.isPresent() ? defender.get()[1] : 0;
	}
	
	public void dropDefender(int x, int y) {
		int amount = player.getItems().getItemAmount(8851);
		if(isActive() && Boundary.isIn(player, CYCLOPS_BOUNDARY) && !Boundary.isIn(player, WAITING_ROOM_BOUNDARY) && amount > 1) {
			int chance = chance();
			int current = currentDefender();
			int item = current == -1 ? DEFENDER_DATA[0][0] : nextDefender();
			if (Misc.random(chance) == 0) {
				Server.itemHandler.createGroundItem(player, item, x, y, 2, 1, player.index);
				player.sendMessage("The cyclops dropped a "+player.getItems().getItemName(item)+" on the ground.", 600000);
			//	player.sendMessage("Rolled:"+chance / chance());
			}
		}
	}
	
	public void removeFromRoom() {
	if (Boundary.isIn(player, CYCLOPS_BOUNDARY)) {
		player.getPA().movePlayer(2911, 9968, 0);
		player.getDH().sendStatement("You do not have enough tokens to continue.");
		player.nextChat = 0;
		}
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
