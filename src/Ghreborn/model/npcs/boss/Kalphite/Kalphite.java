package Ghreborn.model.npcs.boss.Kalphite;

import java.util.HashMap;
import java.util.Map;

import Ghreborn.event.CycleEventHandler;
import Ghreborn.model.npcs.NPC;
import Ghreborn.model.npcs.boss.Kalphite.impl.*;
import Ghreborn.model.npcs.boss.instances.InstancedArea;
import Ghreborn.model.npcs.boss.instances.InstancedAreaManager;
import Ghreborn.model.npcs.boss.instances.SingleInstancedArea;
import Ghreborn.model.npcs.boss.instances.impl.SingleInstancedKalphite;
import Ghreborn.model.players.Boundary;
import Ghreborn.model.players.Client;

public class Kalphite {
	
	private final Object EVENT_LOCK = new Object();

	private final Client player;
	
	private SingleInstancedArea kalphiteInstance;
	
	public static final Boundary BOUNDARY = new Boundary(3452, 9466, 3524, 9536);
	private NPC npc;

	private Map<Integer, Stage> stages = new HashMap<>();

	public Kalphite(Client player) {
		this.player = player;
		stages.put(0, new SpawnKalphite(this, player));
		stages.put(1, new RespawnNpc(this, player));
	}
	
	public void initialize() {
		if (kalphiteInstance != null) {
			InstancedAreaManager.getSingleton().disposeOf(kalphiteInstance);
		}
		int height = InstancedAreaManager.getSingleton().getNextOpenHeight(BOUNDARY);
		kalphiteInstance = new SingleInstancedKalphite(player, BOUNDARY, height);
		InstancedAreaManager.getSingleton().add(height, kalphiteInstance);
		if (kalphiteInstance == null) {
			player.sendMessage("An error occured while trying to enter Bandos instanced. Please try again.");
			return;
		}
		player.getPA().removeAllWindows();
		player.stopMovement();
		player.getPA().sendScreenFade("Welcome to Kalphite...", 1, 3);
		player.KALPHITE_INSTANCE = true;
		CycleEventHandler.getSingleton().addEvent(player, stages.get(0), 1);
	}
	
	public void RespawnNpc() {
		if (kalphiteInstance != null) {
			InstancedAreaManager.getSingleton().disposeOf(kalphiteInstance);
		}
		int height = InstancedAreaManager.getSingleton().getNextOpenHeight(BOUNDARY);
		kalphiteInstance = new SingleInstancedKalphite(player, BOUNDARY, height);
		InstancedAreaManager.getSingleton().add(height, kalphiteInstance);
		if (kalphiteInstance == null) {
			player.sendMessage("An error occured while trying to enter Bandos instanced. Please try again.");
			return;
		}
		player.getPA().removeAllWindows();
		player.KALPHITE_INSTANCE = true;
		CycleEventHandler.getSingleton().addEvent(player, stages.get(1), 1);
	}

	public void stop() {
		try {
		CycleEventHandler.getSingleton().stopEvents(EVENT_LOCK);
		kalphiteInstance.onDispose();
		InstancedAreaManager.getSingleton().disposeOf(kalphiteInstance);
		kalphiteInstance = null;
		} catch (Exception e) {
		}
	}

	public InstancedArea getInstancedKalphite() {
		return kalphiteInstance;
	}

	public NPC getNpc() {
		return npc;
	}
	
	public void setNpc(NPC npc) {
		this.npc = npc;
	}
}

