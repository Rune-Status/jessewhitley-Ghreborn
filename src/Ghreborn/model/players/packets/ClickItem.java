package Ghreborn.model.players.packets;


import java.util.Optional;

import Ghreborn.Config;
import Ghreborn.Server;
import Ghreborn.core.PlayerHandler;
import Ghreborn.event.CycleEvent;
import Ghreborn.event.CycleEventContainer;
import Ghreborn.event.CycleEventHandler;
import Ghreborn.model.content.teleport.Tablets;
import Ghreborn.model.item.Nests;
import Ghreborn.model.items.Item2;
import Ghreborn.model.items.ItemDefinition;
import Ghreborn.model.minigames.treasuretrails.ClueScroll;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.PacketType;
import Ghreborn.model.players.PlayerCannon;
import Ghreborn.model.players.Rights;
import Ghreborn.model.players.TeleportTablets;
import Ghreborn.model.players.skills.hunter.Hunter;
import Ghreborn.model.players.skills.hunter.trap.impl.BirdSnare;
import Ghreborn.model.players.skills.hunter.trap.impl.BoxTrap;
import Ghreborn.model.players.skills.prayer.Bone;
import Ghreborn.model.players.skills.prayer.Prayer;
import Ghreborn.model.players.skills.runecrafting.Pouches;
import Ghreborn.model.players.skills.runecrafting.Pouches.Pouch;
import Ghreborn.net.Packet;
import Ghreborn.util.Misc;

/**
 * Clicking an item, bury bone, eat food etc
 **/
public class ClickItem implements PacketType {

	public static int flower[] = {2980,2981,2982,2983,2984,2985,2986,2987};
	public int randomflower() {
                return flower[(int)(Math.random()*flower.length)];
        }
	public static int flowerX = 0;
	public static int flowerY = 0;
	public static int flowerTime = -1;
	public static int flowers = 0;
	
