package Ghreborn.model.players.combat.magic;

import Ghreborn.model.players.*;
import Ghreborn.model.players.combat.Hitmark;
import Ghreborn.*;
import Ghreborn.util.Misc;
import Ghreborn.model.npcs.NPC;
import Ghreborn.model.npcs.NPCHandler;
import Ghreborn.core.PlayerHandler;

public class MagicExtras {

	public static void multiSpellEffectNPC(Client c, int npcId, int damage) {					
		switch(MagicData.MAGIC_SPELLS[c.oldSpellId][0]) {
			case 12891:
			case 12881:
				if (NPCHandler.npcs[npcId].freezeTimer < -4) {
					NPCHandler.npcs[npcId].freezeTimer = c.getCombat().getFreezeTime();
				}
			break;
		}	
	}

	public static boolean checkMultiBarrageReqsNPC(int i) {
		if(NPCHandler.npcs[i] == null) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean checkMultiBarrageReqs(Client c, int i) {
		if(PlayerHandler.players[i] == null) {
			return false;
		}
		if (i == c.index)
			return false;
		if(!PlayerHandler.players[i].inWild()) {
			return false;
		}
		if(Config.COMBAT_LEVEL_DIFFERENCE) {
			int combatDif1 = c.getCombat().getCombatDifference(c.combatLevel, PlayerHandler.players[i].combatLevel);
			if(combatDif1 > c.wildLevel || combatDif1 > PlayerHandler.players[i].wildLevel) {
				c.sendMessage("Your combat level difference is too great to attack that player here.");
				return false;
			}
		}
		
		if(Config.SINGLE_AND_MULTI_ZONES) {
			if(!PlayerHandler.players[i].inMulti()) {	// single combat zones
				if(PlayerHandler.players[i].underAttackBy != c.index  && PlayerHandler.players[i].underAttackBy != 0) {
					return false;
				}
				if(PlayerHandler.players[i].index != c.underAttackBy && c.underAttackBy != 0) {
					c.sendMessage("You are already in combat.");
					return false;
				}
			}
		}
		return true;
	}

	public static void appendMultiBarrageNPC(Client c, int npcId, boolean splashed) {
		if (NPCHandler.npcs[npcId] != null) {
			NPC n = (NPC)NPCHandler.npcs[npcId];
			if (n.isDead)
				return;
			if (checkMultiBarrageReqsNPC(npcId)) {
				c.barrageCount++;
				c.multiAttacking = true;
				NPCHandler.npcs[npcId].underAttackBy = c.index;
				NPCHandler.npcs[npcId].underAttack = true;
				if (Misc.random(c.getCombat().mageAtk()) > Misc.random(c.getCombat().mageDef()) && !c.magicFailed) {
					if(c.getCombat().getEndGfxHeight() == 100){ // end GFX
						n.gfx100(MagicData.MAGIC_SPELLS[c.oldSpellId][5]);
					} else {
						n.gfx0(MagicData.MAGIC_SPELLS[c.oldSpellId][5]);
					}
					int damage = Misc.random(c.getCombat().magicMaxHit());
					if (NPCHandler.npcs[npcId].HP - damage < 0) { 
						damage = NPCHandler.npcs[npcId].HP;
					}		
					NPCHandler.npcs[npcId].handleHitMask(damage);
					NPCHandler.npcs[npcId].dealDamage(damage);
					c.totalPlayerDamageDealt += damage;
					c.getCombat().multiSpellEffectNPC(npcId, damage);
					c.totalDamageDealt += damage;
					c.getCombat().multiSpellEffectNPC(npcId, damage);
				} else {
					n.gfx100(85);
				}			
			}		
		}	
	}

	public static void multiSpellEffect(Client c, int playerId, int damage) {					
		switch(MagicData.MAGIC_SPELLS[c.oldSpellId][0]) {
			case 13011:
			case 13023:
			if(System.currentTimeMillis() - PlayerHandler.players[playerId].reduceStat > 35000) {
				PlayerHandler.players[playerId].reduceStat = System.currentTimeMillis();
				PlayerHandler.players[playerId].playerLevel[0] -= ((PlayerHandler.players[playerId].getLevelForXP(PlayerHandler.players[playerId].playerXP[0]) * 10) / 100);
			}	
			break;
			case 12919: // blood spells
			case 12929:
				int heal = (int)(damage / 4);
				if(c.playerLevel[3] + heal >= c.getPA().getLevelForXP(c.playerXP[3])) {
					c.playerLevel[3] = c.getPA().getLevelForXP(c.playerXP[3]);
				} else {
					c.playerLevel[3] += heal;
				}
				c.getPA().refreshSkill(3);
			break;
			case 12891:
			case 12881:
				if (PlayerHandler.players[playerId].freezeTimer < -4) {
					PlayerHandler.players[playerId].freezeTimer = c.getCombat().getFreezeTime();
					PlayerHandler.players[playerId].stopMovement();
				}
			break;
		}	
	}

	public static void appendMultiBarrage(Client c, int playerId, boolean splashed) {
		if (PlayerHandler.players[playerId] != null) {
			Client c2 = (Client)PlayerHandler.players[playerId];
			if (c2.isDead || c2.respawnTimer > 0)
				return;
			if (c.getCombat().checkMultiBarrageReqs(playerId)) {
				c.barrageCount++;
				if (Misc.random(c.getCombat().mageAtk()) > Misc.random(c2.getCombat().mageDef()) && !c.magicFailed) {
					if(c.getCombat().getEndGfxHeight() == 100){ // end GFX
						c2.gfx100(MagicData.MAGIC_SPELLS[c.oldSpellId][5]);
					} else {
						c2.gfx0(MagicData.MAGIC_SPELLS[c.oldSpellId][5]);
					}
					int damage = Misc.random(c.getCombat().magicMaxHit());
					if (c2.prayerActive[12]) {
						damage *= (int)(.60);
					}
					if (c2.playerLevel[3] - damage < 0) {
						damage = c2.playerLevel[3];					
					}
					c.getPA().addSkillXP((MagicData.MAGIC_SPELLS[c.oldSpellId][7] + damage*(c.getRights().isIronman() ? 4 : Config.MAGIC_EXP_RATE)), 6); 
					c.getPA().addSkillXP((MagicData.MAGIC_SPELLS[c.oldSpellId][7] + damage*(c.getRights().isIronman() ? 4 : Config.MAGIC_EXP_RATE /3)), 3);
					c2.appendDamage(damage, damage > 0 ? Hitmark.HIT : Hitmark.MISS);
					c2.addDamageReceived(c.playerName, damage);
					c2.getPA().refreshSkill(3);
					c.totalPlayerDamageDealt += damage;
					c.getCombat().multiSpellEffect(playerId, damage);
				} else {
					c2.gfx100(85);
				}			
			}		
		}	
	}
}