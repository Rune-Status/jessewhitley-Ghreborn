package Ghreborn.model.players.packets;

import Ghreborn.Config;
import Ghreborn.Server;
import Ghreborn.definitions.ItemCacheDefinition;
import Ghreborn.model.items.ItemDefinition;
import Ghreborn.model.npcs.PetHandler;
import Ghreborn.model.players.Boundary;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.PacketType;
import Ghreborn.net.Packet;
/**
 * Drop Item
 **/
public class DropItem implements PacketType {

	@Override
	public void processPacket(Client c, Packet packet) {
		int itemId = packet.getUnsignedShortA();
		packet.getUnsignedByte();
		packet.getUnsignedByte();
		int slot = packet.getUnsignedShortA();
		c.alchDelay = System.currentTimeMillis();
		if (!c.canUsePackets) {
			return;
		}
		if (c.getPA().viewingOtherBank) {
			c.getPA().resetOtherBank();
		}
		if (!c.getItems().playerHasItem(itemId)) {
			return;
		}
		if (ItemCacheDefinition.forID(itemId).itemActions[4].equals("Destroy")) {
			String[][] info = {{"Are you sure you want to drop this item?", "14174"}, {"Yes.", "14175"}, {"No.", "14176"}, {"", "14177"}, {"Dropping this item will make you lose it forever.", "14182"}, {"", "14183"}, {ItemCacheDefinition.forID(itemId).getName(), "14184"}};
			c.getPA().sendFrame34(itemId, 0, 14171, 1);
			for (String[] element : info) {
				c.getPA().sendString(element[0], Integer.parseInt(element[1]));
			}
			c.setDestroyItem(itemId);
			c.getPA().sendChatInterface(14170);
			return;
		}
		if(PetHandler.spawnPet(c, itemId, slot, false)) {
			return;
		}
		if (Boundary.isIn(c, Boundary.DUEL_ARENAS)) {
			c.sendMessage("You can't drop items inside the arena!");
			return;
		}
		if (itemId == 12926) {
			int ammo = c.getToxicBlowpipeAmmo();
			int amount = c.getToxicBlowpipeAmmoAmount();
			int charge = c.getToxicBlowpipeCharge();
			if (ammo > 0 && amount > 0) {
				c.sendMessage("You must unload before you can uncharge.");
				return;
			}
			if (charge <= 0) {
				c.sendMessage("The toxic blowpipe had no charge, it is emptied.");
				c.getItems().deleteItem2(itemId, 1);
				c.getItems().addItem(12924, 1);
				return;
			}
			if (c.getItems().freeSlots() < 2) {
				c.sendMessage("You need at least two free slots to do this.");
				return;
			}
			c.getItems().deleteItem2(itemId, 1);
			c.getItems().addItem(12924, 1);
			c.getItems().addItem(12934, charge);
			c.setToxicBlowpipeAmmo(0);
			c.setToxicBlowpipeAmmoAmount(0);
			c.setToxicBlowpipeCharge(0);
			return;
		}

		if(c.getRights().isAdministrator() || c.getRights().isRainbow()) {
			c.getItems().deleteItem(itemId, slot, c.playerItemsN[slot]);
			c.sendMessage("Your item has dissappeared.", 255);
		}
		if (c.inTrade) {
			c.sendMessage("You can't drop items while trading!");
			return;
		}

		boolean droppable = true;
		for (int i : Config.UNDROPPABLE_ITEMS) {
			if (i == itemId) {
				droppable = false;
				break;
			}
		}
		if (slot >= c.playerItems.length || slot < 0 || slot >= c.playerItems.length) {
			return;
		}
		if (c.playerItemsN[slot] != 0 && itemId != -1 && c.playerItems[slot] == itemId + 1) {
			if (droppable) {
				if (c.underAttackBy > 0) {
					if (ItemDefinition.forId(itemId).getSpecialPrice() > 1000) {
						c.sendMessage("You may not drop items worth more than 1000 while in combat.");
						return;
					}
				}
				Server.itemHandler.createGroundItem(c, itemId, c.getX(),
						c.getY(), c.heightLevel, c.playerItemsN[slot], c.getId());
				c.getItems().deleteItem(itemId, slot, c.playerItemsN[slot]);
				  if(Config.SOUND){
					  c.sendSound(c.getSound().DROPITEM);
					  }
			} else {
				c.sendMessage("This items cannot be dropped.");
			}
		}

	}
}
