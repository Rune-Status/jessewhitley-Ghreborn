package Ghreborn.model.npcs.drops;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import Ghreborn.Server;
import Ghreborn.model.items.GameItem;
import Ghreborn.model.npcs.NPC;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.Player;
import Ghreborn.util.Chance;

/**
 * The static-utility class that manages all of the {@link NpcDropTable}s
 * including the process of dropping the items when an {@link Npc} is killed.
 * 
 * @author lare96 <http://github.org/lare96>
 */
public final class NpcDropManager {

	/**
	 * The {@link EnumMap} consisting of the cached common {@link NpcDrop}s used
	 * across many {@link NpcDropTable}s.
	 */
	public static final EnumMap<NpcDropCache, NpcDrop[]> COMMON = new EnumMap<>(NpcDropCache.class);

	/**
	 * The {@link HashMap} that consists of the drops for {@link Npc}s.
	 */
	public static final Map<Integer, NpcDropTable> TABLES = new HashMap<>();

	/**
	 * The default drop table for all {@link Npc}s.
	 */
	private static final NpcDropTable DEFAULT = new NpcDropTable(new NpcDrop[] { new NpcDrop(526, 1, 1, Chance.ALWAYS), new NpcDrop(16545, 1, 1, Chance.LEGNEDLY_RARE) },
			new NpcDropCache[] { NpcDropCache.LOW_RUNES });
	
	//private int[] rare_drops

	/**
	 * VDrops the items in {@code victim}s drop table for {@code killer}. If the
	 * killer doesn't exist, the items are dropped for everyone to see.
	 * 
	 * @param killer
	 *            the killer, may or may not exist.
	 * @param victim
	 *            the victim that was killed.
	 */
	public static void dropItems(Optional<Client> killer, NPC victim) {
		try {
			boolean drops = victim.npcType == 2042 || victim.npcType == 2043 || victim.npcType == 2044
					|| victim.npcType == 494 || victim.npcType == 491 || victim.npcType == 492;

			NpcDropTable table = TABLES.getOrDefault(victim.npcType, DEFAULT);
			List<GameItem> dropItems = table.toItems(killer.orElse(null));
			for (GameItem drop : dropItems) {
				if (drop == null)
					continue;
				if (drops) {
					killer.ifPresent(p -> Server.npcHandler.handleLootShare(p, drop.getId(), drop.getAmount()));
					killer.ifPresent(p -> Server.itemHandler.createGroundItem(p, drop.getId(), p.absX, p.absY,
							p.heightLevel, drop.getAmount(), p.index));
					killer.ifPresent(p -> Server.npcHandler.rareDrops(p, drop.getId(), drop.getAmount()));
					// System.out.println("Zulrah/Kraken Drops");
				} else if (!drops) {
					//killer.ifPresent(p -> Server.npcHandler.handleLootShare(p, drop.getId(), drop.getAmount()));
					killer.ifPresent(p -> Server.itemHandler.createGroundItem(p, drop.getId(), victim.absX, victim.absY,
							victim.heightLevel, drop.getAmount(), p.index));
					killer.ifPresent(p -> Server.npcHandler.rareDrops(p, drop.getId(), drop.getAmount()));
					// System.out.println("Normal Player Drops");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}