package Ghreborn.model.players.skills.construction;

import com.google.gson.Gson;

import Ghreborn.Config;
import Ghreborn.Test1;
import Ghreborn.definitions.ObjectDef;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.skills.Skills;

public class Objects {

	// fake object, real object, level requirement, xp given, item1 required,
	// item1 amount required, item2 required, item2 amount required, object
	// name, animation
	public final static Object[][] CON_DATA = {
			// ** GARDEN ROOM ** //
			{ 15362, 13416, 5, 3, 8427, 1, -1, -1, "Yew tree", 0 },
			{ 15363, 13415, 5, 3, 8425, 1, -1, -1, "Maple tree", 0 },

			{ 15364, 5331, 3, 3, 8431, 1, -1, -1, "Big plant", 0 },
			{ 15365, 5331, 3, 3, 8431, 1, -1, -1, "Big plant", 0 },

			{ 15366, 5334, 1, 3, 8431, 1, -1, -1, "Small plant", 0 },
			{ 15367, 5334, 1, 3, 8431, 1, -1, -1, "Small plant", 0 },
			// ** GARDEN ROOM ** //

			// ** PARLOUR ROOM ** //
			{ 15416, 13597, 4, 3, 960, 4, 1539, 4, "Wooden bookcase", 898 },
			{ 15416, 13598, 29, 7, 8778, 3, -1, -1, "Oak bookcase", 898 },
			{ 15416, 13599, 40, 9, 8782, 3, -1, -1, "Mahogany bookcase", 898 },

			{ 15418, 13609, 10, 3, 1761, 3, -1, -1, "Clay fireplace", 898 },
			{ 15418, 13611, 31, 7, 3420, 2, -1, -1, "Limestone fireplace", 898 },
			{ 15418, 13613, 52, 9, 8786, 1, -1, -1, "Marble fireplace", 898 },
			// ** PARLOUR ROOM ** //

			// ** KITCHEN ROOM ** //
			{ 15405, 13577, 12, 11, 960, 3, 1539, 3, "Wooden table", 898 },
			{ 15405, 13578, 32, 13, 8778, 3, -1, -1, "Oak table", 898 },
			{ 15405, 13579, 42, 14, 8780, 3, -1, -1, "Teak table", 898 },

			{ 15404, 13564, 47, 17, 2353, 15, -1, -1, "Sink", 898 },

			{ 15403, 13565, 9, 13, 960, 8, 1539, 8, "Wooden larder", 898 },
			{ 15403, 13566, 33, 17, 8778, 8, -1, -1, "Oak larder", 898 },
			{ 15403, 13567, 43, 26, 8782, 8, 8790, 3, "Mahogany larder", 898 },
			// ** KITCHEN ROOM ** //

			// ** DINING ROOM ** //
			{ 15298, 13293, 10, 5, 960, 4, 1539, 4, "Wooden table", 898 },
			{ 15298, 13294, 31, 8, 8778, 6, -1, -1, "Oak table", 898 },
			{ 15298, 13296, 52, 16, 8780, 6, -1, -1, "Teak table", 898 },
			{ 15298, 13298, 52, 20, 8782, 6, -1, -1, "Mahogany table", 898 },

			{ 15300, 13300, 10, 3, 960, 4, 1539, 4, "Wooden bench", 898 },
			{ 15300, 13301, 31, 7, 8778, 6, -1, -1, "Oak bench", 898 },
			{ 15300, 13303, 52, 9, 8780, 6, -1, -1, "Teak bench", 898 },
			{ 15300, 13305, 52, 13, 8782, 6, -1, -1, "Mahogany bench", 898 },

			{ 15299, 13300, 10, 3, 960, 4, 1539, 4, "Wooden bench", 898 },
			{ 15299, 13301, 31, 7, 8778, 6, -1, -1, "Oak bench", 898 },
			{ 15299, 13303, 52, 9, 8780, 6, -1, -1, "Teak bench", 898 },
			{ 15299, 13305, 52, 13, 8782, 6, -1, -1, "Mahogany bench", 898 },

			{ 15301, 13609, 10, 3, 1761, 3, -1, -1, "Clay fireplace", 898 },
			{ 15301, 13611, 31, 7, 3420, 2, -1, -1, "Limestone fireplace", 898 },
			{ 15301, 13613, 52, 9, 8786, 1, -1, -1, "Marble fireplace", 898 },
			// ** DINING ROOM ** //

			// ** THRONE ROOM ** //
			{ 15438, 13675, 68, 13, 960, 3, -1, -1, "Oak trapdoor", 898 },
			{ 15438, 13676, 68, 13, 8780, 3, -1, -1, "Teak trapdoor", 898 },
			{ 15438, 13677, 68, 13, 8782, 3, -1, -1, "Mahogany trapdoor", 898 },

			{ 15435, 13672, 68, 13, 960, 3, -1, -1, "Oak lever", 898 },
			{ 15435, 13673, 78, 13, 8780, 3, -1, -1, "Teak lever", 898 },
			{ 15435, 13674, 88, 13, 8782, 3, -1, -1, "Mahogany lever", 898 },

			{ 15426, 13665, 60, 13, 8778, 3, 8786, 3, "Oak throne", 898 },
			{ 15426, 13666, 67, 17, 8780, 3, 8786, 3, "Teak throne", 898 },
			{ 15426, 13667, 74, 20, 8782, 3, 8786, 3, "Mahogany throne", 898 },
			{ 15426, 13667, 99, 540, 8788, 25, -1, -1, "Demonic throne", 898 },

			{ 15437, 13300, 10, 3, 960, 4, 1539, 4, "Wooden bench", 898 },
			{ 15437, 13301, 31, 7, 8778, 6, -1, -1, "Oak bench", 898 },
			{ 15437, 13303, 52, 9, 8780, 6, -1, -1, "Teak bench", 898 },
			{ 15437, 13305, 52, 13, 8782, 6, -1, -1, "Mahogany bench", 898 },
			
			{ 15436, 13300, 10, 3, 960, 4, 1539, 4, "Wooden bench", 898 },
			{ 15436, 13301, 31, 7, 8778, 6, -1, -1, "Oak bench", 898 },
			{ 15436, 13303, 52, 9, 8780, 6, -1, -1, "Teak bench", 898 },
			{ 15436, 13305, 52, 13, 8782, 6, -1, -1, "Mahogany bench", 898 },
			// ** THRONE ROOM ** //

			// ** WORK ROOM ** //
			{ 15439, 13704, 17, 10, 960, 5, 1539, 5, "Wooden workbench", 898 },
			{ 15439, 13706, 46, 11, 8778, 6, 2353, 4, "Oak workbench", 898 },
			{ 15439, 13707, 62, 12, 8778, 2, 2353, 1, "Teak workbench", 898 },
			{ 15439, 13708, 77, 13, 8778, 2, 2353, 1, "Mahogany workbench", 898 },

			{ 15450, 13714, 35, 12, 8778, 8, 3420, 1, "Whetstone", 898 },
			{ 15450, 13715, 55, 14, 8778, 8, 3420, 1, "Armour stand", 898 },

			{ 15448, 13713, 15, 12, 8778, 2, -1, -1, "Repair bench", 898 },

			{ 15441, 13712, 15, 17, 8778, 6, -1, -1, "Clockwork bench", 898 },
			// ** WORK ROOM ** //

			// ** BED ROOM ** //
			{ 15267, 13609, 10, 3, 1761, 3, -1, -1, "Clay fireplace", 898 },
			{ 15267, 13611, 31, 7, 3420, 2, -1, -1, "Limestone fireplace", 898 },
			{ 15267, 13613, 52, 9, 8786, 1, -1, -1, "Marble fireplace", 898 },

			{ 15260, 13148, 20, 3, 1761, 3, 1539, 3, "Oak Bed", 898 },
			{ 15260, 13152, 30, 7, 3420, 3, 8790, 3, "Teak Bed", 898 },
			{ 15260, 13154, 40, 13, 8786, 5, 8790, 3, "Mahogany Bed", 898 },

			{ 15268, 13169, 25, 3, 8778, 3, 8792, 1, "Oak Clock", 898 },
			{ 15268, 13170, 55, 7, 8780, 3, 8792, 1, "Teak Clock", 898 },
			{ 15268, 13171, 85, 13, 8782, 3, 8792, 1, "Mahogany Clock", 898 },

			{ 15261, 13157, 27, 3, 8778, 3, -1, -1, "Oak Wardrobe", 898 },
			{ 15261, 13159, 51, 7, 8780, 3, -1, -1, "Teak Wardrobe", 898 },
			{ 15261, 13160, 75, 13, 8782, 3, -1, -1, "Mahogany Wardrobe", 898 },
			// ** BED ROOM ** //

			// ** SKILL HALL ** //
			{ 15384, 13491, 28, 11, 8778, 3, 1121, -1, "Mithril armour", 898 },
			{ 15384, 13492, 28, 12, 8780, 3, 1123, -1, "Adamant armour", 898 },
			{ 15384, 13493, 28, 13, 8782, 3, 1127, 1, "Rune armour", 898 },

			{ 34225, 13491, 28, 11, 8778, 3, 1121, -1, "Mithril armour", 898 },
			{ 34225, 13492, 28, 12, 8780, 3, 1123, -1, "Adamant armour", 898 },
			{ 34225, 13493, 28, 13, 8782, 3, 1127, 1, "Rune armour", 898 },

			{ 15382, 13493, 58, 30, 7979, 1, -1, -1, "Abyssal demon head", 898 },

			{ 15386, 13493, 58, 10, 8782, 3, 1127, 1, "Rune display case", 898 },

			{ 15381, 13498, 40, 9, 8778, 1, -1, -1, "Staircase", 898 },

			{ 15383, 13490, 40, 9, 8778, 3, 383, 1, "Mounted shark", 898 },
			// ** SKILL HALL ** //

			// ** GAME ROOM ** //
			{ 15342, 13404, 59, 10, 8780, 12, 8784, 1, "Hangman game", 898 },

			{ 15343, 13385, 34, 10, 8778, 4, 8784, 1, "Oak prizechest", 898 },
			{ 15343, 13385, 44, 10, 8780, 4, 8784, 1, "Teak prizechest", 898 },
			{ 15343, 13385, 54, 10, 8782, 4, 8784, 1, "Mahogany prizechest", 898 },
			// ** GAME ROOM ** //

			// ** COMBAT ROOM ** //
			// ** COMBAT ROOM ** //

			// ** QUESTHALL ROOM ** //
			{ 15397, 13597, 4, 3, 960, 4, 1539, 4, "Wooden bookcase", 898 },
			{ 15397, 13598, 29, 7, 8778, 3, -1, -1, "Oak bookcase", 898 },
			{ 15397, 13599, 40, 9, 8782, 3, -1, -1, "Mahogany bookcase", 898 },

			{ 15390, 13497, 40, 9, 8778, 1, -1, -1, "Staircase", 898 },
			// ** QUESTHALL ROOM ** //

			// ** STUDY ROOM ** //
			{ 15421, 13649, 41, 9, 8778, 3, -1, -1, "Oak globe", 898 },
			{ 15421, 13650, 50, 14, 8780, 3, -1, -1, "Teak globe", 898 },
			{ 15421, 13649, 59, 18, 8780, 3, 8784, 1, "Gilded teak globe", 898 },

			{ 15425, 13597, 4, 3, 960, 4, 1539, 4, "Wooden bookcase", 898 },
			{ 15425, 13598, 29, 7, 8778, 3, -1, -1, "Oak bookcase", 898 },
			{ 15425, 13599, 40, 9, 8782, 3, -1, -1, "Mahogany bookcase", 898 },

			{ 15422, 13659, 42, 13, 8778, 4, -1, -1, "Oak Crystal ball", 898 },
			{ 15422, 13660, 54, 15, 8778, 3, 8784, 1, "Gilded oak crystal ball", 898 },
			{ 15422, 13661, 66, 17, 8782, 3, 8784, 2, "Gilded mahogany crystal ball", 898 },

			{ 15420, 13642, 40, 3, 8778, 1, -1, -1, "Oak lecetern", 898 },
			{ 15420, 13644, 47, 7, 8778, 2, -1, -1, "Teak lecetern", 898 },
			{ 15420, 13648, 67, 9, 8782, 2, 8784, 2, "Mahogany lecetern", 898 },

			{ 15424, 13656, 44, 8, 8778, 1, 1775, 1, "Oak Telescope", 898 },
			{ 15424, 13657, 64, 9, 8780, 2, 1775, 1, "Teak Telescope", 898 },
			{ 15424, 13658, 84, 11, 8782, 2, 1775, 1, "Mahogany Telescope", 898 },
			// ** STUDY ROOM ** //

			// ** COSTUME ROOM ** //
			{ 18811, 18794, 87, 17, 8782, 4, 8784, 1, "Wardrobe", 898 },

			{ 18811, 18784, 42, 17, 8778, 4, -1, -1, "Oak wardrobe", 898 },
			{ 18811, 18786, 51, 17, 8778, 6, -1, -1, "Oak carved wardrobe", 898 },
			{ 18811, 18788, 60, 17, 8780, 4, -1, -1, "Teak wardrobe", 898 },
			{ 18811, 18790, 69, 17, 8780, 6, -1, -1, "Teak carved wardrobe", 898 },
			{ 18811, 18792, 78, 17, 8782, 4, -1, -1, "Mahogany wardrobe", 898 },
			{ 18811, 18796, 96, 17, 8786, 1, -1, -1, "Mahogany carved wardrobe", 898 },

			{ 18812, 18798, 50, 13, 8778, 2, -1, -1, "Oak toy box", 898 },
			{ 18812, 18800, 68, 15, 8780, 2, -1, -1, "Teak toy box", 898 },
			{ 18812, 18802, 86, 17, 8782, 2, -1, -1, "Mahogany toy box", 898 },

			{ 18814, 18772, 44, 9, 8778, 2, -1, -1, "Oak costume box", 898 },
			{ 18814, 18774, 62, 11, 8780, 2, -1, -1, "Teak costume box", 898 },
			{ 18814, 18776, 80, 13, 8782, 2, -1, -1, "Mahogany costume box", 898 },

			{ 18813, 18804, 48, 5, 8778, 2, -1, -1, "Oak treasure box", 898 },
			{ 18813, 18806, 66, 7, 8780, 2, -1, -1, "Teak treasure box", 898 },
			{ 18813, 18808, 84, 13, 8782, 2, -1, -1, "Mahogany treasure box", 898 },

			{ 18815, 18778, 46, 13, 8778, 3, -1, -1, "Oak armour case", 898 },
			{ 18815, 18780, 64, 15, 8780, 3, -1, -1, "Teak armour case", 898 },
			{ 18815, 18782, 82, 17, 8782, 3, -1, -1, "Mahogany armour case", 898 },

			{ 18810, 18769, 81, 20, 8782, 4, 8784, 1, "Gilded teak cape rack", 898 },
			{ 18810, 18766, 54, 11, 8778, 4, -1, -1, "Oak cape rack", 898 },
			{ 18810, 18767, 63, 14, 8780, 4, -1, -1, "Oak carved cape rack", 898 },
			{ 18810, 18768, 72, 18, 8782, 4, -1, -1, "Mahogany cape rack", 898 },
			{ 18810, 18770, 90, 22, 8780, 3, 8786, 1, "Marble teak cape rack", 898 },
			{ 18810, 18771, 99, 45, 8782, 3, 8788, 1, "Magic stone cape rack", 898 },

			// ** COSTUME ROOM ** //

			// ** CHAPEL ROOM ** //
			{ 15270, 13195, 70, 25, 8786, 2, 8790, 2, "Altar", 898 },
			{ 15270, 13199, 75, 29, 8786, 2, 8784, 4, "Altar", 898 },
			// ** CHAPEL ROOM ** //

			// ** PORTAL ROOM ** //

			{ 15406, 13615, 50, 5, 8007, 1, -1, -1, "Varrock Teleport", 898 },
			{ 15406, 13616, 50, 5, 8008, 1, -1, -1, "Lumbridge Teleport", 898 },
			{ 15406, 13617, 50, 5, 8009, 1, -1, -1, "Falador Teleport", 898 },
			{ 15406, 13618, 50, 5, 8010, 1, -1, -1, "Camelot Teleport", 898 },
			{ 15406, 13619, 50, 5, 8011, 1, -1, -1, "Ardougne Teleport", 898 },

			{ 15407, 13615, 50, 5, 8007, 1, -1, -1, "Varrock Teleport", 898 },
			{ 15407, 13616, 50, 5, 8008, 1, -1, -1, "Lumbridge Teleport", 898 },
			{ 15407, 13617, 50, 5, 8009, 1, -1, -1, "Falador Teleport", 898 },
			{ 15407, 13618, 50, 5, 8010, 1, -1, -1, "Camelot Teleport", 898 },
			{ 15407, 13619, 50, 5, 8011, 1, -1, -1, "Ardougne Teleport", 898 },

			{ 15408, 13615, 50, 5, 8007, 1, -1, -1, "Varrock Teleport", 898 },
			{ 15408, 13616, 50, 5, 8008, 1, -1, -1, "Lumbridge Teleport", 898 },
			{ 15408, 13617, 50, 5, 8009, 1, -1, -1, "Falador Teleport", 898 },
			{ 15408, 13618, 50, 5, 8010, 1, -1, -1, "Camelot Teleport", 898 },
			{ 15408, 13619, 50, 5, 8011, 1, -1, -1, "Ardougne Teleport", 898 },

			{ 15409, 13640, 50, 5, 3420, 2, -1, -1, "Teleport Focus", 898 },
			{ 15409, 13641, 65, 5, 8786, 1, -1, -1, "Greater Teleport Focus", 898 },
			{ 15409, 13639, 80, 5, 8788, 1, -1, -1, "Greater Teleport Focus", 898 },
			// ** PORTAL ROOM ** //

			// ** STAIR DUNGEON ** //
			{ 15380, 13497, 40, 9, 8778, 1, -1, -1, "Teak staircase", 898 },
	// ** STAIR DUNGEON ** //
	};


