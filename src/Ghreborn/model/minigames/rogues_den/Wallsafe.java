package Ghreborn.model.minigames.rogues_den;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Ghreborn.Config;
import Ghreborn.event.CycleEvent;
import Ghreborn.event.CycleEventContainer;
import Ghreborn.event.CycleEventHandler;
import Ghreborn.model.items.GameItem;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.combat.Hitmark;
import Ghreborn.util.Misc;

public class Wallsafe extends CycleEvent {

	
	private Client player;
	
	public Wallsafe(Client player){
		this.player = player;
	}

	/**
	 * A map containing a List of {@link GameItem}'s that contain items relevant to their rarity.
	 */
	private static Map<Rarity, List<GameItem>> items = new HashMap<>();
	static {
		items.put(Rarity.COMMON,
				Arrays.asList(
						new GameItem(995, 20), 
						new GameItem(995, 40),
						new GameItem(1623))
				);

		items.put(Rarity.UNCOMMON, 
				Arrays.asList(
				new GameItem(1619), 
				new GameItem(1621))
				);
		items.put(Rarity.RARE, 
				Arrays.asList(
						new GameItem(1617))
				);
	}
	/**
	 * Represents the rarity of a certain list of items
	 */
	enum Rarity {
		UNCOMMON, COMMON, RARE
	}
	public static int chance(Client player) {
		return (Misc.random((int) Math.floor(player.playerLevel[17] / 10) + 1));
	}
	public static int timer(Client player) {
		if (player.getItems().playerHasItem(5560)) {
			return (10 - (int) Math.floor(player.playerLevel[17] / 10) + Misc.random(5));
		} else {
			return (10 - (int) Math.floor(player.playerLevel[17] / 10) + Misc.random(11)) + 20;
		}
	}
	public boolean can(Client player) {
		if (player.isCracking) {
			player.sendMessage("You are currently cracking a safe!");
			return false;
		}
		if (player.playerLevel[17] < 50) {
			player.sendMessage("You need a thieving level atleast 50 to crack safes!");
			return false;
		}
		if (player.getItems().freeSlots() < 1) {
			player.sendMessage("You do not have any space left in your inventory.");
			return false;
		}
		return true;
	}
	public void crack(Client player) {
		if (!can(player) || System.currentTimeMillis() - player.lastMysteryBox < 1000) {
			return;
		}
		player.isCracking = true;
		player.sendMessage("You attempt to crack the safe... ");
		player.animation(881);
		player.lastMysteryBox = System.currentTimeMillis();
		CycleEventHandler.getSingleton().stopEvents(this);
		CycleEventHandler.getSingleton().addEvent(this, this, timer(player));
	}

	@Override
	public void execute(CycleEventContainer container) {
		int random = Misc.random(10);
		List<GameItem> itemList = random < 5 ? items.get(Rarity.COMMON) : random >= 5 && random <= 8 ? items.get(Rarity.UNCOMMON) : items.get(Rarity.RARE);
		GameItem item = Misc.getRandomItem(itemList);
		if (chance(player) == 0) {
			player.sendMessage("You slip and trigger a trap!");
			if (player.playerLevel[17] == 99) {
				player.appendDamage(1, Hitmark.HIT);
			} else if (player.playerLevel[17] > 79) {
				player.appendDamage(2, Hitmark.HIT);
			} else if (player.playerLevel[17] > 49) {
				player.appendDamage(3, Hitmark.HIT);
			} else {
				player.appendDamage(4, Hitmark.HIT);
			}
			container.stop();
			player.animation(404);
			player.isCracking = false;
			return;
		}
		player.sendMessage("You get some loot.");
		player.getItems().addItem(item.getId(), item.getAmount());
		player.getPA().addSkillXP(100 * Config.THIEVING_EXPERIENCE, 17);
		player.isCracking = false;
		container.stop();
	}
		
}
