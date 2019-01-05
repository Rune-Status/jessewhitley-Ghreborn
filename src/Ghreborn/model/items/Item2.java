package Ghreborn.model.items;

//This file is free software; you can redistribute it and/or modify it under
//the terms of the GNU General Public License version 2, 1991 as published by
//the Free Software Foundation.

//This program is distributed in the hope that it will be useful, but WITHOUT
//ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
//FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
//details.

//A copy of the GNU General Public License can be found at:
//http://www.gnu.org/licenses/gpl.html

//a collection of item methods

import java.io.*;


public class Item2 {

 public static int flowers[] = {2980, 2981, 2982, 2983, 2984, 2985, 2986, 2987, 2988,};

 public static int randomflowers() {
     return flowers[(int) (Math.random() * flowers.length)];
 }

 public static int balloon[] = {115, 116, 117, 118, 119, 120, 121, 122,};

 public static int randomballoon() {
     return balloon[(int) (Math.random() * balloon.length)];
 }
 public static int Custom[] = {23212, 23213, 23214, 23215, 23216, 23217, 23218, 23219, 23220, 23221, 23222, 23223, 23224, 23225, 23226, 23227, 23228, 23229, 23230, 23231, 23232, 23233, 23234, 23235, 23236, 23237, 23238, 23239, 23240, 23241, 23242, 23243, 23244, 23245, 23246, 23247, 23248, 23249, 23250, 23251, 23252, 23253, 23254, 23255, 23256, 23257, 23258, 23259, 23260, 23261, 23262, 23263, 23264, 23265, 23266, 23267, 23268, 23269, 23270, 23271, 23272, 23273, 23274, 23275, 23276, 23277, 23278, 23279, 23280, 23281, 23282, 23283, 23284, 23285, 23286, 23287, 23288, 23289, 23290, 23291, 23292, 23293, 23294, 23295, 23296, 23297, 23298, 23299, 23300, 23301, 23302, 23303, 23304, 23305, 23306, 23307, 23308, 23309, 23310, 23311, 23312, 23313, 23314, 23315, 23316, 23317, 23318, 23319, 23320, 23321, 23322, 23323, 23324, 23325, 23326, 23327, 23328, 23329, 23330, 23331, 23332, 23333, 23334, 23335, 23336,};

public static int randomGH() {
return Custom[(int) (Math.random() * Custom.length)];
}
public static int CustomXmas[] = {23911, 23912, 23913, 23914, 23915, 23916, 23917, 23918, 23919, 23920, 23921, 23922, 23923, 23924, 23925, 23926, 23348, 23349, 23350, 23351, 23352, 23353, 23354, 23355, 23356, 23357, 23358, 23359, 23360, 23361};

public static int randomXmas() {
return CustomXmas[(int) (Math.random() * CustomXmas.length)];
}

	    public static int corp[] = {995,995,995,995,995,995,995,995,995,995,13734,995,995,995,995,995,995,13754,995,995,995,995,995,995,995,13746,995,995,995,995,995,995,995,995,995,995,995,995,995,995,13748,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,13748,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,13750,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,13752,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,};

 public static int randomcorp() {
     return corp[(int) (Math.random() * corp.length)];
 }

