package Ghreborn.model.npcs.drops;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import Ghreborn.core.PlayerHandler;
import Ghreborn.model.items.GameItem;
import Ghreborn.model.players.Client;
import Ghreborn.util.ArrayIterator;
import Ghreborn.util.Chance;
import Ghreborn.util.RandomGen;

/**
 * A container that holds the unique and common drop tables.
 * 
 * @author lare96 <http://github.org/lare96>
 */
public final class NpcDropTable {
	
	public NpcDrop[] getUnique() {
		return unique;
	}

	public NpcDropCache[] getCommon() {
		return common;
	}
	
	public NpcDrop[] getUniqueDrops() {
		return unique;
	}
	
	public NpcDropCache[] getCommonDrops() {
		return common;
	}

	/**
	 * The unique drop table that consists of both dynamic and rare drops.
	 */
	private final NpcDrop[] unique;

	/**
	 * The common drop table that is shared with other tables.
	 */
	private final NpcDropCache[] common;

	/**
	 * Creates a new {@link NpcDropTable}.
	 *
	 * @param unique
	 *            the unique drop table.
	 * @param common
	 *            the common drop table.
	 */
	public NpcDropTable(NpcDrop[] unique, NpcDropCache[] common) {
		this.unique = unique;
		this.common = common;
	}

	/**
	 * Performs the necessary calculations on all of the tables in this
	 * container to determine an array of items to drop. Please note that this
	 * is not a static implementation meaning that calling this multiple times
	 * will return a different array of items.
	 *
	 * @param player
	 *            the player that these calculations are being performed for.
	 * @return the array of items that were calculated.
	 */

	public static void yell(String msg) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Client c2 = (Client) PlayerHandler.players[j];
				c2.sendMessage(msg);
			}
		}
	}

	public static int dropChance = 0;

	public List<GameItem> toItems(Client player) {

		RandomGen random = new RandomGen();
		List<GameItem> items = new LinkedList<>();
		NpcDropCache cache = random.random(common);
		Iterator<NpcDrop> $it = new ArrayIterator<>(random.shuffle(unique.clone()));
		
		boolean rollExtreme = player.getRights().getValue() == 6 ? random.get().nextInt(3) == 0 : random.get().nextInt(7) == 0;
		boolean rollDon = player.getRights().getValue() == 3 ? random.get().nextInt(3) == 0 : random.get().nextInt(7) == 0;
		boolean rollSup = player.getRights().getValue() == 7 ? random.get().nextInt(3) == 0 : random.get().nextInt(6) == 0;
		boolean rollResp = player.getRights().getValue() == 8 ? random.get().nextInt(3) == 0 : random.get().nextInt(5) == 0;
		boolean rollLeg = player.getRights().getValue() == 20 ? random.get().nextInt(3) == 0 : random.get().nextInt(4) == 0; 
		boolean rollRare = player.playerEquipment[player.playerRing] == 12785 &&  player.playerEquipment[player.playerRing] == 23910 ? random.get().nextInt(3) == 0 : random.get().nextInt(5) == 0; // 30% chance.
		boolean rollCommon = player != null && player.playerEquipment[player.playerRing] == 2572 ? random.get().nextInt(4) == 0 : random.get().nextInt(8) == 0; 
		boolean rollDynamic = random.get().nextBoolean();
		int amount = 0;
		int dropChance = 0;
		while ($it.hasNext()) {
			NpcDrop next = $it.next();
			Chance chance = next.getChance();

			if (chance.getTier() == 0) {
				items.add(next.toItem());
			} else if (chance.getTier() >= Chance.RARE.getTier() && rollRare || rollDon || rollSup || rollResp || rollLeg || rollExtreme) {
				if (player != null) {
					player.dropChance = 1;
					if (chance.successful())
						items.add(next.toItem());
					rollRare = false;
					rollDon = false;
					rollSup = false;
					rollResp = false;
					rollLeg = false;
					rollExtreme = false;
				}
			} else if (rollDynamic && chance.getTier() < Chance.RARE.getTier()) {
				if (amount++ == 2)
					rollDynamic = false;
				if (next.getChance().successful())
					items.add(next.toItem());
			}

			if (!$it.hasNext() && rollCommon) {
				next = random.random(NpcDropManager.COMMON.get(cache));
				if (next.getChance().successful())
					items.add(next.toItem());
				rollCommon = false;
			}
		}
		return items;
	}
}