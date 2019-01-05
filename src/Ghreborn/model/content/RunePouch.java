package Ghreborn.model.content;

import Ghreborn.model.players.Client;

public class RunePouch {
	
	private Client c;
	
	public RunePouch(Client c) {
		this.c = c;
	}

	public void updateRunePouchInterface() {
		for (int z = 0; z < c.runesInPouch; z++) {
			c.getPA().sendFrame34a(41710, c.runes[z], z, c.runeAmount[z]);
		}
		/*for (int i = 0; i < playerItems.length - getItems().freeSlots(); i++) {
			//getPA().sendFrame34a(41711, playerItems[i], i, getItems().getItemAmount(playerItems[i]));
		}*/
	}
	
	public void clearRunePouchInterface() {
		for (int z = 0; z < 3; z++) {
			c.getPA().sendFrame34a(41710, -1, z, -1);
		}
	}
	
	public boolean isRune(int itemId) {
		if (itemId >= 554 && itemId <= 566 || itemId == 9075) {
			return true;
		}
		return false;
	}
	
	public void addItemToRunePouch(int itemId, int amount) {
		if (!isRune(itemId)) {
			c.sendMessage("You can only use runes with the rune pouch.");
			return;
		}
		if (c.runesInPouch == 3) {
			c.sendMessage("Your rune pouch is full.");
			return;
		}
		if (c.runes[0] == itemId || c.runes[1] == itemId || c.runes[2] == itemId) {
			c.sendMessage("You can't add the same type of runes to the pouch twice.");
			return;
		}
		if (amount > 10000) {
			amount = 10000;
			c.sendMessage("You can only use 10000 runes at a times.");
		}
		for (int i = c.runesInPouch; i < c.runesInPouch + 1; i++) {
			c.getItems().deleteItem(itemId, amount);
			c.runes[i] = itemId;
			c.runeAmount[i] = amount;
		}
		c.runesInPouch += 1;
	}
	
	public void emptyRunePouch() {
		if (c.logoutDelay.elapsed() < 10000) {
			c.sendMessage("You must wait until you're out of combat to do this.");
			return;
		}
		if (c.getItems().freeSlots() <= 3) {
			c.sendMessage("You need atleast 4 inventory slots to do this.");
			return;
		}
		for (int i = 0; i < c.runesInPouch; i++) {
			c.getItems().addItem(c.runes[i], c.runeAmount[i]);
			c.runes[i] = -1;
			c.runeAmount[i] = -1;
			clearRunePouchInterface();
			updateRunePouchInterface();
		}
		c.runesInPouch = 0;
	}
	
}
