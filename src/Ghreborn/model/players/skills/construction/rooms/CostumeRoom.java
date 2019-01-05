package Ghreborn.model.players.skills.construction.rooms;

import java.util.ArrayList;
import java.util.List;

import Ghreborn.model.players.skills.construction.Room;
import Ghreborn.model.players.skills.construction.RoomObject;

public class CostumeRoom extends Room {

	public CostumeRoom() {
		super(1904, 5064);
		setName("Costume Room");
		
		List<RoomObject> objects = new ArrayList<RoomObject>();
		objects.add(new RoomObject(18813, 0, 3, 3, "Treasure chest space"));
		objects.add(new RoomObject(18815, 2, 7, 1, "Armour case space"));
		objects.add(new RoomObject(18811, 3, 7, 1, "Magic wardrobe space"));
		objects.add(new RoomObject(18810, 6, 6, 0, "Cape rack space"));
		objects.add(new RoomObject(18812, 7, 3, 1, "Toy box space"));
		objects.add(new RoomObject(18814, 3, 3, 0, "Fancy dress box"));
		objects.add(new RoomObject(15313, 4, 0, 3, 0, "Door hotspot"));
		objects.add(new RoomObject(15314, 3, 0, 3, 0, "Door hotspot"));
		setObjects(objects);
		
		List<RoomObject> globalReplace = new ArrayList<RoomObject>();
		globalReplace.add(new RoomObject(13099, 2, 0, 3, 0, "Window"));
		globalReplace.add(new RoomObject(13099, 5, 0, 3, 0, "Window"));
		globalReplace.add(new RoomObject(13099, 7, 2, 2, 0, "Window"));
		globalReplace.add(new RoomObject(13099, 7, 5, 2, 0, "Window"));
		globalReplace.add(new RoomObject(13099, 0, 2, 0, 0, "Window"));
		globalReplace.add(new RoomObject(13099, 0, 5, 0, 0, "Window"));
		setGlobalReplacementObjects(globalReplace);
	}

}
