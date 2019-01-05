package Ghreborn.model.players.packets;

import java.util.Map.Entry;

import org.apache.commons.lang3.text.WordUtils;

import Ghreborn.Config;
import Ghreborn.Server;
import Ghreborn.core.PlayerHandler;
import Ghreborn.event.CycleEvent;
import Ghreborn.event.CycleEventContainer;
import Ghreborn.event.CycleEventHandler;
import Ghreborn.model.content.QuickPrayer;
import Ghreborn.model.content.Skillcape;
import Ghreborn.model.content.dailytasks.TaskTypes;
import Ghreborn.model.content.teleport.Position;
import Ghreborn.model.content.teleport.TeleportExecutor;
import Ghreborn.model.items.GameItem;
import Ghreborn.model.items.Item2;
import Ghreborn.model.items.ItemDefinition;
import Ghreborn.model.items.bank.BankItem;
import Ghreborn.model.items.bank.BankPin;
import Ghreborn.model.items.bank.BankTab;
import Ghreborn.model.multiplayer_session.MultiplayerSessionStage;
import Ghreborn.model.multiplayer_session.MultiplayerSessionType;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.PacketType;
import Ghreborn.model.players.Player;
import Ghreborn.model.players.RequestHelp;
import Ghreborn.model.players.Rights;
import Ghreborn.model.players.SkillMenu;
import Ghreborn.model.players.skills.cooking.Cooking;
import Ghreborn.model.players.skills.crafting.CraftingData.tanningData;
import Ghreborn.model.players.skills.crafting.GlassBlowing;
import Ghreborn.model.players.skills.crafting.LeatherMaking;
import Ghreborn.model.players.skills.crafting.Tanning;
import Ghreborn.model.players.skills.fletching.Fletching;
import Ghreborn.model.region.music.MusicManager;
import Ghreborn.model.npcs.NPC;
import Ghreborn.model.npcs.NPCDeathTracker.NPCName;
import Ghreborn.model.npcs.NPCHandler;
import Ghreborn.model.npcs.PetHandler;
import Ghreborn.model.npcs.boss.Cerberus.Cerberus;
import Ghreborn.model.npcs.boss.skotizo.Skotizo;
import Ghreborn.model.npcs.drops.DropList;
import Ghreborn.net.Packet;
import Ghreborn.util.Misc;

/**
 * Clicking most buttons
 **/
public class ClickingButtons implements PacketType {

