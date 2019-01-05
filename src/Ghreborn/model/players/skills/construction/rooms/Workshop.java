package Ghreborn.model.players.skills.construction.rooms;

import java.util.ArrayList;
import java.util.List;

import Ghreborn.model.players.skills.construction.Room;
import Ghreborn.model.players.skills.construction.RoomObject;

public class Workshop extends Room {

	public Workshop() {
		super(1856, 5736);
		setName("Workshop");
		
		List<RoomObject> objects = new ArrayList<RoomObject>();
		objects.add(new RoomObject(15439, 3, 4, 0, "Workbench space"));
		objects.add(new RoomObject(15441, 0, 3, 3, "Clockmaking space"));
		objects.add(new RoomObject(15448, 7, 3, 1, "Repair space"));
		objects.add(new RoomObject(15450, 7, 6, 1, "Heraldry space"));
		
		objects.add(new RoomObject(15443, 1, 0, 3, 5, "Tool space"));
		objects.add(new RoomObject(15444, 6, 0, 3, 5, "Tool space"));
		objects.add(new RoomObject(15445, 0, 1, 0, 5, "Tool space"));
		objects.add(new RoomObject(15446, 7, 1, 2, 5, "Tool space"));
		objects.add(new RoomObject(15447, 0, 6, 0, 5, "Tool space"));
		
		objects.add(new RoomObject(15313, 3, 7, 1, 0, "Door hotspot"));
		objects.add(new RoomObject(15314, 4, 7, 1, 0, "Door hotspot"));
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
		globalReplace.add(new RoomObject(-1, 1, 0, 3, 5, "Tool space"));
		globalReplace.add(new RoomObject(-1, 6, 0, 3, 5, "Tool space"));
		globalReplace.add(new RoomObject(-1, 0, 1, 0, 5, "Tool space"));
		globalReplace.add(new RoomObject(-1, 7, 1, 2, 5, "Tool space"));
		globalReplace.add(new RoomObject(-1, 0, 6, 0, 5, "Tool space"));
		setGlobalReplacementObjects(globalReplace);
	}
	
}
