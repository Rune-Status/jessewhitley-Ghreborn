package Ghreborn.model.players.combat.melee;

import Ghreborn.model.npcs.animations.BlockAnimation;
import Ghreborn.model.players.*;
import Ghreborn.model.players.combat.magic.MagicData;
import Ghreborn.*;
import Ghreborn.core.PlayerHandler;

public class MeleeData {

	public static int getKillerId(Client c, int playerId) {
		int oldDamage = 0;
		int killerId = 0;
		for (int i = 1; i < Config.MAX_PLAYERS; i++) {
			if (PlayerHandler.players[i] != null) {
				if (PlayerHandler.players[i].killedBy == playerId) {
					if (PlayerHandler.players[i]
							.withinDistance(PlayerHandler.players[playerId])) {
						if (PlayerHandler.players[i].totalPlayerDamageDealt > oldDamage) {
							oldDamage = PlayerHandler.players[i].totalPlayerDamageDealt;
							killerId = i;
						}
					}
					PlayerHandler.players[i].totalPlayerDamageDealt = 0;
					PlayerHandler.players[i].killedBy = 0;
				}
			}
		}
		return killerId;
	}

	public static void resetPlayerAttack(Player c) {
		c.usingMagic = false;
		c.npcIndex = 0;
		c.face(null);
		c.playerIndex = 0;
		c.getPA().resetFollow();
	}

	public static boolean usingHally(Player c) {
		switch (c.playerEquipment[c.playerWeapon]) {
		case 3190:
		case 3192:
		case 3194:
		case 3196:
		case 3198:
		case 2054:
		case 3202:
		case 3204:
		case 13092:
		case 13091:
			return true;

		default:
			return false;
		}
	}

