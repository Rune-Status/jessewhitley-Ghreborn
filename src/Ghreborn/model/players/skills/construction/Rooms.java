package Ghreborn.model.players.skills.construction;

import Ghreborn.model.players.skills.construction.rooms.CostumeRoom;
import Ghreborn.model.players.skills.construction.rooms.Default;
import Ghreborn.model.players.skills.construction.rooms.Game;
import Ghreborn.model.players.skills.construction.rooms.Garden;
import Ghreborn.model.players.skills.construction.rooms.Throne;


public class Rooms {
	
	public static final Room DEFAULT = new Default();
	public static final Room GARDEN = new Garden();
	public static final Room THRONE = new Throne();
	public static final Room GAME = new Game();
	public static final Room COSTUME_ROOM = new CostumeRoom();
	
	/*public static final Room DEFAULT = new Room(1856, 5056, 0, 0);
	public static final Room GARDEN = new Room(1859, 5066, 0, 0);
	public static final Room THRONE = new Room(1904, 5096, 0, 0);
	public static final Room THRONE_ROOM = new Room(1904, 5080, 0, 0);
	public static final Room GAME = new Room(1960, 5088, 0, 0);
	public static final Room PARLOUR = new Room(1856, 5112, 0, 0);
	public static final Room KITCHEN = new Room(1872, 5112, 0, 0);
	public static final Room DINING = new Room(1890, 5112, 0, 0);
	public static final Room WORKSHOP = new Room(1856, 5096, 0, 0);
	public static final Room BEDROOM = new Room(1904, 5112, 0, 0);
	public static final Room SKILLHALL = new Room(1880, 5104, 0, 0);
	public static final Room COMBAT = new Room(1880, 5088, 0, 0);
	public static final Room QUEST_HALL = new Room(1964, 5104, 0, 0);
	public static final Room STUDY = new Room(1888, 5096, 0, 0);
	public static final Room COSTUME_ROOM = new Room(1904, 5064, 0, 0);
	public static final Room CHAPEL = new Room(1872, 5096, 0, 0);
	public static final Room PORTAL_CHAMBER = new Room(1864, 5088, 0, 0);
	public static final Room FORMAL_GARDEN = new Room(1872, 5064, 0, 0);
	public static final Room THRONE_ROOM = new Room(1904, 5080, 0, 0);
	public static final Room OUBILIETTE = new Room(1896, 5072, 0, 0);
	public static final Room CORRIDOR_DUNGEON = new Room(1888, 5080, 0, 0);
	public static final Room JUNCTION_DUNGEON = new Room(1856, 5080, 0, 0);
	public static final Room STAIRS_DUNGEON = new Room(1872, 5080, 0, 0);
	public static final Room TREASURE_ROOM = new Room(1912, 5088, 0, 0);

	private final static Object[][] CON_ROOMS = new Object[][] { 
		{1, GARDEN},
		{1, PARLOUR},
		{5, KITCHEN},
		{10, DINING},
		{15, THRONE},
		{15, WORKSHOP},
		{15, BEDROOM},
		{25, SKILLHALL},
		{30, GAME},
		{32, COMBAT},
		{35, QUEST_HALL},
		{40, STUDY},
		{42, COSTUME_ROOM},
		{45, CHAPEL},
		{50, PORTAL_CHAMBER},
		{55, FORMAL_GARDEN},
		{60, THRONE_ROOM},
		{65, OUBILIETTE},
		{70, CORRIDOR_DUNGEON},
		{70, JUNCTION_DUNGEON},
		{70, STAIRS_DUNGEON},
		{75, TREASURE_ROOM}
		};
	
	public static Room Build(Client c) {		
		if (c.playerLevel[Skills.CONSTRUCTION] >= (int) CON_ROOMS[c.RoomClicked][0]) {
			return (Room) CON_ROOMS[c.RoomClicked][1];
		}
		return DEFAULT;
	}*/

}
