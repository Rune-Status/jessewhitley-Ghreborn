package Ghreborn.model.content;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import Ghreborn.Server;
import Ghreborn.core.PlayerHandler;
import Ghreborn.event.CycleEvent;
import Ghreborn.event.CycleEventContainer;
import Ghreborn.event.CycleEventHandler;
import Ghreborn.model.content.teleport.Position;
import Ghreborn.model.content.teleport.Teleport;
import Ghreborn.model.content.teleport.Teleport.TeleportType;
import Ghreborn.model.content.teleport.TeleportExecutor;
import Ghreborn.model.players.Boundary;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.Player;
import Ghreborn.util.Misc;
import Ghreborn.world.objects.GlobalObject;

/**
 * Obelisks are the objects that exist in the wilderness that aid player teleportation.
 * Once activated, any player within the obelisk boundary is moved to another obelisk.
 * 
 * @author Jason MacKeigan
 * @date Jan 3, 2015, 11:54:03 PM
 */
public class Obelisks {
	
	/**
	 * The state of each obelisk, if they are either active or inactive.
	 */
	private static Map<Integer, Boolean> state = new HashMap<>();
	
	/**
	 * A single instance of this class
	 */
	private static Obelisks INSTANCE = new Obelisks();

	
	/**
	 * Returns the single instance of the Obelisks class
	 * @return the instance
	 */
	public static Obelisks get() {
		return INSTANCE;
	}
	
	/**
	 * Stores the obelisk object ids with the default state, false, in a map.
	 */
	static {
		for (Location location : Location.values()) {
			state.put(location.objectId, false);
		}
	}
	
	
	/**
	 * The function used to activate a obelisk
	 * @param player	the player attempting to activate a obelisk
	 * @param objectId	the object id of the obelisk
	 */
	public void activate(Client player, int objectId) {
		Location location = Location.forObject(objectId);
		if (location == null || player == null) {
			return;
		}
		boolean active = state.get(objectId);
		if (CycleEventHandler.getSingleton().isAlive(location) || active) {
			player.sendMessage("The obelisk is already active, please wait.");
			return;
		}
		if (player.teleTimer > 0) {
			player.sendMessage("You cannot do this whilst teleporting.");
			return;
		}
		state.put(objectId, true);
		int x = location.getBoundaries().getMinimumX();
		int y = location.getBoundaries().getMinimumY();
		if(x == location.getBoundaries().getMinimumX() && y == location.getBoundaries().getMinimumY());
		Server.getGlobalObjects().add(new GlobalObject(14825, x, y, 0, 0, 10, 14, objectId));
		Server.getGlobalObjects().add(new GlobalObject(14825, x + 4, y, 0, 0, 10, 14, objectId));
		Server.getGlobalObjects().add(new GlobalObject(14825, x, y + 4, 0, 0, 10, 14, objectId));
		Server.getGlobalObjects().add(new GlobalObject(14825, x + 4, y + 4, 0, 0, 10, 14, objectId));
		CycleEventHandler.getSingleton().addEvent(location, new Event(location), 14);
	}
	
	public enum Location {
		LEVEL_13(14829, new Boundary(3154, 3618, 3158, 3622)),
		LEVEL_19(14830, new Boundary(3225, 3665, 3229, 3669)),
		LEVEL_27(14827, new Boundary(3033, 3730, 3037, 3734)),
		LEVEL_35(14828, new Boundary(3104, 3792, 3108, 3796)),
		LEVEL_44(14826, new Boundary(2978, 3864, 2982, 3868)),
		LEVEL_50(14831, new Boundary(3305, 3914, 3309, 3918));
		
		private int objectId;
		private Boundary boundary;
		
		private Location(int objectId, Boundary boundary) {
			this.objectId = objectId;
			this.boundary = boundary;
		}
		
		public int getObjectId() {
			return objectId;
		}
		
		public Boundary getBoundaries() {
			return boundary;
		}
		
		static Location forObject(int objectId) {
			for (Location l : values()) {
				if (l.objectId == objectId) {
					return l;
				}
			}
			return null;
		}
		
		static Location getRandom(Location exclude) {
			ArrayList<Location> locations = new ArrayList<>(Arrays.asList(values()));
			locations.remove(exclude);
			return locations.get(Misc.random(locations.size() - 1));
		}
		
	}
	
	static final class Event extends CycleEvent {
		
		private Location location;
		
		public Event(Location location) {
			this.location = location;
		}
		
		public static void bossMessage(String q) {
			for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Player c2 = (Player)PlayerHandler.players[j];
				c2.sendMessage(q);
				}
			}
		}
		
		@Override
		public void execute(CycleEventContainer container) {
			state.put(location.objectId, false);
			container.stop();
			Boundary boundary = new Boundary(location.boundary.getMinimumX() + 1, location.boundary.getMinimumY() + 1,
					location.boundary.getMinimumX() + 3, location.boundary.getMinimumY() + 3);
			List<Player> players = PlayerHandler.getPlayers().stream().filter(Objects::nonNull).filter(player -> 
				Boundary.isIn(player, boundary)).collect(Collectors.toList());
			if (players.size() > 0) {
				Location randomObelisk = Location.getRandom(location);
				int x = randomObelisk.getBoundaries().getMinimumX() + 1;
				int y = randomObelisk.getBoundaries().getMinimumY() + 1;
				players.forEach(player -> TeleportExecutor.teleport((Client)player, 
						new Teleport(new Position(x + Misc.random(2), y + Misc.random(2), 0), TeleportType.OBELISK)));
			}
		}
	}

}

