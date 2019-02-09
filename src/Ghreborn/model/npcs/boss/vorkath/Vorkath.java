package Ghreborn.model.npcs.boss.vorkath;

import Ghreborn.Server;
import Ghreborn.core.GameEngine;
import Ghreborn.core.World;
import Ghreborn.core.task.Task;
import Ghreborn.event.CycleEvent;
import Ghreborn.event.CycleEventContainer;
import Ghreborn.event.CycleEventHandler;
import Ghreborn.model.content.teleport.Position;
import Ghreborn.model.npcs.NPC;
import Ghreborn.model.npcs.NPCDumbPathFinder;
import Ghreborn.model.npcs.boss.instances.InstancedAreaManager;
import Ghreborn.model.npcs.boss.instances.SingleInstancedArea;
import Ghreborn.model.npcs.boss.instances.impl.SingleInstancedVorkath;
import Ghreborn.model.npcs.boss.vorkath.impl.DeathStage;
import Ghreborn.model.npcs.boss.vorkath.impl.SpawnStage;
import Ghreborn.model.npcs.boss.vorkath.impl.WakeUpStage;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.combat.Hitmark;
import Ghreborn.util.Misc;
import Ghreborn.world.objects.GlobalObject;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Yasin
 */
public class Vorkath {

    private final Client player;

    private AttackType specialAttackType;

    private AttackType attackType;

    private VorkathState vorkathState;

    private final Object eventLock;

    private int attacks;

    private NPC vorkath;

    private boolean zombieSpawned;
    

    private boolean forceDeath;

    private SingleInstancedVorkath vorkathInstance;

    public Vorkath(Client player) {
        this.player = player;
        this.specialAttackType = Arrays.stream(AttackType.values()).filter(type -> type.name().toLowerCase().contains("special")).collect(Collectors.toList()).get(Misc.random(1));//only 0 or 1
        this.vorkathState = VorkathState.SLEEPING;
        this.eventLock = new Object();
    }

    public void start() {
        if (vorkathInstance != null) {
            disposeInstance();
            //player.sendMessage("Vorkath instance is not null, fyi! Disposing..");
        }
        int height = InstancedAreaManager.getSingleton().getNextOpenHeight(VorkathConstants.VORKATH_BOUNDARY);
        vorkathInstance = new SingleInstancedVorkath(player, VorkathConstants.VORKATH_BOUNDARY, height);
        InstancedAreaManager.getSingleton().add(height, vorkathInstance);
        if (vorkathInstance == null) {
            player.getDH().sendStatement("Vorkath can't be fought right now.", "Please try again shortly.");
            player.nextChat = -1;
            return;
        }
        CycleEventHandler.getSingleton().addEvent(eventLock, new SpawnStage(this, player), 1);
    }

    public void disposeInstance() {
        forceDeath = true;
        if (vorkath != null) {
            setVorkathState(VorkathState.SLEEPING);
            Server.npcHandler.kill(vorkath.npcType, vorkathInstance.getHeight());
        }
        attacks = 0;
        InstancedAreaManager.getSingleton().disposeOf(vorkathInstance);
        Server.getGlobalObjects().remove(32000, vorkathInstance.getHeight());
        vorkathInstance = null;
        forceDeath = false;
    }

    public void resetCombat() {
    	if (vorkath == null) {
    		return;
    	}
        player.getCombat().resetPlayerAttack();
        vorkath.underAttack = false;
        vorkath.face = 0;
        if (vorkathState == VorkathState.RESTING) {
            vorkath.attackTimer += 1;
        }
        vorkath.underAttackBy = 0;
    }


    public boolean canSpecial() {
        return attacks % 7 == 0 && attacks > 0;
    }

    public void wakeUp() {
        if(vorkathState == VorkathState.AWAKE) {
            return;
        }
        if (vorkathState != VorkathState.RESTING) { //just to make sure
            vorkathState = VorkathState.AWAKE;
            CycleEventHandler.getSingleton().addEvent(eventLock, new WakeUpStage(this, player, false), 1);
        }
        if (vorkathState == VorkathState.RESTING) { //just to make sure
            vorkathState = VorkathState.AWAKE;
            CycleEventHandler.getSingleton().addEvent(eventLock, new WakeUpStage(this, player, true), 1);
        }
    }