 public static int coalrock[] = {
         453, 453, 453, 453, 453, 453, 453, 453, 453, 453, 453, 453, 453, 453,
         453, 453, 453, 453, 453, 453, 453, 453, 453, 453, 453, 453, 453, 453,
         453, 453, 453, 453, 453, 453, 453, 453, 453, 453, 453, 453, 453, 453,
         453, 453, 453, 453, 453, 453, 453, 453, 453, 453, 453, 453, 453, 453,
         453, 453, 453, 453, 453, 1623, 453, 453, 453, 453, 453, 453, 453, 453,
         453, 453, 453, 453, 453, 453, 453, 453, 453, 453, 453, 453, 453, 453,
         453, 453, 453, 453, 453, 453, 453, 453, 453, 453, 1617, 453, 453, 453,
         453, 453, 453, 453, 453, 453, 453, 453, 453, 453, 453, 453, 453, 453,
         453, 453, 453, 453, 453, 453, 453, 453, 1621, 453, 453, 453, 453, 453,
         453, 453, 453, 453, 453, 453, 453, 453, 453, 453, 453, 453, 453, 453,
         453, 453, 453, 453, 453, 453, 453, 453, 453, 453, 453, 453, 453, 453,
         453, 453, 453, 1619, 453, 453, 453, 453, 453, 453, 453, 453, 453, 453,
         453, 453, 453, 453, 453, 453, 453, 453, 453, 453, 453, 453, 453, 453,
         453, 453, 453, 453, 453, 453, 453, 453, 453, 453, 453, 453, 453, 453,
         453, 453, 453, 453, 453, 453};

 public static int randomCoalRock() {
     return coalrock[(int) (Math.random() * coalrock.length)];
 }

 public static int daggy[] = {
 	};

 public static int randomdaggy() {
     return daggy[(int) (Math.random() * daggy.length)];
 }

 public static int whiteknight[] = {
 	6542,995, 995, 995, 995, 6587, 6589, 6591, 6599, 6601, 6605, 6607, 6609, 6611, 6613, 6615, 6617, 6619, 6621, 6623, 6625, 6627, 6629, 6631, 6633, 995, 995, 995, 995, 995};

 public static int randomwhiteknight() {
     return whiteknight[(int) (Math.random() * whiteknight.length)];
 }
	
	   public static int easteregg[] = {
 	 7928,7929,7930,7931,7932,7933};

 public static int randomeastergg() {
     return easteregg[(int) (Math.random() * easteregg.length)];
 }

 public static int Copperrock[] = {
         436, 436, 436, 436, 436, 436, 436, 436, 436, 436, 436, 436, 436, 436,
         436, 436, 436, 436, 436, 436, 436, 436, 436, 436, 436, 436, 436, 436,
         436, 436, 436, 436, 436, 436, 436, 436, 436, 436, 436, 436, 436, 436,
         436, 436, 436, 436, 436, 436, 436, 436, 436, 436, 436, 436, 436, 436,
         436, 436, 436, 436, 436, 1623, 436, 436, 436, 436, 436, 436, 436, 436,
         436, 436, 436, 436, 436, 436, 436, 436, 436, 436, 436, 436, 436, 436,
         436, 436, 436, 436, 436, 436, 436, 436, 436, 436, 1617, 436, 436, 436,
         436, 436, 436, 436, 436, 436, 436, 436, 436, 436, 436, 436, 436, 436,
         436, 436, 436, 436, 436, 436, 436, 436, 1621, 436, 436, 436, 436, 436,
         436, 436, 436, 436, 436, 436, 436, 436, 436, 436, 436, 436, 436, 436,
         436, 436, 436, 436, 436, 436, 436, 436, 436, 436, 436, 436, 436, 436,
         436, 436, 436, 1619, 436, 436, 436, 436, 436, 436, 436, 436, 436, 436,
         436, 436, 436, 436, 436, 436, 436, 436, 436, 436, 436, 436, 436, 436,
         436, 436, 436, 436, 436, 436, 436, 436, 436, 436, 436, 436, 436, 436,
         436, 436, 436, 436, 436, 436};

 public static int randomCopperRock() {
     return Copperrock[(int) (Math.random() * Copperrock.length)];
 }