	public static void getPlayerAnimIndex(Player c, String weaponName) {
		c.playerStandIndex = 0x328;
		c.playerTurnIndex = 0x337;
		c.playerWalkIndex = 0x333;
		c.playerTurn180Index = 0x334;
		c.playerTurn90CWIndex = 0x335;
		c.playerTurn90CCWIndex = 0x336;
		c.playerRunIndex = 0x338;

		if (weaponName.contains("halberd") || weaponName.contains("scythe of vitur") || weaponName.contains("hasta") || weaponName.contains("guthan")
				|| weaponName.contains("sceptre")) {
			c.playerStandIndex = 809;
			c.playerWalkIndex = 1146;
			c.playerRunIndex = 1210;
			return;
		}
		if (weaponName.contains("sled")) {
			c.playerStandIndex = 1461;
			c.playerWalkIndex = 1468;
			c.playerRunIndex = 1467;
			c.playerTurnIndex = 1468;
			c.playerTurn180Index = 1468;
			c.playerTurn90CWIndex = 1468;
			c.playerTurn90CCWIndex = 1468;
			return;
		}
		if (weaponName.contains("hunting knife")) {
			c.playerStandIndex = 7329;
			c.playerWalkIndex = 7327;
			c.playerRunIndex = 7327;
			return;
		}
		
		if (weaponName.contains("bulwark")) {
			c.playerStandIndex = 7508;
			c.playerWalkIndex = 7510;
			c.playerRunIndex = 7509;
			return;
		}
		
		if (weaponName.contains("elder maul")) {
			c.playerStandIndex = 7518;
			c.playerWalkIndex = 7520;
			c.playerRunIndex = 7519;
			return;
		}
		
		if (weaponName.contains("ballista")) {
			c.playerStandIndex = 7220;
			c.playerWalkIndex = 7223;
			c.playerRunIndex = 7221;
			return;
		}
		if (weaponName.contains("clueless")) {
			c.playerStandIndex = 7271;
			c.playerWalkIndex = 7272;
			c.playerRunIndex = 7273;
			return;
		}
		if (weaponName.contains("casket")) {
			c.playerRunIndex = 7274;
			return;
		}
		if (weaponName.contains("dharok")) {
			c.playerStandIndex = 0x811;
			c.playerWalkIndex = 2064;
			return;
		}
		if (weaponName.contains("ahrim")) {
			c.playerStandIndex = 809;
			c.playerWalkIndex = 1146;
			c.playerRunIndex = 1210;
			return;
		}
		if (weaponName.contains("verac")) {
			c.playerStandIndex = 1832;
			c.playerWalkIndex = 1830;
			c.playerRunIndex = 1831;
			return;
		}
		if (weaponName.contains("wand") || weaponName.contains("staff") || weaponName.contains("banner")  || weaponName.contains("trident")) {
			c.playerStandIndex = 809;
			c.playerRunIndex = 1210;
			c.playerWalkIndex = 1146;
			return;
		}
		if (weaponName.contains("karil")) {
			c.playerStandIndex = 2074;
			c.playerWalkIndex = 2076;
			c.playerRunIndex = 2077;
			return;
		}
		if (weaponName.contains("2h sword") || weaponName.contains("godsword") || weaponName.contains("saradomin sw")
				|| weaponName.contains("saradomin's")) {
			c.playerStandIndex = 2065;
			c.playerWalkIndex = 2562;
			c.playerRunIndex = 2563;
			return;
		}
		if (weaponName.contains("bow")) {
			c.playerStandIndex = 808;
			c.playerWalkIndex = 819;
			c.playerRunIndex = 824;
			return;
		}
		
		switch (c.playerEquipment[c.playerWeapon]) {
		case 19481:
			c.playerStandIndex = 7220;
			c.playerWalkIndex = 7223;
			c.playerRunIndex = 7221;
			break;
		case 7158:
			c.playerStandIndex = 2065;
			c.playerWalkIndex = 2064;
			break;
		case 11824:
			c.playerStandIndex = 809;
			c.playerWalkIndex = 1146;
			c.playerRunIndex = 1210;
			break;
		case 4151:
		case 12006:
		case 12773:
		case 12774:
		case 21213:
		case 21214:
		case 21215:
		case 21216:
		case 21217:
		case 21218:
		case 21219:
		case 21220:
			c.playerWalkIndex = 1660;
			c.playerRunIndex = 1661;
			break;
		case 8004:
		case 7960:
			c.playerStandIndex = 2065;
			c.playerWalkIndex = 2064;
			break;
		case 6528:
		case 20756:
			c.playerStandIndex = 0x811;
			c.playerWalkIndex = 2064;
			c.playerRunIndex = 1664;
			break;
		case 12848:
		case 4153:
		case 23986:
			c.playerStandIndex = 1662;
			c.playerWalkIndex = 1663;
			c.playerRunIndex = 1664;
			break;
		case 10887:
			c.playerStandIndex = 5869;
			c.playerWalkIndex = 5867;
			c.playerRunIndex = 5868;
			break;
		case 11802:
		case 11804:
		case 11838:
		case 12809:
		case 11806:
		case 11808:
			c.playerStandIndex = 7053;
			c.playerWalkIndex = 7052;
			c.playerRunIndex = 7043;
			c.playerTurnIndex = 7044;
			c.playerTurn180Index = 7044;
			c.playerTurn90CWIndex = 7044;
			c.playerTurn90CCWIndex = 7044;
			break;
		case 1305:
			c.playerStandIndex = 809;
			break;
		}
	}

