package Ghreborn.model.minigames.treasuretrails;

import java.util.ArrayList;


import Ghreborn.model.items.Item;
import Ghreborn.model.items.ItemDefinition;
import Ghreborn.model.players.Client;
import Ghreborn.util.Misc;


public class ClueScroll {

	/*
	 * level 3 scroll : I expect you to die! find clue : You've found another
	 * clue!- You've been given a casket! - You found a casket! 6960 clue reward
	 */

	/* the main clue scroll hint interface */

	public static final int CLUE_SCROLL_INTERFACE = 6965;

	public static final int CASKET_LV1 = 2724;
	public static final int CASKET_LV2 = 2726;
	public static final int CASKET_LV3 = 2728;

	public static final int REWARD_CASKET_LV1 = 20546;
	public static final int REWARD_CASKET_LV2 = 20545;
	public static final int REWARD_CASKET_LV3 = 20544;
	public static final int REWARD_CASKET_LV4 = 20543;
	public static final int REWARD_CASKET_LV5 = 19836;

	public static final int CLUE_ITEM = 2701;

	/* the puzzle class constants */

	public static final int PUZZLE_INTERFACE = 6976;

	public static final int PUZZLE_INTERFACE_CONTAINER = 6980;

	public static final int PUZZLE_INTERFACE_DEFAULT_CONTAINER = 6985;

	public static final int CASTLE_PUZZLE = 2800;
	public static final int TREE_PUZZLE = 3565;
	public static final int OGRE_PUZZLE = 3571;

	public static final int PUZZLE_LENGTH = 25;

	public static final int[] firstPuzzle = {2749, 2750, 2751, 2752, 2753, 2754, 2755, 2756, 2757, 2758, 2759, 2760, 2761, 2762, 2763, 2764, 2765, 2766, 2767, 2768, 2769, 2770, 2771, 2772, -1};

	public static final int[] secondPuzzle = {3619, 3620, 3621, 3622, 3623, 3624, 3625, 3626, 3627, 3628, 3629, 3630, 3631, 3632, 3633, 3634, 3635, 3636, 3637, 3638, 3639, 3640, 3641, 3642, -1};

	public static final int[] thirdPuzzle = {3643, 3644, 3645, 3646, 3647, 3648, 3649, 3650, 3651, 3652, 3653, 3654, 3655, 3656, 3657, 3658, 3659, 3660, 3661, 3662, 3663, 3664, 3665, 3666, -1};

	public static String[] levelOneClueNpc = {"Man", "Woman", "Goblin", "Mugger", "Barbarian", "Farmer", "Al-Kharid", "Thug", "Rock Crabs", "Rogue", "Thief", "H.A.M", "Banshees", "Cave Slime", "Afflicted", "Borrakar", "Freidar", "Freygerd", "Inga", "Jennella", "Lensa", "Lanzig"};

	public static String[] levelTwoClueNpc = {"Guard", "Tribesman", "Bandit Camp Humans", "Cockatrice", "Abyssal Leech", "Pyrefiend", "Harpie Bug Swarm", "Black Guard", "Rellekka Warriors", "Market Guard", "Jogre", "Ice Warrior", "Abyssal Guardian", "Paladin", "Vampire", "Dagannoth", "Giant Skeleton", "Abyssal Walker", "Dagannoth", "Wallasalki", "Mummy", "Giant Rock Crab"};

	public static String[] levelThreeClueNpc = {"Greater Demon", "Elf Warrior", "Tyras Guard", "Hellhound", "Dragon", "Dagannoth", "Turoth", "Jellie", "Aberrant Specter", "Gargoyle", "Nechryael", "Abyssal Demon"};

	// todo torn page make into mage books + firelighters + junk items to reward

	public static int[] mainJunk = {554, 555, 556, 557, 558, 559, 560, 561, 562, 563, 564, 565, 566, 374, 380, 362, 1379, 1381, 1383, 1385, 1387, 1065, 1099, 1135, 1097, 1169, 841, 843, 845, 847, 849};
	public static int[] junkItem1 = {1367, 1217, 1179, 1151, 1107, 1077, 1269, 1089, 1125, 1165, 1195, 1283, 1297, 1313, 1327, 1341, 1367, 1426, 334, 330, 851, 853, 855, 857, 859, 4821,12297};
	public static int[] junkItem2 = {1430, 1371, 1345, 1331, 1317, 1301, 1287, 1271, 1211, 1199, 1073, 1161, 1183, 1091, 1111, 1123, 1145, 1199, 1681, 4823};
	public static int[] junkItem3 = {1432, 1373, 1347, 1333, 1319, 1303, 1289, 1275, 1213, 1079, 1093, 1113, 1127, 1147, 1163, 1185, 1201, 4824, 386, 2491, 2497, 2503};

