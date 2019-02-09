package Ghreborn.model.items;

import Ghreborn.model.content.CrystalChest;
import Ghreborn.model.content.trails.MasterClue;
import Ghreborn.model.items.item_combinations.Godswords;
import Ghreborn.model.minigames.warriors_guild.AnimatedArmour;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.PlayerCannon;
import Ghreborn.model.players.Rights;
import Ghreborn.model.players.skills.Skill;
import Ghreborn.model.players.skills.Smelting;
import Ghreborn.model.players.skills.crafting.BraceletMaking;
import Ghreborn.model.players.skills.crafting.GemCutting;
import Ghreborn.model.players.skills.crafting.GlassBlowing;
import Ghreborn.model.players.skills.crafting.JewelryMaking;
import Ghreborn.model.players.skills.crafting.LeatherMaking;
import Ghreborn.model.players.skills.farming.Farming;
import Ghreborn.model.players.skills.firemake.Firemaking;
import Ghreborn.model.players.skills.fletching.BowStringing;
import Ghreborn.model.players.skills.fletching.Fletching;
import Ghreborn.model.players.skills.fletching.Arrow.Arrow;
import Ghreborn.model.players.skills.herblore.Crushable;
import Ghreborn.model.players.skills.herblore.PoisonedWeapon;
import Ghreborn.model.players.skills.herblore.PotionMixing;
import Ghreborn.model.players.skills.prayer.Bone;
import Ghreborn.model.players.skills.prayer.Prayer;
import Ghreborn.util.Misc;

import java.util.Optional;


import Ghreborn.Config;
import Ghreborn.Server;
import Ghreborn.core.PlayerHandler;
import Ghreborn.definitions.ItemCacheDefinition;
import Ghreborn.definitions.ObjectDef;

/**
 * 
 * @author Ryan / Lmctruck30
 * 
 */

public class UseItem {

	public static void ItemonObject(Client c, int objectID, int objectX,
			int objectY, int itemId) {
		if (!c.getItems().playerHasItem(itemId, 1))
			return;
		if (Farming.prepareCrop(c, itemId, objectID, objectX, objectY)) {
			if(c.goodDistance(c.getX(), c.getY(), objectX, objectY, 1)) {
			//this.stop();
			return;
		}
		}
		if (c.playerCannon != null && PlayerCannon.CannonPart.isObjCannon(objectID)) {
			if (c.playerCannon.addItemToCannon(itemId, objectID, objectX, objectY)) {
				PlayerHandler.updateCannon(c.playerCannon);
			}
		}
		if (itemId == 995 && ObjectDef.getObjectDef(objectID).name.contains("Bank")) {
			final int size = c.getItems().getItemAmount(995);
			int tokens = size / 1000;
			c.getItems().deleteItem2(995, size);
			c.getItems().addItem(13204, tokens);
			c.sendMessage("You exchange " + Misc.format(size) + " Coins for " + Misc.format(c.getItems().getItemAmount(13204)) + " Platinum tokens.");
		}
		if (itemId == 13204 && ObjectDef.getObjectDef(objectID).name.contains("Bank")) {
			final int size = c.getItems().getItemAmount(13204);
			c.getItems().deleteItem2(13204, size);
			c.getItems().addItem(995, size * 1000);
			c.sendMessage("You exchange " + Misc.format(size) + " Platinum tokens for " + Misc.format(c.getItems().getItemAmount(995)) + " Coins.");
		}
		ObjectDef def = ObjectDef.getObjectDef(objectID);

		if (def != null) {
			
			if (def.name != null && def.name.toLowerCase().contains("bank")) {
					//ItemDefinition definition = ItemDefinition.forId(itemId);
					boolean stackable = Item.itemStackable[itemId];
					if (stackable) {
						c.getOutStream().createFrame(27);
						c.unNoteItemId = itemId;
						c.settingUnnoteAmount = true;
					} else {
						c.getPA().noteItems(c, itemId);
				}
			}
		}
		switch (objectID) {
		case 16469:
		case 24009:
		case 2030: //Allows for ores to be used on the furnace instead of going though the interface.
			//if (itemId == )
			if (itemId == 19529) {
				if (c.getItems().playerHasItem(6571)) {
					c.getItems().deleteItem(19529, 1);
					c.getItems().deleteItem(6571, 1);
					c.getItems().addItem(19496, 1);
					c.sendMessage("You successfully bind the two parts together into an uncut zenyte.");
				} else {
					c.sendMessage("You need an uncut onyx to do this.");
					return;
				}
			} else if(!c.getItems().playerHasItem(11065)) {
				JewelryMaking.mouldInterface(c);
			} else if(c.getItems().playerHasItem(11065)) {
				BraceletMaking.craftBraceletDialogue(c, itemId);
			}
			String type = itemId == 438 ? "bronze" : itemId == 436 ? "bronze" : itemId == 440 ? "iron" : itemId == 442 ? "silver" : itemId == 453 ? "steel" : itemId == 444 ? "gold" : itemId == 447 ? "mithril" : itemId == 449 ? "adamant" : itemId == 451 ? "rune" : "";			
			c.getSmelting().startSmelting(c, type, "ALL", "FURNACE");
			
			break;
		case 2097:
			c.getSmithingInt().showSmithInterface(itemId);
			break;

		case 23955:
			AnimatedArmour.itemOnAnimator(c, itemId);
			break;
		case 409:
			Optional<Bone> bone = Prayer.isOperableBone(itemId);
			if (bone.isPresent()) {
				c.getPrayer().setAltarBone(bone);
				c.getOutStream().createFrame(27);
				return;
			}
			break;
		default:
			if (c.rights == Rights.OWNER)
				Misc.println("Player At Object id: " + objectID
						+ " with Item id: " + itemId);
			break;
		}

	}