 public static int Tinrock[] = {
         438, 438, 438, 438, 438, 438, 438, 438, 438, 438, 438, 438, 438, 438,
         438, 438, 438, 438, 438, 438, 438, 438, 438, 438, 438, 438, 438, 438,
         438, 438, 438, 438, 438, 438, 438, 438, 438, 438, 438, 438, 438, 438,
         438, 438, 438, 438, 438, 438, 438, 438, 438, 438, 438, 438, 438, 438,
         438, 438, 438, 438, 438, 1623, 438, 438, 438, 438, 438, 438, 438, 438,
         438, 438, 438, 438, 438, 438, 438, 438, 438, 438, 438, 438, 438, 438,
         438, 438, 438, 438, 438, 438, 438, 438, 438, 438, 1617, 438, 438, 438,
         438, 438, 438, 438, 438, 438, 438, 438, 438, 438, 438, 438, 438, 438,
         438, 438, 438, 438, 438, 438, 438, 438, 1621, 438, 438, 438, 438, 438,
         438, 438, 438, 438, 438, 438, 438, 438, 438, 438, 438, 438, 438, 438,
         438, 438, 438, 438, 438, 438, 438, 438, 438, 438, 438, 438, 438, 438,
         438, 438, 438, 1619, 438, 438, 438, 438, 438, 438, 438, 438, 438, 438,
         438, 438, 438, 438, 438, 438, 438, 438, 438, 438, 438, 438, 438, 438,
         438, 438, 438, 438, 438, 438, 438, 438, 438, 438, 438, 438, 438, 438,
         438, 438, 438, 438, 438, 438};

 public static int randomTinRock() {
     return Tinrock[(int) (Math.random() * Tinrock.length)];
 }

 public static int Ironrock[] = {
         440, 440, 440, 440, 440, 440, 440, 440, 440, 440, 440, 440, 440, 440,
         440, 440, 440, 440, 440, 440, 440, 440, 440, 440, 440, 440, 440, 440,
         440, 440, 440, 440, 440, 440, 440, 440, 440, 440, 440, 440, 440, 440,
         440, 440, 440, 440, 440, 440, 440, 440, 440, 440, 440, 440, 440, 440,
         440, 440, 440, 440, 440, 1623, 440, 440, 440, 440, 440, 440, 440, 440,
         440, 440, 440, 440, 440, 440, 440, 440, 440, 440, 440, 440, 440, 440,
         440, 440, 440, 440, 440, 440, 440, 440, 440, 440, 1617, 440, 440, 440,
         440, 440, 440, 440, 440, 440, 440, 440, 440, 440, 440, 440, 440, 440,
         440, 440, 440, 440, 440, 440, 440, 440, 1621, 440, 440, 440, 440, 440,
         440, 440, 440, 440, 440, 440, 440, 440, 440, 440, 440, 440, 440, 440,
         440, 440, 440, 440, 440, 440, 440, 440, 440, 440, 440, 440, 440, 440,
         440, 440, 440, 1619, 440, 440, 440, 440, 440, 440, 440, 440, 440, 440,
         440, 440, 440, 440, 440, 440, 440, 440, 440, 440, 440, 440, 440, 440,
         440, 440, 440, 440, 440, 440, 440, 440, 440, 440, 440, 440, 440, 440,
         440, 440, 440, 440, 440, 440};

 public static int randomIronRock() {
     return Ironrock[(int) (Math.random() * Ironrock.length)];
 }

 public static int Phat[] = {
 	6542,1038, 1040, 1042, 1044, 1046, 6542, 1048, 8000, 10560, 10109, 10110, 10111, 10112, 10113, 10114, 10115, 10116, 10117, 10118,};

 public static int randomPhat() {
     return Phat[(int) (Math.random() * Phat.length)];
 }

 public static int Silvchest[] = {6542,601, 758, 788, 983};

 public static int randomSilvchest() {
     return Silvchest[(int) (Math.random() * Silvchest.length)];
 }

 public static int fish[] = {6542,385, 385, 379, 379, 379, 379, 379};

 public static int randomFish() {
     return fish[(int) (Math.random() * fish.length)];
 }

 public static int rat[] = {
 	6542,5698, 1305, 3105, 1725, 1704, 1323, 1153, 1115, 1067, 1081, 1157, 1119,
         1069, 1083};

 public static int randomrat() {
     return rat[(int) (Math.random() * rat.length)];
 }
 
 public static int td[] = {
 	6542,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,13664,995,995,995,995,995,995,995,995,995,15185,995,995,995,995,995,995,995,995,995,995,995,15195};

public static int randomtd() {
 return td[(int) (Math.random() * td.length)];
}

