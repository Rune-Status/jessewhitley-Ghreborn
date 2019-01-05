package Ghreborn.model.players.skills.hunter;

import java.util.HashMap;
import java.util.Random;

import Ghreborn.Config;
import Ghreborn.Server;
import Ghreborn.event.CycleEvent;
import Ghreborn.event.CycleEventContainer;
import Ghreborn.event.CycleEventHandler;
import Ghreborn.model.npcs.NPC;
import Ghreborn.model.npcs.NPCHandler;
import Ghreborn.model.players.Client;
import Ghreborn.util.Misc;

public class Butterflys {
/**
 * made by sgsrocks on rune-server
 * 
 * 
 * 
 */
	public static final Random random = new Random();

	public enum Flys {
		RUBY_HARVEST(5556, 15, 24, 10020),
		SAPPHIRE_GLACLALIS(5555, 25, 34, 10018),
		SNOWY_KNIGHT(5554, 35, 44, 10016),
		BLACK_WARLOCK(5553, 45, 54, 10014);
		
	public static HashMap<Integer, Flys> Butterflys = new HashMap<>();

	static {
		for(Flys t : Flys.values()) {
			Butterflys.put(t.npc, t);
		}
	}
	private int npc;
	private int levelRequired;
	private int experience;
	private int itemId;

	 Flys(final int npc, final int levelRequired, final int experience, final int itemId) {
		this.npc = npc;
		this.levelRequired = levelRequired;
		this.experience = experience;
		this.itemId = itemId;
	}
	public int getFlysId() {
		return npc;
	}
	public int getLevelRequired() {
		return levelRequired;
	}
	public int getXp() {
		return experience;
	}
	public int getJar() {
		return itemId;
	}
	public static Flys forId(int id) {
		return Butterflys.get(id);
	}
	}
	public static void catchButterfly(Client c, int npcType, final int npcidx) {
		
		if(c.playerSkilling[c.playerHunter]) {
		return;
		}
		Flys t = Flys.Butterflys.get(npcType);

		if(c.playerEquipment[c.playerWeapon] != 10010 && c.playerEquipment[c.playerWeapon] != 11259 ) {
			c.sendMessage("You need a butterfly net");
			return;
		}

		if(c.playerLevel[c.playerHunter] < t.getLevelRequired()) {
			c.sendMessage("You need a hunter of " + t.getLevelRequired() + " To catch this butterfly");
			return;
		}

		if(!c.getItems().playerHasItem(10012)) {
			c.sendMessage("You need a Butterfly jar");
			return;
		}
		c.playerSkilling[c.playerHunter] = true;
		if(Misc.random(25) == 0) {
			c.animation(6605);
			c.sendMessage("You failed to catch the Butterfly");
		} else {
			c.animation(6605);
			c.getItems().addItem(t.getJar(), 1);
			c.getItems().deleteItem(10012, c.getItems().getItemSlot(10012), 1);
			c.getPA().addSkillXP(t.getXp() * Config.HUNTER_EXPERIENCE, c.playerHunter);
			c.sendMessage("You catch the Butterfly ");
			ButterflyDeath(c, npcType, npcidx);
		}
		c.playerSkilling[c.playerHunter] = false;

	}
	public static void respawnButterfly(Client c, final int id, final int x, final int y, final int z) {
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
			
			int i = 0;
			@Override
			public void execute(CycleEventContainer container) {
				if(i++ == 1) {
					Server.npcHandler.newNPC(id, x, y, z, 1, 0, 0, 0, 0);
					container.stop();
				}
			}

			@Override
			public void stop() {
			}
		}, 100);
	}
	public static void ButterflyDeath(Client c, int npcType, int idx) {
		Flys t = Flys.Butterflys.get(npcType);
		NPC n = NPCHandler.npcs[idx];
		if(n != null && n.npcType == t.getFlysId()) {
				int x = n.absX;
				int y = n.absY;
				int z = n.heightLevel;
				n.absX = 0;
				n.absY = 0;
				n.isDead = true;
				NPCHandler.npcs[idx] = null;
				respawnButterfly(c, npcType, x, y, z);
		}
	}
}