	public static void ItemonItem(final Client c, final int itemUsed, final int useWith, final int itemUsedSlot,
			final int usedWithSlot) {
		if (itemUsed == -1 || useWith == -1)
			return;
		GameItem gameItemUsed = new GameItem(itemUsed, c.playerItemsN[itemUsedSlot], itemUsedSlot);
		GameItem gameItemUsedWith = new GameItem(useWith, c.playerItemsN[itemUsedSlot], usedWithSlot);
		//Fletching.resetFletching(c);
		//Arrow.initialize(c, itemUsed, useWith);
		c.getPA().resetVariables();
		//End
		if (itemUsed == 53 || useWith == 53) {
			int arrow = itemUsed == 53 ? useWith : itemUsed;
			c.getFletching().fletchArrow(arrow);
		}
		if (itemUsed == 19584 || useWith == 19584) {
			int javelin = itemUsed == 19584 ? useWith : itemUsed;
			c.getFletching().fletchJavelin(javelin);
		}
		if (itemUsed == 52 && useWith == 314 || itemUsed == 314 && useWith == 52) {
			c.getFletching().fletchHeadlessArrows();
		}
		if (itemUsed == 1777 || useWith == 1777) {
			int unstrung = itemUsed == 1777 ? useWith : itemUsed;
			c.getFletching().fletchUnstrung(unstrung);
		}
		if (itemUsed == 9438 || useWith == 9438) {
			int unstrung = itemUsed == 9438 ? useWith : itemUsed;
			c.getFletching().fletchUnstrungCross(unstrung);
		}
		if (itemUsed == 314 || useWith == 314) {
			int item = itemUsed == 314 ? useWith : itemUsed;
			c.getFletching().fletchUnfinishedBolt(item);
			c.getFletching().fletchDart(item);
		}
		if (itemUsed == 1755 || useWith == 1755) {
			c.getFletching().fletchGem(useWith, itemUsed);
			GemCutting.cutGem(c, itemUsed, useWith);
		}
		if (useWith == 946 || itemUsed == 946) {
			c.getFletching().combine(useWith, itemUsed);
		}
		if (itemUsed == 1775 || useWith == 1775) {
			if (!c.getItems().playerHasItem(1785)) {
				c.sendMessage("In order to do this you must have a glassblowing pipe.");
				return;
			}
			GlassBlowing.makeGlass(c, itemUsed, useWith);
		}
		if (itemUsed >= 11818 && itemUsed <= 11822 && useWith >= 11818 && useWith <= 11822) {
			if (c.getItems().hasAllShards()) {
				c.getItems().makeBlade();
			} else {
				c.sendMessage("You need to have all the shards to combine them into a blade.", 255);
			}
		}
		if (itemUsed == 12924 || useWith == 12924) {
			int ammo = itemUsed == 12924 ? useWith : itemUsed;
			ItemDefinition definition = ItemDefinition.forId(ammo);
			int amount = c.getItems().getItemAmount(ammo);
			if (ammo == 12934) {
				c.sendMessage("Select a dart to store and have the equivellent amount of scales.");
				return;
			}
			if (definition == null || !definition.getName().toLowerCase().contains("dart")) {
				c.sendMessage("That item cannot be equipped with the blowpipe.");
				return;
			}
			if (c.getToxicBlowpipeAmmo() > 0) {
				c.sendMessage("The blowpipe already has ammo, you need to unload it first.");
				return;
			}
			if (amount < 500) {
				c.sendMessage("You need 500 of this item to store it in the pipe.");
				return;
			}
			if (!c.getItems().playerHasItem(12934, amount)) {
				c.sendMessage("You need at least " + amount + " scales in combination with the " + definition.getName()
						+ " to charge this.");
				return;
			}
			if (!c.getItems().playerHasItem(12924)) {
				c.sendMessage("You need a toxic blowpipe (empty) to do this.");
				return;
			}
			if (amount > 16383) {
				c.sendMessage("The blowpipe can only store 16,383 charges at any given time.");
				amount = 16383;
			}
			c.getItems().deleteItem2(12924, 1);
			c.getItems().addItem(12926, 1);
			c.getItems().deleteItem2(ammo, amount);
			c.getItems().deleteItem2(12934, amount);
			c.setToxicBlowpipeCharge(amount);
			c.setToxicBlowpipeAmmo(ammo);
			c.setToxicBlowpipeAmmoAmount(amount);
			c.sendMessage("You store " + amount + " " + definition.getName()
					+ " into the blowpipe and charge it with scales.");
			return;
		}
		if (itemUsed == 12932 && useWith == 11907 || itemUsed == 11907 && useWith == 12932) {
			if (c.playerLevel[Skill.CRAFTING.getId()] < 59) {
				c.sendMessage("You need 59 crafting to do this.");
				return;
			}
			if (!c.getItems().playerHasItem(1755)) {
				c.sendMessage("You need a chisel to do this.");
				return;
			}
			if (c.getTridentCharge() > 0) {
				c.sendMessage("You cannot do this whilst your trident has charge.");
				return;
			}
			c.getItems().deleteItem2(itemUsed, 1);
			c.getItems().deleteItem2(useWith, 1);
			c.getItems().addItem(12899, 1);
			c.sendMessage("You attach the magic fang to the trident and create a trident of the swamp.");
			return;
		}
		if (itemUsed == 5733 || useWith == 5733) {
			c.sendMessage("Whee! " + c.getItems().getItemName(itemUsed == 5733 ? useWith : itemUsed) + " all gone!");
			c.getItems().deleteItem(itemUsed == 5733 ? useWith : itemUsed, 1);
		}
		if (itemUsed == 12932 && useWith == 11791 || itemUsed == 11791 && useWith == 12932) {
			if (c.playerLevel[Skill.CRAFTING.getId()] < 59) {
				c.sendMessage("You must have a Crafting level of 59 to do this.");
				return;
			}
			if (!c.getItems().playerHasItem(1755)) {
				c.sendMessage("You need a chisel to do this.");
				return;
			}
			c.getItems().deleteItem2(itemUsed, 1);
			c.getItems().deleteItem2(useWith, 1);
			c.getItems().addItem(12904, 1);
			c.sendMessage("You attach the magic fang to the staff of the dead and create a toxic staff of the dead.");
			return;
		}
		if (((itemUsed == 554 || itemUsed == 560 || itemUsed == 562) && (useWith == 12899 || useWith == 11907))
				|| ((useWith == 554 || useWith == 560 || useWith == 562) && (itemUsed == 12899 || itemUsed == 11907))) {
			int trident;
			if (itemUsed == 11907 || itemUsed == 12899) {
				trident = itemUsed;
			} else if (useWith == 11907 || useWith == 12899) {
				trident = useWith;
			} else {
				return;
			}
			if (!c.getItems().playerHasItem(995, 1000000) && trident == 11907) {
				c.sendMessage("You need at least 1M coins to add charge.");
				return;
			}
			if (!c.getItems().playerHasItem(12934, 2500) && trident == 12899) {
				c.sendMessage("You need 2500 zulrah scales to charge this.");
				return;
			}
			if (!c.getItems().playerHasItem(554, 1250)) {
				c.sendMessage("You need at least 1250 fire runes to add charge.");
				return;
			}
			if (!c.getItems().playerHasItem(560, 500)) {
				c.sendMessage("You need at least 500 death rune to add charge.");
				return;
			}
			if (!c.getItems().playerHasItem(562, 1000)) {
				c.sendMessage("You need at least 1000 chaos rune to add charge.");
				return;
			}
			if (c.getTridentCharge() >= 1000 && trident == 11907) {
				c.sendMessage("Your trident already has 1000 charge.");
				return;
			}
			if (c.getToxicTridentCharge() >= 1000 && trident == 12899) {
				c.sendMessage("Your trident already has 1000 charge.");
				return;
			}
			c.getItems().deleteItem2(554, 1250);
			c.getItems().deleteItem2(560, 1000);
			c.getItems().deleteItem2(562, 500);
			if (trident == 11907) {
				c.getItems().deleteItem2(995, 1000000);
				c.setTridentCharge(c.getTridentCharge() + 2500);
				c.animation(7137);
				c.gfx100(1250);

			} else {
				c.getItems().deleteItem2(12934, 2500);
				c.setToxicTridentCharge(c.getToxicTridentCharge() + 2500);
			}
			return;
		}
		if (itemUsed == 12927 && useWith == 1755 || itemUsed == 1755 && useWith == 12927) {
			int visage = itemUsed == 12927 ? itemUsed : useWith;
			if (c.playerLevel[Skill.CRAFTING.getId()] < 52) {
				c.sendMessage("You need a crafting level of 52 to do this.");
				return;
			}
			c.getItems().deleteItem2(visage, 1);
			c.getItems().addItem(12929, 1);
			c.sendMessage("You craft the serpentine visage into a serpentine helm (empty).");
			c.sendMessage("Charge the helm with 500 scales.");
			return;
		}
		if (itemUsed == 12929 && useWith == 12934 || itemUsed == 12934 && useWith == 12929) {
			if (!c.getItems().playerHasItem(12934, 500)) {
				c.sendMessage("You need 500 scales to do this.");
				return;
			}
			if (c.getSerpentineHelmCharge() > 0) {
				c.sendMessage("You must uncharge your current helm to re-charge.");
				return;
			}
			int amount = c.getItems().getItemAmount(12934);
			if (amount > 500) {
				amount = 500;
				c.sendMessage("The helm only required 500 zulrah scales to fully charge.");
			}
			c.getItems().deleteItem2(12934, amount);
			c.getItems().deleteItem2(12929, 1);
			c.getItems().addItem(12931, 1);
			c.setSerpentineHelmCharge(amount);
			c.sendMessage("You charge the serpentine helm for 500 zulrah scales.");
			return;
		}
		if (itemUsed == 13233 && useWith == 6739) {
			if (c.getItems().freeSlots() < 3) {
				c.sendMessage("You need atleast <col=0000FF>3 <col=000000>free slots.");
				return;
			}
			c.getItems().deleteItem(6739, 1);
			c.getItems().deleteItem(13233, 1);
			c.getItems().addItem(13241, 1);
			c.sendMessage("You combine your <col=0000FF>Dragon Axe <col=000000> and <col=0000FF>Smouldering Stone<col=000000>.");
		}
		if (itemUsed == 2347 && useWith == 5974 || itemUsed == 5974 && useWith == 2347) {
			c.getItems().deleteItem(5974, 1);
			c.getItems().addItem(5976, 1);
			c.sendMessage("You break the coconut with the hammer.");
			//c.sendMessage("You combine your <col=0000FF>Dragon Axe <col=000000> and <col=0000FF>Smouldering Stone<col=000000>.");
		}
		if (itemUsed == 229 && useWith == 5976 || itemUsed == 5976 && useWith == 229) {
			c.getItems().deleteItem(5976, 1);
			c.getItems().deleteItem(229, 1);
			c.getItems().addItem(5978, 1);
			c.getItems().addItem(5935, 1);
			c.sendMessage("You fill the vial with coconut milk.");
		}
		if (itemUsed == 13233 && useWith == 11920) {
			if (c.getItems().freeSlots() < 3) {
				c.sendMessage("You need atleast <col=0000FF>3 <col=000000>free slots.");
				return;
			}
			c.getItems().deleteItem(11920, 1);
			c.getItems().deleteItem(13233, 1);
			c.getItems().addItem(13243, 1);
			c.sendMessage("You combine your <col=0000FF>Dragon Pickaxe <col=000000> and <col=0000FF>Smouldering Stone<col=000000>.");
		}
		if (itemUsed == 12902 && useWith == 12934 || itemUsed == 12934 && useWith == 12902) {
			if (!c.getItems().playerHasItem(12934, 500)) {
				c.sendMessage("You need 500 scales to do this.");
				return;
			}
			if (c.staffOfDeadCharge > 0) {
				c.sendMessage("You must uncharge your current staff to re-charge.");
				return;
			}
			int amount = c.getItems().getItemAmount(12934);
			if (amount > 500) {
				amount = 500;
				c.sendMessage("The staff only required 500 zulrah scales to fully charge.");
			}
			c.getItems().deleteItem2(12934, amount);
			c.getItems().deleteItem2(12902, 1);
			c.getItems().addItem(12904, 1);
			c.staffOfDeadCharge = amount;
			c.animation(1720);
			c.sendMessage("You charge the toxic staff of the dead for 500 zulrah scales.");
			return;
		}
		if (itemUsed == 12924 || useWith == 12924) {
			int ammo = itemUsed == 12924 ? useWith : itemUsed;
			ItemDefinition definition = ItemDefinition.forId(ammo);
			int amount = c.getItems().getItemAmount(ammo);
			if (ammo == 12934) {
				c.sendMessage("Select a dart to store and have the equivellent amount of scales.");
				return;
			}
			if (definition == null || !definition.getName().toLowerCase().contains("dart")) {
				c.sendMessage("That item cannot be equipped with the blowpipe.");
				return;
			}
			if (c.getToxicBlowpipeAmmo() > 0) {
				c.sendMessage("The blowpipe already has ammo, you need to unload it first.");
				return;
			}
			if (amount < 500) {
				c.sendMessage("You need 500 of this item to store it in the pipe.");
				return;
			}
			if (!c.getItems().playerHasItem(12934, amount)) {
				c.sendMessage("You need at least " + amount + " scales in combination with the " + definition.getName()
						+ " to charge this.");
				return;
			}
			if (!c.getItems().playerHasItem(12924)) {
				c.sendMessage("You need a toxic blowpipe (empty) to do this.");
				return;
			}
			if (amount > 16383) {
				c.sendMessage("The blowpipe can only store 16,383 charges at any given time.");
				amount = 16383;
			}
			c.getItems().deleteItem2(12924, 1);
			c.getItems().addItem(12926, 1);
			c.getItems().deleteItem2(ammo, amount);
			c.getItems().deleteItem2(12934, amount);
			c.setToxicBlowpipeCharge(amount);
			c.setToxicBlowpipeAmmo(ammo);
			c.setToxicBlowpipeAmmoAmount(amount);
			c.sendMessage("You store " + amount + " " + definition.getName()
					+ " into the blowpipe and charge it with scales.");
			return;
		}
		if (itemUsed == 12922 && useWith == 1755 || itemUsed == 1755 && useWith == 12922) {
			if (c.playerLevel[Skill.FLETCHING.getId()] >= 53) {
				c.getItems().deleteItem2(12922, 1);
				c.getItems().addItem(12924, 1);
				c.getPA().addSkillXP(10000, Skill.FLETCHING.getId());
				c.sendMessage("You fletch the fang into a toxic blowpipe.");
			} else {
				c.sendMessage("You need a fletching level of 53 to do this.");
			}
			return;
		}
		if (c.getItems().isHilt(itemUsed) || c.getItems().isHilt(useWith)) {
			int hilt = c.getItems().isHilt(itemUsed) ? itemUsed : useWith;
			int blade = c.getItems().isHilt(itemUsed) ? useWith : itemUsed;
			if (blade == 11798) {
				Godswords.makeGodsword(c, hilt);
			}
		}
		if (itemUsed == 1733 || useWith == 1733) {
			LeatherMaking.craftLeatherDialogue(c, itemUsed, useWith);

		}
		if (itemUsed == 1759 || useWith == 1759) {
			JewelryMaking.stringAmulet(c, itemUsed, useWith);
		}
		if (useWith == 11941) {
			if (ItemDefinition.forId(itemUsed).isStackable() || ItemDefinition.forId(itemUsed).isNoted()) {
				c.getLoot().addItemToLootbag(itemUsed, c.getItems().getItemAmount(itemUsed));
			} else {
				c.getLoot().addItemToLootbag(itemUsed, 1);
			}
		}

		if (itemUsed >= 11710 && itemUsed <= 11714 && useWith >= 11710
				&& useWith <= 11714) {
			if (c.getItems().hasAllShards()) {
				c.getItems().makeBlade();
			}
		}
		if (PotionMixing.get().isPotion(gameItemUsed) && PotionMixing.get().isPotion(gameItemUsedWith)) {
			if (PotionMixing.get().matches(gameItemUsed, gameItemUsedWith)) {
				PotionMixing.get().mix(c, gameItemUsed, gameItemUsedWith);
			} else {
				c.sendMessage("You cannot combine two potions of different types.");
			}
			return;
		}
		if (PoisonedWeapon.poisonWeapon(c, itemUsed, useWith)) {
			return;
		}
		if (Crushable.crushIngredient(c, itemUsed, useWith)) {
			return;
		}
		if (itemUsed == 227 || useWith == 227) {
			GameItem item = new GameItem(itemUsed);
			if (c.getHerblore().makeUnfinishedPotion(c, item))
				return;
		}
		c.getHerblore().mix(useWith);
		if (itemUsed == 227 || useWith == 227) {
			int primary = itemUsed == 227 ? useWith : itemUsed;
			c.getHerblore().mix(primary);
			return;
		}
		if (itemUsed == 2368 && useWith == 2366 || itemUsed == 2366
				&& useWith == 2368) {
			c.getItems().deleteItem(2368, c.getItems().getItemSlot(2368), 1);
			c.getItems().deleteItem(2366, c.getItems().getItemSlot(2366), 1);
			c.getItems().addItem(1187, 1);
		}

		if (Firemaking.playerLogs(c, itemUsed, useWith)) {
			Firemaking.grabData(c, itemUsed, useWith);
		}
		switch (itemUsed) {
		case 985:
		case 987:
		CrystalChest.makeKey(c);
			break;
		
		default:
			if (c.rights == Rights.OWNER)
				Misc.println("Player used Item id: " + itemUsed
						+ " with Item id: " + useWith);
			break;
		}
	}