    public void handleAttack() {
        if (player != null && vorkath != null && !vorkath.isDead && !player.isDead && !zombieSpawned) {
            int distance = player.distanceToPoint(vorkath.getX(), vorkath.getY());
            if (vorkath.actionTimer > 0) {
                return;
            }
            if(distance >= 15) {
                return;
            }
            if(distance <= 3) {
                vorkath.actionTimer += 2;
            }
            vorkath.actionTimer += 5;
            attacks += 1;
            if (canSpecial()) { //Every 7 attacks
                specialAttackType = specialAttackType == AttackType.SPECIAL_1 ? AttackType.SPECIAL_2 : AttackType.SPECIAL_1;
                vorkath.animation(specialAttackType.getAnimationId());
                if (specialAttackType == AttackType.SPECIAL_1) { //acid
                    CycleEventHandler.getSingleton().addEvent(eventLock, handleAcidSpecial(), 1);
                    vorkath.actionTimer += 22;
                } else if (specialAttackType == AttackType.SPECIAL_2) { //jihad
                    fireTargetedProjectile(specialAttackType.getProjectileId());
                    CycleEventHandler.getSingleton().addEvent(eventLock, handleJihadSpecial(), 1);
                    player.stopMovement();
                    vorkath.actionTimer += 7;
                }
            } else {
                attackType = Arrays.stream(AttackType.values()).filter(type ->
                        !type.name().toLowerCase().contains("special")).
                        collect(Collectors.toList()).get(Misc.random(5));
                vorkath.actionTimer += 1;
                vorkath.animation(attackType.getAnimationId());
                if (attackType != AttackType.ONE_HIT) {
                    CycleEventHandler.getSingleton().addEvent(eventLock, handleAttackType(), 1);
                    fireTargetedProjectile(attackType.getProjectileId());
                } else {
                    CycleEventHandler.getSingleton().addEvent(eventLock, handleOneHit(), 1);
                    fireOneshit(attackType.getProjectileId(), 110, player.getX(), player.getY(),
                            50, 50); //50 -> current angle, 50 -> current start height

                }
            }
        }
    }

    public void jump() {
        CycleEventHandler.getSingleton().addEvent(eventLock, new CycleEvent() {
            boolean north;

            @Override
            public void execute(CycleEventContainer container) {
                if (container.getOwner() == null || player == null || player.isDead || player.animId != -1) {
                    container.stop();
                    return;
                }
                int cycle = container.getTotalTicks();
                if (cycle == 1) {
                    if (player.getX() <= 2273 && player.getX() >= 2271 && player.getY() == 4052) {
                        north = true;
                    }
                    player.animation(1115);
                    player.sendMessage("You jump over the ice bricks..");
                }
                if (cycle == 3) {
                    player.stopAnimation();
                    player.sendMessage("and reach the other side..");
                    if(north && vorkathInstance == null) {
                        start();
                    }
                    if(vorkathInstance == null) { //height will be set in spawnstage
                        player.getPA().movePlayer(player.getX(), north ? player.getY() + 2 : player.getY() - 2);
                    } else { //so the player can continue
                        player.getPA().movePlayer(player.getX(), north ? player.getY() + 2 : player.getY() - 2, vorkathInstance.getHeight());
                    }
                    container.stop();
                }

            }
        }, 1);
    }

    private CycleEvent handleJihadSpecial() {
        return new CycleEvent() {
            int SPAWN_X;
            int SPAWN_Y;
            NPC spawn;

            private void killSpawn(boolean explode) {
                spawn.gfx0(542);
                zombieSpawned = false;
                spawn.needRespawn = false;
                spawn.isDead = true;
                if(explode) {
                    player.appendDamage(Misc.random(60) + 10, Hitmark.HIT);
                }
                player.freezeTimer = 0;
            }
            @Override
            public void execute(CycleEventContainer container) {
                if (container.getOwner() == null || vorkath == null || player == null || player.isDead || vorkathInstance == null) {
                    container.stop();
                    return;
                }
                int cycle = container.getTotalTicks();
                if (cycle == 4) {
                    player.gfx0(specialAttackType.getEndGfx());
                    player.freezeTimer = 500;
                    player.sendMessage("You've been frozen.");
                }
                if (cycle == 5) {
                    SPAWN_X = vorkath.getX() + Misc.random(7) + 3;
                    SPAWN_Y = vorkath.getY() - 2;
                    player.sendMessage("The dragon throws a creature towards you..");
                    zombieSpawned = true;
                    fireProjectileToLocation(1484, 130, SPAWN_X, SPAWN_Y, 50);
                }
                if (cycle == 9) {
                    spawn = Server.npcHandler.spawnNpc(player, VorkathConstants.ZOMBIE_SPAWN, SPAWN_X, SPAWN_Y, vorkathInstance.getHeight(), 1, VorkathConstants.ZOMBIE_SPAWN_LIFE_POINTS, 1, 1, 1, false, false);
                }
                if (cycle >= 10) {
                    int distance = player.distanceToPoint(spawn.getX(), spawn.getY());
                    if(zombieSpawned && spawn.isDead) {
                        killSpawn(false);
                        container.stop();
                    }
                    if (distance <= 1 && zombieSpawned) {
                        killSpawn(true);
                        container.stop();
                    }
                }
                if (zombieSpawned && cycle >= 10) {
                    if (spawn.getX() != player.absX - 1 && spawn.getY() != player.absY - 1) {
                        NPCDumbPathFinder.walkTowards(spawn, player.getX(), player.getY());
                    }
                }

                if (!zombieSpawned && cycle >= 20) {
                    container.stop();
                }


                if (zombieSpawned && cycle >= 20 && player.distanceToPoint(spawn.getX(), spawn.getY()) >= 5) { 
                    if (player.distanceToPoint(spawn.getX(), spawn.getY()) < 40) { 
                       killSpawn(false);
                        player.sendMessage("The spawn lost its orientation and blew up..");
                    }
                }
            }
        };
    }