	public static int[] Easyrewards = {12221,12215,12217,12219,12223,12211,12205,12207,12209,12213,12231,12225,12227,12229,12233,12241,12235,12237,12239,12243,2587,2583,2585,3472,2589,2595,2591,2593,3473,2597,7396,7392,7388,7394,7390,7386,12455,12451,12447,12453,12449,12445,10408,10410,10428,10430,10412,10414,10432,10434,10404,10406,10424,10426,10462,10466,10458,10464,10460,10468,12193,12195,12265,12267,12253,12255,7364,7368,7362,7366,2635,2633,2637,12247,10306,10308,10310,10312,10314,10316,10320,10318,10324,10322,7332,7338,7344,7350,7356,2631,12245,10392,10398,12249,12251,10394,10396,10366,12375,20166,20169,20172,20175,20178,20181,20184,20187,20190,20193,20196,20199,20202,20205,20208,20211,20214,20217};

	public static int[] Mediumrewards = {7329, 7330, 7331, 7319, 7321, 7323, 7325, 7327, 7370, 7372, 7378, 7380, 2645, 2647, 2649, 2579, 2577, 2599, 2601, 2603, 2605, 2607, 2609, 2611, 2613, 7334, 7340, 7346, 7352, 7358, 3828, 3832, 3836, 3829, 3833, 3837, 3829, 3833, 3837, 3829, 3833, 3837};

	public static int[] Hardrewards = {3480, 3481, 3483, 3486, 3488, 2653, 2655, 2657, 2659, 2661, 2663, 2665, 2667, 2669, 2671, 2673, 2675, 2581, 2651, 7398, 7399, 7400, 7329, 7330, 7331, 7374, 7376, 7382, 7384, 2615, 2617, 2619, 2621, 2623, 2625, 2627, 2629, 7336, 7342, 7348, 7354, 7360, 3830, 3834, 3838, 3830, 3834, 3838, 3830, 3834, 3838, 2639, 2640, 2643, 10330, 10332, 10334, 10336, 10338, 10340, 10342, 10344, 10346, 10348, 10350, 10352};

	public static int[] Eliterewards = {12538,12534,12536,12532,20002,12530,12528,12526,12351,12441,12443,12373,12335,12337,12391,12389,3486, 3481,3483,3485,3488,12432,12353,12355,12540,12363,12365,12367,12369,12371,12596,19994,12437,10350,10348,10346,12426,10352,10342,10338,10340,10344,12422,10334,10330,10332,10336,12424,12430,12357,12393,12397,12395,12439,12351,19943,19946,19949,19952,19955,19988,19991,19997,20005,19958,19961,19964,19967,19970,19973,19976,19979,19982,19985};

	public static int[] Masterrewards = { 20143, 20008, 20011, 20014, 20017, 20020, 20023, 20026, 20029, 20032, 20035, 20038, 20041, 20044, 20047, 20050, 20053, 20056, 20059, 20062, 20065, 20068, 20071, 20074, 20077, 20080, 20083, 20086, 20089, 20092, 20095, 20098, 20101, 20104, 20107, 20110, 20113, 20116, 20119, 20122, 20125, 20128, 20131, 20134, 20137, 20140};

