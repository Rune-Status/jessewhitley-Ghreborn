package Ghreborn.model.npcs;

import Ghreborn.Server;
import Ghreborn.clip.Region;
import Ghreborn.model.players.Client;

/**
 *
 * @author DF
 *
 **/

public class PetHandler {

	public static final int RATS_NEEDED_TO_GROW = 25;

	private static enum Pets {

		GRAARDOR(12650, 6644), 
		KREE(12649, 6643), 
		ZILYANA(12651, 6633), 
		TSUROTH(12652, 6634), 
		PRIME(12644, 6629), 
		REX(12645, 6641), 
		SUPREME(12643, 6628), 
		CHAOS(11995, 2055), 
		KBD(12653, 6636), 
		KRAKEN(12655, 6640), 
		CALLISTO(13178, 5558),
		MOLE(12646, 6651), 
		ZULRAH_GREEN(12921, 2127), 
		ZULRAH_RED(12939, 2128), 
		ZULRAH_BLUE(12940, 2129), 
		KAL_PRINCESS(12654, 6637), 
		VETION(13179, 5560), 
		VENENATIS(13177, 5557),
		SMOKE_DEV(12648, 6655),
		SCORPIA(13181, 5547),
		CORP(12816, 318),
		HERON(13320, 6715),
		TZREK_JAD(13225, 5892),
		ROCK_GOLEM(13321, 7439),
		ROCK_GOLEM_TIN(21187, 7440),
		ROCK_GOLEM_COPPER(21188, 7441),
		ROCK_GOLEM_IRON(21189, 7442),
		ROCK_GOLEM_COAL(21192, 7445),
		ROCK_GOLEM_GOLD(21193, 7446),
		ROCK_GOLEM_MITHRIL(21194, 7447),
		ROCK_GOLEM_ADAMANT(21196, 7449),
		ROCK_GOLEM_RUNE(21197, 7450),
		BEAVER(13322, 6717),
		BABY_CHINCHOMPA(13323, 6718),
		BABY_CHINCHOMPA1(13324, 6719),
		BABY_CHINCHOMPA2(13325, 6720),
		BABY_CHINCHOMPA3(13326, 6721),
		HELLPUPPY(13247, 3099),
		FIRE_RIFT_GAURDIAN(20665, 7337),
		AIR_RIFT_GUARDIAN(20665, 7338),
		MIND_RIFT_GUARDIAN(20669, 7339),
		WATER_RIFT_GUARDIAN(20671, 7340),
		EARTH_RIFT_GUARDIAN(20673, 7341),
		BODY_RIFT_GUARDIAN(20675, 7342),
		COSMIC_RIFT_GUARDIAN(20677,7343),
		CHAOS_RIFT_GUARDIAN(20679, 7344),
		NATURE_RIFT_GUARDIAN(20681, 7345),
		LAW_RIFT_GUARDIAN(20683, 7346),
		DEATH_RIFT_GUARDIAN(20685, 7347),
		SOUL_RIFT_GUARDIAN(20687, 7348),
		ASTRAL_RIFT_GUARDIAN(20689, 7349),
		BLOOD_RIFT_GUARDIAN(20691, 7350),
		WRATH_RIFT_GUARDIAN(21990, 8024),
		ABYSSAL_ORPHAN(13262, 5883),
		NOON(21748, 7891),
		MIDNIGHT(21750, 7890),
		JALNIBREK(21291, 7674),
		HERBI(21509, 7759),
		OLMLET(20851, 7519),
		SKOTOS(21273, 7671),
		VORKI(21992, 8025);

		private int itemId, npcId;
		private Pets(int itemId, int npcId) {
			this.itemId = itemId;
			this.npcId = npcId;
		}
	}

	public static Pets forItem(int id) {
		for (Pets t : Pets.values()) {
			if (t.itemId == id) {
				return t;
			}
		}
		return null;
	}

	public static Pets forNpc(int id) {
		for (Pets t : Pets.values()) {
			if (t.npcId == id) {
				return t;
			}
		}
		return null;
	}
	