 public static int barrowchest2[] = {
 	6542,4708, 4710, 4712, 4714, 4716, 4718, 4720, 4722, 4724, 4726, 4728, 4730, 4732, 4734, 4736, 4740, 4745, 4747, 4749, 4751, 4753, 4755, 4757, 4758,};

 public static int randombarrowchest2() {
     return barrowchest2[(int) (Math.random() * barrowchest2.length)];
 }

 public static int well[] = {6542,15195, 15185, 15348, 14520, 15334, 7449, 15346,};

 public static int randomwell() {
     return well[(int) (Math.random() * well.length)];
 }

 public static int soldier[] = {
 	6542,3101, 6897, 3202, 1333, 1319, 6542, 1113, 1127, 1147, 1093, 1079, 1373, 4131,
         995};

 public static int randomsoldier() {
     return soldier[(int) (Math.random() * soldier.length)];
 }

 public static int bluedragon[] = {
 	6542,2957, 1050, 14242, 4039, 14238, 14239, 14240, 14241, 14242, 1959, 6857, 6542, 6082, 4084, 2526, 6861, 7399, 7398, 2651, 6859, 4565, 7594, 10014, 6863, 2524, 4702, 7400};

 public static int randombluedragon() {
     return bluedragon[(int) (Math.random() * bluedragon.length)];
 }


 public static int MossGiants[] = {
 	6542,1179, 1141, 1193, 1389, 1243, 1285, 199, 201, 203, 205, 207, 209, 211, 213, 215, 217, 219, 556, 562, 557, 561, 563, 562, 560, 5323, 5104, 5311, 5292, 5293, 5294, 5281, 5100, 5296, 5105, 5282, 4206, 5298, 5280, 5297, 5299, 5106, 5295, 5301, 5302, 5321, 5300, 5304, 5303};

public static int randomMossGiants() {
 return MossGiants[(int) (Math.random() * MossGiants.length)];
}
 public static int irondragon[] = {
 	6542,2351, 2351, 2351, 2351, 6750, 2351, 2351, 2351, 2351, 6752, 2351, 2351, 2351, 2351};

 public static int randomirondragon() {
     return irondragon[(int) (Math.random() * irondragon.length)];
 }

 public static int ogre[] = {6542,837, 5018, 995};

 public static int randomogre() {
     return ogre[(int) (Math.random() * ogre.length)];
 }

 public static int chicken[] = {46542,834, 6542};

 public static int randomchicken() {
     return chicken[(int) (Math.random() * chicken.length)];
 }

 public static int Partyroom[] = {6542,1038, 1040, 1042, 1044, 1046,
         1048, 1050, 4716, 4718, 4720, 4722, 746, 667, 2402, 14507, 14508, 14511,
         2633, 2635, 2637, 2978, 2980, 2982, 2984, 2986, 2988, 2990, 2992, 2994,
         2957, 2653, 2655, 2659, 3478, 2665, 2661, 2663, 2667, 3479, 2673, 2669,
         2671, 3480, 35, 278, 426, 428, 1475, 4724, 4728, 4730, 4726, 6570, 1037, 1050, 6857, 6859, 6861,
         6863, 6856, 6858, 6860, 6862, 6818, 2591, 2593, 2595, 2597, 2607,
         2609, 2611, 2613, 2615, 2617, 2619, 2621, 3473, 3475, 3476, 3669, 3671,
         3672, 9004, 7319, 7321, 7323, 7325, 7327, 5527, 5529, 5531, 5533, 5535,
         5537, 5539, 5541, 5543, 5545, 5547, 5549, 5551};

 public static int randomPartyroom() {
     return Partyroom[(int) (Math.random() * Partyroom.length)];
 }

 public static int skeleton[] = {
 	6542,6137, 6139, 6141, 6542, 995, 995, 995, 995, 995, 995, 995, 995, 995, 995, 995,
         995, 995, 995, 995, 995, 995, 995, 995, 995, 995, 995, 995, 995, 995,
         995, 995, 995, 995, 995, 995, 995, 995, 995};

