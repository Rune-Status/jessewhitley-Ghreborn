package Ghreborn.model.players.skills.construction.rooms;

import java.util.ArrayList;
import java.util.List;

import Ghreborn.model.players.skills.construction.Room;
import Ghreborn.model.players.skills.construction.RoomObject;

public class Study extends Room {

	public Study() {
		super(1888, 5096);
		setName("Study");
		
		List<RoomObject> objects = new ArrayList<RoomObject>();
		objects.add(new RoomObject(15420, 2, 2, 2, "Lectern space"));
		objects.add(new RoomObject(15421, 1, 4, 0, "Globe space"));
		objects.add(new RoomObject(15422, 5, 4, 2, "Crystal ball space"));
		objects.add(new RoomObject(15424, 5, 7, 2, "Telescope space"));
		objects.add(new RoomObject(15424, 5, 7, 2, "Telescope space"));
		objects.add(new RoomObject(15425, 3, 7, 1, "Bookcase space"));
		objects.add(new RoomObject(15425, 4, 7, 1, "Bookcase space"));
		
		objects.add(new RoomObject(15423, 0, 1, 0, 5, "Wall chart space"));
		objects.add(new RoomObject(15423, 1, 7, 1, 5, "Wall chart space"));
		objects.add(new RoomObject(15423, 6, 7, 1, 5, "Wall chart space"));
		objects.add(new RoomObject(15423, 7, 1, 2, 5, "Wall chart space"));
		objects.add(new RoomObject(15313, 0, 4, 0, 0, "Door hotspot"));
		objects.add(new RoomObject(15314, 0, 3, 0, 0, "Door hotspot"));
		objects.add(new RoomObject(15313, 7, 4, 2, 0, "Door hotspot"));
		objects.add(new RoomObject(15314, 7, 3, 2, 0, "Door hotspot"));
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
		globalReplace.add(new RoomObject(-1, 0, 1, 0, 5, "Wall chart space"));
		globalReplace.add(new RoomObject(-1, 1, 7, 1, 5, "Wall chart space"));
		globalReplace.add(new RoomObject(-1, 6, 7, 1, 5, "Wall chart space"));
		globalReplace.add(new RoomObject(-1, 7, 1, 2, 5, "Wall chart space"));
		setGlobalReplacementObjects(globalReplace);
	}
}
