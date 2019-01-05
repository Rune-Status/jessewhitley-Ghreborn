package Ghreborn.model.npcs.drops;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import Ghreborn.Server;
import Ghreborn.definitions.NPCCacheDefinition;
import Ghreborn.model.items.GameItem;
import Ghreborn.model.items.ItemAssistant;
import Ghreborn.model.items.ItemDefinition;
import Ghreborn.model.npcs.NPCHandler;
import Ghreborn.model.npcs.NpcDefinition;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.Rights;
import Ghreborn.util.Chance;
import Ghreborn.util.Misc;


public class DropList {

	private static final ArrayList<Chance> chances = new ArrayList<>();
	private static Map<List<Integer>, TableGroup> groups = new HashMap<>();
	private static List<Integer> ordered = new ArrayList<>();


	static {
		for (final Chance chance : Chance.values())
			chances.add(chance);
		Collections.reverse(chances);
	}
	
	/**
	 * Sets text colour
	 * @param chance
	 * @return
	 */

	private static String getColour(Chance chance) {
		switch (chance) {
		case RARE:
			return "<col=DD5C3E>";
		case VERY_RARE:
			return "<col=ff3000>";
		case EXTREMELY_RARE:
			return "<col=8B0000>";
		case UNCOMMON:
			return "<col=ffff00>";
		case COMMON:
			return "<col=c0ff00>";
		case VERY_UNCOMMON:
			return "<col=ffff00>";
		default:
			return "<col=3CB71E>";

		}
	}
	
	/**
	 * Displays specific NPC Drops on a interface
	 * @param player
	 * @param query
	 * @return
	 */

