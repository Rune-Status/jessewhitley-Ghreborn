package Ghreborn.model.players.skills.construction.rooms;

import java.util.ArrayList;
import java.util.List;

import Ghreborn.model.players.skills.construction.Room;
import Ghreborn.model.players.skills.construction.RoomObject;

public class PortalChamber extends Room {

	public PortalChamber() {
		super(1864, 5088);
		setName("Portal Chamber");
		
		List<RoomObject> objects = new ArrayList<RoomObject>();
		objects.add(new RoomObject(15406, 0, 3, 1, "Portal space"));
		objects.add(new RoomObject(15407, 3, 7, 2, "Portal space"));
		objects.add(new RoomObject(15408, 7, 3, 3, "Portal space"));
		objects.add(new RoomObject(15409, 3, 3, 1, "Centrepiece space"));
		objects.add(new RoomObject(15313, 4, 0, 3, 0, "Door hotspot"));
		objects.add(new RoomObject(15314, 3, 0, 3, 0, "Door hotspot"));
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
		setGlobalReplacementObjects(globalReplace);
	}
}
