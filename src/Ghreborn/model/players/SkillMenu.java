package Ghreborn.model.players;

import Ghreborn.model.content.fillables.Fillables.fillData;

/**
 * 
 * @author Sanity
 * 
 */

public class SkillMenu {

	private static final int INTERFACE_ID = 36100;
	public enum skillguideData {	
		BRONZE("1", 36151, 36152, 36153, "Bronze", ""),
		IRON("1", 36155, 36156, 36157, "Iron", ""),
		STEEL("5", 36159, 36160, 36161, "Steel", ""),
		BLACK("10", 36163, 36164, 36165, "Black", ""),
		WHITE("10", 36167, 36168, 36169, "White", ""),
		MITHRIL("20", 36171, 36172, 36173, "Mithril", ""),
		ADAMANT("30", 36175, 36176, 36177, "Adamant", ""),
		BATTLESTAVES("30", 36179, 36180, 36181, "Battlestaves (with 30 Magic)", ""),
		RUNE("40", 36183, 36184, 36185, "Rune", ""),
		BRINE("40", 36187, 36188, 36189, "Brine sabre", ""),
		Ivandis_Flail("40", 36191, 36192, 36193, "Ivandis Flail", ""),
		Mystic_staves("40", 36195, 36196, 36197, "Mystic staves (with 40 Magic)", ""),
		VOID("42", 36199, 36200, 36201, "Void Knight equipment", " (with 42 combat stats and 22 Prayer)"),
		Granite_Maul("50", 36203, 36204, 36205, "Granite Maul", " (with 50 Strenght)"),
		Granite_longsword("50", 36207, 36208, 36209, "Granite longsword", " (with 50 Strenght)"),
		DRAGON("60", 36211, 36212, 36213, "Dragon", ""),
		Barrelchest_Anchor("60", 36215, 36216, 36217, "Barrelchest Anchor", " (with 40 Strenght)"),
		Obsidian("60", 36219, 36220, 36221, "Obsidian weapons", ""),
		VIGGORA("60", 36223, 36224, 36225, "Viggora's Chainmace", ""),
		RDAGEWEAPONS("65", 36227, 36228, 36229, "3rd age weapons", ""),
		CRYSTAL_WEAPOONS("70", 36231, 36232, 36233, "Crystal weaponry", " (with 50 Agility)"),
		SARADOMIN_SWORD("70", 36235, 36236, 36237, "Saradomin sword", ""),
		Zamorak_spear("70", 36239, 36240, 36241, "Zamorak Spear", ""),
		ABYSSAL("70", 36243, 36244, 36245, "Abyssal whip & dagger", ""),
		ABYSSAL_BLUDGEON("70", 36247, 36248, 36249, "Abbyssal bludgeon", " (with 70 Strenght)"),
		AHRIM("70", 36251, 36252, 36253, "Ahrim's staff", " (with 70 magic)"),
		DHAROK("70", 36255, 36256, 36257, "Dharok's greataxe"," (with 70 Strenght)"),
		TORAG("70", 36259, 36260, 36261, "Torag's hammer"," (with 70 Strength)"),
		VERAC("70", 36263, 36264, 36265, "Verac's flail", ""),
		GUTHAN("70", 36267, 36268, 36269, "Guthan's warspear", ""),
		AVERNIC_DEFENDER("70", 36271, 36272, 36273, "Avernic defender", " (with 70 Defence)"),
		ARCLIGHT("75", 36275, 36276, 36277, "Arclight", ""),
		GODSWORDS("75", 36279, 36280, 36281, "Godswords", "");
		
