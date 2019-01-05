package Ghreborn.model.players.skills.construction.rooms;

import java.util.ArrayList;
import java.util.List;

import Ghreborn.model.players.skills.construction.Room;
import Ghreborn.model.players.skills.construction.RoomObject;

public class Bedroom extends Room {
	
	public Bedroom() {
		super(1904, 5112);
		setName("Bedroom");
		
		List<RoomObject> objects = new ArrayList<RoomObject>();
		objects.add(new RoomObject(15260, 3, 6, 0, "Bed space"));
		objects.add(new RoomObject(15262, 0, 7, 0, "Dresser space"));
		objects.add(new RoomObject(15261, 6, 7, 0, "Wardrobe space"));
		objects.add(new RoomObject(15267, 7, 3, 2, "Fireplace space"));
		objects.add(new RoomObject(15268, 7, 0, 1, 11, "Clock space"));
		
		objects.add(new RoomObject(15313, 0, 4, 0, 0, "Door hotspot"));
		objects.add(new RoomObject(15314, 0, 3, 0, 0, "Door hotspot"));
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
		
		globalReplace.add(new RoomObject(-1, 0, 7, 0, "Dresser space"));
		globalReplace.add(new RoomObject(-1, 2, 0, 0, 5, "Curtain"));
		globalReplace.add(new RoomObject(-1, 5, 0, 0, 5, "Curtain"));
		globalReplace.add(new RoomObject(-1, 2, 7, 0, 5, "Curtain"));
		globalReplace.add(new RoomObject(-1, 5, 7, 0, 5, "Curtain"));
		globalReplace.add(new RoomObject(-1, 7, 2, 0, 5, "Curtain"));
		globalReplace.add(new RoomObject(-1, 7, 5, 0, 5, "Curtain"));
		globalReplace.add(new RoomObject(-1, 0, 2, 0, 5, "Curtain"));
		globalReplace.add(new RoomObject(-1, 0, 5, 0, 5, "Curtain"));
		
		/*for (int x = 0; x < 6; x++) {
			for (int y = 0; y < 4; y++) {
				globalReplace.add(new RoomObject(-1, 1 + x, 1 + y, 0, 22, "Rug space"));
			}
		}*/
		setGlobalReplacementObjects(globalReplace);
	}
	
}
