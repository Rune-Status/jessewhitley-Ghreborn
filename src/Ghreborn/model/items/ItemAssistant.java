package Ghreborn.model.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.ArrayUtils;


import Ghreborn.Config;
import Ghreborn.Server;
import Ghreborn.core.PlayerHandler;
import Ghreborn.definitions.ItemCacheDefinition;
import Ghreborn.model.items.bank.BankItem;
import Ghreborn.model.items.bank.BankTab;
import Ghreborn.model.npcs.NPCHandler;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.Player;
import Ghreborn.model.players.Requirement;
import Ghreborn.model.players.combat.Degrade.DegradableItem;
import Ghreborn.model.players.combat.range.RangeData;
import Ghreborn.model.shops.ShopAssistant;
import Ghreborn.util.Misc;

public class ItemAssistant {

	private Client c;

	public ItemAssistant(Client client) {
		this.c = client;
	}

	/**
	 * Items
	 **/

	public int[][] brokenBarrows = { { 4708, 4860 }, { 4710, 4866 },
			{ 4712, 4872 }, { 4714, 4878 }, { 4716, 4884 }, { 4720, 4896 },
			{ 4718, 4890 }, { 4720, 4896 }, { 4722, 4902 }, { 4732, 4932 },
			{ 4734, 4938 }, { 4736, 4944 }, { 4738, 4950 }, { 4724, 4908 },
			{ 4726, 4914 }, { 4728, 4920 }, { 4730, 4926 }, { 4745, 4956 },
			{ 4747, 4926 }, { 4749, 4968 }, { 4751, 4794 }, { 4753, 4980 },
			{ 4755, 4986 }, { 4757, 4992 }, { 4759, 4998 } };

