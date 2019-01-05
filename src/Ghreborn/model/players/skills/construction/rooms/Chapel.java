package Ghreborn.model.players.skills.construction.rooms;

import java.util.ArrayList;
import java.util.List;

import Ghreborn.model.players.skills.construction.Room;
import Ghreborn.model.players.skills.construction.RoomObject;

public class Chapel extends Room {

	public Chapel() {
		super(1872, 5736);
		setName("Chapel");
		
		List<RoomObject> objects = new ArrayList<RoomObject>();
		objects.add(new RoomObject(15275, 0, 0, 2, 11, "Statue space"));
		objects.add(new RoomObject(15275, 7, 0, 1, 11, "Statue space"));
		objects.add(new RoomObject(15271, 1, 5, 0, "Lamp space"));
		objects.add(new RoomObject(15271, 6, 5, 0, "Lamp space"));
		objects.add(new RoomObject(15276, 7, 3, 1, "Musical space"));
		objects.add(new RoomObject(15269, 3, 7, 0, "Icon space"));
		objects.add(new RoomObject(15270, 3, 5, 2, "Altar space"));
		for (int x = 3; x < 5; x++) {
			for (int y = 0; y < 5; y++) {
				objects.add(new RoomObject(15273, x, y, 0, 22, "Rug space"));
			}
		}
		objects.add(new RoomObject(15313, 0, 4, 0, 0, "Door hotspot"));
		objects.add(new RoomObject(15314, 0, 3, 0, 0, "Door hotspot"));
		objects.add(new RoomObject(15313, 4, 0, 3, 0, "Door hotspot"));
		objects.add(new RoomObject(15314, 3, 0, 3, 0, "Door hotspot"));
		setObjects(objects);
		
		List<RoomObject> globalReplace = new ArrayList<RoomObject>();
		globalReplace.add(new RoomObject(13099, 2, 7, 1, 0, "Window"));
		globalReplace.add(new RoomObject(13099, 5, 7, 1, 0, "Window"));
		globalReplace.add(new RoomObject(13099, 7, 2, 2, 0, "Window"));
		globalReplace.add(new RoomObject(13099, 7, 5, 2, 0, "Window"));
		globalReplace.add(new RoomObject(13099, 0, 2, 0, 0, "Window"));
		globalReplace.add(new RoomObject(13099, 0, 5, 0, 0, "Window"));
		globalReplace.add(new RoomObject(-1, 0, 0, 2, 11, "Statue space"));
		globalReplace.add(new RoomObject(-1, 7, 0, 1, 11, "Statue space"));
		globalReplace.add(new RoomObject(-1, 1, 5, 0, "Lamp space"));
		globalReplace.add(new RoomObject(-1, 6, 5, 0, "Lamp space"));
		globalReplace.add(new RoomObject(-1, 7, 3, 1, "Musical space"));
		globalReplace.add(new RoomObject(-1, 3, 7, 0, "Icon space"));
		for (int x = 3; x < 5; x++) {
			for (int y = 0; y < 5; y++) {
				globalReplace.add(new RoomObject(-1, x, y, 0, 22, "Rug space"));
			}
		}
		setGlobalReplacementObjects(globalReplace);
	}
}
