package Ghreborn.model.npcs.boss.Alchemical_Hydra;

import java.util.HashMap;
import java.util.Map;

import Ghreborn.event.CycleEventHandler;
import Ghreborn.model.npcs.NPC;
import Ghreborn.model.npcs.boss.Alchemical_Hydra.impl.SpawnAlchemicalHydraStageZero;
import Ghreborn.model.npcs.boss.instances.InstancedArea;
import Ghreborn.model.npcs.boss.instances.InstancedAreaManager;
import Ghreborn.model.npcs.boss.instances.SingleInstancedArea;
import Ghreborn.model.npcs.boss.instances.impl.SingleInstancedArmadyl;
import Ghreborn.model.players.Boundary;
import Ghreborn.model.players.Client;

public class Alchemical_Hydra {

	private final Object EVENT_LOCK = new Object();

	private final Client player;
	
	private SingleInstancedArea AlchemicalHydraInstance;
	
	public static final Boundary BOUNDARY = new Boundary(1356, 10256, 1378, 10279);
	private NPC npc;

	private Map<Integer, AlchemicalHydraStage> stages = new HashMap<>();

	public Alchemical_Hydra(Client player) {
		this.player = player;
		stages.put(0, new SpawnAlchemicalHydraStageZero(this, player));
		//stages.put(1, new RespawnNpc(this, player));
	}
	public void initialize() {
		if (AlchemicalHydraInstance != null) {
			InstancedAreaManager.getSingleton().disposeOf(AlchemicalHydraInstance);
		}
		int height = InstancedAreaManager.getSingleton().getNextOpenHeight(BOUNDARY);
		AlchemicalHydraInstance = new SingleInstancedArmadyl(player, BOUNDARY, height);
		InstancedAreaManager.getSingleton().add(height, AlchemicalHydraInstance);
		if (AlchemicalHydraInstance == null) {
			player.sendMessage("An error occured while trying to enter Armadyl instanced. Please try again.");
			return;
		}
		player.getPA().removeAllWindows();
		player.stopMovement();
		player.getPA().sendScreenFade("Welcome to Alchemical Hydra...", 1, 3);
		player.ARMADYL_INSTANCE = true;
		CycleEventHandler.getSingleton().addEvent(player, stages.get(0), 1);
	}
	
	public void RespawnNpcs() {
		if (AlchemicalHydraInstance != null) {
			InstancedAreaManager.getSingleton().disposeOf(AlchemicalHydraInstance);
		}
		int height = InstancedAreaManager.getSingleton().getNextOpenHeight(BOUNDARY);
		AlchemicalHydraInstance = new SingleInstancedArmadyl(player, BOUNDARY, height);
		InstancedAreaManager.getSingleton().add(height, AlchemicalHydraInstance);
		if (AlchemicalHydraInstance == null) {
			player.sendMessage("An error occured while trying to enter Armadyl instanced. Please try again.");
			return;
		}
		player.getPA().removeAllWindows();
		player.ARMADYL_INSTANCE = true;
		CycleEventHandler.getSingleton().addEvent(player, stages.get(1), 1);
	}

	public void stop() {
		try {
		CycleEventHandler.getSingleton().stopEvents(EVENT_LOCK);
		AlchemicalHydraInstance.onDispose();
		InstancedAreaManager.getSingleton().disposeOf(AlchemicalHydraInstance);
		AlchemicalHydraInstance = null;
		//System.out.println("Armadyl ended.");
		} catch (Exception e) {
		}
	}

	public InstancedArea getInstancedAlchemicalHydra() {
		return AlchemicalHydraInstance;
	}

	public NPC getNpc() {
		return npc;
	}
	
	public void setNpc(NPC npc) {
		this.npc = npc;
	}
}