	@Override
	public void processPacket(final Client c,Packet packet) {
		int actionButtonId = packet.hexToInt();
		// int actionButtonId = c.getInStream().readShort();
		if (!c.canUsePackets) {
			return;
		}
		if (c.isDead || c.playerLevel[3] <= 0) {
			return;
		}

		if (c.getRights().isDeveloper() || (c.getRights().isOwner())) {
			Misc.println(c.playerName + " - actionbutton: " + actionButtonId);
		}
		if (c.getDialogue() != null && c.getDialogue().clickButton(actionButtonId)) {
			return;
		}
		if (c.getRandomInterfaceClick().handleButtonClicking(actionButtonId)) {
			return;
		}
		if (c.craftDialogue) {
			LeatherMaking.craftLeather(c, actionButtonId);
		}

		for (tanningData t : tanningData.values()) {
			if (actionButtonId == t.getButtonId(actionButtonId)) {
				Tanning.tanHide(c, actionButtonId);
				return;
			}
		}
		if (c.playerFletch) {
			Fletching.attemptData(c, actionButtonId);
		}
		NPC npc = null;
		GlassBlowing.glassBlowing(c, actionButtonId);
		c.getMusic().handleMusicButtons(actionButtonId);
		QuickPrayer.clickButton(c, actionButtonId);
		/** Drop Manager Buttons **/
		if (actionButtonId >= 128240 && actionButtonId <= 129113) {
			Server.getDropList().select(c, actionButtonId);
			return;
		}

		switch (actionButtonId) {
		case 55095 :
			if (c.getDestroyItem() != -1) {
				c.getItems().deleteItem2(c.getDestroyItem(), c.getItems().getItemAmount(c.getDestroyItem()));
			}
		case 55096 :
			c.setDestroyItem(-1);
			c.getPA().closeAllWindows();
			break;
		case 106251:
			SkillMenu.openInterface(c, 0);
			break;
		case 108020:
			c.sendMessage("This Option has not been added yet.");
			break;
		case 130069:
/*			if (c.BANDOS_CLICKS >= 1) {
				c.sendMessage("You already have an active instance!");
				c.getPA().closeAllWindows();
				return;
			}
			if (c.getBandos().getInstancedBandos() != null) {
				c.sendMessage("You already have an active instance!");
				c.getPA().closeAllWindows();
				return;
			}
				c.getBandos().initialize();
				c.BANDOS_CLICKS = 1;
				c.dialogueAction = -1;
				c.teleAction = -1;
*/
			TeleportExecutor.teleport(c, new Position(2862, 5354, 2));
break;
		case 154078:
			c.getPA().closeAllWindows();
			break;
		case 72096: //Allow Teleport
			c.getPA().getClan().allowTeleport(c);
			break;
		case 72099: //Allow Copy kit
			c.getPA().getClan().allowCopyKit(c);
			break;
		case 113236:
			RequestHelp.setInterface(c);
		break;
		case 109114:
			RequestHelp.callForHelp(c);
		break;
		case 130104:
			if(c.playerLevel[c.playerAttack] >= 65 && c.playerLevel[c.playerStrength] >= 65){
				TeleportExecutor.teleport(c, new Position(2873, 3546, 0));
			}else{
				c.sendMessage("You need a level of 65 of Attack and Strenght to enter.", 255);
			}
			break;
		case 19136:
			QuickPrayer.toggle(c);
			break;
		case 19137:
			c.setSidebarInterface(5, 17200);
			break;
		case 130112:
			if (c.ZULRAH_CLICKS >= 1) {
				c.sendMessage("You already have an active instance!");
				c.getPA().closeAllWindows();
				return;
			}
			if (c.zulrah.getInstancedZulrah() != null) {
				c.sendMessage("You already have an active instance!");
				c.getPA().closeAllWindows();
				return;
			}
			if (c.getItems().playerHasItem(12938, 1)) {
				c.getZulrahEvent().initialize();
				c.getItems().deleteItem(12938, 1);
				c.ZULRAH_CLICKS = 1;
				return;
			}
			c.getZulrahEvent().initialize();
			c.ZULRAH_CLICKS = 1;
			break;
		case 130114:
			if (c.wildLevel > 20) {
				c.sendMessage("You cannot teleport above 20 wilderness.");
				c.getPA().closeAllWindows();
				return;
			}
			if (c.KALPHITE_CLICKS >= 1) {
				c.sendMessage("You already have an active instance!");
				c.getPA().closeAllWindows();
				return;
			}
			if (c.getKalphite().getInstancedKalphite() != null) {
				c.sendMessage("You already have an active instance!");
				c.getPA().closeAllWindows();
				return;
			}
			c.getKalphite().initialize();
			c.KALPHITE_CLICKS = 1;
			break;
		case 130079:
			//TeleportExecutor.teleport(c, new Position(1310, 1239, 0));
			if (System.currentTimeMillis() - c.cerbDelay > 5000) {
				Cerberus cerb = c.createCerberusInstance();
/*				if (!c.debugMessage)
					if (!c.getSlayer().getTask().isPresent()) {
						c.sendMessage("You must have an active cerberus or hellhound task to enter this cave...");
						return;
					}
				if (!c.debugMessage)
					if (!c.getSlayer().getTask().get().getPrimaryName().equals("cerberus")
							&& !c.getSlayer().getTask().get().getPrimaryName().equals("hellhound")) {
						c.sendMessage("You must have an active cerberus or hellhound task to enter this cave...");
						return;
					}
				if (c.getCerberusLostItems().size() > 0) {
					c.getDH().sendDialogues(642, 5870);
					c.nextChat = -1;
					return;
				}*/

				if (cerb == null) {
					c.sendMessage("We are unable to allow you in at the moment.");
					c.sendMessage("Too many players.");
					return;
				}

/*				if (Server.getEventHandler().isRunning(c, "cerb")) {
					c.sendMessage("You're about to fight start the fight, please wait.");
					return;
				}*/
				c.getCerberus().init();
				c.cerbDelay = System.currentTimeMillis();
			} else {
				c.sendMessage("Please wait a few seconds between clicks.");
			}

			break;
		case 130116:
			TeleportExecutor.teleport(c, new Position(1568, 5074, 0));
			break;
		case 130110:
			if(c.getRights().isDonator()) {
				TeleportExecutor.teleport(c, new Position(3244, 9360, 0));
				c.sendMessage("You teleported to Tormented demon's", 255);
			}else {
				c.sendMessage("You need to donate for this", 255);
			}
			break;
		case 130113:
			Skotizo skotizo = c.createSkotizoInstance();
			
			
			/*if (c.getSkotizoLostItems().size() > 0) {
				c.getDH().sendDialogues(642, 5870);
				c.nextChat = -1;
				return;
			}*/

			if (skotizo == null) {
				c.sendMessage("We are unable to allow you in at the moment.");
				c.sendMessage("Too many players.");
				return;
			}
			c.getSkotizo().init();
			c.getItems().deleteItem(19685, 1);
			c.getPA().closeAllWindows();
			c.nextChat = -1;
			break;
		case 53152:
			Cooking.getAmount(c, 1);
			break;
		case 53151:
			Cooking.getAmount(c, 5);
			break;
		case 53150:
			Cooking.getAmount(c, 10);
			break;
		case 53149:
			Cooking.getAmount(c, 28);
			break;
		case 108006: // items kept on death
			c.getPA().openItemsKeptOnDeath();
			break;
		case 121048:
			c.getPA().sendConfig(406, 0);
			c.getPA().sendConfig(407, 0);
			break;
		case 121052:
			c.getPA().sendConfig(406, 1);
			c.getPA().sendConfig(407, 1);
			break;
		case 130102:
			TeleportExecutor.teleport(c, new Position(2438, 5168, 0));
			//c.getFightCave().create(1);
			break;
		case 130077:
			TeleportExecutor.teleport(c, new Position(2976, 4384, 2));
			break;
		case 130065:
			TeleportExecutor.teleport(c, new Position(3056, 9555, 0));
			break;
		case 130090:
			TeleportExecutor.teleport(c, new Position(3105, 3959, 0));
			break;
		case 130091:
			TeleportExecutor.teleport(c, new Position(2982, 3871, 0));
			break;
		case 130092:
			TeleportExecutor.teleport(c, new Position(3184, 3947, 0));
			break;
		case 130093:
			TeleportExecutor.teleport(c, new Position(3327, 3704, 0));
			break;
		case 130094:
			TeleportExecutor.teleport(c, new Position(3137, 3818, 0));
			break;
		case 130095:
			TeleportExecutor.teleport(c, new Position(2987, 3601, 0));
			break;
		case 130096:
			TeleportExecutor.teleport(c, new Position(3201, 3854, 0));
			break;
		case 113249:
			for (int i = 8144; i < 8195; i++) {
				c.getPA().sendFrame126("", i);
			}
			int[] frames = { 8149, 8150, 8151, 8152, 8153, 8154, 8155, 8156, 8157, 8158, 8159, 8160, 8161, 8162, 8163,
					8164, 8165, 8166, 8167, 8168, 8169, 8170, 8171, 8172, 8173, 8174, 8175 };
			c.getPA().sendFrame126("<col=8B0000>Boss Kills for <col=0000FF>" + c.playerName + "", 8144);
			c.getPA().sendFrame126("", 8145);
			c.getPA().sendFrame126("<col=0000FF>Total Boss kills: <col=DD5C3E>" + c.getNpcDeathTracker().getTotal() + "",
					8147);
			c.getPA().sendFrame126("", 8148);
			int index1 = 0;
			for (Entry<NPCName, Integer> entry : c.getNpcDeathTracker().getTracker().entrySet()) {
				if (entry == null) {
					continue;
				}
				if (index1 > frames.length - 1) {
					break;
				}
				if (entry.getValue() > 0) {
					c.getPA()
							.sendFrame126("<col=0000FF>"
									+ WordUtils.capitalize(entry.getKey().name().toLowerCase().replaceAll("_", " "))
									+ " kills: <col=DD5C3E>" + entry.getValue(), frames[index1]);
					index1++;
				}
			}
			c.getPA().showInterface(8134);
			break;
		case 130070:
/*			if (c.SARADOMIN_CLICKS >= 1) {
				c.sendMessage("You already have an active instance!");
				c.getPA().closeAllWindows();
				return;
			}
			if (c.getSaradomin().getInstancedSaradomin() != null) {
				c.sendMessage("You already have an active instance!");
				c.getPA().closeAllWindows();
				return;
			}
				c.getSaradomin().initialize();
				c.SARADOMIN_CLICKS = 1;
				c.dialogueAction = -1;
				c.teleAction = -1;*/
			TeleportExecutor.teleport(c, new Position(2911, 5265, 0));
break;
		case 130072:
/*			if (c.ARMADYL_CLICKS >= 1) {
				c.sendMessage("You already have an active instance!");
				c.getPA().closeAllWindows();
				return;
			}
			if (c.getArmadyl().getInstancedArmadyl() != null) {
				c.sendMessage("You already have an active instance!");
				c.getPA().closeAllWindows();
				return;
			}
				c.getArmadyl().initialize();
				c.ARMADYL_CLICKS = 1;
				c.dialogueAction = -1;
				c.teleAction = -1;*/
			TeleportExecutor.teleport(c, new Position(2839, 5294, 2));
break;
		case 130071:
/*			if (c.ZAMORAK_CLICKS >= 1) {
					c.sendMessage("You already have an active instance!");
					c.getPA().closeAllWindows();
					return;
				}
				if (c.getZamorak().getInstancedZamorak() != null) {
					c.sendMessage("You already have an active instance!");
					c.getPA().closeAllWindows();
					return;
				}
					c.getZamorak().initialize();
					c.ZAMORAK_CLICKS = 1;
					c.dialogueAction = -1;
					c.teleAction = -1;*/
			TeleportExecutor.teleport(c, new Position(2925, 5334, 2));
break;
		case 118098:
			c.getPA().castVeng();
			break;
		// crafting + fletching interface:
		/*case 150:
			if (c.autoRet == 0)
				c.autoRet = 1;
			else
				c.autoRet = 0;
			break;*/
		case 150:

			if (c.autoRet == 0) {
				// c.sendMessage(""+c.autoRet+"");
				c.autoRet = 1;
			}else if (c.autoRet == 1) {
				c.autoRet = 0;
				// c.sendMessage(""+c.autoRet+"");
			}
			break;
		case 87142:
			if(c.getItems().playerHasItem(1511,1)) {
				if(c.getItems().playerHasItem(995, 100)) {
					c.getItems().deleteItem2(1511, 1);
					c.getItems().deleteItem2(995, 100);
					c.getItems().addItem(960, 1);
					c.getPA().closeAllWindows();
					Server.itemHandler.createGroundItem(c, 9468, 3302,
    						3491,c.heightLevel,  1, c.index);
				}else {
					c.sendMessage("You don't have enough coins to convert the logs to planks.");
					return;
				}
			}else {
				c.sendMessage("You haven't got any logs.");
				return;
			}
			break;
		case 87134:				
		int amount1 = c.getItems().getItemAmount(1511);
			if(c.getItems().playerHasItem(1511, amount1)) {
				if(c.getItems().playerHasItem(995, 100 * amount1)) {
					c.getItems().deleteItem2(1511, amount1);
					c.getItems().deleteItem2(995, 100*amount1);
					c.getItems().addItem(960, amount1);
					c.getPA().closeAllWindows();
					Server.itemHandler.createGroundItem(c, 9468, 3302,
    						3491,c.heightLevel,  1, c.index);
				}else if(!c.getItems().playerHasItem(995, 100 * amount1)){
					c.sendMessage("You don't have enough coins to convert the logs to planks.");
					return;
				}
			}else if(!c.getItems().playerHasItem(1511, amount1)) {
				c.sendMessage("You haven't got any logs.");
				return;
			}
			break;
		case 20174:
			c.getPA().closeAllWindows();
			BankPin pin = c.getBankPin();
			if (pin.getPin().length() <= 0)
				c.getBankPin().open(1);
			else if (!pin.getPin().isEmpty() && !pin.isAppendingCancellation())
				c.getBankPin().open(3);
			else if (!pin.getPin().isEmpty() && pin.isAppendingCancellation())
				c.getBankPin().open(4);
			break;
		case 226162:
			if (c.getPA().viewingOtherBank) {
				c.getPA().resetOtherBank();
				return;
			}
			if (!c.isBanking)
				return;
			if (c.getBank().getBankSearch().isSearching()) {
				c.getBank().getBankSearch().reset();
				return;
			}
			for (int slot = 0; slot < c.playerItems.length; slot++) {
				if (c.playerItems[slot] > 0 && c.playerItemsN[slot] > 0) {
					c.getItems().addToBank(c.playerItems[slot] - 1, c.playerItemsN[slot], false);
				}
			}
			c.getItems().updateInventory();
			c.getItems().resetBank();
			c.getItems().resetTempItems();
			break;

		case 226170:
			if (c.getPA().viewingOtherBank) {
				c.getPA().resetOtherBank();
				return;
			}
			if (!c.isBanking)
				return;
			if (c.getBank().getBankSearch().isSearching()) {
				c.getBank().getBankSearch().reset();
				return;
			}
			if (c.getBankPin().requiresUnlock()) {
				c.isBanking = false;
				c.getBankPin().open(2);
				return;
			}
			for (int slot = 0; slot < c.playerEquipment.length; slot++) {
				if (c.playerEquipment[slot] > 0 && c.playerEquipmentN[slot] > 0) {
					c.getItems().addEquipmentToBank(c.playerEquipment[slot], slot,
							c.playerEquipmentN[slot], false);
					c.getItems().wearItem(-1, 0, slot);
				}
			}
			c.getItems().updateInventory();
			c.getItems().resetBank();
			c.getItems().resetTempItems();
			break;

		case 226186:
		case 226198:
		case 226209:
		case 226220:
		case 226231:
		case 226242:
		case 226253:
		case 227008:
		case 227019:
			if (!c.isBanking) {
				c.getPA().removeAllWindows();
				return;
			}
			if (c.getBankPin().requiresUnlock()) {
				c.isBanking = false;
				c.getBankPin().open(2);
				return;
			}
			int tabId = actionButtonId == 226186 ? 0
					: actionButtonId == 226198 ? 1
							: actionButtonId == 226209 ? 2
									: actionButtonId == 226220 ? 3
											: actionButtonId == 226231 ? 4
													: actionButtonId == 226242 ? 5
															: actionButtonId == 226253 ? 6
																	: actionButtonId == 227008 ? 7
																			: actionButtonId == 227019 ? 8 : -1;
			if (tabId <= -1 || tabId > 8)
				return;
			if (c.getBank().getBankSearch().isSearching()) {
				c.getBank().getBankSearch().reset(tabId);
				return;
			}
			if (c.getBank().getBankSearch().isSearching()) {
				c.getBank().getBankSearch().reset();
				return;
			}
			BankTab tab = c.getBank().getBankTab(tabId);
			if (tab.getTabId() == c.getBank().getCurrentBankTab().getTabId())
				return;
			if (tab.size() <= 0 && tab.getTabId() != 0) {
				c.sendMessage("Drag an item into the new tab slot to create a tab.");
				return;
			}
			c.getBank().setCurrentBankTab(tab);
			c.getPA().openUpBank();
			break;

		case 226197:
		case 226208:
		case 226219:
		case 226230:
		case 226241:
		case 226252:
		case 227007:
		case 227018:
			if (c.getPA().viewingOtherBank) {
				c.getPA().resetOtherBank();
				return;
			}
			if (!c.isBanking) {
				c.getPA().removeAllWindows();
				return;
			}
			if (c.getBankPin().requiresUnlock()) {
				c.isBanking = false;
				c.getBankPin().open(2);
				return;
			}
			tabId = actionButtonId == 226197 ? 1
					: actionButtonId == 226208 ? 2
							: actionButtonId == 226219 ? 3
									: actionButtonId == 226230 ? 4
											: actionButtonId == 226241 ? 5
													: actionButtonId == 226252 ? 6
															: actionButtonId == 227007 ? 7 : actionButtonId == 227018 ? 8 : -1;
			tab = c.getBank().getBankTab(tabId);
			if (tab == null || tab.getTabId() == 0 || tab.size() == 0) {
				c.sendMessage("You cannot collapse this tab.");
				return;
			}
			if (tab.size() + c.getBank().getBankTab()[0].size() >= Config.BANK_SIZE) {
				c.sendMessage("You cannot collapse this tab. The contents of this tab and your");
				c.sendMessage("main tab are greater than " + Config.BANK_SIZE + " unique items.");
				return;
			}
			if (c.getBank().getBankSearch().isSearching()) {
				c.getBank().getBankSearch().reset();
				return;
			}
			for (BankItem item : tab.getItems()) {
				c.getBank().getBankTab()[0].add(item);
			}
			tab.getItems().clear();
			if (tab.size() == 0) {
				c.getBank().setCurrentBankTab(c.getBank().getBankTab(0));
			}
			c.getPA().openUpBank();
			break;

		case 226185:
		case 226196:
		case 226207:
		case 226218:
		case 226229:
		case 226240:
		case 226251:
		case 227006:
		case 227017:
			if (c.getPA().viewingOtherBank) {
				c.getPA().resetOtherBank();
				return;
			}
			if (!c.isBanking) {
				c.getPA().removeAllWindows();
				return;
			}
			if (c.getBankPin().requiresUnlock()) {
				c.isBanking = false;
				c.getBankPin().open(2);
				return;
			}
			if (c.getBank().getBankSearch().isSearching()) {
				c.getBank().getBankSearch().reset();
				return;
			}
			tabId = actionButtonId == 226185 ? 0
					: actionButtonId == 226196 ? 1
							: actionButtonId == 226207 ? 2
									: actionButtonId == 226218 ? 3
											: actionButtonId == 226229 ? 4
													: actionButtonId == 226240 ? 5
															: actionButtonId == 226251 ? 6
																	: actionButtonId == 227006 ? 7
																			: actionButtonId == 227017 ? 8 : -1;
			tab = c.getBank().getBankTab(tabId);
			long value = 0;
			if (tab == null || tab.size() == 0)
				return;
			for (BankItem item : tab.getItems()) {
				c.getShops();
				// long tempValue = item.getId() - 1 == 995 ? 1 :
				// ShopAssistant.getItemShopValue(item.getId() - 1);
				long tempValue = item.getId() - 1 == 995 ? 1
						: ItemDefinition.forId(item.getId() - 1).getSpecialPrice();
				value += tempValue * item.getAmount();
			}
			c.sendMessage("<col=255>The total networth of tab " + tab.getTabId() + " is </col><col=600000>"
					+ Long.toString(value) + " gp</col>.");
			break;

		case 22024:
		case 86008:
			c.getPA().openUpBank();
			break;

		// TODO:
		case 226154:
			c.takeAsNote = !c.takeAsNote;
			break;
		case 87143:
			c.sendMessage("This option has not been added yet.");
			break;
		case 104078:
			c.setSidebarInterface(3, 3213);
			break;
		case 87144:
			if(c.getItems().playerHasItem(1511,10)) {
				if(c.getItems().playerHasItem(995, 1000)) {
					c.getItems().deleteItem2(1511, 10);
					c.getItems().deleteItem2(995, 1000);
					c.getItems().addItem(960, 10);
					c.getPA().closeAllWindows();
					Server.itemHandler.createGroundItem(c, 9468, 3302,
    						3491,c.heightLevel,  1, c.index);
				}else {
					c.sendMessage("You don't have enough coins to convert the logs to planks.");
					return;
				}
			}else {
				c.sendMessage("You haven't got any logs.");
				return;
			}
			break;
		case 87145:
			if(c.getItems().playerHasItem(1511,5)) {
				if(c.getItems().playerHasItem(995, 500)) {
					c.getItems().deleteItem2(1511, 5);
					c.getItems().deleteItem2(995, 500);
					c.getItems().addItem(960, 5);
					c.getPA().closeAllWindows();
					Server.itemHandler.createGroundItem(c, 9468, 3302,
    						3491,c.heightLevel,  1, c.index);
				}else {
					c.sendMessage("You don't have enough coins to convert the logs to planks.");
					return;
				}
			}else {
				c.sendMessage("You haven't got any logs.");
				return;
			}
			break;
		case 121029:
			c.setSidebarInterface(11, 31040); // wrench tab
			break;
		case 121032:
			c.setSidebarInterface(11, 31060); // wrench tab	
			break;
			
		case 121035:
			c.setSidebarInterface(11, 31080); // wrench tab	
			break;			
			
		case 121065:
			c.setSidebarInterface(11, 904); // wrench tab
			break;
	/*	case 73248: 
            if (c.actionTimer == 0)
                		c.gfx0(1553);
	    		c.animation(8770);
	    			c.stopMovement();
				c.actionTimer = 10;
break;

case 73223: 
            if (c.actionTimer == 0)
	    		c.animation(855);
	    			c.stopMovement();
				c.actionTimer = 2;
break;
case 73205: 
            if (c.actionTimer == 0)
	    		c.animation(856);
	    			c.stopMovement();
				c.actionTimer = 2;
break;
case 73208: 
            if (c.actionTimer == 0)
	    		c.animation(858);
	    			c.stopMovement();
				c.actionTimer = 2;
break;
case 73209: 
            if (c.actionTimer == 0)
	    		c.animation(859);
	    			c.stopMovement();
				c.actionTimer = 2;
break;
case 73210: 
            if (c.actionTimer == 0)
	    		c.animation(857);
	    			c.stopMovement();
				c.actionTimer = 2;
break;
case 73211: 
            if (c.actionTimer == 0)
	    		c.animation(863);
	    			c.stopMovement();
				c.actionTimer = 2;
break;
case 73212: 
            if (c.actionTimer == 0)
	    		c.animation(2113);
	    			c.stopMovement();
				c.actionTimer = 2;
break;
case 73213: 
            if (c.actionTimer == 0)
	    		c.animation(862);
	    			c.stopMovement();
				c.actionTimer = 2;
break;
case 73214: 
            if (c.actionTimer == 0)
	    		c.animation(864);
	    			c.stopMovement();
				c.actionTimer = 2;
break;
case 73215: 
            if (c.actionTimer == 0)
	    		c.animation(861);
	    			c.stopMovement();
				c.actionTimer = 2;
break;
case 73216: 
            if (c.actionTimer == 0)
	    		c.animation(2109);
	    			c.stopMovement();
				c.actionTimer = 2;
break;
case 73217: 
            if (c.actionTimer == 0)
	    		c.animation(2111);
	    			c.stopMovement();
				c.actionTimer = 2;
break;
case 73218: 
            if (c.actionTimer == 0)
	    		c.animation(866);
	    			c.stopMovement();
				c.actionTimer = 2;
break;
case 73219: 
            if (c.actionTimer == 0)
	    		c.animation(2106);
	    			c.stopMovement();
				c.actionTimer = 2;
break;
case 73220: 
            if (c.actionTimer == 0)
	    		c.animation(2107);
	    			c.stopMovement();
				c.actionTimer = 2;
break;
case 73221: 
            if (c.actionTimer == 0)
	    		c.animation(2108);
	    			c.stopMovement();
				c.actionTimer = 2;
break;

case 73224: 
            if (c.actionTimer == 0)
	    		c.animation(860);
	    			c.stopMovement();
				c.actionTimer = 2;
break;

case 73225: 
            if (c.actionTimer == 0)
		c.gfx0(1702);
	    		c.animation(1374);
	    			c.stopMovement();
				c.actionTimer = 2;
break;
case 73226: 
            if (c.actionTimer == 0)
	    		c.animation(2105);
	    			c.stopMovement();
				c.actionTimer = 2;
break;

case 73227: 
            if (c.actionTimer == 0)
	    		c.animation(2110);
	    			c.stopMovement();
				c.actionTimer = 2;
break;
case 73228: 
            if (c.actionTimer == 0)
	    		c.animation(865);
	    			c.stopMovement();
				c.actionTimer = 2;
break;
case 73229: 
            if (c.actionTimer == 0)
	    		c.animation(2112);
	    			c.stopMovement();
				c.actionTimer = 2;
break;
case 73230: 
            if (c.actionTimer == 0)
	    		c.animation(0x84F);
	    			c.stopMovement();
				c.actionTimer = 2;
break;
case 73231: 
            if (c.actionTimer == 0)
	    		c.animation(0x850);
	    			c.stopMovement();
				c.actionTimer = 2;
break;
case 73232: 
            if (c.actionTimer == 0)
	    		c.animation(1131);
	    			c.stopMovement();
				c.actionTimer = 2;
break;
case 73233: 
            if (c.actionTimer == 0)
	    		c.animation(1130);
	    			c.stopMovement();
				c.actionTimer = 2;
break;
case 73234: 
            if (c.actionTimer == 0)
	    		c.animation(1129);
	    			c.stopMovement();
				c.actionTimer = 2;
break;
case 73235: 
            if (c.actionTimer == 0)
	    		c.animation(1128);
	    			c.stopMovement();
				c.actionTimer = 2;
break;


case 73239: 
            if (c.actionTimer == 0)
	    		c.animation(4275);
	    			c.stopMovement();
				c.actionTimer = 2;
break;

case 73238: 
            if (c.actionTimer == 0)
	    		c.animation(4280);
	    			c.stopMovement();
				c.actionTimer = 2;
break;

case 73237: 
            if (c.actionTimer == 0)
	    		c.animation(1745);
	    			c.stopMovement();
				c.actionTimer = 2;
break;

case 73236: 
            if (c.actionTimer == 0)
	    		c.animation(4276);
	    			c.stopMovement();
				c.actionTimer = 2;
break;

case 73240: 
            if (c.actionTimer == 0)
	    		c.animation(3544);
	    			c.stopMovement();
				c.actionTimer = 2;
break;

case 73241: 
            if (c.actionTimer == 0)
	    		c.animation(3543);
	    			c.stopMovement();
				c.actionTimer = 2;
break;
case 73244: 
            if (c.actionTimer == 0)
//c.lowGFX(1244, 0);
	    		c.animation(7272);
	    			c.stopMovement();
				c.actionTimer = 4;
break;


case 73242: 
            if (c.actionTimer == 0)
	    		c.animation(2836);
	    			c.stopMovement();
				c.actionTimer = 2;
break;

case 73243: 
            if (c.actionTimer == 0)
	    		c.animation(6111);
	    			c.stopMovement();
				c.actionTimer = 2;
break;


case 73246: 
            if (c.actionTimer == 0)
	    		c.animation(7531);
	    			c.stopMovement();
				c.actionTimer = 2;
break;
case 73247:
            if (c.actionTimer == 0)
                	//	c.lowGFX(1537, 0);
	    			c.animation(2414);
	    			c.stopMovement();
				c.actionTimer = 3;
break;

case 73249: 
            if (c.actionTimer == 0)
                	//	c.lowGFX(1734, 0);
	    			c.animation(9990);
	    			c.stopMovement();
				c.actionTimer = 2;
break;

case 73250: 
if (c.actionTimer == 0)
                	//	c.lowGFX(1864, 0);
	    			c.animation(10530);
	    			c.stopMovement();
				c.actionTimer = 2;
break;


case 73252: 
if (c.actionTimer == 0)
                	//	c.lowGFX(1973, 0);
	    			c.animation(11044);
	    			c.stopMovement();		
c.actionTimer = 2;
break;
*/
		case 121068:
		case 121085:
			break;
	
		case 130076:
			if (c.KRAKEN_CLICKS >= 1) {
				c.sendMessage("You already have an active instance!");
				c.getPA().closeAllWindows();
				return;
			}
			if (c.getKraken().getInstancedKraken() != null) {
				c.sendMessage("You already have an active instance!");
				c.getPA().closeAllWindows();
				return;
			}
			c.getKraken().initialize();
			c.KRAKEN_CLICKS = 1;
			break;

	

		/*
		 * case 152: c.isRunning = !c.isRunning; int frame = c.isRunning ==
		 * true ? 1 : 0; c.getPA().sendFrame36(173,frame); break;
		 */
		case 152:
		case 121042:
			if (c.runEnergy < 1) {
				c.isRunning = false;
				c.getPA().sendFrame36(173, 0);
				return;
			}
			c.isRunning2 = !c.isRunning2;
			int frame = c.isRunning2 == true ? 1 : 0;
			c.getPA().sendFrame36(173, frame);
			break;
			
		/**
		 * Mouse Button
		 */
		//case 74176:
		case 121108:
			if (!c.mouseButton) {
				c.mouseButton = true;
				c.getPA().sendFrame36(500, 1);
				c.getPA().sendFrame36(170, 1);
			} else if (c.mouseButton) {
				c.mouseButton = false;
				c.getPA().sendFrame36(500, 0);
				c.getPA().sendFrame36(170, 0);
			}
			break;
			
		/**
		 * Split Chat
		 */
		//case 74184:
		case 121092:
			if (!c.splitChat) {
				c.splitChat = true;
				c.getPA().sendFrame36(502, 1);
				c.getPA().sendFrame36(287, 1);
			} else {
				c.splitChat = false;
				c.getPA().sendFrame36(502, 0);
				c.getPA().sendFrame36(287, 0);
			}
			break;
			
		/**
		 * Chat Effects
		 */
		case 121088:
		case 74180:
			if (!c.chatEffects) {
				c.chatEffects = true;
				c.getPA().sendFrame36(501, 1);
				c.getPA().sendFrame36(171, 0);
			} else {
				c.chatEffects = false;
				c.getPA().sendFrame36(501, 0);
				c.getPA().sendFrame36(171, 1);
			}
			break;
			
		/**
		 * Accept aid
		 */
		case 74188:
		case 121038:
			if (!c.acceptAid) {
				c.acceptAid = true;
				c.getPA().sendFrame36(503, 1);
				c.getPA().sendFrame36(427, 1);
			} else {
				c.acceptAid = false;
				c.getPA().sendFrame36(503, 0);
				c.getPA().sendFrame36(427, 0);
			}
			break;
		/**
		 * Attack option Priority
		 */
		case 121117:
			c.getPA().sendFrame36(315, 0);
			c.getPA().sendFrame36(316, 0);
			c.getPA().sendFrame36(317, 0);
			break;
		
		case 121121:
			c.getPA().sendFrame36(315, 1);
			c.getPA().sendFrame36(316, 1);
			c.getPA().sendFrame36(317, 0);
			break;
			
		case 121125:
			c.getPA().sendFrame36(315, 1);
			c.getPA().sendFrame36(316, 0);
			c.getPA().sendFrame36(317, 1);
			break;
			
		case 121061:// brightness1
			c.brightness = 1;
			c.getPA().sendFrame36(505, 1);
			c.getPA().sendFrame36(506, 0);
			c.getPA().sendFrame36(507, 0);
			c.getPA().sendFrame36(508, 0);
			c.getPA().sendFrame36(166, 1);
			break;
		case 121133:// brightness2
			c.brightness = 2;
			c.getPA().sendFrame36(505, 0);
			c.getPA().sendFrame36(506, 1);
			c.getPA().sendFrame36(507, 0);
			c.getPA().sendFrame36(508, 0);
			c.getPA().sendFrame36(166, 2);
			break;

		case 121137:// brightness3
			c.brightness = 3;
			c.getPA().sendFrame36(505, 0);
			c.getPA().sendFrame36(506, 0);
			c.getPA().sendFrame36(507, 1);
			c.getPA().sendFrame36(508, 0);
			c.getPA().sendFrame36(166, 3);
			break;

		case 121141:// brightness4
			c.brightness = 4;
			c.getPA().sendFrame36(505, 0);
			c.getPA().sendFrame36(506, 0);
			c.getPA().sendFrame36(507, 0);
			c.getPA().sendFrame36(508, 1);
			c.getPA().sendFrame36(166, 4);
			break;

		case 121152://music
			c.Musicvolume = 1;
			c.getPA().sendFrame36(509, 1);
			c.getPA().sendFrame36(510, 0);
			c.getPA().sendFrame36(511, 1);
			c.getPA().sendFrame36(512, 0);
			c.getPA().sendFrame36(168, 0);
			break;

		case 121156:
			c.Musicvolume = 2;
			c.getPA().sendFrame36(509, 0);
			c.getPA().sendFrame36(510, 1);
			c.getPA().sendFrame36(511, 1);
			c.getPA().sendFrame36(512, 0);
			c.getPA().sendFrame36(168, 1);
			break;
			
		case 121160:
			c.Musicvolume = 3;
			c.getPA().sendFrame36(509, 0);
			c.getPA().sendFrame36(510, 0);
			c.getPA().sendFrame36(511, 0);
			c.getPA().sendFrame36(512, 0);
			c.getPA().sendFrame36(168, 2);

			break;
			
		case 121164:
			c.Musicvolume = 4;
			c.getPA().sendFrame36(509, 0);
			c.getPA().sendFrame36(510, 0);
			c.getPA().sendFrame36(511, 1);
			c.getPA().sendFrame36(512, 1);
			c.getPA().sendFrame36(168, 3);

			break;
			
		case 73202:
			c.getPA().showInterface(17890);
			break;
			
		case 108005:
			c.getPA().showInterface(15106);
			break;

			case 69230:
			c.getPA().closeAllWindows();
			break;

			case 130051:
			c.getPA().closeAllWindows();
			break;
			case 130063:
				TeleportExecutor.teleport(c, new Position(2676, 3715, 0));
			break;
			case 130058:
				TeleportExecutor.teleport(c, new Position(3096, 9867, 0));
			break;
			case 130059:
				TeleportExecutor.teleport(c, new Position(2712, 9564, 0));
			break;
			case 130060:
				TeleportExecutor.teleport(c, new Position(2805, 10001, 0));
				break;
			case 130061:
				TeleportExecutor.teleport(c, new Position(2884, 9798, 0));
				break;
			case 130064:
				TeleportExecutor.teleport(c, new Position(3428, 3537, 0));
				break;
			case 130067:
				TeleportExecutor.teleport(c, new Position(1764, 5365, 1));
				break;
			case 73200:
			c.setSidebarInterface(12, 18891);
			break;

			case 73253:
			c.setSidebarInterface(12, 147);
			break;
		// 1st tele option
		case 9190:
			if (c.teleAction == 1) {
				// rock crabs
				TeleportExecutor.teleport(c, new Position(2676, 3715, 0));
			} else if (c.teleAction == 2) {
				// barrows
				TeleportExecutor.teleport(c, new Position(3565, 3314, 0));
			} else if (c.teleAction == 3) {
				// godwars
				TeleportExecutor.teleport(c, new Position(2916, 3612, 0));
			} else if (c.teleAction == 4) {
				// varrock wildy
				TeleportExecutor.teleport(c, new Position(3243, 3513, 0));
			} else if (c.teleAction == 5) {
				TeleportExecutor.teleport(c, new Position(3046, 9779, 0));
			}

			if (c.dialogueAction == 10) {
				TeleportExecutor.teleport(c, new Position(2845, 4832, 0));
				c.dialogueAction = -1;
			} else if (c.dialogueAction == 11) {
				TeleportExecutor.teleport(c, new Position(2786, 4839, 0));
				c.dialogueAction = -1;
			} else if (c.dialogueAction == 12) {
				TeleportExecutor.teleport(c, new Position(2398, 4841, 0));
				c.dialogueAction = -1;
			}
			break;
		// mining - 3046,9779,0
		// smithing - 3079,9502,0

		// 2nd tele option
		case 9191:
			if (c.teleAction == 1) {
				// tav dungeon
				TeleportExecutor.teleport(c, new Position(2884, 9798, 0));
			} else if (c.teleAction == 2) {
				// pest control
				TeleportExecutor.teleport(c, new Position(2662, 2650, 0));
			} else if (c.teleAction == 3) {
				// kbd
				TeleportExecutor.teleport(c, new Position(3007, 3849, 0));
			} else if (c.teleAction == 4) {
				// graveyard
				TeleportExecutor.teleport(c, new Position(3164, 3685, 0));
			} else if (c.teleAction == 5) {
				TeleportExecutor.teleport(c, new Position(3079, 9502, 0));
			}
			if (c.dialogueAction == 10) {
				TeleportExecutor.teleport(c, new Position(2796, 4818, 0));
				c.dialogueAction = -1;
			} else if (c.dialogueAction == 11) {
				TeleportExecutor.teleport(c, new Position(2527, 4833, 0));
				c.dialogueAction = -1;
			} else if (c.dialogueAction == 12) {
				TeleportExecutor.teleport(c, new Position(2464, 4834, 0));
				c.dialogueAction = -1;
			}
			break;
		// 3rd tele option

		case 9192:
			if (c.teleAction == 1) {
				// slayer tower
				TeleportExecutor.teleport(c, new Position(3428, 3537, 0));
			} else if (c.teleAction == 2) {
				// tzhaar
				TeleportExecutor.teleport(c, new Position(2444, 5170, 0));
			} else if (c.teleAction == 3) {
				// dag kings
				TeleportExecutor.teleport(c, new Position(2479, 10147, 0));
			} else if (c.teleAction == 4) {
				// 44 portals
				TeleportExecutor.teleport(c, new Position(2975, 3873, 0));
			} else if (c.teleAction == 5) {
				TeleportExecutor.teleport(c, new Position(2813, 3436, 0));
			}
			if (c.dialogueAction == 10) {
				TeleportExecutor.teleport(c, new Position(2713, 4836, 0));
				c.dialogueAction = -1;
			} else if (c.dialogueAction == 11) {
				TeleportExecutor.teleport(c, new Position(2162, 4833, 0));
				c.dialogueAction = -1;
			} else if (c.dialogueAction == 12) {
				TeleportExecutor.teleport(c, new Position(2207, 4836, 0));
				c.dialogueAction = -1;
			}
			break;
		// 4th tele option
		case 9193:
			if (c.teleAction == 1) {
				// brimhaven dungeon
				TeleportExecutor.teleport(c, new Position(2710, 9466, 0));
			} else if (c.teleAction == 2) {
				// duel arena
				TeleportExecutor.teleport(c, new Position(3366, 3266, 0));
			} else if (c.teleAction == 3) {
				// chaos elemental
				TeleportExecutor.teleport(c, new Position(3295, 3921, 0));
			} else if (c.teleAction == 4) {
				// gdz
				TeleportExecutor.teleport(c, new Position(3288, 3886, 0));
			} else if (c.teleAction == 5) {
				TeleportExecutor.teleport(c, new Position(2724, 3484, 0));
				c.sendMessage("For magic logs, try north of the duel arena.");
			}
			if (c.dialogueAction == 10) {
				TeleportExecutor.teleport(c, new Position(2660, 4839, 0));
				c.dialogueAction = -1;
			} else if (c.dialogueAction == 11) {
				// TeleportExecutor.teleport(c, new Position(2527, 4833, 0); astrals here
				//c.getRunecrafting().craftRunes(2489);
				c.dialogueAction = -1;
			} else if (c.dialogueAction == 12) {
				// TeleportExecutor.teleport(c, new Position(2464, 4834, 0); bloods here
				//c.getRunecrafting().craftRunes(2489);
				c.dialogueAction = -1;
			}
			break;
		// 5th tele option
		case 9194:
			if (c.teleAction == 1) {
				// island
				TeleportExecutor.teleport(c, new Position(2895, 2727, 0));
			} else if (c.teleAction == 2) {
				// last minigame spot
				c.sendMessage("Suggest something for this spot on the forums!");
				c.getPA().closeAllWindows();
			} else if (c.teleAction == 3) {
				// last monster spot
				c.sendMessage("Suggest something for this spot on the forums!");
				c.getPA().closeAllWindows();
			} else if (c.teleAction == 4) {
				// ardy lever
				TeleportExecutor.teleport(c, new Position(2561, 3311, 0));
			} else if (c.teleAction == 5) {
				TeleportExecutor.teleport(c, new Position(2812, 3463, 0));
			}
			if (c.dialogueAction == 10 || c.dialogueAction == 11) {
				c.dialogueId++;
				c.getDH().sendDialogues(c.dialogueId, 0);
			} else if (c.dialogueAction == 12) {
				c.dialogueId = 17;
				c.getDH().sendDialogues(c.dialogueId, 0);
			}
			break;

		case 34185:
		case 34184:
		case 34183:
		case 34182:
		case 34189:
		case 34188:
		case 34187:
		case 34186:
		case 34193:
		case 34192:
		case 34191:
		case 34190:
			if (c.craftingLeather)
				c.getCrafting().handleCraftingClick(actionButtonId);

			break;

		case 15147:
			if (c.smeltInterface) {
				c.smeltType = 2349;
				c.smeltAmount = 1;
				c.getSmithing().startSmelting(c.smeltType);
			}
			break;

		case 15151:
			if (c.smeltInterface) {
				c.smeltType = 2351;
				c.smeltAmount = 1;
				c.getSmithing().startSmelting(c.smeltType);
			}
			break;

		case 15159:
			if (c.smeltInterface) {
				c.smeltType = 2353;
				c.smeltAmount = 1;
				c.getSmithing().startSmelting(c.smeltType);
			}
			break;

		case 29017:
			if (c.smeltInterface) {
				c.smeltType = 2359;
				c.smeltAmount = 1;
				c.getSmithing().startSmelting(c.smeltType);
			}
			break;

		case 29022:
			if (c.smeltInterface) {
				c.smeltType = 2361;
				c.smeltAmount = 1;
				c.getSmithing().startSmelting(c.smeltType);
			}
			break;

		case 29026:
			if (c.smeltInterface) {
				c.smeltType = 2363;
				c.smeltAmount = 1;
				c.getSmithing().startSmelting(c.smeltType);
			}
			break;
		case 58253:
			// c.getPA().showInterface(15106);
			c.getItems().writeBonus();
			break;

		case 59004:
			c.getPA().removeAllWindows();
			break;


		case 9178:
			if (c.usingGlory)
				c.getPA().startTeleport(Config.EDGEVILLE_X, Config.EDGEVILLE_Y,
						0, "modern");
			if (c.dialogueAction == 2)
				c.getPA().startTeleport(3428, 3538, 0, "modern");
			if (c.dialogueAction == 3)
				c.getPA().startTeleport(Config.EDGEVILLE_X, Config.EDGEVILLE_Y,
						0, "modern");
			if (c.dialogueAction == 4)
				c.getPA().startTeleport(3565, 3314, 0, "modern");
			if (c.dialogueAction == 20) {
				c.getPA().startTeleport(2897, 3618, 4, "modern");
				c.killCount = 0;
			}

			break;

		case 9179:
			if (c.usingGlory)
				c.getPA().startTeleport(Config.AL_KHARID_X, Config.AL_KHARID_Y,
						0, "modern");
			if (c.dialogueAction == 2)
				c.getPA().startTeleport(2884, 3395, 0, "modern");
			if (c.dialogueAction == 3)
				c.getPA().startTeleport(3243, 3513, 0, "modern");
			if (c.dialogueAction == 4)
				c.getPA().startTeleport(2444, 5170, 0, "modern");
			if (c.dialogueAction == 20) {
				c.getPA().startTeleport(2897, 3618, 12, "modern");
				c.killCount = 0;
			}
			break;

		case 9180:
			if (c.usingGlory)
				c.getPA().startTeleport(Config.KARAMJA_X, Config.KARAMJA_Y, 0,
						"modern");
			if (c.dialogueAction == 2)
				c.getPA().startTeleport(2471, 10137, 0, "modern");
			if (c.dialogueAction == 3)
				c.getPA().startTeleport(3363, 3676, 0, "modern");
			if (c.dialogueAction == 4)
				c.getPA().startTeleport(2659, 2676, 0, "modern");
			if (c.dialogueAction == 20) {
				c.getPA().startTeleport(2897, 3618, 8, "modern");
				c.killCount = 0;
			}
			break;

		case 9181:
			if (c.usingGlory)
				c.getPA().startTeleport(Config.MAGEBANK_X, Config.MAGEBANK_Y,
						0, "modern");
			if (c.dialogueAction == 2)
				c.getPA().startTeleport(2669, 3714, 0, "modern");
			if (c.dialogueAction == 3)
				c.getPA().startTeleport(2540, 4716, 0, "modern");
			if (c.dialogueAction == 4) {
				c.getPA().startTeleport(3366, 3266, 0, "modern");
				c.sendMessage("Dueling is at your own risk. Refunds will not be given for items lost due to glitches.");
			}
			if (c.dialogueAction == 20) {
				// c.getPA().startTeleport(3366, 3266, 0, "modern");
				// c.killCount = 0;
				c.sendMessage("This will be added shortly");
			}
			break;

		case 1093:
		case 1094:
		case 1097:
			if (c.autocastId > 0) {
				c.getPA().resetAutocast();
			} else {
				if (c.playerMagicBook == 1) {
					if (c.playerEquipment[c.playerWeapon] == 4675)
						c.setSidebarInterface(0, 1689);
					else
						c.sendMessage("You can't autocast ancients without an ancient staff.");
				} else if (c.playerMagicBook == 0) {
					if (c.playerEquipment[c.playerWeapon] == 4170) {
						c.setSidebarInterface(0, 12050);
					} else {
						c.setSidebarInterface(0, 1829);
					}
				}

			}
			break;

		case 9157:// barrows tele to tunnels
			 if (c.dialogueAction == 901) {
				c.dailyEnabled = true;
				c.getDH().sendDialogues(904, 1909);
			} else if (c.dialogueAction == 903) {
				if (c.dailyEnabled == true) {
					c.playerChoice = TaskTypes.PVM;
					//c.dailyChoice = 1;
					c.getDH().sendDialogues(906, 1909);
				} else {
					c.sendMessage("You must enable Daily Tasks first!");
					c.getPA().closeAllWindows();
				}
			}
			break;
		case 9167:
			 if (c.dialogueAction == 915) {
					c.setSidebarInterface(6, 1151); // modern
					c.playerMagicBook = 0;
					//c.sendMessage("You feel a drain on your memory.");
					c.autocastId = -1;
					c.getPA().resetAutocast();
					c.getPA().closeAllWindows();
			 }
			break;
		case 9168:
			 if (c.dialogueAction == 915) {
					c.playerMagicBook = 1;
					c.setSidebarInterface(6, 12855);
					//c.sendMessage("An ancient wisdomin fills your mind.");
					c.getPA().resetAutocast();
					c.getPA().closeAllWindows();
			 }
			break;
		case 9169:
			 if (c.dialogueAction == 915) {
					c.playerMagicBook = 2;
					c.setSidebarInterface(6, 29999);
					//c.sendMessage("An ancient wisdomin fills your mind.");
					c.getPA().resetAutocast();
					c.getPA().closeAllWindows();
			 }
			break;
		case 130117:
			c.createInfernoInstance();
			c.getInfernoMinigame().create(1);
			c.getInfernoMinigame().getPlayer();
			break;
		case 130118:
			TeleportExecutor.teleport(c, new Position(1663, 10046, 0));
			break;
		case 130119:
			TeleportExecutor.teleport(c, new Position(3597, 10291, 0));
			break;
		case 9158:
 if (c.dialogueAction == 901) {
				c.dailyEnabled = false;
				c.getDH().sendDialogues(905, 1909);
		} else if (c.dialogueAction == 903) {
			if (c.dailyEnabled == true) {
				c.playerChoice = TaskTypes.SKILLING;
				//c.dailyChoice = 2;
				c.getDH().sendDialogues(907, 1909);
			} else {
				c.sendMessage("You must enable Daily Tasks first!");
				c.getPA().closeAllWindows();
			}
		}
			break;
		case 164035:
		case 164036:
		case 164037:
			int index = actionButtonId - 164034;
			String[] removed = c.getSlayer().getRemoved();
			if (index < 0 || index > removed.length - 1) {
				return;
			}
			if (removed[index].isEmpty()) {
				c.sendMessage("There is no task in this slow that is being blocked.");
				return;
			}
			removed[index] = "";
			c.getSlayer().setRemoved(removed);
			c.getSlayer().updateCurrentlyRemoved();
			break;

		case 164028:
			c.getSlayer().cancelTask();
			break;
		case 164029:
			c.getSlayer().removeTask();
			break;

		case 160045:
		case 162033:
		case 164021:
			if (c.interfaceId != 41000)
				c.getSlayer().handleInterface("buy");
			break;

		case 160047:
		case 162035:
		case 164023:
			if (c.interfaceId != 41500)
				c.getSlayer().handleInterface("learn");
			break;

		case 160049:
		case 162037:
		case 164025:
			if (c.interfaceId != 42000)
				c.getSlayer().handleInterface("assignment");
			break;

		case 162030:
		case 164018:
		case 160042:
			c.getPA().removeAllWindows();
			break;

		/** Specials **/
		case 29188:
			c.specBarId = 7636; // the special attack text - sendframe126(S P E
								// C I A L A T T A C K, c.specBarId);
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;

		case 29163:
			c.specBarId = 7611;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;

		case 33033:
			c.specBarId = 8505;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;

		case 29038:
			c.specBarId = 7486;
			/*
			 * if (c.specAmount >= 5) { c.attackTimer = 0;
			 * c.getCombat().attackPlayer(c.playerIndex); c.usingSpecial = true;
			 * c.specAmount -= 5; }
			 */
			c.getCombat().handleGmaulPlayer();
			c.getItems().updateSpecialBar();
			break;

		case 29063:
			if (c.getCombat()
					.checkSpecAmount(c.playerEquipment[c.playerWeapon])) {
				c.gfx0(246);
				c.forceChat("Raarrrrrgggggghhhhhhh!");
				c.animation(1056);
				c.playerLevel[2] = c.getLevelForXP(c.playerXP[2])
						+ (c.getLevelForXP(c.playerXP[2]) * 15 / 100);
				c.getPA().refreshSkill(2);
				c.getItems().updateSpecialBar();
			} else {
				c.sendMessage("You don't have the required special energy to use this attack.");
			}
			break;

		case 48023:
			c.specBarId = 12335;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;

		case 29138:
			c.specBarId = 7586;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;

		case 29113:
			c.specBarId = 7561;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;

		case 29238:
			c.specBarId = 7686;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;




		case 4169: // god spell charge
			c.usingMagic = true;
			if (!c.getCombat().checkMagicReqs(48)) {
				break;
			}

			if (System.currentTimeMillis() - c.godSpellDelay < Config.GOD_SPELL_CHARGE) {
				c.sendMessage("You still feel the charge in your body!");
				break;
			}
			c.godSpellDelay = System.currentTimeMillis();
			c.sendMessage("You feel charged with a magical power!");
			c.gfx100(c.MAGIC_SPELLS[48][3]);
			c.animation(c.MAGIC_SPELLS[48][2]);
			c.usingMagic = false;
			break;

		case 28164: // item kept on death
			break;


		case 9154:
			long buttonDelay = 0;
			if (System.currentTimeMillis() - buttonDelay > 2000) {
			c.logout();
			buttonDelay = System.currentTimeMillis();
			}
			break;

		case 21010:
			c.takeAsNote = true;
			break;

		case 21011:
			c.takeAsNote = false;
			break;

			// home teleports
			case 4171:
			case 117048:
			case 75010:
			case 84237:
				TeleportExecutor.teleport(c, new Position(Config.START_LOCATION_X, Config.START_LOCATION_Y, 0));
				break;
			case 50056:
				TeleportExecutor.teleport(c, new Position(Config.START_LOCATION_X, Config.START_LOCATION_Y, 0));
				break;
				
			case 130073:
				TeleportExecutor.teleport(c, new Position(1907, 4365, 0));
				break;

		case 4140://VARROCK TELE
			if(c.playerLevel[c.playerMagic] < 25){
				c.sendMessage("Your Magic level is not high enough for this spell");
				return;
			}
			if (System.currentTimeMillis() - c.teleDelay >= 2500) {
			if(c.getItems().playerHasItem(554, 1) && c.getItems().playerHasItem(556, 3) && c.getItems().playerHasItem(563, 1)){
			c.getItems().deleteItem2(554, 1);
			c.getItems().deleteItem2(556, 3);
			c.getItems().deleteItem2(563, 1);
			c.teleDelay = System.currentTimeMillis();
			TeleportExecutor.teleport(c, new Position(Config.VARROCK_X, Config.VARROCK_Y, 0));
			c.getPA().addSkillXP(35 * Config.MAGIC_EXP_RATE, c.playerMagic);
			c.getPA().refreshSkill(c.playerMagic);
			}else{
				c.sendMessage("You do not have the required runes to cast this spell.");
			}
			}
			break;

		case 4143:
		case 50245://LUMBY TELEPORT
			if(c.playerLevel[c.playerMagic] < 31){
				c.sendMessage("You need an magic level of 31 for this spell.");
				return;
			}
			if (System.currentTimeMillis() - c.teleDelay >= 2500) {
			if(c.getItems().playerHasItem(557, 1) && c.getItems().playerHasItem(556, 3) && c.getItems().playerHasItem(563, 1)){
			c.getItems().deleteItem2(557, 1);
			c.getItems().deleteItem2(556, 3);
			c.getItems().deleteItem2(563, 1);
			c.teleDelay = System.currentTimeMillis();
			TeleportExecutor.teleport(c, new Position(Config.LUMBY_X, Config.LUMBY_Y, 0));
			c.getPA().addSkillXP(41 * Config.MAGIC_EXP_RATE, c.playerMagic);
			c.getPA().refreshSkill(c.playerMagic);
			}else{
				c.sendMessage("You do not have the required runes to cast this spell.");
			}
			}
			break;

		case 50253:
		case 4146:
			if(c.playerLevel[c.playerMagic] < 37){
				c.sendMessage("You need an magic level of 37 for this spell.");
				return;
			}
			if (System.currentTimeMillis() - c.teleDelay >= 2500) {
			if(c.getItems().playerHasItem(555, 1) && c.getItems().playerHasItem(556, 3) && c.getItems().playerHasItem(563, 1)){
			c.getItems().deleteItem2(555, 1);
			c.getItems().deleteItem2(556, 3);
			c.getItems().deleteItem2(563, 1);
			c.teleDelay = System.currentTimeMillis();
			TeleportExecutor.teleport(c, new Position(Config.FALADOR_X, Config.FALADOR_Y, 0));
			c.getPA().addSkillXP(48 * Config.MAGIC_EXP_RATE, c.playerMagic);
			c.getPA().refreshSkill(c.playerMagic);
			}else{
				c.sendMessage("You do not have the required runes to cast this spell.");
			}
			}
			break;

		case 51005:
		case 4150:
			if(c.playerLevel[c.playerMagic] < 45){
				c.sendMessage("You need an magic level of 45 for this spell.");
				return;
			}
			TeleportExecutor.teleport(c, new Position(Config.CAMELOT_X, Config.CAMELOT_Y, 0));
			break;

		case 51013:
		case 6004:
			if(c.playerLevel[c.playerMagic] < 51){
				c.sendMessage("You need an magic level of 51 for this spell.");
				return;
			}
			TeleportExecutor.teleport(c, new Position(Config.ARDOUGNE_X, Config.ARDOUGNE_Y, 0));
			break;

		case 51023:
		case 6005:
			// c.getDH().sendOption5("Option 16", "Option 2", "Option 3",
			// "Option 4", "Option 5");
			// c.teleAction = 6;
			break;

		case 51031:
		case 29031:
			// c.getDH().sendOption5("Option 17", "Option 2", "Option 3",
			// "Option 4", "Option 5");
			// c.teleAction = 7;
			break;
		case 73245:
			Skillcape.performEmote(c, actionButtonId);
			break;
		case 23132: //unmorph
            c.isMorphed = false;
	c.setSidebarInterface(1, 3917);
	c.setSidebarInterface(2, 638);
	c.setSidebarInterface(3, 3213);
	c.setSidebarInterface(4, 1644);
	c.setSidebarInterface(5, 5608);
		if(c.playerMagicBook == 0) {
			c.setSidebarInterface(6, 1151);
		} else if (c.playerMagicBook == 1) {
			c.setSidebarInterface(6, 12855);
		} else if (c.playerMagicBook == 2) {
			c.setSidebarInterface(6, 29999);
		}
	c.setSidebarInterface(7, 18128);
	c.setSidebarInterface(8, 5065);
	c.setSidebarInterface(9, 5715); 
	c.setSidebarInterface(10, 2449);
	c.setSidebarInterface(11, 904);
	c.setSidebarInterface(12, 147);
	c.setSidebarInterface(13, 962);
	c.setSidebarInterface(0, 2423);
if (c.playerEquipment[c.playerRing] == 7927) {
c.getItems().deleteEquipment(c.playerEquipment[c.playerRing], c.playerRing);
c.getItems().addItem(7927,1);
}
if (c.playerEquipment[c.playerRing] == 6583) {
c.getItems().deleteEquipment(c.playerEquipment[c.playerRing], c.playerRing);
c.getItems().addItem(6583,1);
}
if (c.playerEquipment[c.playerRing] == 20017) {
c.getItems().deleteEquipment(c.playerEquipment[c.playerRing], c.playerRing);
c.getItems().addItem(20017,1);
}
if (c.playerEquipment[c.playerRing] == 20005) {
c.getItems().deleteEquipment(c.playerEquipment[c.playerRing], c.playerRing);
c.getItems().addItem(20005,1);
}
c.isNpc = false;
c.getCombat().getPlayerAnimIndex(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
c.getPA().requestUpdates();
c.updateRequired = true;
c.appearanceUpdateRequired = true;
break;
		case 72038:
		case 51039:
			// c.getDH().sendOption5("Option 18", "Option 2", "Option 3",
			// "Option 4", "Option 5");
			// c.teleAction = 8;
			break;

			/**Start of Combat Fixes by Andy (TrustedDealer)*/
			/*Deleted old combat styles because they were fucked up training the wrong skills
			 *Re-wrote Whole catagory
			 *Took 20 - 30 Mins
			 */
				//Attack
	      	case 9125: //Accurate
				case 22230://punch
				case 48010://flick (whip)
				case 14218://pound (mace)
				case 33020://jab (halberd)
				case 21200: //spike (pickaxe)
				case 6168: //chop (axe)
				case 8234: //stab (dagger)
				case 17102: //accurate (darts)
				case 6236: //accurate (long bow)
				case 1080: //bash (staff)
				case 6221: // range accurate
				case 30088: //claws (chop)
				case 1177: //hammer (pound)
				c.fightMode = 0;
				if (c.autocasting)
					c.getPA().resetAutocast();
				break;
				
				//Defence
			case 9126: //Defensive
				case 22228: //block (unarmed)
				case 48008: //deflect (whip)
				case 1175: //block (hammer)
				case 21201: //block (pickaxe)
				case 14219: //block (mace)
				case 1078: //focus - block (staff)
				case 33018: //fend (hally)
				case 6169: //block (axe)
				case 8235: //block (dagger)
				case 18078: //block (spear)
				case 30089: //block (claws)
				c.fightMode = 1;
				if (c.autocasting)
					c.getPA().resetAutocast();
				break;
				//All
			case 9127: // Controlled
				case 14220: //Spike (mace)
				case 6234: //longrange (long bow)
				case 6219: //longrange
				case 18077: //lunge (spear)
				case 18080: //swipe (spear)
				case 18079: //pound (spear)
				case 17100: //longrange (darts)
				c.fightMode = 3;
				if (c.autocasting)
					c.getPA().resetAutocast();
				break;
				//Strength
			case 9128: //Aggressive
				case 14221: //Pummel(mace)
				case 33019: //Swipe(Halberd)
				case 21203: //impale (pickaxe)
				case 21202: //smash (pickaxe)
				case 6171: //hack (axe)
				case 6170: //smash (axe)
				case 6220: // range rapid
				case 8236: //slash (dagger)
				case 8237: //lunge (dagger)
				case 30090: //claws (lunge)
				case 30091: //claws (Slash)
				case 1176: //stat hammer
				case 22229: //block (unarmed)
				case 1079: //pound (staff)
				case 6235: //rapid (long bow)
				case 17101: //repid (darts)
				c.fightMode = 2;
				if (c.autocasting)
					c.getPA().resetAutocast();
				break;
		/** Prayers **/
		case 21233: // thick skin
			c.getCombat().activatePrayer(0);
			break;
		case 21234: // burst of str
			c.getCombat().activatePrayer(1);
			break;
		case 21235: // charity of thought
			c.getCombat().activatePrayer(2);
			break;
		case 70080: // range
			c.getCombat().activatePrayer(3);
			break;
		case 70082: // mage
			c.getCombat().activatePrayer(4);
			break;
		case 21236: // rockskin
			c.getCombat().activatePrayer(5);
			break;
		case 21237: // super human
			c.getCombat().activatePrayer(6);
			break;
		case 21238: // improved reflexes
			c.getCombat().activatePrayer(7);
			break;
		case 21239: // hawk eye
			c.getCombat().activatePrayer(8);
			break;
		case 21240:
			c.getCombat().activatePrayer(9);
			break;
		case 21241: // protect Item
			c.getCombat().activatePrayer(10);
			break;
		case 70084: // 26 range
			c.getCombat().activatePrayer(11);
			break;
		case 70086: // 27 mage
			c.getCombat().activatePrayer(12);
			break;
		case 21242: // steel skin
			c.getCombat().activatePrayer(13);
			break;
		case 21243: // ultimate str
			c.getCombat().activatePrayer(14);
			break;
		case 21244: // incredible reflex
			c.getCombat().activatePrayer(15);
			break;
		case 21245: // protect from magic
			c.getCombat().activatePrayer(16);
			break;
		case 21246: // protect from range
			c.getCombat().activatePrayer(17);
			break;
		case 21247: // protect from melee
			c.getCombat().activatePrayer(18);
			break;
		case 70088: // 44 range
			c.getCombat().activatePrayer(19);
			break;
		case 70090: // 45 mystic
			c.getCombat().activatePrayer(20);
			break;
		case 2171: // retrui
			c.getCombat().activatePrayer(21);
			break;
		case 2172: // redem
			c.getCombat().activatePrayer(22);
			break;
		case 2173: // smite
			c.getCombat().activatePrayer(23);
			break;
		case 70092: // chiv
			c.getCombat().activatePrayer(24);
			break;
		case 70094: // piety
			c.getCombat().activatePrayer(25);
			break;

		case 13092:
			if (!Server.getMultiplayerSessionListener().inSession(c, MultiplayerSessionType.TRADE)) {
				c.sendMessage("You are not trading!");
				return;
			}
			//if (System.currentTimeMillis() - c.getTrade().getLastAccept() < 1000) {
			//	return;
			//}
			c.getTrade().setLastAccept(System.currentTimeMillis());
			Server.getMultiplayerSessionListener().getMultiplayerSession(c, MultiplayerSessionType.TRADE)
					.accept(c, MultiplayerSessionStage.OFFER_ITEMS);
			break;

		case 13218:
			if (!Server.getMultiplayerSessionListener().inSession(c, MultiplayerSessionType.TRADE)) {
				c.sendMessage("You are not trading!");
				return;
			}
			//if (System.currentTimeMillis() - c.getTrade().getLastAccept() < 1000) {
				//return;
			//}
			c.getTrade().setLastAccept(System.currentTimeMillis());
			Server.getMultiplayerSessionListener().getMultiplayerSession(c, MultiplayerSessionType.TRADE)
					.accept(c, MultiplayerSessionStage.CONFIRM_DECISION);
			break;
		/* Rules Interface Buttons */
		case 125011: // Click agree
			if (!c.ruleAgreeButton) {
				c.ruleAgreeButton = true;
				c.getPA().sendFrame36(701, 1);
			} else {
				c.ruleAgreeButton = false;
				c.getPA().sendFrame36(701, 0);
			}
			break;
		case 125003:// Accept
			if (c.ruleAgreeButton) {
				c.getPA().showInterface(3559);
				c.newPlayer = false;
			} else if (!c.ruleAgreeButton) {
				c.sendMessage("You need to click on you agree before you can continue on.");
			}
			break;
		case 125006:// Decline
			c.sendMessage("You have chosen to decline, Client will be disconnected from the Ghreborn.");
			break;
		/* End Rules Interface Buttons */
		case 74206:// area1
			c.getPA().sendFrame36(509, 1);
			c.getPA().sendFrame36(510, 0);
			c.getPA().sendFrame36(511, 0);
			c.getPA().sendFrame36(512, 0);
			break;
		case 74207:// area2
			c.getPA().sendFrame36(509, 0);
			c.getPA().sendFrame36(510, 1);
			c.getPA().sendFrame36(511, 0);
			c.getPA().sendFrame36(512, 0);
			break;
		case 74208:// area3
			c.getPA().sendFrame36(509, 0);
			c.getPA().sendFrame36(510, 0);
			c.getPA().sendFrame36(511, 1);
			c.getPA().sendFrame36(512, 0);
			break;
		case 74209:// area4
			c.getPA().sendFrame36(509, 0);
			c.getPA().sendFrame36(510, 0);
			c.getPA().sendFrame36(511, 0);
			c.getPA().sendFrame36(512, 1);
			break;
		case 168:
			//c.animation(855);
			TeleportExecutor.teleport(c, new Position(Config.START_LOCATION_X, Config.START_LOCATION_Y, 0));
			break;
		case 169:
			//c.animation(856);
			//c.getPA().showInterface(33550);
			TeleportExecutor.teleport(c, new Position(1772, 5497, 0));
			break;
		case 74024:
			c.getPA().showInterface(33550);
			break;
		case 162://cape emote
			//c.animation(857);
			break;
		case 164:
			//c.animation(858);
			TeleportExecutor.teleport(c, new Position(3254, 3287, 0));
			break;
		case 165:
			//c.animation(859);
			TeleportExecutor.teleport(c, new Position(3085, 3518, 0));
			break;
		case 161:
			//c.animation(860);
			TeleportExecutor.teleport(c, new Position(1564, 2845, 0));
			break;
		case 170:
			TeleportExecutor.teleport(c, new Position(2595, 3420, 0));
			break;
		case 171:
			TeleportExecutor.teleport(c, new Position(2388, 3488, 0));
			break;
		case 163:
			//c.animation(863);
			break;
		case 167:
			TeleportExecutor.teleport(c, new Position(1643, 2849, 1));
			break;
		case 172:
			//c.animation(865);
			break;
		case 166:
			// TWIST
			if (c.emotes == 0) {
				c.emotes = 1;
				c.playerStandIndex = 921;
				c.updateRequired = true;
				c.appearanceUpdateRequired = true;
			} else {
				c.emotes = 0;
				c.playerStandIndex = c.playerStandIndex;
				c.updateRequired = true;
				c.appearanceUpdateRequired = true;
			}
			break;
		case 52050:
			c.animation(2105);
			break;
		case 52051:
			c.animation(2106);
			break;
		case 52052:
			c.animation(2107);
			break;
		case 52053:
			c.animation(2108);
			break;
		case 52054:
			c.animation(2109);
			break;
		case 52055:
			c.animation(2110);
			break;
		case 52056:
			//c.animation(2111);
			break;
		case 52057:
			//c.animation(2112);
			break;
		case 52058:
			//c.animation(2113);
			break;
		case 43092:
			//c.animation(0x558);
			break;
		case 2155:
			//c.animation(0x46B);
			break;
		case 25103:
			//c.animation(0x46A);
			break;
		case 25106:
			//c.animation(0x469);
			break;
		case 2154:
			//c.animation(0x468);
			break;
		case 52071:
			//c.animation(0x84F);
			break;
		case 52072:
			//c.animation(0x850);
			break;
		case 59062:
			//c.animation(2836);
			break;
		case 72032:
			//c.animation(3544);
			break;
		case 72033:
			//c.animation(3543);
			break;
		case 72254:
			//c.animation(3866);
			break;
		/* END OF EMOTES */
		case 28166:

			break;
		case 33206:
			// c.getPA().vengMe();
			// SkillMenu.openInterface(c, -1)
			// SkillMenu.openInterface(c,0);
			break;
		case 33212:
			// SkillMenu.openInterface(c, 1);
			break;
		case 33209:
			// SkillMenu.openInterface(c,2);
			break;
		case 33215:
			// SkillMenu.openInterface(c, 4);
			break;


		case 24017:
			c.getPA().resetAutocast();
			// c.sendFrame246(329, 200, c.playerEquipment[c.playerWeapon]);
			c.getItems()
					.sendWeapon(
							c.playerEquipment[c.playerWeapon],
							c.getItems().getItemName(
									c.playerEquipment[c.playerWeapon]));
			// c.setSidebarInterface(0, 328);
			// c.setSidebarInterface(6, c.playerMagicBook == 0 ? 1151 :
			// c.playerMagicBook == 1 ? 12855 : 1151);
			break;
		}
		if (c.isAutoButton(actionButtonId))
			c.assignAutocast(actionButtonId);
	}

}
