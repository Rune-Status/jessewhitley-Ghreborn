package Ghreborn.model.npcs.boss.Kraken;

import java.util.HashMap;
import java.util.Map;

import Ghreborn.event.CycleEventHandler;
import Ghreborn.model.npcs.NPC;
import Ghreborn.model.npcs.boss.Kraken.impl.SpawnKrakenStageZero;
import Ghreborn.model.npcs.boss.instances.InstancedArea;
import Ghreborn.model.npcs.boss.instances.InstancedAreaManager;
import Ghreborn.model.npcs.boss.instances.SingleInstancedArea;
import Ghreborn.model.npcs.boss.instances.impl.SingleInstancedKraken;
import Ghreborn.model.players.Boundary;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.Player;


public class Kraken {

	
	private final Object EVENT_LOCK = new Object();

	private final Client player;
	
	private SingleInstancedArea krakenInstance;
	
	public static final Boundary BOUNDARY = new Boundary(/*(X)*/2269, /*(Y)*/10023,
														/*(X)*/2292, /*(Y)*/10044);
	private NPC npc;

	private Map<Integer, KrakenStage> stages = new HashMap<>();

	public Kraken(Client player) {
		this.player = player;
		stages.put(0, new SpawnKrakenStageZero(this, player));
	}
	
	public void initialize() {
		if (krakenInstance != null) {
			InstancedAreaManager.getSingleton().disposeOf(krakenInstance);
		}
		int height = InstancedAreaManager.getSingleton().getNextOpenHeight(BOUNDARY);
		krakenInstance = new SingleInstancedKraken(player, BOUNDARY, height);
		InstancedAreaManager.getSingleton().add(height, krakenInstance);
		if (krakenInstance == null) {
			player.sendMessage("An error occured while trying to enter Kraken's shrine. Please try again.");
			return;
		}
		player.getPA().removeAllWindows();
		player.stopMovement();
		player.getPA().sendScreenFade("Welcome to Kraken's Cave...", 1, 5);
		CycleEventHandler.getSingleton().addEvent(player, stages.get(0), 1);
	}

	public void stop() {
		CycleEventHandler.getSingleton().stopEvents(EVENT_LOCK);
		krakenInstance.onDispose();
		InstancedAreaManager.getSingleton().disposeOf(krakenInstance);
		krakenInstance = null;
	}

	public InstancedArea getInstancedKraken() {
		return krakenInstance;
	}

	public NPC getNpc() {
		return npc;
	}
	
	public void setNpc(NPC npc) {
		this.npc = npc;
	}
	
	public static void enter(Client player) {
		if (player.wildLevel > 20) {
			player.sendMessage("You cannot teleport above 20 wilderness.");
			player.getPA().closeAllWindows();
			return;
		}
		if (player.KRAKEN_CLICKS >= 1) {
			player.sendMessage("You already have an active instance!");
			player.getPA().closeAllWindows();
			return;
		}
		if (player.getKraken().getInstancedKraken() != null) {
			player.sendMessage("You already have an active instance!");
			player.getPA().closeAllWindows();
			return;
		}
		player.getKraken().initialize();
		player.KRAKEN_CLICKS = 1;
	}
}
