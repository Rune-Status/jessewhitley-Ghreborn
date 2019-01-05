package Ghreborn.model.npcs.drops;

import java.util.*;

import org.apache.commons.lang3.Range;

import Ghreborn.core.PlayerHandler;
import Ghreborn.model.items.GameItem;
import Ghreborn.model.items.Item;
import Ghreborn.model.items.ItemAssistant;
import Ghreborn.model.players.Client;
import Ghreborn.util.Misc;

@SuppressWarnings("serial")
public class TableGroup extends ArrayList<Table> {

	/**
	 * The non-playable character that has access to this group of tables
	 */
	private final List<Integer> npcIds;

	/**
	 * Creates a new group of tables
	 * 
	 * @param npcId the npc identification value
	 */
	public TableGroup(List<Integer> npcsIds) {
		this.npcIds = npcsIds;
	}

	/**
	 * Accesses each {@link Table} in this {@link TableGroup} with hopes of retrieving a {@link List} of {@link GameItem} objects.
	 * 
	 * @return
	 */
	public List<GameItem> access(Client player, double modifier, int repeats) {
		int rights = player.getRights().getValue() - 1;
		List<GameItem> items = new ArrayList<>();
		for (Table table : this) {
			TablePolicy policy = table.getPolicy();
			if (policy.equals(TablePolicy.CONSTANT)) {
				for (NpcDrop drop : table) {
					int minimumAmount = drop.getMinimum();

					items.add(new GameItem(drop.getId(), minimumAmount + Misc.random(drop.getMaximum() - minimumAmount)));
				}
			} else {
				for (int i = 0; i < repeats; i++) {
					double chance = (1.0 / (double) (table.getAccessibility() * modifier)) * 100D;

					double roll = Misc.preciseRandom(Range.between(0.0, 100.0));

					if (chance > 100.0) {
						chance = 100.0;
					}
					if (roll <= chance) {
						NpcDrop drop = table.fetchRandom();
						int minimumAmount = drop.getMinimum();
						GameItem item = new GameItem(drop.getId(),
								minimumAmount + Misc.random(drop.getMaximum() - minimumAmount));

						items.add(item);
						if (chance <= 1.5) {
							if (policy.equals(TablePolicy.VERY_RARE) || policy.equals(TablePolicy.RARE)) {
								if (Item.getItemName(item.getId()).toLowerCase().contains("cowhide")
										|| Item.getItemName(item.getId()).toLowerCase().contains("feather")
										|| Item.getItemName(item.getId()).toLowerCase().contains("arrow")
										|| Item.getItemName(item.getId()).toLowerCase().contains("sq shield")
										|| Item.getItemName(item.getId()).toLowerCase().contains("rune warhammer")
										|| Item.getItemName(item.getId()).toLowerCase().contains("rune battleaxe")
										|| Item.getItemName(item.getId()).toLowerCase().contains("casket")
										|| Item.getItemName(item.getId()).toLowerCase().contains("silver ore")
										|| Item.getItemName(item.getId()).toLowerCase().contains("rune spear")
										|| item.getId() >= 554 && item.getId() <= 566) {
									
								} else {
									PlayerHandler.executeGlobalMessage(
											"@red@" + Misc.capitalize(player.playerName) + " received a drop: "
													+ (item.getAmount() > 1 ? (item.getAmount() + "x ") : ItemAssistant.getItemName(item.getId()) + "@bla@."));
								}
							}
						}
					}
				}
			}
		}
		return items;
	}

	/**
	 * The non-playable character identification values that have access to this group of tables.
	 * 
	 * @return the non-playable character id values
	 */
	public List<Integer> getNpcIds() {
		return npcIds;
	}
}
