package Ghreborn.model.players.skills.construction.rooms;

import java.util.ArrayList;
import java.util.List;

import Ghreborn.model.players.skills.construction.Room;
import Ghreborn.model.players.skills.construction.RoomObject;

public class PortalNexus extends Room {

	public PortalNexus() {
		super(1880, 5760);
		setName("Portal Nexus");
		List<RoomObject> objects = new ArrayList<RoomObject>();
		objects.add(new RoomObject(33346, 3, 3, 0, "Portal Nexus Space"));
		objects.add(new RoomObject(33350, 0, 0, 0, "Rug Space"));
		objects.add(new RoomObject(15263, 2, 0, 0, 5, "Curtain"));
		objects.add(new RoomObject(15263, 5, 0, 0, 5, "Curtain"));
		objects.add(new RoomObject(15263, 2, 7, 0, 5, "Curtain"));
		objects.add(new RoomObject(15263, 5, 7, 0, 5, "Curtain"));
		objects.add(new RoomObject(15263, 7, 2, 0, 5, "Curtain"));
		objects.add(new RoomObject(15263, 7, 5, 0, 5, "Curtain"));
		objects.add(new RoomObject(15263, 0, 2, 0, 5, "Curtain"));
		objects.add(new RoomObject(15263, 0, 5, 0, 5, "Curtain"));
		objects.add(new RoomObject(15313, 0, 4, 0, 0, "Door hotspot"));
		objects.add(new RoomObject(15314, 0, 3, 0, 0, "Door hotspot"));
		objects.add(new RoomObject(15313, 7, 4, 2, 0, "Door hotspot"));
		objects.add(new RoomObject(15314, 7, 3, 2, 0, "Door hotspot"));
		objects.add(new RoomObject(15313, 4, 0, 3, 0, "Door hotspot"));
		objects.add(new RoomObject(15314, 3, 0, 3, 0, "Door hotspot"));
		objects.add(new RoomObject(15313, 4, 7, 1, 0, "Door hotspot"));
		objects.add(new RoomObject(15314, 3, 7, 1, 0, "Door hotspot"));
		setObjects(objects);
		
		List<RoomObject> globalReplace = new ArrayList<RoomObject>();
		globalReplace.add(new RoomObject(13099, 2, 0, 3, 0, "Window"));
		globalReplace.add(new RoomObject(13099, 5, 0, 3, 0, "Window"));
		globalReplace.add(new RoomObject(13099, 2, 7, 1, 0, "Window"));
		globalReplace.add(new RoomObject(13099, 5, 7, 1, 0, "Window"));
		globalReplace.add(new RoomObject(13099, 7, 2, 2, 0, "Window"));
		globalReplace.add(new RoomObject(13099, 7, 5, 2, 0, "Window"));
		globalReplace.add(new RoomObject(13099, 0, 2, 0, 0, "Window"));
		globalReplace.add(new RoomObject(13099, 0, 5, 0, 0, "Window"));
		globalReplace.add(new RoomObject(-1, 2, 0, 0, 5, "Curtain"));
		globalReplace.add(new RoomObject(-1, 5, 0, 0, 5, "Curtain"));
		globalReplace.add(new RoomObject(-1, 2, 7, 0, 5, "Curtain"));
		globalReplace.add(new RoomObject(-1, 5, 7, 0, 5, "Curtain"));
		globalReplace.add(new RoomObject(-1, 7, 2, 0, 5, "Curtain"));
		globalReplace.add(new RoomObject(-1, 7, 5, 0, 5, "Curtain"));
		globalReplace.add(new RoomObject(-1, 0, 2, 0, 5, "Curtain"));
		globalReplace.add(new RoomObject(-1, 0, 5, 0, 5, "Curtain"));
		this.setGlobalReplacementObjects(globalReplace);
	}

}