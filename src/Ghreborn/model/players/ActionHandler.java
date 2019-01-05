package Ghreborn.model.players;


import java.util.stream.IntStream;

import Ghreborn.Config;
import Ghreborn.Server;
import Ghreborn.clip.doors.DoorDefinition;
import Ghreborn.clip.doors.DoorHandler;
import Ghreborn.core.PlayerHandler;
import Ghreborn.definitions.ObjectDef;
import Ghreborn.event.CycleEvent;
import Ghreborn.event.CycleEventContainer;
import Ghreborn.event.CycleEventHandler;
import Ghreborn.model.content.CrystalChest;
import Ghreborn.model.content.Obelisks;
import Ghreborn.model.content.dialogue.impl.DailyDialogue;
import Ghreborn.model.content.dialogue.impl.EllisDialogue;
import Ghreborn.model.content.dialogue.impl.ForesterDialogue;
import Ghreborn.model.content.dialogue.impl.HansXmasDialogue;
import Ghreborn.model.content.dialogue.impl.ManandWomanDialogue;
import Ghreborn.model.content.dialogue.impl.TraderCrewMemberFemale;
import Ghreborn.model.content.dialogue.impl.TraderCrewMemberMale;
import Ghreborn.model.content.dialogue.impl.VeosDialogue;
import Ghreborn.model.content.dialogue.impl.VoteShopsDialogue;
import Ghreborn.model.content.dialogue.impl.ZenyteDialogue;
import Ghreborn.model.content.dialogue.impl.Lumbridge.FredTheFarmer;
import Ghreborn.model.content.dialogue.impl.Lumbridge.HansDialogue;
import Ghreborn.model.content.dialogue.impl.Lumbridge.Lumbridge_guide_Dialogue;
import Ghreborn.model.content.dialogue.impl.Lumbridge.Melee_combat_tutorDialogue;
import Ghreborn.model.content.dialogue.impl.Lumbridge.SheepDialogue;
import Ghreborn.model.items.Item2;
import Ghreborn.model.minigames.raids.Raids;
import Ghreborn.model.npcs.NPC;
import Ghreborn.model.npcs.NPCHandler;
import Ghreborn.model.npcs.PetHandler;
import Ghreborn.model.objects.ChopVines;
import Ghreborn.model.objects.Doors;
import Ghreborn.model.objects.DoubleGates;
import Ghreborn.model.objects.Object;
import Ghreborn.model.objects.SingleGates;
import Ghreborn.model.objects.SpadeInGround;
import Ghreborn.model.objects.WildernessDitch;
import Ghreborn.model.objects.functions.AxeInLog;
import Ghreborn.model.objects.functions.Ladders;
import Ghreborn.model.players.skills.Fishing.Fishing;
import Ghreborn.model.players.skills.construction.Construction;
import Ghreborn.model.players.skills.construction.House;
import Ghreborn.model.players.skills.crafting.JewelryMaking;
import Ghreborn.model.players.skills.crafting.Tanning;
import Ghreborn.model.players.skills.farming.Farming;
import Ghreborn.model.players.skills.hunter.Butterflys;
import Ghreborn.model.players.skills.hunter.Hunter;
import Ghreborn.model.players.skills.hunter.Implings;
import Ghreborn.model.players.skills.hunter.impling.PuroPuro;
import Ghreborn.model.players.skills.runecrafting.Runecrafting;
import Ghreborn.model.players.skills.thieving.Thieving.Pickpocket;
import Ghreborn.model.players.skills.thieving.Thieving.Stall;
import Ghreborn.model.players.skills.woodcutting.*;
import Ghreborn.util.Location3D;
import Ghreborn.util.Misc;
import Ghreborn.util.ScriptManager;
import Ghreborn.world.objects.GlobalObject;

public class ActionHandler {

	private Client c;

	public ActionHandler(Client Client) {
		this.c = Client;
	}

	int a2DimArray[][] = {
			{1865, 5227}, {1865, 5226}, {1868, 5226}, {1868, 5227}, {1867, 5217}, {1867, 5218}, {1870, 5217}, {1870, 5218},
			{1894, 5213}, {1894, 5212}, {1897, 5213}, {1897, 5212}, {1904, 5203}, {1904, 5204}, {1907, 5203}, {1907, 5204},
			{1882, 5188}, {1882, 5189}, {1879, 5189}, {1879, 5188}, {1879, 5240}, {1879, 5239}, {1876, 5240}, {1876, 5239},
			{1884, 5244}, {1884, 5243}, {1887, 5244}, {1887, 5243}, {1889, 5235}, {1889, 5236}, {1886, 5235}, {1886, 5236},
			{1904, 5242}, {1904, 5243}, {1908, 5242}, {1908, 5243}
			
	};
	public void firstClickObject(int objectType, int obX, int obY) {
		if (Server.getMultiplayerSessionListener().inAnySession(c)) {
			return;
		}
		if (c.getPlayerAction().checkAction()) {
			return;
		}
		if (c.isMorphed) {
			return;
			}
		GlobalObject object = new GlobalObject(objectType, obX, obY, c.heightLevel);
		c.getPA().resetVariables();

		c.clickObjectType = 0;
		c.face(c.getClickX(), c.getClickY());
		if(c.getRights().isBetween(9, 10)) {
			c.sendMessage("Object type: " + objectType+" ObjectX:"+obX+" ObjectY"+obY+".");
	}

		Tree tree = Tree.forObject(objectType);
		if (tree != null) {
			Woodcutting.getInstance().chop(c, objectType, obX, obY);
			return;
		}
		DoorDefinition door = DoorDefinition.forCoordinate(c.getClickX(), c.getClickY(), c.getHeight());
		
		if (door != null && DoorHandler.clickDoor(c, door)) {
			return;
		}

		SingleGates.useSingleGate(c, objectType);
		DoubleGates.useDoubleGate(c, objectType);
c.getGnomeAgility().agilityCourse(c, objectType);
Obelisks.get().activate(c, objectType);
Runecrafting.execute(c, objectType);
c.getMining().mine(objectType, new Location3D(obX, obY, c.heightLevel));
Location3D location = new Location3D(obX, obY, c.heightLevel);
final int[] HUNTER_OBJECTS = new int[]{9373, 9377, 9379, 9375, 9348, 9380, 9385, 9344, 9345, 9383, 721}; 
if(IntStream.of(HUNTER_OBJECTS).anyMatch(id -> objectType == id)) {
	if(Hunter.pickup(c, object)){
		return;
	}
	if(Hunter.claim(c, object)) {
		return;
	}
}

if (c.getRaids().handleObjectClick(c,objectType)) {
	return;
}

if(c.goodDistance(c.getClickX(), c.getClickY(), c.getClickX(), c.getClickY(), 1)) {
	if (Doors.getSingleton().handleDoor(c.objectId, c.getClickX(), c.getClickY(), c.heightLevel)) {
	}
}
if (ObjectDef.getObjectDef(objectType).getName().toLowerCase().contains("hay")) {
	c.sendMessage("You search the "	+ObjectDef.getObjectDef(objectType).getName().toLowerCase()+"...");
	c.animation(832);
	//player.setStopPacket(true);
	CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
		@Override
		public void execute(CycleEventContainer b) {
			if (Misc.random(99) == 0) {
				if (Misc.random(1) == 0) {
					c.getItems().addItem(1733, 1);
					c.sendMessage("You find a Needle.");
				} else {
				}
			} else {
				c.sendMessage("You find nothing of interest.");
			}
			b.stop();
		}
		@Override
		public void stop() {
		}
	}, 2);
	//this.stop();
	return;
}
if (ObjectDef.getObjectDef(objectType).getName().toLowerCase().equalsIgnoreCase("bank booth") || ObjectDef.getObjectDef(objectType).getName().toLowerCase().equalsIgnoreCase("bank chest") || ObjectDef.getObjectDef(objectType).getName().toLowerCase().equalsIgnoreCase("Counter")){ 
	c.getPA().openUpBank();
	return;
	}
