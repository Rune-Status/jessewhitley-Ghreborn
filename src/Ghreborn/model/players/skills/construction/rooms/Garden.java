package Ghreborn.model.players.skills.construction.rooms;

import java.util.ArrayList;
import java.util.List;

import Ghreborn.model.players.skills.construction.Room;
import Ghreborn.model.players.skills.construction.RoomObject;

public class Garden extends Room {

	public Garden() {
		super(1859, 5706);
		setName("Garden");

		List<RoomObject> objects = new ArrayList<RoomObject>();
		objects.add(new RoomObject(15361, 3, 3, 1, "Centrepiece space"));
		objects.add(new RoomObject(15362, 1, 5, 0, "Big tree space"));
		objects.add(new RoomObject(15363, 6, 6, 0, "Tree space"));
		objects.add(new RoomObject(15364, 6, 0, 0, "Big plant space 1"));
		objects.add(new RoomObject(15365, 0, 0, 0, "Big plant space 2"));
		objects.add(new RoomObject(15366, 3, 1, 0, "Small plant space 1"));
		objects.add(new RoomObject(15367, 4, 5, 0, "Small plant space 2"));
		objects.add(new RoomObject(29119, 6, 5, 0, "Tip jar space"));
		
		objects.add(new RoomObject(15313, 0, 4, 0, 0, "Door hotspot"));
		objects.add(new RoomObject(15314, 0, 3, 0, 0, "Door hotspot"));
		objects.add(new RoomObject(15313, 3, 7, 1, 0, "Door hotspot"));
		objects.add(new RoomObject(15314, 4, 7, 1, 0, "Door hotspot"));
		objects.add(new RoomObject(15313, 7, 4, 2, 0, "Door hotspot"));
		objects.add(new RoomObject(15314, 7, 3, 2, 0, "Door hotspot"));
		objects.add(new RoomObject(15313, 4, 0, 3, 0, "Door hotspot"));
		objects.add(new RoomObject(15314, 3, 0, 3, 0, "Door hotspot"));
		setObjects(objects);
		
		List<RoomObject> globalReplace = new ArrayList<RoomObject>();
		globalReplace.add(new RoomObject(4525, 3, 3, 1, "Portal"));
		setGlobalReplacementObjects(globalReplace);
	}

}