	public static boolean displayNPCDrops(Client player, String query) {
		if (player == null || query.replaceAll(" ", "").equals(""))
			return false;
		query = query.substring(0, 1).toUpperCase() + query.substring(1);
		final ArrayList<Integer> npcIds = new ArrayList<>();
		for (int i = 0; i < NPCCacheDefinition.newTotalNpcs; i++) {
			final NPCCacheDefinition def = NPCCacheDefinition.forID(i);
			if (def == null || def.getName() == null || def.getName().replaceAll(" ", "").equals(""))
				continue;
			if (def.getName().toLowerCase().equalsIgnoreCase(query))
				npcIds.add(i);
		}
		if (npcIds.isEmpty()) {
			player.sendMessage("No drops were found for the query: " + query + ".");
			return false;
		}
		final ArrayList<NpcDrop> drops = new ArrayList<>();
		for (final int id : npcIds) {
			final NpcDropTable table = NpcDropManager.TABLES.get(id);
			if (table == null)
				continue;
			final String name = NPCCacheDefinition.forID(id).getName();
			if (name == null || name.replaceAll(" ", "").equals(""))
				continue;
			if (table.getUnique() == null || table.getUnique().length == 0)
				continue;
			for (int i = 0; i < table.getUnique().length; i++) {
				if (table.getUnique()[i] != null)
					drops.add(table.getUnique()[i]);
			}
		}
		if (drops.isEmpty()) {
			player.sendMessage("No drops were found for the query: " + query + ".");
			return false;
		}

		final int capacity = 8195;
		for (int i = 8144; i < capacity; i++) {
			player.getPA().sendFrame126("", i);
		}
		int frame = 8144;
		player.getPA().sendFrame126("" + query + "", frame++);
		final ArrayList<Key<Chance, ArrayList<NpcDrop>>> dropsToDisplay = new ArrayList<>();
		chances.stream().filter(Objects::nonNull).forEach(chance -> {
			final ArrayList<NpcDrop> values = new ArrayList<>();
			drops.stream().filter(Objects::nonNull).filter(drop -> drop.getChance().ordinal() == chance.ordinal())
					.forEach(values::add);
			if (!values.isEmpty()) {
				dropsToDisplay.add(new Key<Chance, ArrayList<NpcDrop>>(chance, values));
			}
		});
		frame += 2;

		for (final Key<Chance, ArrayList<NpcDrop>> value : dropsToDisplay) {
			final Chance chance = value.key;
			int range = chance.getDenominator() + 1 - chance.getNumerator();
if(chance.ordinal() == Chance.LEGNEDLY_RARE.ordinal()) {
	range = chance.getDenominator() - 111 - chance.getNumerator();
}
			if (chance.ordinal() == Chance.VERY_RARE.ordinal()) {
				range = chance.getDenominator() - 269 - chance.getNumerator();
			}
			if (chance.ordinal() == Chance.EXTREMELY_RARE.ordinal()) {
				range = chance.getDenominator() - 334 - chance.getNumerator();
			}
			if (chance.ordinal() == Chance.RARE.ordinal()) {
				range = chance.getDenominator() - 195 - chance.getNumerator();
			}
			if (chance.ordinal() == Chance.ALWAYS.ordinal())
				range = 1;
			/**
			 * Remove what is below to get ACTUAL rarity rates (static)
			 */

			String colour = getColour(chance);

			player.getPA().sendFrame126("Rarity - " + colour.toString() + " " + chance.toString() + " <col=000000>[Ratio "
					+ colour.toString() + "1:" + range + "<col=000000>]", frame++);
			if (frame >= capacity) {
				break;
			}
			for (final NpcDrop drop : value.value) {
				final ItemDefinition defs = ItemDefinition.DEFINITIONS[drop.getId()];
				if (defs == null || defs.getName() == null || defs.getName().equalsIgnoreCase("null")) {
					continue;
				}
				final String message = "Drop: " + colour.toString() + "" + defs.getName() + " <col=000000>- Min: "
						+ colour.toString() + "" + drop.getMinimum() + ", <col=000000>Max: " + colour.toString() + ""
						+ drop.getMaximum() + ".";
				player.getPA().sendFrame126(message, frame++);
				if (frame >= capacity) {
					break;
				}
			}
			player.getPA().sendFrame126("", frame++);
		}
		player.getPA().showInterface(8134);
		return true;
	}
	public void clear(Client player) {
		for(int i = 0; i < 150; i++) {
			player.getPA().sendFrame126("", 33008 + i);
		}
		
		player.getPA().sendFrame126("", 43110);
		player.getPA().sendFrame126("", 43111);
		player.getPA().sendFrame126("", 43112);
		player.getPA().sendFrame126("", 43113);
		
		for(int i = 0;i<80;i++){
			player.getPA().itemOnInterface(-1, 0, 34010+i, 0);
			player.getPA().sendString("", 34200+i);
			player.getPA().sendString("", 34300+i);
			player.getPA().sendString("", 34100+i);
			player.getPA().sendString("", 34400+i);
		}
		player.searchList.clear();
	}
	private double getModifier(Client player) {
		double modifier = 1.0;
/*		if(player.getMode().isOsrs()){
			modifier -=.15;
		}*/
		if (player.getItems().isWearingItem(2572)) {
			modifier -= .03;
		} else if (player.getItems().isWearingItem(12785)) {
			modifier -= .05;
		}
		if (player.getRights().isVIP()) {
			modifier -= 0.150;
		} else if (player.getRights().isExtremeDonator()) {
			modifier -= 0.120;
		} else if (player.getRights().isSuperDonater()) {
			modifier -= 0.100;
		} else if (player.getRights().isDonator()) {
			modifier -= 0.100;
/*		} else if (player.getRights().contains(Rights.SUPPORTER)) {
			modifier -= 0.070;
		} else if (player.getRights().contains(Rights.SPONSOR)) {
			modifier -= 0.05;
		} else if (player.getRights().contains(Rights.CONTRIBUTOR)) {
			modifier -= 0.030;*/
		}
		return modifier;
	}
	/**
	 * Searchers after the player inputs a npc name
	 * @param player
	 * @param name
	 */
	public void search(Client player, String name) {
		if(name.matches("^(?=.*[A-Z])(?=.*[0-9])[A-Z0-9]+$")) {
			player.sendMessage("You may not search for alphabetical and numerical combinations.");
			return;
		}
		if (System.currentTimeMillis() - player.lastDropTableSearch < TimeUnit.SECONDS.toMillis(5)) {
			player.sendMessage("You can only do this once every 5 seconds.");
			return;
		}
		player.lastDropTableSearch = System.currentTimeMillis();
		
		clear(player);

		List<Integer> definitions = ordered.stream().filter(Objects::nonNull).filter(def -> NpcDefinition.get(def).getName() != null).filter(def -> NpcDefinition.get(def).getName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toList());

		if(definitions.isEmpty()) {
			definitions = ordered.stream().filter(Objects::nonNull).collect(Collectors.toList());
			List<Integer> npcs = new ArrayList<>();
			int count = 0;
			for(Integer index : definitions) {
				Optional<TableGroup> group = groups.values().stream().filter(g -> g.getNpcIds().contains(NpcDefinition.get(index).getId())).findFirst();
				if(group.isPresent()) {
					TableGroup g = group.get();
					
					for(TablePolicy policy : TablePolicy.values()) {
						Optional<Table> table = g.stream().filter(t -> t.getPolicy() == policy).findFirst();
						if(table.isPresent()) {
							for(NpcDrop drop : table.get()) {
								if(drop == null) {
									continue;
								}
								
								if(ItemAssistant.getItemName(drop.getId()).toLowerCase().contains(name.toLowerCase())) {
									npcs.add(index);
									player.getPA().sendFrame126(StringUtils.capitalize(NpcDefinition.get(NpcDefinition.get(index).getId()).getName().toLowerCase().replaceAll("_", " ")), 33008 + count);
									count++;
								}
							}
						}
					}
				}

			}
			
			player.searchList = npcs;
			return;
			
		}
		
		for(int index = 0; index < definitions.size(); index++) {
			if(index >= 150) {
				break;
			}
			player.getPA().sendFrame126(StringUtils.capitalize(NpcDefinition.get(definitions.get(index)).getName().toLowerCase().replaceAll("_", " ")), 33008 + index);
		}

		player.searchList = definitions;
	}