if(ObjectDef.getObjectDef(objectType).getName().toLowerCase().contains("Ladder")) {
	if(ObjectDef.getObjectDef(objectType).actions[0].equals("Climb-up")) {
		if(obX == 3069 && obY == 10256) { // custom locations
			c.getPA().movePlayer(3017, 3850, 0);
			return;
		}
		if(obX == 3017 && obY == 10249) { // custom locations
			c.getPA().movePlayer(3069, 3857, 0);
			return;
		}

						if(obX == 3097 && obY == 9867) { // custom locations
			c.getPA().movePlayer(3096, 3468, 0);
			return;
						}
						if(obX == 1859 && obY == 5244) { // custom locations
							c.getPA().movePlayer(3081, 3421, c.heightLevel);
							return;
		}
						if(obX == 2906 && obY == 9968) { // custom locations
							c.getPA().movePlayer(2834, 3542, 0);
							return;
						}
	if(obX == 3207 && obY == 3223) { // custom locations
			c.getPA().movePlayer(c.absX, c.absY-2, c.heightLevel+1);
			return;
		}
		if(c.getClickY() > 6400) {
			c.getPA().movePlayer(c.getClickX(), c.getClickY()-6400, c.heightLevel);
			return;
		} else {
			c.getPA().movePlayer(c.absX, c.absY, c.heightLevel+1);
			return;
		}
	}
	if(ObjectDef.getObjectDef(objectType).actions[0].equals("Climb-down")) {
		if(obX == 3017 && obY == 3849) { // custom locations
			c.getPA().movePlayer(3069, 10257, 0);
			return;
		}
		if(c.getClickY() < 6400 && (c.heightLevel & 3) == 0) {
			c.getPA().movePlayer(c.getClickX(), c.getClickY()+6400, c.heightLevel);
			return;
		} else {
			c.getPA().movePlayer(c.absX, c.absY, c.heightLevel-1);
			return;
		}
	}
}
if(ObjectDef.getObjectDef(objectType).name.equals("Barrier")) {
	if(ObjectDef.getObjectDef(objectType).actions[0].equals("Pass")){
		if(c.getClickX() == 1573) { // custom locations
			c.getPA().movePlayer(1575, c.absY, 0);
			return;
		}
		if(c.getClickX() == 1575) { // custom locations
			c.getPA().movePlayer(1573, c.absY, 0);
			return;
		}
		if(c.getClickX() == 1562) { // custom locations
			c.getPA().movePlayer(1560, c.absY, 0);
			return;
		}
		if(c.getClickX() == 1560) { // custom locations
			c.getPA().movePlayer(1562, c.absY, 0);
			return;
		}
		if(c.getClickY() == 5089) { // custom locations
			c.getPA().movePlayer(c.absX, 5087, 0);
			return;
		}
		if(c.getClickY() == 5087) { // custom locations
			c.getPA().movePlayer(c.absX, 5089, 0);
			return;
		}
	}
}
/*if(ObjectDef.getObjectDef(objectType).name.equals("Altar") || ObjectDef.getObjectDef(objectType).name.equals("Chaos altar")) {    
    if(ObjectDef.getObjectDef(objectType).actions[0].equals("Pray-at") || ObjectDef.getObjectDef(objectType).actions[0].equals("Pray")) {
    	if(objectType != 6552) {
    }
                if(c.playerLevel[5] < c.getPA().getLevelForXP(c.playerXP[5])) {
                    c.startAnimation(645);
                    c.playerLevel[5] = c.getPA().getLevelForXP(c.playerXP[5]);
                    c.sendMessage("You recharge your prayer points.");
                    c.getPA().refreshSkill(5);
                } else {
                    c.sendMessage("You already have full prayer points.");
                }
                return;
    } else {
        if (c.playerMagicBook == 0) {
            c.playerMagicBook = 1;
            c.setSidebarInterface(6, 12855);
            c.sendMessage("An ancient wisdomin fills your mind.");
            c.getPA().resetAutocast();
        } else {
            c.setSidebarInterface(6, 1151); // modern
            c.playerMagicBook = 0;
            c.sendMessage("You feel a drain on your memory.");
            c.autocastId = -1;
            c.getPA().resetAutocast();
    }
   
}
}
if(ObjectDef.getObjectDef(objectType).name.equals("Ladder")) {
	if(ObjectDef.getObjectDef(objectType).actions[0].equals("Climb-up")) {
		if(obX == 3069 && obY == 10256) { // custom locations
			c.getPA().movePlayer(3017, 3850, 0);
			return;
		}
		if(obX == 3017 && obY == 10249) { // custom locations
			c.getPA().movePlayer(3069, 3857, 0);
			return;
		}

						if(obX == 3097 && obY == 9867) { // custom locations
			c.getPA().movePlayer(3096, 3468, 0);
			return;
						}
						if(obX == 1859 && obY == 5244) { // custom locations
							c.getPA().movePlayer(3081, 3421, c.heightLevel);
							return;
		}
						if(obX == 2906 && obY == 9968) { // custom locations
							c.getPA().movePlayer(2834, 3542, 0);
							return;
						}
	if(obX == 3207 && obY == 3223) { // custom locations
			c.getPA().movePlayer(c.absX, c.absY-2, c.heightLevel+1);
			return;
		}
		if(c.getClickY() > 6400) {
			c.getPA().movePlayer(c.getClickX(), c.getClickY()-6400, c.heightLevel);
			return;
		} else {
			c.getPA().movePlayer(c.absX, c.absY, c.heightLevel+1);
			return;
		}
	}
	if(ObjectDef.getObjectDef(objectType).actions[0].equals("Climb-down")) {
		if(obX == 3017 && obY == 3849) { // custom locations
			c.getPA().movePlayer(3069, 10257, 0);
			return;
		}
		if(obX == 3069 && obY == 3856) { // custom locations
			c.getPA().movePlayer(3017, 10248, 0);
			return;
		}
		if(obX == 2833 && obY == 3542) { // custom locations
			c.getPA().movePlayer(2907, 9968, 0);
			return;
		}
		if(obX == 3207 && obY == 3223) { // custom locations
			c.getPA().movePlayer(c.absX, c.absY+2, c.heightLevel-1);
			return;
		}
		if(obX == 2820 && obY == 3374){
			c.getPA().movePlayer(c.getClickX(), c.getClickY()+6400, c.heightLevel);
			return;
		}
		if(c.getClickY() < 6400 && (c.heightLevel & 3) == 0) {
			c.getPA().movePlayer(c.getClickX(), c.getClickY()+6400, c.heightLevel);
			return;
		} else {
			c.getPA().movePlayer(c.absX, c.absY, c.heightLevel-1);
			return;
		}
	}
}
if(ObjectDef.getObjectDef(objectType).name.equals("Cave Entrance") && !ObjectDef.getObjectDef(objectType).name.equals("Entrance")) {
	if(ObjectDef.getObjectDef(objectType).actions[0].equals("Enter")) {
		if(obX == 2797 && obY == 3614) { // custom locations
			c.getPA().movePlayer(2808, 10002, 0);
			return;
		}
		if(obX == 2622 && obY == 3392) { // custom locations
			c.getPA().movePlayer(2620, 9797, 0);
			return;
		}
		if(c.getClickY() < 6400) {
			c.getPA().movePlayer(c.getClickX(), c.getClickY()+6400, c.heightLevel);
			return;
	}
	}
	}
if(ObjectDef.getObjectDef(objectType).name.equals("Cave") && !ObjectDef.getObjectDef(objectType).name.equals("Cave Entrance") && !ObjectDef.getObjectDef(objectType).name.equals("Entrance")) {
	if(ObjectDef.getObjectDef(objectType).actions[0].equals("Enter")) {

		if(c.getClickY() < 6400) {
			c.getPA().movePlayer(c.getClickX(), c.getClickY()+6400, c.heightLevel);
			return;
	}
}
}

if(ObjectDef.getObjectDef(objectType).name.equals("Tunnel")) {
	if(ObjectDef.getObjectDef(objectType).actions[0].equals("Enter")) {
		if(obX == 2809 && obY == 10001) { // custom locations
			c.getPA().movePlayer(2796, 3615, 0);
			return;
		}
		if(c.getClickY() > 6400) {
			c.getPA().movePlayer(c.getClickX(), c.getClickY()-6400, c.heightLevel);
			return;
	}
}
}
if(ObjectDef.getObjectDef(objectType).name.equals("Mud pile")) {
	if(ObjectDef.getObjectDef(objectType).actions[0].equals("Climb-over")) {
		if(obX == 2621 && obY == 9796) { // custom locations
			c.getPA().movePlayer(2624, 3391, 0);
			return;
		}
		if(c.getClickY() > 6400) {
			c.getPA().movePlayer(c.getClickX(), c.getClickY()-6400, c.heightLevel);
			return;
	}
}
}
if(ObjectDef.getObjectDef(objectType).name.equals("Stairs")) {
	if(ObjectDef.getObjectDef(objectType).actions[0].equals("Climb-up")) {
		if(c.getClickY() > 6400) {
			c.getPA().movePlayer(c.getClickX(), c.getClickY()-6400, c.heightLevel);
			return;
		} else {
			c.getPA().movePlayer(c.absX, c.absY, c.heightLevel+1);
			return;
		}
	}
	if(ObjectDef.getObjectDef(objectType).actions[0].equals("Climb-down")) {
		if(c.getClickY() < 6400 && (c.heightLevel & 3) == 0) {
			c.getPA().movePlayer(c.getClickX(), c.getClickY()+6400, c.heightLevel);
			return;
		} else {
			c.getPA().movePlayer(c.absX, c.absY, c.heightLevel-1);
			return;
		}
	}
}
if(ObjectDef.getObjectDef(objectType).name.equals("Entrance")) {
	if(ObjectDef.getObjectDef(objectType).actions[0].equals("Climb-down")) {
		if(obX == 3081 && obY == 3421) { // custom locations
			c.getPA().movePlayer(1859, 5243, 0);
			return;
		}
}
}
if(ObjectDef.getObjectDef(objectType).name.equals("Staircase")) {
	if(ObjectDef.getObjectDef(objectType).actions[0].equals("Climb-up")) {
		if(obX == 3212 && obY == 3473) { // custom locations
			c.getPA().movePlayer(3213, 3476, 1);
			return;
		}
		if(obX == 3187 && obY == 9833) { // custom locations
			c.getPA().movePlayer(3188, 3433, 0);
			return;
		}
		if(obX == 2726 && obY == 9775) { // custom locations
			c.getPA().movePlayer(2723, 3374, 0);
			return;
		}
		if(c.getClickY() > 6400) {
			c.getPA().movePlayer(c.getClickX(), c.getClickY()-6400, c.heightLevel);
			return;
		}else{
		c.getPA().movePlayer(c.absX, c.absY, c.heightLevel+1);
		return;
		}
	}
}
	if(ObjectDef.getObjectDef(objectType).actions[0].equals("Climb-down")) {
		if(obX == 3212 && obY == 3474) { // custom locations
			c.getPA().movePlayer(3213, 3472, 0);
			return;
		}
		if(obX == 3189 && obY == 3432) { // custom locations
			c.getPA().movePlayer(3190, 9833, 0);
			return;
		}
		if(obX == 2723 && obY == 3374) { // custom locations
			c.getPA().movePlayer(2727, 9774, 0);
			return;
		}
		if(c.getClickY() < 6400 && (c.heightLevel & 3) == 0) {
			c.getPA().movePlayer(c.getClickX(), c.getClickY()+6400, c.heightLevel);
			return;
		} else {
		c.getPA().movePlayer(c.absX, c.absY, c.heightLevel-1);
		return;
	}
}*/
	if (c.playerCannon != null && (PlayerCannon.CannonPart.isObjCannon(objectType) && objectType != 6)) {
		if (c.playerCannon.pickUpCannon(objectType, obX, obY)) {
			PlayerHandler.removeCannon(c.playerCannon);
			c.playerCannon = null;
		}
	} else if (c.playerCannon != null && objectType == 6) {
		c.playerCannon.fireCannon();
	} else if (PlayerCannon.CannonPart.isObjCannon(objectType)) {
		c.sendMessage("This is not your cannon!");
	}
	if ((c.getRights().isDeveloper() || c.getRights().isOwner()) && Config.SERVER_DEBUG ) {
		c.sendMessage("<col=255>[SERVER DEBUG] " + " - FirstClickObject: X: " + c.absX + " Y: " + c.absY
				+ " Height: " + c.heightLevel + " ObjectID: " + objectType);
	}
		switch (objectType) {
		case 16891:
			CrystalChest.searchChest(c);
			break;
		case 10216:
			Ladders.climbLadder(c,1890, 4406, 1);
			break;
		case 10215:
			Ladders.climbLadder(c,1890, 4406, 0);
			break;
		case 11794:
			if(c.getClickX() == 1644 && c.getClickY() == 2847){
			Ladders.climbLadder(c, 1644, 2847, 1);
			}
			break;
		case 11795:
			if(c.getClickX() == 1644 && c.getClickY() == 2847){
			Ladders.climbLadder(c, 1644, 2847, 0);
			}
			break;
		case 15477:
		case 15478:
		case 15479:
			c.setHouse(House.load(c));
			House house = c.getHouse();
			house.setBuildMode(false);
			house.enter(c);
			break;
		case 21731:
		case 21732:
		case 21733:
		case 21734:
		case 21735:
			ChopVines.walkThru(c, obX, obY);
			break;
		case 21739:
			c.getPA().movePlayer(2649, 9562);
			break;
		case 25336:
			if(c.getClickX() == 1772 && c.getClickY() == 5366){
				c.getPA().movePlayer(1768, 5366, 1);
			}
			break;
		case 25338:
			if(c.getClickX() == 1768 && c.getClickY() == 5366 && c.getHeight() == 1){
				c.getPA().movePlayer(1772, 5366, 0);
			}
			break;
/*		case 25339:
			if(obX == 1778 && obY == 5345){
				c.getPA().movePlayer(1778, 5343, 1);
			}
			break;
		case 25340:
			if(c.getClickX() == 1778 && c.getClickY() == 5343){
				c.getPA().movePlayer(1778, 5346, 0);
			}
			break;*/
		case 21738:
			c.getPA().movePlayer(2647, 9557);
			break;
		case 20882:
			c.getPA().movePlayer(2687, 9506);
			break;
		case 20884:
			c.getPA().movePlayer(2682, 9506);
			break;
		case 30283:
			c.getInfernoMinigame().leaveGame();
			break;
		case 15644:
		case 15641:
		case 24306:
		case 24309:
			if (c.heightLevel == 2) {
				// if(Boundary.isIn(c, WarriorsGuild.WAITING_ROOM_BOUNDARY) &&
				// c.heightLevel == 2) {
				c.getWarriorsGuild().handleDoor();
				return;
				// }
			}
			break;
		case 10043:
			if (c.heightLevel == 0) {
				// if(Boundary.isIn(c, WarriorsGuildBasement.WAITING_ROOM_BOUNDARY) &&
				// c.heightLevel == 2) {
				c.getWarriorsGuildBasement().handleDoor();
				return;
				// }
			}
			break;
		case 29150:
			c.sendMessage("This is in Work in progress", 255);
			//c.getDH().sendDialogues(914, -1);
			break;
		case 29241:
    		for (int j = 0; j <= 6; j++) {
    			if (c.playerLevel[j] < c.getPA().getLevelForXP(c.playerXP[j])) {
    				c.playerLevel[j] += (c.getPA().getLevelForXP(c.playerXP[j]) * .33);
    				if (c.playerLevel[j] > c.getPA().getLevelForXP(c.playerXP[j])) {
    					c.playerLevel[j] = c.getPA().getLevelForXP(c.playerXP[j]);
    				}
    				c.getPA().refreshSkill(j);
    				c.getPA().setSkillLevel(j, c.playerLevel[j], c.playerXP[j]);
    			}
    		}
            c.runEnergy = 100;
            c.updateRequired = true;
    		c.setLastVenomCure(System.currentTimeMillis());
    		c.setVenomDamage((byte) 0);
    		c.poisonDamage = 0;
    		 c.specAmount = 100;
    		 c.getItems().updateSpecialBar();
    		c.lastPoisonSip = System.currentTimeMillis();
    		c.sendMessage("You drink from the Ornate rejuvenation pool");
			break;
		case 2492:
			if (c.killCount >= 20) {
				c.getDH().sendOption4("Armadyl", "Bandos", "Saradomin",
						"Zamorak");
				c.dialogueAction = 20;
			} else {
				c.sendMessage("You need 20 kill count before teleporting to a boss chamber.");
			}
			break;
		case 26564:
		case 26565:
		case 26566:
			c.getPA().movePlayer(2873, 9847, 0);
			break;
		case 26569:
		case 26568:
		case 26567:
			if (c.playerLevel[c.playerSlayer] < 85) {
				c.sendMessage("You need a slayer level of 85 to enter cerberus lair.");
				return;
			}
			c.getPA().movePlayer(1310, 1237, 0);
			break;
		case 21772:
			c.getPA().movePlayer(1310, 1237, 0);
			break;
		case 31485:
			if(c.absX == 3603 && c.absY == 10291){
				c.getPA().movePlayer(3606, 10289, 0);
			}else if(c.absX == 3606 && c.absY == 10289){
				c.getPA().movePlayer(3603, 10291, 0);
			}
			break;
		case 30844:
			if(c.absX == 3596 && c.absY == 10291){
				c.getPA().movePlayer(3746, 3779, 0);
			}
			break;
			case 30681:
				if(c.absX == 3753 && c.absY == 3870){
					c.getPA().movePlayer(3757, 3870, 1);
				}else if(c.absX == 3753 && c.absY == 3869){
					c.getPA().movePlayer(3757, 3869, 1);
				}else if(c.absX == 3753 && c.absY == 3868){
					c.getPA().movePlayer(3757, 3868, 1);
				}
				break;
			case 30944:
				if(c.playerLevel[c.playerMagic] >= 66 && c.playerLevel[c.playerSmithing] >= 66) {
					if(c.getItems().playerHasItem(2347) && c.getItems().playerHasItem(21637) && c.getItems().playerHasItem(2890)) {
						c.getItems().deleteItem2(21637, 1);
						c.getItems().deleteItem2(2890, 1);
						c.playerLevel[c.playerMagic] = 0;
						c.getPA().addSkillXP(2000, c.playerLevel[c.playerMagic]);
						c.getPA().addSkillXP(2000, c.playerLevel[c.playerSmithing]);
						c.gfx100(111);
						c.animation(898);
						c.getItems().addItem(21634, 1);
						c.sendMessage("At a great cost of your personal magical energies,", 255);
						c.sendMessage("you have crafted the wyvern visage and elemental shield",255);
						c.sendMessage("into an anchient wyvern shield.", 255);
					}else {
						c.sendMessage("You need a hammer a Elemental shield and a Wyvern visage.");
					}
				}else {
					c.sendMessage("You need 66 magic and smithing for this.", 255);
				}
				break;
			case 30682:
				if(c.absX == 3757 && c.absY == 3870){
					c.getPA().movePlayer(3753, 3870, 0);
				}else if(c.absX == 3757 && c.absY == 3869){
					c.getPA().movePlayer(3753, 3869, 0);
				}else if(c.absX == 3757 && c.absY == 3868){
					c.getPA().movePlayer(3753, 3868, 0);
				}
				break;
		case 30869:
			if(c.absX == 3746 && c.absY == 3779){
				c.getPA().movePlayer(3596, 10291, 0);
			}
			break;
		case 30849:
			if(c.absX == 3633 && c.absY == 10260){
				c.getPA().movePlayer(3633, 10264, 0);
			}
			break;
			case 30847:
			if(c.absX == 3633 && c.absY == 10264){
				c.getPA().movePlayer(3633, 10260, 0);
			}
			break;
		case 23104:
			if (c.absX == 1292 || c.absX == 1291) {
				c.getPA().movePlayer(1240, 1226, 0);
			} else if (c.absY == 1270 || c.absY == 1269) {
				c.getPA().movePlayer(1304, 1290, 0);
			} else if (c.absX == 1328 || c.absX == 1329) {
				c.getPA().movePlayer(1368, 1226, 0);
			}
			break;
		case 29681:
			if(c.heightLevel == 1) {
				c.getPA().movePlayer(c.absX, c.absY, 2);
			}else if(c.heightLevel == 2) {
				c.getPA().movePlayer(c.absX, c.absY, 1);
			}
			break;
		case 25382:
			if(obX == 3035 && obY == 4841){
				c.getPA().movePlayer(2400, 4835, 0);
			} else if (obX == 3029 && obY == 4837){
			c.getPA().movePlayer(2142, 4813, 0);
			} else if (obX == 3030 && obY == 4830){
			c.getPA().movePlayer(2574, 4849, 0);
			} else if (obX == 3032 && obY == 4825){
			c.getPA().movePlayer(2655, 4830, 0);
			} else if (obX == 3039 && obY == 4822){
			c.getPA().movePlayer(2521, 4834, 0);
			} else if (obX == 3044 && obY == 4823){
			c.getPA().movePlayer(2793, 4828, 0);
			} else if (obX == 3050 && obY == 4833){
			c.getPA().movePlayer(2841, 4829, 0);
			} else if (obX == 3049 && obY == 4837){
			c.getPA().movePlayer(2726, 4832, 0);
			} else if (obX == 3049 && obY == 4837){
			c.getPA().movePlayer(2208, 4830, 0);
			} else if (obX == 3048 && obY == 4839){
			c.getPA().movePlayer(2464, 4818, 0);
			} else if (obX == 3044 && obY == 4841){
			c.getPA().movePlayer(2281, 4837, 0);
			} else if (obX == 3047 && obY == 4826){
			c.getPA().movePlayer(2841, 4829, 0);
			}
			break;
		case 18493:
			if(obX == 3235 && obY == 3228) {
				c.getPA().showInterface(29000);
		        c.getPA().sendFrame126("Head north towards\\n Fred's farm and the\\n windmill.", 29010);
		        c.getPA().sendFrame126("South to the swamps of\\nLumbridge", 29014);
		        c.getPA().sendFrame126("Cross the bridge\\nand head east to\\nAl Kharid or\\n north to Varrock.", 29012);
		        c.getPA().sendFrame126("West to the\\n Lumbridge Castle\\n and Draynor \\n Village. Beware\\n the goblins.", 29016);
			}
			break;
		case 5581: // take axe from log
			AxeInLog.pullAxeFromLog(c, c.getClickX(), c.getClickY());
			break;
			
		case 14910:
			if(c.absX == 2933 && c.absY == 3289){
			if(c.playerLevel[c.playerCrafting] <= 40){
				c.sendMessage("You need an crafting level of 40 to enter this guild.");
				return;
			}else{
				c.getPA().walkTo(0, +1);
				c.sendMessage("You enter the crafting guild.");
			}
			}
			
			break;
		case 9662:
			SpadeInGround.pullSpadeFromGround(c, c.getClickX(), c.getClickY());
			break;
		case 31555:
			if(obX == 3073 && obY == 3654) {
				c.getPA().movePlayer(3196, 10056, 0);
			}
			break;
		case 31557:
			if(obX == 3194 && obY == 10055) {
				c.getPA().movePlayer(3074, 3653, 0);
			}
			break;
		case 2380:
		  /*  if ((System.currentTimeMillis() - c.actionTimer) <= 5000) {	
		    	c.sendMessage("you got to wait to click this again.");
		    } else {
		    	c.getPA().addSkillXP(60 * Config.FIREMAKING_EXPERIENCE, 11);
		        c.sendMessage("You Gain Some FireMaking.");	
		        c.animation(1979);
		       c.gfx100(76);
		        c.actionTimer = System.currentTimeMillis();
		        c.updateRequired = true; 
		        c.appearanceUpdateRequired = true;
		    }*/
			c.sendMessage("This is disabled for right now.");
		    break;
		case 4525: // leave house
			if (c.getHouse() != null)
				c.getHouse().leave(c);
			c.getPA().movePlayer(2953, 3224, 0);
			break;
		case 26709:
			c.getPA().movePlayer(2444, 9825, c.heightLevel);
			break;
		case 26710:
			c.getPA().movePlayer(2430, 3424, c.heightLevel);
			break;
		case 4113: //mini game
			if(c.JDemonkills >= 1) {
			}
			if(c.Demonkills >= 1) {
			}
			if(c.Generalkills >= 1) {
				c.Demonkills -= 1;
				c.JDemonkills -= 1;
				c.Generalkills -= 1;
				c.sendMessage("Congradulations!!! You have beaten the Party Hat Mini game!!!");
				c.getPA().messageall(c.playerName+ " has just finished the Party Hat Mini game!");
				c.getItems().addItem(Item2.randomPhat(), 1);
			} else {
				c.sendMessage("You attempt to open the chest but it seems to be sealed tightly shut.");
			}
			break;
		case 5259: // Portal
			if(c.Druidkills >= 1) {
			}
			if(c.Ghostkills >= 1) {
			}
			if(c.Giantkills >= 1) {
				c.getPA().movePlayer(3239, 3201, 0);
				c.Giantkills -= 1;
				c.Druidkills -= 1;
				c.Ghostkills -= 1;
				c.sendMessage("You pass through the Ghostly Portal.");
			} else {
				c.sendMessage("You attempt to step through the portal but you are stopped.");
				c.sendMessage("A voice tells you to kill the three unclean monsters.");
			}
			break;
		case 16123:
		case 16124:
			for(int i = 0; i < a2DimArray.length; i++) {
				if(c.absX == a2DimArray[i][0] && c.absY == a2DimArray[i][1]) {
					c.animation(4282);
					c.getPA().walkTo(-1, 0);
					return;
				}
			}
			if(c.absX == 1890 && c.absY == 5208 || c.absX == 1889 && c.absY == 5208
			|| c.absX == 1876 && c.absY == 5195 || c.absX == 1877 && c.absY == 5195
			|| c.absX == 1876 && c.absY == 5192 || c.absX == 1877 && c.absY == 5192) {
					c.animation(4282);
						c.getPA().walkTo(0, -1);
				return;
			}
			if(c.absX == obX && c.absY == obY)
					c.animation(4282);
						c.getPA().walkTo(0, +1);
			if(c.absY == obY && c.absX < obX)
					c.animation(4282);
						c.getPA().walkTo(+1, 0);
			if(c.absY > obY && c.absX == obX)
				c.getPA().walkTo(0, -1);
			if(c.absY < obY && c.absX == obX)
				c.getPA().walkTo(0, 1);
		break;
		case 16149:
if (obX == 1902 && obY == 5222) {
			c.getPA().movePlayer(2042, 5245, 0);
}
			break;
		case 16080:
if (obX == 2042 && obY == 5246) {
			c.getPA().movePlayer(1859, 5243, 0);
}
			break;
		case 23271:
			if (!c.ditchDelay.elapsed(2000)) {
				return;
			}
			c.face(obX, obY);
			c.ditchDelay.reset();
			if (c.getClickY() >= 3523) {
				WildernessDitch.leave(c);
			} else
				WildernessDitch.enter(c);
			break;
			/**
			 * Entering the Fight Caves.
			 */
			case 11833:
				if (Boundary.entitiesInArea(Boundary.FIGHT_CAVE) >= 50) {
					c.sendMessage("There are too many people using the fight caves at the moment. Please try again later");
					return;
				}
				//if (System.currentTimeMillis() - c.hunting < 1500) {
					//c.sendMessage("No");
					//return;
				//}
				c.getFightCave().create(0);
				//c.hunting = System.currentTimeMillis();
				break;

			case 11834:
				if (Boundary.isIn(c, Boundary.FIGHT_CAVE)) {
					c.getFightCave().leaveGame();
					return;
				}
				break;
		case 1765:
			c.getPA().movePlayer(3067, 10256, 0);
			break;
		case 2882:
		case 2883:
			if (c.getClickX() == 3268) {
				if (c.absX < c.getClickX()) {
					c.getPA().walkTo(1, 0);
				} else {
					c.getPA().walkTo(-1, 0);
				}
			}
			break;
		case 272:
			c.getPA().movePlayer(c.absX, c.absY, 1);
			break;

		case 273:
			c.getPA().movePlayer(c.absX, c.absY, 0);
			break;
		case 245:
			c.getPA().movePlayer(c.absX, c.absY + 2, 2);
			break;
		case 246:
			c.getPA().movePlayer(c.absX, c.absY - 2, 1);
			break;
		case 19690:
			c.getPA().movePlayer(c.absX, c.absY + 4, 1);
			break;
		case 19691:
			c.getPA().movePlayer(c.absX, c.absY - 4, 0);
			break;
		case 1766:
			c.getPA().movePlayer(3016, 3849, 0);
			break;
		case 6552:
			if (c.playerMagicBook == 0) {
				c.playerMagicBook = 1;
				c.setSidebarInterface(6, 12855);
				c.sendMessage("An ancient wisdomin fills your mind.");
				c.getPA().resetAutocast();
			} else {
				c.setSidebarInterface(6, 1151); // modern
				c.playerMagicBook = 0;
				c.sendMessage("You feel a drain on your memory.");
				c.autocastId = -1;
				c.getPA().resetAutocast();
			}
			break;

		case 410:
			if (c.playerMagicBook == 0) {
				c.playerMagicBook = 2;
				c.setSidebarInterface(6, 29999);
				c.sendMessage("Lunar wisdom fills your mind.");
				c.getPA().resetAutocast();
			} else {
				c.setSidebarInterface(6, 1151); // modern
				c.playerMagicBook = 0;
				c.sendMessage("You feel a drain on your memory.");
				c.autocastId = -1;
				c.getPA().resetAutocast();
			}
			break;

		case 1816:
			c.getPA().startTeleport2(2271, 4680, 0);
			break;
		case 1817:
			c.getPA().startTeleport(3067, 10253, 0, "modern");
			break;
		case 1814:
			// ardy lever
			c.getPA().startTeleport(3153, 3923, 0, "modern");
			break;

		case 9356:
			// c.getPA().enterCaves();
			c.sendMessage("Temporarily removed due to bugs.");
			break;
		case 1733:
			c.getPA().movePlayer(c.absX, c.absY + 6393, 0);
			break;

		case 1734:
			c.getPA().movePlayer(c.absX, c.absY - 6396, 0);
			break;


		case 8959:
			if (c.getClickX() == 2490 && (c.getClickY() == 10146 || c.getClickY() == 10148)) {
				if (c.getPA().checkForPlayer(2490,
						c.getClickY() == 10146 ? 10148 : 10146)) {
					new Object(6951, c.getClickX(), c.getClickY(), c.heightLevel, 1,
							10, 8959, 15);
				}
			}
			break;

		case 2213:
		case 14367:
		case 11758:
		case 3193:
		case 27663:
			c.getPA().openUpBank();
			break;

		case 10177:
			c.getPA().movePlayer(1890, 4407, 0);
			break;
		case 10230:
			c.getPA().movePlayer(2900, 4449, 0);
			break;
		case 10229:
			c.getPA().movePlayer(1912, 4367, 0);
			break;
		case 2623:
			if (c.absX >= c.getClickX())
				c.getPA().walkTo(-1, 0);
			else
				c.getPA().walkTo(1, 0);
			break;
		// pc boat
		case 14315:
			c.getPA().movePlayer(2661, 2639, 0);
			break;
		case 14314:
			c.getPA().movePlayer(2657, 2639, 0);
			break;

		case 1596:
		case 1597:
			if (c.getClickY() >= c.getClickY())
				c.getPA().walkTo(0, -1);
			else
				c.getPA().walkTo(0, 1);
			break;

		case 14235:
		case 14233:
			if (c.getClickX() == 2670)
				if (c.absX <= 2670)
					c.absX = 2671;
				else
					c.absX = 2670;
			if (c.getClickX() == 2643)
				if (c.absX >= 2643)
					c.absX = 2642;
				else
					c.absX = 2643;
			if (c.absX <= 2585)
				c.absY += 1;
			else
				c.absY -= 1;
			c.getPA().movePlayer(c.absX, c.absY, 0);
			break;


		case 4387:
			// Server.castleWars.joinWait(c,1);
			break;

		case 4388:
			// Server.castleWars.joinWait(c,2);
			break;

		case 4408:
			// Server.castleWars.joinWait(c,3);
			break;

		case 9369:
			if (c.getClickY() > 5175)
				c.getPA().movePlayer(2399, 5175, 0);
			else
				c.getPA().movePlayer(2399, 5177, 0);
			break;

		case 9368:
			if (c.getClickY() < 5169) {
				Server.fightPits.removePlayerFromPits(c.index);
				c.getPA().movePlayer(2399, 5169, 0);
			}
			break;
		case 4411:
		case 4415:
		case 4417:
		case 4418:
		case 4419:
		case 4420:
		case 4469:
		case 4470:
		case 4911:
		case 4912:
		case 1747:
		case 1757:
			// Server.castleWars.handleObjects(c, objectType, obX, obY);
			break;

		case 2286:
		case 154:
		case 4058:
		case 2295:
		case 2285:
		case 2313:
		case 2312:
		case 2314:
			c.getAgility().handleGnomeCourse(objectType, obX, obY);
			break;

			/*
			 * Barrows Chest
			 */
		case 10284:
			if (c.barrowsKillCount < 5) {
				c.sendMessage("You haven't killed all the brothers");
			}
			if (c.barrowsKillCount == 5
					&& c.barrowsNpcs[c.randomCoffin][1] == 1) {
				c.sendMessage("I have already summoned this npc.");
			}
			if (c.barrowsNpcs[c.randomCoffin][1] == 0
					&& c.barrowsKillCount >= 5) {
				Server.npcHandler.spawnNpc(c, c.barrowsNpcs[c.randomCoffin][0],
						3551, 9694 - 1, 0, 0, 120, 30, 200, 200, true, true);
				c.barrowsNpcs[c.randomCoffin][1] = 1;
			}
			if ((c.barrowsKillCount > 5 || c.barrowsNpcs[c.randomCoffin][1] == 2)
					&& c.getItems().freeSlots() >= 2) {
				c.getPA().resetBarrows();
				c.getItems().addItem(c.getPA().randomRunes(),
						Misc.random(150) + 100);
				if (Misc.random(2) == 1)
					c.getItems().addItem(c.getPA().randomBarrows(), 1);
				c.getPA().startTeleport(3564, 3288, 0, "modern");
			} else if (c.barrowsKillCount > 5 && c.getItems().freeSlots() <= 1) {
				c.sendMessage("You need at least 2 inventory slot opened.");
			}
			break;
		/*
		 * Doors
		 */
		case 6749:
			if (obX == 3562 && obY == 9678) {
				c.getPA().object(3562, 9678, 6749, -3, 0);
				c.getPA().object(3562, 9677, 6730, -1, 0);
			} else if (obX == 3558 && obY == 9677) {
				c.getPA().object(3558, 9677, 6749, -1, 0);
				c.getPA().object(3558, 9678, 6730, -3, 0);
			}
			break;
		case 6730:
			if (obX == 3558 && obY == 9677) {
				c.getPA().object(3562, 9678, 6749, -3, 0);
				c.getPA().object(3562, 9677, 6730, -1, 0);
			} else if (obX == 3558 && obY == 9678) {
				c.getPA().object(3558, 9677, 6749, -1, 0);
				c.getPA().object(3558, 9678, 6730, -3, 0);
			}
			break;
		case 6727:
			if (obX == 3551 && obY == 9684) {
				c.sendMessage("You cant open this door..");
			}
			break;
		case 6746:
			if (obX == 3552 && obY == 9684) {
				c.sendMessage("You cant open this door..");
			}
			break;
		case 6748:
			if (obX == 3545 && obY == 9678) {
				c.getPA().object(3545, 9678, 6748, -3, 0);
				c.getPA().object(3545, 9677, 6729, -1, 0);
			} else if (obX == 3541 && obY == 9677) {
				c.getPA().object(3541, 9677, 6748, -1, 0);
				c.getPA().object(3541, 9678, 6729, -3, 0);
			}
			break;
		case 6729:
			if (obX == 3545 && obY == 9677) {
				c.getPA().object(3545, 9678, 6748, -3, 0);
				c.getPA().object(3545, 9677, 6729, -1, 0);
			} else if (obX == 3541 && obY == 9678) {
				c.getPA().object(3541, 9677, 6748, -1, 0);
				c.getPA().object(3541, 9678, 6729, -3, 0);
			}
			break;
		case 6726:
			if (obX == 3534 && obY == 9684) {
				c.getPA().object(3534, 9684, 6726, -4, 0);
				c.getPA().object(3535, 9684, 6745, -2, 0);
			} else if (obX == 3535 && obY == 9688) {
				c.getPA().object(3535, 9688, 6726, -2, 0);
				c.getPA().object(3534, 9688, 6745, -4, 0);
			}
			break;
		case 6745:
			if (obX == 3535 && obY == 9684) {
				c.getPA().object(3534, 9684, 6726, -4, 0);
				c.getPA().object(3535, 9684, 6745, -2, 0);
			} else if (obX == 3534 && obY == 9688) {
				c.getPA().object(3535, 9688, 6726, -2, 0);
				c.getPA().object(3534, 9688, 6745, -4, 0);
			}
			break;
		case 6743:
			if (obX == 3545 && obY == 9695) {
				c.getPA().object(3545, 9694, 6724, -1, 0);
				c.getPA().object(3545, 9695, 6743, -3, 0);
			} else if (obX == 3541 && obY == 9694) {
				c.getPA().object(3541, 9694, 6724, -1, 0);
				c.getPA().object(3541, 9695, 6743, -3, 0);
			}
			break;
		case 6724:
			if (obX == 3545 && obY == 9694) {
				c.getPA().object(3545, 9694, 6724, -1, 0);
				c.getPA().object(3545, 9695, 6743, -3, 0);
			} else if (obX == 3541 && obY == 9695) {
				c.getPA().object(3541, 9694, 6724, -1, 0);
				c.getPA().object(3541, 9695, 6743, -3, 0);
			}
			break;
		/*
		 * Cofins
		 */
		case 6707: // verac
			c.getPA().movePlayer(3556, 3298, 0);
			break;

		case 6823:
			if (Ghreborn.model.minigames.Barrows.selectCoffin(c, objectType)) {
				return;
			}
			if (c.barrowsNpcs[0][1] == 0) {
				Server.npcHandler.spawnNpc(c, 2030, c.getClickX(), c.getClickY() - 1, -1,
						0, 120, 25, 200, 200, true, true);
				c.barrowsNpcs[0][1] = 1;
			} else {
				c.sendMessage("You have already searched in this sarcophagus.");
			}
			break;

		case 6706: // torag
			c.getPA().movePlayer(3553, 3283, 0);
			break;

		case 6772:
			if (Ghreborn.model.minigames.Barrows.selectCoffin(c, objectType)) {
				return;
			}
			if (c.barrowsNpcs[1][1] == 0) {
				Server.npcHandler.spawnNpc(c, 2029, c.getClickX() + 1, c.getClickY(), -1,
						0, 120, 20, 200, 200, true, true);
				c.barrowsNpcs[1][1] = 1;
			} else {
				c.sendMessage("You have already searched in this sarcophagus.");
			}
			break;

		case 6705: // karil stairs
			c.getPA().movePlayer(3565, 3276, 0);
			break;
		case 6822:
			if (Ghreborn.model.minigames.Barrows.selectCoffin(c, objectType)) {
				return;
			}
			if (c.barrowsNpcs[2][1] == 0) {
				Server.npcHandler.spawnNpc(c, 2028, c.getClickX(), c.getClickY() - 1, -1,
						0, 90, 17, 200, 200, true, true);
				c.barrowsNpcs[2][1] = 1;
			} else {
				c.sendMessage("You have already searched in this sarcophagus.");
			}
			break;

		case 6704: // guthan stairs
			c.getPA().movePlayer(3578, 3284, 0);
			break;
		case 6773:
			if (Ghreborn.model.minigames.Barrows.selectCoffin(c, objectType)) {
				return;
			}
			if (c.barrowsNpcs[3][1] == 0) {
				Server.npcHandler.spawnNpc(c, 2027, c.getClickX(), c.getClickY() - 1, -1,
						0, 120, 23, 200, 200, true, true);
				c.barrowsNpcs[3][1] = 1;
			} else {
				c.sendMessage("You have already searched in this sarcophagus.");
			}
			break;

		case 6703: // dharok stairs
			c.getPA().movePlayer(3574, 3298, 0);
			break;
		case 6771:
			if (Ghreborn.model.minigames.Barrows.selectCoffin(c, objectType)) {
				return;
			}
			if (c.barrowsNpcs[4][1] == 0) {
				Server.npcHandler.spawnNpc(c, 2026, c.getClickX(), c.getClickY() - 1, -1,
						0, 120, 45, 250, 250, true, true);
				c.barrowsNpcs[4][1] = 1;
			} else {
				c.sendMessage("You have already searched in this sarcophagus.");
			}
			break;

		case 6702: // ahrim stairs
			c.getPA().movePlayer(3565, 3290, 0);
			break;
		case 6821:
			if (Ghreborn.model.minigames.Barrows.selectCoffin(c, objectType)) {
				return;
			}
			if (c.barrowsNpcs[5][1] == 0) {
				Server.npcHandler.spawnNpc(c, 2025, c.getClickX(), c.getClickY() - 1, -1,
						0, 90, 19, 200, 200, true, true);
				c.barrowsNpcs[5][1] = 1;
			} else {
				c.sendMessage("You have already searched in this sarcophagus.");
			}
			break;



		// DOORS
		case 1516:
		case 1519:
			if (c.getClickY() == 9698) {
				if (c.absY >= c.getClickY())
					c.getPA().walkTo(0, -1);
				else
					c.getPA().walkTo(0, 1);
				break;
			}
		case 1530:
		case 1531:
		case 1533:
		case 1534:
		case 11712:
		case 11711:
		case 11707:
		case 11708:
		case 6725:
		case 3198:
		case 3197:
			Server.objectHandler.doorHandling(objectType, c.getClickX(), c.getClickY(),
					0);
			break;

		case 9319:
			if (c.heightLevel == 0)
				c.getPA().movePlayer(c.absX, c.absY, 1);
			else if (c.heightLevel == 1)
				c.getPA().movePlayer(c.absX, c.absY, 2);
			break;

		case 9320:
			if (c.heightLevel == 1)
				c.getPA().movePlayer(c.absX, c.absY, 0);
			else if (c.heightLevel == 2)
				c.getPA().movePlayer(c.absX, c.absY, 1);
			break;

		case 4496:
		case 4494:
			if (c.heightLevel == 2) {
				c.getPA().movePlayer(c.absX - 5, c.absY, 1);
			} else if (c.heightLevel == 1) {
				c.getPA().movePlayer(c.absX + 5, c.absY, 0);
			}
			break;

		case 4493:
			if (c.heightLevel == 0) {
				c.getPA().movePlayer(c.absX - 5, c.absY, 1);
			} else if (c.heightLevel == 1) {
				c.getPA().movePlayer(c.absX + 5, c.absY, 2);
			}
			break;

		case 4495:
			if (c.heightLevel == 1) {
				c.getPA().movePlayer(c.absX + 5, c.absY, 2);
			}
			break;

		case 5126:
			if (c.absY == 3554)
				c.getPA().walkTo(0, 1);
			else
				c.getPA().walkTo(0, -1);
			break;

		case 1755:
			if (c.getClickX() == 2884 && c.getClickY() == 9797)
				c.getPA().movePlayer(c.absX, c.absY - 6400, 0);
			break;
		case 1759:
			if (c.getClickX() == 2884 && c.getClickY() == 3397)
				c.getPA().movePlayer(c.absX, c.absY + 6400, 0);
			break;
		case 16154:
			if (c.getClickX() == 3081 && c.getClickY() == 3420)
				c.getPA().movePlayer(1859, 5243, 0);
			break;
		case 16148:
			if (c.getClickX() == 1859 && c.getClickY() == 5244)
				c.getPA().movePlayer(3081, 3421, 0);
			break;
		case 409:
		case 27661:
			if (c.playerLevel[5] < c.getPA().getLevelForXP(c.playerXP[5])) {
				c.animation(645);
				c.playerLevel[5] = c.getPA().getLevelForXP(c.playerXP[5]);
				c.sendMessage("You recharge your prayer points.");
				c.getPA().refreshSkill(5);
			} else {
				c.sendMessage("You already have full prayer points.");
			}
			break;
		case 2873:
			if (!c.getItems().ownsCape()) {
				c.animation(645);
				c.sendMessage("Saradomin blesses you with a cape.");
				c.getItems().addItem(2412, 1);
			}
			break;
		case 2875:
			if (!c.getItems().ownsCape()) {
				c.animation(645);
				c.sendMessage("Guthix blesses you with a cape.");
				c.getItems().addItem(2413, 1);
			}
			break;
		case 2874:
			if (!c.getItems().ownsCape()) {
				c.animation(645);
				c.sendMessage("Zamorak blesses you with a cape.");
				c.getItems().addItem(2414, 1);
			}
			break;
		case 2879:
			c.getPA().movePlayer(2538, 4716, 0);
			break;
		case 2878:
			c.getPA().movePlayer(2509, 4689, 0);
			break;
		case 5960:
			c.getPA().startTeleport2(3090, 3956, 0);
			break;

		case 1815:
			c.getPA().startTeleport2(Config.EDGEVILLE_X, Config.EDGEVILLE_Y, 0);
			break;

		case 9706:
			c.getPA().startTeleport2(3105, 3951, 0);
			break;
		case 9707:
			c.getPA().startTeleport2(3105, 3956, 0);
			break;

		case 5959:
			c.getPA().startTeleport2(2539, 4712, 0);
			break;

		case 2558:
			c.sendMessage("This door is locked.");
			break;

		case 9294:
			if (c.absX < c.getClickX()) {
				c.getPA().movePlayer(c.getClickX() + 1, c.absY, 0);
			} else if (c.absX > c.getClickX()) {
				c.getPA().movePlayer(c.getClickX() - 1, c.absY, 0);
			}
			break;

		case 9293:
			if (c.absX < c.getClickX()) {
				c.getPA().movePlayer(2892, 9799, 0);
			} else {
				c.getPA().movePlayer(2886, 9799, 0);
			}
			break;
		case 10529:
		case 10527:
			if (c.absY <= c.getClickY())
				c.getPA().walkTo(0, 1);
			else
				c.getPA().walkTo(0, -1);
			break;
		case 3044:
			c.getSmithing().sendSmelting();
			break;
		case 733:
			c.animation(451);
			if (c.getClickX() == 3158 && c.getClickY() == 3951) {
				new Object(734, c.getClickX(), c.getClickY(), c.heightLevel, 1, 10,
						733, 50);
			} else {
				new Object(734, c.getClickX(), c.getClickY(), c.heightLevel, 0, 10,
						733, 50);
			}
			break;

		default:
			ScriptManager.callFunc("objectClick1_" + objectType, c, objectType,
					obX, obY);
			break;

		}
	}

	public void secondClickObject(int objectType, int obX, int obY) {
		if (Server.getMultiplayerSessionListener().inAnySession(c)) {
			return;
		}
		c.clickObjectType = 0;
/*			final String ObjectName = ObjectDef.getObjectDef(objectType).name;
			if (ObjectName.equalsIgnoreCase("bank booth") || ObjectName.equalsIgnoreCase("bank chest") || ObjectName.equalsIgnoreCase("Counter")){ 
				c.getPA().openUpBank();
				return;
				}
			if (ObjectName.equalsIgnoreCase("Furnace")){ 
				c.getSmithing().sendSmelting();
				return;
				
			}
			if (ObjectName.equalsIgnoreCase("flax")){ 
				Flax.pickFlax(c, obX, obY);
				return;
			}
			if(ObjectDef.getObjectDef(objectType).name.equals("Ladder")) {
				if(ObjectDef.getObjectDef(objectType).actions[1].equals("Climb-up")) {
					if(obX == 3069 && obY == 10256) { // custom locations
						c.getPA().movePlayer(3017, 3850, 0);
						return;
					}
					if(obX == 3017 && obY == 10249) { // custom locations
						c.getPA().movePlayer(3069, 3857, 0);
						return;
					}
					if(c.getClickY() > 6400) {
						c.getPA().movePlayer(c.getClickX(), c.getClickY()-6400, c.heightLevel);
						return;
					} else {
						c.getPA().movePlayer(c.absX, c.absY, c.heightLevel+1);
						return;
					}
				}
				if(ObjectDef.getObjectDef(objectType).actions[1].equals("Climb-down")) {
					if(obX == 3017 && obY == 3849) { // custom locations
						c.getPA().movePlayer(3069, 10257, 0);
						return;
					}
					if(obX == 3069 && obY == 3856) { // custom locations
						c.getPA().movePlayer(3017, 10248, 0);
						return;
					}
					if(obX == 1570 && obY == 2829 && c.heightLevel == 1) {
						c.getPA().movePlayer(1570, 2830, 0);
						return;
					}
					if(obX == 1560 && obY == 2829 && c.heightLevel == 1) {
						c.getPA().movePlayer(1560, 2830, 0);
						return;
					}
					if(c.getClickY() < 6400 && (c.heightLevel & 3) == 0) {
						c.getPA().movePlayer(c.getClickX(), c.getClickY()+6400, c.heightLevel);
						return;
					} else {
						c.getPA().movePlayer(c.absX, c.absY, c.heightLevel-1);
						return;
					}
				}
			}
			if(ObjectDef.getObjectDef(objectType).name.equals("Staircase")) {
				if(ObjectDef.getObjectDef(objectType).actions[1].equals("Climb-up")) {
					c.getPA().movePlayer(c.absX, c.absY, c.heightLevel+1);
					return;
				}
				if(ObjectDef.getObjectDef(objectType).actions[1].equals("Climb-down")) {
					c.getPA().movePlayer(c.absX, c.absY, c.heightLevel-1);
					return;
				}
			}*/
			if (c.playerCannon != null && objectType == 6) {
				if (c.playerCannon.pickUpCannon(objectType, obX, obY)) {
					PlayerHandler.removeCannon(c.playerCannon);
					c.playerCannon = null;
				}
			} else if (PlayerCannon.CannonPart.isObjCannon(objectType)) {
				c.sendMessage("This is not your cannon!");
			}

		if (c.isMorphed) {
			return;
			}
		Location3D location = new Location3D(obX, obY, c.heightLevel);
		// c.sendMessage("Object type: " + objectType);
		switch (objectType) {
		case 4525:
			c.getHouse().setLocked(!c.getHouse().isLocked());
			break;
		case 28857:
			if(obX == 1570 && obY == 2830 && c.heightLevel == 0){
				c.getPA().movePlayer(1570, 2829, 1);
			}
			if(obX == 1560 && obY == 2830 && c.heightLevel == 0){
				c.getPA().movePlayer(1560, 2829, 1);
			}
			break;
		case 8551:
			c.getPA().sendFrame36(504, ((8551 << 8) << 16));
break;
		case 11730:
			c.getThieving().steal(Stall.CAKE, objectType, location);
			break;
		case 11731:
			c.getThieving().steal(Stall.GEM, objectType, location);
			break;
		case 11733:
			c.getThieving().steal(Stall.SPICE, objectType, location);
			break;
		case 11732:
			c.getThieving().steal(Stall.FUR, objectType, location);
			break;
		case 11734:
			c.getThieving().steal(Stall.SILVER, objectType, location);
			break;
		case 11729:
			c.getThieving().steal(Stall.SILK, objectType, location);
			break;
		case 14011:
			c.getThieving().steal(Stall.WINE, objectType, location);
			break;

		case 2558:
			if (System.currentTimeMillis() - c.lastLockPick < 3000
					|| c.freezeTimer > 0)
				break;
			if (c.getItems().playerHasItem(1523, 1)) {
				c.lastLockPick = System.currentTimeMillis();
				if (Misc.random(10) <= 3) {
					c.sendMessage("You fail to pick the lock.");
					break;
				}
				if (c.getClickX() == 3044 && c.getClickY() == 3956) {
					if (c.absX == 3045) {
						c.getPA().walkTo2(-1, 0);
					} else if (c.absX == 3044) {
						c.getPA().walkTo2(1, 0);
					}

				} else if (c.getClickX() == 3038 && c.getClickY() == 3956) {
					if (c.absX == 3037) {
						c.getPA().walkTo2(1, 0);
					} else if (c.absX == 3038) {
						c.getPA().walkTo2(-1, 0);
					}
				} else if (c.getClickX() == 3041 && c.getClickY() == 3959) {
					if (c.absY == 3960) {
						c.getPA().walkTo2(0, -1);
					} else if (c.absY == 3959) {
						c.getPA().walkTo2(0, 1);
					}
				}
			} else {
				c.sendMessage("I need a lockpick to pick this lock.");
			}
			break;
		default:
			ScriptManager.callFunc("objectClick2_" + objectType, c, objectType,
					obX, obY);
			break;
		}
	}
	public void messageall(String message){
	for (int i = 0; i < Server.playerHandler.players.length; i++) {
	if (Server.playerHandler.players[i] != null) {
	Client c2 = (Client)Server.playerHandler.players[i];
	c2.sendMessage(message);
	}
	}
	}
	public void thirdClickObject(int objectType, int obX, int obY) {
		if (Server.getMultiplayerSessionListener().inAnySession(c)) {
			return;
		}
		c.clickObjectType = 0;
		//c.sendMessage("Object type: " + objectType);
		if (c.isMorphed) {
			return;
			}
		switch (objectType) {
		default:
			ScriptManager.callFunc("objectClick3_" + objectType, c, objectType,
					obX, obY);
			break;
		}
	}

	public void firstClickNpc(int i) {
		if (Server.getMultiplayerSessionListener().inAnySession(c)) {
			return;
		}
		c.rememberNpcIndex = c.npcClickIndex;
		int idx = c.npcClickIndex;
		c.clickNpcType = 0;
		c.npcClickIndex = 0;
		if (c.isMorphed) {
			return;
			}
		if(c.getRights().isBetween(9, 10)) {
			c.sendMessage("First Npc Click: " + i+".");
	}
		if(Implings.Imps.implings.containsKey(i)) {
			Implings.Imps.catchImp(c, i, idx);
		}
		if(Butterflys.Flys.Butterflys.containsKey(i)) {
			Butterflys.catchButterfly(c, i, idx);
		}
		switch (i) {
		case 8059:
			c.getVorkath().wakeUp();
			break;
		case 3260://man
		case 3078: //Man
		case 3079: //Man
		case 3080: //Man
		case 3081: //Man
		case 3082: //Man
		case 3083: //Woman
		case 3084:// Woman
		case 3085: //Woman
		case 3263: //Drunken man
		case 3264:// Man
		case 3265:// Man
		case 3266:// Man
		case 3267:// Woman
		case 3268:// Woman
		case 3652: //Man
		case 3014: //Man
		case 3015: //Woman
			c.start(new ManandWomanDialogue());
			break;
		case 8494:
			c.start(new HansXmasDialogue());
			break;
		case 3077:
			c.start(new HansDialogue());
			break;
		case 3216:
			c.start(new Melee_combat_tutorDialogue());
			break;
		case 306:
			c.start(new Lumbridge_guide_Dialogue());
			break;
		case 706:
			c.getDH().sendDialogues(9, i);
			break;
		case 2461:
			c.getWarriorsGuild().handleDoor();
			break;
		case 2135:
			c.getWarriorsGuildBasement().handleDoor();
			break;
		case 2794:
			Server.npcHandler.shearSheep(c, 1735, 1737, 893, 2794, 2789, 15);
			break;
		case 732:
			c.start(new FredTheFarmer());
			break;
		case 7238:
			c.start(new ForesterDialogue());
			break;
		case 1331:
			c.start(new TraderCrewMemberMale());
			break;
		case 2147:
			c.start(new VeosDialogue());
			break;
		case 1334:
			c.start(new TraderCrewMemberFemale());
			break;
		case 1909:
			c.getPA().sendFrame126("https://ghreborn.everythingrs.com/services/vote", 12000);
			break;
		case 2258:
			c.getDH().sendDialogues(17, i);
			break;
		case 1599:
			if (c.slayerTask <= 0) {
				c.getDH().sendDialogues(11, i);
			} else {
				c.getDH().sendDialogues(13, i);
			}
			break;

		case 1304:
			c.getDH().sendOption5("Home", "Edgeville", "Island",
					"Dagannoth Kings", "Next Page");
			c.teleAction = 1;
			break;

		case 528:
			if(!c.getRights().isIronman()) {
			c.getShops().openShop(1);
			}else {
				c.sendMessage("You cant use this shop as a ironman.");
			}
			break;
		case ZenyteDialogue.NPC_ID:
			c.start(new ZenyteDialogue());
			break;
		case 3231:
			c.start(new EllisDialogue());
			break;
		case 944:
			if(!c.getRights().isIronman()) {
			c.getShops().openShop(42);
			}else {
				c.sendMessage("You cant use this shop as a ironman.");
			}
			break; 
		case 461:
			if(!c.getRights().isIronman()) {
			c.getShops().openShop(2);
			}else {
				
			}
			break;

		case 683:
			if(!c.getRights().isIronman()) {
			c.getShops().openShop(3);
			}else {
				
			}
			break;

		case 586:
			if(!c.getRights().isIronman()) {
			c.getShops().openShop(4);
			}else {
				
			}
			break;

		case 555:
			if(!c.getRights().isIronman()) {
			c.getShops().openShop(6);
			}else {
				
			}
			break;

		case 519:
			if(!c.getRights().isIronman()) {
			c.getShops().openShop(8);
			}else {
				
			}
			break;

		case 1700:
			if(!c.getRights().isIronman()) {
			c.getShops().openShop(19);
			}else {
				
			}
			break;

		case 251:
			if(!c.getRights().isIronman()) {
			c.getShops().openShop(60);
			}else {
				
			}
			break;

		case 1282:
			if(!c.getRights().isIronman()) {
			c.getShops().openShop(7);
			}else {
				
			}
			break;

		case 1152:
			c.getDH().sendDialogues(16, i);
			break;

		case 494:
			c.getPA().openUpBank();
			break;

		case 3789:
			c.sendMessage((new StringBuilder()).append("You currently have ")
					.append(c.pcPoints).append(" pest control points.")
					.toString());
			break;

		case 3788:
			c.getShops().openVoid();
			break;

		case 905:
			c.getDH().sendDialogues(5, i);
			break;

		case 460:
			c.getDH().sendDialogues(3, i);
			break;

		case 462:
			c.getDH().sendDialogues(7, i);
			break;

			// FISHING
			case 1530: // NET + BAIT
			case 1518:
				// Fishing.attemptdata(c, 1);
				Fishing.startFishing(c, 1, i);
				break;
				
			//case 1520: // NET + BAIT
				// Fishing.attemptdata(c, 1);
				//Fishing.startFishing(c, 3, i);
				//break;
			/*
			 * case 3317: Fishing.attemptdata(c, 14); break;
			 */
			case 4712:
				// Fishing.attemptdata(c, 15);
				Fishing.startFishing(c, 8, i);
				break;
			case 1524:
				// Fishing.attemptdata(c, 11);
				Fishing.startFishing(c, 10, i);
				break;
			case 3417: // TROUT
				Fishing.startFishing(c, 3, i);
				break;
			case 3657:
				// Fishing.attemptdata(c, 8);
				Fishing.startFishing(c, 6, i);
				break;
			case 635:
				// Fishing.attemptdata(c, 13); // DARK CRAB FISHING
				Fishing.startFishing(c, 13, i);
				break;
			case 1527: // LURE
			case 310:
			case 314:
			case 318:
			case 328:
			case 331:
				// Fishing.attemptdata(c, 9);
				Fishing.startFishing(c, 7, i);
				break;


		case 599:
			c.getPA().showInterface(3559);
			c.canChangeAppearance = true;
			break;

		case 904:
			c.sendMessage((new StringBuilder()).append("You have ")
					.append(c.magePoints).append(" points.").toString());
			break;
		}
	}

	public void secondClickNpc(int npcType, int i) {
		if (Server.getMultiplayerSessionListener().inAnySession(c)) {
			return;
		}
		c.clickNpcType = 0;
		c.rememberNpcIndex = c.npcClickIndex;
		c.npcClickIndex = 0;
		if (c.isMorphed) {
			return;
			}
		if(c.getRights().isBetween(9, 10)) {
			c.sendMessage("Second Npc Click: " + npcType+".");
	}
		if(i >= 761 && i <= 773 && i != 767) {
			c.getDH().sendDialogues(908, i);
		}
		if (PetHandler.pickupPet(c, npcType))
			return;
		switch (npcType) {
		case 585:
			if(!c.getRights().isIronman()) {
			c.getShops().openShop(14);
			}else {
				
			}
			break;
		case 1909:
			c.start(new VoteShopsDialogue());
			//c.getShops().openShop(251);
			break;
			case 505:
				if(!c.getRights().isIronman()) {
				c.getShops().openShop(3);
				}else {
					
				}
				break;
				
			case 731:
				c.start(new SheepDialogue());
				break;

		case 508:
			if(!c.getRights().isIronman()) {
			c.getShops().openShop(49);
			}else {
				
			}
			break;
		case 1023:
			if(!c.getRights().isIronman()) {
			c.getShops().openShop(73);
			}else {
				
			}
			break;
		case 3247:
			if(!c.getRights().isIronman()) {
			c.getShops().openShop(34);
			}else {
				
			}
			break;
		case 1045:
			if(!c.getRights().isIronman()) {
			c.getShops().openShop(74);
			}else {
				
			}
			break;
		case 7913:
			if(c.getRights().isDonator() || c.getRights().isStaff()) {
				c.getShops().openShop(250);
			}else {
				c.sendMessage("You need to be an donator to use this shop.");
			}
			break;
		case 1030:
			if(c.getRights().isDonator() || c.getRights().isStaff()) {
				c.getShops().openShop(256);
			}else {
				c.sendMessage("You need to be an donator to use this shop.");
			}
			break;
		case 2180:
			c.getPA().TzTokLottery();
			break;
		case 1172:
			if(!c.getRights().isIronman()) {
			c.getShops().openShop(56);
			}else {
				
			}
			break;
		case 2148:
		case 2149:	
		case 2150:
		case 2151:
			c.getPA().showInterface(25000);
			break;
		case 3231:
			Tanning.sendTanningInterface(c);
			break;
		case 4656:
		case 4650:
		case 4653:
			if(!c.getRights().isIronman()) {
			c.getShops().openShop(109);
			}else {
				
			}
			break;
			
		case 530:
			if(!c.getRights().isIronman()) {
			c.getShops().openShop(59);
			}else {
				
			}
			break;

		case 461:
			if(!c.getRights().isIronman()) {
			c.getShops().openShop(34);
			}else {
				
			}
			break; 
		case 2304:
		case 2305:
			if(!c.getRights().isIronman()) {
			c.getShops().openShop(45);
			}else {
				
			}
			break;

			
		case 5422:
			c.getPA().showInterface(22403);
			break;
		case 548:
			if(!c.getRights().isIronman()) {
			c.getShops().openShop(56);
			}else {
				
			}
			break;
			
		case 209:
			if(!c.getRights().isIronman()) {
			c.getShops().openShop(40);
			}else {
				
			}
			break;

		case 1699:
			if(!c.getRights().isIronman()) {
			c.getShops().openShop(51);
			}else {
				
			}
			break;

		case 555:
			if(!c.getRights().isIronman()) {
			c.getShops().openShop(3);
			}else {
				
			}
			break;
			
		case 1530: // BAIT + NET
		case 1518:
			// Fishing.attemptdata(c, 2);
			Fishing.startFishing(c, 2, i);
			break;
		case 1527: // BAIT + LURE //PIKE
			// Fishing.attemptdata(c, 6);
			Fishing.startFishing(c, 4, i);
			break;
		case 1524:
			Fishing.startFishing(c, 12, i);
			break;
		case 3657:
		case 1519:
		case 1510:
		case 324:// SWORDIES+TUNA-CAGE+HARPOON
			// Fishing.attemptdata(c, 7);
			Fishing.startFishing(c, 5, i);
			break;
		case 1520:
		case 1511:
		case 334: // NET+HARPOON
			// Fishing.attemptdata(c, 10);
			Fishing.startFishing(c, 9, i);
			break;
		case 4928: // Sacred Eel

			break;

		case 3788:
			c.getShops().openVoid();
			break;

		case 494:
			c.getPA().openUpBank();
			break;

		case 527:
			c.getShops().openSkillCape();
			break;



		case 506:
			if(!c.getRights().isIronman()) {
			c.getShops().openShop(55);
			}else {
				
			}
			break;
			
		case 1044:
			if(!c.getRights().isIronman()) {
			c.getShops().openShop(11);
			}else {
				
			}
			break;
		case 2184:
			if(!c.getRights().isIronman()) {
			c.getShops().openShop(197);
			}else {
				
			}
			break;
		case 2183:
			if(!c.getRights().isIronman()) {
			c.getShops().openShop(196);
			}else {
				
			}
			break;
		case 2185:
			if(!c.getRights().isIronman()) {
			c.getShops().openShop(198);
			}else {
				
			}
			break;
			

		case 3078:
			c.getThieving().steal(Pickpocket.MAN, NPCHandler.npcs[c.rememberNpcIndex]);
			break;
		default:
			ScriptManager.callFunc("npcClick2_" + npcType, c, npcType);
			if (c.rights == Rights.OWNER)
				Misc.println("2nd Click NPC : " + npcType);
			break;
		}
	}

	public void thirdClickNpc(int npcType) {
		if (Server.getMultiplayerSessionListener().inAnySession(c)) {
			return;
		}
		c.clickNpcType = 0;
		c.npcClickIndex = 0;
		if (c.isMorphed) {
			return;
			}
		if(c.getRights().isBetween(9, 10)) {
			c.sendMessage("Third Npc Click: " + npcType+".");
	}
		switch (npcType) {
		case 5422:
			if(!c.getRights().isIronman()) {
			c.getShops().openShop(60);
			}else {
				
			}
			break;
		case 1909:
			c.sendMessage("You Have "+c.votePoints+" Vote Points.", 255);
			break;
		default:
			ScriptManager.callFunc("npcClick3_" + npcType, c, npcType);
			if (c.getRights().isOwner())
				Misc.println("Third Click NPC : " + npcType);
			break;

		}
	}

	public void fourthClickNpc(int npcType) {
		if (Server.getMultiplayerSessionListener().inAnySession(c)) {
			return;
		}
		c.clickNpcType = 0;
		c.npcClickIndex = 0;
		if (c.isMorphed) {
			return;
			}
		if(c.getRights().isBetween(9, 10)) {
			c.sendMessage("Fourth Npc Click: " + npcType+".");
	}
		switch(npcType) {
			case 2575:
				c.sendMessage("woooooww, fourth nlick nps works!");
				break;
			case 5110:
				if(!c.getRights().isIronman()) {
				c.getShops().openShop(119);
				}else {
					
				}
				break;
			default:
				ScriptManager.callFunc("npcClick4_"+npcType, c, npcType);
				if(c.getRights().isOwner()) 
					Misc.println("Fourth Click NPC : "+npcType);
				break;

		}
	}

	public void fourthClickObject(int objectType, int obX, int obY) {
		if (Server.getMultiplayerSessionListener().inAnySession(c)) {
			return;
		}
		c.clickObjectType = 0;
		c.face(obX, obY);
		if (c.isMorphed) {
			return;
			}
		Construction.handleConstructionClick(c, objectType, obX, obY);
		 c.sendMessage("Object type4: " + objectType);
		switch (objectType) {
		case 25016:
		case 25017:
		case 25018:
		case 25029:
			PuroPuro.magicalWheat(c);
			break;
		default:
			ScriptManager.callFunc("objectClick" + objectType, c, objectType,
					obX, obY);
			break;

		}
	}
	public void fifthClickObject(int objectType, int obX, int obY) {
		if (Server.getMultiplayerSessionListener().inAnySession(c)) {
			return;
		}
		c.clickObjectType = 0;
		c.turnPlayerTo(obX, obY);
		if (c.isMorphed) {
			return;
			}
		 c.sendMessage("Object type 5: " + objectType);
		switch (objectType) {
		case 25016:
		case 25017:
		case 25018:
		case 25029:
			PuroPuro.magicalWheat(c);
			break;
		default:
			ScriptManager.callFunc("objectClick" + objectType, c, objectType,
					obX, obY);
			break;

		}
	}

	public void operateNpcAction4(int npcType) {
		if (Server.getMultiplayerSessionListener().inAnySession(c)) {
			return;
		}
		c.clickNpcType = 0;
		c.rememberNpcIndex = c.npcClickIndex;
		c.npcClickIndex = 0;

		switch (npcType) {
		case 402:
		case 405:
		case 490:
			c.getSlayer().handleInterface("buy");
			break;

		case 315:
			c.getDH().sendDialogues(545, npcType);
			break;
		}
	}

}