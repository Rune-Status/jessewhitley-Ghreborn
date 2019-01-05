package Ghreborn.model.content;

import java.util.ArrayList;
import java.util.List;

import Ghreborn.Server;
import Ghreborn.model.Location;
import Ghreborn.model.npcs.NPC;
import Ghreborn.model.players.Boundary;
import Ghreborn.model.players.Client;

public class InstancedArea extends PlayerContent {

	private final int instanceHeight;

	private boolean started;
	private List<Boundary> boundaries = new ArrayList<>();
	private List<NPC> npcSpawns = new ArrayList<>();

	private Object state;
	private int phase;

	public InstancedArea(Client player, int heightLevel, Boundary... boundaryArray) {
		super(player);

		instanceHeight = getPlayer().getId() * heightLevel;
		for (Boundary boundary : boundaryArray) {
			boundaries.add(boundary);
		}
	}

	public void setState(Object state) {
		this.state = state;
	}

	@SuppressWarnings("unchecked")
	public <T> T getState() {
		return (T) state;
	}

	public int incPhase() {
		return incPhase(1);
	}

	public int incPhase(int amount) {
		return (phase += amount);
	}

	public int setPhase(int phase) {
		return (this.phase = phase);
	}

	public int getPhase() {
		return phase;
	}

	public NPC spawnNpc(int id, Location location, int walkingType, int hitpoints, int maxHit, int attack, int defence, boolean aggresive, boolean headIcon) {
		NPC npc = Server.npcHandler.spawnNpc(getPlayer(), id, location.getX(), location.getY(), instanceHeight, walkingType, hitpoints, maxHit, attack, defence, aggresive, headIcon);
		npcSpawns.add(npc);
		return npc;
	}

	public final void process() {
		if (inArea()) {
			if (!started) {
				start();
				return;
			}

			onProcess();
		} else if (started) {
			destruct();
		}
	}

	public final void start() {
		onStart();
		started = true;
	}

	public final void destruct() {
		onDestruct();
		started = false;
	}

	public void onStart() {

	}

	public void onProcess() {

	}

	public void onLogout() {

	}

	public void onDestruct() {

	}

	public int getInstanceHeight() {
		return instanceHeight;
	}

	public boolean inArea() {
		return boundaries.stream().anyMatch((boundary) -> Boundary.isIn(getPlayer(), boundary));
	}

}