	public static int getItemIdForNpcId(int npcId) {
		return forNpc(npcId).itemId;
	}
	public static void ownerDeath(Client c) {
		if (!c.insure) {
			c.summonId = -1;
			c.hasNpc = false;
			c.sendMessage("@blu@You have died and lost your pet, you should've had pet insurance!");
		} else
		if (c.insure) {
			c.getItems().addItemToBank(c.spawnId, 1);
			c.spawnId = -1;
			c.summonId = -1;
			c.hasNpc = false;
			c.sendMessage("@blu@Your pet was insured and was added to your bank!");
		}
	}
	public static boolean spawnPet(Client c, int itemId, int slot, boolean ignore) {
		Pets pets = forItem(itemId);
		if(pets != null) {
			int npcId = pets.npcId;
			if(c.hasNpc && !ignore) {
				c.sendMessage("You already have a follower!");
				return true;
			}
			int offsetX = 0;
			int offsetY = 0;
			if (Region.getClipping(c.getX() - 1, c.getY(), c.heightLevel, -1, 0)) {
				offsetX = -1;
			} else if (Region.getClipping(c.getX() + 1, c.getY(), c.heightLevel, 1, 0)) {
				offsetX = 1;
			} else if (Region.getClipping(c.getX(), c.getY() - 1, c.heightLevel, 0, -1)) {
				offsetY = -1;
			} else if (Region.getClipping(c.getX(), c.getY() + 1, c.heightLevel, 0, 1)) {
				offsetY = 1;
			}
			Server.npcHandler.spawnNpc3(c, npcId, c.absX+offsetX, c.absY+offsetY, c.heightLevel, 0, 120, 25, 200, 200, true, false, true);
			c.getPA().followPlayer();
			if(!ignore) {
				c.getItems().deleteItem(itemId, slot, c.playerItemsN[slot]);
				c.hasNpc = true;
				c.summonId = itemId;
				//c.SaveGame();
			}
			return true;
		} else {
			return false;
		}
	}

	public static boolean callfollower(Client c, int npcId){
		Pets pets = forNpc(npcId);
		if(pets != null){
			int offsetX = 0;
			int offsetY = 0;
			if (Region.getClipping(c.getX() - 1, c.getY(), c.heightLevel, -1, 0)) {
				offsetX = -1;
			} else if (Region.getClipping(c.getX() + 1, c.getY(), c.heightLevel, 1, 0)) {
				offsetX = 1;
			} else if (Region.getClipping(c.getX(), c.getY() - 1, c.heightLevel, 0, -1)) {
				offsetY = -1;
			} else if (Region.getClipping(c.getX(), c.getY() + 1, c.heightLevel, 0, 1)) {
				offsetY = 1;
			}
			if(NPCHandler.npcs[c.rememberNpcIndex].spawnedBy == c.index && c.summonId == pets.itemId){
				NPCHandler.npcs[c.rememberNpcIndex].absX =  c.absX+offsetX;
				NPCHandler.npcs[c.rememberNpcIndex].absY = c.absY+offsetY;
				NPCHandler.npcs[c.rememberNpcIndex].heightLevel = c.heightLevel;
				c.sendMessage("You call your follower.");
			}else{
				c.sendMessage("You dont have a follower right now.");
			}
			return true;
		}else{
		return false;
		}
	}

	public static boolean pickupPet(Client c, int npcId) {
		Pets pets = forNpc(npcId);
		if(pets != null) {
			if(NPCHandler.npcs[c.rememberNpcIndex].spawnedBy == c.index && c.summonId == pets.itemId) {
				int itemId = pets.itemId;
				if(c.getItems().freeSlots() > 0) {
					NPCHandler.npcs[c.rememberNpcIndex].absX = 0;
					NPCHandler.npcs[c.rememberNpcIndex].absY = 0;
					NPCHandler.npcs[c.rememberNpcIndex] = null;
					c.animation(827);
					c.getItems().addItem(itemId, 1);
					c.summonId = -1;
					c.hasNpc = false;
					c.sendMessage("You pick up your pet.");
				} else {
					c.sendMessage("You do not have enough inventory space to do this.");
				}
			} else {
				c.sendMessage("This is not your pet.");
			}
			return true;
		} else {
			return false;
		}
	}



}