    private CycleEvent handleAcidSpecial() {
        return new CycleEvent() {
            int x;
            int y;

            @Override
            public void execute(CycleEventContainer container) {
                if (container.getOwner() == null || vorkath == null || player == null || player.isDead || vorkathInstance == null) {
                    container.stop();
                    return;
                }
                int cycle = container.getTotalTicks();
                if(Server.getGlobalObjects().exists(32000, player.getX(), player.getY(), player.getHeight())) {
                    int randomDamage = Misc.random(10) + 7;
                    vorkath.HP += randomDamage;
                    vorkath.updateRequired = true;
                    player.appendDamage(randomDamage, Hitmark.HIT);
                    player.sendMessage("You step on the acid and take some damage");
                }
                if (cycle == 1) {
                    int minX = VorkathConstants.VORKATH_BOUNDARY.getMinimumX();
                    int maxX = VorkathConstants.VORKATH_BOUNDARY.getMaximumX();
                    int minY = 4054; //it's bugged in the boundaries
                    int maxY = VorkathConstants.VORKATH_BOUNDARY.getMaximumY();
                    for (int i = 0; i < 40; i++) {
                        int randomX = minX + Misc.random(maxX - minX);
                        int randomY = minY + Misc.random(maxY - minY);
                        if ((randomX <= 2276 && randomX >= 2268 && randomY <= 4069 && randomY >= 4061)) {
                            continue;
                        }
                        fireProjectileToLocation(1486, 100,
                                randomX,
                                randomY, 60);
                        CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
                            @Override
							public void execute(CycleEventContainer container) {
                            	Server.getGlobalObjects().add(new GlobalObject(32000, randomX, randomY, vorkathInstance.getHeight(), Misc.random(3) + 1, 10, -1, -1));
								
							}
                        }, 5);
                    }
                }
                if (cycle >= 3 && cycle <= 25) {
                	if (cycle >= 5) {
                     	if (x == player.getX() && y == player.getY()) {
                          	player.appendDamage(30, Hitmark.HIT);
                      	}
                    }
                    x = player.getX();
                    y = player.getY();
                    fireProjectileToLocation(1482, 95, x, y, 35);
                    player.getPA().stillGfx(131, x, y, 15, 95);
                    fireProjectileToLocation(1482, 70, x, y, 35);
                    player.getPA().stillGfx(131, x, y, 15, 70);
                }
                if (cycle == 30) {
                    Server.getGlobalObjects().remove(32000, vorkathInstance.getHeight());
                    container.stop();
                }
            }
        };
    }

    private CycleEvent handleOneHit() {
        return new CycleEvent() {
            Position arrival;

            @Override
            public void execute(CycleEventContainer container) {
                if (container.getOwner() == null || vorkath == null || player == null || player.isDead || vorkathInstance == null) {
                    container.stop();
                    return;
                }
                int cycle = container.getTotalTicks();
                if (cycle == 0) {
                    arrival = new Position(player.getX(), player.getY());
                }
                if (cycle == 5) {
                    int arrivalX = arrival.getX();
                    int arrivalY = arrival.getY();
                    int playerX = player.getX();
                    int playerY = player.getY();
                    if (playerX == arrivalX && playerY == arrivalY) {
                        applyRandomDamage(player.getMaximumHealth());
                        player.getPA().stillGfx(attackType.getEndGfx(), arrival.getX(), arrival.getY(), 100, 0);
                    } if(playerX == (arrivalX + 1)
                            || playerX == (arrivalX - 1)
                            || playerY == (arrivalY + 1)
                            || arrivalY == (arrivalY - 1)) {
                        applyRandomDamage(player.getMaximumHealth() / 2);
                        player.getPA().stillGfx(attackType.getEndGfx(), arrivalX, arrivalY, 100, 0);
                    } else {
                        player.getPA().stillGfx(attackType.getEndGfx(), arrivalX, arrivalY, 20, 0);
                    }
                    container.stop();
                }
            }
        };
    }

    private void fireTargetedProjectile(int projectileId) {
        int offY = (vorkath.getX() - player.getX()) * -1;
        int offX = (vorkath.getY() - player.getY()) * -1;
        int delay = 0;
        player.getPA().createPlayersProjectile(vorkath.getX() + 3, vorkath.getY(), offX, offY, 50, 110, projectileId, 35, 31, 65, delay);
    }

    private void fireProjectileToLocation(int projectileId, int projectileSpeed, int targetX, int targetY, int startHeight) {
        int offY = (vorkath.getX() - targetX) * -1;
        int offX = (vorkath.getY() - targetY) * -1;
        int delay = 0;
        player.getPA().createPlayersProjectile(vorkath.getX() + 3, vorkath.getY(), offX, offY - 3, 50, projectileSpeed, projectileId, startHeight, 31, 65, delay);
    }

    private void fireOneshit(int projectileId, int projectileSpeed, int targetX, int targetY, int angle, int startHeight) {
        int offY = (vorkath.getX() - targetX) * -1;
        int offX = (vorkath.getY() - targetY) * -1;
        int delay = 0;
        player.getPA().createPlayersProjectile(vorkath.getX() + 3, vorkath.getY(), offX, offY - 3, 50, projectileSpeed, projectileId, startHeight, 31, 65, delay);
    }


    private CycleEvent handleAttackType() {
        return new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
                if (container.getOwner() == null || vorkath == null || player == null || player.isDead || vorkathInstance == null) {
                    container.stop();
                    return;
                }
                int cycle = container.getTotalTicks();
                //player.getPA().stillGfx(attackType.getEndGfx(), player.getX(), player.getY(), 100, 110);
                if (cycle == 4) {
                    handleEffect();
                }
                if (cycle == 5) {
                    player.getPA().stillGfx(attackType.getEndGfx(), player.getX(), player.getY(), 100, 0);
                    container.stop();
                }
            }
        };
    }

    private void applyRandomDamage(int amount) {
        player.appendDamage(Misc.random(amount) + 1, Hitmark.HIT);
    }

    private void handleEffect() {
        switch (attackType) {
            case MAGIC:
                if (player.prayerActive[16]) {
                    player.appendDamage(0, Hitmark.MISS);
                    return;
                } else {
                    applyRandomDamage(35);
                }
                break;
            case POISON:
                applyRandomDamage(3);
               // player.getHealth().proposeStatus(HealthStatus.POISON, Misc.random(12), Optional.of(vorkath));
                break;
            case RANGED:
                if (player.prayerActive[17]) {
                    player.appendDamage(0, Hitmark.MISS);
                    return;
                } else {
                    applyRandomDamage(30);
                }
                break;
            case DRAGON_FIRE:
                boolean isResistent =
                        player.getItems().isWearingItem(1540)
                                || player.getItems().isWearingItem(11283)
                                || player.getItems().isWearingItem(11284)
                                || (System.currentTimeMillis() - player.lastAntifirePotion < player.antifireDelay);
                if (isResistent) {
                    player.sendMessage("Your resistance reflects the dragons fire!");
                    player.appendDamage(0, Hitmark.MISS);
                    return;
                } else {
                    applyRandomDamage(35);
                    player.sendMessage("You got horribly burned by the dragons fire.");
                }
                break;
            case PRAYER_SNIPE:
                for (int i = 0; i < player.prayerActive.length - 1; i++) {
                    if (!player.prayerActive[i])
                        continue;
                    player.prayerActive[i] = false;
                    player.getPA().sendFrame36(player.PRAYER_GLOW[i], 0);
                }
                player.headIcon = -1;
                player.getPA().requestUpdates();
                applyRandomDamage(3);
                break;
		default:
			break;
        }
    }


    public void handleDeath() {
        vorkathState = VorkathState.RESTING;
        CycleEventHandler.getSingleton().addEvent(eventLock, new DeathStage(this, player), 1);
    }

    public void setVorkathInstance(SingleInstancedVorkath vorkathInstance) {
        this.vorkathInstance = vorkathInstance;
    }

    public AttackType getAttackType() {
        return attackType;
    }

    public VorkathState getVorkathState() {
        return vorkathState;
    }

    public SingleInstancedArea getVorkathInstance() {
        return vorkathInstance;
    }

    public Object getEventLock() {
        return eventLock;
    }

    public AttackType getSpecialAttackType() {
        return specialAttackType;
    }

    public void setVorkathState(VorkathState vorkathState) {
        this.vorkathState = vorkathState;
    }

    public NPC getNpc() {
        return vorkath;
    }

    public void setVorkath(NPC vorkath) {
        this.vorkath = vorkath;
    }

    public int getAttacks() {
        return attacks;
    }

    public void setAttacks(int attacks) {
        this.attacks = attacks;
    }

    public boolean isForceDeath() {
        return forceDeath;
    }

    public void setForceDeath(boolean forceDeath) {
        this.forceDeath = forceDeath;
    }

    public boolean isZombieSpawned() {
    	return zombieSpawned;
    }
}
