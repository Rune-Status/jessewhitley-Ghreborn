package Ghreborn.model.players.skills.construction.rooms;

import java.util.ArrayList;
import java.util.List;

import Ghreborn.model.players.skills.construction.Room;
import Ghreborn.model.players.skills.construction.RoomObject;

public class Throne extends Room {

	public Throne() {
		super(1904, 5096);
		setName("Throne");
		
		List<RoomObject> objects = new ArrayList<RoomObject>();		
		objects.add(new RoomObject(15427, 3, 3, 3, 22, "Floor space"));
		objects.add(new RoomObject(15427, 3, 4, 0, 22, "Floor space"));
		objects.add(new RoomObject(15427, 4, 4, 2, 22, "Floor space"));
		objects.add(new RoomObject(15427, 4, 3, 1, 22, "Floor space"));
		objects.add(new RoomObject(15438, 1, 6, 0, 22, "Trapdoor space"));
		objects.add(new RoomObject(15426, 3, 6, 0, "Throne space"));
		objects.add(new RoomObject(15426, 4, 6, 0, "Throne space"));
		objects.add(new RoomObject(15435, 6, 6, 0, "Lever space"));
		
		objects.add(new RoomObject(15434, 3, 7, 1, 5, "Decoration space"));
		objects.add(new RoomObject(15434, 4, 7, 1, 5, "Decoration space"));
		
		for (int y = 0; y < 6; y++) {
			objects.add(new RoomObject(15436, 0, y, 3, "Seating space"));
		}
		
		for (int y = 0; y < 6; y++) {
			objects.add(new RoomObject(15437, 7, y, 1, "Seating space"));
		}
		
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
		globalReplace.add(new RoomObject(-1, 3, 3, 3, 22, "Floor space"));
		globalReplace.add(new RoomObject(-1, 3, 4, 0, 22, "Floor space"));
		globalReplace.add(new RoomObject(-1, 4, 4, 2, 22, "Floor space"));
		globalReplace.add(new RoomObject(-1, 4, 3, 1, 22, "Floor space"));
		globalReplace.add(new RoomObject(-1, 3, 7, 1, 5, "Decoration space"));
		globalReplace.add(new RoomObject(-1, 4, 7, 1, 5, "Decoration space"));
		setGlobalReplacementObjects(globalReplace);
	}
	
}
