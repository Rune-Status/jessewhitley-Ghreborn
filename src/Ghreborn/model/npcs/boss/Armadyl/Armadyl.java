package Ghreborn.model.npcs.boss.Armadyl;

import java.util.HashMap;
import java.util.Map;

import Ghreborn.event.CycleEventHandler;
import Ghreborn.model.npcs.NPC;
import Ghreborn.model.npcs.boss.Armadyl.impl.RespawnNpc;
import Ghreborn.model.npcs.boss.Armadyl.impl.SpawnArmadylStageZero;
import Ghreborn.model.npcs.boss.instances.InstancedArea;
import Ghreborn.model.npcs.boss.instances.InstancedAreaManager;
import Ghreborn.model.npcs.boss.instances.SingleInstancedArea;
import Ghreborn.model.npcs.boss.instances.impl.SingleInstancedArmadyl;
import Ghreborn.model.players.Boundary;
import Ghreborn.model.players.Client;

public class Armadyl {
	
	private final Object EVENT_LOCK = new Object();

	private final Client player;
	
	private SingleInstancedArea armadylInstance;
	
	public static final Boundary BOUNDARY = new Boundary(2820, 5295, 2844, 5310);
	private NPC npc;

	private Map<Integer, ArmadylStage> stages = new HashMap<>();

	public Armadyl(Client player) {
		this.player = player;
		stages.put(0, new SpawnArmadylStageZero(this, player));
		stages.put(1, new RespawnNpc(this, player));
	}
	
	public void initialize() {
		if (armadylInstance != null) {
			InstancedAreaManager.getSingleton().disposeOf(armadylInstance);
		}
		int height = InstancedAreaManager.getSingleton().getNextOpenHeight(BOUNDARY);
		armadylInstance = new SingleInstancedArmadyl(player, BOUNDARY, height);
		InstancedAreaManager.getSingleton().add(height, armadylInstance);
		if (armadylInstance == null) {
			player.sendMessage("An error occured while trying to enter Armadyl instanced. Please try again.");
			return;
		}
		player.getPA().removeAllWindows();
		player.stopMovement();
		player.getPA().sendScreenFade("Welcome to Armadyl...", 1, 3);
		player.ARMADYL_INSTANCE = true;
		CycleEventHandler.getSingleton().addEvent(player, stages.get(0), 1);
	}
	
	public void RespawnNpcs() {
		if (armadylInstance != null) {
			InstancedAreaManager.getSingleton().disposeOf(armadylInstance);
		}
		int height = InstancedAreaManager.getSingleton().getNextOpenHeight(BOUNDARY);
		armadylInstance = new SingleInstancedArmadyl(player, BOUNDARY, height);
		InstancedAreaManager.getSingleton().add(height, armadylInstance);
		if (armadylInstance == null) {
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
		armadylInstance.onDispose();
		InstancedAreaManager.getSingleton().disposeOf(armadylInstance);
		armadylInstance = null;
		//System.out.println("Armadyl ended.");
		} catch (Exception e) {
		}
	}

	public InstancedArea getInstancedArmadyl() {
		return armadylInstance;
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