	/**
	 * Loads the selected npc choosen by the player to view their drops
	 * @param player
	 * @param button
	 */
	public void select(Client player, int button) {
		int listIndex;
		
		//So the idiot client dev didn't organize the buttons in a singulatiry order. So i had to shift around the id's
		//so if you have 50 npcs in the search you can click them all fine
		if(button <= 128255) {
			listIndex = button - 128240;
		} else {
			listIndex = (128255 - 128240) + 1 + button - 129000;
		}
		
		if (listIndex < 0 || listIndex > ordered.size() - 1) {
			return;
		}

		//Finding NPC ID
		int npcId = player.searchList.isEmpty() ? ordered.get(listIndex) : player.searchList.get(listIndex);
		
		Optional<TableGroup> group = groups.values().stream().filter(g -> g.getNpcIds().contains(npcId)).findFirst();

		//If the group in the search area contains this NPC
		group.ifPresent(g -> {
			if (System.currentTimeMillis() - player.lastDropTableSelected < TimeUnit.SECONDS.toMillis(5)) {
				player.sendMessage("You can only do this once every 5 seconds.");
				return;
			}

			//Loads the definition and maxhit/aggressiveness to display
			NpcDefinition npcDef = NpcDefinition.get(npcId);
			
			player.getPA().sendFrame126("Health: @whi@" + npcDef.getHitpoints(), 43110);
			player.getPA().sendFrame126("Combat Level: @whi@" + npcDef.getHitpoints(), 43111);
			if(NPCHandler.getNpc(npcId) != null){
				player.getPA().sendFrame126("Max Hit: @whi@" + NPCHandler.getNpc(npcId).maxHit, 43112);
			} else {
				player.getPA().sendFrame126("Max Hit: @whi@?", 43112);
			}
			player.getPA().sendFrame126("Aggressive: @whi@" + (Server.npcHandler.isAggressive(npcId, true) ? "true" : "false"), 43113);
			
			player.lastDropTableSelected = System.currentTimeMillis();
			
			double modifier = getModifier(player);
			
			//Iterates through all 5 drop table's (Found in TablePolicy -> Enum)
			for (TablePolicy policy : TablePolicy.POLICIES) {
				Optional<Table> table = g.stream().filter(t -> t.getPolicy() == policy).findFirst();
				if (table.isPresent()) {
					double chance = (1.0 /(table.get().getAccessibility() * modifier)) * 100D;
					int in_kills = (int) (100 / chance);
					if (chance > 100.0) {
						chance = 100.0;
					}
					if (in_kills == 0) {
						in_kills = 1;
					}
					
					//Updates the interface with all new information
					updateAmounts(player, policy, table.get(), in_kills);
				} else {
					updateAmounts(player, policy, new ArrayList<>(), -10);
				}
			}
			
			//If the game has displayed all drops and there are empty slots that haven't been filled, clear them
			if(player.dropSize < 80) {
				for(int i = player.dropSize;i<80;i++){
					player.getPA().sendString("", 34200+i);
					player.getPA().itemOnInterface(-1, 0, 34010+i, 0);
					player.getPA().sendString("", 34300+i);
					player.getPA().sendString("", 34100+i);
					player.getPA().sendString("", 34400+i);
				}
			}
			player.dropSize = 0;
		});
	}

