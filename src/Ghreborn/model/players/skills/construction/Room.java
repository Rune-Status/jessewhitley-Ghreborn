package Ghreborn.model.players.skills.construction;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

import Ghreborn.Test1;
import Ghreborn.definitions.ObjectDef;
import Ghreborn.model.players.Client;
import Ghreborn.util.Map;

public class Room {

	@Expose
	private String name = "", clazz = "";

	@Expose
	private int x, y, rotation = 0;

	@Expose
	private int[] position = new int[2];

	private List<Buildable> buildables = new ArrayList<Buildable>();
	
	private List<RoomObject> OBJECTS = new ArrayList<RoomObject>();
	
	private List<RoomObject> GLOBAL_REPLACE_OBJECTS = new ArrayList<RoomObject>();
	
	private List<RoomObject> ONCHANGE_OBJECTS = new ArrayList<RoomObject>();

	@Expose
	private List<RoomObject> CUSTOM_OBJECTS = new ArrayList<RoomObject>();
	
	public Room(int x, int y) {
		this.x = x;
		this.y = y;
		this.clazz = this.getClass().getName();
		
		ONCHANGE_OBJECTS.add(new RoomObject(13098, 0, 3, 0, 0, "Wall"));
		ONCHANGE_OBJECTS.add(new RoomObject(13098, 0, 4, 0, 0, "Wall"));
		ONCHANGE_OBJECTS.add(new RoomObject(13098, 3, 7, 1, 0, "Wall"));
		ONCHANGE_OBJECTS.add(new RoomObject(13098, 4, 7, 1, 0, "Wall"));
		ONCHANGE_OBJECTS.add(new RoomObject(13098, 7, 4, 2, 0, "Wall"));
		ONCHANGE_OBJECTS.add(new RoomObject(13098, 7, 3, 2, 0, "Wall"));
		ONCHANGE_OBJECTS.add(new RoomObject(13098, 4, 0, 3, 0, "Wall"));
		ONCHANGE_OBJECTS.add(new RoomObject(13098, 3, 0, 3, 0, "Wall"));
	}

	/**
	 * Ran when the house loads the room
	 */
	public void onLoad(Client client) {
		if (!client.getHouse().isBuildMode()) {
			for (RoomObject object : OBJECTS) {
				removeObject(client, object);
			}
		} else {
			for (RoomObject object : OBJECTS) {
				spawnObject(client, object);
			}
		}
		
		for (RoomObject object : GLOBAL_REPLACE_OBJECTS) {
			if (object.getId() == -1) {
				RoomObject rm = getObjectByPosition(object.getX(), object.getY());
				if (rm == null) {
//					System.err.println("Global Replacement Object missing parent: " + object.getX() + ", " + object.getY());
					continue;
				}
				removeObject(client, rm);
			} else {
				spawnObject(client, object);
			}
		}

		for (RoomObject object : CUSTOM_OBJECTS) {
			spawnObject(client, object);
		}
	}
	
	public void delete(Client client) {
		for (RoomObject object : OBJECTS) {
			removeObject(client, object);
		}
		
		for (RoomObject object : ONCHANGE_OBJECTS) {
			spawnObject(client, object);
		}
		
		for (RoomObject object : GLOBAL_REPLACE_OBJECTS) {
			RoomObject rm = getObjectByPosition(object.getX(), object.getY());
			if (rm == null) {
//				System.err.println("Global Replacement Object missing parent: " + object.getX() + ", " + object.getY());
				continue;
			}
			removeObject(client, rm);
		}
		
		for (RoomObject object : CUSTOM_OBJECTS) {
			removeObject(client, object);
		}
	}

	public void newObject(Client client, RoomObject object) {
		CUSTOM_OBJECTS.add(object);
		spawnObject(client, object);
	}

	public void removeCustomObject(Client client, RoomObject object) {
		RoomObject toRemove = getCustomObjectByPosition(object.getX(), object.getY());
		
		CUSTOM_OBJECTS.remove(toRemove);
		spawnObject(client, object);
	}

	public void spawnObject(Client client, RoomObject object) {
		ObjectDef objectDef = ObjectDef.getObjectDef(object.getId());

		if (objectDef == null) {
			System.err.println("There is no object definition for object " + object.getId());
			return;
		}

		int[] newCoords = Test1.getNewObjectCoords(getRotation(), new int[] { object.getX(), object.getY() });

		int defaultObjectRotation = object.getRotation()/*getObjectRotation(object.getX(), object.getY())*/;

		if (object.getX() == 3 && object.getY() == 3) {
			newCoords[0] = object.getX();
			newCoords[1] = object.getY();
		} else {
			newCoords[0] = Map.getNewXCoord(rotation, (object.getRotation() == 0 || object.getRotation() == 2) ? objectDef.tileSizeY : objectDef.tileSizeX, object.getX() & 7, object.getY() & 7,
					(object.getRotation() == 0 || object.getRotation() == 2) ? objectDef.tileSizeX : objectDef.tileSizeY);
			newCoords[1] = Map.getNewYCoord(object.getY() & 7, (object.getRotation() == 0 || object.getRotation() == 2) ? objectDef.tileSizeY : objectDef.tileSizeX, rotation, (object.getRotation() == 0 || object.getRotation() == 2) ? objectDef.tileSizeX
					: objectDef.tileSizeY, object.getX() & 7);
		}

		int newX = newCoords[0], newY = newCoords[1];

		client.getPA().checkObjectSpawn(object.getId(), (((getPosition()[0] - 2) * 8) + newX), (((getPosition()[1] - 2) * 8) + newY), defaultObjectRotation + rotation & 3, object.getType());
	}