	public static int getWepAnim(Player c, String weaponName) {
		if (c.playerEquipment[c.playerWeapon] <= 0) {
			switch (c.fightMode) {
			case 0:
				return 422;
			case 2:
				return 423;
			case 1:
				return 422;
			}
		}
		if (weaponName.contains("toxic blowpipe")) {
			return 5061;
		}
		if (weaponName.contains("elder maul")) {
			return 7516;
		}
		if (weaponName.contains("dart")) {
			// eturn c.fightMode == 2 ? 582 : 6600;
			return 6600;
		}
		if (weaponName.contains("dragon 2h")) {
			return 407;
		}
		if (weaponName.contains("knife") || weaponName.contains("javelin") || weaponName.contains("thrownaxe")) {
			return 806;
		}
		if (weaponName.contains("cross") && !weaponName.contains("karil")
				|| weaponName.contains("c'bow") && !weaponName.contains("karil")) {
			return 4230;
		}
		if (weaponName.contains("halberd") || weaponName.contains("scythe of vitur")) {
			return 440;
		}
		if (weaponName.startsWith("dragon dagger")) {
			return 402;
		}
		if (weaponName.endsWith("dagger")) {
			return 412;
		}
		if (weaponName.contains("2h sword") || weaponName.contains("godsword")
				|| weaponName.contains("aradomin sword")) {
			if (c.fightMode == 0)
				return 406;
			else if (c.fightMode == 1)
				return 406;
			else if (c.fightMode == 2)
				return 406;
			else
				return 406;
		}
		if (weaponName.contains("dharok")) {
			switch (c.fightMode) {
			case 0:// attack
				return 2067;
			case 2:// str
				return 2067;
			case 1:// def
				return 2067;
			case 3:// crush
				return 2066;
			}
		}
		if (weaponName.contains("sword") && !weaponName.contains("training")) {
			return 451;
		}
		if (weaponName.contains("karil")) {
			return 2075;
		}
		if (weaponName.contains("bow") && !weaponName.contains("'bow") && !weaponName.contains("karil")) {
			return 426;
		}
		if (weaponName.contains("'bow") && !weaponName.contains("karil")) {
			return 4230;
		}
		if (weaponName.contains("hasta")) {
			return 4198;
		}
		switch (c.playerEquipment[c.playerWeapon]) { // if you don't want to use
		case 19481:
			return 7218;
		case 10581: // strings
			return 402;
		case 9703:
			return 412;

		case 6522:
			return 2614;
		case 10034:
		case 10033:
			return 2779;
		case 11791:
			return 440;
		case 8004:
		case 7960:
			return 2075;
		case 12848:
		case 4153: // granite maul
		case 23986:
			return 1665;
		case 11824:
		case 23994:
		case 4726: // guthan
			return 2080;
		case 4747: // torag
			return 0x814;
		case 4710: // ahrim
			return 406;
		case 4755: // verac
			return 2062;
		case 4734: // karil
			return 2075;
		case 4151:
		case 12006:
		case 12773:
		case 12774:
		case 21213:
		case 21214:
		case 21215:
		case 21216:
		case 21217:
		case 21218:
		case 21219:
		case 21220:
			return 1658;
		case 6528:
			return 2661;
		case 10887:
			return 5865;
		default:
			return 451;
		}
	}

	public static int getBlockEmote(Player c) {
		c.getItems();
		String shield = c.getItems().getItemName(c.playerEquipment[c.playerShield]).toLowerCase();
		c.getItems();
		String weapon = c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase();
		if (shield.contains("defender"))
			return 4177;
		if (shield.contains("2h") && c.playerEquipment[c.playerWeapon] != 7158)
			return 7050;
		if (shield.contains("book")
				|| (weapon.contains("wand") || (weapon.contains("staff") || weapon.contains("trident"))))
			return 420;
		if (shield.contains("shield"))
			return 1156;
		if (weapon.contains("elder maul")) {
			return 7517;
		}
		switch (c.playerEquipment[c.playerWeapon]) {
		case 19481:
			return 7219;
		case 1734:
		case 411:
			return 3895;
		case 1724:
			return 3921;
		case 1709:
			return 3909;
		case 1704:
			return 3916;
		case 1699:
			return 3902;
		case 1689:
			return 3890;
		case 4755:
			return 2063;
		case 15241:
			return 12156;
		case 18355:
			return 13046;
		case 13652:
		case 14484:
			return 397;
		case 3101:
			return 397;
		/*
		 * case 11824: return 12008;
		 */
		case 12848:
		case 4153:
		case 23986:
			return 1666;
		case 7158:
			return 410;
		case 4151:
		case 12006:
		case 12773:
		case 12774:
		case 21213:
		case 21214:
		case 21215:
		case 21216:
		case 21217:
		case 21218:
		case 21219:
		case 21220:
			return 1659;
		case 15486:
			return 11806;
		case 18349:
			return 12030;
		case 18353:
			return 13054;

		case 11802:
		case 11806:
		case 11808:
		case 11804:
		case 11838:
		case 12809:
		case 11730:
			return 7056;
		case -1:
			return 424;
		default:
			return 424;
		}
	}

