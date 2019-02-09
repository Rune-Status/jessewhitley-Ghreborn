package Ghreborn.model.npcs;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;


import Ghreborn.Config;
import Ghreborn.Server;
import Ghreborn.clip.Region;
import Ghreborn.model.content.DailyTaskKills;
import Ghreborn.model.content.barrows.Barrows;
import Ghreborn.model.content.barrows.brothers.Brother;
import Ghreborn.model.content.godwars.*;
import Ghreborn.model.item.Antifire;
import Ghreborn.model.items.Item;
import Ghreborn.model.items.Item2;
import Ghreborn.model.items.ItemAssistant;
import Ghreborn.model.minigames.fight_cave.Wave;
import Ghreborn.model.minigames.inferno.InfernoWave;
import Ghreborn.model.minigames.rfd.DisposeTypes;
import Ghreborn.model.minigames.warriors_guild.AnimatedArmour;
import Ghreborn.model.npcs.animations.AttackAnimation;
import Ghreborn.model.npcs.animations.DeathAnimation;
import Ghreborn.model.npcs.boss.Callisto.Callisto;
import Ghreborn.model.npcs.boss.Cerberus.Cerberus;
import Ghreborn.model.npcs.boss.Gorillas.DemonicGorilla;
import Ghreborn.model.npcs.boss.Kraken.impl.SpawnEntity;
import Ghreborn.model.npcs.boss.Vetion.Vetion;
import Ghreborn.model.npcs.boss.abyssalsire.AbyssalSireConstants;
import Ghreborn.model.npcs.boss.raids.Tekton;
import Ghreborn.model.npcs.boss.skotizo.Skotizo;
import Ghreborn.model.npcs.boss.vorkath.Vorkath;
import Ghreborn.model.npcs.boss.vorkath.VorkathConstants;
import Ghreborn.model.npcs.boss.zulrah.Zulrah;
import Ghreborn.model.npcs.drop.NpcDropSystem;
import Ghreborn.model.npcs.drops.NpcDropManager;
import Ghreborn.model.players.Boundary;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.Player;
import Ghreborn.model.players.combat.Damage;
import Ghreborn.model.players.combat.DamageEffect;
import Ghreborn.model.players.combat.Hitmark;
import Ghreborn.model.players.skills.hunter.impling.PuroPuro;
import Ghreborn.util.Location3D;
import Ghreborn.util.Misc;
import Ghreborn.world.ItemHandler;
import Ghreborn.world.objects.GlobalObject;
import Ghreborn.core.PlayerHandler;
import Ghreborn.definitions.ItemCacheDefinition;
import Ghreborn.definitions.NPCCacheDefinition;
import Ghreborn.event.EventManager;
import Ghreborn.event.CycleEvent;
import Ghreborn.event.CycleEventContainer;
import Ghreborn.event.CycleEventHandler;
import Ghreborn.event.Event;
import Ghreborn.event.EventContainer;

public class NPCHandler {
	public static int maxNPCs = 30000;
	public static int maxListedNPCs = 30000;
	public static int maxNPCDrops = 30000;
	public static NPC npcs[] = new NPC[maxNPCs];
	public static int ScorpX;
	public static int ScorpY;
	

	public static void loadDefs() {

			loadAutoSpawn("./Data/cfg/spawn-config.cfg");
			startGame();
	}
	public static void startGame() {
		for (int i = 0; i < PuroPuro.IMPLINGS.length; i++) {
			newNPC(PuroPuro.IMPLINGS[i][0], PuroPuro.IMPLINGS[i][1], PuroPuro.IMPLINGS[i][2], 0, 1, -1, -1, -1, -1);
		}
		
		/**
		 * Random spawns
		 */
		int random_spawn = Misc.random(2);
		int x = 0;
		int y = 0;
		switch(random_spawn) {
		case 0:
			x = 2620;
			y = 4347;
			break;
			
		case 1:
			x = 2607;
			y = 4321;
			break;
			
		case 2:
			x = 2589;
			y = 4292;
			break;
		}
		newNPC(7302, x, y, 0, 1, -1, -1, -1, -1);
	}
	/**
	 * Tekton variables
	 */
	public static String tektonAttack = "MELEE";
	public static Boolean tektonWalking = false;
	NPC TEKTON = NPCHandler.getNpc(7544);

	/**
	 * Ice demon variables
	 */
	NPC ICE_DEMON = NPCHandler.getNpc(7584);

	/**
	 * Skeletal mystics
	 */
	NPC SKELE_MYSTIC_ONE = NPCHandler.getNpc(7604);
	NPC SKELE_MYSTIC_TWO = NPCHandler.getNpc(7605);
	NPC SKELE_MYSTIC_THREE = NPCHandler.getNpc(7606);