	public static void handleCasket(Client player, int itemId) {
		switch (itemId) {
			case REWARD_CASKET_LV1 :
				player.getItems().deleteItem(itemId, 1);
				addClueReward(player, 1);
				break;
			case REWARD_CASKET_LV2 :
				player.getItems().deleteItem(itemId, 1);
				addClueReward(player, 2);
				break;
			case REWARD_CASKET_LV3 :
				//player.getItems().deleteItem(itemId, 1);
				addClueReward(player, 3);
				break;
				
			case REWARD_CASKET_LV4 :
				//player.getItems().deleteItem(itemId, 1);
				addClueReward(player, 4);
				break;
				
			case REWARD_CASKET_LV5 :
				//player.getItems().deleteItem(itemId, 1);
				addClueReward(player, 5);
				break;
		}
			/*case CASKET_LV1 :
				player.getItems().removeItem(itemId, 1);
				clueReward(player, 1, "You've found another clue!", false, "Here is your reward!");
				break;
			case CASKET_LV2 :
				player.getItems().removeItem(itemId, 1);
				clueReward(player, 2, "You've found another clue!", false, "Here is your reward!");
				break;
			case CASKET_LV3 :
				player.getItems().removeItem(itemId, 1);
				clueReward(player, 3, "You've found another clue!", false, "Here is your reward!");
				break;*/
		}
	public static void addClueReward(Client player, int clueLevel) {
		ArrayList<Integer> array = new ArrayList<Integer>();
		int random = Misc.random(4) + 2;
		switch (clueLevel) {
			case 1 :
				for (int i = 0; i < random; i++) {
					int percent = Misc.random(100);
					if (percent <= 7) {
						array.add(Easyrewards[Misc.random(Easyrewards.length - 1)]);
					} else if (percent > 7 && percent <= 30 && !array.contains(995)) {
						array.add(995);
					} else {
						array.add(Misc.random(2) == 1 ? junkItem1[Misc.random(junkItem1.length - 1)] : mainJunk[Misc.random(mainJunk.length - 1)]);
					}
				}
				break;
			case 2 :
				for (int i = 0; i < random; i++) {
					int percent = Misc.random(100);
					if (percent <= 7) {
						array.add(Mediumrewards[Misc.random(Mediumrewards.length - 1)]);
					} else if (percent > 7 && percent <= 30 && !array.contains(995)) {
						array.add(995);
					} else {
						array.add(Misc.random(2) == 1 ? junkItem2[Misc.random(junkItem2.length - 1)] : mainJunk[Misc.random(mainJunk.length - 1)]);
					}

				}
				break;
			case 3 ://Hard rewards
				for (int i = 0; i < random; i++) {
					int percent = Misc.random(100);
					if (percent <= 7) {
						array.add(Hardrewards[Misc.random(Hardrewards.length - 1)]);
					} else if (percent > 7 && percent <= 30 && !array.contains(995)) {
						array.add(995);
					} else {
						array.add(Misc.random(2) == 1 ? junkItem3[Misc.random(junkItem3.length - 1)] : mainJunk[Misc.random(mainJunk.length - 1)]);
					}
				}
				break;
			case 4 ://Elite rewards
				for (int i = 0; i < random; i++) {
					int percent = Misc.random(100);
					if (percent <= 7) {
						array.add(Eliterewards[Misc.random(Eliterewards.length - 1)]);
					} else if (percent > 7 && percent <= 30 && !array.contains(995)) {
						array.add(995);
					} else {
						array.add(Misc.random(2) == 1 ? junkItem3[Misc.random(junkItem3.length - 1)] : mainJunk[Misc.random(mainJunk.length - 1)]);
					}
				}
				break;
			case 5 ://Master rewards
				for (int i = 0; i < random; i++) {
					int percent = Misc.random(100);
					if (percent <= 7) {
						array.add(Masterrewards[Misc.random(Masterrewards.length - 1)]);
					} else if (percent > 7 && percent <= 30 && !array.contains(995)) {
						array.add(995);
					} else {
						array.add(Misc.random(2) == 1 ? junkItem3[Misc.random(junkItem3.length - 1)] : mainJunk[Misc.random(mainJunk.length - 1)]);
					}
				}
				break;
		}

		int[] items = new int[random];
		int[] amounts = new int[random];
		Item[] item = new Item[random];
		for (int i = 0; i < random; i++) {
			items[i] = array.get(i);
			amounts[i] = ItemDefinition.forId(items[i]).isStackable() ? items[i] == 995 ? Misc.random(10000) : Misc.random(4) + 11 : 1;
			if (ItemDefinition.forId(items[i]).getName().toLowerCase().contains("page")) {
				amounts[i] = 1;
			}
			item[i] = new Item(items[i], amounts[i]);
			player.getItems().addItemToBank(items[i], amounts[i]);
		}
		displayReward(player, items, amounts);
		player.getPA().showInterface(6960);

		player.sendMessage("Well done, you've completed the Treasure Trail!");
	}
	public static void displayReward(Client c, int items[], int amount[]) {
		c.outStream.createFrameVarSizeWord(53);
		c.outStream.writeWord(6963);
		c.outStream.writeWord(items.length);
		for(int i = 0; i < items.length; i++) {
			if(c.playerItemsN[i] > 254) {
				c.outStream.writeByte(255);
				c.outStream.writeDWord_v2((amount[i]));
			} else {
				c.outStream.writeByte((amount[i]));
			}
			if(items[i] > 0) {
				c.outStream.writeWordBigEndianA(items[i] + 1);
			} else {
				c.outStream.writeWordBigEndianA(0);
			}
		}
		c.outStream.endFrameVarSizeWord();
		c.flushOutStream();
	}
}