	public static void handleObjectClick(Client c, int objectType, int obX, int obY) {
		House house = c.getHouse();

		if (house == null || !house.getOwner().equals(c) || !house.isBuildMode()) {
			c.sendMessage("You must be in build mode to do this.");
			return;
		}
		if (!c.getItems().playerHasItem(2347) || !c.getItems().playerHasItem(8794)) {
			c.sendMessage("You need a hammer and saw to make this.");
			return;
		}

		for (int i = 0; i < CON_DATA.length; i++) {
			if (objectType == (int) CON_DATA[i][0]) {
				if (c.playerLevel[Skills.CONSTRUCTION] >= (int) CON_DATA[i][2]) {
					if (c.getItems().playerHasItem((int) CON_DATA[i][4], (int) CON_DATA[i][5])) {
						if ((int) CON_DATA[i][6] != -1) {
							if (c.getItems().playerHasItem((int) CON_DATA[i][6], (int) CON_DATA[i][7])) {
								if (c.getItems().isStackable((int) CON_DATA[i][6])) {
									c.getItems().deleteItem((int) CON_DATA[i][6], c.getItems().getItemSlot((int) CON_DATA[i][6]), (int) CON_DATA[i][7]);
								} else {
									c.getItems().deleteItem((int) CON_DATA[i][6], (int) CON_DATA[i][7]);
								}
							} else {
								c.sendMessage("You need at least <col=DD5C3E>" + CON_DATA[i][7] + " " + c.getItems().getItemName((int) CON_DATA[i][6]) + "s <col=000000>to build a <col=0000FF>" + CON_DATA[i][8] + ".");
								return;
							}
						}
						
						Room room = Construction.getCurrentRoom(c);
						ObjectDef def = ObjectDef.getObjectDef((int) CON_DATA[i][1]);
						String objectName = def.name;
						int[] newCoords = Test1.getOriginalObjectCoords(room.getRotation(), new int[] { obX % 8, obY % 8 });
						RoomObject roomObject = room.getObjectByPosition(newCoords[0], newCoords[1]);

						if (roomObject == null) {
							return;
						}
						
						RoomObject replacementObject = new RoomObject((int) CON_DATA[i][1], roomObject.getX(), roomObject.getY(), roomObject.getRotation(), objectName);

						/*if (room.getCustomObjectByPosition(newCoords[0], newCoords[1]) != null) {
							c.sendMessage("You remove the <col=0000FF>" + CON_DATA[i][8] + ".");
							replacementObject.setId((int) CON_DATA[i][0]);
							room.removeCustomObject(c, replacementObject);
							return;
						}*/
						
						c.getItems().deleteItem((int) CON_DATA[i][4], (int) CON_DATA[i][5]);
						c.getPA().addSkillXP((int) CON_DATA[i][3] * Config.CONSTRUCTION_EXPERIENCE, Skills.CONSTRUCTION);
						if ((int) CON_DATA[i][9] != 0) {
							c.animation((int) CON_DATA[i][9]);
						}
						c.sendMessage("You build a <col=0000FF>" + CON_DATA[i][8] + ".");
						room.newObject(c, replacementObject);
						return;
					} else {
						c.sendMessage("You need at least <col=DD5C3E>" + CON_DATA[i][5] + " " + c.getItems().getItemName((int) CON_DATA[i][4]) + "s <col=000000>to build a <col=0000FF>" + CON_DATA[i][8] + ".");
					}
				} else {
					c.sendMessage("You need a construction level of <col=DD5C3E>" + CON_DATA[i][2] + " <col=000000>to build a <col=0000FF>" + CON_DATA[i][8] + ".");
				}
			}
		}

		for (int i = 0; i < CON_DATA.length; i++) {
			if (objectType == (int) CON_DATA[i][1]) {
				Room room = Construction.getCurrentRoom(c);

				int[] newCoords = Test1.getOriginalObjectCoords(room.getRotation(), new int[] { obX % 8, obY % 8 });

				System.out.println(new Gson().toJson(newCoords));
				
				RoomObject roomObject = room.getObjectByPosition(newCoords[0], newCoords[1]);

				if (roomObject == null) {
					return;
				}

				room.removeCustomObject(c, new RoomObject((int) CON_DATA[i][0], roomObject.getX(), roomObject.getY(), roomObject.getRotation()));
				c.sendMessage("You remove the <col=0000FF>" + CON_DATA[i][8] + ".");
			}
		}
	}

	public static boolean isObject(int objectType) {
		for (int i = 0; i < CON_DATA.length; i++) {
			if (objectType == (int) CON_DATA[i][0] || objectType == (int) CON_DATA[i][1])
				return true;
		}
		return false;
	}

}