	public void multiAttackGfx(int i, int gfx) {
		if (npcs[i].projectileId < 0)
			return;
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Client c = (Client) PlayerHandler.players[j];
				if (c.heightLevel != npcs[i].heightLevel)
					continue;
				if (PlayerHandler.players[j].goodDistance(c.absX,
						c.absY, npcs[i].absX, npcs[i].absY, 15)) {
					int nX = NPCHandler.npcs[i].getX() + offset(i);
					int nY = NPCHandler.npcs[i].getY() + offset(i);
					int pX = c.getX();
					int pY = c.getY();
					int offX = (nY - pY) * -1;
					int offY = (nX - pX) * -1;
					c.getPA().createPlayersProjectile(nX, nY, offX, offY, 50,
							getProjectileSpeed(i), npcs[i].projectileId, 43,
							31, -c.getIndex() - 1, 65);
					if (npcs[i].npcType == 7554) {
						c.getPA().sendPlayerObjectAnimation(c, 3220, 5738, 7371, 10, 3, c.getHeight());
					}
				}
			}
		}
	}
	public void startWinterSpawnTimer() {
		//System.out.println("Winter Demon event started");
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Client c = (Client) PlayerHandler.players[j];
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
			int timer = 3600;//3600;
			@Override
			public void execute(CycleEventContainer container) {
				if (timer == 1) {
					int spawnLocation [][] = {{3266, 3419},{3235, 3225},{3086, 3421},{2965, 3386},{3080, 3250}, {3295, 3218}}; //Coordinates (Can Add More)
					String locationName[] = {"East Varrock", "Lumbridge", "Barbarian Village", "Falador", "Draynor Village", "Al Kharid", "Lumbridge", "Al Kharid"}; //Location (Add this when you add coords)
					int where = Misc.random(spawnLocation.length-1); //Get's a random location.
					spawnNpc(8508, spawnLocation[where][0], spawnLocation[where][1], 0, 0, 10000, 50, 0, 0);
					c.getPA().messageall("<col=ffff00><shad=ff0000>A Winter Demon has just spawned in " + locationName[where] +"!");
	                                	c.getPA().messageall("<col=ffff00><shad=ff0000>Hunt him down and kill him for an epic prize!");
				}					
				if (timer > 0) {
					timer--;
					
				}
			}
			@Override
			public void stop() {

			}
		}, 1000);
			}
		}
	}
	
	public static String getNpcName(int npcId) {
		if (npcId <= -1) {
			return "None";
		}
		if (NpcDefinition.DEFINITIONS[npcId] == null) {
			return "None";
		}
		return NpcDefinition.DEFINITIONS[npcId].getName();
	}

	public NPC[] getNPCs() {
		return npcs;
	}
	public static void KILL_POOLS(Player player, int npcType, int absX, int absY, int height) {
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				Arrays.asList(npcs).stream().filter(Objects::nonNull).filter(
						n -> n.npcType == npcType && n.absX == absX && n.absY == absY && n.heightLevel == height)
						.forEach(npc -> npc.isDead = true);
				container.stop();
			}
		}, 1);
	}

	public static void KILL_TENT(Client player, int npcType, int absX, int absY, int height) {
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				Arrays.asList(npcs).stream().filter(Objects::nonNull).filter(
						n -> n.npcType == npcType && n.absX == absX && n.absY == absY && n.heightLevel == height)
						.forEach(npc -> npc.isDead = true);
				container.stop();
			}
		}, 1);
	}
	
	@SuppressWarnings("null")
	public NPC spawnNpc(final Player c, int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit,
			int attack, int defence, boolean attackPlayer, boolean headIcon) {
		// first, search for a free slot
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}
		if (slot == -1) {
			// Misc.println("No Free Slot");
			return null; // no free slot found
		}
		final NPC newNPC = new NPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.face(c);
		newNPC.HP = HP;
		newNPC.maximumHealth = HP;
		newNPC.maxHit = maxHit;
		newNPC.attack = attack;
		newNPC.defence = defence;
		newNPC.spawnedBy = c.getId();
		if (headIcon)
			c.getPA().drawHeadicon(1, slot, 0, 0);
		if (attackPlayer) {
			newNPC.underAttack = true;
			if (c != null) {
				newNPC.killerId = c.index;
			}
		}
		npcs[slot] = newNPC;
		return newNPC;
	}
	public boolean switchesAttackers(int i) {
		switch (npcs[i].npcType) {
		case 2205:
		case 963:
		case 965:
		case 7554:
		case 3129:
		case 2215:
		case 3162:
		case 7792: //Long-tailed Wyvern
		case 7793: //Taloned Wyvern
		case 7794: //Spitting Wyvern
		case 7795: //Ancient Wyvern
		case 2208:
		case 239:
		case 6611:
		case 6612:
		case 494:
		case 319:
		case 320:
		case 5535:
		case 2551:
		case 6609:
		case 2552:
		case 5862:
		case 2553:
		case 2559:
		case 2560:
		case 2561:
		case 2563:
		case 2564:
		case 2565:
		case 2892:
		case 2894:
		case 1046:
		case 6615:
		case 6616:
		case 7604:
		case 7605:
		case 7606:
		case 7544:
		case 5129:
		case 4922:
			return true;

		}

		return false;
	}
	private int multiAttackDistance(NPC npc) {
		if (npc == null) {
			return 0;
		}
		switch (npc.npcType) {
		case 239:
			return 35;
		}
		return 15;
	}
	public void multiAttackDamage(int i) {
		int damage = Misc.random(getMaxHit(i));
		// int damage = getMaxHit(i);
		// int max = getMaxHit(i);
		Hitmark hitmark = damage > 0 ? Hitmark.HIT : Hitmark.MISS;
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Client c = (Client) PlayerHandler.players[j];
				if (c.isDead || c.heightLevel != npcs[i].heightLevel)
					continue;
				if (PlayerHandler.players[j].goodDistance(c.absX, c.absY, npcs[i].absX, npcs[i].absY,
						multiAttackDistance(npcs[i]))) {
					if (npcs[i].attackType == 4) {
						if (npcs[i].npcType == 6611 || npcs[i].npcType == 6612) {
							if (!(c.absX > npcs[i].absX - 5 && c.absX < npcs[i].absX + 5 && c.absY > npcs[i].absY - 5
									&& c.absY < npcs[i].absY + 5)) {
								continue;
							}
							c.sendMessage(
									"Vet'ion pummels the ground sending a shattering earthquake shockwave through you.");
							Vetion.createVetionEarthquake(c);
						}
						c.appendDamage(damage, hitmark);
					} else if (npcs[i].attackType == 3) {
						int resistance = c.getItems().isWearingItem(1540) || c.getItems().isWearingItem(11283)
								|| c.getItems().isWearingItem(11284) ? 1 : 0;
						if (System.currentTimeMillis() - c.lastAntifirePotion < c.antifireDelay) {
							resistance++;
						}
						c.sendMessage("Resistance: " + resistance);
						if (resistance == 0) {
							damage = Misc.random(getMaxHit(i));
							if (npcs[i].npcType == 465) {
								c.sendMessage("You are badly burnt by the cold breeze!");
							} else {
								c.sendMessage("You are badly burnt by the dragon fire!");
							}
							} else if (resistance == 1)
							damage = Misc.random(15);
						else if (resistance == 2)
							damage = 0;
						if (c.playerLevel[3] - damage < 0)
							damage = c.playerLevel[3];
						c.gfx100(npcs[i].endGfx);
						c.appendDamage(damage, hitmark);
					} else if (npcs[i].attackType == 2) {
						if (npcs[i].npcType == 6611 || npcs[i].npcType == 6612) {
							if (Vetion.vetionSpellCoordinates.stream()
									.noneMatch(p -> p[0] == c.absX && p[1] == c.absY)) {
								continue;
							}
						}
						if (npcs[i].npcType == 319) {
							if (coreCoordinates.stream().noneMatch(p -> p[0] == c.absX && p[1] == c.absY)) {
								continue;
							}
						}

						/*if (npcs[i].npcType == 6619) {
							if (Fanatic.EARTH.stream().noneMatch(p -> p[0] == c.absX && p[1] == c.absY)) {
								continue;
							}
						}

						if (npcs[i].npcType == 6618) {
							if (Archaeologist.SPELL_COORDINATES.stream()
									.noneMatch(p -> p[0] == c.absX && p[1] == c.absY)) {
								if (Archaeologist.NEXT_SPELL.stream()
										.noneMatch(p -> p[0] == c.absX && p[1] == c.absY)) {
									continue;
								}
							}
						}*/

						if (!c.prayerActive[16]) {
							if (Misc.random(500) + 200 > Misc.random(c.getCombat().mageDef())) {
								c.appendDamage(damage, damage > 0 ? Hitmark.HIT : Hitmark.MISS);
							} else {
								c.appendDamage(0, Hitmark.MISS);
							}

						} else {
							if (npcs[i].npcType == 6610) {
								damage *= .7;
								c.appendDamage(damage, damage > 0 ? Hitmark.HIT : Hitmark.MISS);
							} else if (npcs[i].npcType == 6528) {
								damage *= .5;
								c.appendDamage(damage, damage > 0 ? Hitmark.HIT : Hitmark.MISS);
							} else {
								c.appendDamage(0, Hitmark.MISS);
							}
						}
					} else if (npcs[i].attackType == 1) {
						if (!c.prayerActive[17]) {
							if (Misc.random(500) + 200 > Misc.random(c.getCombat().calculateRangeDefence())) {
								c.appendDamage(damage, damage > 0 ? Hitmark.HIT : Hitmark.MISS);
							} else {
								c.appendDamage(0, Hitmark.MISS);
							}
						} else {
							c.appendDamage(0, Hitmark.MISS);
						}
					}
					if (npcs[i].endGfx > 0) {
						c.gfx0(npcs[i].endGfx);
					}
				}
				c.getPA().refreshSkill(3);
			}
		}
	}

	public int getClosePlayer(int i) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				if (j == npcs[i].spawnedBy)
					return j;
				if (goodDistance(PlayerHandler.players[j].absX, PlayerHandler.players[j].absY, npcs[i].npcSize,
						npcs[i].absX, npcs[i].absY, extraDistance(i) + distanceRequired(i) + followDistance(i))
						|| isFightCaveNpc(i)) {
					if ((PlayerHandler.players[j].underAttackBy <= 0 && PlayerHandler.players[j].underAttackBy2 <= 0)
							|| PlayerHandler.players[j].inMulti())
						if (PlayerHandler.players[j].heightLevel == npcs[i].heightLevel)
							return j;
				}
			}
		}
		return 0;
	}

	public static boolean isSpawnedBy(Player player, NPC npc) {
		if (player != null && npc != null)
			if (npc.spawnedBy == player.index || npc.killerId == player.index)
				return true;
		return false;
	}

	public boolean goodDistance(int npcX, int npcY, int npcSize, int playerX, int playerY, int distance) {
		return playerX >= (npcX - distance) && playerX <= (npcX + npcSize + distance) && playerY >= (npcY - distance)
				&& playerY <= (npcY + npcSize + distance);
	}
	public static boolean goodDistance(int objectX, int objectY, int playerX, int playerY, int distance) {
		return Math.sqrt(Math.pow(objectX - playerX, 2) + Math.pow(objectY - playerY, 2)) <= distance;
	}
	public int getCloseRandomPlayer(int i) {
		ArrayList<Integer> players = new ArrayList<>();
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				if (Boundary.isIn(npcs[i], Boundary.GODWARS_BOSSROOMS)) {
					if (!Boundary.isIn(PlayerHandler.players[j], Boundary.GODWARS_BOSSROOMS)) {
						npcs[i].killerId = 0;
						continue;
					}
				}
				if (goodDistance(PlayerHandler.players[j].absX, PlayerHandler.players[j].absY, npcs[i].absX,
						npcs[i].absY, distanceRequired(i) + followDistance(i)) || isFightCaveNpc(i)) {
					if ((PlayerHandler.players[j].underAttackBy <= 0 && PlayerHandler.players[j].underAttackBy2 <= 0)
							|| PlayerHandler.players[j].inMulti())
						if (PlayerHandler.players[j].heightLevel == npcs[i].heightLevel)
							players.add(j);

				}
			}
		}
		if (players.size() > 0)
			return players.get(Misc.random(players.size() - 1));
		return 0;
	}

	public int npcSize(int i) {
		switch (npcs[i].npcType) {
		case 2883:
		case 2882:
		case 2881:
			return 3;
		}
		return 0;
	}
	public boolean isAggressive(int i, boolean searching) {
		if (Boundary.isIn(npcs[i], Boundary.GODWARS_BOSSROOMS)) {
			return true;
		}
		if (Boundary.isIn(npcs[i], Zulrah.BOUNDARY)) {
			return true;
		}
		NpcDefinition def = npcs[i].definition();
		if (def != null) {
			return def.isAggressive();
		}
		switch (npcs[i].npcType) {
		case VorkathConstants.AWAKENED_VORKATH_ID:
			return true;
		case 5535:
		case 5867:
		case 8349:
		case 5868:
		case 465:
		case 5869:
		case 5363:
		case 6609:
		case 6342:
		case 6618:
		case 6619:
		case 6611:
		case 2054:
		case 8031:
		case 8091:
		case 8090:
		case 8030:
		case 6615:
		case 2550:
		case 2551:
		case 319:
		case 320:
		case 2562:
		case 2563:
		case 3129:
		case 3132:
		case 3130:
		case 3131:
		case 2205:
		case 2208:
		case 2207:
		case 2206:
		case 6829:
		case 2215:
		case 2218:
		case 2217:
		case 2216:
		case 3163:
		case 3164:
		case 3165:
		case 3162:
		case 494:
		case 498:
		case 3943:
		case 6610:
		case 7792: //Long-tailed Wyvern
		case 7793: //Taloned Wyvern
		case 7794: //Spitting Wyvern
		case 7795: //Ancient Wyvern
		case Skotizo.SKOTIZO_ID:
		case Skotizo.REANIMATED_DEMON:
		case Skotizo.DARK_ANKOU:
		case Wave.TZ_KEK_SPAWN:
		case Wave.TZ_KIH:
		case Wave.TZ_KEK:
		case Wave.TOK_XIL:
		case Wave.YT_MEJKOT:
		case Wave.KET_ZEK:
		case Wave.TZTOK_JAD:
			return true;
			// GWD
		case 6230:
		case 6231:
		case 6229:
		case 6232:
		case 6240:
		case 6241:
		case 6242:
		case 6233:
		case 6234:
		case 6243:
		case 6244:
		case 6245:
		case 6246:
		case 6238:
		case 6239:
		case 6625:
		case 122:// Npcs That Give BandosKC
		case 6278:
		case 6277:
		case 6276:
		case 6283:
		case 6282:
		case 6281:
		case 6280:
		case 6279:
		case 6271:
		case 6272:
		case 6273:
		case 6274:
		case 6269:
		case 6270:
		case 6268:
		case 6221:
		case 6219:
		case 6220:
		case 6217:
		case 6216:
		case 6215:
		case 6214:
		case 6213:
		case 6212:
		case 6211:
		case 6218:
		case 6275:
		case 6257:// Npcs That Give SaraKC
		case 6255:
		case 6256:
		case 6259:
		case 6254:
		case 1689:
		case 1694:
		case 1699:
		case 1704:
		case 1709:
		case 1714:
		case 1724:
		case 1734:
		case 6914: // Lizardman, Lizardman brute
		case 6915:
		case 6916:
		case 6917:
		case 6918:
		case 6919:
		case 6766:
		case 7573:
		case 7617: // Tekton magers
		case 7544: // Tekton
		case 7604: // Skeletal mystic
		case 7605: // Skeletal mystic
		case 7606: // Skeletal mystic
		case 7585: //
			case 7554:
		case 7563: // muttadiles
		case 5129:
		case 4922:

			return true;
		}
		if (npcs[i].inWild() && npcs[i].maximumHealth > 0)
			return true;
		return false;
		// return npcs[i].definition().isAggressive();
	}

	public static boolean isFightCaveNpc(int i) {
		if (npcs[i] == null)
			return false;
		switch (npcs[i].npcType) {
		case Wave.TZ_KEK_SPAWN:
		case Wave.TZ_KIH:
		case Wave.TZ_KEK:
		case Wave.TOK_XIL:
		case Wave.YT_MEJKOT:
		case Wave.KET_ZEK:
		case Wave.TZTOK_JAD:
			return true;
		}
		return false;
	}

	/**
	 * Summon npc, barrows, etc
	 * @return 
	 **/
    public static boolean IsDropping = false;
    public void MonsterDropItem(int NPCID) { {
            if (IsDropping == false) {
                IsDropping = true;
                int Play = npcs[NPCID].killerId;
                int Maxi = 100000;
				int p = npcs[NPCID].killerId;
				if (PlayerHandler.players[p] != null){
					Client c = (Client) PlayerHandler.players[p];
                for (int i = 0; i <= Maxi; i++) {
                    if (Server.itemHandler.DroppedItemsID[i] > 0) {} else {
                        int NPCID2 = NPCID + 34;

                        System.out.println("Npc id =" + npcs[NPCID].npcType);

                            if (npcs[NPCID].npcType == 113) {
                				Server.itemHandler.createGroundItem(c, Item2.randomJogre(), npcs[NPCID].absX,
                						npcs[NPCID].absY,c.heightLevel,  1, c.index);
                            }
                            if(npcs[NPCID].npcType == 193) //Druid
                            {
                            c.Druidkills += 1; 
                            }
                            if(npcs[NPCID].npcType == 104) //Ghost
                            {
                            c.Ghostkills += 1; 
                            }
                            if(npcs[NPCID].npcType == 111) 
                            {
                            c.Giantkills += 1; 
                            }
                            if(npcs[NPCID].npcType == 4694) //Lesser Demon
                            {
                            c.Demonkills += 1;
                            c.sendMessage("Good! Now kill the General!");
                            c.getPA().movePlayer(3246, 3244, 0);
                            }
                            if(npcs[NPCID].npcType == 258) //General Khazard
                            {
                            c.Generalkills += 1;
                            c.sendMessage("Wow, you have made it this far! Kill Him to beat the Mini game!");
                            c.getPA().movePlayer(3202, 3266, 0);
                            }
                            if(npcs[NPCID].npcType == 1472) //Jungle demon
                            {
                            c.sendMessage("You finished the Mini game! Click on the Chest to claim your reward!");
                            c.getPA().movePlayer(3207, 3222, 0);
                            c.JDemonkills += 1;
                            }
                            if (i == Maxi) {
                                if (Server.itemHandler.DropItemCount
                                        >= (Server.itemHandler.MaxDropItems + 1)) {
                                    Server.itemHandler.DropItemCount = 0;
                                    //println("! Notify item resterting !");
                                }
                            }
                            break;
                        }
                    }
                    IsDropping = false;
                }
            }
        }
    }
	public int getNpcKillerId(int npcId) {
		int oldDamage = 0;
		int killerId = 0;
		int count = 0;
		for (int p = 1; p < Config.MAX_PLAYERS; p++) {
			if (PlayerHandler.players[p] != null) {
				if (PlayerHandler.players[p].lastNpcAttacked == npcId) {
					if (PlayerHandler.players[p].totalDamageDealt > oldDamage
							|| (killerId != 0 && PlayerHandler.players[killerId].ironman)) {
						if (count > 0 && PlayerHandler.players[p].ironman)
							continue;
						oldDamage = PlayerHandler.players[p].totalDamageDealt;
						killerId = p;
						count++;
					}
					PlayerHandler.players[p].totalDamageDealt = 0;
				}
			}
		}
		return killerId;
	}

	/**
	 * Emotes
	 **/

	public static int getAttackEmote(int i) {
		return AttackAnimation.handleEmote(i);
	}
	public int extraDistance(int i) {
		switch (npcs[i].npcType) {
		case 494:
		case 496:
		case 2042:
		case 2043:
		case 2044:
		case 493:
			return 20;
		default:
			return 0;
		}
	}
	public int getDeadEmote(int i) {
		return DeathAnimation.handleEmote(i);
	}
	public static boolean isSkotizoNpc(int i) {
		if (npcs[i] == null)
			return false;
		switch (npcs[i].npcType) {
		case Skotizo.SKOTIZO_ID:
		case Skotizo.AWAKENED_ALTAR_NORTH:
		case Skotizo.AWAKENED_ALTAR_SOUTH:
		case Skotizo.AWAKENED_ALTAR_WEST:
		case Skotizo.AWAKENED_ALTAR_EAST:
		case Skotizo.REANIMATED_DEMON:
		case Skotizo.DARK_ANKOU:
			return true;
		}
		return false;
	}
	private ArrayList<int[]> vetionSpellCoordinates = new ArrayList<>(3);
	private ArrayList<int[]> archSpellCoordinates = new ArrayList<>(3);
	private ArrayList<int[]> fanaticSpellCoordinates = new ArrayList<>(3);
	private ArrayList<int[]> corpSpellCoordinates = new ArrayList<>(3);
	private ArrayList<int[]> olmSpellCoordinates = new ArrayList<>(3);
	private ArrayList<int[]> explosiveSpawnCoordinates = new ArrayList<>(3);
	private ArrayList<int[]> cerberusGroundCoordinates = new ArrayList<>(3);

	private void groundSpell(NPC npc, Client player, int startGfx, int endGfx, String coords, int time) {
		if (player == null) {
			return;
		}
		switch (coords) {
		case "vetion":
			player.coordinates = vetionSpellCoordinates;
			break;

		case "archaeologist":
			player.coordinates = archSpellCoordinates;
			break;

		case "fanatic":
			player.coordinates = fanaticSpellCoordinates;
			break;

		case "corp":
			player.coordinates = corpSpellCoordinates;
			break;

		case "olm":
			player.coordinates = olmSpellCoordinates;
			break;

		case "spawns":
			player.coordinates = explosiveSpawnCoordinates;

			List<NPC> exploader = Arrays.asList(NPCHandler.npcs);
			if (exploader.stream().filter(Objects::nonNull).anyMatch(n -> n.npcType == 6768 && !n.isDead)) {
				return;
			}
			break;

		case "cerberus":
			player.coordinates = cerberusGroundCoordinates;
			break;
		}
		int x = player.getX();
		int y = player.getY();
		player.coordinates.add(new int[] { x, y });
		for (int i = 0; i < 2; i++) {
			player.coordinates.add(new int[] { (x - 1) + Misc.random(3), (y - 1) + Misc.random(3) });
		}
		for (int[] point : player.coordinates) {
			int nX = npc.absX + 2;
			int nY = npc.absY + 2;
			int x1 = point[0] + 1;
			int y1 = point[1] + 2;
			int offY = (nX - x1) * -1;
			int offX = (nY - y1) * -1;
			if (startGfx > 0) {
				player.getPA().createPlayersProjectile(nX, nY, offX, offY, 40, getProjectileSpeed(npc.index),
						startGfx, 31, 0, -1, 5);
			}
			if (Objects.equals(coords, "spawns")) {
				spawnNpc(6768, point[0], point[1], 0, 0, -1, -1, -1, -1);
			}

		}
		if (Objects.equals(coords, "spawns")) {
			CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {

				@Override
				public void execute(CycleEventContainer container) {
					kill(6768, 0);
					container.stop();
				}

			}, 7);
		}
		CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				for (int[] point : player.coordinates) {
					int x2 = point[0];
					int y2 = point[1];
					if (endGfx > 0) {
						player.getPA().createPlayersStillGfx(endGfx, x2, y2, player.heightLevel, 5);
					}
					if (Objects.equals(coords, "cerberus")) {
						player.getPA().createPlayersStillGfx(1247, x2, y2, player.heightLevel, 5);
					}
				}
				player.coordinates.clear();
				container.stop();
			}

		}, time);
	}
	/**
	 * Handles kills towards daily tasks
	 * @param npc	The npc killed.
	 */
	private void handleDailyKills(NPC npc) {
		Client player = (Client) PlayerHandler.players[npc.killedBy];
		
		if (player != null) {
			DailyTaskKills.kills(player, npc.npcType);
		}
	}
	/**
	 * 
	 * Attack delays
	 **/
	public int getNpcDelay(int i) {
		switch (npcs[i].npcType) {
		case VorkathConstants.AWAKENED_VORKATH_ID:
		case VorkathConstants.SLEEPING_VORKATH_ID:
			return 4;
		case 2025:
		case 2028:
			return 7;
			
		case 2745:
			return 8;

		case 8349: case 8350: case 8351:
			 if (npcs[i].attackType == 2)
				 return 4;
			 else if (npcs[i].attackType == 1)
				 return 6;
			 else if (npcs[i].attackType == 0)
				 return 7;
		case 7554:
			return npcs[i].attackType == 2 ? 4 : 6;
		case 2558:
		case 2559:
		case 2560:
		case 2561:
		case 2550:
			return 6;
		case Brother.AHRIM:
			return 6;
		case Brother.DHAROK:
			return 7;
		case Brother.GUTHAN:
			return 5;
		case Brother.KARIL:
			return 4;
		case Brother.TORAG:
			return 5;
		case Brother.VERAC:
			return 5;
			// saradomin gw boss
		case 2562:
			return 2;

		default:
			return 5;
		}
	}

	/**
	 * Hit delays
	 **/
	public static boolean allowUnclippedDamage(int i) {
		switch (npcs[i].npcType) {
		case AbyssalSireConstants.SLEEPING_NPC_ID:
			return true;
		}
		return false;
	}
	public int getHitDelay(int i) {
		switch (npcs[i].npcType) {
		case 6611:
		case 6612:
			return npcs[i].attackType == 2 ? 3 : 2;
		case 6618:
			return npcs[i].attackType == 2 ? 3 : 2;
		case 6528:
		case 6610:
			return 3;
		case 6619:
			return npcs[i].attackType == 2 ? 3 : 2;
		case 2265:
		case 2266:
			// case 2267:
		case 2054:
		case 2892:
		case 2894:
			return 3;
		case 2215:
			npcs[i].graardor = Misc.random(5);
			if (npcs[i].graardor == 5) {
				return 3;
			}
			return 2;

		case 3129:
			npcs[i].tsutsaroth = Misc.random(4);
			if (npcs[i].tsutsaroth == 4) {
				return 3;
			}
			return 2;
		case 2205:
			npcs[i].zilyana = Misc.random(6);
			if (npcs[i].zilyana == 6 || npcs[i].zilyana == 5 || npcs[i].zilyana == 4) {
				return 6;
			}
			return 2;

		case 3125:
		case 3121:
		case 2167:
		case 2558:
		case 2559:
		case 2560:
			return 3;

		case 3127:
			if (npcs[i].attackType == 1 || npcs[i].attackType == 2)
				return 5;
			else
				return 2;

		case 2025:
			return 4;
		case 2028:
			return 3;

		default:
			return 2;
		}
	}

	/**
	 * Npc respawn time
	 **/
	public int getRespawnTime(int i) {
		NpcDefinition def = npcs[i].definition();
		if (def != null) {
			return def.getRespawnTime();
		}
		switch (npcs[i].npcType) {
		case 6600:
		case 6601:
		case 6602:
		case 320:
		case 1049:
		case 6617:
		case 3118:
		case 3120:
		case 6768:
		case 5862:
		case 5054:
		case 2402:
		case 2401:
		case 2400:
		case 2399:
		case 5916:
		case 7604:
		case 7605:
		case 7606:
		case 7585:
		case 5129:
		case 4922:
		case 7563:
		case 7573:
		case 7544:
		case 7566:
		case 7559:
			case 7553:
			case 7554:
			case 7555:
		case 7560:
		case 7528:
		case 7529:
		case 8508:
		case 7525: // Vanguard
		case 7526: // Vanguard
		case 7527: // Vanguard
		case 7530: // Vespula
		case 7531: // Vespula
		case 7532: // Vespula
		case 7533: // <col=00ffff>Abyssal portal</col>
		case 7534: // Lux grub
		case 7535: // Lux grub
		case 7536: // Lux grub
		case 7537: // Lux grub
		case 7538: // Vespine soldier
		case 7539: // Vespine soldier
		case 7540: // Tekton
		case 7541: // Tekton
		case 7542: // Tekton
		case 7543: // Tekton (enraged)
		case 7545: // Tekton
		case 7546: // Scavenger runt
		case 7547: // Scavenger runt
		case 7548: // Scavenger beast
		case 7549: // Scavenger beast
		case 7550: // Great Olm (Right claw)
		case 7551: // Great Olm
		case 7552: // Great Olm (Left claw)
		case 7556: // null
		case 7557: // null
		case 7558: // <col=00ffff>Fire</col>
		case 7561: // Muttadile
		case 7562: // Muttadile
		case 7564: // <col=00ffff>Meat tree</col>
		case 7565: // <col=00ffff>Rocks</col>
		case 7567: // Vasa Nistirio
		case 7568: // <col=00ffff>Glowing crystal</col>
		case 7569: // <col=00ffff>Guardian</col>
		case 7570: // <col=00ffff>Guardian</col>
		case 7571: // <col=00ffff>Guardian</col>
		case 7572: // <col=00ffff>Guardian</col>
		case 7574: // Lizardman shaman
		case 7575: // Spawn
		case 7576: // Jewelled Crab
		case 7577: // Jewelled Crab (red)
		case 7578: // Jewelled Crab (green)
		case 7579: // Jewelled Crab (blue)
		case 7580: // Energy focus (white)
		case 7581: // Energy focus (red)
		case 7582: // Energy focus (green)
		case 7583: // Energy focus (blue)
		case 7584: // Ice demon
		case 7586: // Icefiend
		case 7587: // Guanic bat
		case 7588: // Prael bat
		case 7589: // Giral bat
		case 7590: // Phluxia bat
		case 7591: // Kryket bat
		case 7592: // Murng bat
		case 7593: // Psykk bat
		case 7594: // Cave snake
		case 7595: // Captain Rimor
		case 7596: // null
		case 7597: // Lizard
		case 7598: // <col=00ffff>Strange Device</col>
		case 7599: // Mountain Guide
		case 7600: // Mountain Guide
		case 7601: // Swamp Priest
		case 7602: // Corrupted scavenger
		case 7603: // Corrupted scavenger
		case 7607: // Imerominia
		case 7608: // Pagida
		case 7609: // Istoria
		case 7610: // Logios
		case 7611: // Meleti
		case 7612: // Krato
		case 7613: // Ektheme
		case 7614: // Archeio
			return -1;

		case 963:
		case 965:
			return 10;

		case 6618:
		case 6619:
		case 319:
		case 5890:
			return 30;

		case 1046:
		case 465:
			return 60;

		case 6609:
		case 2265:
		case 2266:
		case 2267:
			return 70;

		case 6611:
		case 6612:
		case 492:
			return 90;

		case 2558:
		case 2559:
		case 2560:
		case 2561:
		case 2562:
		case 2563:
		case 2564:
		case 2205:
		case 2206:
		case 2207:
		case 2208:
		case 2215:
		case 2216:
		case 2217:
		case 2218:
		case 3129:
		case 3130:
		case 3131:
		case 3132:
		case 3162:
		case 3163:
		case 3164:
		case 3165:
		case 1641:
		case 1642:
			return 100;

		case 1643:
			return 180;

		case 1654:
			return 250;

		case 3777:
		case 3778:
		case 3779:
		case 3780:
		case 7302:
			return 500;
		default:
		return 30;
		}

	}
	/**
	 * Barrows kills
	 * 
	 * @param i
	 *            the barrow brother whom been killed
	 */
	public static NPC newNPC(int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit,
			int attack, int defence) {
		// first, search for a free slot
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}

		if (slot == -1)
			return null; // no free slot found

		NPC newNPC = new NPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType; // try
		npcs[slot] = newNPC;
		return newNPC;
	}
	public void shearSheep(Client player, int itemNeeded, int itemGiven, int animation, final int currentId, final int newId, int transformTime) {
		NPC npc = npcs[currentId];
		if (!player.getItems().playerHasItem(itemNeeded)) {
			player.sendMessage("You need " + ItemAssistant.getItemName(itemNeeded).toLowerCase() + " to do that.");
			return;
		}
		if (npcs[currentId].isTransformed == true) {
			player.sendMessage("This sheep has already been shorn.");
			return;
		}
		if (animation > 0) {
			player.animation(animation);
		}
		npcs[currentId].isTransformed = true;
		npc.requestTransform(newId);
		Server.npcHandler.npcs[currentId].forceChat("Baa!");
		player.getItems().addItem(itemGiven, 1);
		player.sendMessage("You get some " + ItemAssistant.getItemName(itemGiven).toLowerCase() + ".");
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				npc.requestTransform(currentId);
				container.stop();
			}

			@Override
			public void stop() {
				npcs[currentId].isTransformed = false;
			}
		}, transformTime);
	}
	public void process() {
		try {
			// Player player = PlayerHandler.players[npcs[i].spawnedBy];
			for (int i = 0; i < maxNPCs; i++) {
				if (npcs[i] == null)
					continue;
				npcs[i].onReset();
			}
			startWinterSpawnTimer();

			for (int i = 0; i < maxNPCs; i++) {
				if (npcs[i] != null) {
					NPC npc = npcs[i];
					int type = npcs[i].npcType;
					Player slaveOwner = (PlayerHandler.players[npcs[i].summonedBy] != null
							? (Player) PlayerHandler.players[npcs[i].summonedBy] : null);
					if (npcs[i] != null && slaveOwner == null && npcs[i].summoner) {
						npcs[i].absX = 0;
						npcs[i].absY = 0;
					}
					if (npcs[i] != null && slaveOwner != null && slaveOwner.hasNpc
							&& (!slaveOwner.goodDistance(npcs[i].getX(), npcs[i].getY(), slaveOwner.absX,
									slaveOwner.absY, 15) || slaveOwner.heightLevel != npcs[i].heightLevel)
							&& npcs[i].summoner) {
						npcs[i].absX = slaveOwner.absX;
						npcs[i].absY = slaveOwner.absY;
						npcs[i].heightLevel = slaveOwner.heightLevel;

					}

					if (npcs[i].actionTimer > 0) {
						npcs[i].actionTimer--;
					}

					if (npcs[i].npcType == 506) {
						if (System.currentTimeMillis() - npcs[i].lastText > 15000) {
							npcs[i].updateRequired = true;
							npcs[i].forceChat("Get your general supplies here!");
							npcs[i].lastText = System.currentTimeMillis();
						}
					}
					if (npcs[i].npcType == 1909) {
						if (System.currentTimeMillis() - npcs[i].lastText > 15000) {
							npcs[i].updateRequired = true;
							npcs[i].forceChat("Talk to me to vote.");
							npcs[i].lastText = System.currentTimeMillis();
						}
					}
					if (npcs[i].npcType == 3247) {
						if (System.currentTimeMillis() - npcs[i].lastText > 15000) {
							npcs[i].updateRequired = true;
							npcs[i].forceChat("Get your magical supplies here!");
							npcs[i].lastText = System.currentTimeMillis();
						}
					}
					if (npcs[i].npcType == 1044) {
						if (System.currentTimeMillis() - npcs[i].lastText > 15000) {
							npcs[i].updateRequired = true;
							npcs[i].forceChat("Get your range supplies here!");
							npcs[i].lastText = System.currentTimeMillis();
						}
					}
					if (npcs[i].npcType == 1045) {
						if (System.currentTimeMillis() - npcs[i].lastText > 15000) {
							npcs[i].updateRequired = true;
							npcs[i].forceChat("Get your fishing supplies here!");
							npcs[i].lastText = System.currentTimeMillis();
						}
					}
					if (npcs[i].npcType == 508) {
						if (System.currentTimeMillis() - npcs[i].lastText > 15000) {
							npcs[i].updateRequired = true;
							npcs[i].forceChat("Get your Basic Armours here!");
							npcs[i].lastText = System.currentTimeMillis();
						}
					}
					if (npcs[i].npcType == 1838) {
						if (System.currentTimeMillis() - npcs[i].lastText > 15000) {
							npcs[i].updateRequired = true;
							npcs[i].forceChat("Quack!");
							npcs[i].lastText = System.currentTimeMillis();
						}
					}
					if (npcs[i].npcType == 2805) {
						if (System.currentTimeMillis() - npcs[i].lastText > 15000) {
							npcs[i].updateRequired = true;
							npcs[i].forceChat("Moo");
							npcs[i].lastText = System.currentTimeMillis();
						}
					}
					if (npcs[i].npcType == 505) {
						if (System.currentTimeMillis() - npcs[i].lastText > 15000) {
							npcs[i].updateRequired = true;
							npcs[i].forceChat("Get your skilling supplies here!");
							npcs[i].lastText = System.currentTimeMillis();
						}
					}
					if (npcs[i].npcType == 527) {
						if (System.currentTimeMillis() - npcs[i].lastText > 15000) {
							npcs[i].updateRequired = true;
							npcs[i].forceChat("Get your skill capes here!");
							npcs[i].lastText = System.currentTimeMillis();
						}
					}
					if (npcs[i].npcType == 1172) {
						if (System.currentTimeMillis() - npcs[i].lastText > 15000) {
							npcs[i].updateRequired = true;
							npcs[i].forceChat("Get your Crafting supplies here!");
							npcs[i].lastText = System.currentTimeMillis();
						}
					}
					if (npcs[i].freezeTimer > 0) {
						npcs[i].freezeTimer--;
					}
					// Player playerr = PlayerHandler.players[i];

					if (npcs[i].hitDelayTimer > 0) {
						npcs[i].hitDelayTimer--;
					}

					if (npcs[i].hitDelayTimer == 1) {
						npcs[i].hitDelayTimer = 0;
						applyDamage(i);
					}

					if (npcs[i].attackTimer > 0) {
						npcs[i].attackTimer--;
					}
					if (npcs[i].HP > 0 && !npcs[i].isDead) {
						if (npcs[i].npcType == 6611 || npcs[i].npcType == 6612) {
							if (npcs[i].HP < (npcs[i].maximumHealth / 2) && !npcs[i].spawnedMinions) {
								NPCHandler.spawnNpc(5054, npcs[i].getX() - 1, npcs[i].getY(), 0, 1, 175, 14, 100, 120);
								NPCHandler.spawnNpc(5054, npcs[i].getX() + 1, npcs[i].getY(), 0, 1, 175, 14, 100, 120);
								npcs[i].spawnedMinions = true;
							}
						}
					}

					if (npcs[i].npcType == 6602 && !npcs[i].isDead) {
						NPC runiteGolem = getNpc(6600);
						if (runiteGolem != null && !runiteGolem.isDead) {
							npcs[i].isDead = true;
							npcs[i].needRespawn = false;
							npcs[i].actionTimer = 0;
						}
					}
					if (npcs[i].spawnedBy > 0) { // delete summons npc
						if (PlayerHandler.players[npcs[i].spawnedBy] == null
								|| PlayerHandler.players[npcs[i].spawnedBy].heightLevel != npcs[i].heightLevel
								|| PlayerHandler.players[npcs[i].spawnedBy].respawnTimer > 0
								|| !PlayerHandler.players[npcs[i].spawnedBy].goodDistance(npcs[i].getX(),
										npcs[i].getY(), PlayerHandler.players[npcs[i].spawnedBy].getX(),
										PlayerHandler.players[npcs[i].spawnedBy].getY(),
										NPCHandler.isFightCaveNpc(i) ? 60 : NPCHandler.isSkotizoNpc(i) ? 60 : 20)) {

							if (PlayerHandler.players[npcs[i].spawnedBy] != null) {
								for (int o = 0; o < PlayerHandler.players[npcs[i].spawnedBy].barrowsNpcs.length; o++) {
									if (npcs[i].npcType == PlayerHandler.players[npcs[i].spawnedBy].barrowsNpcs[o][0]) {
										if (PlayerHandler.players[npcs[i].spawnedBy].barrowsNpcs[o][1] == 1)
											PlayerHandler.players[npcs[i].spawnedBy].barrowsNpcs[o][1] = 0;
									}
								}
							}
							npcs[i] = null;
						}
					}

					if (npcs[i] == null)
						continue;
					if (npcs[i].lastX != npcs[i].getX() || npcs[i].lastY != npcs[i].getY()) {
						npcs[i].lastX = npcs[i].getX();
						npcs[i].lastY = npcs[i].getY();
					}
					Client glyphSpawner = (Client) PlayerHandler.players[npcs[i].spawnedBy];
					if(type ==7707){
						glyphSpawner.getInferno().glyphX=npc.absX;
						glyphSpawner.getInferno().glyphY=npc.absY;
						//glyphSpawner.sendMessage("@red@"+glyphSpawner.getInferno().glyphX + " " +glyphSpawner.getInferno().glyphY);
					}
					if (type == 7707 && npc.absX == 2270 && npc.absY >= 5361 && glyphSpawner.getInferno().glyphCanMove) { // Move
																															// forward
						npc.moveX = GetMove(npc.absX, 2270);
						npc.moveY = GetMove(npc.absY, 5360);
						npc.updateRequired = true;
						npc.getNextNPCMovement();
						npc.walkingHome = false;
					} else if (type == 7707 && npc.absX == 2270 && npc.absY == 5360
							&& glyphSpawner.getInferno().glyphCanMove) { // From forward, start left
						glyphSpawner.getInferno().glyphCanMove = false;
						glyphSpawner.getInferno().glyphMoveLeft = true;
						npc.moveX = GetMove(npc.absX, 2257);
						npc.moveY = GetMove(npc.absY, 5360);
						npc.updateRequired = true;
						npc.getNextNPCMovement();
						npc.walkingHome = false;
					} else if (type == 7707 && npc.absY == 5360 && glyphSpawner.getInferno().glyphMoveLeft) { // Once all
																												// the way
																												// to the
																												// left,
																												// move all
																												// the way
																												// to the
																												// right
						if (npc.absX == 2257 && npc.absY == 5360) {
							glyphSpawner.getInferno().glyphMoveLeft = false;
							glyphSpawner.getInferno().glyphMoveRight = true;
						}
						npc.moveX = GetMove(npc.absX, 2257);
						npc.moveY = GetMove(npc.absY, 5360);
						npc.updateRequired = true;
						npc.getNextNPCMovement();
						npc.walkingHome = false;
					} else if (type == 7707 && npc.absY == 5360 && glyphSpawner.getInferno().glyphMoveRight) { // Once all
																												// the way
																												// to the
																												// right,
																												// move all
																												// the way
																												// to the
																												// left
						if (npc.absX == 2283 && npc.absY == 5360) {
							glyphSpawner.getInferno().glyphMoveLeft = true;
							glyphSpawner.getInferno().glyphMoveRight = false;
						}
						npc.moveX = GetMove(npc.absX, 2283);
						npc.moveY = GetMove(npc.absY, 5360);
						npc.updateRequired = true;
						npc.getNextNPCMovement();
						npc.walkingHome = false;
					}
					if(type == 8615) {
						if(npc.HP <= 825) {
							npc.requestTransform(8619);
						}
					} 
					if(type == 8619) {
						if(npc.HP <= 550) {
							npc.requestTransform(8620);
						}
					}
					if(type == 8620) {
						if(npc.HP <= 275) {
							npc.requestTransform(8621);
						}
					}
					if (type >= 2042 && type <= 2044 && npcs[i].HP > 0) {
						Client player = (Client) PlayerHandler.players[npcs[i].spawnedBy];
						if (player != null && player.getZulrahEvent().getNpc() != null
								&& npcs[i].equals(player.getZulrahEvent().getNpc())) {
							int stage = player.getZulrahEvent().getStage();
							if (type == 2042) {
								if (stage == 0 || stage == 1 || stage == 4 || stage == 9 && npcs[i].totalAttacks >= 20
										|| stage == 11 && npcs[i].totalAttacks >= 5) {
									continue;
								}
							}
							if (type == 2044) {
								if ((stage == 5 || stage == 8) && npcs[i].totalAttacks >= 5) {
									continue;
								}
							}
						}
					}

					/**
					 * Attacking player
					 **/
					// Player player = PlayerHandler.players[npcs[i].spawnedBy];
					if (isAggressive(i, false) && !npc.underAttack && npc.killerId <= 0 && !npc.isDead
							&& !switchesAttackers(i) && npc.inMulti() && !Boundary.isIn(npc, Boundary.GODWARS_BOSSROOMS)
							&& !Boundary.isIn(npcs[i], Boundary.CORP)) {
						Player closestPlayer = null;
						int closestDistance = Integer.MAX_VALUE;
						God god = GodwarsNPCs.NPCS.get(npc.npcType);

						for (Player player : Server.playerHandler.players) {
							if (player == null) {
								continue;
							}
							if (player.isIdle)
								continue;

							if (god != null && player.inGodwars() && player.getEquippedGodItems() != null
									&& player.getEquippedGodItems().contains(god)) {
								continue;
							}
							/**
							 * Skips attacking a player if mode set to invisible
							 */
							if (player.isInvisible()) {
								continue;
							}

							int distance = Misc.distanceToPoint(npc.absX, npc.absY, player.absX, player.absY);
							if (distance < closestDistance && distance <= distanceRequired(i) + followDistance(i)) {
								closestDistance = distance;
								closestPlayer = player;
							}
						}
						if (closestPlayer != null) {
							npc.killerId = closestPlayer.getIndex();
							closestPlayer.underAttackBy = npc.getIndex();
							closestPlayer.underAttackBy2 = npc.getIndex();
						}
					} else if (isAggressive(i, false) && !npcs[i].underAttack && !npcs[i].isDead
							&& (switchesAttackers(i) || Boundary.isIn(npc, Boundary.GODWARS_BOSSROOMS))) {

						if (System.currentTimeMillis() - npcs[i].lastRandomlySelectedPlayer > 10000) {
							int player = getCloseRandomPlayer(i);

							if (player > 0) {
								npcs[i].killerId = player;
								PlayerHandler.players[player].underAttackBy = i;
								PlayerHandler.players[player].underAttackBy2 = i;
								npcs[i].lastRandomlySelectedPlayer = System.currentTimeMillis();
							}
						}
					}

					/*if (NpcDefinition.DEFINITIONS[i].isAggressive() && !npcs[i].underAttack && !npcs[i].isDead
							&& !switchesAttackers(i)) {
						npcs[i].killerId = getCloseRandomPlayer(i);
					}else if (NpcDefinition.DEFINITIONS[i].isAggressive() && !npcs[i].underAttack && !npcs[i].isDead
							&& switchesAttackers(i)) {
						npcs[i].killerId = getCloseRandomPlayer(i);
					}*/
					if (npcs[i].npcType == 320) {
						npcs[i].damageDealt += npcs[i].damageDone;

					}

					if (System.currentTimeMillis() - npcs[i].lastDamageTaken > 5000) {
						npcs[i].underAttackBy = 0;
						npcs[i].underAttack = false;
					}

					if ((npcs[i].killerId > 0 || npcs[i].underAttack) && !npcs[i].walkingHome
							&& retaliates(npcs[i].npcType)) {
						if (!npcs[i].isDead) {
							int p = npcs[i].killerId;
							if (PlayerHandler.players[p] != null) {
								if (npcs[i].summoner == false) {
									Client c = (Client) PlayerHandler.players[p];
									followPlayer(i, c.index);
									if (npcs[i] == null)
										continue;
									if (npcs[i].attackTimer == 0) {
										attackPlayer(c, i);
										// Player player =
										// PlayerHandler.players[npcs[i].spawnedBy];
										// npcs[i].face(player);
										// npcs[i].lastKillerId = c.index;
									}
								} else {
									Player c = PlayerHandler.players[p];
									if (npcs[i].absX == c.absX && npcs[i].absY == c.absY) {
										stepAway(i);
										npcs[i].randomWalk = false;
									} else {
										followPlayer(i, c.index);
									}
								}
							} else {
								npcs[i].killerId = 0;
								npcs[i].underAttack = false;
								npcs[i].face(null);
								// npcs[i].face(c);
							}

						}
					}


					/**
					 * Random walking and walking home
					 **/
					if (npcs[i] == null)
						continue;
					if ((!npcs[i].underAttack || npcs[i].walkingHome) && !isFightCaveNpc(i) && npcs[i].randomWalk
							&& !npcs[i].isDead) {
						npcs[i].face(null);
						npcs[i].killerId = 0;
						if (npcs[i].spawnedBy == 0) {
							if ((npcs[i].absX > npcs[i].makeX + Config.NPC_RANDOM_WALK_DISTANCE)
									|| (npcs[i].absX < npcs[i].makeX - Config.NPC_RANDOM_WALK_DISTANCE)
									|| (npcs[i].absY > npcs[i].makeY + Config.NPC_RANDOM_WALK_DISTANCE)
									|| (npcs[i].absY < npcs[i].makeY - Config.NPC_RANDOM_WALK_DISTANCE)
											&& npcs[i].npcType != 1635 && npcs[i].npcType != 1636 && npcs[i].npcType != 1637
											&& npcs[i].npcType != 1638 && npcs[i].npcType != 1639 && npcs[i].npcType != 1640
											&& npcs[i].npcType != 1641 && npcs[i].npcType != 1642 && npcs[i].npcType != 1643
											&& npcs[i].npcType != 1654 && npcs[i].npcType != 7302) {
								npcs[i].walkingHome = true;
							}
						}
						if (npcs[i].walkingHome && npcs[i].absX == npcs[i].makeX && npcs[i].absY == npcs[i].makeY) {
							npcs[i].walkingHome = false;
						} else if (npcs[i].walkingHome) {
							NPCDumbPathFinder.walkTowards(npcs[i], npcs[i].makeX, npcs[i].makeY);
							//npcs[i].updateRequired = true;
						}
						if (npcs[i].walkingType >= 0) {
							switch (npcs[i].walkingType) {

							case 5:
								npcs[i].face(npcs[i].absX - 1, npcs[i].absY);//west
								break;
							case 4:
								npcs[i].face(npcs[i].absX + 1, npcs[i].absY);//east
								break;
							case 3:
								npcs[i].face(npcs[i].absX, npcs[i].absY - 1);//south
								break;
							case 2:
								npcs[i].face(npcs[i].absX, npcs[i].absY + 1);//north
								break;

							default:
								if (npcs[i].walkingType >= 0) {
									npcs[i].face(npcs[i].absX, npcs[i].absY);
								}
								break;
							}
						}
						if (npcs[i].walkingType == 1 && (!npcs[i].underAttack) && !npcs[i].walkingHome) {
							if (System.currentTimeMillis() - npcs[i].getLastRandomWalk() > npcs[i].getRandomWalkDelay()) {
								int direction = Misc.random3(8);
								int movingToX = npcs[i].getX() + NPCClipping.DIR[direction][0];
								int movingToY = npcs[i].getY() + NPCClipping.DIR[direction][1];
								if (npcs[i].npcType >= 1635 && npcs[i].npcType <= 1643 || npcs[i].npcType == 1654
										|| npcs[i].npcType == 7302) {
									NPCDumbPathFinder.walkTowards(npcs[i], npcs[i].getX() - 1 + Misc.random(8),
											npcs[i].getY() - 1 + Misc.random(8));
								} else {
									if (Math.abs(npcs[i].makeX - movingToX) <= 1 && Math.abs(npcs[i].makeY - movingToY) <= 1
											&& NPCDumbPathFinder.canMoveTo(npcs[i], direction)) {
										NPCDumbPathFinder.walkTowards(npcs[i], movingToX, movingToY);
									}
								}
								npcs[i].setRandomWalkDelay(TimeUnit.SECONDS.toMillis(1 + Misc.random(2)));
								npcs[i].setLastRandomWalk(System.currentTimeMillis());
							}
						}
					}
					if (npcs[i].walkingHome) {
						if (!npcs[i].isDead) {
							NPCDumbPathFinder.walkTowards(npcs[i], npcs[i].makeX, npcs[i].makeY);
							if (npcs[i].moveX == 0 && npcs[i].moveY == 0) {
								npcs[i].teleport(npcs[i].makeX, npcs[i].makeY, npcs[i].heightLevel);
							}
							if (npcs[i].absX == npcs[i].makeX && npcs[i].absY == npcs[i].makeY) {
								npcs[i].walkingHome = false;
							}
						} else {
							npcs[i].walkingHome = false;
						}
					}
					if (npcs[i].isDead == true) {
						Client player = (Client) PlayerHandler.players[npcs[i].spawnedBy];
					
						if (npcs[i].actionTimer == 0 && npcs[i].applyDead == false && npcs[i].needRespawn == false) {
							if (npcs[i].npcType == 6618) {
								npcs[i].forceChat("Ow!");
							}
							if (SpawnEntity.DISTURBED_POOLS.get(npcs[i]) != null)
								SpawnEntity.DISTURBED_POOLS.remove(npcs[i]);
							if (npcs[i].npcType == 6611) {
								npcs[i].requestTransform(6612);
								npcs[i].HP = 255;
								npcs[i].isDead = false;
								npcs[i].spawnedMinions = false;
								npcs[i].forceChat("Do it again!!");
							} else {
								if (npcs[i].npcType == 6612) {
									npcs[i].npcType = 6611;
									npcs[i].spawnedMinions = false;
								}
								npcs[i].updateRequired = true;
								npcs[i].face(null);
								npcs[i].killedBy = getNpcKillerId(i);
								npcs[i].animId = getDeadEmote(i); // dead emote
								npcs[i].animUpdateRequired = true;
								if(npcs[i].npcType == 8621) {
									npcs[i].requestTransform(8622);
									npcs[i].animation(8258);
							}
								switch (npcs[i].npcType) {	
								case Skotizo.SKOTIZO_ID:
									if (player.getSkotizo() != null) {
										player.getSkotizo().end(DisposeTypes.COMPLETE);
									}
									break;
								case InfernoWave.TZKAL_ZUK:
									if (player.getInferno() != null) {
										player.getInferno().end(DisposeTypes.COMPLETE);
									}
									break;
								case 5862:
									Cerberus cerb = player.getCerberus();
									if (cerb != null) {
										cerb.end(DisposeTypes.COMPLETE);
									}
									break;
								case Skotizo.AWAKENED_ALTAR_NORTH:
									Server.getGlobalObjects().remove(28923, 1694, 9904, player.getSkotizo().getHeight()); // Remove North - Awakened Altar
										Server.getGlobalObjects().add(new GlobalObject(28924, 1694, 9904, player.getSkotizo().getHeight(), 2, 10, -1, -1)); // North - Empty Altar
										//player.getPA().sendChangeSprite(29232, (byte) 0);
										player.getSkotizo().altarCount--;
										player.getSkotizo().northAltar = false;
										player.getSkotizo().altarMap.remove(1);
									break;
								case Skotizo.AWAKENED_ALTAR_SOUTH:
										Server.getGlobalObjects().remove(28923, 1696, 9871, player.getSkotizo().getHeight()); // Remove South - Awakened Altar
										Server.getGlobalObjects().add(new GlobalObject(28924, 1696, 9871, player.getSkotizo().getHeight(), 0, 10, -1, -1)); // South - Empty Altar
										//player.getPA().sendChangeSprite(29233, (byte) 0);
										player.getSkotizo().altarCount--;
										player.getSkotizo().southAltar = false;
										player.getSkotizo().altarMap.remove(2);
									break;
								case Skotizo.AWAKENED_ALTAR_WEST:
										Server.getGlobalObjects().remove(28923, 1678, 9888, player.getSkotizo().getHeight()); // Remove West - Awakened Altar
										Server.getGlobalObjects().add(new GlobalObject(28924, 1678, 9888, player.getSkotizo().getHeight(), 1, 10, -1, -1)); // West - Empty Altar
										//player.getPA().sendChangeSprite(29234, (byte) 0);
										player.getSkotizo().altarCount--;
										player.getSkotizo().westAltar = false;
										player.getSkotizo().altarMap.remove(3);
									break;
								case Skotizo.AWAKENED_ALTAR_EAST:
										Server.getGlobalObjects().remove(28923, 1714, 9888, player.getSkotizo().getHeight()); // Remove East - Awakened Altar
										Server.getGlobalObjects().add(new GlobalObject(28924, 1714, 9888, player.getSkotizo().getHeight(), 3, 10, -1, -1)); // East - Empty Altar
										//player.getPA().sendChangeSprite(29235, (byte) 0);
										player.getSkotizo().altarCount--;
										player.getSkotizo().eastAltar = false;
										player.getSkotizo().altarMap.remove(4);
									break;
								case Skotizo.DARK_ANKOU:
									player.getSkotizo().ankouSpawned = false;
									break;
									
								}
								npcs[i].freezeTimer = 0;
								npcs[i].applyDead = true;
								//killedBarrow(i);
								//killedCrypt(i);
								if (player != null) {
									this.tzhaarDeathHandler(player, i);
									this.infernoDeathHandler(player, i);
									continue;
								}
								killedBarrow(i);
								npcs[i].actionTimer = 4; // delete time
								resetPlayersInCombat(i);
							}
						} else if (npcs[i].actionTimer == 0 &&npcs[i].applyDead&&!npcs[i].needRespawn) {
							if(npcs[i].npcType == VorkathConstants.SLEEPING_VORKATH_ID) {
								npcs[i].isDead = false;
								//player.sendMessage("HELL NO");
								continue;
							}
							if (npcs[i].npcType == VorkathConstants.AWAKENED_VORKATH_ID) {
								if (player.getVorkath().isForceDeath()) { //if forced death (ie player logout)
									npcs[i].isDead = true;
									npcs[i].updateRequired = true;
								} else {
									//player.sendMessage("Applying death");
									npcs[i].killedBy = player.getIndex();
									dropItems(i);
									player.getVorkath().resetCombat();
									player.getVorkath().handleDeath();
								}
								continue;
							}
							int killerIndex = npcs[i].killedBy;
							npcs[i].needRespawn = true;
							npcs[i].actionTimer = getRespawnTime(i); // respawn time
							dropItems(i);
							if (killerIndex < PlayerHandler.players.length - 1) {
								Client target = (Client) PlayerHandler.players[npcs[i].killedBy];

								if (target != null) {
									target.getSlayer().killTaskMonster(npcs[i]);
									
									  //if (target.getSlayer().isSuperiorNpc()) {
									  //target.getSlayer().handleSuperiorExp(npcs[i]); }
									 
								}
							}
							if(npcs[i].inRaids()){
								Player killer = PlayerHandler.players[npcs[i].killedBy];
								killer.getRaids().raidLeader.getRaids().handleMobDeath(npcs[i].npcType);
							}
							MonsterDropItem(i);
							//appendBossSlayerExperience(i);
							appendSlayerExperience(i);
							//appendBossKC(i);
							appendKillCount(i);
							handleDailyKills(npc);
							npcs[i].absX = npcs[i].makeX;
							npcs[i].absY = npcs[i].makeY;
							npcs[i].HP = npcs[i].maximumHealth;
							npcs[i].animId = 0x328;
							npcs[i].updateRequired = true;
							npcs[i].animUpdateRequired = true;
							if (npcs[i].npcType == 3127) {
								handleJadDeath(i);
							}

							if (npcs[i].npcType == 320) {
								npcs[i].absX = -1;
								npcs[i].absY = -1;
								npcs[i].heightLevel = -1;
								npcs[i].needRespawn = false;
								npcs[i] = null;
								return;
							}

							if (npcs[i].npcType == 6345) {
								npcs[i].absX = -1;
								npcs[i].absY = -1;
								npcs[i].heightLevel = -1;
								npcs[i].needRespawn = false;
								npcs[i].isDead = true;
								npcs[i] = null;
								//Server.task.handleDeath();
								return;
							}

							if (npcs[i].npcType == 7101) {
								npcs[i].absX = -1;
								npcs[i].absY = -1;
								npcs[i].heightLevel = -1;
								npcs[i].needRespawn = false;
								npcs[i].isDead = true;
								npcs[i] = null;
								//Server.task.handleDeath();
								return;
							}

							if (npcs[i].npcType == 5054) {
								npcs[i].absX = -1;
								npcs[i].absY = -1;
								npcs[i].heightLevel = -1;
								npcs[i].needRespawn = false;
								npcs[i] = null;
								return;
							}

							if (npcs[i].npcType == 6600) {
								spawnNpc(6601, npcs[i].absX, npcs[i].absY, 0, 0, 0, 0, 0, 0);
							} else if (npcs[i].npcType == 6601) {
								spawnNpc(6602, npcs[i].absX, npcs[i].absY, 0, 0, 0, 0, 0, 0);
								npcs[i] = null;
								NPC golem = getNpc(6600);
								if (golem != null) {
									golem.actionTimer = 150;
								}
							}
						} else if (npcs[i].actionTimer == 0 && npcs[i].needRespawn == true && npcs[i].npcType != 1739
								&& npcs[i].npcType != 1740 && npcs[i].npcType != 1741 && npcs[i].npcType != 1742
								&& npcs[i].npcType != 1747) {
							if (player != null) {
								npcs[i] = null;
							} else {
								int old1 = npcs[i].npcType;
								int old2 = npcs[i].makeX;
								int old3 = npcs[i].makeY;
								int old4 = npcs[i].heightLevel;
								int old5 = npcs[i].walkingType;
								int old6 = npcs[i].maximumHealth;
								int old7 = npcs[i].maxHit;
								int old8 = npcs[i].attack;
								int old9 = npcs[i].defence;
								npcs[i] = null;
								newNPC(old1, old2, old3, old4, old5, old6, old7, old8, old9);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static NPC getNpc(int npcType, int x, int y) {
		for (NPC npc : npcs)
			if (npc != null && npc.npcType == npcType && npc.absX == x && npc.absY == y)
				return npc;
		return null;
	}
	public static NPC getNpc(int npcType) {
		for (NPC npc : npcs)
			if (npc != null && npc.npcType == npcType)
				return npc;
		return null;
	}
	public static void spawnNpc(int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit,
			int attack, int defence) {
		// first, search for a free slot
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}
		if (slot == -1) {
			// Misc.println("No Free Slot");
			return; // no free slot found
		}
		NPC newNPC = new NPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		npcs[slot] = newNPC;
	}


	public boolean getsPulled(int i) {
		switch (npcs[i].npcType) {
		case 2550:
			if (npcs[i].firstAttacker > 0)
				return false;
			break;
		}
		return true;
	}

	public boolean multiAttacks(int i) {
		switch (npcs[i].npcType) {
		case 6345:
			if (npcs[i].attackType == 2) {
				return true;
			}
		case 7554:
			return true;
		case 7101:
			return true;
		case 7792: //Long-tailed Wyvern
		case 7793: //Taloned Wyvern
		case 7794: //Spitting Wyvern
		case 7795: //Ancient Wyvern
		case 7604:
		case 7605:
		case 7606:
			return npcs[i].attackType == 4;

		case 6618:
			return npcs[i].attackType == 2 || npcs[i].attackType == 1 ? true : false;
		case 6611:
		case 6612:
		case 6619:
			return npcs[i].attackType == 2 || npcs[i].attackType == 4 ? true : false;
		case 6528:
			return npcs[i].attackType == 2 || npcs[i].attackType == 4 && Misc.random(3) == 0 ? true : false;
		case 6610:
			return npcs[i].attackType == 2;
		case 2558:
			return true;
		case 2562:
			if (npcs[i].attackType == 2)
				return true;
		case 267:
			if (npcs[i].attackType == 3)
				return true;
		case 2215:
			return npcs[i].attackType == 1;
		case 3162:
			return true;
		default:
			return false;
		}

	}


	/**
	 * Barrows kills
	 * 
	 * @param i
	 *            the barrow brother whom been killed
	 */
	private void killedBarrow(int i) {
		Client player = (Client) PlayerHandler.players[npcs[i].killedBy];
		if (player != null && player.getBarrows() != null) {
			Optional<Brother> brother = player.getBarrows().getBrother(npcs[i].npcType);
			if (brother.isPresent()) {
				brother.get().handleDeath();
			} else if (Boundary.isIn(npcs[i], Barrows.TUNNEL)) {
				if (player.getBarrows().getKillCount() < 25) {
					player.getBarrows().increaseMonsterKilled();
				}
			}
		}
	}
	private void tzhaarDeathHandler(Client player, int i) {// hold a vit plz
		if (npcs[i] != null) {
			if (player != null) {
				if (player.getFightCave() != null) {
					if (isFightCaveNpc(i))
						killedTzhaar(player, i);
					if (npcs[i] != null && npcs[i].npcType == 3127) {
						this.handleJadDeath(i);
					}
				}
			}
		}
	}

	@SuppressWarnings("unused")
	private void killedTzhaar(Client player, int i) {
		if (player.getFightCave() != null) {
			player.getFightCave().setKillsRemaining(player.getFightCave().getKillsRemaining() - 1);
			if (player.getFightCave().getKillsRemaining() == 0) {
				player.waveId++;
				player.getFightCave().spawn();
			}
		}
	}

	public void handleJadDeath(int i) {
		Client c = (Client) PlayerHandler.players[npcs[i].spawnedBy];
		c.getItems().addItem(6570, 1);
		// c.getDH().sendDialogues(69, 2617);
		c.getFightCave().stop();
		yell("Player "+c.playerName+" has killed jad!");
		c.waveId = 300;
	}


	/**
	 * Dropping Items!
	 **/
	public void yell(String msg) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Client c2 = (Client) PlayerHandler.players[j];
				c2.sendMessage(msg);
			}
		}
	}
	public void rareDrops(Client c, int item, int amount) {
		if (c.dropChance == 0) {
			// c.sendMessage("No rare drop acquired.");
			return;

		} else if (c.dropChance == 1) {
			for (int loot : c.getPA().RARE_ITEMS) {			
				if (loot == item) {
					yell("<img=10>[<col=0000FF>DROP<col=000000>] <col=000000>" + c.playerName + " received a <col=0000FF>RARE <col=000000>drop: <col=0000FF>"
							+ amount + "x " + Item.getItemName(item));
					c.dropChance = 0;
					return;
						}
				
			}
				for (int loot1 : c.getPA().VERY_RARE_ITEMS) {
				if (loot1 == item) {
					yell("<img=10>[<col=0000FF>DROP<col=000000>] <col=000000>" + c.playerName + " received a <col=0000FF>VERY RARE <col=000000>drop: <col=0000FF>"
							+ amount + "x " + Item.getItemName(item));
					c.dropChance = 0;
					return;
						}
				}
				for (int loot2 : c.getPA().EXTREMELY_RARE_ITEMS) {
				if (loot2 == item) {
					yell("<img=10>[<col=0000FF>DROP<col=000000>] <col=000000>" + c.playerName + " received a <col=0000FF>EXTREMELY RARE <col=000000>drop: <col=0000FF>" 
							+ amount + "x " + Item.getItemName(item));
					c.dropChance = 0;
					return;
						}
					}
				
				}
			}
	

	public void dropItems(int i) {

		Client c = (Client) PlayerHandler.players[npcs[i].killedBy];
		if (c != null) {
			//c.getAchievements().kill(npcs[i]);
			if (npcs[i].npcType == 2042 || npcs[i].npcType == 2043 || npcs[i].npcType == 2044) {
				c.getZulrahEvent().stop();
			}
			if (npcs[i].npcType == 7151 	|| npcs[i].npcType == 7152 || npcs[i].npcType == 7153) {
				c.hits = 0;
			}
			if(npcs[i].npcType == 8611) {
				npcs[i].requestTransform(8610);
			}
			if (npcs[i].npcType == 5862) {
				c.CAST_GHOSTS = 0;
				c.CAST_ROCKS = 0;
			}
			if (c.ARMADYL_INSTANCE == true && npcs[i].npcType == 3162 || npcs[i].npcType == 3163
					|| npcs[i].npcType == 3164 || npcs[i].npcType == 3165) {
				c.ARMADYL_MINION++;
			}

			if (c.ARMADYL_MINION == 4) {
				c.ARMADYL_MINION = 0;
				c.ARMADYL_INSTANCE = false;
				c.getArmadyl().stop();
				c.getArmadyl().RespawnNpcs();
			}

			if (c.BANDOS_INSTANCE == true && npcs[i].npcType == 2215 || npcs[i].npcType == 2216
					|| npcs[i].npcType == 2217 || npcs[i].npcType == 2218) {
				c.BANDOS_MINION++;
			}

			if (c.BANDOS_MINION == 4) {// true that
				c.BANDOS_MINION = 0;
				c.BANDOS_INSTANCE = false;
				c.getBandos().stop();
				c.getBandos().RespawnNpcs();
			}
			if (npcs[i].npcType == 319) {
				newMinion = 0;
				hasMinions = false;
				kill(320, 2);
			}


			if (c.SARADOMIN_INSTANCE == true && npcs[i].npcType == 2205 || npcs[i].npcType == 2206
					|| npcs[i].npcType == 2207 || npcs[i].npcType == 2208) {
				c.SARADOMIN_MINION++;
			}

			if (c.SARADOMIN_MINION == 4) {
				c.SARADOMIN_MINION = 0;
				c.SARADOMIN_INSTANCE = false;
				c.getSaradomin().stop();
				c.getSaradomin().RespawnNpcs();
			}
			if (c.KALPHITE_INSTANCE == true && npcs[i].npcType == 965) {
				c.KALPHITE_MINION++;
			}

			if (c.KALPHITE_MINION == 1) {// true that
				c.KALPHITE_MINION = 0;
				c.KALPHITE_INSTANCE = false;
				c.getKalphite().stop();
				c.getKalphite().RespawnNpc();
			}

			if (c.ZAMORAK_INSTANCE == true && npcs[i].npcType == 3129 || npcs[i].npcType == 3130
					|| npcs[i].npcType == 3131 || npcs[i].npcType == 3132) {
				c.ZAMORAK_MINION++;
			}

			if (c.ZAMORAK_MINION == 4) {
				c.ZAMORAK_MINION = 0;
				c.ZAMORAK_INSTANCE = false;
				c.getZamorak().stop();
				c.getZamorak().RespawnNpcs();
			}
			if(isFightCaveNpc(npcs[i].npcType)) {
				return;
			}
			if (npcs[i].npcType == 494) {
				if (c.getKraken().getInstancedKraken() != null) {
					KILL_TENT(c, 5535, 2275, 10038, c.getKraken().getInstancedKraken().getHeight());
					KILL_TENT(c, 5535, 2275, 10034, c.getKraken().getInstancedKraken().getHeight());
					KILL_TENT(c, 5535, 2285, 10038, c.getKraken().getInstancedKraken().getHeight());
					KILL_TENT(c, 5535, 2285, 10034, c.getKraken().getInstancedKraken().getHeight());
					// KILL_TENT(c, npcid, npcs[i].absX, npcs[i].absY,
					// c.heightLevel);
					Server.itemHandler.createGroundItem(c, 526, 2283, 10030, c.heightLevel, 1, c.index);
					c.getKraken().stop();
				}
			}
			if (npcs[i].npcType == 8508) {

				Server.itemHandler.createGroundItem(c, Item2.randomXmas(), npcs[i].absX, npcs[i].absY, c.heightLevel, 1, c.index);
				//c.getKraken().stop();
		}
			if (npcs[i].npcType == 2461 || npcs[i].npcType == 2463 || npcs[i].npcType == 2464) {
				c.getWarriorsGuild().dropDefender(npcs[i].absX, npcs[i].absY);
			}
			if (npcs[i].npcType == 2137 || npcs[i].npcType == 2138 || npcs[i].npcType == 2139 || npcs[i].npcType == 2140 || npcs[i].npcType == 2141 || npcs[i].npcType == 2142) {
				c.getWarriorsGuildBasement().dropDefender(npcs[i].absX, npcs[i].absY);
			}
			/*
			 * BRONZE(2450, 1155, 1117, 1075, 5, 10, 2, 20, 20), IRON(2451,
			 * 1153, 1115, 1067, 10, 20, 4, 30, 30), STEEL(2452, 1157, 1119,
			 * 1069, 15, 40, 6, 50, 50), MITHRIL(2454, 1159, 1121, 1071, 50, 80,
			 * 10, 100, 100), ADAMANT(2455, 1161, 1123, 1073, 60, 100, 13, 120,
			 * 120), RUNE(2456, 1163, 1127, 1079, 80, 120, 18, 150, 150);
			 */
			c.getNpcDeathTracker().add(getNpcListName(npcs[i].npcType));
			if (AnimatedArmour.isAnimatedArmourNpc(npcs[i].npcType)) {
				AnimatedArmour.dropTokens(c, npcs[i].npcType, npcs[i].absX, npcs[i].absY);
			}
			PetHandler.receive(c, npcs[i]);

			if (npcs[i].npcType == 912 || npcs[i].npcType == 913 || npcs[i].npcType == 914)
				c.magePoints += 1;
			int dropX = npcs[i].absX;
			int dropY = npcs[i].absY;
			int dropHeight = npcs[i].heightLevel;
			if (npcs[i].npcType == VorkathConstants.AWAKENED_VORKATH_ID) {
				dropX = 2267;
				dropY = 4061;
				if (c.getNpcDeathTracker().getTracker().get("Vorkath") != null) {
					int kills = c.getNpcDeathTracker().getTracker().get("Vorkath");
					if (kills == 50) {
						Server.itemHandler.createGroundItem(c, 21907, dropX, dropY, c.heightLevel, 1);
					}
				}
			}
			//c.getItems().addItem(995, Misc.random(500, 999));
			NpcDropManager.dropItems(Optional.of(c), npcs[i]);
			//NpcDropSystem.get().drop(c, npcs[i]);
		}
	}


	public void appendKillCount(int i) {
		Client c = (Client) PlayerHandler.players[npcs[i].killedBy];
		if (c != null) {
			int[] kcMonsters = { 122, 49, 2558, 2559, 2560, 2561, 2550, 2551,
					2552, 2553, 2562, 2563, 2564, 2565 };
			for (int j : kcMonsters) {
				if (npcs[i].npcType == j) {
					if (c.killCount < 20) {
						c.killCount++;
						c.sendMessage("Killcount: " + c.killCount);
					} else {
						c.sendMessage("You already have 20 kill count");
					}
					break;
				}
			}
		}
	}

	// id of bones dropped by npcs
	public int boneDrop(int type) {
		switch (type) {
		case 1:// normal bones
		case 9:
		case 100:
		case 12:
		case 17:
		case 803:
		case 18:
		case 81:
		case 101:
		case 41:
		case 19:
		case 90:
		case 75:
		case 86:
		case 78:
		case 912:
		case 913:
		case 914:
		case 1648:
		case 1643:
		case 1618:
		case 1624:
		case 181:
		case 119:
		case 49:
		case 26:
		case 1341:
			return 526;
		case 117:
			return 532;// big bones
		case 50:// drags
		case 53:
		case 54:
		case 55:
		case 941:
		case 1590:
		case 1591:
		case 1592:
			return 536;
		case 84:
		case 1615:
		case 1613:
		case 82:
		case 2054:
			return 592;
		case 2881:
		case 2882:
		case 2883:
			return 6729;
		default:
			return -1;
		}
	}

	public int getStackedDropAmount(int itemId, int npcId) {
		switch (itemId) {
		case 995:
			switch (npcId) {
			case 1:
				return 50 + Misc.random(50);
			case 9:
				return 133 + Misc.random(100);
			case 1624:
				return 1000 + Misc.random(300);
			case 1618:
				return 1000 + Misc.random(300);
			case 1643:
				return 1000 + Misc.random(300);
			case 1610:
				return 1000 + Misc.random(1000);
			case 1613:
				return 1500 + Misc.random(1250);
			case 1615:
				return 3000;
			case 18:
				return 500;
			case 101:
				return 60;
			case 913:
			case 912:
			case 914:
				return 750 + Misc.random(500);
			case 1612:
				return 250 + Misc.random(500);
			case 1648:
				return 250 + Misc.random(250);
			case 90:
				return 200;
			case 82:
				return 1000 + Misc.random(455);
			case 52:
				return 400 + Misc.random(200);
			case 49:
				return 1500 + Misc.random(2000);
			case 1341:
				return 1500 + Misc.random(500);
			case 26:
				return 500 + Misc.random(100);
			case 20:
				return 750 + Misc.random(100);
			case 21:
				return 890 + Misc.random(125);
			case 117:
				return 500 + Misc.random(250);
			case 2607:
				return 500 + Misc.random(350);
			}
			break;
		case 11212:
			return 10 + Misc.random(4);
		case 565:
		case 561:
			return 10;
		case 560:
		case 563:
		case 562:
			return 15;
		case 555:
		case 554:
		case 556:
		case 557:
			return 20;
		case 892:
			return 40;
		case 886:
			return 100;
		case 6522:
			return 6 + Misc.random(5);

		}

		return 1;
	}

	/**
	 * Slayer Experience
	 **/
	public void appendSlayerExperience(int i) {
		Client c = (Client) PlayerHandler.players[npcs[i].killedBy];
		if (c != null) {
			if (c.slayerTask == npcs[i].npcType) {
				c.taskAmount--;
				c.getPA().addSkillXP(npcs[i].maximumHealth * Config.SLAYER_EXPERIENCE,
						18);
				if (c.taskAmount <= 0) {
					c.getPA().addSkillXP(
							(npcs[i].maximumHealth * 8) * Config.SLAYER_EXPERIENCE, 18);
					c.slayerTask = -1;
					c.sendMessage("You completed your slayer task. Please see a slayer master to get a new one.");
				}
			}
		}
	}

	/**
	 * Resets players in combat
	 */

	public void resetPlayersInCombat(int i) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null)
				if (PlayerHandler.players[j].underAttackBy2 == i)
					PlayerHandler.players[j].underAttackBy2 = 0;
		}
	}

	/**
	 * Npc Follow Player
	 **/

	public int GetMove(int Place1, int Place2) {
		if ((Place1 - Place2) == 0) {
			return 0;
		} else if ((Place1 - Place2) < 0) {
			return 1;
		} else if ((Place1 - Place2) > 0) {
			return -1;
		}
		return 0;
	}

	public boolean followPlayer(int i) {
		switch (npcs[i].npcType) {
		case VorkathConstants.AWAKENED_VORKATH_ID:
		case VorkathConstants.SLEEPING_VORKATH_ID:
			return false;
		case 5867:
		case 5868:
		case 5869:
		case 2042:
		case 2043:
		case 2044:
		case 494:
		case 497:
		case 5535:
		case 2892:
		case 2894:
		case 1739:
		case 1747:
		case 1740:
		case 1741:
		case 1742:
			return false;
		}
		return true;
	}
	public boolean canAttack(int i) {
		switch (npcs[i].npcType) {
		case 9999:
		case AbyssalSireConstants.SLEEPING_NPC_ID:
			return false;
		}
		return true;
	}

	public void followPlayer(int i, int playerId) {
		if (PlayerHandler.players[playerId] == null) {
			return;
		}
		if (!canAttack(i)) {
			return;
		}
		Player player = PlayerHandler.players[playerId];
		if (PlayerHandler.players[playerId].respawnTimer > 0) {
			npcs[i].facePlayer(0);
			npcs[i].randomWalk = true;
			npcs[i].underAttack = false;
			return;
		}
		if (Boundary.isIn(npcs[i], Boundary.CORP)) {
			if (!Boundary.isIn(player, Boundary.CORP)) {
				npcs[i].killerId = 0;
				return;
			}
		}
		if (Boundary.isIn(npcs[i], Boundary.GODWARS_BOSSROOMS)) {
			if (!Boundary.isIn(player, Boundary.GODWARS_BOSSROOMS)) {
				npcs[i].killerId = 0;
				return;
			}
		}
		if (Boundary.isIn(npcs[i], Zulrah.BOUNDARY) && (npcs[i].npcType >= 2042 && npcs[i].npcType <= 2044 || npcs[i].npcType == 6720)) {
			return;
		}
		if (!followPlayer(i)) {
			npcs[i].facePlayer(playerId);
			return;
		}
		npcs[i].facePlayer(playerId);
		
		if (npcs[i].npcType >= 1739 && npcs[i].npcType <= 1742 || npcs[i].npcType == 1747) {
			return;
		}
		int playerX = PlayerHandler.players[playerId].absX;
		int playerY = PlayerHandler.players[playerId].absY;
		npcs[i].randomWalk = false;
		int followDistance = followDistance(i);
		double distance = ((double) distanceRequired(i)) + (npcs[i].getSize() > 1 ? 0.5 : 0.0);
		
		if (player.absX == npcs[i].absX && player.absY == npcs[i].absY) {
			stepAway(i);
			npcs[i].randomWalk = false;
			npcs[i].facePlayer(player.index);
		}
		
		if (npcs[i].getDistance(playerX, playerY) <= distance)
			return;

		if ((npcs[i].spawnedBy > 0) || (npcs[i].absX < npcs[i].makeX + followDistance) && (npcs[i].absX > npcs[i].makeX - followDistance)
				&& (npcs[i].absY < npcs[i].makeY + followDistance) && (npcs[i].absY > npcs[i].makeY - followDistance)) {
			if (npcs[i].heightLevel == PlayerHandler.players[playerId].heightLevel) {
				if (PlayerHandler.players[playerId] != null && npcs[i] != null) {
					NPCDumbPathFinder.follow(npcs[i], player);		
				}
			}
		} else {
			npcs[i].facePlayer(0);
			npcs[i].randomWalk = true;
			npcs[i].underAttack = false;
		}
	}
	private ArrayList<int[]> coreCoordinates = new ArrayList<>(3);
	private boolean hasMinions = false;
	private int newMinion = 0;
	public static NPC spawnNpc67(Player c, int npcType, int x, int y, int heightLevel, int WalkingType, int HP,
			int maxHit, int attack, int defence, boolean headIcon, boolean attackPlayer) {
		// first, search for a free slot
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}
		if (slot == -1) {
			// Misc.println("No Free Slot");
			return null; // no free slot found
		}
		NPC newNPC = new NPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		// newNPC.HP = WildernessBoss.healthCalculations(c, i);
		newNPC.HP = HP;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.maxHit = maxHit;
		newNPC.attack = attack;
		newNPC.defence = defence;
		newNPC.needRespawn = false;
		if (headIcon)
			c.getPA().drawHeadicon(1, slot, 0, 0);
		if (attackPlayer) {
			newNPC.underAttack = true;
			if (c != null) {
				newNPC.killerId = c.index;
			}
		}
		return npcs[slot] = newNPC;
	}

	private void handleDarkCores(NPC npc, Client player) {
		if (player == null) {
			return;
		}
		int x = player.getX();
		int y = player.getY();
		coreCoordinates.add(new int[] { x, y });
		for (int[] point : coreCoordinates) {
			int nX = npc.absX + 2;
			int nY = npc.absY + 2;
			int iX = player.absX + 2;
			int iY = player.absY + 2;
			int x1 = point[0] + 1;
			int y1 = point[1] + 2;
			int offY = (nX - x1) * -1;
			int offX = (nY - y1) * -1;

			player.getPA().createPlayersProjectile(nX, nY, offX, offY, 40, getProjectileSpeed(npc.index), 319, 31, 0,
					-1, 5);
		}
		CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				for (int[] point : coreCoordinates) {
					int coordX = point[0];
					int coordY = point[1];
					// player.getPA().createPlayersStillGfx(317, coordX, coordY,
					// 0, 5);
					NPCHandler.spawnNpc67(player, 320, player.absX, player.absY, 2, -1, 50, 20, 2000, 80, true, true);
				}
				coreCoordinates.clear();
				container.stop();
			}

		}, 4);
	}

	private void doProjectiles(NPC npc, Client player) {
		if (player == null) {
			return;
		}
		int x = player.getX();
		int y = player.getY();

		player.getPA().createPlayersProjectile(npc.absX, npc.absY, player.absX, player.absY, 40,
				getProjectileSpeed(npc.index), 314, 31, 0, -1, 5);

		CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				int pX = player.getX();
				int pY = player.getY();
				int nX = npc.getX();
				int nY = npc.getY();
				int offX = (pY - nY) * -1;
				int offY = (pX - nX) * -1;
				int offX1 = (pY - nY) * -1;
				int offY1 = (pX - nX);
				int offX2 = (pY - nY) * -1;
				int offY2 = (pX - nX) * +2;
				player.getPA().createPlayersProjectile(pX, pY, offX1, offY1, 50,
						player.getCombat().getProjectileSpeed(), 315, 25, 10, player.oldPlayerIndex, 5);
				player.getPA().createPlayersProjectile(pX, pY, offX2, offY2, 50,
						player.getCombat().getProjectileSpeed(), 315, 31, 10, player.oldPlayerIndex, 5);
				/* Front */
				int offX3 = (pY - nY) * +1;
				int offY3 = (pX - nX);
				int offX4 = (pY - nY) * +1;
				int offY4 = (pX - nX) * +2;

				player.getPA().createPlayersProjectile(pX, pY, offX3, offY3, 50,
						player.getCombat().getProjectileSpeed(), 315, 36, 10, player.oldPlayerIndex, 10);
				player.getPA().createPlayersProjectile(pX, pY, offX4, offY4, 50,
						player.getCombat().getProjectileSpeed(), 315, 41, 10, player.oldPlayerIndex, 10);
				container.stop();
			}
		}, 3);
	}
	public void sendLootShareMessage(String message) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Player c2 = PlayerHandler.players[j];
				c2.sendMessage("<col=006600>" + message + "");
			}
		}
	}

	public int[] unallowed = { 2366, 592, 4587, 1149, 530, 526, 536, 1333, 1247, 1089, 1047, 1319 };

	public void handleLootShare(Client c, int item, int amount) {
		for (int i = 0; i < unallowed.length; i++) {
			if (item == unallowed[i]) {
				return;
			}
		}
		if (ItemCacheDefinition.forID(item).getvalue() > 100000) {
			sendLootShareMessage(c.playerName + " received a drop: " + amount + " x "
					+ Item.getItemName(item));
			/*
			 * int npcId = 0; NpcDefinition.DEFINITIONS[npcId].getName();
			 * PlayerHandler.executeGlobalMessage("<col=CC0000>" +
			 * WordUtils.capitalize(c.playerName) + "</col><col=255>" +
			 * " Has received a </col><col=CC0000>" +
			 * org.brutality.model.items.Item.getItemName(item) +
			 * "</col><col=255>'s" + " drop from "+
			 * NpcDefinition.DEFINITIONS[npcId].getName());
			 */
		}
	}
	public void loadSpell(Client c, int i) {
				int chance = 0;
				if (DemonicGorilla.isDemonicGorilla(npcs[i])) {
					DemonicGorilla.switchPrayer(npcs[i]);
				}
		switch (npcs[i].npcType) {
		case 2892:
			npcs[i].projectileId = 94;
			npcs[i].attackType = 2;
			npcs[i].endGfx = 95;
			break;
		case 2894:
			npcs[i].projectileId = 298;
			npcs[i].attackType = 1;
			break;
		case 264:
		case 259:
		case 247:
		case 268:
		case 270:
		case 274:
		case 272:
		case 273:
		case 2919:
		case 7273:
		case 7274:
		case 7275:
		case 6593:
			int random2 = Misc.random(2);
			if (random2 == 0) {
				npcs[i].projectileId = 393;
				npcs[i].endGfx = 430;
				npcs[i].attackType = 3;
			} else {
				npcs[i].attackType = 0;
				npcs[i].projectileId = -1;
				npcs[i].endGfx = -1;
			}
			break;
		case 7544:
			if (Objects.equals(tektonAttack, "MELEE")) {
				npcs[i].attackType = 0;
			} else if (Objects.equals(tektonAttack, "SPECIAL")) {
				npcs[i].attackType = 4;
				Tekton.tektonSpecial(c);
				tektonAttack = "MELEE";
				npcs[i].hitDelayTimer = 4;
				npcs[i].attackTimer = 8;
			}
			break;
			/**
			 * Tekton magers
			 */
			case 7617:
				npcs[i].attackType = 2;
				npcs[i].projectileId = 1348;
				npcs[i].endGfx = 1345;
				npcs[i].hitDelayTimer = 5;
				npcs[i].attackTimer = 15;
				break;
				case 7529:
					if (Misc.random(10) == 5) {
						npcs[i].attackType = 4;
						npcs[i].projectileId = 1348;
						npcs[i].endGfx = 1345;
						npcs[i].hitDelayTimer = 3;
					} else {
						npcs[i].attackType = 2;
						npcs[i].projectileId = 1348;
						npcs[i].endGfx = 1345;
						npcs[i].hitDelayTimer = 3;
					}
					break;

				case 7566:
					npcs[i].attackType = 2;
					npcs[i].projectileId = 1289;
					npcs[i].endGfx = 1295;
					npcs[i].hitDelayTimer = 3;
					break;

				case 7559: // deathly ranger
					npcs[i].attackType = 1;
					npcs[i].projectileId = 1120;
					//npcs[i].endGfx = 1345;
					npcs[i].hitDelayTimer = 3;
					break;

				case 7560:// deathly mage
					npcs[i].attackType = 2;
					npcs[i].projectileId = 1348;
					npcs[i].endGfx = 1345;
					npcs[i].hitDelayTimer = 3;
					break;
				case 7554:
					npcs[i].attackType = 2;
					npcs[i].projectileId = 970;
					npcs[i].endGfx = 971;
					npcs[i].hitDelayTimer = 3;
					break;
			case 7604:
			case 7605:
			case 7606:
				if (Misc.random(10) == 5) {
					npcs[i].attackType = 4;
					npcs[i].forceChat("RAA!");
					npcs[i].projectileId = 1348;
					npcs[i].endGfx = 1345;
					npcs[i].hitDelayTimer = 3;
				} else {
					npcs[i].attackType = 2;
					npcs[i].projectileId = 1348;
					npcs[i].endGfx = 1345;
					npcs[i].hitDelayTimer = 3;
				}
				break;
		case Skotizo.AWAKENED_ALTAR_NORTH:
		case Skotizo.AWAKENED_ALTAR_SOUTH:
		case Skotizo.AWAKENED_ALTAR_WEST:
		case Skotizo.AWAKENED_ALTAR_EAST:
			npcs[i].attackType = 2;
			npcs[i].projectileId = 1242;
			npcs[i].endGfx = 1243;
		break;
		
		
		case Skotizo.SKOTIZO_ID:
			int randomStyle;
			if (c.getSkotizo().firstHit) {
				randomStyle = 1;
				c.getSkotizo().firstHit = false;
			} else {
				randomStyle = Misc.random(1);
			}
			switch (randomStyle) {
				case 0:
					npcs[i].attackType = 0;
					npcs[i].projectileId = -1;
					npcs[i].endGfx = -1;
					break;
					
				case 1:
					npcs[i].attackType = 2;
					npcs[i].projectileId = 1242;
					npcs[i].endGfx = 1243;
					break;
			}
			break;
		case 465:
			boolean distanceToWyvern = c.goodDistance(npcs[i].absX, npcs[i].absY, c.getX(), c.getY(), 3);
			int newRandom = Misc.random(10);
			if (newRandom >= 2) {
				npcs[i].attackType = 2;
				npcs[i].projectileId = 258;
				npcs[i].endGfx = -1;
			} else if (distanceToWyvern && newRandom == 1) {
				npcs[i].attackType = 0;
				npcs[i].projectileId = -1;
				npcs[i].endGfx = -1;
			} else {
				npcs[i].attackType = 3;
				npcs[i].projectileId = 162;
				npcs[i].endGfx = 163;
			}
			break;
		case 6609:
			int attackStyles1 = Misc.random(100);
			final Random random5551 = new Random();
			if (attackStyles1 >= 0 && attackStyles1 <= 40 && c.CAST_KNOCK <= 4) {
				npcs[i].attackType = 0;
				npcs[i].projectileId = -1;
				npcs[i].attackTimer = 7;
				npcs[i].endGfx = -1;
				c.CAST_KNOCK++;
			} else if (attackStyles1 >= 41 && attackStyles1 <= 70 && c.CAST_KNOCK <= 4) {
				npcs[i].attackType = 2;
				npcs[i].projectileId = 395;
				npcs[i].attackTimer = 7;
				npcs[i].endGfx = 431;
				c.CAST_KNOCK++;
			} else if (attackStyles1 >= 71 && attackStyles1 <= 100 && c.CAST_KNOCK <= 4) {
				npcs[i].attackType = 2;
				npcs[i].projectileId = 395;
				npcs[i].attackTimer = 7;
				npcs[i].endGfx = 431;
				c.CAST_KNOCK++;
			} else if (c.CAST_KNOCK >= 5) {
				Callisto.KnockBack(c, npcs[i].absX - random5551.nextInt(5), npcs[i].absY);
				npcs[i].attackType = 2;
				npcs[i].projectileId = 395;
				npcs[i].attackTimer = 7;
				npcs[i].endGfx = 431;
				c.CAST_KNOCK = 0;
			}
			break;
	
		case 319:
			int ATTACK_STYLE;
			ATTACK_STYLE = Misc.random(2);
			if (npcs[i].HP < 1000 && hasMinions == false && newMinion == 0) {
				npcs[i].attackType = 2;
				npcs[i].attackTimer = 7;
				npcs[i].hitDelayTimer = 4;
				handleDarkCores(npcs[i], c);
				newMinion = 1;
				hasMinions = true;
			} else if (npcs[i].HP < 650 && hasMinions == false && newMinion == 1) {
				npcs[i].attackType = 2;
				npcs[i].attackTimer = 7;
				npcs[i].hitDelayTimer = 4;
				handleDarkCores(npcs[i], c);
				newMinion = 2;
				hasMinions = true;
			} else if (npcs[i].HP < 250 && hasMinions == false && newMinion == 2) {
				npcs[i].attackType = 2;
				npcs[i].attackTimer = 7;
				npcs[i].hitDelayTimer = 4;
				handleDarkCores(npcs[i], c);
				newMinion = 3;
				hasMinions = true;
			} else if (ATTACK_STYLE == 1) {
				npcs[i].attackType = 2;
				npcs[i].projectileId = 316;
				npcs[i].endGfx = -1;
				npcs[i].hitDelayTimer = 3;
				npcs[i].attackTimer = 4;
				hasMinions = false;
			} else if (ATTACK_STYLE == 2) {
				npcs[i].attackType = 2;
				npcs[i].projectileId = 314;
				npcs[i].endGfx = -1;
				npcs[i].hitDelayTimer = 3;
				npcs[i].attackTimer = 4;
				hasMinions = false;
				doProjectiles(npcs[i], c);
			}
			break;
		case 494:
		case 5535:
			npcs[i].attackType = 2;
			if (Misc.random(5) > 0 && npcs[i].npcType == 494 || npcs[i].npcType == 5535) {
				npcs[i].gfx0(161);
				npcs[i].projectileId = 162;
				npcs[i].endGfx = 163;
			} else {
				npcs[i].gfx0(161);
				npcs[i].projectileId = 162;
				npcs[i].endGfx = 163;
			}
			break;

		case 2042:
			chance = 1;
			if (c != null) {
				if (c.getZulrahEvent().getStage() == 9) {
					chance = 2;
				}
			}
			chance = Misc.random(chance);
			npcs[i].setFacePlayer(true);
			if (chance < 2) {
				npcs[i].attackType = 1;
				npcs[i].projectileId = 97;
				npcs[i].endGfx = -1;
				npcs[i].hitDelayTimer = 3;
				npcs[i].attackTimer = 4;
			} else {
				npcs[i].attackType = 2;
				npcs[i].projectileId = 156;
				npcs[i].endGfx = -1;
				npcs[i].hitDelayTimer = 3;
				npcs[i].attackTimer = 4;
			}
			break;
		case 2043:
			npcs[i].setFacePlayer(false);
			npcs[i].face(c.getX(), c.getY());
			npcs[i].targetedLocation = new Location3D(c.getX(), c.getY(), c.heightLevel);
			npcs[i].attackType = 0;
			npcs[i].attackTimer = 9;
			npcs[i].hitDelayTimer = 6;
			npcs[i].projectileId = -1;
			npcs[i].endGfx = -1;
			break;
		case 2044:
			npcs[i].setFacePlayer(true);
			if (Misc.random(3) > 0) {
				npcs[i].attackType = 2;
				npcs[i].projectileId = 1046;
				npcs[i].endGfx = -1;
				npcs[i].hitDelayTimer = 3;
				npcs[i].attackTimer = 4;
			} else {
				npcs[i].attackType = 1;
				npcs[i].projectileId = 1044;
				npcs[i].endGfx = -1;
				npcs[i].hitDelayTimer = 3;
				npcs[i].attackTimer = 4;
			}
			break;
		case 50:
			int random = Misc.random(4);
			if (random == 0) {
				npcs[i].projectileId = 393; // red
				npcs[i].endGfx = 430;
				npcs[i].attackType = 3;
			} else if (random == 1) {
				npcs[i].projectileId = 394; // green
				npcs[i].endGfx = 429;
				npcs[i].attackType = 3;
			} else if (random == 2) {
				npcs[i].projectileId = 395; // white
				npcs[i].endGfx = 431;
				npcs[i].attackType = 3;
			} else if (random == 3) {
				npcs[i].projectileId = 396; // blue
				npcs[i].endGfx = 428;
				npcs[i].attackType = 3;
			} else if (random == 4) {
				npcs[i].projectileId = -1; // melee
				npcs[i].endGfx = -1;
				npcs[i].attackType = 0;
			}
			break;
		case 6611:
		case 6612:
			chance = Misc.random(100);
			if (chance < 25) {
				npcs[i].attackType = 2;
				npcs[i].attackTimer = 7;
				npcs[i].hitDelayTimer = 4;
				Vetion.createVetionSpell(npcs[i], c);
			} else if (chance > 90 && System.currentTimeMillis() - npcs[i].lastSpecialAttack > 15_000) {
				npcs[i].attackType = 4;
				npcs[i].attackTimer = 5;
				npcs[i].hitDelayTimer = 2;
				npcs[i].lastSpecialAttack = System.currentTimeMillis();
			} else {
				npcs[i].attackType = 0;
				npcs[i].attackTimer = 5;
				npcs[i].hitDelayTimer = 2;
			}
			break;
			/**
			 * Cerberus
			 */
			case 5867: // range
				if (c.MAGIC_ATTACK >= 2 && c.MAGIC_ATTACK <= 10
						|| c.MELEE_ATTACK >= 2 && c.MELEE_ATTACK <= 10) {
					return;
				}
				if (!c.prayerActive[17]) {
					npcs[i].projectileId = 1248;
					npcs[i].hitDelayTimer = 4;
					npcs[i].attackTimer = 7;
					c.MELEE_ATTACK += 4;
				} else if (c.prayerActive[17]) {
					npcs[i].attackType = 1;
					npcs[i].projectileId = 1248;
					npcs[i].hitDelayTimer = 4;
					npcs[i].attackTimer = 7;
					c.MELEE_ATTACK += 4;
				}
				break;
			case 5868: // mage
				if (!c.prayerActive[16] && c.MAGIC_ATTACK == 10 && c.RANDOM == 3) {
					npcs[i].projectileId = 100;
					npcs[i].hitDelayTimer = 4;
					npcs[i].endGfx = 101;
					npcs[i].attackTimer = 7;
					c.RANGE_ATTACK++;
				} else if (c.prayerActive[16] && c.MAGIC_ATTACK == 10 && c.RANDOM == 3) {
					npcs[i].attackType = 2;
					npcs[i].projectileId = 100;
					npcs[i].hitDelayTimer = 4;
					npcs[i].endGfx = 101;
					npcs[i].attackTimer = 7;
					c.RANGE_ATTACK++;
				}
				break;
			case 5869: // melee
				if (!c.prayerActive[18] && c.MELEE_ATTACK == 10 && c.RANDOM_MELEE == 3) {
					npcs[i].projectileId = 1248;
					npcs[i].hitDelayTimer = 4;
					npcs[i].attackTimer = 7;
					c.MAGIC_ATTACK += 4;
					c.MELEE_ATTACK += 2;
				} else if (c.prayerActive[18] && c.MELEE_ATTACK == 10 && c.RANDOM_MELEE == 3) {
					npcs[i].attackType = 0;
					npcs[i].projectileId = 1248;
					npcs[i].hitDelayTimer = 4;
					npcs[i].attackTimer = 7;
					c.MAGIC_ATTACK += 4;
					c.MELEE_ATTACK += 2;
				}
				break;

				/**
				 * Cerberus
				 */
				case 5862:
					if (Objects.equals(c.CERBERUS_ATTACK_TYPE, "GROUND_ATTACK")) {
						startAnimation(4492, i);
						npcs[i].forceChat("Grrrrrrrrrrrrrr");
						npcs[i].attackType = 3;
						npcs[i].hitDelayTimer = 4;
						groundSpell(npcs[i], c, -1, 1246, "cerberus", 4);
						c.CERBERUS_ATTACK_TYPE = "MELEE";
					}
					if (Objects.equals(c.CERBERUS_ATTACK_TYPE, "GHOST_ATTACK")) {
						startAnimation(4494, i);
						// npcs[i].forceChat("Aaarrrooooooo");
						c.CERBERUS_ATTACK_TYPE = "MELEE";
					}
					if (Objects.equals(c.CERBERUS_ATTACK_TYPE, "FIRST_ATTACK")) {
						startAnimation(4493, i);
						npcs[i].attackTimer = 5;
						c.CERBERUS_ATTACK_TYPE = "MELEE";
						CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
							int ticks = 0;

							@Override
							public void execute(CycleEventContainer container) {
								if (c.disconnected) {
									stop();
									return;
								}
								switch (ticks++) {
								case 0:
									npcs[i].attackType = 0;
									npcs[i].projectileId = -1;
									npcs[i].endGfx = -1;
									break;

								case 2:
									npcs[i].attackType = 1;
									npcs[i].projectileId = 1245;
									npcs[i].endGfx = 1244;
									break;

								case 4:
									npcs[i].attackType = 2;
									npcs[i].projectileId = 1242;
									npcs[i].endGfx = 1243;
									container.stop();
									break;
								}
								// System.out.println("Ticks - cerb " + ticks);
							}

							@Override
							public void stop() {

							}
						}, 2);
					} else {
						randomStyle = Misc.random(2);

						switch (randomStyle) {
						case 0:
							npcs[i].attackType = 0;
							npcs[i].projectileId = -1;
							npcs[i].endGfx = -1;
							break;

						case 1:
							npcs[i].attackType = 1;
							npcs[i].projectileId = 1245;
							npcs[i].endGfx = 1244;
							break;

						case 2:
							npcs[i].attackType = 2;
							npcs[i].projectileId = 1242;
							npcs[i].endGfx = 1243;
							break;
						}
					}
					break;
				case 239:
					int random1 = Misc.random(100);
					int distance = c.distanceToPoint(npcs[i].absX, npcs[i].absY);
					if (random1 >= 60 && random1 < 65) {
						npcs[i].projectileId = 394; // green
						npcs[i].endGfx = 429;
						npcs[i].attackType = 3;
					} else if (random1 >= 65 && random1 < 75) {
						npcs[i].projectileId = 395; // white
						npcs[i].endGfx = 431;
						npcs[i].attackType = 3;
					} else if (random1 >= 75 && random1 < 80) {
						npcs[i].projectileId = 396; // blue
						npcs[i].endGfx = 428;
						npcs[i].attackType = 3;
					} else if (random1 >= 80 && distance <= 4) {
						npcs[i].projectileId = -1; // melee
						npcs[i].endGfx = -1;
						npcs[i].attackType = 0;
					} else {
						npcs[i].projectileId = 393; // red
						npcs[i].endGfx = 430;
						npcs[i].attackType = 3;
					}
					break;
		// arma npcs
		case 2561:
			npcs[i].attackType = 0;
			break;
		case 2560:
			npcs[i].attackType = 1;
			npcs[i].projectileId = 1190;
			break;
		case 2559:
			npcs[i].attackType = 2;
			npcs[i].projectileId = 1203;
			break;
		case 2558:
			random1 = Misc.random(1);
			npcs[i].attackType = 1 + random1;
			if (npcs[i].attackType == 1) {
				npcs[i].projectileId = 1197;
			} else {
				npcs[i].attackType = 2;
				npcs[i].projectileId = 1198;
			}
			break;
		// sara npcs
		case 2562: // sara
			random1 = Misc.random(1);
			if (random1 == 0) {
				npcs[i].attackType = 2;
				npcs[i].endGfx = 1224;
				npcs[i].projectileId = -1;
			} else if (random1 == 1)
				npcs[i].attackType = 0;
			break;
		case 2563: // star
			npcs[i].attackType = 0;
			break;
		case 2564: // growler
			npcs[i].attackType = 2;
			npcs[i].projectileId = 1203;
			break;
		case 2565: // bree
			npcs[i].attackType = 1;
			npcs[i].projectileId = 9;
			break;
		// bandos npcs
		case 2550:
			random1 = Misc.random(2);
			if (random1 == 0 || random1 == 1)
				npcs[i].attackType = 0;
			else {
				npcs[i].attackType = 1;
				npcs[i].endGfx = 1211;
				npcs[i].projectileId = 288;
			}
			break;
		case 2551:
			npcs[i].attackType = 0;
			break;
		case 2552:
			npcs[i].attackType = 2;
			npcs[i].projectileId = 1203;
			break;
		case 2553:
			npcs[i].attackType = 1;
			npcs[i].projectileId = 1206;
			break;
		case 2025:
			npcs[i].attackType = 2;
			int r = Misc.random(3);
			if (r == 0) {
				npcs[i].gfx100(158);
				npcs[i].projectileId = 159;
				npcs[i].endGfx = 160;
			}
			if (r == 1) {
				npcs[i].gfx100(161);
				npcs[i].projectileId = 162;
				npcs[i].endGfx = 163;
			}
			if (r == 2) {
				npcs[i].gfx100(164);
				npcs[i].projectileId = 165;
				npcs[i].endGfx = 166;
			}
			if (r == 3) {
				npcs[i].gfx100(155);
				npcs[i].projectileId = 156;
			}
			break;
		case 2881:// supreme
			npcs[i].attackType = 1;
			npcs[i].projectileId = 298;
			break;

		case 2882:// prime
			npcs[i].attackType = 2;
			npcs[i].projectileId = 162;
			npcs[i].endGfx = 477;
			break;

		case 2028:
			npcs[i].attackType = 1;
			npcs[i].projectileId = 27;
			break;

		case 2054:
			int r2 = Misc.random(1);
			if (r2 == 0) {
				npcs[i].attackType = 1;
				npcs[i].gfx100(550);
				npcs[i].projectileId = 551;
				npcs[i].endGfx = 552;
			} else {
				npcs[i].attackType = 2;
				npcs[i].gfx100(553);
				npcs[i].projectileId = 554;
				npcs[i].endGfx = 555;
			}
			break;
		case 2745:
			int r3 = 0;
			if (goodDistance(npcs[i].absX, npcs[i].absY,
					PlayerHandler.players[npcs[i].spawnedBy].absX,
					PlayerHandler.players[npcs[i].spawnedBy].absY, 1))
				r3 = Misc.random(2);
			else
				r3 = Misc.random(1);
			if (r3 == 0) {
				npcs[i].attackType = 2;
				npcs[i].endGfx = 157;
				npcs[i].projectileId = 448;
			} else if (r3 == 1) {
				npcs[i].attackType = 1;
				npcs[i].endGfx = 451;
				npcs[i].projectileId = -1;
			} else if (r3 == 2) {
				npcs[i].attackType = 0;
				npcs[i].projectileId = -1;
			}
			break;
		case 2205:
			switch (npcs[i].zilyana) {
			case 0:
			case 1:
			case 2:
			case 3:
				npcs[i].attackType = 0;
				break;
			case 4:
			case 5:
			case 6:
				npcs[i].attackType = 2;
				break;
			}
			break;
		case 3162:
			npcs[i].kree = Misc.random(2);
			switch (npcs[i].kree) {
			case 0:
				npcs[i].attackType = 2;
				npcs[i].projectileId = 1199;
				break;
			case 1:
			case 2:
				npcs[i].attackType = 1;
				npcs[i].projectileId = 1198;
				break;
			}
			if (Misc.random(5) >= 3) {
				npcs[i].forceChat(npcs[i].Kree());
			}
			break;
		case 3129:
			if (Misc.random(5) >= 3) {
				npcs[i].forceChat(npcs[i].Tsutsaroth());
			}
			switch (npcs[i].tsutsaroth) {
			case 0:
			case 1:
			case 2:
			case 3:
				npcs[i].attackType = 0;
				npcs[i].projectileId = -1;
				break;
			case 4:
				npcs[i].attackType = 2;
				// npcs[i].multiAttack = true;
				npcs[i].gfx0(1165);
				npcs[i].projectileId = 1166;
				break;
			}
			break;
		case 2206: // star
			npcs[i].setFacePlayer(true);
			npcs[i].attackType = 0;
			npcs[i].projectileId = -1;
			break;
		case 2207: // growler
			npcs[i].setFacePlayer(true);
			npcs[i].attackType = 2;
			npcs[i].projectileId = 1203;
			break;
		case 2208: // bree
			npcs[i].setFacePlayer(true);
			npcs[i].attackType = 1;
			npcs[i].projectileId = 9;
			break;
			// bandos npcs
			/*
			 * case 2215:// bandos random = Misc.random(3); if (random == 0 ||
			 * random == 1) { npcs[i].attackType = 0; npcs[i].projectileId = -1;
			 * npcs[i].endGfx = -1; } else { npcs[i].attackType = 1;
			 * npcs[i].projectileId = 1200; npcs[i].endGfx = -1; } break;
			 */
			case 2215:
				switch (npcs[i].graardor) {
				case 0:
				case 1:
				case 2:
				case 3:
				case 4:
					if (Misc.random(5) >= 4) {
						npcs[i].forceChat(npcs[i].Graardor());
					}
					npcs[i].attackType = 0;
					npcs[i].projectileId = -1;
					break;
				case 5:
					if (Misc.random(5) >= 3) {
						npcs[i].forceChat(npcs[i].Graardor());
					}
					npcs[i].attackType = 1;
					// npcs[i].multiAttack = true;
					npcs[i].gfx0(1203);
					npcs[i].projectileId = 1202;
					break;
				}
				break;
		case 8349: case 8350: case 8351:
			 if (goodDistance(npcs[i].absX, npcs[i].absY, PlayerHandler.players[npcs[i].killerId].absX, PlayerHandler.players[npcs[i].killerId].absY, 2))
				 r3 = Misc.random(2);
			 else
				 r3 = Misc.random(1);
			 if (r3 == 0) {
				 npcs[i].attackType = 2;
				 //npcs[i].gfx100(1885);
				 //npcs[i].projectileId = 1884;
			 } else if (r3 == 1) {
				 npcs[i].attackType = 1;
				// npcs[i].projectileId = 1889;
			 } else if (r3 == 2) {
				 npcs[i].attackType = 0;
				 //npcs[i].gfx100(1886);
				 npcs[i].projectileId = -1;
			 } else if (Misc.random(4) == 1) {
				 npcs[i].attackType = 0;
				// npcs[i].gfx100(1885);
				npcs[i].animation(10917);
				 npcs[i].forceChat("Aaaaaaaaargh!! Now you will die!");
				 c.appendDamage(c.playerLevel[3] - 10, Hitmark.HIT);
				 npcs[i].projectileId = -1;
			 }			
			 break;
		case 2743:
			npcs[i].attackType = 2;
			npcs[i].projectileId = 445;
			npcs[i].endGfx = 446;
			break;

		case 2631:
			npcs[i].attackType = 1;
			npcs[i].projectileId = 443;
			break;
		}
			}

	/**
	 * Distanced required to attack
	 **/
	/**
	 * Distanced required to attack
	 **/
	public int distanceRequired(int i) {
		switch (npcs[i].npcType) {
		case 7152:
		case 7151:
			return 5;
		case 7241: //Abyssal demon
		case 7242: //Black demon
		case 7243: //Black demon
		case 7244: //Greater demon
		case 7245: //Greater demon
		case 7246: //Greater demon
		case 7247: //Lesser demon
		case 7248: //Lesser demon
		case 7249: //Dust devil
		case 7250: //Dark beast
		case 7251: //Fire giant
		case 7252: //Fire giant
		case 7253: //Bronze dragon
		case 7254: //Iron dragon
		case 7255: //Steel dragon
		case 7256: //Hellhound
		case 7257: //Ankou
		case 7258: //Shade
		case 7259: //Dagannoth
		case 7260: //Dagannoth
		case 7261: //Hill Giant
		case 7262: //Moss giant
		case 7263: //Ghost
		case 7264: //Ghost
		case 7265: //Skeleton
		case 7266: //King Sand Crab
		case 7268: //Possessed pickaxe
		case 7269: //Magic axe
		case 7270: //Cyclops
		case 7271: //Cyclops
		case 7272: //Twisted Banshee
		case 7273: //Brutal blue dragon
		case 7274: //Brutal red dragon
		case 7275: //Brutal black dragon
		case 7276: //Mutated Bloodveld
		case 7277: //Warped Jelly
		case 7278: //Greater Nechryael
		case 7279: //Deviant spectre
			return 2;

			case 7554:
			return 25;
			case VorkathConstants.AWAKENED_VORKATH_ID:
				return 35;
		case Skotizo.SKOTIZO_ID:
			return npcs[i].attackType == 2 ? 15 : 2;
		/*		case Skotizo.SKOTIZO_ID:
			return npcs[i].attackType == CombatType.MAGE ? 15 : 2;
		 * case 6767: return 25;
		 */
		case 5869:
		case 5868:
		case 5867:
			return 50;
		case 319:
			return 20;
		case 494:
		case 5535:
			return 25;
		case Zulrah.SNAKELING:
			return 1;
		case 6619:
		case 6618:
			return 4;
		case 7604: // Skeletal mystic
		case 7605: // Skeletal mystic
		case 7606: // Skeletal mystic
		case 4922:
			return 8;

		case 465:
			return npcs[i].attackType == 1 || npcs[i].attackType == 3 ? 6 : 2;
		case 5890:
		case 7544:
		case 5129:
			return npcs[i].attackType == 0 ? 3 : 7;
			/**
			 * Cerberus
			 */
			case 5862:
			case 6766:
			case 7144:
			case 7145:
			case 7146:
				return npcs[i].attackType == 0 ? 1 : 7;

		case 2205:
			if (npcs[i].attackType == 0) {
				return 2;
			} else if (npcs[i].attackType == 2) {
				return 10;
			}
		case 6615:
			if (npcs[i].freezeTimer > 0) {
				return 10;
			} else if (npcs[i].freezeTimer < 0) {
				return 1;
			}
		case 3163:
		case 3162:
		case 3164:
		case 3165:
		case 2208:
			return 13;
		/*
		 * case 493: return 200;
		 */
		case 2215:
			return npcs[i].attackType == 0 ? npcs[i].getSize() : 6;
		/*
		 * case 493: return npcs[i].attackType == 0 ? npcs[i].getSize() : 6;
		 */
		case 2217:
		case 2218:
			return 6;
		case 2044:
		case 2043:
		case 2042:
			return 50;
		case 6611:
		case 6612:
			return npcs[i].attackType == 4 ? 8 : 3;
		case 6528:
		case 6610:
			return 8;
		case 6609:
			return 8;
		case 3998:
		case 497:
			return 10;
		case 2025:
		case 2028:
			return 6;
		case 2562:
		case 3131:
		case 3132:
		case 3130:
		case 2206:
			return 2;
		case 2207:
			return 13;
		case 2265:// dag kings
		case 2266:
		case 2054:// chaos ele
		case 3125:
		case 3121:
		case 2167:
		case 3127:
			return 8;
		case 2267:
			return 4;
		case 239:
			return npcs[i].attackType == 3 ? 18 : 4;
		case 2552:
		case 2553:
		case 2556:
		case 2557:
		case 2558:
		case 2559:
		case 2560:
		case 2564:
		case 2565:
			return 9;
		// things around dags
		case 2892:
		case 2894:
			return 10;
		default:
			return 1;
		}
	}

	public int followDistance(int i) {
		switch (npcs[i].npcType) {
		case 2550:
		case 2551:
		case 2562:
		case 2563:
			return 8;
		case 239:
			return 90;
		case 7241: //Abyssal demon
		case 7242: //Black demon
		case 7243: //Black demon
		case 7244: //Greater demon
		case 7245: //Greater demon
		case 7246: //Greater demon
		case 7247: //Lesser demon
		case 7248: //Lesser demon
		case 7249: //Dust devil
		case 7250: //Dark beast
		case 7251: //Fire giant
		case 7252: //Fire giant
		case 7253: //Bronze dragon
		case 7254: //Iron dragon
		case 7255: //Steel dragon
		case 7256: //Hellhound
		case 7257: //Ankou
		case 7258: //Shade
		case 7259: //Dagannoth
		case 7260: //Dagannoth
		case 7261: //Hill Giant
		case 7262: //Moss giant
		case 7263: //Ghost
		case 7264: //Ghost
		case 7265: //Skeleton
		case 7266: //King Sand Crab
		case 7268: //Possessed pickaxe
		case 7269: //Magic axe
		case 7270: //Cyclops
		case 7271: //Cyclops
		case 7272: //Twisted Banshee
		case 7273: //Brutal blue dragon
		case 7274: //Brutal red dragon
		case 7275: //Brutal black dragon
		case 7276: //Mutated Bloodveld
		case 7277: //Warped Jelly
		case 7278: //Greater Nechryael
		case 7279: //Deviant spectre
			return 3;
		case 8349: case 8350: case 8351:
			 return 7;
		case 2883:
			return 4;
		case 2881:
		case 2882:
			return 1;
		case 7792: //Long-tailed Wyvern
		case 7793: //Taloned Wyvern
		case 7794: //Spitting Wyvern
		case 7795: //Ancient Wyvern
			return 6;
		case 2205:
		case 2206:
		case 2207:
		case 2208:
		case 2215:
		case 2216:
		case 2217:
		case 2218:
		case 3129:
		case 3130:
		case 3131:
		case 3132:
		case 3162:
		case 3163:
		case 3164:
		case 3165:
			return 20;
		case 2045:
		case 494:
			return 20;
		case 7544:
			return 15;

		}
		return 0;

	}

	public static int getProjectileSpeed(int i) {
		switch (npcs[i].npcType) {
		case 2881:
		case 2882:
		case 2054:
			return 85;

		case 2745:
			return 130;

		case 50:
			return 90;

		case 2025:
			return 85;

		case 2028:
			return 80;

		default:
			return 85;
		}
	}

	/**
	 * NPC Attacking Player
	 **/

	public void attackPlayer(final Client c, int i) {
		if (!goodDistance(npcs[i].getX(), npcs[i].getY(), c.getX(), c.getY(), 1) && npcs[i].npcType == 8349 && npcs[i].attackType == 0)
		 {
			 npcs[i].attackType = 1+Misc.random(1);
			 return;
		 }
		if (npcs[i].lastX != npcs[i].getX() || npcs[i].lastY != npcs[i].getY()) {
			return;
		}
		if(npcs[i].npcType == VorkathConstants.SLEEPING_VORKATH_ID) {
			return;
		}
		if (npcs[i].npcType == VorkathConstants.AWAKENED_VORKATH_ID) {
			if (c.getVorkath() != null) {
				c.getVorkath().handleAttack();
			}
		}
		int[] resetCombat = { 239, 319, 600, 2054, 2642, 5779, 5862, 6342, 6609, 6611, 6618, 6619, 6767 };
		
		if (npcs[i].resetTimer > 60 && Arrays.binarySearch(resetCombat, npcs[i].npcType) >= 0) {
			npcs[i].resetCombat();
		}
		

		if (npcs[i].npcType == 6720) {
			return;
		}
		if (!canAttack(i)) {
			return;
		}


		if (npcs[i] != null) {
			if (npcs[i].isDead)
				return;
			if (!npcs[i].inMulti() && npcs[i].underAttackBy > 0 && npcs[i].underAttackBy != c.index) {
				npcs[i].killerId = 0;
				return;
			}
			if (!npcs[i].inMulti() && (c.underAttackBy > 0 || (c.underAttackBy2 > 0 && c.underAttackBy2 != i))) {
				npcs[i].killerId = 0;
				return;
			}
			if (npcs[i].heightLevel != c.heightLevel) {
				npcs[i].killerId = 0;
				return;
			}

			if (npcs[i].npcType == 5869) {
				if (c.MELEE_ATTACK == 4 || c.MELEE_ATTACK == 6 || c.MELEE_ATTACK == 8) {
					c.MELEE_ATTACK += 2;
					c.RANDOM_MELEE++;
					return;
				}
			}

			if (npcs[i].npcType == 5868) {
				if (c.MAGIC_ATTACK == 4 || c.MAGIC_ATTACK == 6 || c.MAGIC_ATTACK == 8) {
					c.MELEE_ATTACK = 0;
					c.MAGIC_ATTACK += 2;
					c.RANDOM++;
					return;
				}
			}

			if (npcs[i].npcType == 5868) {
				if (c.MAGIC_ATTACK == 10 && c.RANGE_ATTACK == 1) {
					return;
				}
			}

			if (npcs[i].npcType == 5868) {
				if (c.MAGIC_ATTACK <= 3) {
					return;
				}
			}

			if (npcs[i].npcType == 5869) {
				if (c.MELEE_ATTACK == 12) {
					return;
				}
			}

			if (npcs[i].npcType == 5869) {
				if (c.MELEE_ATTACK <= 3) {
					return;
				}
			}

			if (npcs[i].npcType == 5867) {
				if (c.MAGIC_ATTACK >= 3 || c.MELEE_ATTACK >= 2) {
					return;
				}
			}

			if (npcs[i].npcType >= 1739 && npcs[i].npcType <= 1742 || npcs[i].npcType > 6600 && npcs[i].npcType <= 6602
					|| npcs[i].npcType == 1747) {
				npcs[i].killerId = 0;
				return;
			}

			if (Boundary.isIn(npcs[i], Boundary.GODWARS_BOSSROOMS)) {
				if (!Boundary.isIn(c, Boundary.GODWARS_BOSSROOMS)) {
					npcs[i].killerId = 0;
					return;
				}
			}
			npcs[i].face(c);
			double distance = distanceRequired(i);
			if (npcs[i].getSize() > 1)
				distance += 0.5;
			if (npcs[i].getDistance(c.getX(), c.getY()) > distance) {
				npcs[i].resetTimer++;
				return;
			}
				if (c.respawnTimer <= 0) {
					if(npcs[i].npcType == VorkathConstants.AWAKENED_VORKATH_ID) {
						return;
					}
					npcs[i].attackTimer = getNpcDelay(i);
					npcs[i].hitDelayTimer = getHitDelay(i);
					npcs[i].attackType = 0;
						loadSpell(c, i);
						if (npcs[i].attackType == 3) {
							npcs[i].hitDelayTimer += 2;
							c.getCombat().absorbDragonfireDamage();
						}
					if (multiAttacks(i)) {
						multiAttackGfx(i, npcs[i].projectileId);
						startAnimation(getAttackEmote(i), i);
						npcs[i].oldIndex = c.index;
						return;
					}
					if (npcs[i].projectileId > 0) {
						if(npcs[i].npcType == 7706) {
							NPC glyph = getNpc(7707,c.getHeight());
							if (glyph == null){
								return;
							}

							if (c.getInferno().isBehindGlyph()) {
								c.getInferno().behindGlyph=true;
								//c.sendMessage("is behind glyph");
							}
						}
						int nX = NPCHandler.npcs[i].getX() + offset(i);
						int nY = NPCHandler.npcs[i].getY() + offset(i);
						int pX = c.getX();
						int pY = c.getY();
						int offX = (nY - pY) * -1;
						int offY = (nX - pX) * -1;
						c.getPA().createPlayersProjectile(nX, nY, offX, offY,
								50, getProjectileSpeed(i),
								npcs[i].projectileId, 43, 31, -c.getId() - 1,
								65);
					}
					c.underAttackBy2 = i;
					c.singleCombatDelay2 = System.currentTimeMillis();
					npcs[i].oldIndex = c.index;
					startAnimation(getAttackEmote(i), i);
					c.getPA().removeAllWindows();

				}
			//}
		}
	}
	public static void kill(Client player, int npcType, int absX, int absY, int height) {
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				Arrays.asList(npcs).stream().filter(Objects::nonNull).filter(
						n -> n.npcType == npcType && n.absX == absX && n.absY == absY && n.heightLevel == height)
						.forEach(npc -> npc.isDead = true);
				container.stop();
			}
		}, 1);
	}
	public int offset(int i) {
		switch (npcs[i].npcType) {
		case 50:
			return 2;
		case 2881:
		case 2882:
			return 1;
		case 2745:
		case 2743:
			return 1;
		}
		return 0;
	}

	public boolean specialCase(Client c, int i) { // responsible for npcs that
													// much
		if (goodDistance(npcs[i].getX(), npcs[i].getY(), c.getX(), c.getY(), 8)
				&& !goodDistance(npcs[i].getX(), npcs[i].getY(), c.getX(),
						c.getY(), distanceRequired(i)))
			return true;
		return false;
	}

	public boolean retaliates(int npcType) {
		return npcType < 3777 || npcType > 3780
				&& !(npcType >= 2440 && npcType <= 2446 || npcType == VorkathConstants.AWAKENED_VORKATH_ID);
	}
	private boolean prayerProtectionIgnored(int npcId) {
		switch (npcs[npcId].npcType) {
		case 6610:
			return npcs[npcId].attackType == 2 || npcs[npcId].attackType == 4 ? true : false;
		case 6767:
			return true;
		case 5862:
			return true;
		case 465:
			return npcs[npcId].attackType == 3;
		case 6611:
		case 6612:
		case 6618:
		case 6619:
		case 963:
		case 965:
			return npcs[npcId].attackType == 2 ? true : false;
		case 6609:
			return npcs[npcId].attackType == 2 || npcs[npcId].attackType == 4 ? true : false;
		}
		return false;
	}
	public static NpcDefinition[] getNpcDef() {
		return NpcDefinition.DEFINITIONS;
	}
	public static NPCCacheDefinition[] getNpcCacheDef() {
		return NPCCacheDefinition.definitions;
	}

	public void applyDamage(int i) {
		if (npcs[i] != null) {
			if (PlayerHandler.players[npcs[i].oldIndex] == null) {
				return;
			}
			if (npcs[i].isDead)
				return;
			if (npcs[i].npcType >= 1739 && npcs[i].npcType <= 1742 || npcs[i].npcType == 1747) {
				return;
			}
			Client c = (Client) Server.playerHandler.players[npcs[i].oldIndex];
			if(npcs[i].npcType == 7706 && c.getInferno().behindGlyph){
				c.getInferno().behindGlyph = false;
				return;
			}
			if (multiAttacks(i)) {
				multiAttackDamage(i);
				return;
			}
			if (c.playerIndex <= 0 && c.npcIndex <= 0)
				if (c.autoRet == 1)
					c.npcIndex = i;
			if (c.attackTimer <= 3) {
				if (!NPCHandler.isFightCaveNpc(i)){
					c.animation(c.getCombat().getBlockEmote());
			}
			}
/*			if (c.getItems().isWearingItem(12931) || c.getItems().isWearingItem(13197)
					|| c.getItems().isWearingItem(13199)) {
				DamageEffect venom = new SerpentineHelmEffect();
				if (venom.isExecutable(c)) {
					venom.execute(c, npcs[i], new Damage(6));
				}
			}*/
			npcs[i].totalAttacks++;
			boolean protectionIgnored = prayerProtectionIgnored(i);
			if (c.respawnTimer <= 0) {
				int damage = 0;
				int secondDamage = -1;
				
				Optional<Brother> activeBrother = c.getBarrows().getActive();
				
				if (npcs[i].attackType == 0) {
					damage = Misc.random(getMaxHit(i));
					// damage = Misc.random(npcs[i].maxHit); //TODO
					// damage = Misc.random(npcs[i].maxHit);
					if (10 + Misc.random(c.getCombat().calculateMeleeDefence()) > Misc
							.random(NPCHandler.npcs[i].attack)) {
						damage = 0;
					}

					if (npcs[i].npcType == 2043 && c.getZulrahEvent().getNpc() != null && c.getZulrahEvent().getNpc().equals(npcs[i])) {
						Boundary boundary = new Boundary(npcs[i].targetedLocation.getX(), npcs[i].targetedLocation.getY(), npcs[i].targetedLocation.getX(),
								npcs[i].targetedLocation.getY());
						if (!Boundary.isIn(c, boundary)) {
							return;
						}
						damage = 20 + Misc.random(25);
					}
					/**
					 * Special attacks
					 */
					if (activeBrother.isPresent() && activeBrother.get().getId() == npcs[i].npcType) {
						double random = Math.random();
						if (random <= Barrows.SPECIAL_CHANCE) {
							switch (activeBrother.get().getId()) {
							case Brother.DHAROK:
								double healthRatio = Math.round(
										(npcs[i].HP / npcs[i].maximumHealth) * 10)
										/ 10d;
								healthRatio = Double.max(0.1, healthRatio);
								damage *= -2 * healthRatio + 3;
								break;
							case Brother.GUTHAN:
								int addedHealth = c.prayerActive[18] ? 0
										: Integer.min(damage,
												npcs[i].maximumHealth - npcs[i].HP);
								if (addedHealth > 0) {
									c.gfx0(398);
									npcs[i].HP += addedHealth;
								}
								break;

							case Brother.TORAG:
								c.gfx0(399);
								break;

							case Brother.VERAC:
								protectionIgnored = true;
								damage /= 2;
								break;

							}
						}
					}

					if (c.prayerActive[18] && !protectionIgnored) { // protect
																	// from
																	// melee
						if (npcs[i].npcType == 5890)
							damage /= 3;
						else if (npcs[i].npcType == 963 || npcs[i].npcType == 965 || npcs[i].npcType == 8349
								|| npcs[i].npcType == 8133 || npcs[i].npcType == 6342 || npcs[i].npcType == 2054
								|| npcs[i].npcType == 239 || npcs[i].npcType == 998 || npcs[i].npcType == 999
								|| npcs[i].npcType == 1000 || npcs[i].npcType == 7554 || npcs[i].npcType == 319
								|| npcs[i].npcType == 320 || npcs[i].npcType == 6615 || npcs[i].npcType == 5916
								|| npcs[i].npcType == 7544 || npcs[i].npcType == 5129 || npcs[i].npcType == 7792 
								|| npcs[i].npcType == 7793 || npcs[i].npcType == 7794 || npcs[i].npcType == 7795)
							damage = (damage / 2);
						else
							damage = 0;
					}
					if (c.playerEquipment[c.playerShield] == 12817) {
						if (Misc.random(100) > 30 && damage > 0) {
							damage *= .75;
						}
					}
/*
					if (npcs[i].npcType == 5867) {
						if (!c.prayerActive[17]) {
							Cerberus.GhostDamage(c);
						}
					}
					if (npcs[i].npcType == 5868) {
						if (!c.prayerActive[16]) {
							Cerberus.GhostDamage(c);
						}
					}
					if (npcs[i].npcType == 5869) {
						if (!c.prayerActive[18]) {
							Cerberus.GhostDamage(c);
						}
					}*/
					/*if (damage == 0 && npcs[i].npcType == 7153) {
						c.hits++;
					}*/

					if (npcs[i].npcType == 320) {
						if (npcs[i] != null) {
							npcs[319].HP += damage;
							npcs[320].animId = 1689;
							npcs[320].animUpdateRequired = true;
							npcs[i].updateRequired = true;
							c.sendMessage("The corporal beast has been healed: " + damage);
						}
					}

					if (c.vengOn && damage > 0) {
						c.getCombat().appendVengeanceNPC(i, damage);
					}

					if (c.playerLevel[3] - damage < 0) {
						damage = c.playerLevel[3];
					}
				} else if (npcs[i].attackType == 1) { // range
					damage = Misc.random(getMaxHit(i)); // TODO
					// damage = Misc.random(npcs[i].maxHit);
					if (10 + Misc.random(c.getCombat().calculateRangeDefence()) > Misc
							.random(NPCHandler.npcs[i].attack)) {
						damage = 0;
					}
					/**
					 * Special attacks
					 */
					if (activeBrother.isPresent() && activeBrother.get().getId() == npcs[i].npcType) {
						double random = Math.random();
						if (random <= Barrows.SPECIAL_CHANCE) {
							switch (activeBrother.get().getId()) {
							case Brother.KARIL:
								c.playerLevel[Config.AGILITY] = Integer.max(0,
										(int) (c.playerLevel[Config.AGILITY] * 0.8));
								c.getPA().refreshSkill(Config.AGILITY);
								c.gfx0(401);
								break;
							}
						}
					}
					if (c.prayerActive[17] && !protectionIgnored) { // protect
																	// from
																	// range
						if (npcs[i].npcType == 963 || npcs[i].npcType == 965 || npcs[i].npcType == 8349
								|| npcs[i].npcType == 8133 || npcs[i].npcType == 6342 || npcs[i].npcType == 2054
								|| npcs[i].npcType == 7554 || npcs[i].npcType == 239 || npcs[i].npcType == 319
								|| npcs[i].npcType == 499 || npcs[i].npcType == 7792 || npcs[i].npcType == 7793 
								|| npcs[i].npcType == 7794 || npcs[i].npcType == 7795) {
							damage = (damage / 2);
						} else {
							damage = 0;
						}
						if (c.playerLevel[3] - damage < 0) {
							damage = c.playerLevel[3];
						}
					}
					if (npcs[i].npcType == 2042 || npcs[i].npcType == 2044 || npcs[i].npcType == 320) {
						if (c.isSusceptibleToVenom()) {
							c.setVenomDamage((byte) 6);
						}
					}
					if (damage == 0 && npcs[i].npcType == 7151) {
						c.hits++;
					}
					if (npcs[i].endGfx > 0 && isFightCaveNpc(i)) {
						c.gfx100(npcs[i].endGfx);
					}
				} else if (npcs[i].attackType == 2) { // magic
					damage = Misc.random(getMaxHit(i)); // TODO
					// damage = Misc.random(npcs[i].maxHit);
					boolean magicFailed = false;
					if (npcs[i].npcType == 2205) {
						secondDamage = Misc.random(27);
					}
					if (10 + Misc.random(c.getCombat().mageDef()) > Misc.random(NPCHandler.npcs[i].attack)) {
						damage = 0;
						if (secondDamage > -1) {
							secondDamage = 0;
						}
						magicFailed = true;
					}
					if (npcs[i].npcType == 6609) {
						damage = Misc.random(getMaxHit(i));
						c.sendMessage("Callisto's fury sends an almighty shockwave through you.");
					}
					if (npcs[i].npcType == 2205 && npcs[i].attackType == 2) {
						damage = Misc.random(31);
					}
					if (npcs[i].npcType == 6342) {
						damage = Misc.random(35);
					}
					
					if (c.prayerActive[16] && !protectionIgnored) {
						switch (npcs[i].npcType) {
						case 1677:
						case 963:
						case 965:
						case 8349:
						case 8133:
						case 6342:
						case 2054:
						case 239:
						case 1046:
						case 319:
						case 7554:
						case 7604: // Skeletal mystic
						case 7605: // Skeletal mystic
						case 7606: // Skeletal mystic
						case 7617:
						case 4922:
							damage /= 2;
							break;
						}
						if (npcs[i].npcType == 494 || npcs[i].npcType == 5535 || npcs[i].npcType == 7101
								|| npcs[i].npcType == 7152 || npcs[i].npcType == 963 || npcs[i].npcType == 965
								|| npcs[i].npcType == 239 || npcs[i].npcType == 319 || npcs[i].npcType == 2205
								|| npcs[i].npcType == 2207 || npcs[i].npcType == 6609 || npcs[i].npcType == 6610
								|| npcs[i].npcType == 319 || npcs[i].npcType == 7792 || npcs[i].npcType == 7793 
								|| npcs[i].npcType == 7794 || npcs[i].npcType == 7795) {
							int max = npcs[i].npcType == 494 ? 2 : 0;
							if (Misc.random(2) == 0) {
								damage = 1 + Misc.random(max);
							} else {
								damage = 0; // TODO
								if (secondDamage > -1) {
									secondDamage = 0;
								}
							}
						} else if (npcs[i].npcType == 1677 || npcs[i].npcType == 1158 || npcs[i].npcType == 1160
								|| npcs[i].npcType == 8349 || npcs[i].npcType == 8133 || npcs[i].npcType == 2054
								|| npcs[i].npcType == 239 || npcs[i].npcType == 6528) {
							damage /= 2;
						} else {
							damage = 0;
							if (secondDamage > -1) {
								secondDamage = 0;
							}
							magicFailed = true;
						}
					}
					if (c.playerLevel[3] - damage < 0) {
						damage = c.playerLevel[3];
					}
					/*if (damage == 0 && npcs[i].npcType == 7152) {
						c.hits++;
					}*/
					if (npcs[i].endGfx > 0 && (!magicFailed || isFightCaveNpc(i))) {
						c.gfx100(npcs[i].endGfx);
					} else {
						c.gfx100(85);
					}
				} else if (npcs[i].attackType == 3 /*&& !c.hungerGames*/) { // fire
																		// breath
					int resistance = c.getItems().isWearingItem(1540) || c.getItems().isWearingItem(11283)
							|| c.getItems().isWearingItem(11284) ? 1 : 0;
					if (System.currentTimeMillis() - c.lastAntifirePotion < c.antifireDelay) {
						resistance++;
					}
					if (resistance == 0) {
						if (getNpcDef()[npcs[i].npcType].getName().contains("baby")) {
							damage = Misc.random(15);
						} else {
							damage = Misc.random(35);
						}
						c.sendMessage("You are badly burnt by the dragon fire!");
					} else if (resistance == 1) {
						damage = Misc.random(10);
					} else if (resistance == 2) {
						damage = 0;
					}
					if (npcs[i].endGfx != 430 && resistance == 2) {
						damage = 5 + Misc.random(5);
					}
					switch (npcs[i].endGfx) {
					case 429:
						if (c.isSusceptibleToPoison()) {
							c.setPoisonDamage((byte) 6);
						}
						break;

					case 428:
						c.freezeTimer = 10;
						break;

					case 431:
						c.lastSpear.reset();
						break;
					}
					if (c.playerLevel[3] - damage < 0)
						damage = c.playerLevel[3];
					c.gfx100(npcs[i].endGfx);
				} else if (npcs[i].attackType == 4) { // special attacks
					damage = Misc.random(getMaxHit(i));
					// damage = Misc.random(npcs[i].maxHit(i));
					switch (npcs[i].npcType) {
					case 3129:
						int prayerReduction = c.playerLevel[5] / 2;
						if (prayerReduction < 1) {
							break;
						}
						c.playerLevel[5] -= prayerReduction;
						if (c.playerLevel[5] < 0) {
							c.playerLevel[5] = 0;
						}
						c.getPA().refreshSkill(5);
						c.sendMessage(
								"K'ril Tsutsaroth slams through your protection prayer, leaving you feeling drained.");
						break;
					case 6528:
						prayerReduction = c.playerLevel[5] / 10;
						if (prayerReduction < 1) {
							break;
						}
						c.playerLevel[5] -= prayerReduction;
						if (c.playerLevel[5] < 0) {
							c.playerLevel[5] = 0;
						}
						c.getPA().refreshSkill(5);
						c.sendMessage("Your prayer has been drained drastically.");
						break;
					case 6610:
						if (c.prayerActive[16]) {
							damage *= .7;
						}
						secondDamage = Misc.random(getMaxHit(i));
						if (secondDamage > 0) {
							c.gfx0(80);
						}
						break;
					}
				}
				int poisonDamage = isPoisionous(npcs[i]);
				if (poisonDamage > 0 && c.isSusceptibleToPoison() && Misc.random(10) == 1) {
					c.setPoisonDamage((byte) poisonDamage);
				}
				if (c.playerLevel[3] - damage < 0 || secondDamage > -1 && c.playerLevel[3] - secondDamage < 0) {
					damage = c.playerLevel[3];
					if (secondDamage > -1) {
						secondDamage = 0;
					}
				}
				handleSpecialEffects(c, i, damage);
				c.logoutDelay.reset();
				if (damage > -1) {
					c.appendDamage(damage, damage > 0 ? Hitmark.HIT : Hitmark.MISS);
				}
				if (secondDamage > -1) {
					c.appendDamage(secondDamage, secondDamage > 0 ? Hitmark.HIT : Hitmark.MISS);
				}
				c.getCombat().applyRecoilNPC(damage + (secondDamage > 0 ? secondDamage : 0), i);
				//c.getCombat().applyDharokRecoil(damage + (secondDamage > 0 ? secondDamage : 0), i);
				/*if (c.playerLevel[3] <= 0 && npcs[i].npcType == 28) {
					c.setKilledByZombie(true);
				*/}
				c.getPA().refreshSkill(3);
				c.updateRequired = true;
			}
		}

	public void handleSpecialEffects(Client c, int i, int damage) {
		if (npcs[i].npcType == 2892 || npcs[i].npcType == 2894) {
			if (damage > 0) {
				if (c != null) {
					if (c.playerLevel[5] > 0) {
						c.playerLevel[5]--;
						c.getPA().refreshSkill(5);
						c.getPA().appendPoison(12);
					}
				}
			}
		}

	}

	private int isPoisionous(NPC npc) {
		switch (npc.npcType) {
		case 961:
			return 8;
		case 3129:
			return 16;
		case 2042:
		case 2043:
		case 2044:
			return 6;
		}
		return 0;
	}
	public static void startAnimation(int animId, int i) {
		npcs[i].animId = animId;
		npcs[i].animUpdateRequired = true;
		npcs[i].updateRequired = true;
	}


	public int getMaxHit(int i) {
		switch (npcs[i].npcType) {
		case 499:
			if (npcs[i].attackType == 2) {
				return 10;
			}
		case 239:
			return npcs[i].attackType == 3 ? 65 : 25;
		case 7275:
			return npcs[i].attackType == 3 ? 50 : 29;
case 7274:
			return npcs[i].attackType == 3 ? 50 : 26;
case 7273:
			return npcs[i].attackType == 3 ? 50 : 21;
		case 8349: 
		 case 8350: 
		 case 8351:
		 if (npcs[i].attackType == 0)
			 return 30;
		 else
			 if (npcs[i].attackType == 1)
				 return 33;
			 else
				 if (npcs[i].attackType == 2)
					 return 33;
		case 5779:
			return 32;
		case 963:
		case 965:
			return 37;
		case 5054:
			return 7;
			
		case 465:
			return npcs[i].attackType == 3 ? 55 : 13;
			
		case 6767:
			return npcs[i].attackType == 0 ? 31 : npcs[i].attackType == 1 ? 10 : 15;
		case Zulrah.SNAKELING:
			return 15;
		case 5862:
			return npcs[i].attackType == 0 ? 28 : npcs[i].attackType == 1 ? 30 : 38;
		case 5868:
		case 5867:
		case 5869:
			return 18;
		case 7101:
			return npcs[i].attackType == 2 ? 30 : 20;
		case 6345:
			if (npcs[i].attackType == 2) {
				return 30;
			}

		case 6619:
		case 6618:
			if (npcs[i].attackType == 2) {
				return 58;
			} else if (npcs[i].attackType == 1) {
				return 38;
			}
		case 2042:
		case 2043:
		case 2044:
			return 41;
		case 2054:
			return 50;
		case 320:
			return 20;
		case 6609:
			if (npcs[i].attackType == 0 || npcs[i].attackType == 2) {
				return 32;
			}
		case 6342:
			if (npcs[i].attackType == 0) {
				return 40;
			}

		case 494:
			// if (npcs[i].attackType == 2)
			// return 37;
			// else
			// return 33;
			return 37;
		case 5535:
			return 8;
		case 2208:
		case 2207:
		case 2206:
			return 16;
		case 3129:
			return npcs[i].attackType == 0 ? 60 : npcs[i].attackType == 4 ? 49 : 30;
		case 6611:
		case 6612:
			return 46;
		case 6613:
			return 7;
		case 6528:
			return npcs[i].attackType == 2 ? 40 : 50;
		case 6610:
			return 30;
		case 2558:
			return npcs[i].attackType == 2 ? 38 : 68;
		case 2562:
			return 31;
		case 6593:
			return 15;
		case 7795:
			if (npcs[i].attackType == 0) {
				return 10;
			} else if (npcs[i].attackType == 1) {
				return 16;
			} else if (npcs[i].attackType == 3)
				return 44;
		case 7794:
			if (npcs[i].attackType == 0) {
				return 9;
			} else if (npcs[i].attackType == 1) {
				return 13;
			} else if (npcs[i].attackType == 3)
				return 50;
		case 7793:
			if (npcs[i].attackType == 0) {
				return 10;
			} else if (npcs[i].attackType == 1) {
				return 12;
			} else if (npcs[i].attackType == 3)
				return 10;
		case 7792:
			if (npcs[i].attackType == 0) {
				return 13;
			} else if (npcs[i].attackType == 3)
				return 9;
		case 3162:
			if (npcs[i].attackType == 0) {
				return 26;
			} else if (npcs[i].attackType == 1) {
				return 71;
			} else if (npcs[i].attackType == 2)
				return 21;
		case 2215:
			if (npcs[i].attackType == 1)
				return 35;
			else
				return 60;
			/*
			 * case 3162: return npcs[i].attackType == 1 ? 71 :
			 * npcs[i].attackType == 2 ? 21 : 15;
			 */
		case 1158:
			if (npcs[i].attackType == 2)
				return 30;
			return 21;
		case 1160:
			if (npcs[i].attackType == 2 || npcs[i].attackType == 1)
				return 30;
			return 21;
		case 2205:
			if (npcs[i].attackType == 0) {
				return 31;
			} else if (npcs[i].attackType == 1) {
				return 31;
			}
		}
		return npcs[i].maxHit == 0 ? 1 : npcs[i].maxHit;
	}
	
	@SuppressWarnings("resource")
	public static boolean loadAutoSpawn(String FileName) {
		String line = "";
		String token = "";
		String token2 = "";
		String token2_2 = "";
		String[] token3 = new String[10];
		boolean EndOfFile = false;
		BufferedReader characterfile = null;
		try {
			characterfile = new BufferedReader(new FileReader("./" + FileName));
		} catch (FileNotFoundException fileex) {
			Misc.println(FileName + ": file not found.");
			return false;
		}
		try {
			line = characterfile.readLine();
		} catch (IOException ioexception) {
			Misc.println(FileName + ": error loading file.");
		}
		while (EndOfFile == false && line != null) {
			line = line.trim();
			int spot = line.indexOf("=");
			if (spot > -1) {
				token = line.substring(0, spot);
				token = token.trim();
				token2 = line.substring(spot + 1);
				token2 = token2.trim();
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token3 = token2_2.split("\t");
				if (token.equals("spawn")) {
					newNPC(Integer.parseInt(token3[0]),
							Integer.parseInt(token3[1]),
							Integer.parseInt(token3[2]),
							Integer.parseInt(token3[3]),
							Integer.parseInt(token3[4]),
							getNpcListHP(Integer.parseInt(token3[0])),
							Integer.parseInt(token3[5]),
							Integer.parseInt(token3[6]),
							Integer.parseInt(token3[7]));

				}
			} else {
				if (line.equals("[ENDOFSPAWNLIST]")) {
					try {
						characterfile.close();
					} catch (IOException ioexception) {
					}
					return true;
				}
			}
			try {
				line = characterfile.readLine();
			} catch (IOException ioexception1) {
				EndOfFile = true;
			}
		}
		try {
			characterfile.close();
		} catch (IOException ioexception) {
		}
		return false;
	}

	public static int getNpcListHP(int npcId) {
		if (npcId <= -1) {
			return 0;
		}
		if (NpcDefinition.DEFINITIONS[npcId] == null) {
			return 0;
		}
		return NpcDefinition.DEFINITIONS[npcId].getHitpoints();

	}

	public static String getNpcListName(int npcId) {
		if (npcId <= -1) {
			return "None";
		}
		if (NpcDefinition.DEFINITIONS[npcId] == null) {
			return "None";
		}
		return NpcDefinition.DEFINITIONS[npcId].getName();
	}

	/*public static boolean loadNPCList(String fileName) {
		String line = "";
		String token = "";
		String token2 = "";
		String[] token3 = new String[10];
		boolean EndOfFile = false;
		BufferedReader characterfile = null;
		try {
			characterfile = new BufferedReader(new FileReader("./" + fileName));
		} catch (FileNotFoundException fileex) {
			Misc.println(fileName + ": file not found.");
			return false;
		}
		try {
			line = characterfile.readLine();
		} catch (IOException ioexception) {
			Misc.println(fileName + ": error loading file.");
		}
		while (EndOfFile == false && line != null) {
			line = line.trim();
			int spot = line.indexOf("=");
			if (spot > -1) {
				token = line.substring(0, spot);
				token = token.trim();
				token2 = line.substring(spot + 1);
				token2 = token2.trim();
				token3 = token2.split("\t");
				int size = Integer.parseInt(token3[4]);
				if (token.equals("npc")) {
					NPCHandler.newNPCList(
							Integer.parseInt(token3[0]), 
							token3[1],
							Integer.parseInt(token3[2]),
							Integer.parseInt(token3[3]),
							size);
				}
			} else {
				if (line.equals("[ENDOFNPCLIST]")) {
					try {
						characterfile.close();
					} catch (IOException ioexception) {
					}
					return true;
				}
			}
			try {
				line = characterfile.readLine();
			} catch (IOException ioexception1) {
				EndOfFile = true;
			}
		}
		try {
			characterfile.close();
		} catch (IOException ioexception) {
		}
		return false;
	}*/

	public static int getBlockEmote(int i) {
		switch (NPCHandler.npcs[i].npcType) {
		case 7160:
			return 8792;
		case 7159:
			return 8793;
		case Skotizo.SKOTIZO_ID:
			return 4676;
		case 7585:
		case Skotizo.REANIMATED_DEMON:
			return 65;
		default:
			return 0x326;
		}
	}

	public void spawnNpc2(int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit, int attack, int defence) {
		// first, search for a free slot
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}
		if(slot == -1) {
			//Misc.println("No Free Slot");
			return;		// no free slot found
		}
		NPC newNPC = new NPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.HP = HP;
		newNPC.maximumHealth = HP;
		newNPC.maxHit = maxHit;
		newNPC.attack = attack;
		newNPC.defence = defence;
		npcs[slot] = newNPC;
	}
	
	
	public void spawnNpc3(Player c, int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit,
			int attack, int defence, boolean attackPlayer, boolean headIcon, boolean summonFollow) {
		// first, search for a free slot
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}
		if (slot == -1) {
			// Misc.println("No Free Slot");
			return; // no free slot found
		}
		NPC newNPC = new NPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.spawnedBy = c.getId();
		newNPC.underAttack = true;
		newNPC.face(c);
		if (headIcon)
			c.getPA().drawHeadicon(1, slot, 0, 0);
		if (summonFollow) {
			newNPC.summoner = true;
			newNPC.summonedBy = c.index;
			c.hasNpc = true;
		}
		if (attackPlayer) {
			newNPC.underAttack = true;
			if (c != null) {
				newNPC.killerId = c.index;
			}
		}
		npcs[slot] = newNPC;
	}
	public void stepAway(int i) {
		int[][] points = { { -1, -1 }, { 1, 1 }, { 1, -1 }, { -1, 1 } };

		for (int[] k : points) {
			int dir = NPCClipping.getDirection(k[0], k[1]);
			if (NPCDumbPathFinder.canMoveTo(npcs[i], dir)) {
				NPCDumbPathFinder.walkTowards(npcs[i], npcs[i].getX() + NPCClipping.DIR[dir][0],
						npcs[i].getY() + NPCClipping.DIR[dir][1]);
				break;
			}
		}
	}

	public int summonItemId(int itemId) {
		if(itemId == 1555) return 761;
		if(itemId == 1556) return 762;
		if(itemId == 1557) return 763;
		if(itemId == 1558) return 764;
		if(itemId == 1559) return 765;
		if(itemId == 1560) return 766;
		if(itemId == 1561) return 768;
		if(itemId == 1562) return 769;
		if(itemId == 1563) return 770;
		if(itemId == 1564) return 771;
		if(itemId == 1565) return 772;
		if(itemId == 1566) return 773;
		if(itemId == 7585) return 3507;
		if(itemId == 7584) return 3506;
		if(itemId == 7583) return 3505;
		if(itemId == 12500) return 4000; //prince
		if(itemId == 11995) return 8599; //ele
		if(itemId == 12502) return 4002; //bandos
		if(itemId == 12503) return 4003; //arma
		if(itemId == 12504) return 4004; //zammy
		if(itemId == 12505) return 4005; //sara
		if(itemId == 12506) return 4006; //dag sup
		if(itemId == 12507) return 4007; //dag prime
		if(itemId == 12508) return 4008; //dag rex
		return 0;
	}

	public static void kill(int npcType, int height) {
		Arrays.asList(npcs).stream().filter(Objects::nonNull)
				.filter(n -> n.npcType == npcType && n.heightLevel == height).forEach(npc -> npc.isDead = true);
	}

	public static NPC getNpc(int npcType, int x, int y, int height) {
		for (NPC npc : npcs) {
			if (npc != null && npc.npcType == npcType && npc.absX == x && npc.absY == y && npc.heightLevel == height) {
				return npc;
			}
		}
		return null;
	}

	public static NPC getNpc(int npcType, int height) {
		for (NPC npc : npcs) {
			if (npc != null && npc.npcType == npcType && npc.heightLevel == height) {
				return npc;
			}
		}
		return null;
	}

	public boolean isUndead(int index) {
		String name = getNpcListName(npcs[index].npcType);
		for (String s : Config.UNDEAD)
			if (s.equalsIgnoreCase(name))
				return true;
		return false;
	}

	public static NPC spawn(int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit, int attack, int defence, boolean attackPlayer) {
		// first, search for a free slot
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}
		if (slot == -1) {
			System.out.println("Cannot find any available slots to spawn npc into + npchandler @spawnNpc - line 2287");
			return null;
		}
		NPC newNPC = new NPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.HP = HP;
		newNPC.maximumHealth = HP;
		newNPC.maxHit = maxHit;
		newNPC.attack = attack;
		newNPC.defence = defence;
		newNPC.needRespawn = false;
		npcs[slot] = newNPC;
		return newNPC;
	}	
	private void infernoDeathHandler(Client player, int i) {// hold a vit plz
		if (npcs[i] != null) {
			if (player != null) {
				if (player.getInfernoMinigame() != null) {
					if (isInfernoNpc(i))
						killedInferno(player, i);
				}
			}
		}
	}
	private void killedInferno(Client player, int i) {
		if (player.getInfernoMinigame() != null) {
			player.getInfernoMinigame().setKillsRemaining(player.getInfernoMinigame().getKillsRemaining() - 1);
			if (player.getInfernoMinigame().getKillsRemaining() == 0) {
				player.infernoWaveId++;
				System.out.println("Inferno Wave ID: "+player.infernoWaveId);
				player.getInfernoMinigame().spawn();
			}
		}
	}
	

	public static boolean isInfernoNpc(int i) {
		if (npcs[i] == null)
			return false;
		switch (npcs[i].npcType) {

		case InfernoWave.JAL_NIB:
		case InfernoWave.JAL_MEJRAH: 
		case InfernoWave.JAL_AK: 
		case InfernoWave.JAL_AKREK_MEJ:
		case InfernoWave.JAL_AKREK_XIL:
		case InfernoWave.JAL_AKREK_KET:
		case InfernoWave.JAL_IMKOT: 
		case InfernoWave.JAL_XIL:
		case InfernoWave.JAL_ZEK:
		case InfernoWave.JALTOK_JAD: 
		case InfernoWave.YT_HURKOT:
		case InfernoWave.TZKAL_ZUK:
		case InfernoWave.ANCESTRAL_GLYPH:
		case InfernoWave.JAL_MEJJAK:
			
		
			return true;
		}
		return false;
	}
	

}