	/**
	 * Updates the interface for the selected NPC
	 * @param player
	 * @param policy
	 * @param drops
	 * @param kills
	 */
	private void updateAmounts(Client player, TablePolicy policy, List<NpcDrop> drops, int kills) {
		
		//Iterates through all drops in that catagory
		for (int index = 0; index < drops.size(); index++) {
			NpcDrop drop = drops.get(index);
			int minimum = drop.getMinimum();
			int maximum = drop.getMaximum();
			int frame = (34200 + player.dropSize + index);//collumnOffset + (index * 2);
			
			//if max = min, just send the max
			if (minimum == maximum) {
				player.getPA().sendString(Misc.getValueWithoutRepresentation(drop.getMaximum()), frame);
			} else {
				player.getPA().sendString(Misc.getValueWithoutRepresentation(drop.getMinimum()) + " - " + Misc.getValueWithoutRepresentation(drop.getMaximum()), frame);
			}
			player.getPA().itemOnInterface(drop.getId(), 1, 34010+player.dropSize + index, 0);
			player.getPA().sendString(Misc.optimizeText(policy.name().toLowerCase()), 34300+player.dropSize + index);
			player.getPA().sendString(Server.itemHandler.getItemList(drop.getId()).itemName, 34100 + player.dropSize + index);
			if(kills == -10){
				player.getPA().sendString(1 + "/?", 34400 + player.dropSize + index);
			} else {
				player.getPA().sendString(1 + "/"+kills, 34400 + player.dropSize + index);
			}
		}
		
		player.dropSize += drops.size();
	}

	static int amountt = 0;

	private FileReader fileReader;

	/**
	 * Testing droptables of chosen npcId
	 * @param player		The player who is testing the droptable
	 * @param npcId			The npc who of which the player is testing the droptable from
	 * @param amount		The amount of times the player want to grab a drop from the npc droptable
	 */
	public void test(Client player, int npcId, int amount) {
		Optional<TableGroup> group = groups.values().stream().filter(g -> g.getNpcIds().contains(npcId)).findFirst();

		amountt = amount;

		while (amount-- > 0) {
			group.ifPresent(g -> {
				List<GameItem> drops = g.access(player, 1.0, 1);

				for (GameItem item : drops) {
					player.getItems().addItemToBank(item.getId(), item.getAmount());
				}
			});
		}
		player.sendMessage("Completed " + amountt + " drops from " + Server.npcHandler.getNpcName(npcId) + ".");
	}

	private static final class Key<K, V> {

		private final K key;
		private final V value;

		private Key(K key, V value) {
			this.key = key;
			this.value = value;
		}
	}
}