	public void resetItems(int WriteFrame) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSizeWord(53);
			c.getOutStream().writeWord(WriteFrame);
			c.getOutStream().writeWord(c.playerItems.length);
			for (int i = 0; i < c.playerItems.length; i++) {
				if (c.playerItemsN[i] > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord_v2(c.playerItemsN[i]);
				} else {
					c.getOutStream().writeByte(c.playerItemsN[i]);
				}
				c.getOutStream().writeWordBigEndianA(c.playerItems[i]);
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}
	}
	public int getWornItemSlot(int itemId) {
		for (int i = 0; i < c.playerEquipment.length; i++)
			if (c.playerEquipment[i] == itemId)
				return i;
		return -1;
	}
	public int getItemCount(int itemID) {
		int count = 0;
		for (int j = 0; j < c.playerItems.length; j++) {
			if (c.playerItems[j] == itemID + 1) {
				count += c.playerItemsN[j];
			}
		}
		return count;
	}

	public void writeBonus() {
		int offset = 0;
		String send = "";
		for (int i = 0; i < c.playerBonus.length; i++) {
			if (c.playerBonus[i] >= 0) {
				send = BONUS_NAMES[i] + ": +" + c.playerBonus[i];
			} else {
				send = BONUS_NAMES[i] + ": -"
						+ java.lang.Math.abs(c.playerBonus[i]);
			}

			if (i == 10) {
				offset = 1;
			}
			c.getPA().sendFrame126(send, (1675 + i + offset));
		}

	}

	public int getTotalCount(int itemID) {
		int count = 0;
		for (int j = 0; j < c.playerItems.length; j++) {
			if (ItemDefinition.forId(itemID + 1).isNoted()) {
				if (itemID + 2 == c.playerItems[j])
					count += c.playerItemsN[j];
			}
			if (!ItemDefinition.forId(itemID + 1).isNoted()) {
				if (itemID + 1 == c.playerItems[j]) {
					count += c.playerItemsN[j];
				}
			}
		}
		for (int j = 0; j < c.bankItems.length; j++) {
			if (c.bankItems[j] == itemID + 1) {
				count += c.bankItemsN[j];
			}
		}
		return count;
	}


	public void sendItemsKept() {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSizeWord(53);
			c.getOutStream().writeWord(6963);
			c.getOutStream().writeWord(c.itemKeptId.length);
			for (int i = 0; i < c.itemKeptId.length; i++) {
				if (c.playerItemsN[i] > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord_v2(1);
				} else {
					c.getOutStream().writeByte(1);
				}
				if (c.itemKeptId[i] > 0) {
					c.getOutStream().writeWordBigEndianA(c.itemKeptId[i] + 1);
				} else {
					c.getOutStream().writeWordBigEndianA(0);
				}
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}
	}

	/**
	 * Item kept on death
	 **/

	public void keepItem(int keepItem, boolean deleteItem) {
		int value = 0;
		int item = 0;
		int slotId = 0;
		boolean itemInInventory = false;
		for (int i = 0; i < c.playerItems.length; i++) {
			if (c.playerItems[i] - 1 > 0) {
				int inventoryItemValue = c.getShops().getItemShopValue(
						c.playerItems[i] - 1);
				if (inventoryItemValue > value && (!c.invSlot[i])) {
					value = inventoryItemValue;
					item = c.playerItems[i] - 1;
					slotId = i;
					itemInInventory = true;
				}
			}
		}
		for (int i1 = 0; i1 < c.playerEquipment.length; i1++) {
			if (c.playerEquipment[i1] > 0) {
				int equipmentItemValue = c.getShops().getItemShopValue(
						c.playerEquipment[i1]);
				if (equipmentItemValue > value && (!c.equipSlot[i1])) {
					value = equipmentItemValue;
					item = c.playerEquipment[i1];
					slotId = i1;
					itemInInventory = false;
				}
			}
		}
		if (itemInInventory) {
			c.invSlot[slotId] = true;
			if (deleteItem) {
				deleteItem(c.playerItems[slotId] - 1,
						getItemSlot(c.playerItems[slotId] - 1), 1);
			}
		} else {
			c.equipSlot[slotId] = true;
			if (deleteItem) {
				deleteEquipment(item, slotId);
			}
		}
		c.itemKeptId[keepItem] = item;
	}
	
	public int getTotalWorth() {
		int worth = 0;
		for (int inventorySlot = 0; inventorySlot < c.playerItems.length; inventorySlot++) {
			int inventoryId = c.playerItems[inventorySlot] - 1;
			int inventoryAmount = c.playerItemsN[inventorySlot];
			// int price = ShopAssistant.getItemShopValue(inventoryId);
			int price = ItemDefinition.forId(inventoryId).getSpecialPrice();
			if (inventoryId == 996)
				price = 1;
			if (inventoryId > 0 && inventoryAmount > 0) {
				worth += (price * inventoryAmount);
			}
		}
		for (int equipmentSlot = 0; equipmentSlot < c.playerEquipment.length; equipmentSlot++) {
			int equipmentId = c.playerEquipment[equipmentSlot];
			int equipmentAmount = c.playerEquipmentN[equipmentSlot];
			int price = ShopAssistant.getItemShopValue(equipmentId);
			if (equipmentId > 0 && equipmentAmount > 0) {
				worth += (price * equipmentAmount);
			}
		}
		return worth;
	}

	/**
	 * Reset items kept on death
	 **/

	public void resetKeepItems() {
		for (int i = 0; i < c.itemKeptId.length; i++) {
			c.itemKeptId[i] = -1;
		}
		for (int i1 = 0; i1 < c.invSlot.length; i1++) {
			c.invSlot[i1] = false;
		}
		for (int i2 = 0; i2 < c.equipSlot.length; i2++) {
			c.equipSlot[i2] = false;
		}
	}

	/**
	 * delete all items
	 **/

	public void deleteAllItems() {
		for (int i1 = 0; i1 < c.playerEquipment.length; i1++) {
			deleteEquipment(c.playerEquipment[i1], i1);
		}
		for (int i = 0; i < c.playerItems.length; i++) {
			deleteItem(c.playerItems[i] - 1, getItemSlot(c.playerItems[i] - 1),
					c.playerItemsN[i]);
		}
	}
	/**
	 * Check all slots and determine whether or
	 * not a slot is accompanied by that item
	 */
	public boolean isWearingItem(int itemID) {
		for(int i = 0; i < 12; i++) {
			if(c.playerEquipment[i] == itemID) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Check all slots and determine the amount
	 * of said item worn in that slot
	 */
	public int getWornItemAmount(int itemID) {
		for(int i = 0; i < 12; i++) {
			if(c.playerEquipment[i] == itemID) {
				return c.playerEquipmentN[i];
			}
		}
		return 0;
	}
	/**
	 * Drop all items for your killer
	 **/

	public void dropAllItems() {
		Client o = (Client) PlayerHandler.players[c.killerId];

		for (int i = 0; i < c.playerItems.length; i++) {
			if (o != null) {
				if (tradeable(c.playerItems[i] - 1)) {
					Server.itemHandler.createGroundItem(o,
							c.playerItems[i] - 1, c.getX(), c.getY(), c.heightLevel,
							c.playerItemsN[i], c.killerId);
				} else {
					if (specialCase(c.playerItems[i] - 1))
						Server.itemHandler.createGroundItem(o, 995, c.getX(),
								c.getY(), c.heightLevel,
								getUntradePrice(c.playerItems[i] - 1),
								c.killerId);
					Server.itemHandler.createGroundItem(c,
							c.playerItems[i] - 1, c.getX(), c.getY(), c.heightLevel,
							c.playerItemsN[i], c.index);
				}
			} else {
				Server.itemHandler.createGroundItem(c, c.playerItems[i] - 1,
						c.getX(), c.getY(), c.heightLevel, c.playerItemsN[i], c.index);
			}
		}
		for (int e = 0; e < c.playerEquipment.length; e++) {
			if (o != null) {
				if (tradeable(c.playerEquipment[e])) {
					Server.itemHandler.createGroundItem(o,
							c.playerEquipment[e], c.getX(), c.getY(), c.heightLevel,
							c.playerEquipmentN[e], c.killerId);
				} else {
					if (specialCase(c.playerEquipment[e]))
						Server.itemHandler.createGroundItem(o, 995, c.getX(),
								c.getY(), c.heightLevel,
								getUntradePrice(c.playerEquipment[e]),
								c.killerId);
					Server.itemHandler.createGroundItem(c,
							c.playerEquipment[e], c.getX(), c.getY(), c.heightLevel,
							c.playerEquipmentN[e], c.index);
				}
			} else {
				Server.itemHandler.createGroundItem(c, c.playerEquipment[e],
						c.getX(), c.getY(), c.heightLevel, c.playerEquipmentN[e], c.index);
			}
		}
		if (o != null) {
			Server.itemHandler.createGroundItem(o, 526, c.getX(), c.getY(),  c.heightLevel, 1,
					c.killerId);
		}
	}

	public int getUntradePrice(int item) {
		switch (item) {
		case 2518:
		case 2524:
		case 2526:
			return 100000;
		case 2520:
		case 2522:
			return 150000;
		}
		return 0;
	}

	public boolean specialCase(int itemId) {
		switch (itemId) {
		case 2518:
		case 2520:
		case 2522:
		case 2524:
		case 2526:
			return true;
		}
		return false;
	}

	public void handleSpecialPickup(int itemId) {
		// c.sendMessage("My " + getItemName(itemId) +
		// " has been recovered. I should talk to the void knights to get it back.");
		// c.getItems().addToVoidList(itemId);
	}

	public void addToVoidList(int itemId) {
		switch (itemId) {
		case 2518:
			c.voidStatus[0]++;
			break;
		case 2520:
			c.voidStatus[1]++;
			break;
		case 2522:
			c.voidStatus[2]++;
			break;
		case 2524:
			c.voidStatus[3]++;
			break;
		case 2526:
			c.voidStatus[4]++;
			break;
		}
	}

	public boolean tradeable(int itemId) {
		for (int j = 0; j < Config.ITEM_TRADEABLE.length; j++) {
			if (itemId == Config.ITEM_TRADEABLE[j])
				return false;
		}
		return true;
	}

	/**
	 * Add Item
	 **/
	public boolean addItem(int item, int amount) {
		// synchronized(c) {
		if (amount < 1) {
			return false;
		}
		if (item <= 0) {
			return false;
		}
		if ((((freeSlots() >= 1) || playerHasItem(item, 1)) && ItemDefinition.forId(item).isStackable()
				|| ((freeSlots() > 0) && !ItemDefinition.forId(item).isStackable()))) {
			for (int i = 0; i < c.playerItems.length; i++) {
				if ((c.playerItems[i] == (item + 1)) && ItemDefinition.forId(item).isStackable()
						&& (c.playerItems[i] > 0)) {
					c.playerItems[i] = (item + 1);
					if (((c.playerItemsN[i] + amount) < Config.MAXITEM_AMOUNT) && ((c.playerItemsN[i] + amount) > -1)) {
						c.playerItemsN[i] += amount;
					} else {
						c.playerItemsN[i] = Config.MAXITEM_AMOUNT;
					}
					if (c.getOutStream() != null && c != null) {
						c.getOutStream().createFrameVarSizeWord(34);
						c.getOutStream().writeWord(3214);
						c.getOutStream().writeByte(i);
						c.getOutStream().writeWord(c.playerItems[i]);
						if (c.playerItemsN[i] > 254) {
							c.getOutStream().writeByte(255);
							c.getOutStream().writeDWord(c.playerItemsN[i]);
						} else {
							c.getOutStream().writeByte(c.playerItemsN[i]);
						}
						c.getOutStream().endFrameVarSizeWord();
						c.flushOutStream();
					}
					i = 30;
					return true;
				}
			}
			for (int i = 0; i < c.playerItems.length; i++) {
				if (c.playerItems[i] <= 0) {
					c.playerItems[i] = item + 1;
					if ((amount < Config.MAXITEM_AMOUNT) && (amount > -1)) {
						c.playerItemsN[i] = 1;
						if (amount > 1) {
							c.getItems().addItem(item, amount - 1);
							return true;
						}
					} else {
						c.playerItemsN[i] = Config.MAXITEM_AMOUNT;
					}
					resetItems(3214);
					i = 30;
					return true;
				}
			}
			return false;
		} else {
			resetItems(3214);
			c.sendMessage("Not enough space in your inventory.");
			return false;
		}
		// }
	}

	public String itemType(int item) {
		if(Item.playerCape(item)) {
			return "cape";
		}
		if(Item.playerBoots(item)) {
			  return "boots";
		}
		if(Item.playerGloves(item)) {
			  return "gloves";
		}
		if(Item.playerShield(item)) {
			return "shield";
		}
		if(Item.playerAmulet(item)) {
			return "amulet";
		}
		if(Item.playerArrows(item)) {
			return "arrows";
		}
		if(Item.playerRings(item)) {
			return "ring";
		}
		if(Item.playerHats(item)) {
			return "hat";
		}
		if(Item.playerLegs(item)) {
			return "legs";
		}
		if(Item.playerBody(item)) {
			return "body";
		}
		return "weapon";
	}

	/**
	 * Bonuses
	 **/

	public final String[] BONUS_NAMES = { "Stab", "Slash", "Crush", "Magic",
			"Range", "Stab", "Slash", "Crush", "Magic", "Range", "Strength",
			"Prayer" };

	public void resetBonus() {
		for (int i = 0; i < c.playerBonus.length; i++) {
			c.playerBonus[i] = 0;
		}
	}

	public void getBonus() {
		for (int i = 0; i < c.playerEquipment.length; i++) {
			if (c.playerEquipment[i] > -1) {
				int[] ids = ItemDefinition.forId(c.playerEquipment[i]).getBonus();
				for (int k = 0; k < c.playerBonus.length; k++) {
					c.playerBonus[k] += ids[k];
				}
			}
		}
		if (c.getItems().isWearingItem(12926) && c.getToxicBlowpipeAmmoAmount() > 0 && c.getToxicBlowpipeCharge() > 0) {
			int dartStrength = RangeData.getRangeStr(c.getToxicBlowpipeAmmo());
			if (dartStrength > 18) {
				dartStrength = 18;
			}
			c.playerBonus[4] += RangeData.getRangeStr(c.getToxicBlowpipeAmmo());
		}
		if (EquipmentSet.VERAC.isWearingBarrows(c) && isWearingItem(12853)) {
			c.playerBonus[11] += 4;
		}
	}

	/**
	 * Wear Item
	 **/

	public void sendWeapon(int Weapon, String WeaponName) {
		String WeaponName2 = WeaponName.replaceAll("Bronze", "");
		WeaponName2 = WeaponName2.replaceAll("Iron", "");
		WeaponName2 = WeaponName2.replaceAll("Steel", "");
		WeaponName2 = WeaponName2.replaceAll("Black", "");
		WeaponName2 = WeaponName2.replaceAll("Mithril", "");
		WeaponName2 = WeaponName2.replaceAll("Adamant", "");
		WeaponName2 = WeaponName2.replaceAll("Rune", "");
		WeaponName2 = WeaponName2.replaceAll("Granite", "");
		WeaponName2 = WeaponName2.replaceAll("Dragon", "");
		WeaponName2 = WeaponName2.replaceAll("Drag", "");
		WeaponName2 = WeaponName2.replaceAll("Crystal", "");
		WeaponName2 = WeaponName2.trim();
		if (WeaponName.equals("Unarmed")) {
			c.setSidebarInterface(0, 5855); // punch, kick, block
			c.getPA().sendFrame126(WeaponName, 5857);
		} else if (WeaponName.endsWith("whip")) {
			c.setSidebarInterface(0, 12290); // flick, lash, deflect
			c.getPA().sendFrame246(12291, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 12293);
		} else if (WeaponName.endsWith("bow") || WeaponName.endsWith("10")
				|| WeaponName.endsWith("full")
				|| WeaponName.startsWith("seercull")) {
			c.setSidebarInterface(0, 1764); // accurate, rapid, longrange
			c.getPA().sendFrame246(1765, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 1767);
		} else if (WeaponName.startsWith("Staff")
				|| WeaponName.endsWith("staff") || WeaponName.endsWith("wand")) {
			c.setSidebarInterface(0, 328); // spike, impale, smash, block
			c.getPA().sendFrame246(329, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 331);
		} else if (WeaponName2.startsWith("dart")
				|| WeaponName2.startsWith("knife")
				|| WeaponName2.startsWith("javelin")
				|| WeaponName.equalsIgnoreCase("toktz-xil-ul")) {
			c.setSidebarInterface(0, 4446); // accurate, rapid, longrange
			c.getPA().sendFrame246(4447, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 4449);
		} else if (WeaponName2.startsWith("dagger")
				|| WeaponName2.contains("sword")) {
			c.setSidebarInterface(0, 2276); // stab, lunge, slash, block
			c.getPA().sendFrame246(2277, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 2279);
		} else if (WeaponName2.startsWith("pickaxe")) {
			c.setSidebarInterface(0, 5570); // spike, impale, smash, block
			c.getPA().sendFrame246(5571, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 5573);
		} else if (WeaponName2.startsWith("axe")
				|| WeaponName2.startsWith("battleaxe")) {
			c.setSidebarInterface(0, 1698); // chop, hack, smash, block
			c.getPA().sendFrame246(1699, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 1701);
		} else if (WeaponName2.startsWith("halberd")) {
			c.setSidebarInterface(0, 8460); // jab, swipe, fend
			c.getPA().sendFrame246(8461, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 8463);
		} else if (WeaponName2.startsWith("Scythe")) {
			c.setSidebarInterface(0, 8460); // jab, swipe, fend
			c.getPA().sendFrame246(8461, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 8463);
		} else if (WeaponName2.startsWith("spear")) {
			c.setSidebarInterface(0, 4679); // lunge, swipe, pound, block
			c.getPA().sendFrame246(4680, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 4682);
		} else if (WeaponName2.toLowerCase().contains("mace")) {
			c.setSidebarInterface(0, 3796);
			c.getPA().sendFrame246(3797, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 3799);

		} else if (c.playerEquipment[c.playerWeapon] == 4153) {
			c.setSidebarInterface(0, 425); // war hamer equip.
			c.getPA().sendFrame246(426, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 428);
		} else {
			c.setSidebarInterface(0, 2423); // chop, slash, lunge, block
			c.getPA().sendFrame246(2424, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 2426);
		}

	}

	/**
	 * Weapon Requirements
	 **/

	public void getRequirements(String itemName, int itemId) {
		c.attackLevelReq = c.defenceLevelReq = c.strengthLevelReq = c.rangeLevelReq = c.magicLevelReq = 0;
		if (itemName.contains("mystic") || itemName.contains("nchanted")) {
			if (itemName.contains("staff")) {
				c.magicLevelReq = 20;
				c.attackLevelReq = 40;
			} else {
				c.magicLevelReq = 20;
				c.defenceLevelReq = 20;
			}
		}
		if (itemName.contains("infinity")) {
			c.magicLevelReq = 50;
			c.defenceLevelReq = 25;
		}
		if (itemName.contains("splitbark")) {
			c.magicLevelReq = 40;
			c.defenceLevelReq = 40;
		}
		if (itemName.contains("Zuriel's robe top")) {
			c.magicLevelReq = 78;
			c.defenceLevelReq = 78;
		}
		if (itemName.contains("Zuriel's robe bottom")) {
			c.magicLevelReq = 78;
			c.defenceLevelReq = 78;
		}
		if (itemName.contains("Zuriel's hood")) {
			c.magicLevelReq = 78;
			c.defenceLevelReq = 78;
		}
		if (itemName.contains("Zuriel's staff")) {
			c.magicLevelReq = 78;
		}
		if (itemName.contains("Morrigan's_leather_body")) {
			c.rangeLevelReq = 78;
			c.defenceLevelReq = 78;
		}
		if (itemName.contains("Morrigan's_leather_chaps")) {
			c.rangeLevelReq = 78;
			c.defenceLevelReq = 78;
		}
		if (itemName.contains("Morrigan's_coif")) {
			c.rangeLevelReq = 78;
			c.defenceLevelReq = 78;
		}
		if (itemName.contains("Green")) {
			if (itemName.contains("hide")) {
				c.rangeLevelReq = 40;
				if (itemName.contains("body"))
					c.defenceLevelReq = 40;
				return;
			}
		}
		if (itemName.contains("Blue")) {
			if (itemName.contains("hide")) {
				c.rangeLevelReq = 50;
				if (itemName.contains("body"))
					c.defenceLevelReq = 40;
				return;
			}
		}
		if (itemName.contains("Red")) {
			if (itemName.contains("hide")) {
				c.rangeLevelReq = 60;
				if (itemName.contains("body"))
					c.defenceLevelReq = 40;
				return;
			}
		}
		if (itemName.contains("Black")) {
			if (itemName.contains("hide")) {
				c.rangeLevelReq = 70;
				if (itemName.contains("body"))
					c.defenceLevelReq = 40;
				return;
			}
		}
		if (itemName.contains("bronze")) {
			if (!itemName.contains("knife") && !itemName.contains("dart")
					&& !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")) {
				c.attackLevelReq = c.defenceLevelReq = 1;
			}
			return;
		}
		if (itemName.contains("iron")) {
			if (!itemName.contains("knife") && !itemName.contains("dart")
					&& !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")) {
				c.attackLevelReq = c.defenceLevelReq = 1;
			}
			return;
		}
		if (itemName.contains("steel")) {
			if (!itemName.contains("knife") && !itemName.contains("dart")
					&& !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")) {
				c.attackLevelReq = c.defenceLevelReq = 5;
			}
			return;
		}
		if (itemName.contains("black")) {
			if (!itemName.contains("knife") && !itemName.contains("dart")
					&& !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")
					&& !itemName.contains("vamb") && !itemName.contains("chap")) {
				c.attackLevelReq = c.defenceLevelReq = 10;
			}
			return;
		}
		if (itemName.contains("mithril")) {
			if (!itemName.contains("knife") && !itemName.contains("dart")
					&& !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")) {
				c.attackLevelReq = c.defenceLevelReq = 20;
			}
			return;
		}
		if (itemName.contains("adamant")) {
			if (!itemName.contains("knife") && !itemName.contains("dart")
					&& !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")) {
				c.attackLevelReq = c.defenceLevelReq = 30;
			}
			return;
		}
		if (itemName.contains("rune")) {
			if (!itemName.contains("knife") && !itemName.contains("dart")
					&& !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")
					&& !itemName.contains("'bow")) {
				c.attackLevelReq = c.defenceLevelReq = 40;
			}
			return;
		}
		if (itemName.contains("dragon")) {
			if (!itemName.contains("nti-") && !itemName.contains("fire")) {
				c.attackLevelReq = c.defenceLevelReq = 60;
				return;
			}
		}
		if (itemName.contains("crystal")) {
			if (itemName.contains("shield")) {
				c.defenceLevelReq = 70;
			} else {
				c.rangeLevelReq = 70;
			}
			return;
		}
		if (itemName.contains("ahrim")) {
			if (itemName.contains("staff")) {
				c.magicLevelReq = 70;
				c.attackLevelReq = 70;
			} else {
				c.magicLevelReq = 70;
				c.defenceLevelReq = 70;
			}
		}
		if (itemName.contains("karil")) {
			if (itemName.contains("crossbow")) {
				c.rangeLevelReq = 70;
			} else {
				c.rangeLevelReq = 70;
				c.defenceLevelReq = 70;
			}
		}
		if (itemName.contains("godsword")) {
			c.attackLevelReq = 75;
		}
		if (itemName.contains("3rd age") && !itemName.contains("amulet")) {
			c.defenceLevelReq = 60;
		}
		if (itemName.contains("Initiate")) {
			c.defenceLevelReq = 20;
		}
		if (itemName.contains("verac") || itemName.contains("guthan")
				|| itemName.contains("dharok") || itemName.contains("torag")) {

			if (itemName.contains("hammers")) {
				c.attackLevelReq = 70;
				c.strengthLevelReq = 70;
			} else if (itemName.contains("axe")) {
				c.attackLevelReq = 70;
				c.strengthLevelReq = 70;
			} else if (itemName.contains("warspear")) {
				c.attackLevelReq = 70;
				c.strengthLevelReq = 70;
			} else if (itemName.contains("flail")) {
				c.attackLevelReq = 70;
				c.strengthLevelReq = 70;
			} else {
				c.defenceLevelReq = 70;
			}
		}

		switch (itemId) {
		case 8839:
		case 8840:
		case 8842:
		case 11663:
		case 11664:
		case 11665:
			c.attackLevelReq = 42;
			c.rangeLevelReq = 42;
			c.strengthLevelReq = 42;
			c.magicLevelReq = 42;
			c.defenceLevelReq = 42;
			return;
		case 10551:
		case 2503:
		case 2501:
		case 2499:
		case 1135:
			c.defenceLevelReq = 40;
			return;
		case 4827:
		case 6522:
			c.rangeLevelReq = 60;
			break;
		case 6524:
			c.defenceLevelReq = 60;
			break;
		case 11284:
			c.defenceLevelReq = 75;
			return;
		case 6889:
		case 6914:
			c.magicLevelReq = 60;
			break;
		case 861:
			c.rangeLevelReq = 50;
			break;
		case 10828:
			c.defenceLevelReq = 55;
			break;
		case 11724:
		case 11726:
		case 11728:
			c.defenceLevelReq = 65;
			break;
		case 3751:
		case 3749:
		case 3755:
			c.defenceLevelReq = 40;
			break;

		case 7462:
		case 7461:
			c.defenceLevelReq = 40;
			break;
		case 8846:
			c.defenceLevelReq = 5;
			break;
		case 8847:
			c.defenceLevelReq = 10;
			break;
		case 8848:
			c.defenceLevelReq = 20;
			break;
		case 8849:
			c.defenceLevelReq = 30;
			break;
		case 8850:
			c.defenceLevelReq = 40;
			break;

		case 7460:
			c.defenceLevelReq = 40;
			break;

		case 837:
			c.rangeLevelReq = 61;
			break;

		case 4151: // if you don't want to use names
			c.attackLevelReq = 70;
			return;

		case 6724: // seercull
			c.rangeLevelReq = 60; // idk if that is correct
			return;
		case 4153:
			c.attackLevelReq = 50;
			c.strengthLevelReq = 50;
			return;
		}
	}

	/**
	 * two handed weapon check
	 **/
	public boolean is2handed(String itemName, int itemId) {
		ItemDefinition definition = ItemDefinition.forId(itemId);
		if (definition.isTwoHanded()) {
			return true;
		}
		if (itemName.contains("godsword") || itemName.contains("crystal") || itemName.contains("aradomin sword")
				|| itemName.contains("2h") || itemName.contains("spear") || itemName.contains("halberd")
				|| itemName.contains("longbow") || itemName.contains("shortbow") || itemName.contains("ark bow")
				|| itemName.contains("ahrim") || itemName.contains("karil") || itemName.contains("verac")
				|| itemName.contains("guthan") || itemName.contains("dharok") || itemName.contains("torag") || 
				itemName.contains("abyssal bludgeon") || itemName.contains("spade") || itemName.contains("casket") || 
				itemName.contains("clueless") || itemName.contains("ballista") || itemName.contains("hunting knife") || itemName.contains("elder maul") || itemName.contains("bulwark") || itemName.contains("claws")) {
			return true;
		}
		if(itemName.contains("lucky")) {
			return false;
		}
		switch (itemId) {
		case 12926:
		case 6724:
		case 11838:
		case 12809:
		case 13652:
		case 14484:
		case 3101:
		case 4153:
		case 12848:
		case 13263:
		case 6528:
		case 10887:
		case 12424:
		case 20997:
			return true;
		}
		return false;
	}
	/**
	 * Weapons special bar, adds the spec bars to weapons that require them and
	 * removes the spec bars from weapons which don't require them
	 **/

	public void addSpecialBar(int weapon) {
		switch (weapon) {

		case 859: // magic bows /*working*/
		case 861:
		case 12424:
		case 11235:
		case 12765:
		case 12766:
		case 12767:
		case 12768:
		case 11785:
		case 12788:
		case 12926:
		case 19481:
			c.getPA().sendFrame171(0, 7549);
			specialAmount(weapon, c.specAmount, 7561);
			break;
		case 4151: // whip
			c.getPA().sendFrame171(0, 12323);
			specialAmount(weapon, c.specAmount, 12335);
			break;

		case 4587: // dscimmy
			c.getPA().sendFrame171(0, 7599);
			specialAmount(weapon, c.specAmount, 7611);
			break;

		case 3204: // d hally
			c.getPA().sendFrame171(0, 8493);
			specialAmount(weapon, c.specAmount, 8505);
			break;

		case 1377: // d battleaxe
			c.getPA().sendFrame171(0, 7499);
			specialAmount(weapon, c.specAmount, 7511);
			break;

		case 4153: // gmaul
			c.getPA().sendFrame171(0, 7474);
			specialAmount(weapon, c.specAmount, 7486);
			break;

		case 1249: // dspear
			c.getPA().sendFrame171(0, 7674);
			specialAmount(weapon, c.specAmount, 7686);
			break;

		case 1215:// dragon dagger
		case 1231:
		case 5680:
		case 5698:
		case 1305: // dragon long
		case 15333:
		case 11698:
		case 11700:
		case 11730:
		case 11696:
			c.getPA().sendFrame171(0, 7574);
			specialAmount(weapon, c.specAmount, 7586);
			break;

		case 1434: // dragon mace
			c.getPA().sendFrame171(0, 7624);
			specialAmount(weapon, c.specAmount, 7636);
			break;

		default:
			c.getPA().sendFrame171(1, 7624); // mace interface
			c.getPA().sendFrame171(1, 7474); // hammer, gmaul
			c.getPA().sendFrame171(1, 7499); // axe
			c.getPA().sendFrame171(1, 7549); // bow interface
			c.getPA().sendFrame171(1, 7574); // sword interface
			c.getPA().sendFrame171(1, 7599); // scimmy sword interface, for most
												// swords
			c.getPA().sendFrame171(1, 8493);
			c.getPA().sendFrame171(1, 12323); // whip interface
			break;
		}
	}

	/**
	 * Specials bar filling amount
	 **/

	public void specialAmount(int weapon, double specAmount, int barId) {
		c.specBarId = barId;
		c.getPA().sendFrame70(specAmount >= 10 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 9 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 8 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 7 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 6 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 5 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 4 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 3 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 2 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 1 ? 500 : 0, 0, (--barId));
		updateSpecialBar();
		sendWeapon(weapon, getItemName(weapon));
	}

	/**
	 * Special attack text and what to highlight or blackout
	 **/

	public void updateSpecialBar() {
		if (c.usingSpecial) {
			c.getPA()
					.sendFrame126(
							""
									+ (c.specAmount >= 2 ? "<col=ffff00>S P"
											: "<col=000000>S P")
									+ ""
									+ (c.specAmount >= 3 ? "<col=ffff00> E"
											: "<col=000000> E")
									+ ""
									+ (c.specAmount >= 4 ? "<col=ffff00> C I"
											: "<col=000000> C I")
									+ ""
									+ (c.specAmount >= 5 ? "<col=ffff00> A L"
											: "<col=000000> A L")
									+ ""
									+ (c.specAmount >= 6 ? "<col=ffff00>  A"
											: "<col=000000>  A")
									+ ""
									+ (c.specAmount >= 7 ? "<col=ffff00> T T"
											: "<col=000000> T T")
									+ ""
									+ (c.specAmount >= 8 ? "<col=ffff00> A"
											: "<col=000000> A")
									+ ""
									+ (c.specAmount >= 9 ? "<col=ffff00> C"
											: "<col=000000> C")
									+ ""
									+ (c.specAmount >= 10 ? "<col=ffff00> K"
											: "<col=000000> K"), c.specBarId);
		} else {
			c.getPA().sendFrame126("<col=000000>S P E C I A L  A T T A C K",
					c.specBarId);
		}
	}

	/**
	 * Wear Item
	 **/

	public boolean wearItem(int wearID, int slot) {
		// synchronized (c) {
		int targetSlot = 0;
		boolean canWearItem = true;
		if (c.playerItems[slot] == (wearID + 1)) {
			ItemCacheDefinition item = ItemCacheDefinition.forID(wearID);

			if (item == null) {
				if (wearID == 15098) {
					c.sendMessage(
							"Please navigate to the designated dicing area in order to roll (South of edge bank)");
					return false;
				}
				c.sendMessage("This item is currently unwearable.");
				return false;
			}
			getRequirements(getItemName(wearID).toLowerCase(), wearID);
			if (!Requirement.canEquip(c, wearID)) {
				return false;
			}
			Optional<DegradableItem> degradable = DegradableItem.forId(wearID);
			if (degradable.isPresent()) {
				if (c.claimDegradableItem[degradable.get().ordinal()]) {
					c.sendMessage("A previous item simialr to this has degraded. You must go to the old man");
					c.sendMessage("in edgeville to claim this item.");
					return false;
				}
			}
			/*
			 * String i = Item.getItemName(c.wearId); if (!i.contains("staff")
			 * && !i.contains("wand") && c.wearSlot == c.playerWeapon) {
			 * c.getPA().resetAutocast(); }
			 */
			//targetSlot = item.getEquipmentSlot();
			if(itemType(wearID).equalsIgnoreCase("cape")) {
				targetSlot = 1;
			}else if(itemType(wearID).equalsIgnoreCase("hat")) {
				targetSlot = 0;
			}else if(itemType(wearID).equalsIgnoreCase("amulet")) {
				targetSlot = 2;
			}else if(itemType(wearID).equalsIgnoreCase("arrows")) {
				targetSlot = 13;
			}else if(itemType(wearID).equalsIgnoreCase("body")) {
				targetSlot = 4;
			}else if(itemType(wearID).equalsIgnoreCase("shield")) {
				targetSlot = 5;
			}else if(itemType(wearID).equalsIgnoreCase("legs")) {
				targetSlot = 7;
			}else if(itemType(wearID).equalsIgnoreCase("gloves")) {
				targetSlot = 9;
			}else if(itemType(wearID).equalsIgnoreCase("boots")) {
				targetSlot = 10;
			}else if(itemType(wearID).equalsIgnoreCase("ring")) {
				targetSlot = 12;
			}else{
				targetSlot = 3;
			}
			/*if(targetSlot == 3) {
				c.attackTimer = c.attackTimer = c.getCombat().getAttackDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());;
				c.sendMessage(""+c.attackTimer);
			}*/
			//c.switching = true;
			if (targetSlot == 3) {
				c.getPA().resetAutocast();
				c.usingSpecial = false;
				addSpecialBar(wearID);
				if (wearID != 4153 && wearID != 12848) {
					c.getCombat().resetPlayerAttack();
				}
				/*c.attackTimer -= 2;
				if (c.attackTimer < 1)
					c.attackTimer = 1;*/
				
			}
			if (targetSlot == -1) {
				if (wearID >= 5509 && wearID <= 5512 || wearID == 21347 || wearID == 15098 || wearID == 11918 || wearID == 13656 || wearID == 7959 || wearID == 7960) {
					return false;
				} else {
				c.sendMessage("This item cannot be worn.");
				return false;
				}
			}
			if (!canWearItem) {
				return false;
			}
			if (targetSlot == 10 || targetSlot == 7 || targetSlot == 5 || targetSlot == 4 || targetSlot == 0
					|| targetSlot == 9 || targetSlot == 10) {
				if (c.defenceLevelReq > 0) {
					if (c.getPA().getLevelForXP(c.playerXP[1]) < c.defenceLevelReq) {
						c.sendMessage("You need a defence level of " + c.defenceLevelReq + " to wear this item.");
						canWearItem = false;
						return false;
					}
				}

				if (c.rangeLevelReq > 0) {
					if (c.getPA().getLevelForXP(c.playerXP[4]) < c.rangeLevelReq) {
						c.sendMessage("You need a range level of " + c.rangeLevelReq + " to wear this item.");
						canWearItem = false;
						return false;
					}
				}

				if (c.magicLevelReq > 0) {
					if (c.getPA().getLevelForXP(c.playerXP[6]) < c.magicLevelReq) {
						c.sendMessage("You need a magic level of " + c.magicLevelReq + " to wear this item.");
						canWearItem = false;
						return false;
					}
				}
			}
			// }

			if (!canWearItem) {
				return false;
			}

			int wearAmount = c.playerItemsN[slot];
			if (wearAmount < 1) {

				return false;
			}
			if (slot >= 0 && wearID >= 0) {
				int toEquip = c.playerItems[slot];
				int toEquipN = c.playerItemsN[slot];
				int toRemove = c.playerEquipment[targetSlot];
				int toRemoveN = c.playerEquipmentN[targetSlot];
				boolean stackable = false;
				if (getItemName(toRemove).contains("javelin") || getItemName(toRemove).contains("dart")
						|| getItemName(toRemove).contains("knife") || getItemName(toRemove).contains("bolt")
						|| getItemName(toRemove).contains("arrow") || getItemName(toRemove).contains("Bolt")
						|| getItemName(toRemove).contains("bolts") || getItemName(toRemove).contains("thrownaxe")
						|| getItemName(toRemove).contains("throwing"))
					stackable = true;
				else
					stackable = false;
				if (toEquip == toRemove + 1 && ItemDefinition.forId(toRemove).isStackable()) {
					deleteItem(toRemove, getItemSlot(toRemove), toEquipN);
					c.playerEquipmentN[targetSlot] += toEquipN;
				} else if (targetSlot != 5 && targetSlot != 3) {
					if (playerHasItem(toRemove, 1) && stackable == true) {
						c.playerItems[slot] = 0;// c.playerItems[slot] =
												// toRemove + 1;
						c.playerItemsN[slot] = 0;// c.playerItemsN[slot] =
													// toRemoveN;
						if (toRemove > 0 && toRemoveN > 0)// c.playerEquipment[targetSlot]
															// = toEquip - 1;
							addItem2(toRemove, toRemoveN);// c.playerEquipmentN[targetSlot]
															// = toEquipN;
					} else {
						c.playerItems[slot] = toRemove + 1;
						c.playerItemsN[slot] = toRemoveN;
					}
					c.playerEquipment[targetSlot] = toEquip - 1;
					c.playerEquipmentN[targetSlot] = toEquipN;
				} else if (targetSlot == 5) {
					boolean wearing2h = is2handed(getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase(),
							c.playerEquipment[c.playerWeapon]);
					if (wearing2h) {
						toRemove = c.playerEquipment[c.playerWeapon];
						toRemoveN = c.playerEquipmentN[c.playerWeapon];
						c.playerEquipment[c.playerWeapon] = -1;
						c.playerEquipmentN[c.playerWeapon] = 0;
						updateSlot(c.playerWeapon);
					}
					c.playerItems[slot] = toRemove + 1;
					c.playerItemsN[slot] = toRemoveN;
					c.playerEquipment[targetSlot] = toEquip - 1;
					c.playerEquipmentN[targetSlot] = toEquipN;
				} else if (targetSlot == 3) {
					boolean is2h = is2handed(getItemName(wearID).toLowerCase(), wearID);
					boolean wearingShield = c.playerEquipment[c.playerShield] > 0;
					boolean wearingWeapon = c.playerEquipment[c.playerWeapon] > 0;
					if (is2h) {
						if (wearingShield && wearingWeapon) {
							if (freeSlots() > 0) {
								if (playerHasItem(toRemove, 1) && stackable == true) {
									c.playerItems[slot] = 0;// c.playerItems[slot]
															// = toRemove + 1;
									c.playerItemsN[slot] = 0;// c.playerItemsN[slot]
																// = toRemoveN;
									if (toRemove > 0 && toRemoveN > 0)// c.playerEquipment[targetSlot]
																		// =
																		// toEquip
																		// - 1;
										addItem2(toRemove, toRemoveN);// c.playerEquipmentN[targetSlot]
																		// =
																		// toEquipN;
								} else {
									c.playerItems[slot] = toRemove + 1;
									c.playerItemsN[slot] = toRemoveN;
								}
								c.playerEquipment[targetSlot] = toEquip - 1;
								c.playerEquipmentN[targetSlot] = toEquipN;
								removeItem(c.playerEquipment[c.playerShield], c.playerShield);
							} else {
								c.sendMessage("You do not have enough inventory space to do this.");
								return false;
							}
						} else if (wearingShield && !wearingWeapon) {
							c.playerItems[slot] = c.playerEquipment[c.playerShield] + 1;
							c.playerItemsN[slot] = c.playerEquipmentN[c.playerShield];
							c.playerEquipment[targetSlot] = toEquip - 1;
							c.playerEquipmentN[targetSlot] = toEquipN;
							c.playerEquipment[c.playerShield] = -1;
							c.playerEquipmentN[c.playerShield] = 0;
							updateSlot(c.playerShield);
						} else {
							if (playerHasItem(toRemove, 1) && stackable == true) {
								c.playerItems[slot] = 0;// c.playerItems[slot] =
														// toRemove + 1;
								c.playerItemsN[slot] = 0;// c.playerItemsN[slot]
															// = toRemoveN;
								if (toRemove > 0 && toRemoveN > 0)// c.playerEquipment[targetSlot]
																	// = toEquip
																	// - 1;
									addItem2(toRemove, toRemoveN);// c.playerEquipmentN[targetSlot]
																	// =
																	// toEquipN;
							} else {
								c.playerItems[slot] = toRemove + 1;
								c.playerItemsN[slot] = toRemoveN;
							}
							c.playerEquipment[targetSlot] = toEquip - 1;
							c.playerEquipmentN[targetSlot] = toEquipN;
						}
					} else {
						if (playerHasItem(toRemove, 1) && stackable == true) {
							c.playerItems[slot] = 0;// c.playerItems[slot] =
													// toRemove + 1;
							c.playerItemsN[slot] = 0;// c.playerItemsN[slot] =
														// toRemoveN;
							if (toRemove > 0 && toRemoveN > 0)// c.playerEquipment[targetSlot]
																// = toEquip -
																// 1;
								addItem2(toRemove, toRemoveN);// c.playerEquipmentN[targetSlot]
																// = toEquipN;
						} else {
							c.playerItems[slot] = toRemove + 1;
							c.playerItemsN[slot] = toRemoveN;
						}
						c.playerEquipment[targetSlot] = toEquip - 1;
						c.playerEquipmentN[targetSlot] = toEquipN;
					}
				}
				changed = -1;
				//resetItems(3214);
				//c.updateItems = true;
				//c.switching = false;
			}
			if (c.getOutStream() != null && c != null) {
				c.getOutStream().createFrameVarSizeWord(34);
				c.getOutStream().writeWord(1688);
				c.getOutStream().writeByte(targetSlot);
				c.getOutStream().writeWord(wearID + 1);

				if (c.playerEquipmentN[targetSlot] > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord(c.playerEquipmentN[targetSlot]);
				} else {
					c.getOutStream().writeByte(c.playerEquipmentN[targetSlot]);
				}

				c.getOutStream().endFrameVarSizeWord();
				c.flushOutStream();
			}
			sendWeapon(c.playerEquipment[c.playerWeapon], getItemName(c.playerEquipment[c.playerWeapon]));
			resetBonus();
			getBonus();
			writeBonus();
			c.getCombat().getPlayerAnimIndex(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
			c.getPA().requestUpdates();
			return true;
		} else {
			return false;
		}
		// }
	}
	/**
	 * Wielding items.
	 **/
	
	public void update() {
		if (changed == -1000) {
			return;
		} 
		if (changed == -1) {
			resetItems(3214);
			changed = -1000;
		} else {
			if (c.getOutStream() != null && c != null) {
				c.getOutStream().createFrameVarSizeWord(34);
				c.getOutStream().writeWord(3214);
				c.getOutStream().writeByte(changed);
				c.getOutStream().writeWord(c.playerItems[changed]);
				if (c.playerItemsN[changed] > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord(c.playerItemsN[changed]);
				} else {
					c.getOutStream().writeByte(c.playerItemsN[changed]);
				}
				c.getOutStream().endFrameVarSizeWord();
				c.flushOutStream();
			}
			changed = -1000;
		}
	}
	
	private int changed = -1000;
	
	public boolean addItem2(int item, int amount) {
		// synchronized(c) {
		if (amount < 1) {
			return false;
		}
		if (item <= 0) {
			return false;
		}
		if ((((freeSlots() >= 1) || playerHasItem(item, 1)) && ItemDefinition.forId(item).isStackable()
				|| ((freeSlots() > 0) && !ItemDefinition.forId(item).isStackable()))) {
			for (int i = 0; i < c.playerItems.length; i++) {
				if ((c.playerItems[i] == (item + 1)) && ItemDefinition.forId(item).isStackable()
						&& (c.playerItems[i] > 0)) {
					c.playerItems[i] = (item + 1);
					if (((c.playerItemsN[i] + amount) < Config.MAXITEM_AMOUNT) && ((c.playerItemsN[i] + amount) > -1)) {
						c.playerItemsN[i] += amount;
					} else {
						c.playerItemsN[i] = Config.MAXITEM_AMOUNT;
					}
			/*		*/
					changed = i;
					i = 30;
					return true;
				}
			}
			for (int i = 0; i < c.playerItems.length; i++) {
				if (c.playerItems[i] <= 0) {
					c.playerItems[i] = item + 1;
					if ((amount < Config.MAXITEM_AMOUNT) && (amount > -1)) {
						c.playerItemsN[i] = 1;
						if (amount > 1) {
							c.getItems().addItem2(item, amount - 1);
							return true;
						}
					} else {
						c.playerItemsN[i] = Config.MAXITEM_AMOUNT;
					}
					//resetItems(3214);
					changed = -1;
					i = 30;
					return true;
				}
			}
			return false;
		} else {
			//resetItems(3214);
			changed = -1;
			c.sendMessage("Not enough space in your inventory.");
			return false;
		}
		// }
	}
	public void wearItem(int wearID, int wearAmount, int targetSlot) {
		synchronized (c) {
			if (c.getOutStream() != null && c != null) {
				c.getOutStream().createFrameVarSizeWord(34);
				c.getOutStream().writeWord(1688);
				c.getOutStream().writeByte(targetSlot);
				c.getOutStream().writeWord(wearID + 1);

				if (wearAmount > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord(wearAmount);
				} else {
					c.getOutStream().writeByte(wearAmount);
				}
				c.getOutStream().endFrameVarSizeWord();
				c.flushOutStream();
				c.playerEquipment[targetSlot] = wearID;
				c.playerEquipmentN[targetSlot] = wearAmount;
				c.getItems().sendWeapon(
						c.playerEquipment[c.playerWeapon],
						c.getItems().getItemName(
								c.playerEquipment[c.playerWeapon]));
				c.getItems().resetBonus();
				c.getItems().getBonus();
				c.getItems().writeBonus();
				c.getCombat().getPlayerAnimIndex(
						c.getItems()
								.getItemName(c.playerEquipment[c.playerWeapon])
								.toLowerCase());
				c.updateRequired = true;
				c.setAppearanceUpdateRequired(true);
			}
		}
	}

	public void updateSlot(int slot) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSizeWord(34);
			c.getOutStream().writeWord(1688);
			c.getOutStream().writeByte(slot);
			c.getOutStream().writeWord(c.playerEquipment[slot] + 1);
			if (c.playerEquipmentN[slot] > 254) {
				c.getOutStream().writeByte(255);
				c.getOutStream().writeDWord(c.playerEquipmentN[slot]);
			} else {
				c.getOutStream().writeByte(c.playerEquipmentN[slot]);
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}

	}

	/**
	 * Remove Item
	 **/
	public void removeItem(int wearID, int slot) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			if (c.playerEquipment[slot] > -1) {
				if (addItem(c.playerEquipment[slot], c.playerEquipmentN[slot])) {
					c.playerEquipment[slot] = -1;
					c.playerEquipmentN[slot] = 0;
					sendWeapon(c.playerEquipment[c.playerWeapon],
							getItemName(c.playerEquipment[c.playerWeapon]));
					resetBonus();
					getBonus();
					writeBonus();
					c.getCombat().getPlayerAnimIndex(
							c.getItems()
									.getItemName(
											c.playerEquipment[c.playerWeapon])
									.toLowerCase());
					c.getOutStream().createFrame(34);
					c.getOutStream().writeWord(6);
					c.getOutStream().writeWord(1688);
					c.getOutStream().writeByte(slot);
					c.getOutStream().writeWord(0);
					c.getOutStream().writeByte(0);
					c.flushOutStream();
					c.updateRequired = true;
					c.setAppearanceUpdateRequired(true);
				}
			}
		}
	}

	/**
	 * BANK
	 */

	public void rearrangeBank() {
		int totalItems = 0;
		int highestSlot = 0;
		for (int i = 0; i < Config.BANK_SIZE; i++) {
			if (c.bankItems[i] != 0) {
				totalItems++;
				if (highestSlot <= i) {
					highestSlot = i;
				}
			}
		}

		for (int i = 0; i <= highestSlot; i++) {
			if (c.bankItems[i] == 0) {
				boolean stop = false;

				for (int k = i; k <= highestSlot; k++) {
					if (c.bankItems[k] != 0 && !stop) {
						int spots = k - i;
						for (int j = k; j <= highestSlot; j++) {
							c.bankItems[j - spots] = c.bankItems[j];
							c.bankItemsN[j - spots] = c.bankItemsN[j];
							stop = true;
							c.bankItems[j] = 0;
							c.bankItemsN[j] = 0;
						}
					}
				}
			}
		}

		int totalItemsAfter = 0;
		for (int i = 0; i < Config.BANK_SIZE; i++) {
			if (c.bankItems[i] != 0) {
				totalItemsAfter++;
			}
		}

		if (totalItems != totalItemsAfter)
			c.disconnected = true;
	}

	public void itemOnInterface(int id, int amount) {
		// synchronized(c) {
		c.getOutStream().createFrameVarSizeWord(53);
		c.getOutStream().writeWord(2274);
		c.getOutStream().writeWord(1);
		if (amount > 254) {
			c.getOutStream().writeByte(255);
			c.getOutStream().writeDWord_v2(amount);
		} else {
			c.getOutStream().writeByte(amount);
		}
		c.getOutStream().writeWordBigEndianA(id);
		c.getOutStream().endFrameVarSizeWord();
		c.flushOutStream();
	}

	public void resetBank() {
		int tabId = c.getBank().getCurrentBankTab().getTabId();
		for (int i = 0; i < c.getBank().getBankTab().length; i++) {
			if (i == 0)
				continue;
			if (i != c.getBank().getBankTab().length - 1 && c.getBank().getBankTab()[i].size() == 0
					&& c.getBank().getBankTab()[i + 1].size() > 0) {
				for (BankItem item : c.getBank().getBankTab()[i + 1].getItems()) {
					c.getBank().getBankTab()[i].add(item);
				}
				c.getBank().getBankTab()[i + 1].getItems().clear();
			}
		}
		c.getPA().sendFrame36(700, 0);
		c.getPA().sendFrame34a(58040, -1, 0, 0);
		int newSlot = -1;
		for (int i = 0; i < c.getBank().getBankTab().length; i++) {
			BankTab tab = c.getBank().getBankTab()[i];
			if (i == tabId) {
				c.getPA().sendFrame36(700 + i, 1);
			} else {
				c.getPA().sendFrame36(700 + i, 0);
			}
			if (tab.getTabId() != 0 && tab.size() > 0 && tab.getItem(0) != null) {
				c.getPA().sendFrame171(0, 58050 + i);
				c.getPA().sendFrame34a(58040 + i, c.getBank().getBankTab()[i].getItem(0).getId() - 1, 0,
						c.getBank().getBankTab()[i].getItem(0).getAmount());
			} else if (i != 0) {
				if (newSlot == -1) {
					newSlot = i;
					c.getPA().sendFrame34a(58040 + i, -1, 0, 0);
					c.getPA().sendFrame171(0, 58050 + i);
					continue;
				}
				c.getPA().sendFrame34a(58040 + i, -1, 0, 0);
				c.getPA().sendFrame171(1, 58050 + i);
			}
		}
		c.getOutStream().createFrameVarSizeWord(53);
		c.getOutStream().writeWord(5382); // bank
		c.getOutStream().writeWord(Config.BANK_SIZE);
		BankTab tab = c.getBank().getCurrentBankTab();
		for (int i = 0; i < Config.BANK_SIZE; i++) {
			if (i > tab.size() - 1) {
				c.getOutStream().writeByte(0);
				c.getOutStream().writeWordBigEndianA(0);
				continue;
			}
			BankItem item = tab.getItem(i);
			if (item == null)
				item = new BankItem(-1, 0);
			if (item.getAmount() > 254) {
				c.getOutStream().writeByte(255);
				c.getOutStream().writeDWord_v2(item.getAmount());
			} else {
				c.getOutStream().writeByte(item.getAmount());
			}
			if (item.getAmount() < 1)
				item.setAmount(0);
			if (item.getId() > Config.ITEM_LIMIT || item.getId() < 0)
				item.setId(-1);
			c.getOutStream().writeWordBigEndianA(item.getId());
		}
		c.getOutStream().endFrameVarSizeWord();
		c.flushOutStream();
		c.getPA().sendFrame126("" + c.getBank().getCurrentBankTab().size(), 58061);
		c.getPA().sendFrame126(Integer.toString(tabId), 5292);
		c.getPA().sendFrame126(Misc.capitalize(c.playerName.toLowerCase()) + "'s bank of Ghreborn", 58064);
	}

	public void resetTempItems() {
		// synchronized(c) {
		int itemCount = 0;
		for (int i = 0; i < c.playerItems.length; i++) {
			if (c.playerItems[i] > -1) {
				itemCount = i;
			}
		}
		c.getOutStream().createFrameVarSizeWord(53);
		c.getOutStream().writeWord(5064);
		c.getOutStream().writeWord(itemCount + 1);
		for (int i = 0; i < itemCount + 1; i++) {
			if (c.playerItemsN[i] > 254) {
				c.getOutStream().writeByte(255);
				c.getOutStream().writeDWord_v2(c.playerItemsN[i]);
			} else {
				c.getOutStream().writeByte(c.playerItemsN[i]);
			}
			if (c.playerItems[i] > Config.ITEM_LIMIT || c.playerItems[i] < 0) {
				c.playerItems[i] = Config.ITEM_LIMIT;
			}
			c.getOutStream().writeWordBigEndianA(c.playerItems[i]);
		}
		c.getOutStream().endFrameVarSizeWord();
		c.flushOutStream();
		// }
	}

	public int itemAmount(int itemID) {
		int tempAmount = 0;
		for (int i = 0; i < c.playerItems.length; i++) {
			if (c.playerItems[i] == itemID) {
				tempAmount += c.playerItemsN[i];
			}
		}
		return tempAmount;
	}

	public boolean isStackable(int itemID) {
		return ItemDefinition.forId(itemID).isStackable();
	}

	/**
	 * Update Equip tab
	 **/

	public void setEquipment(int wearID, int amount, int targetSlot) {
		// synchronized(c) {
		c.getOutStream().createFrameVarSizeWord(34);
		c.getOutStream().writeWord(1688);
		c.getOutStream().writeByte(targetSlot);
		c.getOutStream().writeWord(wearID + 1);
		if (amount > 254) {
			c.getOutStream().writeByte(255);
			c.getOutStream().writeDWord(amount);
		} else {
			c.getOutStream().writeByte(amount);
		}
		c.getOutStream().endFrameVarSizeWord();
		c.flushOutStream();
		c.playerEquipment[targetSlot] = wearID;
		c.playerEquipmentN[targetSlot] = amount;
		c.updateRequired = true;
		c.setAppearanceUpdateRequired(true);
	}

	/**
	 * Move Items
	 **/

	public void moveItems(int from, int to, int moveWindow, boolean insertMode) {
		if (moveWindow == 3214) {
			int tempI;
			int tempN;
			tempI = c.playerItems[from];
			tempN = c.playerItemsN[from];
			c.playerItems[from] = c.playerItems[to];
			c.playerItemsN[from] = c.playerItemsN[to];
			c.playerItems[to] = tempI;
			c.playerItemsN[to] = tempN;
		}
		if (moveWindow == 5382) {
			if (!c.isBanking) {
				c.getPA().removeAllWindows();
				resetBank();
				return;
			}
			if (c.getPA().viewingOtherBank) {
				c.getPA().resetOtherBank();
				return;
			}
			if (c.getBank().getBankSearch().isSearching()) {
				c.getBank().getBankSearch().reset();
				return;
			}
			if (c.getBankPin().requiresUnlock()) {
				resetBank();
				c.isBanking = false;
				c.getBankPin().open(2);
				return;
			}
			if (to > 999) {
				int tabId = to - 1000;
				if (tabId < 0)
					tabId = 0;
				if (tabId == c.getBank().getCurrentBankTab().getTabId()) {
					c.sendMessage("You cannot add an item from it's tab to the same tab.");
					resetBank();
					return;
				}
				if (from > c.getBank().getCurrentBankTab().size()) {
					resetBank();
					return;
				}
				BankItem item = c.getBank().getCurrentBankTab().getItem(from);
				if (item == null) {
					resetBank();
					return;
				}
				c.getBank().getCurrentBankTab().remove(item);
				c.getBank().getBankTab()[tabId].add(item);
			} else {
				if (from > c.getBank().getCurrentBankTab().size() - 1
						|| to > c.getBank().getCurrentBankTab().size() - 1) {
					resetBank();
					return;
				}
				if (!insertMode) {
					BankItem item = c.getBank().getCurrentBankTab().getItem(from);
					c.getBank().getCurrentBankTab().setItem(from, c.getBank().getCurrentBankTab().getItem(to));
					c.getBank().getCurrentBankTab().setItem(to, item);
				} else {
					int tempFrom = from;
					for (int tempTo = to; tempFrom != tempTo;)
						if (tempFrom > tempTo) {
							swapBankItem(tempFrom, tempFrom - 1);
							tempFrom--;
						} else if (tempFrom < tempTo) {
							swapBankItem(tempFrom, tempFrom + 1);
							tempFrom++;
						}
				}
			}
		}
		if (moveWindow == 5382) {
			resetBank();
		}
		if (moveWindow == 5064) {
			int tempI;
			int tempN;
			tempI = c.playerItems[from];
			tempN = c.playerItemsN[from];

			c.playerItems[from] = c.playerItems[to];
			c.playerItemsN[from] = c.playerItemsN[to];
			c.playerItems[to] = tempI;
			c.playerItemsN[to] = tempN;
			resetItems(3214);
		}
		resetTempItems();
		if (moveWindow == 3214) {
			resetItems(3214);
		}

	}

	public void swapBankItem(int from, int to) {
		BankItem item = c.getBank().getCurrentBankTab().getItem(from);
		c.getBank().getCurrentBankTab().setItem(from, c.getBank().getCurrentBankTab().getItem(to));
		c.getBank().getCurrentBankTab().setItem(to, item);
	}

	/**
	 * delete Item
	 **/

	public void deleteEquipment(int i, int j) {
		// synchronized(c) {
		if (PlayerHandler.players[c.index] == null) {
			return;
		}
		if (i < 0) {
			return;
		}

		c.playerEquipment[j] = -1;
		c.playerEquipmentN[j] = c.playerEquipmentN[j] - 1;
		c.getOutStream().createFrame(34);
		c.getOutStream().writeWord(6);
		c.getOutStream().writeWord(1688);
		c.getOutStream().writeByte(j);
		c.getOutStream().writeWord(0);
		c.getOutStream().writeByte(0);
		getBonus();
		if (j == c.playerWeapon) {
			sendWeapon(-1, "Unarmed");
		}
		resetBonus();
		getBonus();
		writeBonus();
		c.updateRequired = true;
		c.setAppearanceUpdateRequired(true);
	}

	public void deleteItem(int id, int amount) {
		if (id <= 0)
			return;
		for (int j = 0; j < c.playerItems.length; j++) {
			if (amount <= 0)
				break;
			if (c.playerItems[j] == id + 1) {
				c.playerItems[j] = 0;
				c.playerItemsN[j] = 0;
				amount--;
			}
		}
		resetItems(3214);
	}

	public void deleteItem(int id, int slot, int amount) {
		if (id <= 0 || slot < 0) {
			return;
		}
		if (c.playerItems[slot] == (id + 1)) {
			if (c.playerItemsN[slot] > amount) {
				c.playerItemsN[slot] -= amount;
			} else {
				c.playerItemsN[slot] = 0;
				c.playerItems[slot] = 0;
			}
			resetItems(3214);
		}
	}

	public void deleteItem2(int id, int amount) {
		int am = amount;
		for (int i = 0; i < c.playerItems.length; i++) {
			if (am == 0) {
				break;
			}
			if (c.playerItems[i] == (id + 1)) {
				if (c.playerItemsN[i] > amount) {
					c.playerItemsN[i] -= amount;
					break;
				} else {
					c.playerItems[i] = 0;
					c.playerItemsN[i] = 0;
					am--;
				}
			}
		}
		resetItems(3214);
	}

	/**
	 * Delete Arrows
	 **/
	public void deleteArrow() {
		// synchronized(c) {
		if (c.playerEquipment[c.playerCape] == 10499 && Misc.random(5) != 1
				&& c.playerEquipment[c.playerArrows] != 4740)
			return;
		if (c.playerEquipmentN[c.playerArrows] == 1) {
			c.getItems().deleteEquipment(c.playerEquipment[c.playerArrows],
					c.playerArrows);
		}
		if (c.playerEquipmentN[c.playerArrows] != 0) {
			c.getOutStream().createFrameVarSizeWord(34);
			c.getOutStream().writeWord(1688);
			c.getOutStream().writeByte(c.playerArrows);
			c.getOutStream().writeWord(c.playerEquipment[c.playerArrows] + 1);
			if (c.playerEquipmentN[c.playerArrows] - 1 > 254) {
				c.getOutStream().writeByte(255);
				c.getOutStream().writeDWord(
						c.playerEquipmentN[c.playerArrows] - 1);
			} else {
				c.getOutStream().writeByte(
						c.playerEquipmentN[c.playerArrows] - 1);
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
			c.playerEquipmentN[c.playerArrows] -= 1;
		}
		c.updateRequired = true;
		c.setAppearanceUpdateRequired(true);
	}

	public void deleteEquipment() {
		// synchronized(c) {
		if (c.playerEquipmentN[c.playerWeapon] == 1) {
			c.getItems().deleteEquipment(c.playerEquipment[c.playerWeapon],
					c.playerWeapon);
		}
		if (c.playerEquipmentN[c.playerWeapon] != 0) {
			c.getOutStream().createFrameVarSizeWord(34);
			c.getOutStream().writeWord(1688);
			c.getOutStream().writeByte(c.playerWeapon);
			c.getOutStream().writeWord(c.playerEquipment[c.playerWeapon] + 1);
			if (c.playerEquipmentN[c.playerWeapon] - 1 > 254) {
				c.getOutStream().writeByte(255);
				c.getOutStream().writeDWord(
						c.playerEquipmentN[c.playerWeapon] - 1);
			} else {
				c.getOutStream().writeByte(
						c.playerEquipmentN[c.playerWeapon] - 1);
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
			c.playerEquipmentN[c.playerWeapon] -= 1;
		}
		c.updateRequired = true;
		c.setAppearanceUpdateRequired(true);
	}

	/**
	 * Dropping Arrows
	 **/

	public void dropArrowNpc() {
		//if (c.getCombat().usingJavelins()) {
			//return;
		//}
		if ((c.playerEquipment[c.playerCape] == 10499 
				|| c.playerEquipment[c.playerCape] == 13337 
				|| c.playerEquipment[c.playerCape] ==  9756 
				|| c.playerEquipment[c.playerCape] == 9757))
			return;
		int enemyX = NPCHandler.npcs[c.oldNpcIndex].getX();
		int enemyY = NPCHandler.npcs[c.oldNpcIndex].getY();
		int height = NPCHandler.npcs[c.oldNpcIndex].heightLevel;
		if (Misc.random(10) >= 4) {
			if (Server.itemHandler.itemAmount(c.playerName, c.rangeItemUsed, enemyX, enemyY, height) == 0) {
				Server.itemHandler.createGroundItem(c, c.rangeItemUsed, enemyX, enemyY, height, 1, c.getId());
			} else if (Server.itemHandler.itemAmount(c.playerName, c.rangeItemUsed, enemyX, enemyY, height) != 0) {
				int amount = Server.itemHandler.itemAmount(c.playerName, c.rangeItemUsed, enemyX, enemyY, height);
				Server.itemHandler.removeGroundItem(c, c.rangeItemUsed, enemyX, enemyY, height, false);
				Server.itemHandler.createGroundItem(c, c.rangeItemUsed, enemyX, enemyY, height, amount + 1, c.getId());
			}
		}
	}

	/**
	 * Ranging arrows.
	 */
	public void dropArrowPlayer() {
		if (c.getCombat().usingJavelins()) {
			return;
		}
		int enemyX = PlayerHandler.players[c.oldPlayerIndex].getX();
		int enemyY = PlayerHandler.players[c.oldPlayerIndex].getY();
		int height = PlayerHandler.players[c.oldPlayerIndex].heightLevel;
		if ((c.playerEquipment[c.playerCape] == 10499 
				|| c.playerEquipment[c.playerCape] == 13337 
				|| c.playerEquipment[c.playerCape] ==  9756 
				|| c.playerEquipment[c.playerCape] == 9757))
			return;
		if (Misc.random(10) >= 4) {
			if (Server.itemHandler.itemAmount(c.playerName, c.rangeItemUsed, enemyX, enemyY, height) == 0) {
				Server.itemHandler.createGroundItem(c, c.rangeItemUsed, enemyX, enemyY, height, 1, c.getId());
			} else if (Server.itemHandler.itemAmount(c.playerName, c.rangeItemUsed, enemyX, enemyY, height) != 0) {
				int amount = Server.itemHandler.itemAmount(c.playerName, c.rangeItemUsed, enemyX, enemyY, height);
				Server.itemHandler.removeGroundItem(c, c.rangeItemUsed, enemyX, enemyY, height, false);
				Server.itemHandler.createGroundItem(c, c.rangeItemUsed, enemyX, enemyY, height, amount + 1, c.getId());
			}
		}
	}

	public void removeAllItems() {
		for (int i = 0; i < c.playerItems.length; i++) {
			c.playerItems[i] = 0;
		}
		for (int i = 0; i < c.playerItemsN.length; i++) {
			c.playerItemsN[i] = 0;
		}
		resetItems(3214);
	}

	public int freeSlots() {
		int freeS = 0;
		for (int i = 0; i < c.playerItems.length; i++) {
			if (c.playerItems[i] <= 0) {
				freeS++;
			}
		}
		return freeS;
	}

	public int findItem(int id, int[] items, int[] amounts) {
		for (int i = 0; i < c.playerItems.length; i++) {
			if (((items[i] - 1) == id) && (amounts[i] > 0)) {
				return i;
			}
		}
		return -1;
	}

	public static String getItemName(int ItemID) {
		if (ItemID == -1) {
			return "Unarmed";
		}
		if (ItemID == 15573) {
			return "Unarmed";
		}
		return ItemCacheDefinition.forID(ItemID).getName();
	}
	public int getItemId(String itemName) {
		for (ItemDefinition it : ItemDefinition.DEFINITIONS) {
			if (it.getName().equals(itemName)) {
				return it.getId();
			}
		}
		return -1;
	}
	
	public int getItemSlot(int ItemID) {
		for (int i = 0; i < c.playerItems.length; i++) {
			if ((c.playerItems[i] - 1) == ItemID) {
				return i;
			}
		}
		return -1;
	}

	public int getItemAmount(int ItemID) {
		int itemCount = 0;
		for (int i = 0; i < c.playerItems.length; i++) {
			if ((c.playerItems[i] - 1) == ItemID) {
				itemCount += c.playerItemsN[i];
			}
		}
		return itemCount;
	}

	public boolean playerHasItem(int itemID, int amt, int slot) {
		itemID++;
		int found = 0;
		if (c.playerItems[slot] == (itemID)) {
			for (int i = 0; i < c.playerItems.length; i++) {
				if (c.playerItems[i] == itemID) {
					if (c.playerItemsN[i] >= amt) {
						return true;
					} else {
						found++;
					}
				}
			}
			if (found >= amt) {
				return true;
			}
			return false;
		}
		return false;
	}
	public void createGroundItem(int itemID, int itemX, int itemY, int itemAmount, int height) {
		if (c.heightLevel != height) {
			return;
		}
		c.getOutStream().createFrame(85);
		c.getOutStream().writeByteC(itemY - 8 * c.mapRegionY);
		c.getOutStream().writeByteC(itemX - 8 * c.mapRegionX);
		c.getOutStream().createFrame(44);
		c.getOutStream().writeWordBigEndianA(itemID);
		c.getOutStream().writeWord(itemAmount);
		c.getOutStream().writeByte(0);
		c.flushOutStream();
		return;
	}


	public boolean playerHasItem(int itemID) {
		itemID++;
		for (int i = 0; i < c.playerItems.length; i++) {
			if (c.playerItems[i] == itemID)
				return true;
		}
		return false;
	}

	public boolean playerHasItem(int itemID, int amt) {
		itemID++;
		int found = 0;
		for (int i = 0; i < c.playerItems.length; i++) {
			if (c.playerItems[i] == itemID) {
				if (c.playerItemsN[i] >= amt) {
					return true;
				} else {
					found++;
				}
			}
		}
		if (found >= amt) {
			return true;
		}
		return false;
	}
	/**
	 * Getting un-noted items.
	 * 
	 * @param ItemID
	 * @return
	 */
	public int getUnnotedItem(int ItemID) {
		return ItemDefinition.forId(ItemID).getUnnotedId();
	}

	/**
	 * Drop Item
	 **/

	public void createGroundItem(int itemID, int itemX, int itemY,
			int itemAmount) {
		c.getOutStream().createFrame(85);
		c.getOutStream().writeByteC((itemY - 8 * c.mapRegionY));
		c.getOutStream().writeByteC((itemX - 8 * c.mapRegionX));
		c.getOutStream().createFrame(44);
		c.getOutStream().writeWordBigEndianA(itemID);
		c.getOutStream().writeWord(itemAmount);
		c.getOutStream().writeByte(0);
		c.flushOutStream();
	}

	/**
	 * Pickup Item
	 **/

	public void removeGroundItem(int itemID, int itemX, int itemY, int Amount, int height) {
		if (c == null) {
			return;
		}
		if (c.heightLevel != height) {
			return;
		}
		c.getOutStream().createFrame(85);
		c.getOutStream().writeByteC((itemY - 8 * c.mapRegionY));
		c.getOutStream().writeByteC((itemX - 8 * c.mapRegionX));
		c.getOutStream().createFrame(156);
		c.getOutStream().writeByteS(0);
		c.getOutStream().writeWord(itemID);
		c.flushOutStream();
	}

	public boolean ownsCape() {
		if (c.getItems().playerHasItem(2412, 1)
				|| c.getItems().playerHasItem(2413, 1)
				|| c.getItems().playerHasItem(2414, 1))
			return true;
		for (int j = 0; j < Config.BANK_SIZE; j++) {
			if (c.bankItems[j] == 2412 || c.bankItems[j] == 2413
					|| c.bankItems[j] == 2414)
				return true;
		}
		if (c.playerEquipment[c.playerCape] == 2413
				|| c.playerEquipment[c.playerCape] == 2414
				|| c.playerEquipment[c.playerCape] == 2415)
			return true;
		return false;
	}

	public boolean hasAllShards() {
		return playerHasItem(11818, 1) && playerHasItem(11820, 1) && playerHasItem(11822, 1);
	}
	
	public boolean hasAllPieces() {
		return playerHasItem(19679, 1) && playerHasItem(19681, 1) && playerHasItem(19683, 1);
	}
	public boolean hasFreeSlots(int slots) {
		return (freeSlots() >= slots);
	}
	/**
	 * Makes the Kodai wand.
	 */
	public void makeKodai() {
		deleteItem(21043, 1);
		deleteItem(6914, 1);
		addItem(21006, 1);
		c.getDH().sendStatement("You combine the insignia and wand to make a Kodai Wand!.");
	}
	/**
	 * Makes the godsword blade.
	 */
	public void makeBlade() {
		deleteItem(11818, 1);
		deleteItem(11820, 1);
		deleteItem(11822, 1);
		addItem(11798, 1);
		c.getDH().sendStatement("You combine the shards to make a godsword blade.");
	}
	public void makeTotem() {
		deleteItem(19679, 1);
		deleteItem(19681, 1);
		deleteItem(19683, 1);
		addItem(19685, 1);
		c.getDH().sendStatement("You combine the pieces to make a dark totem.");
	}


	public boolean isHilt(int i) {
		return i >= 11810 && i <= 11816 && i % 2 == 0;
	}

	public void replaceItem(Client c, int i, int l) {
		for(int k = 0; k < c.playerItems.length; k++) {
			if(playerHasItem(i, 1)) {
				deleteItem(i, getItemSlot(i), 1);
				addItem(l, 1);
			}
		}
	}

	public void addItemOrDrop(int i) {
		// TODO Auto-generated method stub
		
	}
	public boolean isNotable(int itemId) {
		return ItemDefinition.forId(itemId).isNoted();
	}
	public void addItemToBank1(int itemId, int amount) {
		BankTab tab = c.getBank().getCurrentBankTab();
		BankItem item = new BankItem(itemId + 1, amount);
		Iterator<BankTab> iterator = Arrays.asList(c.getBank().getBankTab()).iterator();
		while (iterator.hasNext()) {
			BankTab t = iterator.next();
			if (t != null && t.size() > 0) {
				Iterator<BankItem> iterator2 = t.getItems().iterator();
				while (iterator2.hasNext()) {
					BankItem i = iterator2.next();
					if (i.getId() == item.getId() && !isNotable(itemId)) {
						if (t.getTabId() != tab.getTabId()) {
							tab = t;
						}
					} else {
						if (isNotable(itemId) && i.getId() == item.getId() - 1) {
							item = new BankItem(itemId, amount);
							if (t.getTabId() != tab.getTabId()) {
								tab = t;
							}
						}
					}
				}
			}
		}
		if (isNotable(itemId))
			item = new BankItem(itemId, amount);
		if (tab.freeSlots() == 0) {
			c.getBank().setCurrentBankTab(c.getBank().getBankTab()[1]);
		}else if (tab.freeSlots() == 0) {
			c.getBank().setCurrentBankTab(c.getBank().getBankTab()[2]);
		}else if (tab.freeSlots() == 0) {
			c.getBank().setCurrentBankTab(c.getBank().getBankTab()[3]);
		}else if (tab.freeSlots() == 0) {
			c.getBank().setCurrentBankTab(c.getBank().getBankTab()[4]);
		}else if (tab.freeSlots() == 0) {
			c.getBank().setCurrentBankTab(c.getBank().getBankTab()[5]);
		}else if (tab.freeSlots() == 0) {
			c.getBank().setCurrentBankTab(c.getBank().getBankTab()[6]);
		}else if (tab.freeSlots() == 0) {
			c.getBank().setCurrentBankTab(c.getBank().getBankTab()[7]);
	}else if (tab.freeSlots() == 0) {
		c.getBank().setCurrentBankTab(c.getBank().getBankTab()[8]);
	}
		long totalAmount = ((long) tab.getItemAmount(item) + (long) item.getAmount());
		if (totalAmount >= Integer.MAX_VALUE) {
			c.sendMessage("The item has been dropped on the floor.");
			Server.itemHandler.createGroundItem(c, itemId, c.absX, c.absY, c.heightLevel, amount, c.playerIndex);
			return;
		}
		tab.add(item);
		resetTempItems();
		if (c.isBanking)
			resetBank();
		c.sendMessage(getItemName(itemId) + " x" + item.getAmount() + " has been added to your bank.");
	}

	/**
	 * Banking your item.
	 * 
	 * @param itemID
	 * @param fromSlot
	 * @param amount
	 * @return
	 */

	public boolean addToBank(int itemID, int amount, boolean updateView) {
		try {
		if (c.getPA().viewingOtherBank) {
			c.getPA().resetOtherBank();
			return false;
		}
		if (!c.isBanking)
			return false;
		if (!c.getItems().playerHasItem(itemID))
			return false;
		if (c.getBank().getBankSearch().isSearching()) {
			c.getBank().getBankSearch().reset();
			return false;
		}
		if (c.getBankPin().requiresUnlock()) {
			resetBank();
			c.getBankPin().open(2);
			return false;
		}
		BankTab tab = c.getBank().getCurrentBankTab();
		BankItem item = new BankItem(itemID + 1, amount);
		Iterator<BankTab> iterator = Arrays.asList(c.getBank().getBankTab()).iterator();
		while (iterator.hasNext()) {
			BankTab t = iterator.next();
			if (t != null && t.size() > 0) {
				Iterator<BankItem> iterator2 = t.getItems().iterator();
				while (iterator2.hasNext()) {
					BankItem i = iterator2.next();
					if (i.getId() == item.getId() && !isNotable(itemID)) {
						if (t.getTabId() != tab.getTabId()) {
							tab = t;
						}
					} else {
						if (ItemDefinition.DEFINITIONS[itemID].isNoteable() && i.getId() == item.getId()) {
							item = new BankItem(itemID, amount);
							if (t.getTabId() != tab.getTabId()) {
								tab = t;
							}
						}
					}
				}
			}
		}
		if (isNotable(itemID)) {
			item = new BankItem(itemID, amount);
		}
		if (item.getAmount() > getItemAmount(itemID))
			item.setAmount(getItemAmount(itemID));
		if (tab.getItemAmount(item) == Integer.MAX_VALUE) {
			c.sendMessage("Your bank is already holding the maximum amount of " + getItemName(itemID).toLowerCase()
					+ " possible.");
			return false;
		}
		if (tab.freeSlots() == 0 && !tab.contains(item)) {
			c.sendMessage("Your current bank tab is full.");
			return false;
		}
		long totalAmount = ((long) tab.getItemAmount(item) + (long) item.getAmount());
		if (totalAmount >= Integer.MAX_VALUE) {
			int difference = Integer.MAX_VALUE - tab.getItemAmount(item);
			item.setAmount(difference);
			deleteItem2(itemID, difference);
		} else {
			deleteItem2(itemID, item.getAmount());
		}
		tab.add(item);
		if (updateView) {
			resetTempItems();
			resetBank();
		}
		}catch(NullPointerException exception) {
			c.sendMessage("That item cant be banked rn. id: "+itemID+".", 255);
		}
		return true;

	}

	public boolean bankContains(int itemId) {
		for (BankTab tab : c.getBank().getBankTab())
			if (tab.contains(new BankItem(itemId + 1)))
				return true;
		return false;
	}

	public boolean bankContains(int itemId, int itemAmount) {
		for (BankTab tab : c.getBank().getBankTab()) {
			if (tab.containsAmount(new BankItem(itemId + 1, itemAmount))) {
				return true;
			}
		}
		return false;
	}

	public boolean isBankSpaceAvailable(BankItem item) {
		for (BankTab tab : c.getBank().getBankTab()) {
			if (tab.contains(item)) {
				return tab.spaceAvailable(item);
			}
		}
		return false;
	}

	public boolean removeFromAnyTabWithoutAdding(int itemId, int itemAmount, boolean updateView) {
		if (c.getPA().viewingOtherBank) {
			c.getPA().resetOtherBank();
		}
		BankTab tab = null;
		BankItem item = new BankItem(itemId + 1, itemAmount);
		for (BankTab searchedTab : c.getBank().getBankTab()) {
			if (searchedTab.contains(item)) {
				tab = searchedTab;
				break;
			}
		}
		if (tab == null) {
			return false;
		}
		if (itemAmount <= 0)
			return false;
		if (!tab.contains(item))
			return false;
		if (tab.getItemAmount(item) < itemAmount) {
			item.setAmount(tab.getItemAmount(item));
		}
		if (item.getAmount() < 0)
			item.setAmount(0);
		tab.remove(item);
		if (tab.size() == 0) {
			c.getBank().setCurrentBankTab(c.getBank().getBankTab(0));
		}
		if (updateView) {
			resetBank();
		}
		c.getItems().resetItems(5064);
		return true;
	}
	public void addItemToBank(int itemId, int amount) {
		BankTab tab = c.getBank().getCurrentBankTab();
		BankItem item = new BankItem(itemId + 1, amount);
		Iterator<BankTab> iterator = Arrays.asList(c.getBank().getBankTab()).iterator();
		while (iterator.hasNext()) {
			BankTab t = iterator.next();
			if (t != null && t.size() > 0) {
				Iterator<BankItem> iterator2 = t.getItems().iterator();
				while (iterator2.hasNext()) {
					BankItem i = iterator2.next();
					if (i.getId() == item.getId() && !isNotable(itemId)) {
						if (t.getTabId() != tab.getTabId()) {
							tab = t;
						}
					} else {
						if (isNotable(itemId) && i.getId() == item.getId() - 1) {
							item = new BankItem(itemId, amount);
							if (t.getTabId() != tab.getTabId()) {
								tab = t;
							}
						}
					}
				}
			}
		}
		if (isNotable(itemId))
			item = new BankItem(itemId, amount);
		if (tab.freeSlots() == 0) {
			c.sendMessage("The item has been dropped on the floor.");
			Server.itemHandler.createGroundItem(c, itemId, c.absX, c.absY, c.heightLevel, amount, c.index);
			return;
		}
		long totalAmount = ((long) tab.getItemAmount(item) + (long) item.getAmount());
		if (totalAmount >= Integer.MAX_VALUE) {
			c.sendMessage("The item has been dropped on the floor.");
			Server.itemHandler.createGroundItem(c, itemId, c.absX, c.absY, c.heightLevel, amount, c.index);
			return;
		}
		tab.add(item);
		resetTempItems();
		if (c.isBanking)
			resetBank();
		//c.sendMessage(getItemName(itemId) + " x" + item.getAmount() + " has been added to your bank.");
	}
	public void removeFromBank(int itemId, int itemAmount, boolean updateView) {
		BankTab tab = c.getBank().getCurrentBankTab();
		BankItem item = new BankItem(itemId + 1, itemAmount);
		boolean noted = false;
		if (!c.isBanking)
			return;
		if (c.getPA().viewingOtherBank) {
			c.getPA().resetOtherBank();
			return;
		}
		if (itemAmount <= 0)
			return;
		if (c.getBankPin().requiresUnlock()) {
			resetBank();
			c.getBankPin().open(2);
			return;
		}
		if (System.currentTimeMillis() - c.lastBankDeposit < 100)
			return;
		if (Server.getMultiplayerSessionListener().inAnySession(c)) {
			c.getPA().closeAllWindows();
			return;
		}
		if (!tab.contains(item))
			return;
		if (c.takeAsNote) {
			if (ItemDefinition.DEFINITIONS[itemId + 1].isNoted()) {
				noted = true;
			} else
				c.sendMessage("This item cannot be taken out as noted.");
		}
		if (freeSlots() == 0 && !playerHasItem(itemId)) {
			c.sendMessage("There is not enough space in your inventory.");
			return;
		}
		if (getItemAmount(itemId) == Integer.MAX_VALUE) {
			c.sendMessage("Your inventory is already holding the maximum amount of " + getItemName(itemId).toLowerCase()
					+ " possible.");
			return;
		}
		if (isStackable(item.getId() - 1) || noted) {
			long totalAmount = (long) getItemAmount(itemId) + (long) itemAmount;
			if (totalAmount > Integer.MAX_VALUE)
				item.setAmount(tab.getItemAmount(item) - getItemAmount(itemId));
		}
		if (tab.getItemAmount(item) < itemAmount) {
			item.setAmount(tab.getItemAmount(item));
		}
		if (!isStackable(item.getId() - 1) && !noted) {
			if (freeSlots() < item.getAmount())
				item.setAmount(freeSlots());
		}
		if (item.getAmount() < 0)
			item.setAmount(0);
		if (!noted)
			addItem(item.getId() - 1, item.getAmount());
		else
			addItem(item.getId(), item.getAmount());
		tab.remove(item);
		if (tab.size() == 0) {
			c.getBank().setCurrentBankTab(c.getBank().getBankTab(0));
		}
		if (updateView) {
			resetBank();
		}
		c.getItems().resetItems(5064);
	}

	public boolean addEquipmentToBank(int itemID, int slot, int amount, boolean updateView) {
		if (c.getPA().viewingOtherBank) {
			c.getPA().resetOtherBank();
			return false;
		}
		if (!c.isBanking)
			return false;
		if (c.playerEquipment[slot] != itemID || c.playerEquipmentN[slot] <= 0)
			return false;
		BankTab tab = c.getBank().getCurrentBankTab();
		BankItem item = new BankItem(itemID + 1, amount);
		Iterator<BankTab> iterator = Arrays.asList(c.getBank().getBankTab()).iterator();
		while (iterator.hasNext()) {
			BankTab t = iterator.next();
			if (t != null && t.size() > 0) {
				Iterator<BankItem> iterator2 = t.getItems().iterator();
				while (iterator2.hasNext()) {
					BankItem i = iterator2.next();
					if (i.getId() == item.getId() && !isNotable(itemID)) {
						if (t.getTabId() != tab.getTabId()) {
							tab = t;
							c.getBank().setCurrentBankTab(tab);
							resetBank();
						}
					} else {
						if (isNotable(itemID) && i.getId() == item.getId() - 1) {
							item = new BankItem(itemID, amount);
							if (t.getTabId() != tab.getTabId()) {
								tab = t;
								c.getBank().setCurrentBankTab(tab);
								resetBank();
							}
						}
					}
				}
			}
		}
		if (isNotable(itemID))
			item = new BankItem(itemID, amount);
		if (item.getAmount() > c.playerEquipmentN[slot])
			item.setAmount(c.playerEquipmentN[slot]);
		if (tab.getItemAmount(item) == Integer.MAX_VALUE) {
			c.sendMessage("Your bank is already holding the maximum amount of " + getItemName(itemID).toLowerCase()
					+ " possible.");
			return false;
		}
		if (tab.freeSlots() == 0 && !tab.contains(item)) {
			c.sendMessage("Your current bank tab is full.");
			return false;
		}
		long totalAmount = ((long) tab.getItemAmount(item) + (long) item.getAmount());
		if (totalAmount >= Integer.MAX_VALUE) {
			c.sendMessage("Your bank is already holding the maximum amount of this item.");
			return false;
		} else
			c.playerEquipmentN[slot] -= item.getAmount();
		if (c.playerEquipmentN[slot] <= 0) {
			c.playerEquipmentN[slot] = -1;
			c.playerEquipment[slot] = -1;
		}
		tab.add(item);
		if (updateView) {
			resetTempItems();
			resetBank();
			updateSlot(slot);
		}
		return true;
	}


    public boolean playerHasEquipped(int itemID) {
        itemID++;
        for (int i = 0; i < c.playerEquipment.length; i++) {
            if (c.playerEquipment[i] == itemID) {
                return true;
            }
        }
        return false;
    }
	public void addOrDropItem(int item, int amount) {
		if (isStackable(item) && hasFreeSlots(1)) {
			addItem(item, amount);
		} else if (!hasFreeSlots(amount) && !isStackable(item)) {
			Server.itemHandler.createGroundItem(c, item, c.absX, c.absY, c.heightLevel, amount, c.index);
			c.sendMessage("You have no inventory space, so the item(s) appear beneath you.");
		} else {
			addItem(item, amount);
		}
	}
	/**
	 * Pickup items from the ground.
	 **/

	public void createGroundItem(GroundItem item) {
		c.getOutStream().createFrame(85);
		c.getOutStream().writeByteC((item.getY() - 8 * c.mapRegionY));
		c.getOutStream().writeByteC((item.getX() - 8 * c.mapRegionX));
		c.getOutStream().createFrame(44);
		c.getOutStream().writeWordBigEndianA(item.getId());
		c.getOutStream().writeWord(item.getAmount());
		c.getOutStream().writeByte(0);
		c.flushOutStream();
	}

	public void removeGroundItem(GroundItem item) {
		c.getOutStream().createFrame(85);
		c.getOutStream().writeByteC((item.getY() - 8 * c.mapRegionY));
		c.getOutStream().writeByteC((item.getX() - 8 * c.mapRegionX));
		c.getOutStream().createFrame(156);
		c.getOutStream().writeByteS(0);
		c.getOutStream().writeWord(item.getId());
		c.flushOutStream();
	}
	public boolean updateInventory = false;

	public void updateInventory() {
		updateInventory = false;
		resetItems(3214);
	}
	/**
	 * Determines if the player is wearing any of the given items.
	 * 
	 * @param items the array of item id values.
	 * @return true if the player is wearing any of the optional items.
	 */
	public boolean isWearingAnyItem(int... items) {
		for (int equipmentId : c.playerEquipment) {
			for (int item : items) {
				if (equipmentId == item) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Gets all items.
	 * 
	 * @return array of items consisting of both inventory and equipment
	 */
	public List<GameItem> getAllItems() {
		List<GameItem> items = new ArrayList<>();

		for (int i = 0; i < c.playerItems.length; i++) {
			int id = c.playerItems[i] - 1;
			int amount = c.playerItemsN[i];
			if (id > 0 && amount > 0) {
				items.add(new GameItem(id, amount));
			}
		}

		for (int i = 0; i < c.playerEquipment.length; i++) {
			int id = c.playerEquipment[i];
			int amount = c.playerEquipmentN[i];
			if (id > 0 && amount > 0) {
				items.add(new GameItem(id, amount));
			}
		}

		return items;
	}
	public ArrayList<List<GameItem>> getItemsKeptOnDeath() {
		List<GameItem> items = getAllItems();
		LinkedList<GameItem> keptItems = new LinkedList<>();
		
		items.sort(new Comparator<GameItem>() {
			@Override
			public int compare(GameItem itemA, GameItem itemB) {
				int priceA = ItemDefinition.forId(itemA.getId()).getSpecialPrice();
				int priceB = ItemDefinition.forId(itemB.getId()).getSpecialPrice();
				return priceA < priceB ? 1 : priceA > priceB ? -1 : 0;
			}
		});

		int count = getKeptItemsCount();

		Iterator<GameItem> $it = items.iterator();
		while ($it.hasNext()) {
			GameItem item = $it.next();
			if (!ArrayUtils.contains(Config.ITEMS_KEPT_ON_DEATH, item.getId())) {
				continue;
			}

			keptItems.add(item);
			$it.remove();
		}
		
		$it = items.iterator();
		while ($it.hasNext()) {
			if (count <= 0) {
				break;
			}

			GameItem item = $it.next();
			keptItems.addFirst(new GameItem(item.getId(), 1));
			item.setAmount(item.getAmount() - 1);
			count--;
			
			if (item.getAmount() <= 0) {
				$it.remove();
			}
		}

		ArrayList<List<GameItem>> result = new ArrayList<>();
		result.add(items);
		result.add(keptItems);

		return result;
	}
	public int getKeptItemsCount() {
		int count = c.isSkulled ? 0 : 3;
		if (c.prayerActive[10]) {
			count++;
		}
		return count;
	}

	public int getTotalRiskedWorth() {
		long worth = 0;

		for (GameItem item : getItemsKeptOnDeath().get(0)) {
			worth += ItemDefinition.forId(item.getId()).getSpecialPrice() * item.getAmount();
		}

		return worth > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) worth;
	}
	public void dropItems(List<GameItem> items) {
		if (c.getRights().isOwner()) {
			return;
		}
		if (c.getRights().isDeveloper()) {
			return;
		}

		Client killer = c;
		if (c.getKiller() != null) {
			Client potentialKiller = (Client) PlayerHandler.getPlayer(c.getKiller());
			if (potentialKiller != null && !potentialKiller.ironman) {
				killer = potentialKiller;
			}
		}
		
		for (GameItem item : items) {
			//System.out.println("Testing...");
			Server.itemHandler.createGroundItem(killer, item.getId(), c.getX(), c.getY(), c.getHeight(),
					item.getAmount(), killer.getId());
		}
	}
	public boolean canAdd(int itemId, int amount) {
		if (amount < 0) {
			return false;
		}
		long count = getItemCount(itemId);
		ItemDefinition def = ItemDefinition.forId(itemId);
		if (def != null && def.isStackable()) {
			if (count > 0) {
				return (count + amount) < Integer.MAX_VALUE;
			}
			return freeSlots() > 0;
		}
		return freeSlots() >= amount;
	}
	public void deleteItemNoSave(int id, int slot, int amount) {
		if (id <= 0 || slot < 0) {
			return;
		}
		if (c.playerItems[slot] == (id + 1)) {
			if (c.playerItemsN[slot] > amount) {
				c.playerItemsN[slot] -= amount;
			} else {
				c.playerItemsN[slot] = 0;
				c.playerItems[slot] = 0;
			}
			resetItems(3214);
		}
	}
	/**
	 * Adds an item to the players inventory, bank, or drops it. It will do this under any circumstance so if it cannot be added to the inventory it will next try to send it to the
	 * bank and if it cannot, it will drop it.
	 * 
	 * @param itemId the item
	 * @param amount the amount of said item
	 */
	public void addItemUnderAnyCircumstance(int itemId, int amount) {
		if (!addItem(itemId, amount)) {
				Server.itemHandler.createGroundItem(c, itemId, c.getX(), c.getY(), c.heightLevel, amount);
				c.sendMessage("<col=DD5C3E>Your box has been dropped to the ground!");
				return;
			}
			sendItemToAnyTabOrDrop(new BankItem(itemId, amount), c.getX(), c.getY());
	}

	/**
	 * The x and y represents the possible x and y location of the dropped item if in fact it cannot be added to the bank.
	 */
	public void sendItemToAnyTabOrDrop(BankItem item, int x, int y) {
		item = new BankItem(item.getId() + 1, item.getAmount());
		if (Item.itemIsNote[item.getId()] && bankContains(item.getId() - 2)) {
			if (isBankSpaceAvailable(item)) {
				sendItemToAnyTab(item.getId() - 1, item.getAmount());
			} else {
				Server.itemHandler.createGroundItem(c, item.getId() - 1, x, y, c.heightLevel, item.getAmount());
			}
		} else {
			sendItemToAnyTab(item.getId() - 1, item.getAmount());
		}
	}
	public void sendItemToAnyTab(int itemId, int amount) {
		BankItem item = new BankItem(itemId, amount);
		for (BankTab tab : c.getBank().getBankTab()) {
			if (tab.freeSlots() > 0 || tab.contains(item)) {
				c.getBank().setCurrentBankTab(tab);
				addItemToBank(itemId, amount);
				return;
			}
		}
		addItemToBank(itemId, amount);
	}
	/**
	 * Determines if the player is wearing a specific item at a particular slot
	 * 
	 * @param itemId the item we're checking to see the player is wearing
	 * @param slot the slot the item should be detected in
	 * @return true if the item is being word
	 */
	public boolean isWearingItem(int itemId, int slot) {

		return slot>=0&&slot<=c.playerEquipment.length-1&&c.playerEquipment[slot]==itemId;
	}
	public boolean hasItemAmount(int i, int parseInt) {
		// TODO Auto-generated method stub
		return false;
	}
	public int freeEquipmentSlots() {
		int slots = 0;
		for (int i = 0; i < c.playerEquipment.length; i++) {
			if (c.playerEquipment[i] <= 0) {
				slots++;
			}
		}
		return slots;
	}

	public boolean isWearingItems() {
		return freeEquipmentSlots() < 14;
	}


}