		private String LevelId; 
		private int frameid, frameid2, levelframe;
		private String Textframeid, Textframeid2;
		private skillguideData(String levelId, int levelframe, int frameid, int frameid2, String Textframeid, String Textframeid2){
			this.LevelId = levelId;
			this.levelframe = levelframe;
			this.frameid = frameid;
			this.frameid2 = frameid2;
			this.Textframeid = Textframeid;
			this.Textframeid2 = Textframeid2;
			
		}
		public String getLevelId(){
			return LevelId;
		}
		public int getLevelframe(){
			return levelframe;
		}
		public int Getframe(){
			return frameid;
		}
		public int Getframe2(){
			return frameid2;
		}
		public String getFrameId(){
			return Textframeid;
		}
		public String getFrameId2(){
			return Textframeid2;
		}

	}
	private static final int[][] items = {
			{ 1205, 1203, 1207, 1217, 6591, 1209, 1211, 1391, 1213, 11037, 22398,
				1405, 8841, 4153, 21646, 1215, 10887, 6523, 22545, 12426, 13091,
				11838, 11824, 4151, 13263, 4710, 4718, 4747, 4755, 4726, 22322,
				19675, 11808, 11791, 22296, 12006, 22324, 12809, 22325, 21003, 21015},
			{ 1117, 1115, 1119, 1125, 1121, 6916, 1123, 1127, 3751, 2513,
					10348, 11724, 11720, 4720, 11283, 9753 },
			{ 4153, 6528, 9750 },
			{ 9768 },
			{ 841, 843, 849, 853, 857, 1135, 861, 2499, 4827, 6522, 2501,
					9185, 10330, 4214, 2503, 4734, 9756 }, { 9759 },
			{ 4099, 6916, 6889, 7401, 3387, 4675, 10338, 4712 } };
	private static final String[][] LEVELS = {
			{ "1", "1", "5", "10", "10", "20", "30", "30", "40", "40", "40",
					"40", "42", "50", "50", "60", "60", "60", "60", "65", 
					"70", "70", "70", "70", "70", "70", "70", "70", "70",
					"70", "70", "75", "75", "75", "75", "75", "75", "75", 
					"75", "75" },
			{ "1", "1", "5", "10", "20", "25", "30", "40", "45", "60", "65",
					"65", "70", "70", "75", "99" },
			{ "50", "60", "99" },
			{ "99" },
			{ "1", "5", "20", "30", "30", "40", "50", "50", "60", "60", "60",
					"61", "65", "70", "70", "70", "99" }, { "99" },
			{ "20", "25", "25", "40", "40", "50", "65", "70" } };

	private static final String[][] DESCRIPTION = {
			{ "Bronze", "Iron", "Steel",
					"Black", "White", "Mithril", "Adamant", "Battlestaves (with 30 Magic)",
					"Rune", "Brine sabre", "Ivandis Flail", "Mystic staves (with 40 Magic)", 
					"Void Knight equipment", "Granite Maul (with 50 Strenght)", "Granite longsword (with 50 Strenght)",
					"Dragon", "Barrelchest Anchor (with 40 Strenght)", "Obsidian weapons", "Viggora's Chainmace",
					"3rd age weapons", "Crystal weaponry (with 50 Agility)", "Saradomin sword", "Zamorak spear",
					"Abyssal whip & dagger", "Abyssal bludgeon (with 70 Strenght)","Ahrim's staff (with 70 Magic)", "Dharok's greataxe (with 70 Strenght)", 
					 "Torag's hammers (with 70 Strenght)", "Verac's flail", "Guthan's warspear", 
					 "Averic defender","Arclight", "Godswords", "Staff of the Dead (with 75 Magic)",
					 "Staff of Light (with 75 magic)", "Abyssal tentacle", "Gharazi rapier", 
					 "Blessed Saradomin sword", "Scythe of Vitur (with 75 Strenght)", 
					 "Elder maul (with 75 Strenght)", "Dinh's bulwark" },
			{ "Bronze Armour", "Iron Armour", "Steel Armour", "Black Armour",
					"Mithril Armour", "Infinity", "Adamant Armour",
					"Rune Armour", "Fremennik Helmets", "Dragon Armour",
					"3rd Age Armour", "Bandos", "Armadyl", "Barrows Armour",
					"Dragonfire Shield", "Cape of Achievement" },
			{ "Granite Items", "Obby Maul", "Cape of Achievement" },
			{ "Cape of Achievement" },
			{ "Normal Bows", "Oak Bows", "Willow Bows", "Maple Bows",
					"Yew Bows", "Green D'hide", "Magic Bows", "Blue D'hide",
					"Dark Bow", "Obby Ring", "Red D'hide", "Rune C'bow",
					"3rd age Range", "Crystal Bow", "Black D'hide", "Karil's",
					"Cape of Achievement" },
			{ "Cape of Achievement" },
			{ "Mystic ", "Infinity ", "Mage's book", "Enchanted ",
					"Splitbark ", "Ancient staff", "3rd age mage", "Ahrims" } };