 public static int randomskeleton() {
     return skeleton[(int) (Math.random() * skeleton.length)];
 }

 public static int crawlinghand[] = {
 	6542,2615, 1333, 526, 526, 526, 526, 526, 526, 526, 526, 526, 526, 526, 526,
         526, 526};

 public static int randomcrawlinghand() {
     return crawlinghand[(int) (Math.random() * crawlinghand.length)];
 }

 public static int Keys[] = {
         5698, 1305, 3105, 1725, 1704, 1323, 1153, 1115, 1067, 1081, 1157, 1119,
         1069, 1083, 3101, 6897, 3202, 1333, 1319, 1113, 1127, 1147, 1093, 1079,
         1373, 4131, 2957, 1050, 4039, 1959, 6857, 6082, 4084, 2526, 6861,
         7399, 7398, 2651, 6859, 4565, 7594, 10014, 6863, 2524, 4702, 7400, 1038,
         1040, 1042, 1044, 1046, 1048, 1050, 15334, 15336, 15333, 15335, 4716, 4718, 4720, 4722,
         14507, 14508, 14511, 2633, 2635, 2637, 2978, 2980, 2982, 2984, 2986, 2988, 2990, 2992, 2994,
         2957, 2653, 2655, 2659, 3478, 2665, 2661, 2663, 2667, 3479, 2673, 2669,
         2671, 3480, 4724, 4728, 4730, 4726, 6570, 1037, 1050, 6857, 6859, 6861,
         6863, 6856, 6858, 6860, 6862, 6818, 4119, 4121, 4123, 4125, 4127, 4129, 4131, 1725, 1704,
         1038, 3105, 1305, 5698, 4587, 4726, 7386, 7394, 7390, 1037, 1989, 6666, 2944, 6779, 9093,
         8013, 8014, 8015, 8016, 8017, 8018, 8019, 4565, 1959, 1053, 1055, 1057, 1042, 1040, 1044, 1038, 1046, 1050, 1641, 2633, 2635, 2637, 2978, 2980, 2982, 2984, 2986, 2988, 2990, 2992, 2994,
         2957, 2653, 2655, 2659, 3478, 2665, 2661, 2663, 2667, 3479, 2673, 2669,
         2671, 3480, 4724, 4728, 4730, 4726, 6570, 1037, 1050, 6857, 6859, 6861,
         6863, 6856, 6858, 6860, 6862, 6818, 1641, 1038, 1040, 1042, 1044, 1046, 1048, 8000, 10560};

 public static int randomKeys() {
     return Keys[(int) (Math.random() * Keys.length)];
 }

 public static int cavebug[] = {
 	6542,4119, 4121, 4123, 4125, 4127, 4129, 4131, 526, 526, 526, 526, 526, 526,
         526, 526, 526, 526, 526, 526, 526, 526, 526, 526, 526, 526};

 public static int randomcavebug() {
     return cavebug[(int) (Math.random() * cavebug.length)];
 }

 public static int jelly[] = {
 	6542,14860, 6809, 526, 526, 526, 526, 526, 526, 526, 526, 526, 526, 526, 526,
         526, 526, 526, 526, 526, 526, 526, 526, 526, 526, 526, 526, 526, 526,
         526, 526};

 public static int randomjelly() {
     return jelly[(int) (Math.random() * jelly.length)];
 }

 public static int aberrantspecter[] = {
 	6542,3840, 3842, 3844, 526, 526, 526, 526, 526, 526, 526, 526, 526, 526, 526,
         526, 526, 526, 526, 526, 526, 526, 526, 526, 526, 526, 526, 526, 526,
         526};

 public static int randomaberrantspecter() {
     return aberrantspecter[(int) (Math.random() * aberrantspecter.length)];
 }

