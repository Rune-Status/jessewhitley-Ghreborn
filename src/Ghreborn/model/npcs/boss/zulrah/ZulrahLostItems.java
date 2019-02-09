package Ghreborn.model.npcs.boss.zulrah;

import java.util.ArrayList;

import Ghreborn.Server;
import Ghreborn.model.items.GameItem;
import Ghreborn.model.items.ItemAssistant;
import Ghreborn.model.items.bank.BankItem;
import Ghreborn.model.players.Client;

@SuppressWarnings("serial")
public class ZulrahLostItems extends ArrayList<GameItem> {

	/**
	 * The player that has lost items
	 */
	private final Client player;

	/**
	 * Creates a new class for managing lost items by a single player
	 * 
	 * @param player the player who lost items
	 */
	public ZulrahLostItems(final Client player) {
		this.player = player;
	}

	/**
	 * Stores the players items into a list and deletes their items
	 */
	public void store() {
		for (int i = 0; i < player.playerItems.length; i++) {
			if (player.playerItems[i] < 1) {
				continue;
			}
			add(new GameItem(player.playerItems[i] - 1, player.playerItemsN[i]));
		}
		for (int i = 0; i < player.playerEquipment.length; i++) {
			if (player.playerEquipment[i] < 1) {
				continue;
			}
			add(new GameItem(player.playerEquipment[i], player.playerEquipmentN[i]));
		}
		player.getItems().deleteEquipment();
		player.getItems().deleteAllItems();
	}

	public void retain() {
		int price = 500_000;
		if (!player.getItems().playerHasItem(995, price)) {
			//player.talkingNpc = 2040;
			player.sendMessage("You need at least 500,000GP to claim your items.");
			return;
		}
		for (GameItem item : this) {
			player.getItems().sendItemToAnyTabOrDrop(new BankItem(item.getId(), item.getAmount()), player.getX(), player.getY());
		}
		clear();
		player.getItems().deleteItem2(995, price);
			player.sendMessage("You have retained all of your lost items for 500,000GP.");
			player.sendMessage("Your items are in your bank.");
		}

}