	/**
	 * cooking
	 * 
	 * 317,335,331,359,377,371,383,389,395,9801 1,15,25,30,40,45,80,91 "Shrimp",
	 * "Trout", "Salmon", "Tuna", "Lobster", "Swordfish", "Shark",
	 * "Manta Ray","Cape of Achievement"
	 */

	/**
	 * fishing
	 * 
	 * 317,335,331,359,377,371,383,389,395,9801 1,20,30,35,40,50,76,81 "Shrimp",
	 * "Trout", "Salmon", "Tuna", "Lobster", "Swordfish", "Shark",
	 * "Manta Ray","Cape of Achievement"
	 */

	/**
	 * woodcutting
	 * 
	 * 1351,1349,1511,1353,1521,1355,1519,1357,1517,1515,1359,1513,7797
	 * "1","1","1","6","15","21","30","31","41","45","60","61","75","99"
	 * "Bronze Axe"
	 * ,"Iron Axe","Logs","Steel Axe","Oak Logs","Mithril Axe","Willow Logs"
	 * ,"Adamant Axe"
	 * ,"Rune Axe","Maple Logs","Yew Logs","Dragon Axe","Magic Logs"
	 * ,"Cape of Achievement"
	 */

	/**
	 * mining
	 * 
	 * 1265,1267,1436,434,436,1269,440,442,1273,453,1271,444,1275,447,449,451,
	 * 9792 "1","1","1","1","1","1","6","15","20","21","30","31","40","41","65",
	 * "70","85","99"
	 * "Bronze pickaxe","Iron pickaxe","Rune essence","Clay","Copper"
	 * ,"Tin","steel pickaxe"
	 * ,"Iron ore","Silver ore","Mithril pickaxe","Coal ore"
	 * ,"Adamant pickaxe","Gold ore"
	 * ,"Rune pickaxe","Mithril ore","Adamanetite ore","Runite ore"
	 */

	/**
	 * 
	 * 556,558,555,557,554,559,564,562,9075,561,563,560,565
	 * "1","2","5","9","14","20","27","35","40","44","54","65","77"
	 * "Air rune","Mind rune"
	 * ,"Water rune","Earth rune","Fire rune","Body rune","Cosmic rune"
	 * ,"Chaos rune"
	 * ,"Astral rune","Nature rune","Law rune","Death rune","Blood rune"
	 */
	private static final String[] SKILLS = { "Attack", "Defence", "Strength",
			"Hitpoints", "Ranged", "Prayer", "Magic" };

	public static void openInterface(Client c, int skillType) {
		//removeSidebars(c);
		writeItems(c, skillType);
		writeText(c, skillType);
		c.getPA().showInterface(INTERFACE_ID);
	}

	private static void removeSidebars(Client c) {
		int[] temp = { 8849, 8846, 8823, 8824, 8827, 8837, 8840, 8843, 8859,
				8862, 8865, 15303, 15306, 15309 };
		for (int j = 0; j < temp.length; j++) {
			c.getPA().sendFrame126("", temp[j]);
		}
	}

	private static void writeItems(Client c, int skillType) {
		synchronized (c) {
			c.outStream.createFrameVarSizeWord(53);
			c.outStream.writeWord(36250);
			c.outStream.writeWord(items[skillType].length);
			for (int j = 0; j < items[skillType].length; j++) {
				c.outStream.writeByte(1);
				if (items[skillType][j] > 0) {
					c.outStream.writeWordBigEndianA(items[skillType][j] + 1);
				} else {
					c.outStream.writeWordBigEndianA(0);
				}
			}
			c.outStream.endFrameVarSizeWord();
			c.flushOutStream();
		}
	}

	private static void writeText(Client c, int skillType) {
		for(final skillguideData g : skillguideData.values()) {
			c.getPA().sendFrame126("Attack", 36102);
			c.getPA().sendFrame126("Weapons", 36103);
			c.getPA().sendFrame126(g.getLevelId(), g.getLevelframe());
			c.getPA().sendFrame126(g.getFrameId(), g.Getframe());
			c.getPA().sendFrame126(g.getFrameId2(), g.Getframe2());
	}
	}
}