 public static int abyssaldemon[] = {
 	6542,4151, 526, 526, 526, 526, 526, 526, 526, 526, 526, 526, 526, 526, 526,
         526, 526, 526, 526, 526, 526, 526, 526, 526, 526, 526};

 public static int randomabyssaldemon() {
     return abyssaldemon[(int) (Math.random() * abyssaldemon.length)];
 }

 public static int darkbeast[] = {
 	6542,6818, 11192, 526, 526, 526, 526, 526, 526, 526, 526, 526, 526, 526, 526,
         526, 526, 526, 526, 526, 526, 526, 526, 526, 526, 526, 526, 526, 526,
         526, 526, 526, 526, 526, 526, 526, 526, 526, 526, 526, 526, 526, 526,
         526, 526, 526, 526, 526, 526, 526, 526, 526, 526, 526, 526, 526, 526,
         526, 526, 526, 526, 526, 526, 526, 526, 526, 526, 526, 526, 526, 526,
         526, 526, 526, 526, 526, 526, 526, 526};

 public static int randomdarkbeast() {
     return darkbeast[(int) (Math.random() * darkbeast.length)];
 }

 public static int barbarian[] = {
 	6542};

 public static int randombarbarian() {
     return barbarian[(int) (Math.random() * barbarian.length)];
 }

 public static int fishy[] = {
 	6542,995, 995, 995, 995, 35, 995, 995, 995, 995, 773, 995, 995, 995, 995, 995};

 public static int randomfishy() {
     return fishy[(int) (Math.random() * fishy.length)];
 }

 public static int Dagannoths[] = {
 	6542,1037,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995, 1989, 6666, 2944, 6779, 9093, 6542, 8013, 8014, 13444, 13445, 13446, 13447, 13448, 13449, 995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,13450, 13451, 13452, 13453, 13454, 13455, 13456, 13457, 13558,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995, 13459, 13460, 13461, 13462, 13463,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,13464, 13465, 13466, 13467, 13468, 13469, 13470, 13471, 13472, 13473, 13474, 13475, 13476, 13477, 13478, 13479, 13480, 13481, 8015, 8016, 8017, 8018, 8019, 4565, 1959, 1053,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995,995, 1055, 1057, 1042, 1040, 1044, 1038, 1046, 1050};

 public static int randomDagannoths() {
     return Dagannoths[(int) (Math.random() * Dagannoths.length)];
 }

 public static int unicorn[] = {
 	6542,6966, 995, 995, 995, 995, 995, 995, 995, 995, 995, 995, 995, 995, 995,
         995, 995, 995, 995, 995};

 public static int randomunicorn() {
     return unicorn[(int) (Math.random() * unicorn.length)];
 }

 public static int battlemagesara[] = {
 	6542,14507, 14508, 14513, 5698, 995, 995, 995, 995, 995, 995, 995, 995, 995,
         995, 995, 995, 995, 995, 995, 995, 995, 995, 995, 995, 995, 995, 995,
         995, 995, 995, 995};

 public static int randombattlemagesara() {
     return battlemagesara[(int) (Math.random() * battlemagesara.length)];
 }

 public static int battlemagezammy[] = {
 	6542,14507, 14508, 14512, 5698, 995, 995, 995, 995, 995, 995, 995, 995, 995,
         995, 995, 995, 995, 995, 995, 995, 995, 995, 995, 995, 995, 995, 995,
         995, 995, 995, 995};

 public static int randombattlemagezammy() {
     return battlemagezammy[(int) (Math.random() * battlemagezammy.length)];
 }

 public static int battlemageguthix[] = {
 	6542,14507, 14508, 14511, 5698, 995, 995, 995, 995, 995, 995, 995, 995, 995,
         995, 995, 995, 995, 995, 995, 995, 995, 995, 995, 995, 995, 995, 995,
         995, 995, 995, 995};

 public static int randombattlemageguthix() {
     return battlemageguthix[(int) (Math.random() * battlemageguthix.length)];
 }