	public static int getAttackDelay(Player c, String s) {
		if (c.usingMagic) {
			if (c.spellId == 52 || c.spellId == 53) {
				return 4;
			}
			switch (MagicData.MAGIC_SPELLS[c.spellId][0]) {
			case 12871: // ice blitz
			case 13023: // shadow barrage
			case 12891: // ice barrage
				return 5;

			default:
				return 5;
			}
		}
		if (c.playerEquipment[c.playerWeapon] == -1)
			return 4;// unarmed
		switch (c.playerEquipment[c.playerWeapon]) {
		case 5698:
			return 4;
		case 11889:
			return 5;
		case 11824:
			return 5;
		case 11785:
			return 6;
		case 9185:
		case 19481:
			return 6;
		case 12926:
			return 3;
		case 12765:
		case 12766:
		case 12767:
		case 12768:
		case 11235:
			return 9;
		case 12424:
		case 11838:
		case 12809:
			return 4;
		case 6528:
			return 7;
		case 10033:
		case 10034:
			return 5;
		case 9703:
			return 5;
		}
		if (s.contains("dagger")) {
			return 4;
		}
		if (s.endsWith("greataxe"))
			return 7;
		else if (s.equals("torags hammers"))
			return 5;
		else if (s.equals("barrelchest anchor"))
			return 7;
		else if (s.equals("guthans warspear"))
			return 5;
		else if (s.equals("veracs flail"))
			return 5;
		else if (s.equals("ahrims staff"))
			return 6;
		else if (s.contains("staff")) {
			if (s.contains("zamarok") || s.contains("guthix") || s.contains("saradomian") || s.contains("slayer")
					|| s.contains("ancient"))
				return 4;
			else
				return 5;
		} else if (s.contains("bow")) {
			if (s.contains("composite") || s.equals("seercull"))
				return 5;
			else if (s.contains("aril"))
				return 4;
			else if (s.contains("Ogre"))
				return 8;
			else if (s.contains("short") || s.contains("hunt") || s.contains("sword"))
				return 4;
			else if (s.contains("long") || s.contains("crystal"))
				return 6;
			else if (s.contains("'bow") && !s.contains("armadyl")) {
				return 7;
			}
			return 5;
		} 
		else if (s.contains("godsword") || s.contains("2h"))
			return 6;
		else if (s.contains("longsword"))
			return 5;
		else if (s.contains("sword"))
			return 4;
		else if (s.contains("scimitar") || s.contains("of the dead"))
			return 4;
		else if (s.contains("mace"))
			return 5;
		else if (s.contains("battleaxe"))
			return 6;
		else if (s.contains("pickaxe"))
			return 5;
		else if (s.contains("thrownaxe"))
			return 5;
		else if (s.contains("axe"))
			return 5;
		else if (s.contains("warhammer"))
			return 6;
		else if (s.contains("2h"))
			return 7;
		else if (s.contains("spear"))
			return 5;
		else if (s.contains("claw"))
			return 4;
		else if (s.contains("halberd"))
			return 7;
		else if (s.equals("granite maul"))
			return 7;
		else if (s.equals("toktz-xil-ak"))// sword
			return 4;
		else if (s.equals("tzhaar-ket-em"))// mace
			return 5;
		else if (s.equals("tzhaar-ket-om"))// maul
			return 7;
		else if (s.equals("toktz-xil-ek"))// knife
			return 4;
		else if (s.equals("toktz-xil-ul"))// rings
			return 4;
		else if (s.equals("toktz-mej-tal"))// staff
			return 6;
		else if (s.contains("whip") || s.contains("tentacle") || s.contains("bludgeon"))
			return 4;
		else if (s.contains("dart"))
			return 3;
		else if (s.contains("knife"))
			return 3;
		else if (s.contains("javelin"))
			return 6;
		return 5;
	}
	public static int getHitDelay(Player c, int i, String weaponName) {
		if (c.usingMagic) {
			switch (MagicData.MAGIC_SPELLS[c.spellId][0]) {
			case 12891:
				return 4;
			case 12871:
				return 6;
			default:
				return 4;
			}
		}
		if (weaponName.contains("Karil's crossbow")) {
			return 3;
		}
		if (weaponName.contains("dart")) {
			return 3;
		}
		if (weaponName.contains("knife") || weaponName.contains("javelin") || weaponName.contains("thrownaxe")) {
			return 3;
		}
		/*
		 * if (weaponName.contains("cross") || weaponName.contains("c'bow")) {
		 * return 5; }
		 */
		if (weaponName.contains("bow") && !c.dbowSpec) {
			return 4;
		} else if (c.dbowSpec) {
			return 4;
		}

		switch (c.playerEquipment[c.playerWeapon]) {
		case 767:
		case 837:
		case 8880:
		case 9174:
		case 9176:
		case 9177:
		case 9179:
		case 9181:
		case 9183:
		case 9185:
		case 19481:
		case 11165:
		case 11167:
		case 11785:
			return 5;
		case 4734:
			return 3;
		case 6522: // Toktz-xil-ul
			return 3;
		case 10887:
			return 3;
		case 10034:
		case 10033:
			return 3;
		default:
			return 2;
		}
	}

	public static int npcDefenceAnim(int i) {
		return BlockAnimation.handleEmote(i);
	}
	
}
