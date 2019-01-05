package Ghreborn.model.players.skills.construction.rooms;

import java.util.ArrayList;
import java.util.List;

import Ghreborn.model.players.skills.construction.Room;
import Ghreborn.model.players.skills.construction.RoomObject;

public class Kitchen extends Room {

	public Kitchen() {
		super(1872, 5752);
		setName("Kitchen");
		
		List<RoomObject> objects = new ArrayList<RoomObject>();
		objects.add(new RoomObject(15398, 3, 7, 1, "Stove space"));
		objects.add(new RoomObject(15399, 7, 6, 2, 5, "Shelf space"));
		objects.add(new RoomObject(15400, 1, 7, 1, 5, "Shelf space"));
		objects.add(new RoomObject(15400, 6, 7, 1, 5, "Shelf space"));
		objects.add(new RoomObject(15401, 0, 6, 3, "Barrel space"));
		objects.add(new RoomObject(15402, 0, 0, 0, 22, "Cat basket space"));
		objects.add(new RoomObject(15403, 6, 0, 2, "Larder space"));
		objects.add(new RoomObject(15404, 7, 3, 2, "Sink space"));
		objects.add(new RoomObject(15405, 3, 3, 2, "Table space"));
		objects.add(new RoomObject(15313, 0, 4, 0, 0, "Door hotspot"));
		objects.add(new RoomObject(15314, 0, 3, 0, 0, "Door hotspot"));
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
		globalReplace.add(new RoomObject(-1, 2, 0, 0, 5, "Curtain"));
		globalReplace.add(new RoomObject(-1, 5, 0, 0, 5, "Curtain"));
		globalReplace.add(new RoomObject(-1, 2, 7, 0, 5, "Curtain"));
		globalReplace.add(new RoomObject(-1, 5, 7, 0, 5, "Curtain"));
		globalReplace.add(new RoomObject(-1, 7, 2, 0, 5, "Curtain"));
		globalReplace.add(new RoomObject(-1, 7, 5, 0, 5, "Curtain"));
		globalReplace.add(new RoomObject(-1, 0, 2, 0, 5, "Curtain"));
		globalReplace.add(new RoomObject(-1, 0, 5, 0, 5, "Curtain"));
		setGlobalReplacementObjects(globalReplace);
	}
	
}
