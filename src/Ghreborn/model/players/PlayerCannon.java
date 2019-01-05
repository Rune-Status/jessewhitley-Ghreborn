package Ghreborn.model.players;

import java.util.ArrayList;
import java.util.List;

import Ghreborn.Server;
import Ghreborn.clip.PathChecker;
import Ghreborn.clip.Region;
import Ghreborn.core.PlayerHandler;
import Ghreborn.event.CycleEvent;
import Ghreborn.event.CycleEventContainer;
import Ghreborn.event.CycleEventHandler;
import Ghreborn.model.npcs.NPC;
import Ghreborn.model.npcs.NPCHandler;
import Ghreborn.model.npcs.NpcDefinition;
import Ghreborn.util.Misc;
import Ghreborn.world.objects.GlobalObject;

public class PlayerCannon {
	
	final int MAX_HIT = 24;
	final int RANGE = 8;
	final int TARGETS = 6;
	
	public PlayerCannon(Client c) {
		this.c = c;
		name = c.playerName;
		hitNpcs = new ArrayList<Integer>();
	}
	
	public PlayerCannon setUpCannon() {
		if(c!= null && c.getItems().playerHasItem(CannonPart.BASE.getId())) {
			c.animation(827);
			obj = new GlobalObject(CannonPart.BASE.getObj(), c.absX, c.absY, c.heightLevel, 0, 10, -1);
			Region.addWorldObject(CannonPart.BASE.getObj(), obj.getX(), obj.getY(), obj.getHeight(), obj.getFace());
			c.sendMessage("You set up your cannon.");
			phase = CannonPart.BASE;
			c.getItems().deleteItem(phase.getId(), 1);
			c.canUsePackets = false;
			
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if(c == null || c.playerCannon == null || c.disconnected || c.isDead) {
						container.stop();
						return;
					}	
					
					switch(phase) {
						
					case BASE:
					case STAND:
					case BARREL:
						if(c.getItems().playerHasItem(CannonPart.values()[phase.ordinal() + 1].getId())) {
							phase = CannonPart.values()[phase.ordinal() + 1];
							c.getItems().deleteItem(phase.getId(), 1);
							c.animation(827);
 							//obj.updateType(phase.getObj());
 							PlayerHandler.updateCannon(c.playerCannon);
						} else {
							container.stop();
						}
						break;

					case FURNACE:
						container.stop();
						break;
					
					}
					
				}
				
				@Override
				public void stop() {
					c.canUsePackets = true;
					super.stop();
				}
				
			}, 3);
		}
		return this;
	}
	
	public boolean pickUpCannon(int obj, int x, int y) {
		if(c == null)
			return false;
		else if(obj != phase.getObj() || getX() != x || getY() != y || c.heightLevel != getHeight()) {
			c.sendMessage("This is not your cannon!");
			return false;
		} else if(c.getItems().freeSlots() < (phase.ordinal() + ((ammo > 0 && !c.getItems().playerHasItem(CANNONBALL)) ? 1 : 0))) {
			c.sendMessage("You do not have enough inventory space to pickup this cannon!");

			return false;
		}
		
		
		for(int i = 1; i <= phase.ordinal(); i++) {
			c.getItems().addItem(CannonPart.values()[i].getId(), 1);
		}
		if(ammo != 0)
			c.getItems().addItem(CANNONBALL, ammo);
		phase = CannonPart.NONE;
		firing = false;
		c.sendMessage("You pickup your cannon.");
		c.animation(827);
		
		return true;
	}
	
	public boolean addItemToCannon(int itemId, int obj, int x, int y) {
		if(c == null)
			return false;
		else if(obj != phase.getObj() || getX() != x || getY() != y || c.heightLevel != getHeight()) {
			c.sendMessage("This is not your cannon!");
			return false;
		} 
		
		if(itemId == CANNONBALL) {
			if(phase == CannonPart.FURNACE) {
				if(ammo >= 30) {
					c.sendMessage("Your cannon is already at full ammo!");
				} else {
					int load = Math.min(c.getItems().getItemCount(CANNONBALL), 30 - ammo);
					if(load > 0) {
						c.sendMessage(ammo == 30 ? "You fill your cannon up with " + load + " cannonballs." :"You add " + load + " cannonballs to your cannon.");
						ammo += load;
						c.getItems().deleteItem2(CANNONBALL, load);
					}
				}
			} else {
				c.sendMessage("Your cannon is still missing some parts!");
			}
		} else if(phase != CannonPart.FURNACE) {
			if(itemId == CannonPart.values()[phase.ordinal() + 1].getId()) {
				phase = CannonPart.values()[phase.ordinal() + 1];
				c.sendMessage("You add a part to your cannon.");
				c.animation(827);
				c.getItems().deleteItem(phase.getId(), 1);
				return true;
			} else {
				c.sendMessage("This is not the right part!");
			}
		}
		
		return false;
	}
	
	public void fireCannon() {
		if(ammo <= 0) {
			if(c.getItems().playerHasItem(CANNONBALL)) {
				int load = Math.min(c.getItems().getItemCount(CANNONBALL), 30 - ammo);
				if(load > 0) {
					c.sendMessage(ammo == 30 ? "You fill your cannon up with " + load + " cannonballs." :"You add " + load + " cannonballs to your cannon.");
					ammo += load;
					c.getItems().deleteItem2(CANNONBALL, load);
				}
			} else {
				c.sendMessage("Your cannon has no ammo!");
				return;
			}
		}
		if(firing) {
			c.sendMessage("Your cannon is already firing!");
			return;
		}
		firing = true;
		hits = 1;
		if(c.inMulti()) {
			hits = TARGETS;
		}
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if(c == null || c.playerCannon == null || c.disconnected || c.isDead || ammo == 0 || !firing) {
					if(ammo == 0 && c!= null)
						c.sendMessage("Your cannon has run out of ammo!");
					firing = false;
					container.stop();
					return;
				}
				
				if(c.distanceToPoint(getX(), getY()) > 25) {
					c.sendMessage("You have moved too far away from your cannon!");
					firing = false;
					container.stop();
					return;
				}
			
				for(int i = 0; i < NPCHandler.npcs.length; i++) {
					if(hits == 0)
						break;
					if(NPCHandler.npcs[i] == null || NPCHandler.npcs[i].isDead || NPCHandler.npcs[i].heightLevel != getHeight() || NPCHandler.npcs[i].HP == 0 || hitNpcs.contains(i)
							|| NPCHandler.npcs[i].maximumHealth == 0 || !NpcDefinition.DEFINITIONS[NPCHandler.npcs[i].npcType].isAttackable())
						continue;
					if(!NPCHandler.npcs[i].inMulti() && (NPCHandler.npcs[i].underAttackBy > 0 && NPCHandler.npcs[i].underAttackBy != c.index) 
							|| (c.underAttackBy > 0)  || (c.underAttackBy2 > 0 && c.underAttackBy2 != i))
						continue;
					if(correctQuadrant(orientation, NPCHandler.npcs[i].absX, NPCHandler.npcs[i].absY)
							&& c.WithinDistance(getX(), getY(), NPCHandler.npcs[i].absX, NPCHandler.npcs[i].absY, RANGE) 
							&& PathChecker.isProjectilePathClear(NPCHandler.npcs[i].absX, NPCHandler.npcs[i].absY, getHeight(), getX(), getY())) {
						
						fireAtNPC(NPCHandler.npcs[i]);
						hitNpcs.add(i);
						
						ammo--;
						hits--;
					}
				}
				
				if(orientation == Orientation.NORTHWEST) {
					orientation = Orientation.NORTH;
					hits = 1;
					if(c.inMulti()) {
						hits = TARGETS;
					}
					hitNpcs.clear();
				} else 
					orientation = Orientation.values()[orientation.ordinal() + 1];
				
				PlayerHandler.updateCannonOrientation(c.playerCannon, orientation.getAnim());
				
			}
		}, 1);
	}
	
	public void fireAtNPC(NPC npc) {
		int damage = Misc.random(MAX_HIT);
		if(damage > npc.HP)
			damage = npc.HP;
		
		npc.hitDiff = damage;
		npc.HP -= damage;
		npc.hitUpdateRequired = true;
		npc.faceUpdateRequired = true;
		npc.killerId = c.playerIndex;
		npc.face(c);
		npc.updateRequired = true;
		
		npc.underAttackBy = c.index;
		npc.underAttack = true;
		npc.lastDamageTaken = System.currentTimeMillis();
		c.lastNpcAttacked = npc.index;

		if(Server.npcHandler.getsPulled(npc.index))
			npc.killerId = c.index;

		
		int oX = getX()+1;
        int oY = getY()+1;
        int offX = ((oX - npc.absX) * -1);
        int offY = ((oY - npc.absY) * -1);
 
        c.getPA().createPlayersProjectile(oX, oY, offY, offX, 50, 70, 53, 30, 20, -c.getId() + 1, 30);
		c.getPA().addSkillXP(damage * 2, c.playerRanged);
	}
	
	public boolean correctQuadrant(Orientation orientation, int x, int y) {
		switch(orientation) {
	        case NORTH:
	            if (y > getY() && x >= getX() - 1 && x <= getX() + 1)
	                return true;
	            break;
	        case NORTHEAST:
	            if (x >= getX() + 1 && y >= getY() + 1)
	                return true;
	            break;
	        case EAST:
	            if (x > getX() && y >= getY() - 1 && y <= getY() + 1)
	                return true;
	            break;
	        case SOUTHEAST:
	            if (y <= getY() - 1 && x >= getX() + 1)
	                return true;
	            break;
	        case SOUTH:
	            if (y < getY() && x >= getX() - 1 && x <= getX() + 1)
	                return true;
	            break;
	        case SOUTHWEST:
	            if (x <= getX() - 1 && y <= getY() - 1)
	                return true;
	            break;
	        case WEST:
	            if (x < getX() && y >= getY() - 1 && y <= getY() + 1)
	                return true;
	            break;
	        case NORTHWEST:
	            if (x <= getX() - 1 && y >= getY() + 1)
	                return true;
		}
		return false;
	}
	
	public GlobalObject getGlobalObj() {
		return obj;
	}
	
	public CannonPart getPhase() {
		return phase;
	}
	
	static final int CANNONBALL = 2;
	int ammo = 0;
	boolean firing;
	public Client c;
	public CannonPart phase = CannonPart.NONE;
	Orientation orientation = Orientation.NORTH;
	GlobalObject obj;
	private int hits;
	List<Integer> hitNpcs;
	public String name;
	
	public int getX() {
		return obj == null ? -1 : obj.getX();
	}
	
	public int getY() {
		return obj == null ? -1 : obj.getY();
	}
	
	public int getHeight() {
		return obj == null ? -1 : obj.getHeight();
	}
	
	enum Orientation {
		NORTH(516),
		NORTHEAST(517),
		EAST(518),
		SOUTHEAST(519),
		SOUTH(520),
		SOUTHWEST(521),
		WEST(514),
		NORTHWEST(515);
		
		Orientation(int anim) {
			this.anim = anim;
		}
		
		int anim;
		
		public int getAnim() {
			return anim;
		}
	}
	
	public enum CannonPart {
		NONE(-1, -1),
		BASE(6, 7),
		STAND(8, 8),
		BARREL(10, 9),
		FURNACE(12, 6);
		
		CannonPart(int id, int objId) {
			this.id = id;
			this.objId = objId;
		}
		
		int id, objId;
		
		public int getObj() {
			return objId;
		}
		
		public int getId() {
			return id;
		}
		
		public static boolean isObjCannon(int obj) {
			for(CannonPart p : CannonPart.values()) {
				if(p.getObj() == obj)
					return true;
			}
			return false;
		}
		
	}

}