	@Override
	public void processPacket(Client c, Packet packet) {
		int frame = packet.getLEShortA(); // use to be
		// readSignedWordBigEndianA();
int itemSlot = packet.getShortA(); // use to be
// readUnsignedWordA();
int itemId = packet.getLEShort(); // us to be
		if (!c.canUsePackets && (itemId != 10025)) {
			return;
		}
		
		if (itemSlot >= c.playerItems.length || itemSlot < 0) {
			return;
		}
		if (itemId != c.playerItems[itemSlot] - 1) {
			return;
		}
		if (c.isDead || c.playerLevel[3] <= 0) {
			return;
		}
		if (c.getRights().isDeveloper() && Config.SERVER_DEBUG) {
			Misc.println(c.playerName + " - FirstItemOption: " + itemId);
		}
        if(Nests.handleNest(c, itemId)){
            return;
        }
		c.lastClickedItem = itemId;
		c.getHerblore().clean(itemId);
		//Tablets.teleport(c, itemId);
		switch (itemId) {
		case 5509:
			Pouches.fill(c, Pouch.forId(itemId), itemId, 0);
			break;
		case 5510:
			Pouches.fill(c, Pouch.forId(itemId), itemId, 1);
			break;
		case 5512:
			Pouches.fill(c, Pouch.forId(itemId), itemId, 2);
			break;
		}
		c.getFlowers().plantMithrilSeed(itemId);
		if (c.getFood().isFood(itemId)) {
			c.getFood().eat(itemId, itemSlot);
		} else if (c.getPotions().isPotion(itemId)) {
			c.getPotions().handlePotion(itemId, itemSlot);
		}
		if (itemId == 23958) //Money bag
			if (c.getItems().playerHasItem(23958)) {
				c.getMoneyBag().open();
				return;
			}
		/* Mystery box */
        if (itemId == 6199)
            if (c.getItems().playerHasItem(6199)) {
                c.getMysteryBox().open();
                return;
            }
		if (itemId == 20703) //Daily Gear Box
			if (c.getItems().playerHasItem(20703)) {
				c.getDailyGearBox().open();
				return;
			}
		if (itemId == 20791) //Daily Skilling Box
			if (c.getItems().playerHasItem(20791)) {
				c.getDailySkillBox().open();
				return;
			}
		if (itemId == 23959) //SmallMoney bag
			if (c.getItems().playerHasItem(23959)) {
				c.getItems().deleteItem(23959, 1);
				c.getItems().addItem(995, 10000+Misc.random(10000));
			}
		if (itemId == 23960) //SmallMoney bag
			if (c.getItems().playerHasItem(23960)) {
				c.getItems().deleteItem(23960, 1);
				c.getItems().addItem(995, 50000+Misc.random(50000));
			}
		if (itemId == 23961) //SmallMoney bag
			if (c.getItems().playerHasItem(23961)) {
				c.getItems().deleteItem(23961, 1);
				c.getItems().addItem(995, 100000+Misc.random(100000));
			}
		if (itemId == 23962) //SmallMoney bag
			if (c.getItems().playerHasItem(23962)) {
				c.getItems().deleteItem(23962, 1);
				c.getItems().addItem(995, 5000000+Misc.random(5000000));
			}
		if (itemId == 4155) {
			if (!c.getSlayer().getTask().isPresent()) {
				c.sendMessage("You do not have a task, please talk with a slayer master!", 255);
				return;
			}
			c.sendMessage("I currently have " + c.getSlayer().getTaskAmount() + " " + c.getSlayer().getTask().get().getPrimaryName() + " to kill.", 255);
			c.getPA().closeAllWindows();
		}
		if (itemId == 2839) {
			if (c.getSlayer().isHelmetCreatable() == true) {
				c.sendMessage("You have already learned this recipe. You have no more use for this scroll.", 255);
				return;
			}
			if (c.getItems().playerHasItem(2839)) {
				c.getSlayer().setHelmetCreatable(true);
				c.sendMessage("You have learned the slayer helmet recipe. You can now assemble it");
				c.sendMessage("using a <col=0000FF>Black Mask<col=000000>, <col=0000FF>Facemask<col=000000>, <col=0000FF>Nose peg<col=000000>, <col=0000FF>Spiny helmet<col=000000> and <col=0000FF>Earmuffs<col=000000>.");
				c.getItems().deleteItem(2839, 1);
			}
		}
		Optional<Bone> bone = Prayer.isOperableBone(itemId);
		if (bone.isPresent()) {
			c.getPrayer().bury(bone.get());
			return;
		}
		TeleportTablets.operate(c, itemId);
		if (itemId == 11941) {
			c.setSidebarInterface(3, 26700);
			if (c.itemsInLootBag == 0) {
				return;
			} else
				c.getLoot().updateLootbagInterface();
			//c.getItems().resetItemsLoot(26701);
			//c.sendMessage("Worked.");
		}
		if (itemId == 2379) {
			//if (c.getPoisonDamage() > 0 || c.getVenomDamage() > 0) {
				//c.sendMessage("You are poisoned or effected by venom, you should heal this first.");
				//return;
			//}
			if (c.playerLevel[c.playerHitpoints] == 1) {
				c.sendMessage("I better not do that.");
				return;
			}
			c.sendMessage("Wow, the rock exploded in your mouth. That looks like it hurt.");
			c.playerLevel[c.playerHitpoints] = 1;
			c.getPA().refreshSkill(c.playerHitpoints);
			c.getItems().deleteItem2(itemId, 1);
			return;
		}
		if (itemId == 11877) { //Empty Vial Pack
		    if (c.getItems().hasFreeSlots(2)) {
			    c.getItems().deleteItem(11877, 1);
		        c.getItems().addItem(230, 100);
		    }
		}
		if (itemId == 11879) { //Water-Filled Vial Pack
		    if (c.getItems().hasFreeSlots(2)) {
			    c.getItems().deleteItem(11879, 1);
		        c.getItems().addItem(228, 100);
		    }
		}
		if (itemId == 11881) { //Feather Pack
		    if (c.getItems().hasFreeSlots(2)) {
			    c.getItems().deleteItem(11881, 1);
		        c.getItems().addItem(314, 100);
		    }
		}
		if (itemId == 11883) { //Bait Pack
		    if (c.getItems().hasFreeSlots(2)) {
			    c.getItems().deleteItem(11883, 1);
		        c.getItems().addItem(313, 100);
		    }
		}
		if (itemId == 11885) { //Broad Arrowhead Pack
		    if (c.getItems().hasFreeSlots(2)) {
			    c.getItems().deleteItem(11885, 1);
		        c.getItems().addItem(11874, 100);
		    }
		}
		if (itemId == 11887) { //Unfinished broad bolt Pack
		    if (c.getItems().hasFreeSlots(2)) {
			    c.getItems().deleteItem(11887, 1);
		        c.getItems().addItem(11876, 100);
		    }
		}
		if (itemId == 12009) { //Soft Clay Pack
		    if (c.getItems().hasFreeSlots(2)) {
			    c.getItems().deleteItem(12009, 1);
		        c.getItems().addItem(1762, 100);
		    }
		}
		if (itemId == 12641) { //Amylase Pack
		    if (c.getItems().hasFreeSlots(2)) {
			    c.getItems().deleteItem(12641, 1);
		        c.getItems().addItem(12640, 100);
		    }
		}
		if (itemId == 12728) { //Air rune Pack
		    if (c.getItems().hasFreeSlots(2)) {
			    c.getItems().deleteItem(12728, 1);
		        c.getItems().addItem(556, 100);
		    }
		}
		if (itemId == 12730) { //Water rune Pack
		    if (c.getItems().hasFreeSlots(2)) {
			    c.getItems().deleteItem(12730, 1);
		        c.getItems().addItem(555, 100);
		    }
		}
		if (itemId == 12732) { //Earth rune Pack
		    if (c.getItems().hasFreeSlots(2)) {
			    c.getItems().deleteItem(12732, 1);
		        c.getItems().addItem(557, 100);
		    }
		}
		if (itemId == 12734) { //Fire rune Pack
		    if (c.getItems().hasFreeSlots(2)) {
			    c.getItems().deleteItem(12734, 1);
		        c.getItems().addItem(554, 100);
		    }
		}
		if (itemId == 12736) { //Mind rune Pack
		    if (c.getItems().hasFreeSlots(2)) {
			    c.getItems().deleteItem(12736, 1);
		        c.getItems().addItem(558, 100);
		    }
		}
		if (itemId == 12738) { //Chaos rune Pack
		    if (c.getItems().hasFreeSlots(2)) {
			    c.getItems().deleteItem(12738, 1);
		        c.getItems().addItem(562, 100);
		    }
		}
		if (itemId == 12740) { //Bird snare Pack
		    if (c.getItems().hasFreeSlots(2)) {
			    c.getItems().deleteItem(12740, 1);
		        c.getItems().addItem(10007, 100);
		    }
		}
		if (itemId == 12742) { //Box trap Pack
		    if (c.getItems().hasFreeSlots(2)) {
			    c.getItems().deleteItem(12742, 1);
		        c.getItems().addItem(10009, 100);
		    }
		}
		if (itemId == 12744) { //Magic imp box Pack
		    if (c.getItems().hasFreeSlots(2)) {
			    c.getItems().deleteItem(12744, 1);
		        c.getItems().addItem(10026, 100);
		    }
		}
		if (itemId == 12857) { //Olive oil Pack
		    if (c.getItems().hasFreeSlots(2)) {
			    c.getItems().deleteItem(12857, 1);
		        c.getItems().addItem(3423, 100);
		    }
		}
		if (itemId == 12859) { //Eye of Newt Pack
		    if (c.getItems().hasFreeSlots(2)) {
			    c.getItems().deleteItem(12859, 1);
		        c.getItems().addItem(222, 100);
		    }
		}
		if (itemId == 13193) { //Bone bolt Pack
		    if (c.getItems().hasFreeSlots(2)) {
			    c.getItems().deleteItem(13193, 1);
		        c.getItems().addItem(8882, 100);
		    }
		}
		if (itemId == 13250) { //Plant pot Pack
		    if (c.getItems().hasFreeSlots(2)) {
			    c.getItems().deleteItem(13250, 1);
		        c.getItems().addItem(5357, 100);
		    }
		}
		if (itemId == 13252) { //Sack Pack
		    if (c.getItems().hasFreeSlots(2)) {
			    c.getItems().deleteItem(13252, 1);
		        c.getItems().addItem(5419, 100);
		    }
		}
		if (itemId == 13254) { //Basket Pack
		    if (c.getItems().hasFreeSlots(2)) {
			    c.getItems().deleteItem(13254, 1);
		        c.getItems().addItem(5377, 100);
		    }
		}
		if (itemId == 13432) { //Sand Worms Pack
		    if (c.getItems().hasFreeSlots(2)) {
			    c.getItems().deleteItem(13432, 1);
		        c.getItems().addItem(13431, 100);
		    }
		}
		if (itemId == 19704) { //Compost Pack
		    if (c.getItems().hasFreeSlots(2)) {
			    c.getItems().deleteItem(19704, 1);
		        c.getItems().addItem(6033, 100);
		    }
		}
		if (itemId == 20742) { //Empty Jug Pack
		    if (c.getItems().hasFreeSlots(2)) {
			    c.getItems().deleteItem(20742, 1);
		        c.getItems().addItem(3734, 100);
		    }
		}
		if (itemId == 13040) { //Saradomin armour set
		    if (c.getItems().hasFreeSlots(4)) {
			    c.getItems().deleteItem(13040, 1);
		        c.getItems().addItem(2661, 1);
			c.getItems().addItem(2663, 1);
		    	c.getItems().addItem(2665, 1);
			c.getItems().addItem(2667, 1);
			}
		}
		if(itemId == 7776) {
			c.getItems().deleteItem2(7776, 1);
			c.votePoints += 1;
		}
		if (itemId == 13044) { //Zammy armour set
		    if (c.getItems().hasFreeSlots(4)) {
			    c.getItems().deleteItem(13044, 1);
		        c.getItems().addItem(2653, 1);
			c.getItems().addItem(2655, 1);
		    	c.getItems().addItem(2657, 1);
			c.getItems().addItem(2659, 1);
			}
		}
		if (itemId == 13048) { //Guthix armour set
		    if (c.getItems().hasFreeSlots(4)) {
			    c.getItems().deleteItem(13048, 1);
		        c.getItems().addItem(2669, 1);
			c.getItems().addItem(2671, 1);
		    	c.getItems().addItem(2673, 1);
			c.getItems().addItem(2675, 1);
			}
		}
		if(itemId == 16542)
if(c.getItems().playerHasItem(16542)){
	c.getDonatorBox().open();
	return;
}
		if(itemId == 16543)
if(c.getItems().playerHasItem(16543)){
	c.getSuperDonatorBox().open();
	return;
}
		if(itemId == 16544)
if(c.getItems().playerHasItem(16544)){
	c.getExtremeDonatorBox().open();
	return;
}
		if(itemId == 16547) {
			if(c.getItems().hasFreeSlots(20)) {
			c.setRights(Rights.LEGENDARY_DONATOR);
			c.getItems().deleteItem(16547, 1);
			c.getItems().addItem(Item2.randomGH(), 1);
			c.getItems().addItem(Item2.randomGH(), 1);
			c.getItems().addItem(Item2.randomGH(), 1);
			c.getItems().addItem(Item2.randomGH(), 1);
			c.getItems().addItem(Item2.randomGH(), 1);
			c.getItems().addItem(Item2.randomGH(), 1);
			c.getItems().addItem(Item2.randomGH(), 1);
			c.getItems().addItem(Item2.randomGH(), 1);
			c.getItems().addItem(Item2.randomGH(), 1);
			c.getItems().addItem(Item2.randomGH(), 1);
			c.getItems().addItem(Item2.randomGH(), 1);
			c.getItems().addItem(Item2.randomGH(), 1);
			c.getItems().addItem(Item2.randomGH(), 1);
			c.getItems().addItem(Item2.randomGH(), 1);
			c.getItems().addItem(Item2.randomGH(), 1);
			c.getItems().addItem(Item2.randomGH(), 1);
			c.getItems().addItem(Item2.randomGH(), 1);
			c.getItems().addItem(Item2.randomGH(), 1);
			c.getItems().addItem(Item2.randomGH(), 1);
			c.getItems().addItem(Item2.randomGH(), 1);
			c.sendMessage("You Open the Legendary Donator Box. ");
			c.logout();
			
		}else {
			c.sendMessage("you need 20 inv slots open.");
		}
		}
		if(itemId == 16548) {
			if(c.getItems().hasFreeSlots(1)) {
			c.setRights(Rights.RAINBOW_DONATOR);
			c.getItems().deleteItem(16548, 1);
			c.sendMessage("You Open the Rainbow Donator Box. ");
			c.logout();
			
		}else {
			c.sendMessage("you need 1 inv slots open.");
		}
		}
		if(itemId == 6) {
			if(c.playerCannon == null) {
				if( c.inWild() || c.inDuelArena() || c.inDuel || c.inTrade) {
					c.sendMessage("You cannot put a cannon down here!");
				} else if(PlayerHandler.cannonExists(c.absX, c.absY, c.heightLevel)) {
					c.sendMessage("A cannon already exists here!");
				} else {				
					PlayerHandler.addNewCannon(c.playerCannon = new PlayerCannon(c).setUpCannon());
				}
			} else {
				c.sendMessage("You already have a cannon put down!");
			}
		}
		if(itemId == 10025) {
			c.sendMessage("You open the box to find supplies!");
			c.getItems().deleteItem(10025, 1);
			c.getItems().addItem(2436, 1);
			c.getItems().addItem(2442, 1);
			c.getItems().addItem(2440, 1);
			c.getItems().addItem(385, 6);

		}
		if (itemId == 11738){
			int[] Secondaries = {222, 236, 226, 224, 1976, 5106, 9737, 232, 2971, 240, 6050, 244, 246, 3139, 248, 260, 12934, 270};
			int randomSecondary = Secondaries[Misc.random(Secondaries.length - 1)];
			int[] grimyHerbs = {200, 202, 204, 206, 208, 210, 212, 214, 216, 218, 220, 1525, 2486, 3050, 3052};
			int randomHerb = grimyHerbs[Misc.random(grimyHerbs.length - 1)];
			int randomHerb2 = grimyHerbs[Misc.random(grimyHerbs.length - 1)];
			if (c.getItems().freeSlots() < 3) {
				c.sendMessage("You need 3 free slots to open this box.");
				return;
			} 
			if(c.getItems().playerHasItem(11738)){
				c.getItems().deleteItem2(11738, 1);
				c.getItems().addItem(randomSecondary, 10);
				c.getItems().addItem(randomHerb, 1);
				c.getItems().addItem(randomHerb2, 1);

			}
			
		}
		if (itemId == 16545){ // Lucky Box
			int[] Luckys = {15340, 15341, 15342, 15343, 25342, 25343,25344,25345, 25346, 25347, 25348, 25349};
			int randomLucky = Luckys[Misc.random(Luckys.length - 1)];
			if (c.getItems().freeSlots() < 1) {
				c.sendMessage("You need 3 free slots to open this box.");
				return;
			} 
			if(c.getItems().playerHasItem(16545)){
	        	   c.getItems().deleteItem2(16545, 1);
	        	   c.getItems().addItem(randomLucky, 1);
	        	   c.sendMessage("You just got an "+ItemDefinition.DEFINITIONS[randomLucky].getName()+"!");
	        	   c.getPA().messageall("<col=830014>"+c.playerName+" Has Gotten an "+ItemDefinition.DEFINITIONS[randomLucky].getName()+"!");
	           }
		}
		if (itemId == 16546){ // Lucky Box
			int[] Luckys = {12650,12649,12641,12644,12645,12643,11995,12653,12655,13178,12646,12921,12936,12940,13179,13177,12648,13181,12616,13320,13225,21187,21188,21189,21192,21193,21194,21196,21197,13322,13323,13324,13325,13326,13247,13262,21748,21750,21291,21509,20851,21273,21992};
			int randomLucky = Luckys[Misc.random(Luckys.length - 1)];
			if (c.getItems().freeSlots() < 1) {
				c.sendMessage("You need 1 free slots to open this box.");
				return;
			} 
			if(c.getItems().playerHasItem(16546)){
	        	   c.getItems().deleteItem2(16546, 1);
	        	   c.getItems().addItem(randomLucky, 1);
	        	   c.sendMessage("You just got an "+ItemDefinition.DEFINITIONS[randomLucky].getName()+"!");
	        	   c.getPA().messageall("<col=830014>"+c.playerName+" Has Gotten an "+ItemDefinition.DEFINITIONS[randomLucky].getName()+"!");
	           }
		}
		if(itemId == 10006) {
			Hunter.lay(c, new BirdSnare(c));
		}
		if(itemId == 10008) {
			Hunter.lay(c, new BoxTrap(c));
		}
		if (itemId == 23822){ // Lucky Box
			int[] Luckys = {23337, 23663, 13887, 13893, 13896, 13890, 13894, 13652, 20784, 6568,20050,20211,20214,20217,4151,4178,13576,20601,20604,21018,21021,21024,21009,21000,23559,11826,11828,11830,11832,11834,11836,11838,4708,4710,4712,4714,4716,4718,4720,4722,4724,4726,4730,4732,4734,4736,4738,4740,4745,4747,4749,4751,4753,4755,4757,4759,12817,12825,12821,12831,12283};
			int randomLucky = Luckys[Misc.random(Luckys.length - 1)];
			if (c.getItems().freeSlots() < 1) {
				c.sendMessage("You need 1 free slots to open this box.");
				return;
			} 
			if(c.getItems().playerHasItem(23822)){
	        	   c.getItems().deleteItem2(23822, 1);
	        	   c.getItems().addItem(randomLucky, 1);
	        	   c.sendMessage("You just got an "+ItemDefinition.DEFINITIONS[randomLucky].getName()+"!");
	        	   c.getPA().messageall("<col=255>"+c.playerName+" Has Gotten an "+ItemDefinition.DEFINITIONS[randomLucky].getName()+" From an Uncommon Mystery box!");
	           }
		}
		if (itemId == 23823){ // Lucky Box
			int[] Luckys = {11865,19641,19645,19649,1038,1037,1040,1042,1044,1046,1048,1050,1053,1055,1057,2633,2635,2637,2639,2641,2643,2651,3481,3483,3485,3486,3488,23272,23273,20000,12785};
			int randomLucky = Luckys[Misc.random(Luckys.length - 1)];
			if (c.getItems().freeSlots() < 1) {
				c.sendMessage("You need 1 free slots to open this box.");
				return;
			} 
			if(c.getItems().playerHasItem(23823)){
	        	   c.getItems().deleteItem2(23823, 1);
	        	   c.getItems().addItem(randomLucky, 1);
	        	   c.sendMessage("You just got an "+ItemDefinition.DEFINITIONS[randomLucky].getName()+"!");
	        	   c.getPA().messageall("<col=255>"+c.playerName+" Has Gotten an "+ItemDefinition.DEFINITIONS[randomLucky].getName()+" From an Common Mystery box!");
	           }
		}
		if (itemId == 23824){ // Lucky Box
			int[] Luckys = {1038,1037,1040,1042,1044,1046,1048,1050,1053,1055,1057,23698,23715,23716,23213,23214,23215,23216,23217,23220,23221,23222,23229,23230,23231,23232,23233,23234,23247,23484,23498,23501,23558,23655,23658,23746,23756,23827,23842};
			int randomLucky = Luckys[Misc.random(Luckys.length - 1)];
			if (c.getItems().freeSlots() < 1) {
				c.sendMessage("You need 1 free slots to open this box.");
				return;
			} 
			if(c.getItems().playerHasItem(23824)){
	        	   c.getItems().deleteItem2(23824, 1);
	        	   c.getItems().addItem(randomLucky, 1);
	        	   c.sendMessage("You just got an "+ItemDefinition.DEFINITIONS[randomLucky].getName()+"!");
	        	   c.getPA().messageall("<col=255>"+c.playerName+" Has Gotten an "+ItemDefinition.DEFINITIONS[randomLucky].getName()+" From an Rare Mystery box!");
	           }
		}
		if (itemId == 23825){ // Lucky Box
			int[] Luckys = {23869,23722,23812,23716,23724,23721,23729,23695,13068,13069,13072,13073,23814,21314,11863,7927,10171,10330,10332,10334,10336,10338,10340,10342,10344,10346,10348,10350,10352,12422,12424,12426,12437,12439,20011,20014,20576,20577,23717,23707,23708,23709,23747,23758,23828,23829,23831,23832,23833,23841,23860,23868,12399};
			int randomLucky = Luckys[Misc.random(Luckys.length - 1)];
			if (c.getItems().freeSlots() < 1) {
				c.sendMessage("You need 1 free slots to open this box.");
				return;
			} 
			if(c.getItems().playerHasItem(23825)){
	        	   c.getItems().deleteItem2(23825, 1);
	        	   c.getItems().addItem(randomLucky, 1);
	        	   c.sendMessage("You just got an "+ItemDefinition.DEFINITIONS[randomLucky].getName()+"!");
	        	   c.getPA().messageall("<col=255>"+c.playerName+" Has Gotten an "+ItemDefinition.DEFINITIONS[randomLucky].getName()+" From an Super Rare Mystery box!");
	           }
		}
		if (itemId ==  550){ // present for xmas event
c.getPA().showMap();
}
		if (itemId == 952) {
			if (c.inArea(3553, 3301, 3561, 3294)) {
				c.teleTimer = 3;
				c.newLocation = 1;
			} else if (c.inArea(3550, 3287, 3557, 3278)) {
				c.teleTimer = 3;
				c.newLocation = 2;
			} else if (c.inArea(3561, 3292, 3568, 3285)) {
				c.teleTimer = 3;
				c.newLocation = 3;
			} else if (c.inArea(3570, 3302, 3579, 3293)) {
				c.teleTimer = 3;
				c.newLocation = 4;
			} else if (c.inArea(3571, 3285, 3582, 3278)) {
				c.teleTimer = 3;
				c.newLocation = 5;
			} else if (c.inArea(3562, 3279, 3569, 3273)) {
				c.teleTimer = 3;
				c.newLocation = 6;
			}
		}
	}

}
