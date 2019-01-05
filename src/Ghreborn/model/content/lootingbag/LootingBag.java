package Ghreborn.model.content.lootingbag;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import Ghreborn.model.items.GameItem;
import Ghreborn.model.items.ItemDefinition;
import Ghreborn.model.players.Client;

public class LootingBag {
	
	private static int size = 28;
	
	// 11941
	
	private static Map<Integer, GameItem> items = new HashMap<>(size);
	
	public static void add(Client player, int itemUsedSlot, GameItem /*item*/ itemUsed) {
		if (itemUsedSlot > size) {
			throw new IllegalStateException("Range of slot exceeds size of container.");
		}
		ItemDefinition itemDef = ItemDefinition.forId(itemUsed.getId());
		if (itemDef == null) {
			return;
		}
		int id = itemUsed.getId();
		int amount = itemUsed.getAmount();
//	if (type.isEquipment()) {
		int equipmentSlot = itemDef.getEquipmentSlot();
			if (!itemDef.isWearable()) {
				player.sendMessage("This item is not wearable.");
			}
			if (amount > 1) {
				if (equipmentSlot != 13 && equipmentSlot != 3 || !itemDef.isStackable()) {
					amount = 1;
				}
			}
			if (equipmentSlot == 5 || equipmentSlot == 3) {
				int shieldSlot = items.containsKey(5) ? items.get(5).getId() : -1;
				int weaponSlot = items.containsKey(3) ? items.get(3).getId() : -1;
				if (shieldSlot != -1 && equipmentSlot == 3) {
					if (itemDef.isTwoHanded()) {
						player.sendMessage("You cannot add a 2 handed weapon whilst wearing a shield.");
						return;
					}
				}
				if (weaponSlot != -1 && equipmentSlot == 5) {
					ItemDefinition i = ItemDefinition.forId(weaponSlot);
					if (i == null) {
						return;
					}
					if (i.isTwoHanded()) {
						player.sendMessage("You cannot add a shield whilst wearing a 2 handed weapon.");
						return;
					}
				}
			}
		//}
		if (id < 0) {
			return;
		}
		if (amount < 1) {
			amount = 1;
		}
		boolean containsItem = items.values().stream().anyMatch(i -> i.getId() == id);
		if (itemUsed.isStackable() && containsItem) {
			Iterator<Entry<Integer, GameItem>> iterator = items.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<Integer, GameItem> entry = iterator.next();
				int currentSlot = entry.getKey();
				GameItem currentItem = entry.getValue();
				if (currentItem.getId() == id) {
					long total = ((long) currentItem.getAmount() + (long) amount);
					if (total > Integer.MAX_VALUE) {
						iterator.remove();
						entry.setValue(new GameItem(id, Integer.MAX_VALUE));
					} else {
						entry.setValue(new GameItem(id, currentItem.getAmount() + amount));
					}
					itemUsedSlot = currentSlot;
					break;
				}
			}
		} else if (itemUsed.isStackable() && !containsItem) {
			items.put(itemUsedSlot, new GameItem(id, amount));
		} else {
			if (items.containsKey(itemUsedSlot)) {
				items.remove(itemUsedSlot);
			}
			//if (amount > availableSlots()) {
			//	amount = availableSlots();
		//	}
			int initialSlot = itemUsedSlot;
			while (amount-- > 0) {
			//	if (items.containsKey(initialSlot)) {
			//		slot = selectOpenSlot();
			//	}
				if (itemUsedSlot == -1) {
					break;
				}
				items.put(itemUsedSlot, new GameItem(itemUsed.getId(), 1));
				//player.getPA().sendFrame34a(26700 + itemUsedSlot, itemUsed.getId(), 0, 1);
				player.getPA().sendFrame34a(26700, 861, 2, 3); //int item, int amount , int frame, int slot);
			}
			return;
		}
		//player.getPA().sendFrame34a(26700 + itemUsedSlot, itemUsed.getId(), 0, items.get(itemUsedSlot).getAmount());
		player.getPA().sendFrame34a(26700, 861, 2, 3);
	}
	
	/*public boolean submitItem(Client player , int slot)
	 {
	  if (items.size() >= 28) {
	   player.sendMessage("Bag is full.");
	   return false;
	  }
	  int id = player.playerItems[slot];
	  int amt = player.playerItemsN[slot];
	  
	 // items.add(new Item(id, amt));
	  player.playerItems[slot] = 0;
	  player.playerItemsN[slot] = 0;
	  System.out.println("Depositted item: " + id);
	  //player.resetKeepItems();
	  return true;
	
	 }*/
	
	/*if(useWith == 11941)
	  {
	   //c.lootBag.submitItem(itemUsedSlot);
	   return;
	  }*/
}