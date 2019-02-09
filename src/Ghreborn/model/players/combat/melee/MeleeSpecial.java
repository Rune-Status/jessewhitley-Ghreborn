package Ghreborn.model.players.combat.melee;

import Ghreborn.model.npcs.NPC;
import Ghreborn.model.npcs.NPCHandler;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.Player;
import Ghreborn.core .PlayerHandler;
import Ghreborn.util.Misc;

	/**
	 *
	 * Weapon Specials
	 *
	 * Class MeleeData
	 * @author 2012
	 *
	 */

public class MeleeSpecial {

	public static boolean checkSpecAmount(Client c, int weapon) {
		switch(weapon) {
			case 1249:
			case 1215:
			case 1231:
			case 5680:
			case 5698:
			case 1305:
			case 1434:
			if(c.specAmount >= 2.5) {
				c.specAmount -= 2.5;
				c.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;
			
			case 4151:
			case 15333:
			case 11698:
			case 4153:
			case 14484:
			case 10887:
			if(c.specAmount >= 5) {
				c.specAmount -= 5;
				c.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;
			case 11785:
				if (c.specAmount > 4) {
					c.specAmount -= 4;
					c.getItems().addSpecialBar(weapon);
					return true;
				}
				return false;
 			case 19780:
			if(c.specAmount >= 6) {
				c.specAmount -= 6;
				c.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;
			
 			case 12809:
 			case 19481:
 				if (c.specAmount > 6.5) {
 					c.specAmount -= 6.5;
 					c.getItems().addSpecialBar(weapon);
 					return true;
 				}
 				return false;
			case 3204:
			if(c.specAmount >= 3) {
				c.specAmount -= 3;
				c.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;
			
			case 1377:
			case 11696:
			case 11730:
			if(c.specAmount >= 10) {
				c.specAmount -= 10;
				c.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;
			
			case 4587:
			case 859:
			case 861:
			case 4827:
			case 11700:
			if(c.specAmount >= 5.5) {
				c.specAmount -= 5.5;
				c.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;

			
			default:
			return true; // incase u want to test a weapon
		}
	}

	public static void activateSpecial(Client c, int weapon, int i){
		if (NPCHandler.npcs[i] == null && c.npcIndex > 0) {
			return;
		}
		Player o = null;

		NPC npc = null;

		if (c.npcIndex > 0) {
			npc = NPCHandler.npcs[i];
		}

		if (c.playerIndex > 0) {
			o = PlayerHandler.players[i];
		}

		if (o == null && npc == null) {
			return;
		}

		c.doubleHit = false;
		c.specEffect = 0;
		c.projectileStage = 0;
		c.specMaxHitIncrease = 2;
		if(c.npcIndex > 0) {
			c.oldNpcIndex = i;
		} else if (c.playerIndex > 0){
			c.oldPlayerIndex = i;
			PlayerHandler.players[i].underAttackBy = c.index;
			PlayerHandler.players[i].logoutDelay.reset();
			PlayerHandler.players[i].singleCombatDelay = System.currentTimeMillis();
			PlayerHandler.players[i].killerId = c.index;
		}
		if(c.playerIndex > 0) {
			c.getPA().followPlayer();
		} else if(c.npcIndex > 0) {
			c.getPA().followNpc();
		}
		switch(weapon) {
			case 10887:
			c.gfx0(1027);
			c.animation(5870);
			c.hitDelay = c.getCombat().getHitDelay(i, c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
			c.specDamage = 1.20;
			c.specAccuracy = 1.85;
			break;

			case 1305: // dragon long
			c.gfx100(248);
			c.animation(1058);
			c.hitDelay = c.getCombat().getHitDelay(i, c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
			c.specAccuracy = 1.10;
			c.specDamage = 1.20;
			break;
			case 11785:
				c.animation(4230);
				c.gfx100(301);
				c.acbSpec = true;
				c.usingBow = true;
				c.rangeItemUsed = c.playerEquipment[c.playerArrows];
				if (c.playerIndex > 0)
					c.getCombat().fireProjectilePlayer(0);
				else if (c.npcIndex > 0)
					c.getCombat().fireProjectileNpc(0);
				c.hitDelay = c.getCombat().getHitDelay(i,
						c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
				c.specAccuracy = 1.60;
				c.specDamage = 1.55;
				c.getCombat().calculateRangeAttack();
				break;
				
			case 19780:
			c.gfx100(1247);
			c.animation(4000);
			c.specAccuracy = 10.00;
			c.specDamage = 1.75;
			c.ssSpec = true;
			c.hitDelay = c.getCombat().getHitDelay(i, c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
			break;
			
			case 1215: // dragon daggers
			case 1231:
			case 5680:
			case 5698:
			c.gfx100(252);
			c.animation(1062);
			c.hitDelay = c.getCombat().getHitDelay(i, c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
			c.doubleHit = true;
			c.specAccuracy = 1.30;
			c.specDamage = 1.05;
			break;
			
			case 11730:
			c.gfx100(1224);
			c.animation(7072);
			c.hitDelay = c.getCombat().getHitDelay(i, c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
			c.doubleHit = true;
			c.ssSpec = true;
			c.specAccuracy = 1.30;
			break;

			case 14484: // Dragon claws
			c.animation(7000);
			c.usingClaws = true;
			c.doubleHit = true;
			c.hitDelay = c.getCombat().getHitDelay(i, c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
			break;

			case 4151: // whip
			if(NPCHandler.npcs[i] != null) {
				NPCHandler.npcs[i].gfx100(341);
			}
			c.specAccuracy = 1.10;
			c.animation(1658);
			c.hitDelay = c.getCombat().getHitDelay(i, c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
			break;
			
			case 15333: // ags
			c.animation(7074);
			c.specDamage = 1.25;
			c.specAccuracy = 1.85;
			c.gfx0(1222);
			c.hitDelay = c.getCombat().getHitDelay(i, c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
			break;
			
			case 11700:
				c.animation(7070);		
				c.gfx0(1221);
				c.specAccuracy = 1.25;
				c.hitDelay = c.getCombat().getHitDelay(i, c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
				c.specEffect = 2;
			break;
			
			case 11696:
				c.animation(7073);
				c.gfx0(1223);
				c.specDamage = 1.10;
				c.specAccuracy = 1.5;
				c.hitDelay = c.getCombat().getHitDelay(i, c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
				c.specEffect = 3;
			break;
			
			case 11698:
				c.animation(7071);
				c.gfx0(1220);
				c.specAccuracy = 1.25;
				c.specEffect = 4;
				c.hitDelay = c.getCombat().getHitDelay(i, c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
			break;
			
			
			case 3204: // d hally
			c.gfx100(282);
			c.animation(1203);
			c.hitDelay = c.getCombat().getHitDelay(i, c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
			if(NPCHandler.npcs[i] != null && c.npcIndex > 0) {
				if(!c.goodDistance(c.getX(), c.getY(), NPCHandler.npcs[i].getX(), NPCHandler.npcs[i].getY(), 1)){
					c.doubleHit = true;
				}
			}
			if(PlayerHandler.players[i] != null && c.playerIndex > 0) {
				if(!c.goodDistance(c.getX(), c.getY(), PlayerHandler.players[i].getX(),PlayerHandler.players[i].getY(), 1)){
					c.doubleHit = true;
					c.delayedDamage2 = Misc.random(c.getCombat().calculateMeleeMaxHit());
				}
			}
			break;
			
			case 4153: // maul
			c.animation(1667);
			c.hitDelay = c.getCombat().getHitDelay(i, c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
			/*if (c.playerIndex > 0)
				gmaulPlayer(i);
			else
				gmaulNpc(i);*/
			c.gfx100(337);
			break;
			
			case 4587: // dscimmy
			c.gfx100(347);
			c.specEffect = 1;
			c.animation(1872);
			c.hitDelay = c.getCombat().getHitDelay(i, c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
			break;
			
			case 1434: // mace
			c.animation(1060);
			c.gfx100(251);
			c.specMaxHitIncrease = 3;
			c.hitDelay = c.getCombat().getHitDelay(i, c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase())+1;
			c.specDamage = 1.35;
			c.specAccuracy = 1.15;
			break;
			
			case 859: // magic long
			c.usingBow = true;
			c.bowSpecShot = 3;
			c.rangeItemUsed = c.playerEquipment[c.playerArrows];
			c.getItems().deleteArrow();	
			c.lastWeaponUsed = weapon;
			c.animation(426);
			c.gfx100(250);	
			c.hitDelay = c.getCombat().getHitDelay(i, c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
			c.projectileStage = 1;
			if (c.fightMode == 2)
				c.attackTimer--;
			break;
			
			case 861: // magic short	
			c.usingBow = true;			
			c.bowSpecShot = 1;
			c.rangeItemUsed = c.playerEquipment[c.playerArrows];
			c.getItems().deleteArrow();
			c.getItems().deleteArrow();
			c.lastWeaponUsed = weapon;
			c.animation(1074);
			c.hitDelay = 3;
			c.projectileStage = 1;
			c.hitDelay = c.getCombat().getHitDelay(i, c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
			if (c.fightMode == 2)
				c.attackTimer--;
			if (c.playerIndex > 0)
				c.getCombat().fireProjectilePlayer(0);
			else if (c.npcIndex > 0)
				c.getCombat().fireProjectileNpc(0);	
			break;
			
		case 4827: // dark bow	
			c.usingBow = true;
			c.dbowSpec = true;
			c.rangeItemUsed = c.playerEquipment[c.playerArrows];
			c.getItems().deleteArrow();
			c.getItems().deleteArrow();
			if (c.playerIndex > 0) {
				c.getItems().dropArrowPlayer();
			} else if(c.npcIndex > 0) {
				c.getItems().dropArrowNpc();
			}
			c.lastWeaponUsed = weapon;
			c.hitDelay = 3;
			c.animation(426);
			c.projectileStage = 1;
			c.gfx100(c.getCombat().getRangeStartGFX());
			c.hitDelay = c.getCombat().getHitDelay(i, c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
			if (c.fightMode == 2)
				c.attackTimer--;
			if (c.playerIndex > 0)
				c.getCombat().fireProjectilePlayer(0);
			else if (c.npcIndex > 0)
				c.getCombat().fireProjectileNpc(0);
			c.specAccuracy = 1.85;
			c.specDamage = 1.65;
		break;
		}
		if (c.playerEquipment[c.playerWeapon] == 861 || c.playerEquipment[c.playerWeapon] == 11235
				|| c.playerEquipment[c.playerWeapon] == 11785 || c.playerEquipment[c.playerWeapon] == 12926 || c.playerEquipment[c.playerWeapon] == 19481) {
			c.delayedDamage = Misc.random(c.getCombat().rangeMaxHit());
			c.delayedDamage2 = Misc.random(c.getCombat().rangeMaxHit());
		} else {
			c.delayedDamage = Misc.random(c.getCombat().calculateMeleeMaxHit());
			c.delayedDamage2 = Misc.random(c.getCombat().calculateMeleeMaxHit());
		}
	
		c.usingSpecial = false;
		c.getItems().updateSpecialBar();
	}
}