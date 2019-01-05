package Ghreborn.model.npcs.boss.Zamorak;

import java.util.HashMap;
import java.util.Map;

import Ghreborn.event.CycleEventHandler;
import Ghreborn.model.npcs.NPC;
import Ghreborn.model.npcs.boss.Zamorak.impl.RespawnNpc;
import Ghreborn.model.npcs.boss.Zamorak.impl.SpawnZamorakStageZero;
import Ghreborn.model.npcs.boss.instances.InstancedArea;
import Ghreborn.model.npcs.boss.instances.InstancedAreaManager;
import Ghreborn.model.npcs.boss.instances.SingleInstancedArea;
import Ghreborn.model.npcs.boss.instances.impl.SingleInstancedZamorak;
import Ghreborn.model.players.Boundary;
import Ghreborn.model.players.Client;

public class Zamorak {
	
	private final Object EVENT_LOCK = new Object();

	private final Client player;
	
	private SingleInstancedArea zamorakInstance;
	
	public static final Boundary BOUNDARY = new Boundary(2917, 5315, 2937, 5334);
	private NPC npc;

	private Map<Integer, ZamorakStage> stages = new HashMap<>();

	public Zamorak(Client player) {
		this.player = player;
		stages.put(0, new SpawnZamorakStageZero(this, player));
		stages.put(1, new RespawnNpc(this, player));
	}
	
	public void initialize() {
		if (zamorakInstance != null) {
			InstancedAreaManager.getSingleton().disposeOf(zamorakInstance);
		}
		int height = InstancedAreaManager.getSingleton().getNextOpenHeight(BOUNDARY);
		zamorakInstance = new SingleInstancedZamorak(player, BOUNDARY, height);
		InstancedAreaManager.getSingleton().add(height, zamorakInstance);
		if (zamorakInstance == null) {
			player.sendMessage("An error occured while trying to enter Zamorak instanced. Please try again.");
			return;
		}
		player.getPA().removeAllWindows();
		player.stopMovement();
		player.getPA().sendScreenFade("Welcome to Zamorak...", 1, 3);
		player.ZAMORAK_INSTANCE = true;
		CycleEventHandler.getSingleton().addEvent(player, stages.get(0), 1);
	}
	
	public void RespawnNpcs() {
		if (zamorakInstance != null) {
			InstancedAreaManager.getSingleton().disposeOf(zamorakInstance);
		}
		int height = InstancedAreaManager.getSingleton().getNextOpenHeight(BOUNDARY);
		zamorakInstance = new SingleInstancedZamorak(player, BOUNDARY, height);
		InstancedAreaManager.getSingleton().add(height, zamorakInstance);
		if (zamorakInstance == null) {
			player.sendMessage("An error occured while trying to enter Zamorak instanced. Please try again.");
			return;
		}
		player.getPA().removeAllWindows();
		player.ZAMORAK_INSTANCE = true;
		CycleEventHandler.getSingleton().addEvent(player, stages.get(1), 1);
	}

	public void stop() {
		try {
			CycleEventHandler.getSingleton().stopEvents(EVENT_LOCK);
			zamorakInstance.onDispose();
			InstancedAreaManager.getSingleton().disposeOf(zamorakInstance);
			zamorakInstance = null;
			//System.out.println("Zamorak ended.");
		} catch (Exception e) {

		}
	}

	public InstancedArea getInstancedZamorak() {
		return zamorakInstance;
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