	public void removeObject(Client client, RoomObject object) {		
		ObjectDef objectDef = ObjectDef.getObjectDef(object.getId());

		if (objectDef == null) {
			System.err.println("There is no object definition for object " + object.getId());
			return;
		}

		int[] newCoords = Test1.getNewObjectCoords(getRotation(), new int[] { object.getX(), object.getY() });

		int defaultObjectRotation = getObjectRotation(object.getX(), object.getY());

		if (object.getX() == 3 && object.getY() == 3) {
			newCoords[0] = object.getX();
			newCoords[1] = object.getY();
		} else {
			newCoords[0] = Map.getNewXCoord(rotation, (object.getRotation() == 0 || object.getRotation() == 2) ? objectDef.tileSizeY : objectDef.tileSizeX, object.getX() & 7, object.getY() & 7,
					(object.getRotation() == 0 || object.getRotation() == 2) ? objectDef.tileSizeX : objectDef.tileSizeY);
			newCoords[1] = Map.getNewYCoord(object.getY() & 7, (object.getRotation() == 0 || object.getRotation() == 2) ? objectDef.tileSizeY : objectDef.tileSizeX, rotation, (object.getRotation() == 0 || object.getRotation() == 2) ? objectDef.tileSizeX
					: objectDef.tileSizeY, object.getX() & 7);
		}

		int newX = newCoords[0], newY = newCoords[1];
		
		client.getPA().checkObjectSpawn(-1, (((getPosition()[0] - 2) * 8) + newX), (((getPosition()[1] - 2) * 8) + newY), defaultObjectRotation + rotation & 3, object.getType());
	}

	/**
	 * Get an objects rotation based on x and y coords of the room
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public int getObjectRotation(int x, int y) {
		for (RoomObject object : OBJECTS) {
			if (object.getX() == x && object.getY() == y)
				return object.getRotation() + rotation & 3;
		}
		return 0;
	}

	/**
	 * Get a object by the position that it is in, in the room
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public RoomObject getObjectByPosition(int x, int y) {
		for (RoomObject object : OBJECTS) {
			if (object.getX() == x && object.getY() == y)
				return object;
		}
		return null;
	}
	
	/**
	 * Get a room object by the position that it is in, in the room
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public RoomObject getCustomObjectByPosition(int x, int y) {
		for (RoomObject object : CUSTOM_OBJECTS) {
			if (object.getX() == x && object.getY() == y)
				return object;
		}
		return null;
	}

	/**
	 * Get the position of the room
	 * 
	 * @return
	 */
	public int[] getPosition() {
		return position;
	}

	/**
	 * Set the position of the room
	 * 
	 * @param rotation
	 */
	public void setPosition(int[] position) {
		this.position = position;
	}

	/**
	 * Get the rooms name
	 * 
	 * @return The rooms name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the rooms name
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the rooms class name
	 * 
	 * @return The rooms name
	 */
	public String getClassName() {
		return clazz;
	}

	/**
	 * Get the x coord of where the room is located
	 * 
	 * @return
	 */
	public int getX() {
		return x;
	}

	/**
	 * Get the y coord of where the room is located
	 * 
	 * @return
	 */
	public int getY() {
		return y;
	}

	/**
	 * Get the rotation of the room
	 * 
	 * @return
	 */
	public int getRotation() {
		return rotation;
	}

	/**
	 * Set the rotation of the room
	 * 
	 * @param rotation
	 */
	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

	/**
	 * Get the objects for this room
	 * 
	 * @return
	 */
	public List<RoomObject> getObjects() {
		return OBJECTS;
	}

	/**
	 * Set the objects for this room
	 * 
	 * @param objects
	 */
	public void setObjects(List<RoomObject> objects) {
		OBJECTS = objects;
	}

	/**
	 * Get the global replacement objects for this room
	 * 
	 * @return
	 */
	public List<RoomObject> getGlobalReplacementObjects() {
		return GLOBAL_REPLACE_OBJECTS;
	}

	/**
	 * Set the global replacement objects for this room
	 * 
	 * @param objects
	 */
	public void setGlobalReplacementObjects(List<RoomObject> objects) {
		GLOBAL_REPLACE_OBJECTS = objects;
	}
	
	/**
	 * Get the objects that need to be respawned when the room rotates objects
	 * for this room
	 * 
	 * @return
	 */
	public List<RoomObject> getOnChangeObjects() {
		return ONCHANGE_OBJECTS;
	}

	/**
	 * Set the on change objects for this room
	 * 
	 * @param objects
	 */
	public void setOnChangeObjects(List<RoomObject> objects) {
		ONCHANGE_OBJECTS = objects;
	}
	
	/**
	 * Get the custom objects for this room
	 * 
	 * @return
	 */
	public List<RoomObject> getCustomObjects() {
		return CUSTOM_OBJECTS;
	}

	/**
	 * Set the customs objects for this room
	 * 
	 * @param objects
	 */
	public void setCustomObjects(List<RoomObject> customObjects) {
		CUSTOM_OBJECTS = customObjects;
	}

}
