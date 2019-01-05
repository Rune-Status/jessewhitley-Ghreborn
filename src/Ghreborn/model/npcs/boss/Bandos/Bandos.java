package Ghreborn.model.npcs.boss.Bandos;

import java.util.HashMap;
import java.util.Map;

import Ghreborn.event.CycleEventHandler;
import Ghreborn.model.npcs.NPC;
import Ghreborn.model.npcs.boss.Bandos.impl.RespawnNpc;
import Ghreborn.model.npcs.boss.Bandos.impl.SpawnBandosStageZero;
import Ghreborn.model.npcs.boss.instances.InstancedArea;
import Ghreborn.model.npcs.boss.instances.InstancedAreaManager;
import Ghreborn.model.npcs.boss.instances.SingleInstancedArea;
import Ghreborn.model.npcs.boss.instances.impl.SingleInstancedBandos;
import Ghreborn.model.players.Boundary;
import Ghreborn.model.players.Client;

public class Bandos {
	
	private final Object EVENT_LOCK = new Object();

	private final Client player;
	
	private SingleInstancedArea bandosInstance;
	
	public static final Boundary BOUNDARY = new Boundary(/*(X)*/2861, /*(Y)*/5348, //these coords are correct?yes
														/*(X)*/2880, /*(Y)*/5375);
	private NPC npc;
	public static final int MINION1 = 2216;
	public static final int MINION2 = 2217;
	public static final int MINION3 = 2218;

	private Map<Integer, BandosStage> stages = new HashMap<>();

	public Bandos(Client player) {
		this.player = player;
		stages.put(0, new SpawnBandosStageZero(this, player));
		stages.put(1, new RespawnNpc(this, player));
	}
	
	public void initialize() {
		if (bandosInstance != null) {
			InstancedAreaManager.getSingleton().disposeOf(bandosInstance);
		}
		int height = InstancedAreaManager.getSingleton().getNextOpenHeight(BOUNDARY);
		bandosInstance = new SingleInstancedBandos(player, BOUNDARY, height);
		InstancedAreaManager.getSingleton().add(height, bandosInstance);
		if (bandosInstance == null) {
			player.sendMessage("An error occured while trying to enter Bandos instanced. Please try again.");
			return;
		}
		player.getPA().removeAllWindows();
		player.stopMovement();
		player.getPA().sendScreenFade("Welcome to Bandos...", 1, 3);
		player.BANDOS_INSTANCE = true;
		CycleEventHandler.getSingleton().addEvent(player, stages.get(0), 1);
	}
	
	public void RespawnNpcs() {
		if (bandosInstance != null) {
			InstancedAreaManager.getSingleton().disposeOf(bandosInstance);	
		}
		int height = InstancedAreaManager.getSingleton().getNextOpenHeight(BOUNDARY);
		bandosInstance = new SingleInstancedBandos(player, BOUNDARY, height);
		InstancedAreaManager.getSingleton().add(height, bandosInstance);
		if (bandosInstance == null) {
			player.sendMessage("An error occured while trying to enter Bandos instanced. Please try again.");
			return;
		}
		player.getPA().removeAllWindows();
		//player.stopMovement();
		//player.getPA().sendScreenFade("Welcome to Bandos...", 1, 3);
		player.BANDOS_INSTANCE = true;
		CycleEventHandler.getSingleton().addEvent(player, stages.get(1), 1);
	}

	public void stop() {
		try {
		CycleEventHandler.getSingleton().stopEvents(EVENT_LOCK);
		bandosInstance.onDispose();
		InstancedAreaManager.getSingleton().disposeOf(bandosInstance);
		bandosInstance = null;
		//System.out.println("Bandos ended.");
		} catch (Exception e) {
		}
	}

	public InstancedArea getInstancedBandos() {
		return bandosInstance;
	}

	public NPC getNpc() {
		return npc;
	}
	
	public void setNpc(NPC npc) {
		this.npc = npc;
	}
	
	/*public int getStage() {
		return stage;
	}*/
}