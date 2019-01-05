package Ghreborn.model.players.combat.range;

import Ghreborn.model.npcs.NPC;
import Ghreborn.model.npcs.NPCHandler;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.combat.Hitmark;
import Ghreborn.core .PlayerHandler;
import Ghreborn.util.Misc;

public class RangeExtras {

	public static void appendMutliChinchompa(Client c, int npcId) {
		if (NPCHandler.npcs[npcId] != null) {
			NPC n = (NPC)NPCHandler.npcs[npcId];
			if (n.isDead) {
				return;
			}
			c.multiAttacking = true;
			int damage = Misc.random(c.getCombat().rangeMaxHit());
			if (NPCHandler.npcs[npcId].HP - damage < 0) { 
				damage = NPCHandler.npcs[npcId].HP;
			}
			NPCHandler.npcs[npcId].underAttackBy = c.index;
			NPCHandler.npcs[npcId].underAttack = true;
			NPCHandler.npcs[npcId].handleHitMask(damage);
			NPCHandler.npcs[npcId].dealDamage(damage);
		}
	}

	private static void createCombatGFX(Client c, int i, int gfx, boolean height100) {
		Client p = (Client)PlayerHandler.players[i];
		NPC n = (NPC)NPCHandler.npcs[i];
		if(c.playerIndex > 0) {
			if(height100) {
				p.gfx100(gfx);
			} else {
				p.gfx0(gfx);
			}
		} else if(c.npcIndex > 0) {
			if(height100) {
				n.gfx100(gfx);
			} else {
				n.gfx0(gfx);
			}
		}
	}

	public static boolean boltSpecialAvailable(Client player) {
		int chance = 10;
		switch (player.playerEquipment[player.playerArrows]) {
			case 9244:
				chance = 13;
				break;
		}
		if(player.playerEquipment[player.playerWeapon] == 11785
				&& player.usingSpecial && player.specAmount >= 4) {
			chance = 3;
		}
		return Misc.random(chance) == 0;
	}
	
	public static boolean wearingCrossbow(Client player) {
		return player.playerEquipment[player.playerWeapon] == 11785 
				|| player.playerEquipment[player.playerWeapon] == 9185 || player.playerEquipment[player.playerWeapon] == 19481;
	}
	
	public static boolean wearingBolt(Client player) {
		return player.playerEquipment[player.playerArrows] >= 9236
				&& player.playerEquipment[player.playerArrows] <= 9244;
	}
	public static void crossbowSpecial(Client c, int i) {
		Client p = (Client)PlayerHandler.players[i];
		NPC n = (NPC)NPCHandler.npcs[i];

		c.crossbowDamage = 1.4;

		switch (c.lastArrowUsed) {
			case 9236: // Lucky Lightning
				createCombatGFX(c, i, 749, false);
				c.crossbowDamage = 1.25;
				break;
			case 9237: // Earth's Fury
				createCombatGFX(c, i, 755, false);
				break;
			case 9238: // Sea Curse
				createCombatGFX(c, i, 750, false);
				c.crossbowDamage = 1.10;
				break;
			case 9239: // Down to Earth
				createCombatGFX(c, i, 757, false);
 				if(c.playerIndex > 0) {
					p.playerLevel[6] -= 2;
					p.getPA().refreshSkill(6);
					p.sendMessage("Your magic has been lowered!");
				}
				break;
			case 9240: // Clear Mind
				createCombatGFX(c, i, 751, false);
				if(c.playerIndex > 0) {
					p.playerLevel[5] -= 2;
					p.getPA().refreshSkill(5);
					p.sendMessage("Your prayer has been lowered!");
					c.playerLevel[5] += 2;
					if(c.playerLevel[5] >= c.getPA().getLevelForXP(c.playerXP[5])) {
						c.playerLevel[5] = c.getPA().getLevelForXP(c.playerXP[5]);
					}
					c.getPA().refreshSkill(5);
				}
				break;
			case 9241: // Magical Posion
				createCombatGFX(c, i, 752, false);
				if(c.playerIndex > 0) {
					p.getPA().appendPoison(6);
				}
				break;
			case 9242: // Blood Forfiet
				createCombatGFX(c, i, 754, false);

				if(c.playerLevel[3] - c.playerLevel[3]/20 < 1) {
					break;
				}
				int damage = c.playerLevel[3]/20;
				c.appendDamage(damage, damage > 0 ? Hitmark.HIT : Hitmark.MISS);
				if(c.npcIndex > 0) {
					n.handleHitMask(n.HP/10);
					n.dealDamage(n.HP/10);
				}
					p.addDamageReceived(c.playerName, damage);
				break;
			case 9243: // Armour Piercing
				createCombatGFX(c, i, 758, true);
				c.crossbowDamage = 1.15;
				c.ignoreDefence = true;
				break;
			case 9244: // Dragon's Breath
				createCombatGFX(c, i, 756, false);
				if(c.playerEquipment[c.playerShield] != 1540 || c.playerEquipment[c.playerShield] != 11283
					|| c.playerEquipment[c.playerShield] != 11284) {
						c.crossbowDamage = 1.48;
				}
				break;
			case 9245: // Life Leech
				createCombatGFX(c, i, 753, false);
				c.crossbowDamage = 1.15;
				c.playerLevel[3] += c.boltDamage/25;
				if(c.playerLevel[3] >= 112) {
					c.playerLevel[3] = 112;
				}
				c.getPA().refreshSkill(3);
				break;
		}
	}
}