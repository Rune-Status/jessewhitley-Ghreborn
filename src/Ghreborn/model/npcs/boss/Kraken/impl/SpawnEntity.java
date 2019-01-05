package Ghreborn.model.npcs.boss.Kraken.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import Ghreborn.Server;
import Ghreborn.event.CycleEvent;
import Ghreborn.event.CycleEventContainer;
import Ghreborn.event.CycleEventHandler;
import Ghreborn.model.npcs.NPC;
import Ghreborn.model.npcs.NPCHandler;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.Player;


public class SpawnEntity {
	
	public static final Map<NPC, Client> DISTURBED_POOLS = new ConcurrentHashMap<>();
	
	public static void spawnEntity(Client c, int i) {
		final NPC whirlpool = NPCHandler.npcs[i];
		if (whirlpool == null || whirlpool.npcType != 493 && whirlpool.npcType != 496)
			return;
		final Client player = DISTURBED_POOLS.get(whirlpool);
		if (player != null && player == c) {
			c.sendMessage("You've already disturbed this pool!");
			return;
		}
		final boolean head = whirlpool.npcType == 496;
		c.getCombat().resetPlayerAttack();
		NPCHandler.KILL_POOLS(c, head ? 496 : 493, whirlpool.absX, whirlpool.absY, whirlpool.heightLevel);
		DISTURBED_POOLS.put(whirlpool,c);
		if (head) {
			for (NPC n : DISTURBED_POOLS.keySet()) {
				if (n == null)
					continue;
				Client p = DISTURBED_POOLS.get(n);
				if (p == null)
					continue;
				if (p == c)
					DISTURBED_POOLS.remove(p);
				p.krakenTent = 0;
			}
		}
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				NPC npc = Server.npcHandler.spawnNpc(c, head ? 494 : 5535, whirlpool.absX, whirlpool.absY, c.heightLevel, -1, head ? 255 : 80, 10, 500, 500, true, false);
				c.krakenTent++;
				if (head) {
					npc.animation(3617);
				}
				npc.killerId = c.getId();
				npc.face(c);
				container.stop();
			}
		}, 3);
	} 
}