package Ghreborn.model.players.skills.construction.rooms;

import java.util.ArrayList;
import java.util.List;

import Ghreborn.model.players.skills.construction.Room;
import Ghreborn.model.players.skills.construction.RoomObject;

public class Parlour extends Room {

	public Parlour() {
		super(1856, 5752);
		setName("Parlour");
		
		List<RoomObject> objects = new ArrayList<RoomObject>();
		objects.add(new RoomObject(4521, 0, 1, 0, "Bookcase space"));
		objects.add(new RoomObject(4521, 7, 1, 2, "Bookcase space"));
		objects.add(new RoomObject(4512, 7, 1, 2, "Bookcase space"));
		objects.add(new RoomObject(4516, 2, 4, 2, 11, "Chair space"));
		objects.add(new RoomObject(4517, 5, 4, 1, 11, "Chair space"));
		objects.add(new RoomObject(4515, 4, 3, 2, "Chair space"));
		objects.add(new RoomObject(4523, 3, 7, 1, "Fireplace space"));
		
		objects.add(new RoomObject(15313, 0, 4, 0, 0, "Door hotspot"));
		objects.add(new RoomObject(15314, 0, 3, 0, 0, "Door hotspot"));
		objects.add(new RoomObject(15313, 7, 4, 2, 0, "Door hotspot"));
		objects.add(new RoomObject(15314, 7, 3, 2, 0, "Door hotspot"));
		objects.add(new RoomObject(15313, 4, 0, 3, 0, "Door hotspot"));
		objects.add(new RoomObject(15314, 3, 0, 3, 0, "Door hotspot"));
		
		objects.add(new RoomObject(15263, 2, 0, 0, 5, "Curtain"));
		objects.add(new RoomObject(15263, 5, 0, 0, 5, "Curtain"));
		objects.add(new RoomObject(15263, 2, 7, 0, 5, "Curtain"));
		objects.add(new RoomObject(15263, 5, 7, 0, 5, "Curtain"));
		objects.add(new RoomObject(15263, 7, 2, 0, 5, "Curtain"));
		objects.add(new RoomObject(15263, 7, 5, 0, 5, "Curtain"));
		objects.add(new RoomObject(15263, 0, 2, 0, 5, "Curtain"));
		objects.add(new RoomObject(15263, 0, 5, 0, 5, "Curtain"));
		
		/*for (int x = 0; x < 6; x++) {
			for (int y = 0; y < 4; y++) {
				objects.add(new RoomObject(15264, 1 + x, 1 + y, 0, 22, "Rug space"));
			}
		}*/
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