 public static int troll[] = {
 	6542,3741, 3741, 3741, 3741, 995, 995, 995, 995, 995, 995, 995, 995, 995, 995,
         995, 995, 995, 995, 995, 995, 995, 995, 995};

 public static int randomtroll() {
     return troll[(int) (Math.random() * troll.length)];
 }

 public static int KQ[] = {
         2633, 2635, 2637, 2978, 2980, 2982, 2984, 2986, 2988, 2990, 2992, 2994,
         2957, 2653, 2655, 2659, 3478, 2665, 2661, 2663, 2667, 3479, 2673, 2669,
         2671, 3480, 4724, 6542,4728, 4730, 4726, 6570, 1037, 1050, 6857, 6859, 6861,
         6863, 6856, 6858, 6860, 6862, 6542, 6818};

 public static int randomKQ() {
     return KQ[(int) (Math.random() * KQ.length)];
 }

 public static int Jogre[] = {4587,6542, 15352, 7158, 6542, 1149, 4151, 5698, 1377, 1305, 1434};

 public static int randomJogre() {
     return Jogre[(int) (Math.random() * Jogre.length)];
 }

 public static int Ice_giant[] = {1543,6542, 1546, 1545, 1548};

 public static int randomIce_giant() {
     return Ice_giant[(int) (Math.random() * Ice_giant.length)];
 }

 public static int Dagannoth_Rex[] = {532, 532, 532, 532, 532, 532, 532, 532, 532, 532, 532, 532, 532, 15348, 15349, 532, 532, 532};

 public static int randomDagannoth_Rex() {
     return Dagannoth_Rex[(int) (Math.random() * Dagannoth_Rex.length)];
 }

 public static int Dagannoth_Prime[] = {532, 532, 532, 532, 532, 532, 532, 532, 532, 532, 532, 532, 15350, 15334, 532, 532, 532};

 public static int randomDagannoth_Prime() {
     return Dagannoth_Prime[(int) (Math.random() * Dagannoth_Prime.length)];
 }

 public static int Dagannoth_Supreme[] = {532, 532, 532, 532, 532, 532, 532, 532, 532, 532, 532, 532, 15185, 15195, 532, 532, 532};

 public static int randomDagannoth_Supreme() {
     return Dagannoth_Supreme[(int) (Math.random() * Dagannoth_Supreme.length)];
 }

 public static int Flambeed[] = {6104, 991};

 public static int randomFlambeed() {
     return Flambeed[(int) (Math.random() * Flambeed.length)];
 }

 public static int Black_Demon[] = {532, 532, 6542,532, 532, 532, 532, 532, 532, 532, 532, 532, 14503, 14505, 14504, 532, 532, 532};

 public static int randomBlack_Demon() {
     return Black_Demon[(int) (Math.random() * Black_Demon.length)];
 }

 public static int Skeleton_Hellhound[] = {6542,532, 532, 532, 532, 532, 532, 532, 532, 532, 532, 14507, 14506, 14508, 14509, 532, 532, 532};

 public static int randomSkeleton_Hellhound() {
     return Skeleton_Hellhound[(int) (Math.random() * Skeleton_Hellhound.length)];
 }

 public static int Agrith_Naar[] = {6542,532, 532, 532, 532, 532, 532, 532, 532, 532, 532, 532, 532, 532, 7449, 3140, 532, 532, 532};

 public static int randomAgrith_Naar() {
     return Agrith_Naar[(int) (Math.random() * Agrith_Naar.length)];
 }


 public static int Arzinian_Being_of_Bordanzan[] = {6542,532, 532, 532, 532, 532, 532, 532, 532, 532, 532, 532, 532, 15345, 14521, 15346, 532, 532, 532};

 public static int randomArzinian_Being_of_Bordanzan() {
     return Arzinian_Being_of_Bordanzan[(int) (Math.random() * Arzinian_Being_of_Bordanzan.length)];
 }

 public static int KBD[] = {6542,15352, 15334, 4585, 14519, 4087, 15195};

 public static int randomKBD() {
     return KBD[(int) (Math.random() * KBD.length)];
 }
}
