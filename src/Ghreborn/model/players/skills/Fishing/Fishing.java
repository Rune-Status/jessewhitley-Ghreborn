package Ghreborn.model.players.skills.Fishing;

import java.util.Random;

import Ghreborn.Config;
import Ghreborn.core.PlayerHandler;
import Ghreborn.event.CycleEvent;
import Ghreborn.event.CycleEventContainer;
import Ghreborn.event.CycleEventHandler;
import Ghreborn.model.content.dailytasks.DailyTasks;
import Ghreborn.model.content.dailytasks.DailyTasks.PossibleTasks;
import Ghreborn.model.npcs.NPCHandler;
import Ghreborn.model.players.Client;
import Ghreborn.util.Misc;

/**
 * Fishing
 * 
 * @author Micheal/01053 https://www.rune-server.org/members/01053/
 */

public class Fishing {

	public static final Random random = new Random();

	public static void startFishing(Client c, int npcId, int i) {
		startFishing(c, npcId, i, false);
	}
	
	public static void startFishing(Client player, int npcId, int i, boolean cont) {
		FishingData.data fishing = FishingData.data.fishingSpots.get(npcId);
		if (player.getItems().freeSlots() == 0) {
			player.getDH().sendStatement("You have no more free slots.");
			return;
		}
		if (player.playerLevel[10] < fishing.getRequirement()) {
			player.sendMessage("You need a fishing level of " + fishing.getRequirement() + " to fish this fish.");
			return;
		}
		if (!player.getItems().playerHasItem(fishing.getItemReq())) {
			player.sendMessage("You need a " + player.getItems().getItemName(fishing.getItemReq()) + " to catch this fish!");
			return;
		}
		if ((!player.getItems().playerHasItem(fishing.getBait()) && fishing.getBait() != 314) ||
				(fishing.getBait() == 314 && (!player.getItems().playerHasItem(2950) && (!player.getItems().playerHasItem(fishing.getBait())))))  {
			player.sendMessage("You need a " + player.getItems().getItemName(fishing.getBait()) + " to catch this fish!");
			return;
		}	
		if (player.stopPlayerPacket) {
			return;
		}
			player.alreadyFishing = true;
			player.stopPlayerPacket = true;
			if(!cont)
				player.sendMessage("You start fishing.");
			player.fishingNpc = i;
			int x = NPCHandler.npcs[i].absX;
			int y = NPCHandler.npcs[i].absY;
			player.animation(fishing.getAnimation());
			CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {

				@Override
				public void execute(CycleEventContainer container) {
					if (player == null || player.disconnected || player.teleporting || player.isDead) {
						container.stop();
						return;
					}
					if (player.getItems().freeSlots() == 0) {
						player.getDH().sendStatement("You have no more free slots.");
						container.stop();
						return;
					}
					if(player.fishingNpc == -1 || NPCHandler.npcs[player.fishingNpc] == null) {
						player.alreadyFishing = false;
						container.stop();
						return;
					} else if(NPCHandler.npcs[player.fishingNpc].absX != x || NPCHandler.npcs[player.fishingNpc].absY != y) {
						player.alreadyFishing = false;
						container.stop();
						return;
					}
					if (!player.alreadyFishing) {
						container.stop();
						return;
					}
					if (!player.stopPlayerPacket) {
						container.stop();
						return;
					}
					int cont = Misc.random(10);
					int chance = Misc.random(50);
					int petchance = Misc.random(4500);
					int petchance1 = Misc.random(3000);
					if (fishing.equals(FishingData.data.Shrimp) || fishing.equals(FishingData.data.Sardine)
							|| fishing.equals(FishingData.data.Trout) || fishing.equals(FishingData.data.Pike)
							|| fishing.equals(FishingData.data.Tuna) || fishing.equals(FishingData.data.Lobster)
							|| fishing.equals(FishingData.data.Monkfish)) {
						if (petchance == 1) {
							player.sendMessage(
									"<col=DD5C3E>You receive a skilling pet. It has been added to your bank. Congratulations!");
							player.getItems().addItemToBank(13320, 1);
							for (int j = 0; j < PlayerHandler.players.length; j++) {
								if (PlayerHandler.players[j] != null) {
									Client c2 = (Client) PlayerHandler.players[j];
									c2.sendMessage(
											"<col=006600>" + player.playerName + " received a skilling pet: 1 x Heron.");
								}
							}
						}
					}
					if (fishing.equals(FishingData.data.Karambwan) || fishing.equals(FishingData.data.Shark)
							|| fishing.equals(FishingData.data.Turtle) || fishing.equals(FishingData.data.MANTA_RAY)
							|| fishing.equals(FishingData.data.Angler) || fishing.equals(FishingData.data.DARK_CRAB)) {
						if (petchance1 == 1) {
							player.sendMessage(
									"<col=DD5C3E>You receive a skilling pet. It has been added to your bank. Congratulations!");
							player.getItems().addItemToBank(13320, 1);
							for (int j = 0; j < PlayerHandler.players.length; j++) {
								if (PlayerHandler.players[j] != null) {
									Client c2 = (Client) PlayerHandler.players[j];
									c2.sendMessage(
											"<col=006600>" + player.playerName + " received a skilling pet: 1 x Heron.");
								}
							}
						}
					}
					if (npcId == fishing.getIdentifier()) {
						int r = random.nextInt(fishing.getFish().length);
						if(fishing.getFish()[r] == 383) {
							DailyTasks.increase(player, PossibleTasks.SHARKS);
						}
						//Achievements.increase(player, AchievementType.FISHING, 1);
						player.getItems().addItem(fishing.getFish()[r], 1);
						if (player.playerEquipment[player.playerHat] == 13258 && player.playerEquipment[player.playerChest] == 13259 && player.playerEquipment[player.playerLegs] == 13260 && player.playerEquipment[player.playerFeet] == 13261) {
							player.getPA().addSkillXP(fishing.getExperience() * Config.FISHING_EXPERIENCE * 1.2, 10);
						} else {
							player.getPA().addSkillXP(fishing.getExperience() * Config.FISHING_EXPERIENCE, 10);
						}
						if(!(fishing.getBait() == 314 && player.getItems().playerHasItem(2950)) || fishing.getBait() != 314)
							player.getItems().deleteItem(fishing.getBait(), 1);
						player.sendMessage("You catch a <col=0000FF>" + player.getItems().getItemName(fishing.getFish()[r]) + "<col=000000>.");
						player.stopAnimation();
						player.alreadyFishing = false;
						int index = player.fishingNpc;
						container.stop();
						if (cont != 0) {
							startFishing(player, npcId, index, true);
						}
						
					}
				}
				
				@Override
				public void stop() {
					player.stopPlayerPacket = false;
					player.fishingNpc = -1;
					super.stop();
				}
				
			}, fishing.getIdentifier() + 5 + playerFishingLevel(player));
		}

	private static int playerFishingLevel(Client c) {
		return (10 - (int) Math.floor(c.playerLevel[c.playerFishing] / 10));
	}
}