package Ghreborn.model.players;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import Ghreborn.Config;
import Ghreborn.core.PlayerHandler;


public class Logs {

	private Player c;

	public Logs(Player Client) {
		this.c = Client;
	}

	public void received(String itemName, int item, int itemAmount, Player recipient) {
		for (int loot : TRACE_ITEMS) {
			if (loot == item) {
				Calendar date = Calendar.getInstance();
				try {
					BufferedWriter write = new BufferedWriter(
							new FileWriter("./Data/trades/received/" + c.playerName + ".txt", true));
					try {
						write.newLine();
						write.write("Year : " + date.get(Calendar.YEAR) + "\tMonth : " + date.get(Calendar.MONTH)
								+ "\tDay : " + date.get(Calendar.DAY_OF_MONTH));
						write.newLine();
						write.write("Hour: " + date.get(Calendar.HOUR_OF_DAY) + " Minute: " + date.get(Calendar.MINUTE)
								+ " ");
						write.newLine();
						write.write("" + recipient.playerName + " Received " + itemName + " x" + itemAmount + " from "
								+ c.playerName + " ");
						write.newLine();
						write.write("-----------------------------------");
					} finally {
						write.close();
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void playerKills() {
		Player o = PlayerHandler.players[c.killerId];
		Calendar C = Calendar.getInstance();
		try {
			BufferedWriter bItem = new BufferedWriter(
					new FileWriter("./Data/kills/Players Killed/" + o.playerName + ".txt", true));
			try {
				bItem.newLine();
				bItem.write("Year : " + C.get(Calendar.YEAR) + "\tMonth : " + C.get(Calendar.MONTH) + "\tDay : "
						+ C.get(Calendar.DAY_OF_MONTH));
				bItem.newLine();
				bItem.write("" + o.playerName + " killed " + c.playerName + "in pvp.");
				bItem.newLine();
				bItem.write("--------------------------------------------------");
			} finally {
				bItem.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int[] TRACE_ITEMS = {11802, 11803/*AGS*/, /*scrolls*/2690, 2691, 2692,2996/*PKP*/,4151, 4152 /*whip*/, /*arma cbow*/11875, 11876, 12914, 12915, 12916, 12917, 12918, 12919, 12920/*ANTI_VEN*/, 10551 /*Torso*/,
			   10548/*Fighter_Hat*/, 7462/*B_GLOVES*/, 1893, 1894, 6814, 6815, 1807, 1806, 1613, 1614, 11230, 11231, 11232, 11233, 11234, 3140, 3141, 12414, 12415, 12417, 12418, 6570, 12954, 4151, 13637, 13638, 13636, 13635, 13634, 13633, 13631, 13629, 13627,
			   1993, 1994, 4087, 4088, 4181, 4180, 4585, 4586, 12415, 12416, 4566, 2572, 2573, 11980, 11981, 11982, 11983, 11984, 11985, 11986, 11987, 11988, 11989,
			   12397, 12398, 9005, 9006,11934, 11935, 377, 378, 2513, 2514, 3140, 3141, 12141, 12534, 12535, 1755, 1756, 5601, 1592, 1593, 1595, 1596, 1597, 1598,
			   7509/*ROCK_CAKE*/, 6737, 6738/*B_RING*/, 6735, 6736/*WARRIOR_RING*/, 1419,
			   11804/*BGS*/, 11806, 11807/*SGS*/, 11808, 11809/*ZGS*/, 11838, 11839/*SS*/,
			    11833/*BCP*/,  11835 /*TASSETS*/, 11283, 11284, 11285, 11287 /*DFS*/,
			   11235, 11236/*DARK_BOW*/, 11786,/*ACB*/10735, 10734,
			   2577, 2578/*RANGER_BOOTS*/, 2581, 2582/*ROBIN_HAT*/, 6733, 6734/*ARCHER_RING*/,
			   6914, 6915/*Master_Wand*/, 6889, 6890/*Mages'_Book*/, 6731, 6732 /*SEERS_RING*/,
			   6916, 6917, 6918, 6919, 6920, 6921, 6922, 6923, 6924, 6925/*INFINITY*/, 
			   12855/*Hunters_Honour*/, 12856/*Rogues_Revenge*/, 12804, 12805/*Sara_Tear*/, 12851/*Amulet_Damned*/, 
			   12849, 12527/*Fury(o)*/, 11837, 12850, 12852, 12853, 12820,
			   12608/*Grante_Clamp*/, 12610/*Book_War*/, 12612/*Book_Law*/, 12757/*Book_Darkness*/, 
			   12526/*Blue_Dbow*/, 12527, 12528, 12529, 12530, 12531,
			   12759, 12760/*Green_Dbow*/, 12761, 12762/*Yellow_Dbow*/, 12763, 12764/*White_Dbow*/, 12457/*Dark_Infinity_Hat*/, 
			   12458/*Dark_Inifinity_Robe*/, 12459, 12419, 12420, 1242, 12421, 12846/*(MORE INFINITY)*/,
			   12817, 12818, 12821, 12822, 12825, 12826, 12830/*Spiri_Shield*/,
			   12831, 12832/*Blessed_Spirit*/, 11773/*B_RING_(i)*/, 11771/*ARCHER_RING_(i)*/, 11770/*SEER_RING_(i)*/, 11772,
			   11811/*Armadyl_Hilt*/, 11812, 11813/*Bandos_Hilt*/, 11814, 11815/*Saradomin_Hilt*/, 11816, 11817/*Zamorak_Hilt*/,
			   11824, 11825/*ZAMORAK_SPEAR*/, 11826, 11827/*ARMA_HELM*/,
			   11829/*ARMDAYL_CHEST*/,  11831/*ARMA_SKIRT*/, 11836, 11837/*BANDOS_BOOTS*/, 11847/*BLACK_HWEEN*/,
			   11850, 11851/*GRACE_HOOD*/, 11852, 11853/*GRACE_CAPE*/, 11854, 11855/*GRACE_TOP*/,
			   11856, 11857/*GRACE_LEG*/, 11858, 11859/*GRACE_GLOVE*/, 11860, 11861/*GRACE_BOOT*/,
			   11861/*BLACK_PHAT*/, 11863/*RAINBOW_PHAT*/, 11864, 11865, 8901, 2839/*SLAYER_HELM*/, 11889, 11890/*Zamorak_Hasta*/,
			   11905, 11906, 11907, 11908, 11909/*TRIDENT_OF_SEAS*/, 11919, 12956, 12957, 12958, 12959,/*COW_SET*/ 11920, 11921 /*D_PICK*/, 11924, 11925/*MALEDICTION_WARD*/,
			   11926/*ODIUM WARD*/, 11928, 11929, 11930 /*ODIUM_SHARDS*/, 11931, 11932, 11933/*MALEDICTION SHARDS*/,
			   11941, 11942/*LOOTING_BAG*/, 11990, 11991/*FEDORA*/, 12002, 12003/*OCCULT_NECK*/, 12004, 12005, 12006/*KRAKEN_TENT*/, 12954/*D_DEFENDER*/,
			   12960, 12962, 12964, 12968, 12970, 11792, 11794, 11795, 11796, 11797, 11798, 11799, 11800, 11801,
			   11805, 12809, 12810, 12811, 12812, 12813, 12814, 12815, 12436, 13208, 13207, 11927, 13206, 13576, 2678,
			   4067, 13182, 1037, 4084, 4083, 6199, 12905, 12906, 12907, 12908, 12909, 12910, 12911, 12912,
			   12913, 12914, 12915, 12916, 12917, 12918, 12919, 12920,
			   1039, 1041, 1043, 
			   1045, 1047, 1049,
			   2422, 4561, 10476, 11730, 11731, 11732, 11733, 11734,
			   12806, 12807, 12808, 13196, 13197, 13198, 13199, 13080, 13081,
			   
			   1050, 6737, 6733, 6731, 6735, 12954, 6570, 7462,
			   13140, 13107, 13115, 13120, 13124, 13131, 13124, 
			   13103, 13136, 13128, 13111, 13144, 12813, 12814, 
			   12815, 12810, 12811, 12812, 12887, 12888, 12889, 
			   12890, 12891, 12892, 12893, 12894, 12895, 12896,
			   11919, 12956, 12957, 12958, 12959, 12637, 12638,
			   6665, 6666, 2990, 2991, 2992, 2993, 2994, 2995, 12603,12819,12827,12823,11286,12605,11832,11834,11830,11828,11791,11785, Config.TOKENS_ID, 11810,
			   12845, 9920, 12359, 2651, 8950, 8928, 12412, 
			   12432, 12434, 2639, 2641, 2643, 12321, 12323, 
			   12325, 11280, 10394, 340, 12335, 12514, 12537,
			   
			   8839, 8840, 8842, 11663, 11664, 11665, 11862,
			
			   9747, 9748, 9749, 9750, 9751, 9752, 9753, 9754,
			   9755, 9756, 9757, 9758, 9759, 9760, 9761, 9762,
			   9763, 9764, 9765, 9766, 9767, 9768, 9769, 9770,
			   9771, 9772, 9773, 9774, 9775, 9776, 9778, 9779,
			   9780, 9781, 9782, 9783, 9784, 9785, 9786, 9787,
			   9788, 9789, 9790, 9791, 9792, 9793, 9794, 9795,
			   9796, 9797, 9798, 9799, 9800, 9801, 9802, 9803,
			   9804, 9805, 9806, 9807, 9808, 9809, 9810, 9811,
			   9812, 9813, 9950, 9814, 10639, 10640, 10641, 10642, 10643,
			   10644, 10645, 10646, 10647, 10648, 10649, 10650,
			   10651, 10652, 10653, 10654, 10655, 10656, 10657,
			   10658, 10659, 10660, 10661, 10662, 13069, 13068,
			   
			   /*PETS*/
			   12816, 12650, 12649, 12651, 12652,
			   12644, 12645, 12643, 11995, 12653,
			   13178, 12655, 12646, 12921, 12939, 
			   12940, 12654, 13179, 13177, 13180,
			   13181, 12647, 12548,
			   
			   /*PARTYHAT_SET*/
			   13174, 13173, 13175, 13176, 12399, 12400,
			   
			   /*SIRE*/
			   13207, 13205, 
			   
			   /*DONATION_SCROLLS*/
			   2701, 2700, 2699, 2698, 2697,
			   2679, 2677, 2677,
			   
			   12637, 12638, 12639, 12596, 12597,

			   12601, 12602, 12604, 12606, 
			   
			   /*BOOTS*/
			   13211, 13213, 13215,
			   
			   /*3rd_Age*/
			   10330, 10331, 10332, 10333, 10334, 10335, 10336, 10337, 10338,
			   10339, 10340, 10341, 10342, 10343, 10344, 10345, 10346, 10347,
			   10348, 10349, 10350, 10351, 10352, 10353, 12422, 12423,
			   12424, 12425, 12426, 12427, 12437, 12438, 12425, 12426, 12424,
			   
			   6570, 10566, 10637, 12833, 12791,
			   
			   12877, 12878/*DH_SET*/, 12873, 12874/*GUTH_SET*/,
			   12875, 12876/*VERAC_SET*/, 12879, 12880/*TORAG_SET*/,
			   12881, 12882/*AHRIM_SET*/, 12883, 12884/*KARIL_SET*/,
			   
			   /*HOLIDAY_ITEMS*/
			   12887, 12888, 12889, 12890,
			   12891/*SANTA*/,
			   
			   12892, 12893, 12894, 12895,
			   12896, 12897, 12898/*ANTI_SANTA*/,
			   
			   2996, 995, 12695, 12696, 12701, 12702, 12697, 12698, 12699, 12700,

			   /*ZULRAH*/
			   12921, 12922, 12923, 12924, 12925, 12926, 12927,
			   12929, 12928, 12931, 12932, 12933, 12904,
			   12939, 12930, 12940, 12902, 12899, 12900, 12901, 12903,
			   
			   /*PARTYHATS*/
			   1038, 1040, 1042, 1044, 1046,
			   1048, 1050, 1053, 1055, 1057,
			   
			   /*MISC*/ 12798, 12799, 12800, 12801, 12802, 12786, 12783, 12013, 12014, 12015,
			   			12016,
			   			12775, 12776, 12777, 1277, 12779, 12780, 12781, 12782,		
};
}
