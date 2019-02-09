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
import Ghreborn.definitions.ItemCacheDefinition;
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
	private static Map<List<Integer>, NpcDropTable> groups = new HashMap<>();
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
			return "<shad=000000><col=DD5C3E>";
		case VERY_RARE:
			return "<shad=000000><col=ff3000>";
		case EXTREMELY_RARE:
			return "<shad=000000><col=8B0000>";
		case UNCOMMON:
			return "<shad=000000><col=ffff00>";
		case COMMON:
			return "<shad=000000><col=c0ff00>";
		case VERY_UNCOMMON:
			return "<shad=000000><col=ffff00>";
		default:
			return "<shad=000000><col=3CB71E>";

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
	range = chance.getDenominator() - 400 - chance.getNumerator();
}
if (chance.ordinal() == Chance.EXTREMELY_RARE.ordinal()) {
	range = chance.getDenominator() - 334 - chance.getNumerator();
}
			if (chance.ordinal() == Chance.VERY_RARE.ordinal()) {
				range = chance.getDenominator() - 269 - chance.getNumerator();
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

			player.getPA().sendFrame126("Rarity - " + colour.toString() + " " + chance.toString() + " </shad><col=000000>[Ratio "
					+ colour.toString() + "1:" + range + "<col=000000>]", frame++);
			if (frame >= capacity) {
				break;
			}
			for (final NpcDrop drop : value.value) {
				final ItemCacheDefinition defs = ItemCacheDefinition.forID(drop.getId());
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

	private static final class Key<K, V> {

		private final K key;
		private final V value;

		private Key(K key, V value) {
			this.key = key;
			this.value = value;
		}
	}
}