	public static void ItemonNpc(Client c, int itemId, int npcId, int slot) {
		switch(npcId) {
		
		case 7303:
			MasterClue.exchangeClue(c);
			break;
			
		case 412:
			break;
		
		}
		switch (itemId) {
		case 4162:
			
			break;
		default:
			if (c.rights == Rights.OWNER)
				Misc.println("Player used Item id: " + itemId
						+ " with Npc id: " + npcId + " With Slot : " + slot);
			break;
		}

	}

	public static boolean combines(int itemA, int itemB, int combinationA, int combinationB) {
		return (itemA == combinationA && itemB == combinationB) || (itemA == combinationB && itemB == combinationA);
	}

	public static void unNoteItems(Client c, int itemId, int amount) {
		ItemDefinition definition = ItemDefinition.forId(itemId);
		int counterpartId = Server.itemHandler.getCounterpart(itemId);
		
		/**
		 * If a player enters an amount which is greater than the amount of the item they have it will set it to the amount
		 * they currently have.
		 */
		int amountOfNotes = c.getItems().getItemAmount(itemId);
		if (amount > amountOfNotes) {
			amount = amountOfNotes;
		}
		
		/**
		 * Stops if you are trying to unnote an unnotable item
		 */
		if (counterpartId == -1) {
			c.sendMessage("You can only use unnotable items on this bank to un-note them.");
			return;
		}
		/**
		 * Stops if you do not have the item you are trying to unnote
		 */
		if (!c.getItems().playerHasItem(itemId, 1)) {
			return;
		}
		
		/**
		 * Preventing from unnoting more items that you have space available
		 */
		if (amount > c.getItems().freeSlots()) {
			amount = c.getItems().freeSlots();
		}
		
		/**
		 * Stops if you do not have any space available
		 */
		if (amount <= 0) {
			c.sendMessage("You need at least one free slot to do this.");
			return;
		}
		
		/**
		 * Deletes the noted item and adds the amount of unnoted items
		 */
		c.getItems().deleteItem2(itemId, amount);
		c.getItems().addItem(counterpartId, amount);
		c.getDH().sendStatement("You unnote x"+amount+" of " + definition.getName() + ".");
		c.settingUnnoteAmount = false;
		c.unNoteItemId = 0;
		return;
	}

}
