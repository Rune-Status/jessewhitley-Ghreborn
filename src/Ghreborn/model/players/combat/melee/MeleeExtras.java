package Ghreborn.model.players.combat.melee;

import Ghreborn.event.CycleEvent;
import Ghreborn.event.CycleEventContainer;
import Ghreborn.event.CycleEventHandler;
import Ghreborn.model.npcs.NPCHandler;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.Player;
import Ghreborn.model.players.combat.Hitmark;
import Ghreborn.core .PlayerHandler;
import Ghreborn.util.Misc;

public class MeleeExtras {

	public static void applySmite(Client c, int index, int damage) {
		if (!c.prayerActive[23])
			return;
		if (damage <= 0)
			return;
		if (PlayerHandler.players[index] != null) { 
			Client c2 = (Client)PlayerHandler.players[index];
			c2.playerLevel[5] -= (int)(damage/4);
			if (c2.playerLevel[5] <= 0) {
				c2.playerLevel[5] = 0;
				c2.getCombat().resetPrayers();
			}
			c2.getPA().refreshSkill(5);
		}
	}

	public static void handleDragonFireShield(final Client c) {
		if(PlayerHandler.players[c.playerIndex].playerLevel[3] <= 0) {
			return;
		}
		if (c.playerIndex > 0 && PlayerHandler.players[c.playerIndex] != null) {
			if(c.dfsCount < 40) {
				c.sendMessage("My shield hasn't finished charging.");
				return;
			}
			final int pX = c.getX();
			final int pY = c.getY();
			final int oX = PlayerHandler.players[c.playerIndex].getX();
			final int oY = PlayerHandler.players[c.playerIndex].getY();
			final int offX = (pY - oY)* -1;
			final int offY = (pX - oX)* -1;
			final int damage = Misc.random(25) + 5;
			c.dfsCount = 0;
			c.animation(6696);
			c.gfx0(1165);
			c.attackTimer += 3;
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 10, 1166, 25, 27, c.playerIndex - 1, 0);
					container.stop();
				}
				@Override
				public void stop() {

				}
			}, 3); 
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					PlayerHandler.players[c.playerIndex].gfx100(1167);
					PlayerHandler.players[c.playerIndex].playerLevel[3] -= damage;
					if (!c.getHitUpdateRequired()) {
						c.setHitDiff(damage);
						c.setHitUpdateRequired(true);
					} else if (!c.getHitUpdateRequired2()) {
						c.setHitDiff2(damage);
						c.setHitUpdateRequired2(true);
					}
					PlayerHandler.players[c.playerIndex].updateRequired = true;						
					container.stop();
				}
				@Override
				public void stop() {

				}
			}, 3);
		}				
	}

	public static void handleDragonFireShieldNPC(final Client c) {
		if(NPCHandler.npcs[c.npcIndex].HP <= 0) {
			return;
		}
		if (c.npcIndex > 0 && NPCHandler.npcs[c.npcIndex] != null) {
			if(c.dfsCount < 40) {
				c.sendMessage("My shield hasn't finished charging.");
				return;
			}
			if(NPCHandler.npcs[c.npcIndex].HP <= 1) {
				return;
			}
			final int pX = c.getX();
			final int pY = c.getY();
 			final int nX = NPCHandler.npcs[c.npcIndex].getX();
			final int nY = NPCHandler.npcs[c.npcIndex].getY();
			final int offX = (pY - nY)* -1;
			final int offY = (pX - nX)* -1;
			final int damage = Misc.random(25) + 5;
			c.dfsCount = 0;
			c.animation(6696);
			c.gfx0(1165);
			c.attackTimer += 3;
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 10, 1166, 25, 27, c.npcIndex + 1, 0);
					container.stop();
				}
				@Override
				public void stop() {

				}
			}, 3);
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					NPCHandler.npcs[c.npcIndex].gfx100(1167);
					NPCHandler.npcs[c.npcIndex].handleHitMask(damage);
					NPCHandler.npcs[c.npcIndex].HP -= damage;						
					container.stop();
				}
				@Override
				public void stop() {

				}
			}, 3);
		}
	}

	public static void addCharge(Client c) {
		if(c.playerEquipment[c.playerShield] != 11283) {
			return;
		}
		c.dfsCount++;
		if(c.dfsCount >= 40) {
			c.dfsCount = 40;
		}
		if(c.dfsCount == 39) {
			c.sendMessage("Your dragon fireshield has finished charging!");
		}
	}

	public static void appendVengeanceNPC(Client c, int otherPlayer, int damage) {
		if (damage <= 0)
			return;
		if (c.npcIndex > 0 && NPCHandler.npcs[c.npcIndex] != null) {
			c.forcedText = "Taste vengeance!";
			c.forcedChatUpdateRequired = true;
			c.updateRequired = true;
			c.vengOn = false;
			if ((NPCHandler.npcs[c.npcIndex].HP - damage) > 0) {
				damage = (int)(damage * 0.75);
				if (damage > NPCHandler.npcs[c.npcIndex].HP) {
					damage = NPCHandler.npcs[c.npcIndex].HP;
				}
				NPCHandler.npcs[c.npcIndex].HP -= damage;
				NPCHandler.npcs[c.npcIndex].handleHitMask(damage);
			}
		}	
		c.updateRequired = true;
	}

	public static void appendVengeance(Client c, int otherPlayer, int damage) {
		if (damage <= 0)
			return;
		Player o = PlayerHandler.players[otherPlayer];
		o.forcedText = "Taste vengeance!";
		o.forcedChatUpdateRequired = true;
		o.updateRequired = true;
		o.vengOn = false;
		if ((o.playerLevel[3] - damage) > 0) {
			damage = (int)(damage * 0.75);
			if (damage > c.playerLevel[3]) {
				damage = c.playerLevel[3];
			}
			if (!c.getHitUpdateRequired()) {
				c.setHitDiff(damage);
				c.setHitUpdateRequired(true);
			} else if (!c.getHitUpdateRequired2()) {
				c.setHitDiff2(damage);
				c.setHitUpdateRequired2(true);
			}
			c.playerLevel[3] -= damage;
			c.getPA().refreshSkill(3);
		}	
		c.updateRequired = true;
	}

	public static void applyRecoilNPC(Client c, int damage, int i) {
		if (damage > 0 && c.playerEquipment[c.playerRing] == 2550) {
			int recDamage = damage/10 + 1;
			NPCHandler.npcs[c.npcIndex].HP -= recDamage;
			NPCHandler.npcs[c.npcIndex].handleHitMask(recDamage);
			removeRecoil(c);
			c.recoilHits += damage;
		}
	}

	public static void applyRecoil(Client c, int damage, int i) {
		if (damage > 0 && PlayerHandler.players[i].playerEquipment[c.playerRing] == 2550) {
			int recDamage = damage/10 + 1;
			if (!c.getHitUpdateRequired()) {
				c.setHitDiff(recDamage);
				c.setHitUpdateRequired(true);				
			} else if (!c.getHitUpdateRequired2()) {
				c.setHitDiff2(recDamage);
				c.setHitUpdateRequired2(true);
			}
			c.appendDamage(recDamage, recDamage > 0 ? Hitmark.HIT : Hitmark.MISS);
			c.updateRequired = true;
			removeRecoil(c);
			c.recoilHits += damage;
		}	
	}

	public static void removeRecoil(Client c) {
		if(c.recoilHits >= 400) {
			c.getItems().removeItem(2550, c.playerRing);
			c.getItems().deleteItem(2550, c.getItems().getItemSlot(2550), 1);
			c.sendMessage("Your ring of recoil shaters!");
			c.recoilHits = 0;
		} else {
			c.recoilHits++;
		}
	}

	public static void graniteMaulSpecial(Client c) {
		if (c.playerIndex > 0) {
			Client o = (Client)PlayerHandler.players[c.playerIndex];
			if (c.goodDistance(c.getX(), c.getY(), o.getX(), o.getY(), c.getCombat().getRequiredDistance())) {
 				if (c.getCombat().checkReqs()) {
					if (c.getCombat().checkSpecAmount(4153)) {						
 						boolean hit = Misc.random(c.getCombat().calculateMeleeAttack()) > Misc.random(o.getCombat().calculateMeleeDefence());
						int damage = 0;
						if (hit)
							damage = Misc.random(c.getCombat().calculateMeleeMaxHit());
						if (o.prayerActive[18] && System.currentTimeMillis() - o.protMeleeDelay > 1500)
							damage *= .6;
						if(o.playerLevel[3] - damage <= 0) {
							damage = o.playerLevel[3];
						}
						if(o.playerLevel[3] > 0) {
							o.handleHitMask(damage);
							c.animation(1667);
							o.gfx100(337);
							o.appendDamage(damage, damage > 0 ? Hitmark.HIT : Hitmark.MISS);
						}
					}	
				}	
			}			
		} else if(c.npcIndex > 0) {
			int x = NPCHandler.npcs[c.npcIndex].absX;
			int y = NPCHandler.npcs[c.npcIndex].absY;
			if (c.goodDistance(c.getX(), c.getY(), x, y, 2)) {
				if (c.getCombat().checkReqs()) {
					if (c.getCombat().checkSpecAmount(4153)) {
						int damage = Misc.random(c.getCombat().calculateMeleeMaxHit());
						if(NPCHandler.npcs[c.npcIndex].HP - damage < 0) {
							damage = NPCHandler.npcs[c.npcIndex].HP;
						}
						if(NPCHandler.npcs[c.npcIndex].HP > 0) {
							NPCHandler.npcs[c.npcIndex].HP -= damage;
							NPCHandler.npcs[c.npcIndex].handleHitMask(damage);
							c.animation(1667);
							c.gfx100(337);
						}
					}
				}
			}
		}
	}

	public static void applyDharokRecoilNPC(Player c, int damage, int i) {
		int chance = Misc.random(20);
		if (chance == 1 && damage > 0 && c.getPA().wearingDharok(c)) {
			int recDamage = damage *= 0.25;
			if (recDamage < 1) {
				recDamage = 1;
			}
			if (NPCHandler.npcs[i].HP <= 0 || NPCHandler.npcs[i].isDead) {
				return;
			}
			NPCHandler.npcs[i].HP -= recDamage;
			NPCHandler.npcs[i].handleHitMask(recDamage);
			c.sendMessage("<col=0000FF>You feel your amulet start to empower and unleash damage on your target.");
			c.recoilHits += damage;
		}
	}
	
	public static void applyDharokRecoilPlayer(Client c, int damage, int i) {
		int chance = Misc.random(1);
		/*if (Boundary.isIn(c, Boundary.DUEL_ARENAS)) {
			DuelSession session = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(
					c, MultiplayerSessionType.DUEL);
			if (Objects.nonNull(session)) {
				if (session.getRules().contains(Rule.NO_RINGS)) {
					return;
				}
			}
		}*/
		if (chance == 1 && damage > 0 && c.getPA().wearingDharok(c)) {
			int recDamage = damage *= 0.25;
			c.appendDamage(recDamage, recDamage > 0 ? Hitmark.HIT : Hitmark.MISS);
			c.addDamageReceived(PlayerHandler.players[i].playerName, recDamage);
			c.updateRequired = true;
			c.sendMessage("<col=0000FF>You feel your amulet start to empower and unleash damage on your target.");
			c.recoilHits += damage;
		}	
	}
}