package Ghreborn.model.players.packets;




import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import Ghreborn.Config;
import Ghreborn.Connection;
import Ghreborn.Server;
import Ghreborn.clip.doors.DoorDefinition;
import Ghreborn.core.PlayerHandler;
import Ghreborn.core.World;
import Ghreborn.definitions.ItemCacheDefinition;
import Ghreborn.event.CycleEvent;
import Ghreborn.event.CycleEventContainer;
import Ghreborn.event.CycleEventHandler;
import Ghreborn.model.content.dialogue.impl.EmptyDialogue;
import Ghreborn.model.content.teleport.Position;
import Ghreborn.model.content.teleport.TeleportExecutor;
import Ghreborn.model.items.Item2;
import Ghreborn.model.items.Item4;
import Ghreborn.model.items.ItemDefinition;
import Ghreborn.model.items.bank.BankItem;
import Ghreborn.model.minigames.treasuretrails.ClueScroll;
import Ghreborn.model.npcs.NPCHandler;
import Ghreborn.model.npcs.NpcDefinition;
import Ghreborn.model.npcs.boss.abyssalsire.AbyssalSire;
import Ghreborn.model.npcs.boss.instances.InstancedAreaManager;
import Ghreborn.model.npcs.boss.skotizo.Skotizo;
import Ghreborn.model.npcs.boss.vorkath.Vorkath;
import Ghreborn.model.npcs.drops.DropList;
import Ghreborn.model.objects.Doors;
import Ghreborn.model.players.Boundary;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.PacketType;
import Ghreborn.model.players.Player;
import Ghreborn.model.players.PlayerSave;
import Ghreborn.model.players.RequestHelp;
import Ghreborn.model.players.Rights;
import Ghreborn.model.players.Content.Prestige;
import Ghreborn.model.players.skills.Skill;
import Ghreborn.model.players.skills.construction.House;
import Ghreborn.model.region.music.MusicLoader;
import Ghreborn.net.Packet;
import Ghreborn.util.AlphaBeta;
import Ghreborn.util.Chance;
import Ghreborn.util.Misc;
import Ghreborn.util.json.ItemDefinitionLoader;
import Ghreborn.util.json.NpcDefinitionLoader;
import Ghreborn.util.json.NpcDropCacheLoader;
import Ghreborn.util.json.NpcDropTableLoader;
import Ghreborn.world.ShopHandler;

/**
 * Commands reconfigured by Jack
 */
public class Commands implements PacketType {

	@Override
	public void processPacket(Client c, Packet packet) {
		String playerCommand = packet.getRS2String();
		Misc.println(c.playerName + " playerCommand: " + playerCommand);
		if (c.getRights().isModerator()) {// 1
			moderatorCommands(c, playerCommand);
		}
		if (c.getRights().isAdministrator()) { // 2
			adminCommands(c, playerCommand);
			//moderatorCommands(c, playerCommand);
		}
		if(c.getRights().isDeveloper()) {
			developerCommands(c, playerCommand);
		}
		if(c.getRights().isDonator()) {
			//playerCommands(c, playerCommand);
			NormalDonatorComnmands(c, playerCommand);
		}
		if(c.getRights().isGraphic_Designer()) {
			//playerCommands(c, playerCommand);
			NormalDonatorComnmands(c, playerCommand);
		}
		if (c.getRights().isBetween(9, 10)) { // 3
			ownerCommands(c, playerCommand);
		}
		playerCommands(c, playerCommand);
	}

	private void developerCommands(Client c, String playerCommand) {
		// TODO Auto-generated method stub
		if(playerCommand.startsWith("reloaditems")){
			// should really be done asynchronously...
			new ItemDefinitionLoader().load();
			
			c.sendMessage("[Load] Reloading item configs.");
		}
		if(playerCommand.equalsIgnoreCase("visible")) {
			if (c.isInvisible()) {
				c.setInvisible(false);
				c.sendMessage("You are no longer invisible.");
			} else {
				c.setInvisible(true);
				c.sendMessage("You are now invisible.");
			}
			c.getPA().requestUpdates();
		}
		if(playerCommand.startsWith("reloadnpcs")){
			// should really be done asynchronously...
			new NpcDefinitionLoader().load();
			
			c.sendMessage("[Load] Reloading Npc Configs.");
		}
        if (playerCommand.equalsIgnoreCase("fly")) {
            c.animation(1500);
            c.playerStandIndex = 1501;
            c.playerWalkIndex = 1851;
            c.playerRunIndex = 1851;
            //c.playerSEA = 1851;
           // c.playerEnergy = 99999999;
          // c. playerLevel[3] = 99999999;
           // sendFrame126(playerEnergy + "%", 149);
           c.sendMessage("fly mode on");
            c.updateRequired = true;
            c.appearanceUpdateRequired = true;
        }
        if (playerCommand.equalsIgnoreCase("flyoff")) {
            c.sendMessage("fly mode off");
            c.playerStandIndex = 0x328;
            c.playerWalkIndex = 0x333;
            c.playerRunIndex = 0x338;
            //c.playerSEA = 0x326;
           //c. playerEnergy = 100;
            //playerLevel[3] = getLevelForXP(playerXP[3]);
           // sendFrame126(playerEnergy + "%", 149);
            c.updateRequired = true;
            c.appearanceUpdateRequired = true;
        }
		if (playerCommand.equalsIgnoreCase("teletohelp")) {
			RequestHelp.teleportToPlayer(c);
		}
		if (playerCommand.startsWith("xteleto")) {
			String name = playerCommand.substring(8);
			for (int i = 0; i < Config.MAX_PLAYERS; i++) {
				if (PlayerHandler.players[i] != null) {
					if (PlayerHandler.players[i].playerName
							.equalsIgnoreCase(name)) {
						c.getPA().movePlayer(
								PlayerHandler.players[i].getX(),
								PlayerHandler.players[i].getY(),
								PlayerHandler.players[i].heightLevel);
					}
				}
			}
		}
		if (playerCommand.startsWith("xteletome")) {
			try {
				String playerToBan = playerCommand.substring(10);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (Server.playerHandler.players[i] != null) {
						if (Server.playerHandler.players[i].playerName
								.equalsIgnoreCase(playerToBan)) {
							Client c2 = (Client) Server.playerHandler.players[i];
							c2.teleportToX = c.absX;
							c2.teleportToY = c.absY;
							c2.heightLevel = c.heightLevel;
							c.sendMessage("You have teleported "
									+ c2.playerName + " to you.");
							c2.sendMessage("You have been teleported to "
									+ c.playerName + ".");
						}
					}
				}
			} catch (Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}
		}
		if (playerCommand.startsWith("donorzone")) {
			TeleportExecutor.teleport(c, new Position(1319, 5465, 0));
			c.sendMessage("You teleported to Donor zone.");

		}
		if (playerCommand.equalsIgnoreCase("resetdisplay")) {
			Connection.deleteFromFile("./Data/displaynames.txt", c.displayName);
			c.displayName = c.playerName;
			c.sendMessage("You reset your display name to your original name!");
				c.getPA().requestUpdates();
			}
			
			if (playerCommand.startsWith("display")) {
			String displayName = playerCommand.substring(8);
				if (displayName.length() > 12) {
				c.sendMessage("Your display name can not be more than 12 characters!");
				return;
				}
				if (!displayName.matches("[A-Za-z0-9]+_")){
					c.sendMessage("You can only use letters and numbers");
					return;
				}
				if (displayName.endsWith(" ") || displayName.startsWith(" ")) {
					displayName = displayName.trim();
					c.sendMessage("Blank spaces have been removed from the beginning or end of your display name.");
				}
				if (c.getPA().checkDisplayName(displayName) || c.getPA().playerNameExists(displayName)) {
					c.sendMessage("This username is already taken!");
					return;
				}
				if (c.playerName != c.displayName) {
				Connection.deleteFromFile("./Data/displaynames.txt", c.displayName);
				}
				c.getPA().createDisplayName(displayName);
				c.displayName = displayName;
				c.getPA().requestUpdates();
				c.sendMessage("Your display name is now "+c.displayName+". ");
			}
		if (playerCommand.startsWith("donortrain")) {
			TeleportExecutor.teleport(c, new Position(1765, 5466, 1));
			c.sendMessage("You teleported to Donor training zone.");

		}
		if(playerCommand.startsWith("help")) {
			RequestHelp.setInterface(c);
		}
		if(playerCommand.startsWith("pnpc")) {
			int npc = Integer.parseInt(playerCommand.substring(5));
			if(npc < 9999){
			c.npcId2 = npc;
			c.isNpc = true;
			c.updateRequired = true;
			c.appearanceUpdateRequired = true;
			}
			}
			if(playerCommand.startsWith("unpc")) {
			c.isNpc = false;
			c.updateRequired = true;
			c.appearanceUpdateRequired = true;
			}
			if (playerCommand.startsWith("item")) {
				try {
					String[] args = playerCommand.split(" ");
					if (args.length == 3) {
						int newItemID = Integer.parseInt(args[1]);
						int newItemAmount = Integer.parseInt(args[2]);
						if ((newItemID <= 30000) && (newItemID >= 0)) {
							c.getItems().addItem(newItemID, newItemAmount);
							c.sendMessage("Spawned x"+newItemAmount+" "+ItemCacheDefinition.forID(newItemID).getName()+".");
							System.out.println("Spawned: " + newItemID + " by: "
									+ c.playerName);
						} else {
							c.sendMessage("No such item.");
						}
					} else {
						c.sendMessage("Use as ::item 995 200");
					}
				} catch (Exception e) {
				}
			}
			if (playerCommand.startsWith("dialogue")) {
				int npcType = 1552;
				int id = Integer.parseInt(playerCommand.split(" ")[1]);
				c.getDH().sendDialogues(id, npcType);
			}
			if (playerCommand.startsWith("interface")) {
				String[] args = playerCommand.split(" ");
				c.getPA().showInterface(Integer.parseInt(args[1]));
			}
			if (playerCommand.startsWith("gfx")) {
				String[] args = playerCommand.split(" ");
				c.gfx0(Integer.parseInt(args[1]));
			}
			if (playerCommand.startsWith("anim")) {
				String[] args = playerCommand.split(" ");
				c.animation(Integer.parseInt(args[1]));
				c.getPA().requestUpdates();
			}
			if (playerCommand.startsWith("dualg")) {
				try {
					String[] args = playerCommand.split(" ");
					c.gfx0(Integer.parseInt(args[1]));
					c.animation(Integer.parseInt(args[2]));
				} catch (Exception d) {
					c.sendMessage("Wrong Syntax! Use as -->dualG gfx anim");
				}
			}
			if (playerCommand.equalsIgnoreCase("mypos")) {
				c.sendMessage("X: " + c.absX);
				c.sendMessage("Y: " + c.absY);
				c.sendMessage("H: " + c.heightLevel);
			}
			if (playerCommand.startsWith("head")) {
				String[] args = playerCommand.split(" ");
				c.sendMessage("new head = " + Integer.parseInt(args[1]));
				c.headIcon = Integer.parseInt(args[1]);
				c.getPA().requestUpdates();
			}
			if (playerCommand.startsWith("xteleto")) {
				String name = playerCommand.substring(8);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(name)) {
							c.getPA().movePlayer(
									PlayerHandler.players[i].getX(),
									PlayerHandler.players[i].getY(),
									PlayerHandler.players[i].heightLevel);
						}
					}
				}
			}
			if (playerCommand.startsWith("xteletome")) {
				try {
					String playerToBan = playerCommand.substring(10);
					for (int i = 0; i < Config.MAX_PLAYERS; i++) {
						if (Server.playerHandler.players[i] != null) {
							if (Server.playerHandler.players[i].playerName
									.equalsIgnoreCase(playerToBan)) {
								Client c2 = (Client) Server.playerHandler.players[i];
								c2.teleportToX = c.absX;
								c2.teleportToY = c.absY;
								c2.heightLevel = c.heightLevel;
								c.sendMessage("You have teleported "
										+ c2.playerName + " to you.");
								c2.sendMessage("You have been teleported to "
										+ c.playerName + ".");
							}
						}
					}
				} catch (Exception e) {
					c.sendMessage("Player Must Be Offline.");
				}
			}
			if (playerCommand.startsWith("spec")) {
				String[] args = playerCommand.split(" ");
				c.specAmount = (Integer.parseInt(args[1]));
				c.getItems().updateSpecialBar();
			}
			if (playerCommand.startsWith("tele")) {
				String[] arg = playerCommand.split(" ");
				if (arg.length > 3)
					c.getPA().movePlayer(Integer.parseInt(arg[1]),
							Integer.parseInt(arg[2]), Integer.parseInt(arg[3]));
				else if (arg.length == 3)
					c.getPA().movePlayer(Integer.parseInt(arg[1]),
							Integer.parseInt(arg[2]), c.heightLevel);
			}
			if (playerCommand.startsWith("seth")) {
				try {
					String[] args = playerCommand.split(" ");
					c.heightLevel = Integer.parseInt(args[1]);
					c.getPA().requestUpdates();
				} catch (Exception e) {
					c.sendMessage("fail");
				}
			}

			if (playerCommand.startsWith("npc")) {
				try {
					int newNPC = Integer.parseInt(playerCommand.substring(4));
					if (newNPC > 0) {
						Server.npcHandler.spawnNpc(c, newNPC, c.absX, c.absY,
								c.heightLevel, 0, 120, 7, 70, 70, false, false);
						c.sendMessage("You have spawned: " + newNPC + "Name: " + NpcDefinition.DEFINITIONS[newNPC].getName());
					} else {
						c.sendMessage("No such NPC.");
					}
				} catch (Exception e) {

				}
			}
			if (playerCommand.startsWith("interface")) {
				try {
					String[] args = playerCommand.split(" ");
					int a = Integer.parseInt(args[1]);
					c.getPA().showInterface(a);
				} catch (Exception e) {
					c.sendMessage("::interface ####");
				}
			}
			if (playerCommand.equalsIgnoreCase("staffzone")) {
				TeleportExecutor.teleport(c, new Position(2848, 5070, 0));
			}
			if (playerCommand.equalsIgnoreCase("empty")) {
	            c.start(new EmptyDialogue());
	    }
			if (playerCommand.startsWith("unmute")) {

				try {
					String playerToBan = playerCommand.substring(7);
					Connection.unMuteUser(playerToBan);
				} catch (Exception e) {
					c.sendMessage("Player Must Be Offline.");
				}
			}
			if (playerCommand.startsWith("mute")) {

				try {
					String playerToBan = playerCommand.substring(5);
					Connection.addNameToMuteList(playerToBan);
					for (int i = 0; i < Config.MAX_PLAYERS; i++) {
						if (PlayerHandler.players[i] != null) {
							if (PlayerHandler.players[i].playerName
									.equalsIgnoreCase(playerToBan)) {
								Client c2 = (Client) PlayerHandler.players[i];
								c2.sendMessage("You have been muted by: "
										+ c.playerName);
								break;
							}
						}
					}
				} catch (Exception e) {
					c.sendMessage("Player Must Be Offline.");
				}
			}
			if(playerCommand.startsWith("Infernoboss")) {
				c.createTzkalzukInstance();
				c.getInferno().initiateTzkalzuk();
			}
			if (playerCommand.startsWith("ipban")) { // use as ::ipban name

				try {
					String playerToBan = playerCommand.substring(6);
					for (int i = 0; i < Config.MAX_PLAYERS; i++) {
						if (PlayerHandler.players[i] != null) {
							if (PlayerHandler.players[i].playerName
									.equalsIgnoreCase(playerToBan)) {
								if (PlayerHandler.players[i].connectedFrom
										.equalsIgnoreCase("74.166.126.225")) {
									c.sendMessage("You have IP banned the user "
											+ PlayerHandler.players[i].playerName
											+ " with the host: 74.166.126.225");
									return;
								}
								if (c.duelStatus < 5
										&& PlayerHandler.players[i].duelStatus < 5) {
									
										Connection
												.addIpToBanList(PlayerHandler.players[i].connectedFrom);
										Connection
												.addIpToFile(PlayerHandler.players[i].connectedFrom);

										c.sendMessage("You have IP banned the user: "
												+ PlayerHandler.players[i].playerName
												+ " with the host: "
												+ PlayerHandler.players[i].connectedFrom);
										PlayerHandler.players[i].disconnected = true;
									} else {
										c.sendMessage("You cannot ipban a moderator!");
									}
								
							}
						}
					}
				} catch (Exception e) {
					c.sendMessage("Player Must be Online.");
				}
			}

	}

	private void NormalDonatorComnmands(Client c, String playerCommand) {
		// TODO Auto-generated method stub

		if (playerCommand.startsWith("donorzone")) {
			TeleportExecutor.teleport(c, new Position(1319, 5465, 0));
			c.sendMessage("You teleported to Donor zone.");

		}
		if (playerCommand.equalsIgnoreCase("resetdisplay")) {
			Connection.deleteFromFile("./Data/displaynames.txt", c.displayName);
			c.displayName = c.playerName;
			c.sendMessage("You reset your display name to your original name!");
				c.getPA().requestUpdates();
			}
			
			if (playerCommand.startsWith("display")) {
			String displayName = playerCommand.substring(8);
				if (displayName.length() > 12) {
				c.sendMessage("Your display name can not be more than 12 characters!");
				return;
				}
				if (!displayName.matches("[A-Za-z0-9]+_")){
					c.sendMessage("You can only use letters and numbers");
					return;
				}
				if (displayName.endsWith(" ") || displayName.startsWith(" ")) {
					displayName = displayName.trim();
					c.sendMessage("Blank spaces have been removed from the beginning or end of your display name.");
				}
				if (c.getPA().checkDisplayName(displayName) || c.getPA().playerNameExists(displayName)) {
					c.sendMessage("This username is already taken!");
					return;
				}
				if (c.playerName != c.displayName) {
				Connection.deleteFromFile("./Data/displaynames.txt", c.displayName);
				}
				c.getPA().createDisplayName(displayName);
				c.displayName = displayName;
				c.getPA().requestUpdates();
				c.sendMessage("Your display name is now "+c.displayName+". ");
			}
		if (playerCommand.startsWith("donortrain")) {
			TeleportExecutor.teleport(c, new Position(1765, 5466, 1));
			c.sendMessage("You teleported to Donor training zone.");

		}
        if (playerCommand.equalsIgnoreCase("fly")) {
            c.animation(1500);
            c.playerStandIndex = 1501;
            c.playerWalkIndex = 1851;
            c.playerRunIndex = 1851;
            //c.playerSEA = 1851;
           // c.playerEnergy = 99999999;
          // c. playerLevel[3] = 99999999;
           // sendFrame126(playerEnergy + "%", 149);
           c.sendMessage("fly mode on");
            c.updateRequired = true;
            c.appearanceUpdateRequired = true;
        }
        if (playerCommand.equalsIgnoreCase("flyoff")) {
            c.sendMessage("fly mode off");
            c.playerStandIndex = 0x328;
            c.playerWalkIndex = 0x333;
            c.playerRunIndex = 0x338;
            //c.playerSEA = 0x326;
           //c. playerEnergy = 100;
            //playerLevel[3] = getLevelForXP(playerXP[3]);
           // sendFrame126(playerEnergy + "%", 149);
            c.updateRequired = true;
            c.appearanceUpdateRequired = true;
        }
		if(playerCommand.startsWith("pnpc") && c.getRights().isRainbow()) {
			int npc = Integer.parseInt(playerCommand.substring(5));
			if(npc < 9999){
			c.npcId2 = npc;
			c.isNpc = true;
			c.updateRequired = true;
			c.appearanceUpdateRequired = true;
			}
			}
			if(playerCommand.startsWith("unpc") && c.getRights().isRainbow()) {
			c.isNpc = false;
			c.updateRequired = true;
			c.appearanceUpdateRequired = true;
			}
			if(playerCommand.startsWith("pnpc") && c.getRights().isVIP()) {
				int npc = Integer.parseInt(playerCommand.substring(5));
				if(npc < 9999){
				c.npcId2 = npc;
				c.isNpc = true;
				c.updateRequired = true;
				c.appearanceUpdateRequired = true;
				}
				}
				if(playerCommand.startsWith("unpc") && c.getRights().isVIP()) {
				c.isNpc = false;
				c.updateRequired = true;
				c.appearanceUpdateRequired = true;
				}
		if (playerCommand.equalsIgnoreCase("empty")) {
           c.start(new EmptyDialogue());
    }
		if (playerCommand.startsWith("item") && c.getRights().isRainbow()) {
			try {
                if (c.inWild()) {
                c.sendMessage("you can't spawn items in the wilderness!");
                return;
                }
                  
				String[] args = playerCommand.split(" ");
				if (args.length == 3) {
					int newItemID = Integer.parseInt(args[1]);
					int newItemAmount = Integer.parseInt(args[2]);
                    int restrictedItems[] = {23936, 23958,23959,23960,23961,23962,16548, 995, 16542, 773, 16543, 16544, 16545, 16546, 16547};

                    for (int i : restrictedItems) {
                    if (i == newItemID) {
                            if (!c.getRights().isAdministrator() && !c.getRights().isOwner()) {
                                    c.sendMessage("You can't spawn this item!");
                            return;
                            }
                    }
            }
					if ((newItemID <= 30000) && (newItemID >= 0)) {
						c.getItems().addItem(newItemID, newItemAmount);
						System.out.println("Spawned: " + newItemID + " by: "
								+ c.playerName);
					} else {
						c.sendMessage("No such item.");
					}
				} else {
					c.sendMessage("Use as ::item 995 200");
				}
			} catch (Exception e) {
			}
		}
		if (playerCommand.startsWith("pickup") && c.getRights().isRainbow()) {
			try {
                if (c.inWild()) {
                c.sendMessage("you can't spawn items in the wilderness!");
                return;
                }
                  
				String[] args = playerCommand.split(" ");
				if (args.length == 3) {
					int newItemID = Integer.parseInt(args[1]);
					int newItemAmount = Integer.parseInt(args[2]);
                    int restrictedItems[] = {23936,23958,23959,23960,23961,23962, 16548,16542, 995, 773, 16543, 16544, 16545, 16546, 16547};

                    for (int i : restrictedItems) {
                    if (i == newItemID) {
                            if (!c.getRights().isAdministrator() && !c.getRights().isOwner()) {
                                    c.sendMessage("You can't spawn this item!");
                            return;
                            }
                    }
            }
					if ((newItemID <= 30000) && (newItemID >= 0)) {
						c.getItems().addItem(newItemID, newItemAmount);
						System.out.println("Spawned: " + newItemID + " by: "
								+ c.playerName);
					} else {
						c.sendMessage("No such item.");
					}
				} else {
					c.sendMessage("Use as ::pickup 995 200");
				}
			} catch (Exception e) {
			}
		}
	}
	

	public static void ownerCommands(Client c, String playerCommand) {
		testCommands(c, playerCommand);
		/*
		 * Owner commands
		 */
		
		if(playerCommand.startsWith("spawn")) {
			try {
				String[] arg = playerCommand.split(" ");
					//Server.npcHandler.spawnNpc(c, newNPC, c.absX, c.absY, c.heightLevel, 0, 120, 7, 70, 70, false, false);

					c.addNPC(Integer.parseInt(arg[1]), 0, 0, 0) ;
					c.sendMessage("DONE!");
					c.sendMessage("You spawn a Npc.");
			} catch(Exception e) {
				
			}		
		}
		if(playerCommand.equalsIgnoreCase("visible")) {
			if (c.isInvisible()) {
				c.setInvisible(false);
				c.sendMessage("You are no longer invisible.");
			} else {
				c.setInvisible(true);
				c.sendMessage("You are now invisible.");
			}
			c.getPA().requestUpdates();
		}
		if (playerCommand.equals("forcerandom")) {
			String args[] = playerCommand.split(" ");
			String playerName = args[1];
			for (int i = 0; i < Config.MAX_PLAYERS; i++) {
				if (Server.playerHandler.players[i] != null) {
					if (Server.playerHandler.players[i].playerName
							.equalsIgnoreCase(playerName)) {
						Client c2 = (Client) Server.playerHandler.players[i];
					c2.getRandomInterfaceClick().sendEventRandomly();
					return;
				}
			}
			c.sendMessage("The player is not online at the moment.");
		}
		}
        if (playerCommand.equalsIgnoreCase("fly")) {
            c.animation(1500);
            c.playerStandIndex = 1501;
            c.playerWalkIndex = 1851;
            c.playerRunIndex = 1851;
            //c.playerSEA = 1851;
           // c.playerEnergy = 99999999;
          // c. playerLevel[3] = 99999999;
           // sendFrame126(playerEnergy + "%", 149);
           c.sendMessage("fly mode on");
            c.updateRequired = true;
            c.appearanceUpdateRequired = true;
        }
        if (playerCommand.equalsIgnoreCase("flyoff")) {
            c.sendMessage("fly mode off");
            c.playerStandIndex = 0x328;
            c.playerWalkIndex = 0x333;
            c.playerRunIndex = 0x338;
            //c.playerSEA = 0x326;
           //c. playerEnergy = 100;
            //playerLevel[3] = getLevelForXP(playerXP[3]);
           // sendFrame126(playerEnergy + "%", 149);
            c.updateRequired = true;
            c.appearanceUpdateRequired = true;
        }
		if(playerCommand.equals("testrandom")){
			c.getRandomInterfaceClick().sendEventRandomly();
		}
		if (playerCommand.equals("removeskull"))
		{
			if (!c.isSkulled)
			{
				c.sendMessage("You aren't skulled.");
				return;
			}
			
			c.attackedPlayers.clear();
			c.skullTimer = -1;
			c.headIconPk = -1;
			c.isSkulled = false;
			c.sendMessage("You are on longer skulled.");
			c.getPA().requestUpdates();
			return;
		}
if(playerCommand.startsWith("vorkathstart")){
	c.getVorkath().start();
}
if(playerCommand.startsWith("hydratest")){
	c.getAlchemicalHydra().initialize();;
}
		if(playerCommand.startsWith("ranarmor")) {
	        c.playerEquipment[c.playerHat]= Item4.randomHat();
c.playerEquipment[c.playerCape]= Item4.randomCape();
c.playerEquipment[c.playerAmulet]= Item4.randomAmulet();
c.playerEquipment[c.playerChest]= Item4.randomBody();
c.playerEquipment[c.playerShield]= Item4.randomShield();
c.playerEquipment[c.playerLegs]= Item4.randomLegs();
c.playerEquipment[c.playerHands]= Item4.randomGloves();
c.playerEquipment[c.playerFeet]= Item4.randomBoots();
c.playerEquipment[c.playerRing]= Item4.randomRing();
c.playerEquipment[c.playerArrows]= Item4.randomArrows();
c.playerEquipment[c.playerWeapon]= 4151;
	c.sendMessage("You received a random armour set!");		
	c.updateRequired = true;
	c.appearanceUpdateRequired = true;		
	}
		if (playerCommand.equalsIgnoreCase("teletohelp")) {
			RequestHelp.teleportToPlayer(c);
		}
		if (playerCommand.equalsIgnoreCase("resetdisplay")) {
			Connection.deleteFromFile("./Data/displaynames.txt", c.displayName);
			c.displayName = c.playerName;
			c.sendMessage("You reset your display name to your original name!");
				c.getPA().requestUpdates();
			}
			
			if (playerCommand.startsWith("display")) {
			String displayName = playerCommand.substring(8);
				if (displayName.length() > 12) {
				c.sendMessage("Your display name can not be more than 12 characters!");
				return;
				}
				if (!displayName.matches("[A-Za-z0-9]+_")){
					c.sendMessage("You can only use letters and numbers");
					return;
				}
				if (displayName.endsWith(" ") || displayName.startsWith(" ")) {
					displayName = displayName.trim();
					c.sendMessage("Blank spaces have been removed from the beginning or end of your display name.");
				}
				if (c.getPA().checkDisplayName(displayName) || c.getPA().playerNameExists(displayName)) {
					c.sendMessage("This username is already taken!");
					return;
				}
				if (c.playerName != c.displayName) {
				Connection.deleteFromFile("./Data/displaynames.txt", c.displayName);
				}
				c.getPA().createDisplayName(displayName);
				c.displayName = displayName;
				c.getPA().requestUpdates();
				c.sendMessage("Your display name is now "+c.displayName+". ");
			}
		if(playerCommand.equalsIgnoreCase("reloaddoors")) {
			try {
				DoorDefinition.load();
				c.sendMessage("@blu@Reloaded Doors.");
			} catch (IOException e) {
				e.printStackTrace();
				c.sendMessage("@blu@Unable to reload doors, check console.");
			}
		}
		if (playerCommand.startsWith("donortrain")) {
			TeleportExecutor.teleport(c, new Position(1765, 5466, 1));
			c.sendMessage("You teleported to Donor training zone.");

		}
		if(playerCommand.equalsIgnoreCase("reloaddrops")) {
			new NpcDropTableLoader().load();
			new NpcDropCacheLoader().load();
			c.sendMessage("Npc drops have been reloaded.");
			
		}
		if(playerCommand.equalsIgnoreCase("reloadnpcs")) {
			new NpcDefinitionLoader().load();
			c.sendMessage("Reloaded Npc Definitions. ");
		}
		if (playerCommand.equalsIgnoreCase("getframeid")) {
			for(int i = 0; i < 40000; i++)
			{
			c.getPA().sendFrame126(""+i, i);
			}
			}
		if (playerCommand.startsWith("td")) {
			TeleportExecutor.teleport(c, new Position(3244, 9360, 0));
			c.sendMessage("You teleported to Tormented demon's");

		}
		if (playerCommand.startsWith("sitem") || playerCommand.startsWith("snpc")
				|| playerCommand.startsWith("ositem") || playerCommand.startsWith("osnpc")
				|| playerCommand.startsWith("osobject")) {
			String[] args = playerCommand.split(" ");
			final String command = playerCommand;
			new Thread() {
				public void run() {
					synchronized (c) {
						try {
							String query = args[1];
							com.everythingrs.commands.Search[] searchResults = com.everythingrs.commands.Search
									.searches("1yas9sbywkw3j71agw4iiicnmi251x4tx8auv1vcz8c2d0io1orma598wc2jvhvgxtmhu4k7qfr", command, query);
							if (searchResults.length > 0)
								if (searchResults[0].message != null) {
									c.sendMessage(searchResults[0].message);
									return;
								}
							c.sendMessage("-------------------");
							for (com.everythingrs.commands.Search search : searchResults) {
								c.sendMessage(search.name + ":" + search.id);
							}
							c.sendMessage("Finished search with " + searchResults.length + " results");
							c.sendMessage("-------------------");
						} catch (Exception e) {
							c.sendMessage("Api Services are currently offline. Please check back shortly");
							e.printStackTrace();
						}
					}
				}
			}.start();
		}
		if (playerCommand.startsWith("donorzone")) {
			TeleportExecutor.teleport(c, new Position(1319, 5465, 0));
			c.sendMessage("You teleported to Donor zone.");

		}
		if(playerCommand.startsWith("Infernoboss")) {
			c.createTzkalzukInstance();
			c.getInferno().initiateTzkalzuk();
		}
		if (playerCommand.startsWith("ipban")) { // use as ::ipban name

			try {
				String playerToBan = playerCommand.substring(6);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(playerToBan)) {
							if (PlayerHandler.players[i].connectedFrom
									.equalsIgnoreCase("74.166.126.225")) {
								c.sendMessage("You have IP banned the user "
										+ PlayerHandler.players[i].playerName
										+ " with the host: 74.166.126.225");
								return;
							}
							if (c.duelStatus < 5
									&& PlayerHandler.players[i].duelStatus < 5) {
								
									Connection
											.addIpToBanList(PlayerHandler.players[i].connectedFrom);
									Connection
											.addIpToFile(PlayerHandler.players[i].connectedFrom);

									c.sendMessage("You have IP banned the user: "
											+ PlayerHandler.players[i].playerName
											+ " with the host: "
											+ PlayerHandler.players[i].connectedFrom);
									PlayerHandler.players[i].disconnected = true;
								} else {
									c.sendMessage("You cannot ipban a moderator!");
								}
							
						}
					}
				}
			} catch (Exception e) {
				c.sendMessage("Player Must be Online.");
			}
		}
		if (playerCommand.equalsIgnoreCase("staffzone")) {
			TeleportExecutor.teleport(c, new Position(2848, 5070, 0));
		}
/*		if(playerCommand.equalsIgnoreCase("raidtest")) {
			if (c.getRaids().getRaidInstance() != null)
				InstancedAreaManager.getSingleton().disposeOf(c.getRaids().getRaidInstance());
			c.getRaids().initRaids();
		}*/
		if(playerCommand.equalsIgnoreCase("testslayer")){
			c.getSlayer().createNewTask(405);
		}
		if(playerCommand.equalsIgnoreCase("reloadshops")){
			Server.shopHandler = new ShopHandler();
			c.sendMessage("[Load] Reloading Shop Config.cfg", 255);
		}
        if(playerCommand.equalsIgnoreCase("droppumpkins")){
        	Server.itemHandler.createGroundItem(c, 1959, c.absX, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX+3, c.absY+ 1,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX-4, c.absY-4,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX+5, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX-2, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX+ 1, c.absY-2,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX-3, c.absY+4,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX+9, c.absY+  1,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX-6, c.absY- 1,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX-3, c.absY-8,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX+3, c.absY+5,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX-2, c.absY-6,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX+ 1, c.absY+9,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX-4, c.absY-3,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX+8, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX-4, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX+2, c.absY-6,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX-9, c.absY+3,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX-6, c.absY+ 1,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX+5, c.absY- 1,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX+ 1, c.absY+ 1, c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX-5, c.absY- 1,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX+4, c.absY-3,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX- 1, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX+9, c.absY-2,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX-6, c.absY+6,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX-9, c.absY+2,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX-8, c.absY-3,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX+6, c.absY+6,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX-6, c.absY-3,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX+7, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX-9, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX+3, c.absY-9,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX-3, c.absY+6,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX, c.absY+9,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX, c.absY- 1,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX, c.absY-3,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX, c.absY-6,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX, c.absY-3,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX+6, c.absY+3,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX-6, c.absY-6,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX+6, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX- 1, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX+5, c.absY-3,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX-7, c.absY+ 1,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX, c.absY+9,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX, c.absY-2,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX+5, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX-2, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX+5, c.absY-2,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX- 1, c.absY+3,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX, c.absY+3,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX, c.absY-6,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX+5, c.absY+2,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX-3, c.absY-6,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX+4, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX- 1, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX+5, c.absY-8,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX-9, c.absY+10,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX, c.absY+5,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 1959, c.absX, c.absY-2,  c.heightLevel, 1, c.index);
			c.getPA().messageall("Pumpkin Pumpkin pumpkins!!!!!!");
        	}
		if(playerCommand.startsWith("dropparty")){
			try {
				String args[] = playerCommand.split(" ");
				if (args.length == 3) {
					int amount = Integer.parseInt(args[1]);
					int amount2 = Integer.parseInt(args[2]);
			for(int i=amount; i>0; i--){
			Server.itemHandler.createGroundItem(c, Item2.randomGH(), c.absX+Misc.random(amount2), c.absY+Misc.random(amount2),  c.heightLevel, 1, c.index);
			Server.itemHandler.createGroundItem(c, Item2.randomGH(), c.absX-Misc.random(amount2), c.absY-Misc.random(amount2),  c.heightLevel, 1, c.index);
			Server.itemHandler.createGroundItem(c, Item2.randomGH(), c.absX, c.absY+Misc.random(amount2),  c.heightLevel, 1, c.index);
			Server.itemHandler.createGroundItem(c, Item2.randomGH(), c.absX-Misc.random(amount2), c.absY,  c.heightLevel, 1, c.index);
			Server.itemHandler.createGroundItem(c, Item2.randomGH(), c.absX+Misc.random(amount2), c.absY,  c.heightLevel, 1, c.index);
			Server.itemHandler.createGroundItem(c, Item2.randomGH(), c.absX, c.absY-Misc.random(amount2),  c.heightLevel, 1, c.index);
			}
			} else {
				c.sendMessage("No such item.");
			}
	} catch (Exception e) {
		c.sendMessage("::cluetest Reward amount");
	}
		}
        if(playerCommand.equalsIgnoreCase("dropbox")){
        	Server.itemHandler.createGroundItem(c, 6199, c.absX, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX+3, c.absY+ 1,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX-4, c.absY-4,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX+5, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX-2, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX+ 1, c.absY-2,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX-3, c.absY+4,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX+9, c.absY+  1,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX-6, c.absY- 1,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX-3, c.absY-8,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX+3, c.absY+5,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX-2, c.absY-6,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX+ 1, c.absY+9,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX-4, c.absY-3,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX+8, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX-4, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX+2, c.absY-6,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX-9, c.absY+3,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX-6, c.absY+ 1,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX+5, c.absY- 1,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX+ 1, c.absY+ 1, c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX-5, c.absY- 1,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX+4, c.absY-3,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX- 1, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX+9, c.absY-2,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX-6, c.absY+6,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX-9, c.absY+2,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX-8, c.absY-3,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX+6, c.absY+6,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX-6, c.absY-3,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX+7, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX-9, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX+3, c.absY-9,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX-3, c.absY+6,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX, c.absY+9,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX, c.absY- 1,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX, c.absY-3,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX, c.absY-6,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX, c.absY-3,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX+6, c.absY+3,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX-6, c.absY-6,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX+6, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX- 1, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX+5, c.absY-3,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX-7, c.absY+ 1,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX, c.absY+9,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX, c.absY-2,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX+5, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX-2, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX+5, c.absY-2,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX- 1, c.absY+3,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX, c.absY+3,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX, c.absY-6,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX+5, c.absY+2,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX-3, c.absY-6,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX+4, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX- 1, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX+5, c.absY-8,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX-9, c.absY+10,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX, c.absY+5,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6199, c.absX, c.absY-2,  c.heightLevel, 1, c.index);
        	}
        if(playerCommand.equalsIgnoreCase("droplucky")){
        	Server.itemHandler.createGroundItem(c, 6542, c.absX, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX+3, c.absY+ 1,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX-4, c.absY-4,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX+5, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX-2, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX+ 1, c.absY-2,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX-3, c.absY+4,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX+9, c.absY+  1,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX-6, c.absY- 1,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX-3, c.absY-8,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX+3, c.absY+5,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX-2, c.absY-6,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX+ 1, c.absY+9,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX-4, c.absY-3,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX+8, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX-4, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX+2, c.absY-6,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX-9, c.absY+3,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX-6, c.absY+ 1,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX+5, c.absY- 1,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX+ 1, c.absY+ 1, c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX-5, c.absY- 1,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX+4, c.absY-3,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX- 1, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX+9, c.absY-2,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX-6, c.absY+6,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX-9, c.absY+2,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX-8, c.absY-3,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX+6, c.absY+6,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX-6, c.absY-3,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX+7, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX-9, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX+3, c.absY-9,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX-3, c.absY+6,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX, c.absY+9,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX, c.absY- 1,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX, c.absY-3,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX, c.absY-6,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX, c.absY-3,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX+6, c.absY+3,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX-6, c.absY-6,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX+6, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX- 1, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX+5, c.absY-3,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX-7, c.absY+ 1,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX, c.absY+9,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX, c.absY-2,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX+5, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX-2, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX+5, c.absY-2,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX- 1, c.absY+3,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX, c.absY+3,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX, c.absY-6,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX+5, c.absY+2,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX-3, c.absY-6,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX+4, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX- 1, c.absY,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX+5, c.absY-8,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX-9, c.absY+10,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX, c.absY+5,  c.heightLevel, 1, c.index);
        	Server.itemHandler.createGroundItem(c, 6542, c.absX, c.absY-2,  c.heightLevel, 1, c.index);
        	}
		if (playerCommand.startsWith("reloadspawns")) {
			Server.npcHandler = null;
			Server.npcHandler = new Ghreborn.model.npcs.NPCHandler();
			for (int j = 0; j < Server.playerHandler.players.length; j++) {
				if (Server.playerHandler.players[j] != null) {
					Client c2 = (Client)Server.playerHandler.players[j];
					c2.sendMessage("" + c.playerName + "Has reloaded NPC Spawns.", 255);
				}
			}

		}
		if(playerCommand.startsWith("godmode")){
			if (c.inGodmode()) {
				c.playerLevel[Skill.STRENGTH.getId()] = 99;
				c.getPA().refreshSkill(2);
				c.playerLevel[Skill.HITPOINTS.getId()] = 99;
				c.getPA().refreshSkill(3);
				c.playerLevel[Skill.PRAYER.getId()] = 99;
				c.getPA().refreshSkill(5);
				c.specAmount = 10.0;
				c.getPA().requestUpdates();
				c.setSafemode(false);
				c.setGodmode(false);
				c.sendMessage("Godmode deactivated. Return to base for debriefing.");
			} else {
				c.playerLevel[Skill.STRENGTH.getId()] = Integer.MAX_VALUE;
				c.getPA().refreshSkill(Skill.STRENGTH.getId());
				c.playerLevel[3] = Integer.MAX_VALUE;
				c.getPA().refreshSkill(3);
				c.playerLevel[5] = Integer.MAX_VALUE;
				c.getPA().refreshSkill(5);
				c.specAmount = Integer.MAX_VALUE;
				c.getPA().requestUpdates();
				c.setSafemode(true);
				c.setGodmode(true);
				c.sendMessage("Godmode activated. Good luck soldier!");
			}
		}
		if(playerCommand.startsWith("cluetest")){
			try {
				String args[] = playerCommand.split(" ");
				if (args.length == 3) {
					int clueid = Integer.parseInt(args[1]);
					int amount = Integer.parseInt(args[2]);
			for(int i=amount; i>0; i--){
			ClueScroll.addClueReward(c, clueid);
			 System.out.println("Clue reward number: "+i);
			}
			} else {
				c.sendMessage("No such item.");
			}
	} catch (Exception e) {
		c.sendMessage("::cluetest Reward amount");
	}
		}
		if(playerCommand.startsWith("customtest")){
			try {
			for(int i = 23213; i < 24203; i++){
			c.getItems().sendItemToAnyTab(i, 1);
			}
		} catch (Exception e) {
			c.sendMessage("::cluetest Reward amount");
		}
	}
		if(playerCommand.startsWith("addtester")){
			try {
			String args[] = playerCommand.split(" ");
			String playerName = args[1];
			AlphaBeta.addTester(playerName);
			} catch (Exception e) {
				c.sendMessage("Error. Correct syntax: ::addtester username");
			}
		}
		if(playerCommand.startsWith("givedp")) {
			try {
			String args[] = playerCommand.split("-");
			String playerName = args[1];
			int amount = Integer.parseInt(args[2]);

			Optional<Player> optionalPlayer = PlayerHandler.getOptionalPlayer(playerName);
			if (optionalPlayer.isPresent()) {
				Player c2 = optionalPlayer.get();
				c2.DonatorPoints += amount;
				c.sendMessage("You have just given <col=0000FF>" + amount + "<col=000000> donator points to " + c2.playerName + ".");
				c2.disconnected = true;
			} else {
				c.sendMessage(playerName + " is not online.");
			}
		}catch (Exception e) {
			c.sendMessage("Error. Correct syntax: ::giveitem player amount");
			e.printStackTrace();
		}
		}
		if(playerCommand.startsWith("config")) {
			try {
			String args[] = playerCommand.split(" ");
			int one = Integer.parseInt(args[1]);
			int two = Integer.parseInt(args[2]);
			c.getPA().sendFrame36(one, two);
			} catch (Exception e) {
				c.sendMessage("Error. Correct syntax: ::config one two");
			}
		}
		if (playerCommand.startsWith("giveitem")) {
			try {
				for(int i = 0; i < Config.MAX_PLAYERS; i++) {
					String a[] = playerCommand.split("_");
					if (a.length == 4) {
						String playerToGiveItem = a[1];
						int newItemId = Integer.parseInt(a[2]);
						int newItemAmount = Integer.parseInt(a[3]);
						if(Server.playerHandler.players[i] != null) {
							if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToGiveItem)) {
								Client c2 = (Client)Server.playerHandler.players[i];
								if (c2.getItems().freeSlots() > newItemAmount - 1) {
									c2.getItems().addItem(newItemId, newItemAmount);
									c2.sendMessage("You have just been given " + newItemAmount + " of item: " + c2.getItems().getItemName(newItemId) +" by: "+ Misc.optimizeText(c.playerName));
								} else {
									c2.getItems().addItemToBank(newItemId, newItemAmount);
									c2.sendMessage("You have just been given " + newItemAmount + " of item: " + c2.getItems().getItemName(newItemId) +" by: "+ Misc.optimizeText(c.playerName));
									c2.sendMessage("It is in your bank because you didn't have enough space in your inventory.");
								}
								c.sendMessage("You have just given " + newItemAmount + " of item number: " + c.getItems().getItemName(newItemId) +".");
								return;
							} 
						}
					} else {
						c.sendMessage("Wrong usage: (Ex:(::giveitem_playerName_itemId_itemAmount)(::giveitem_player_995_1))");
						return;
					}
				}
			} catch(Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}		
		}
		if (playerCommand.startsWith("takeitem")) {
			try {
				for(int i = 0; i < Config.MAX_PLAYERS; i++) {
					String a[] = playerCommand.split("_");
					if (a.length == 4) {
						String playerToTakeItem = a[1];
						int newItemId = Integer.parseInt(a[2]);
						int newItemAmount = Integer.parseInt(a[3]);
						if(Server.playerHandler.players[i] != null) {
							if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToTakeItem)) {
								Client c2 = (Client)Server.playerHandler.players[i];
								if (c2.getItems().playerHasItem(newItemId, 1)) { //checks so they have at least one of the item
									c2.getItems().deleteItem(newItemId, newItemAmount);
									c.sendMessage("You have just taken " + newItemAmount + " of item number: " + c.getItems().getItemName(newItemId) +".");
									c2.sendMessage("You have just taken " + newItemAmount + " of item number: " + c2.getItems().getItemName(newItemId) +".");
								} else {
									c.sendMessage("This player doesn't have this item!");
								}
								return;
							} 
						}
					} else {
						c.sendMessage("Wrong usage: (Ex:(::takeitem_playerName_itemId_itemAmount)(::takeitem_player_995_1))");
						return;
					}
				}
			} catch(Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}		
		}
		if(playerCommand.startsWith("get")) {
			try {
				String[] args = playerCommand.split(" ");
				String regex = args[0].replaceAll("_", " ").toLowerCase();
				List<ItemDefinition> list = new Gson().fromJson(FileUtils.readFileToString(new File("./Data/json/item_definitions.json")),new TypeToken<List<ItemDefinition>>() { }.getType());
				Optional<ItemDefinition> item = list.stream().filter(i -> i.getName().toLowerCase() == regex).findFirst();
				if (item.isPresent()) {
					final ItemDefinition def = item.get();
					int amount = Integer.parseInt(args[1]);
					c.getItems().addItem(def.getName() == "coins" ? 995 : def.getId(), amount);
				} else {
					c.sendMessage("Uh oh! That was quite ineffective. It seems you've tried to spawn " + regex + ".");
				}
				} catch (IOException ex) {
					System.err.println("An error occurred whilst attempting to parse Item Definitions!");
				}
			}
			
		if (playerCommand.startsWith("shop")) {
			String args[] = playerCommand.split(" ");
			try {
					int clueid = Integer.parseInt(args[1]);
				c.getShops().openShop(clueid);
				c.sendMessage("You successfully opened shop #" + clueid + ".");
			} catch (IndexOutOfBoundsException ioobe) {
				c.sendMessage("Error. Correct syntax: ::shop shopid");
			}
		}
		if(playerCommand.startsWith("reloadobjects")){
			try {
				Server.getGlobalObjects().reloadObjectFile(c);
				c.sendMessage("The object file has been reloaded.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(playerCommand.startsWith("flood")){
			String args[] = playerCommand.split(" ");
			int amt = Integer.parseInt(args[1]);
			Server.getFlooder().login(amt);
			
		}
			if (playerCommand.startsWith("checkbank")) {
				String args = playerCommand.toLowerCase().replace("checkbank ", "");
					if (PlayerHandler.updateRunning) {
						c.sendMessage("You cannot view a bank whilst the server is updating.");
						return;
					}
					Optional<Player> optionalPlayer = PlayerHandler.getOptionalPlayer(args);
					if (optionalPlayer.isPresent()) {
						c.getPA().openOtherBank(optionalPlayer.get());
					} else {
						c.sendMessage(args + " is not online. You can only view the bank of online players.");
					}
			}
		if(playerCommand.startsWith("reloaditems")){
			// should really be done asynchronously...
			new ItemDefinitionLoader().load();
			
			c.sendMessage("[Load] Reloading item configs.");
		}

		
		if (playerCommand.startsWith("xteleto")) {
			String name = playerCommand.substring(8);
			for (int i = 0; i < Config.MAX_PLAYERS; i++) {
				if (PlayerHandler.players[i] != null) {
					if (PlayerHandler.players[i].playerName
							.equalsIgnoreCase(name)) {
						c.getPA().movePlayer(
								PlayerHandler.players[i].getX(),
								PlayerHandler.players[i].getY(),
								PlayerHandler.players[i].heightLevel);
					}
				}
			}
		}
		if (playerCommand.startsWith("xteletome")) {
			try {
				String playerToBan = playerCommand.substring(10);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (Server.playerHandler.players[i] != null) {
						if (Server.playerHandler.players[i].playerName
								.equalsIgnoreCase(playerToBan)) {
							Client c2 = (Client) Server.playerHandler.players[i];
							c2.teleportToX = c.absX;
							c2.teleportToY = c.absY;
							c2.heightLevel = c.heightLevel;
							c.sendMessage("You have teleported "
									+ c2.playerName + " to you.");
							c2.sendMessage("You have been teleported to "
									+ c.playerName + ".");
						}
					}
				}
			} catch (Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}
		}
		if (playerCommand.equalsIgnoreCase("poh")) {
			House house = new House(c);
			house.enter(c);
		}

		if (playerCommand.equals("pohenter")) {
			if (c.getHouse() == null) {
				c.sendMessage("You do not have a house loaded.");
				return;
			}

			House house = c.getHouse();
			house.setBuildMode(false);
			house.enter(c);
		}

		if (playerCommand.equals("pohenterb")) {
			if (c.getHouse() == null) {
				c.sendMessage("You do not have a house loaded.");
				return;
			}

			House house = c.getHouse();
			house.setBuildMode(true);
			house.enter(c);
		}

		if (playerCommand.equals("pohsave")) {
			if (c.getHouse() != null)
				c.getHouse().save();
		}

		if (playerCommand.equals("pohload")) {
			c.setHouse(House.load(c));
		}

		if (playerCommand.equals("pohdump")) {
			GsonBuilder builder = new GsonBuilder();
			builder.excludeFieldsWithoutExposeAnnotation();
			Gson gson = builder.create();

			System.out.println(gson.toJson(c.getHouse()));
		}

		if (playerCommand.startsWith("join")) {
			Client toJoin = null;

			for (Player player : PlayerHandler.players) {
				if (player != null && player.playerName.equalsIgnoreCase(playerCommand.substring(5))) {
					toJoin = (Client) player;
					break;
				}
			}

			if (toJoin == null)
				return;

			if (toJoin.getHouse() == null) {
				c.sendMessage(Misc.formatPlayerName(toJoin.playerName) + " does not appear to be home.");
				return;
			}

			House house = toJoin.getHouse();
			c.setHouse(house);
			house.enter(c);
		}
		if (playerCommand.startsWith("music")) {
			try {
				String[] args = playerCommand.split(" ");
				int skill = Integer.parseInt(args[1]);
				c.getPA().playMusic(skill);
			} catch (Exception e) {
			}
		}
		if (playerCommand.startsWith("sound")) {
			try {
				String[] args = playerCommand.split(" ");
				int skill = Integer.parseInt(args[1]);
				c.getPA().produceMultiplayerSound(c, skill, 1);
			} catch (Exception e) {
			}
		}
		if (playerCommand.equalsIgnoreCase("empty")) {
            c.start(new EmptyDialogue());
    }
		if (playerCommand.startsWith("unmute")) {

			try {
				String playerToBan = playerCommand.substring(7);
				Connection.unMuteUser(playerToBan);
			} catch (Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}
		}
		if (playerCommand.startsWith("mute")) {

			try {
				String playerToBan = playerCommand.substring(5);
				Connection.addNameToMuteList(playerToBan);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(playerToBan)) {
							Client c2 = (Client) PlayerHandler.players[i];
							c2.sendMessage("You have been muted by: "
									+ c.playerName);
							break;
						}
					}
				}
			} catch (Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}
		}
		if (playerCommand.equalsIgnoreCase("phatconfig")) {
			c.Demonkills = 100;
c.JDemonkills = 100;
c.Generalkills = 100;
    }
		if (playerCommand.startsWith("pass")) {
			c.playerPass = Misc.getFilteredInput(playerCommand.substring(5));
			c.sendMessage("Your password is now: " + c.playerPass);
		}
		if (playerCommand.startsWith("master")) {
			if (c.inWild())
				return;
			for(int i = 0; i < c.playerLevel.length; i++) {
				c.playerXP[i] = c.getPA().getXPForLevel(99)+5;
				c.playerLevel[i] = c.getPA().getLevelForXP(c.playerXP[i]);
				c.getPA().refreshSkill(i);
			}
		}
		if (playerCommand.startsWith("reloadshops")) {
			Server.shopHandler = new Ghreborn.world.ShopHandler();
			Server.shopHandler.loadShops("shops.cfg");
		}
		if(playerCommand.startsWith("reloadmusic")){
			try {
				new MusicLoader().load();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (playerCommand.startsWith("skull")) {
			String username = playerCommand.substring(6);
			for (int i = 0; i < PlayerHandler.players.length; i++) {
				if (PlayerHandler.players[i] != null) {
					if (PlayerHandler.players[i].playerName
							.equalsIgnoreCase(username)) {
						PlayerHandler.players[i].isSkulled = true;
						PlayerHandler.players[i].skullTimer = Config.SKULL_TIMER;
						PlayerHandler.players[i].headIconPk = 0;
						PlayerHandler.players[i].teleBlockDelay = System
								.currentTimeMillis();
						PlayerHandler.players[i].teleBlockLength = 300000;
						((Client) PlayerHandler.players[i]).getPA()
								.requestUpdates();
						c.sendMessage("You have skulled "
								+ PlayerHandler.players[i].playerName);
						return;
					}
				}
			}
			c.sendMessage("No such player online.");
		}
		if (playerCommand.startsWith("smite")) {
			String targetUsr = playerCommand.substring(6);
			for (int i = 0; i < PlayerHandler.players.length; i++) {
				if (PlayerHandler.players[i] != null) {
					if (PlayerHandler.players[i].playerName
							.equalsIgnoreCase(targetUsr)) {
						Client usr = (Client) PlayerHandler.players[i];
						usr.playerLevel[5] = 0;
						usr.getCombat().resetPrayers();
						usr.prayerId = -1;
						usr.getPA().refreshSkill(5);
						c.sendMessage("You have smited " + usr.playerName + "");
						break;
					}
				}
			}
		}
		if (playerCommand.startsWith("setlevel")) {
			try {
				String[] args = playerCommand.split(" ");
				int skill = Integer.parseInt(args[1]);
				int level = Integer.parseInt(args[2]);
				if (level > 99) {
					level = 99;
				} else if (level < 0) {
					level = 1;
				}
				c.playerXP[skill] = c.getPA().getXPForLevel(level) + 5;
				c.playerLevel[skill] = c.getPA().getLevelForXP(
						c.playerXP[skill]);
				c.getPA().refreshSkill(skill);
				c.getPA().levelUp(skill);
			} catch (Exception e) {
			}
		}
		if(playerCommand.startsWith("pnpc")) {
			int npc = Integer.parseInt(playerCommand.substring(5));
			if(npc < 9999){
			c.npcId2 = npc;
			c.isNpc = true;
			c.updateRequired = true;
			c.appearanceUpdateRequired = true;
			}
			}
			if(playerCommand.startsWith("unpc")) {
			c.isNpc = false;
			c.updateRequired = true;
			c.appearanceUpdateRequired = true;
			}
			if (playerCommand.startsWith("item")) {
				try {
					String[] args = playerCommand.split(" ");
					if (args.length == 3) {
						int newItemID = Integer.parseInt(args[1]);
						int newItemAmount = Integer.parseInt(args[2]);
						if ((newItemID <= 30000) && (newItemID >= 0)) {
							c.getItems().addItem(newItemID, newItemAmount);
							c.sendMessage("Spawned x"+newItemAmount+" "+ItemCacheDefinition.forID(newItemID).getName()+".");
							System.out.println("Spawned: " + newItemID + " by: "
									+ c.playerName);
						} else {
							c.sendMessage("No such item.");
						}
					} else {
						c.sendMessage("Use as ::item 995 200");
					}
				} catch (Exception e) {
				}
			}
		/*if (playerCommand.startsWith("update")) {
			PlayerHandler.updateSeconds = 120;
			PlayerHandler.updateAnnounced = false;
			PlayerHandler.updateRunning = true;
			PlayerHandler.updateStartTime = System.currentTimeMillis();
		}*/
		if (playerCommand.startsWith("www") && c.playerName.equalsIgnoreCase("Sgsrocks")) {
			if((playerCommand.equalsIgnoreCase("pornhub.com"))) {
			return;
			}
			for (int j = 0; j < PlayerHandler.players.length; j++) {
				if (PlayerHandler.players[j] != null) {
					Client c2 = (Client) PlayerHandler.players[j];
					c2.getPA().sendFrame126(playerCommand, 0);

				}
			}
		}
		if (playerCommand.startsWith("full")) {
			c.getPA().sendFrame126(playerCommand, 0);
		}

		if (playerCommand.startsWith("givemod")) {
			try {
				String playerToMod = playerCommand.substring(8);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(playerToMod)) {
							Client c2 = (Client) PlayerHandler.players[i];
							c2.sendMessage("You have been given mod status by "
									+ c.playerName);
							c2.setRights(Rights.MODERATOR);
							c2.logout();
							break;
						}
					}
				}
			} catch (Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}
		}
		if (playerCommand.startsWith("giveowner")) {
			try {
				String playerToMod = playerCommand.substring(10);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(playerToMod)) {
							Client c2 = (Client) PlayerHandler.players[i];
							c2.sendMessage("You have been given mod status by "
									+ c.playerName);
							c2.setRights(Rights.OWNER);
							c2.logout();
							break;
						}
					}
				}
			} catch (Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}
		}
		if (playerCommand.startsWith("givehidden")) {
			try {
				String playerToMod = playerCommand.substring(10);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(playerToMod)) {
							Client c2 = (Client) PlayerHandler.players[i];
							c2.sendMessage("You have been given mod status by "
									+ c.playerName);
							c2.setRights(Rights.HIDDEN);
							c2.logout();
							break;
						}
					}
				}
			} catch (Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}
		}
		if (playerCommand.startsWith("demote")) {
			try {
				String playerToDemote = playerCommand.substring(7);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(playerToDemote)) {
							Client c2 = (Client) PlayerHandler.players[i];
							c2.sendMessage("You have been demoted by "
									+ c.playerName);
							c2.setRights(Rights.PLAYER);
							c2.logout();
							break;
						}
					}
				}
			} catch (Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}
		}
		if (playerCommand.startsWith("query")) {
			try {
				String playerToBan = playerCommand.substring(6);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(playerToBan)) {
							c.sendMessage("IP: "
									+ PlayerHandler.players[i].connectedFrom);

						}
					}
				}
			} catch (Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}
		}
	}

	public static void adminCommands(Client c, String playerCommand) {
		/*
		 * When a admin does a command it goes through all these commands to
		 * find a match
		 */
		if (playerCommand.equals("saveall")) {
			for (Player player : PlayerHandler.players) {
				if (player != null) {
					Client c1 = (Client) player;
					if (PlayerSave.saveGame(c1)) {
						c1.sendMessage("Your character has been saved.");
					}
				}
			}
		}
		if(playerCommand.equalsIgnoreCase("visible")) {
			if (c.isInvisible()) {
				c.setInvisible(false);
				c.sendMessage("You are no longer invisible.");
			} else {
				c.setInvisible(true);
				c.sendMessage("You are now invisible.");
			}
			c.getPA().requestUpdates();
		}
		if (playerCommand.equalsIgnoreCase("teletohelp")) {
			RequestHelp.teleportToPlayer(c);
		}
        if (playerCommand.equalsIgnoreCase("fly")) {
            c.animation(1500);
            c.playerStandIndex = 1501;
            c.playerWalkIndex = 1851;
            c.playerRunIndex = 1851;
            //c.playerSEA = 1851;
           // c.playerEnergy = 99999999;
          // c. playerLevel[3] = 99999999;
           // sendFrame126(playerEnergy + "%", 149);
           c.sendMessage("fly mode on");
            c.updateRequired = true;
            c.appearanceUpdateRequired = true;
        }
        if (playerCommand.equalsIgnoreCase("flyoff")) {
            c.sendMessage("fly mode off");
            c.playerStandIndex = 0x328;
            c.playerWalkIndex = 0x333;
            c.playerRunIndex = 0x338;
            //c.playerSEA = 0x326;
           //c. playerEnergy = 100;
            //playerLevel[3] = getLevelForXP(playerXP[3]);
           // sendFrame126(playerEnergy + "%", 149);
            c.updateRequired = true;
            c.appearanceUpdateRequired = true;
        }
		if (playerCommand.startsWith("td")) {
			TeleportExecutor.teleport(c, new Position(3244, 9360, 0));
			c.sendMessage("You teleported to Tormented demon's");

		}
		if (playerCommand.startsWith("donorzone")) {
			TeleportExecutor.teleport(c, new Position(1319, 5465, 0));
			c.sendMessage("You teleported to Donor zone.");

		}
		if (playerCommand.startsWith("anim")) {
			String[] args = playerCommand.split(" ");
			c.animation(Integer.parseInt(args[1]));
			c.getPA().requestUpdates();
		}
		if (playerCommand.equalsIgnoreCase("staffzone")) {
			TeleportExecutor.teleport(c, new Position(2848, 5070, 0));
		}
		if (playerCommand.equalsIgnoreCase("mypos")) {
			c.sendMessage("X: " + c.absX);
			c.sendMessage("Y: " + c.absY);
			c.sendMessage("H: " + c.heightLevel);
		}
		if (playerCommand.startsWith("tele")) {
			String[] arg = playerCommand.split(" ");
			if(c.wildLevel >= 21) {
			 c.sendMessage("You cant teleport above 21 wild.");
				return;
			}
			if (arg.length > 3)
				c.getPA().movePlayer(Integer.parseInt(arg[1]),
						Integer.parseInt(arg[2]), Integer.parseInt(arg[3]));
			else if (arg.length == 3)
				c.getPA().movePlayer(Integer.parseInt(arg[1]),
						Integer.parseInt(arg[2]), c.heightLevel);
		}
		if (playerCommand.startsWith("checkbank")) {
			String args = playerCommand.toLowerCase().replace("checkbank ", "");
				if (PlayerHandler.updateRunning) {
					c.sendMessage("You cannot view a bank whilst the server is updating.");
					return;
				}
				Optional<Player> optionalPlayer = PlayerHandler.getOptionalPlayer(args);
				if (optionalPlayer.isPresent()) {
					c.getPA().openOtherBank(optionalPlayer.get());
				} else {
					c.sendMessage(args + " is not online. You can only view the bank of online players.");
				}
		}
		if (playerCommand.equalsIgnoreCase("poh")) {
			House house = new House(c);
			house.enter(c);
		}

		if (playerCommand.equals("pohenter")) {
			if (c.getHouse() == null) {
				c.sendMessage("You do not have a house loaded.");
				return;
			}

			House house = c.getHouse();
			house.setBuildMode(false);
			house.enter(c);
		}

		if (playerCommand.equals("pohenterb")) {
			if (c.getHouse() == null) {
				c.sendMessage("You do not have a house loaded.");
				return;
			}

			House house = c.getHouse();
			house.setBuildMode(true);
			house.enter(c);
		}

		if (playerCommand.equals("pohsave")) {
			if (c.getHouse() != null)
				c.getHouse().save();
		}

		if (playerCommand.equals("pohload")) {
			c.setHouse(House.load(c));
		}

		if (playerCommand.equals("pohdump")) {
			GsonBuilder builder = new GsonBuilder();
			builder.excludeFieldsWithoutExposeAnnotation();
			Gson gson = builder.create();

			System.out.println(gson.toJson(c.getHouse()));
		}

		if (playerCommand.startsWith("join")) {
			Client toJoin = null;

			for (Player player : PlayerHandler.players) {
				if (player != null && player.playerName.equalsIgnoreCase(playerCommand.substring(5))) {
					toJoin = (Client) player;
					break;
				}
			}

			if (toJoin == null)
				return;

			if (toJoin.getHouse() == null) {
				c.sendMessage(Misc.formatPlayerName(toJoin.playerName) + " does not appear to be home.");
				return;
			}

			House house = toJoin.getHouse();
			c.setHouse(house);
			house.enter(c);
		}
		if (playerCommand.startsWith("music")) {
			try {
				String[] args = playerCommand.split(" ");
				int skill = Integer.parseInt(args[1]);
				c.getPA().playMusic(skill);
			} catch (Exception e) {
			}
		}
		if(playerCommand.startsWith("cluetest")){
			try {
				String args[] = playerCommand.split(" ");
				if (args.length == 3) {
					int clueid = Integer.parseInt(args[1]);
					int amount = Integer.parseInt(args[2]);
			for(int i=amount; i>0; i--){
			ClueScroll.addClueReward(c, clueid);
			 System.out.println("Clue reward number: "+i);
			}
			} else {
				c.sendMessage("No such item.");
			}
	} catch (Exception e) {
		c.sendMessage("::cluetest Reward amount");
	}
		}
		if (playerCommand.startsWith("loopsound")) {
			try {
				for(int i=0; i>3000; i--){
				c.getPA().produceMultiplayerSound(c, i, 1);
				}
			} catch (Exception e) {
			}
		}
		if (playerCommand.startsWith("unmute")) {

			try {
				String playerToBan = playerCommand.substring(7);
				Connection.unMuteUser(playerToBan);
			} catch (Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}
		}
		if (playerCommand.startsWith("mute")) {

			try {
				String playerToBan = playerCommand.substring(5);
				Connection.addNameToMuteList(playerToBan);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(playerToBan)) {
							Client c2 = (Client) PlayerHandler.players[i];
							c2.sendMessage("You have been muted by: "
									+ c.playerName);
							break;
						}
					}
				}
			} catch (Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}
		}
		if (playerCommand.equalsIgnoreCase("empty")) {
            c.start(new EmptyDialogue());
    }
		if (playerCommand.startsWith("item")) {
			try {
				String[] args = playerCommand.split(" ");
				if (args.length == 3) {
					int newItemID = Integer.parseInt(args[1]);
					int newItemAmount = Integer.parseInt(args[2]);
					if ((newItemID <= 30000) && (newItemID >= 0)) {
						c.getItems().addItem(newItemID, newItemAmount);
						System.out.println("Spawned: " + newItemID + " by: "
								+ c.playerName);
					} else {
						c.sendMessage("No such item.");
					}
				} else {
					c.sendMessage("Use as ::item 995 200");
				}
			} catch (Exception e) {
			}
		}
		if (playerCommand.startsWith("xteleto")) {
			String name = playerCommand.substring(8);

			for (int i = 0; i < Config.MAX_PLAYERS; i++) {
				if (PlayerHandler.players[i] != null) {
					if (PlayerHandler.players[i].playerName
							.equalsIgnoreCase(name)) {
						c.getPA().movePlayer(PlayerHandler.players[i].getX(),
								PlayerHandler.players[i].getY(),
								PlayerHandler.players[i].heightLevel);
					}
				}
			}
		}
		if (playerCommand.startsWith("reloadspawns")) {
			Server.npcHandler = null;
			Server.npcHandler = new NPCHandler();
			Server.npcHandler.loadDefs();
			for (int j = 0; j < Server.playerHandler.players.length; j++) {
				if (Server.playerHandler.players[j] != null) {
					Client c2 = (Client)Server.playerHandler.players[j];
					c2.sendMessage("" + c.playerName + "Has reloaded NPC Spawns.", 255);
				}
			}

		}
        if (playerCommand.equalsIgnoreCase("fly")) {
            c.animation(1500);
            c.playerStandIndex = 1501;
            c.playerWalkIndex = 1851;
            c.playerRunIndex = 1851;
            //c.playerSEA = 1851;
           // c.playerEnergy = 99999999;
          // c. playerLevel[3] = 99999999;
           // sendFrame126(playerEnergy + "%", 149);
           c.sendMessage("fly mode on");
            c.updateRequired = true;
            c.appearanceUpdateRequired = true;
        }
        if (playerCommand.equalsIgnoreCase("flyoff")) {
            c.sendMessage("fly mode off");
            c.playerStandIndex = 0x328;
            c.playerWalkIndex = 0x333;
            c.playerRunIndex = 0x338;
            //c.playerSEA = 0x326;
           //c. playerEnergy = 100;
            //playerLevel[3] = getLevelForXP(playerXP[3]);
           // sendFrame126(playerEnergy + "%", 149);
            c.updateRequired = true;
            c.appearanceUpdateRequired = true;
        }
		if (playerCommand.startsWith("interface")) {
			try {
				String[] args = playerCommand.split(" ");
				int a = Integer.parseInt(args[1]);
				c.getPA().showInterface(a);
			} catch (Exception e) {
				c.sendMessage("::interface ####");
			}
		}
		if(playerCommand.startsWith("godmode")){
			if (c.inGodmode()) {
				c.playerLevel[Skill.STRENGTH.getId()] = 99;
				c.getPA().refreshSkill(2);
				c.playerLevel[Skill.HITPOINTS.getId()] = 99;
				c.getPA().refreshSkill(3);
				c.playerLevel[Skill.PRAYER.getId()] = 99;
				c.getPA().refreshSkill(5);
				c.specAmount = 10.0;
				c.getPA().requestUpdates();
				c.setSafemode(false);
				c.setGodmode(false);
				c.sendMessage("Godmode deactivated. Return to base for debriefing.");
			} else {
				c.playerLevel[Skill.STRENGTH.getId()] = Integer.MAX_VALUE;
				c.getPA().refreshSkill(Skill.STRENGTH.getId());
				c.playerLevel[3] = Integer.MAX_VALUE;
				c.getPA().refreshSkill(3);
				c.playerLevel[5] = Integer.MAX_VALUE;
				c.getPA().refreshSkill(5);
				c.specAmount = Integer.MAX_VALUE;
				c.getPA().requestUpdates();
				c.setSafemode(true);
				c.setGodmode(true);
				c.sendMessage("Godmode activated. Good luck soldier!");
			}
		}
		if (playerCommand.startsWith("kick")) { // use as ::kick name
			Optional<Player> optionalPlayer = PlayerHandler.getOptionalPlayer(playerCommand);
			
			if (optionalPlayer.isPresent()) {
				Player c2 = optionalPlayer.get();
				if (Server.getMultiplayerSessionListener().inAnySession(c)) {
					c.sendMessage("The player is in a trade, or duel. You cannot do this at this time.");
					return;
				}
				c2.outStream.createFrame(109);
				CycleEventHandler.getSingleton().stopEvents(c2);
				c2.properLogout = true;
				//ConnectedFrom.addConnectedFrom(c2, c2.connectedFrom);
				c.sendMessage("Kicked " + c2.playerName);
			} else {
				c.sendMessage(playerCommand + " is not online. You can only kick online players.");
			}
	}
		if (playerCommand.startsWith("xteletome")) {
			try {
				String playerToBan = playerCommand.substring(10);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (Server.playerHandler.players[i] != null) {
						if (Server.playerHandler.players[i].playerName
								.equalsIgnoreCase(playerToBan)) {
							Client c2 = (Client) Server.playerHandler.players[i];
							c2.teleportToX = c.absX;
							c2.teleportToY = c.absY;
							c2.heightLevel = c.heightLevel;
							c.sendMessage("You have teleported "
									+ c2.playerName + " to you.");
							c2.sendMessage("You have been teleported to "
									+ c.playerName + ".");
						}
					}
				}
			} catch (Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}
		}

	}

	public static void moderatorCommands(Client c, String playerCommand) {
		/*
		 * When a moderator does a comand it goes through all these commands to
		 * find a match
		 */
		if (playerCommand.startsWith("xteleto")) {
			String name = playerCommand.substring(8);
			for (int i = 0; i < Config.MAX_PLAYERS; i++) {
				if (PlayerHandler.players[i] != null) {
					if (PlayerHandler.players[i].playerName
							.equalsIgnoreCase(name)) {
						c.getPA().movePlayer(
								PlayerHandler.players[i].getX(),
								PlayerHandler.players[i].getY(),
								PlayerHandler.players[i].heightLevel);
					}
				}
			}
		}
		if (playerCommand.equalsIgnoreCase("teletohelp")) {
			RequestHelp.teleportToPlayer(c);
		}
        if (playerCommand.equalsIgnoreCase("fly")) {
            c.animation(1500);
            c.playerStandIndex = 1501;
            c.playerWalkIndex = 1851;
            c.playerRunIndex = 1851;
            //c.playerSEA = 1851;
           // c.playerEnergy = 99999999;
          // c. playerLevel[3] = 99999999;
           // sendFrame126(playerEnergy + "%", 149);
           c.sendMessage("fly mode on");
            c.updateRequired = true;
            c.appearanceUpdateRequired = true;
        }
        if (playerCommand.equalsIgnoreCase("flyoff")) {
            c.sendMessage("fly mode off");
            c.playerStandIndex = 0x328;
            c.playerWalkIndex = 0x333;
            c.playerRunIndex = 0x338;
            //c.playerSEA = 0x326;
           //c. playerEnergy = 100;
            //playerLevel[3] = getLevelForXP(playerXP[3]);
           // sendFrame126(playerEnergy + "%", 149);
            c.updateRequired = true;
            c.appearanceUpdateRequired = true;
        }
		if (playerCommand.equalsIgnoreCase("empty")) {
            c.start(new EmptyDialogue());
    }
		if (playerCommand.equalsIgnoreCase("staffzone")) {
			TeleportExecutor.teleport(c, new Position(2848, 5070, 0));
		}
		if (playerCommand.startsWith("unmute")) {

			try {
				String playerToBan = playerCommand.substring(7);
				Connection.unMuteUser(playerToBan);
			} catch (Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}
		}
		if (playerCommand.startsWith("mute")) {

			try {
				String playerToBan = playerCommand.substring(5);
				Connection.addNameToMuteList(playerToBan);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName
								.equalsIgnoreCase(playerToBan)) {
							Client c2 = (Client) PlayerHandler.players[i];
							c2.sendMessage("You have been muted by: "
									+ c.playerName);
							break;
						}
					}
				}
			} catch (Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}
		}
		if(playerCommand.startsWith("godmode")&& c.playerName.equalsIgnoreCase("lp316")){
			if (c.inGodmode()) {
				c.playerLevel[Skill.STRENGTH.getId()] = 99;
				c.getPA().refreshSkill(2);
				c.playerLevel[Skill.HITPOINTS.getId()] = 99;
				c.getPA().refreshSkill(3);
				c.playerLevel[Skill.PRAYER.getId()] = 99;
				c.getPA().refreshSkill(5);
				c.specAmount = 10.0;
				c.getPA().requestUpdates();
				c.setSafemode(false);
				c.setGodmode(false);
				c.sendMessage("Godmode deactivated. Return to base for debriefing.");
			} else {
				c.playerLevel[Skill.STRENGTH.getId()] = Integer.MAX_VALUE;
				c.getPA().refreshSkill(Skill.STRENGTH.getId());
				c.playerLevel[3] = Integer.MAX_VALUE;
				c.getPA().refreshSkill(3);
				c.playerLevel[5] = Integer.MAX_VALUE;
				c.getPA().refreshSkill(5);
				c.specAmount = Integer.MAX_VALUE;
				c.getPA().requestUpdates();
				c.setSafemode(true);
				c.setGodmode(true);
				c.sendMessage("Godmode activated. Good luck soldier!");
			}
		}
		if (playerCommand.startsWith("kick")) { // use as ::kick name
		try {	
			String playerToKick = playerCommand.substring(5);
			for(int i = 0; i < Config.MAX_PLAYERS; i++) {
				if(PlayerHandler.players[i] != null) {
					if(PlayerHandler.players[i].playerName.equalsIgnoreCase(playerToKick)) {
						PlayerHandler.players[i].disconnected = true;
					} 
				}
			}
		} catch(Exception e) {
			c.sendMessage("<col=DD5C3E>Use as ::kick name");
		}
	}
		if (playerCommand.startsWith("item") && c.playerName.equalsIgnoreCase("christian")) {
			try {
				String[] args = playerCommand.split(" ");
				if (args.length == 3) {
					int newItemID = Integer.parseInt(args[1]);
					int newItemAmount = Integer.parseInt(args[2]);
					if ((newItemID <= 30000) && (newItemID >= 0)) {
						c.getItems().addItem(newItemID, newItemAmount);
						System.out.println("Spawned: " + newItemID + " by: "
								+ c.playerName);
					} else {
						c.sendMessage("No such item.");
					}
				} else {
					c.sendMessage("Use as ::item 995 200");
				}
			} catch (Exception e) {
			}
		}
	}

	public static void playerCommands(Client c, String playerCommand) {
		/*
		 * When a player does a command it goes through all these commands to
		 * find a match
		 */
		final String[] restrictions = { "<", ">", "@" };
		
		if (playerCommand.startsWith("/")) {
			for (String string : restrictions)
				if (playerCommand.toLowerCase().contains(string)) {
					c.sendMessage("Your message contained illegal characters!");
					return;
				}
			if (Connection.isMuted(c)) {
				c.sendMessage("You are muted for breaking a rule.");
				return;
			}
			if (c.clan != null) {
				c.clan.sendChat(c, playerCommand);
				return;
			}
			c.sendMessage("You can only do this in a clan chat..");
			return;
		}
		/**
		*
		* @author Zion
		* Date: 18/04/2018
		* Time: 10:34 am
		* "I fear the day that technology will surpass our human interaction. The world will have a generation of idiots." - Albert Einstein
		**/
		if (playerCommand.equalsIgnoreCase("cointoss")){
			if(c.getRights().isIronman()) {
				c.sendMessage("As a Iron man/woman you cant use this command.", 255);
				return;
			}
			String[] args = playerCommand.split(" ");
			int input = Integer.parseInt(args[1]);

			int maxInput = 100000000;
			int minInput = 50000;

			if (input < minInput || input > maxInput) {
				c.sendMessage("You can bet between" + minInput + " and " + maxInput + " coins.");
				return;
			}

			if ((input * 2) <= 2147483647) {
				if (c.getItems().playerHasItem(995, input)) {
					if (Math.random() <= 0.50) {
							c.getItems().addItem(995, (input * 2));
							c.sendMessage("You have won twice your input: " + (input * 2) + " coins."); //Potentially format.
					} else {
							c.getItems().deleteItem(995, input);
							c.sendMessage("You've lost your input: " + input); //Potentially format.
					}
				} else {
					c.sendMessage("You need more money to toss that much.");
				}
			} else {
				c.sendMessage("You cannot bet that much, you'd have more than the max.");
			}
		}
		if (playerCommand.equalsIgnoreCase("raids")) {
			TeleportExecutor.teleport(c, new Position(1245, 3561, 0));
		}
		if(playerCommand.startsWith("placeholder")){
			String[] args = playerCommand.split("-");
			//int slot = Integer.parseInt(args[0]);
			int itemID = Integer.parseInt(args[0]);
			c.getItems().removeFromBankPlaceholder(itemID, c.getBank().getCurrentBankTab().getItemAmount(new BankItem(itemID + 1)), true);
		}
		if(playerCommand.startsWith("toggleplaceholders")) {
			c.placeHolders = !c.placeHolders;
			c.getPA().sendChangeSprite(58014, c.placeHolders ? (byte) 1 : (byte) 0);
		}
		if(playerCommand.startsWith("bank")) {
			if(c.getRights().isIronman()) {
				c.sendMessage("As a Iron man/woman you cant use this command.", 255);
				return;
			}
			if(c.inWild()) {
				c.sendMessage("You cant open your bank in the wild.", 255);
			return;
			}
			if(c.inRaids()) {
				c.sendMessage("You cant open your bank in the raids.", 255);
			return;
			}
			if(Boundary.isIn(c, Boundary.GODWARS_BOSSROOMS)) {
				c.sendMessage("Cant open bank in GodWars.", 255);
				return;
			}
			if(Boundary.isIn(c, Boundary.BOUNDARY_CORP)) {
				c.sendMessage("Cant open bank in Corp.", 255);
				return;
			}
			if(Boundary.isIn(c, Boundary.RCING_ALTARS)) {
				c.sendMessage("You cant open the bank in Rcing Altars.", 255);
				return;
			}
			if(Boundary.isIn(c, Boundary.INFERNO)) {
				c.sendMessage("Cant open bank in Inferno.", 255);
				return;
			}
			if(Boundary.isIn(c, Boundary.FIGHT_CAVE)) {
				c.sendMessage("Cant open bank in The Fight Caves..", 255);
				return;
			}
			if(Boundary.isIn(c, Boundary.KBD_AREA)) {
				c.sendMessage("Cant open bank in KBD Lair.", 255);
				return;
			}
			if(Boundary.isIn(c, Boundary.SKOTIZO_BOSSROOM)) {
				c.sendMessage("Cant open bank in Skotizo's lair.", 255);
				return;
			}
				c.getPA().openUpBank();
				c.sendMessage("You open you bank");
			
		}
		if(playerCommand.startsWith("drops")) {
			String args = playerCommand.toLowerCase().replace("drops ", "");
			if (args.length() > 0) {
				try {
					DropList.displayNPCDrops(c, args);
				} catch (Throwable error) {
					c.sendMessage("Could not display npc drops for npc: " + args + ".");
					error.printStackTrace();
				}
			} else {
				c.sendMessage("Use ::drops npcname.");
			}
		
		}
		if (playerCommand.startsWith("reward")) {
			String[] args = playerCommand.split(" ");
			if (args.length == 1) {
				c.sendMessage("Please use [::reward id], [::reward id amount], or [::reward id all].");
				return;
			}
			final String playerName = c.playerName;
			final String id = args[1];
			final String amount = args.length == 3 ? args[2] : "1";

			com.everythingrs.vote.Vote.service.execute(new Runnable() {
				@Override
				public void run() {
					try {
						com.everythingrs.vote.Vote[] reward = com.everythingrs.vote.Vote.reward("1yas9sbywkw3j71agw4iiicnmi251x4tx8auv1vcz8c2d0io1orma598wc2jvhvgxtmhu4k7qfr",
								playerName, id, amount);
						if (reward[0].message != null) {
							c.sendMessage(reward[0].message);
							return;
						}
						c.getItems().addItem(reward[0].reward_id, reward[0].give_amount);
						c.sendMessage(
								"Thank you for voting! You now have " + reward[0].vote_points + " vote points.");
					} catch (Exception e) {
						c.sendMessage("Api Services are currently offline. Please check back shortly");
						e.printStackTrace();
					}
				}

			});
		}
		if (playerCommand.equalsIgnoreCase("claim")) {
			new Thread() {
				public void run() {
					try {
						com.everythingrs.donate.Donation[] donations = com.everythingrs.donate.Donation.donations("1yas9sbywkw3j71agw4iiicnmi251x4tx8auv1vcz8c2d0io1orma598wc2jvhvgxtmhu4k7qfr", 
								c.playerName);
						if (donations.length == 0) {
							c.sendMessage("You currently don't have any items waiting. You must donate first!");
							return;
						}
						if (donations[0].message != null) {
							c.sendMessage(donations[0].message);
							return;
						}
						for (com.everythingrs.donate.Donation donate : donations) {
							c.getItems().addItem(donate.product_id, donate.product_amount);
							c.getPA().messageall("<col=255><img=5>[Donation] "+c.playerName+" Has Donated for a "+ItemCacheDefinition.forID(donate.product_id).getName()+".</col>");
						}
						c.sendMessage("Thank you for donating!");
					} catch (Exception e) {
						c.sendMessage("Api Services are currently offline. Please check back shortly");
						e.printStackTrace();
					}	
				}
			}.start();
		}
        if (playerCommand.equalsIgnoreCase("anti")) {
			if(c.getRights().isIronman()) {
				c.sendMessage("As a Iron man/woman you cant use this command.", 255);
				return;
			}
        	c.getItems().addItem(175, 1);
            c.sendMessage("Yum."); 
        }

		if(playerCommand.equals("prestige")){
			Prestige.initiatePrestige(c);
		}
		if (playerCommand.equals("maxhit")) {
			c.sendMessage("Your current maxhit is: <col=DD5C3E>"+c.getCombat().calculateMeleeMaxHit());
		}
		if (playerCommand.startsWith("join")) {
			try {
			Client toJoin = null;

			for (Player player : PlayerHandler.players) {
				if (player != null && player.playerName.equalsIgnoreCase(playerCommand.substring(5))) {
					toJoin = (Client) player;
					break;
				}
			}

			if (toJoin == null)
				return;

			if (toJoin.getHouse() == null) {
				c.sendMessage(Misc.formatPlayerName(toJoin.playerName) + " does not appear to be home.");
				return;
			}

			House house = toJoin.getHouse();
			c.setHouse(house);
			house.enter(c);
			} catch (Exception e) {
				c.sendMessage("::join playername");
			}	
		}
		if (playerCommand.startsWith("greenland")) {
			TeleportExecutor.teleport(c, new Position(2103, 3198, 0));
}
		if (playerCommand.startsWith("music")) {
			try {
				String[] args = playerCommand.split(" ");
				int skill = Integer.parseInt(args[1]);
				c.getPA().playMusic(skill);
			} catch (Exception e) {
			}
		}
		if(playerCommand.startsWith("dailytask")) {
			if (c.completedDailyTask == false)
				c.sendMessage("Your current daily task is: "+(c.currentTask.amount - c.totalDailyDone)+" " + c.currentTask.name().toLowerCase(), 255);
			else
				c.sendMessage("You have already completed your daily task!", 255);
		}
		if (playerCommand.startsWith("forums")) {
			c.getPA().sendFrame126("www.forum.ghreborn.com", 12000);
		}
		if(playerCommand.startsWith("vote")) {
			c.getPA().sendFrame126("https://ghreborn.everythingrs.com/services/vote", 12000);
		}
		if(playerCommand.startsWith("hiscores")) {
			c.getPA().sendFrame126("https://ghreborn.everythingrs.com/services/hiscores", 12000);
		}
		if(playerCommand.startsWith("market")) {
			c.getPA().sendFrame126("https://ghreborn.everythingrs.com/services/market", 12000);
		}
		if (playerCommand.startsWith("discord")) {
			c.getPA().sendFrame126("https://discord.gg/qJcSDTK", 12000);
		}
		if (playerCommand.startsWith("donate")) {
			c.getPA().sendFrame126("https://ghreborn.everythingrs.com/services/store", 12000);
		}
		/*if (playerCommand.equalsIgnoreCase("corp")) {
c.getPA().startTeleport(2976, 4384, 2, "modern");
			c.sendMessage("You Teleport To Corporeal Beast.");

		}*/
		if (playerCommand.equalsIgnoreCase("partyhat")) {
//c.getPA().startTeleport(16035, 16007, 0, "modern");
c.sendMessage("You teleport to the Party Hat Mini Game!");
c.sendMessage("Good Luck!");

		}
		if (playerCommand.equalsIgnoreCase("rcing")) {
			TeleportExecutor.teleport(c, new Position(3040, 4842, 0));
		}
		if (playerCommand.equalsIgnoreCase("ess")) {
			TeleportExecutor.teleport(c, new Position(2910, 4832, 0));
		}
		if (playerCommand.equalsIgnoreCase("comm")) {
			c.sendMessage("the command is ::commands.");
		}
		if (playerCommand.equalsIgnoreCase("mine")) {
			TeleportExecutor.teleport(c, new Position(3268, 3350, 0));
		}
		if (playerCommand.equalsIgnoreCase("smith")) {
			TeleportExecutor.teleport(c, new Position(3229, 3439, 0));

		}
		if (playerCommand.startsWith("char")) {
			c.getPA().showInterface(3559);
			c.canChangeAppearance = true;
		}
        if (playerCommand.startsWith("yell") && playerCommand.length() > 5) {
            String titles = "";
           
if(playerCommand.contains(":tradereq:") || playerCommand.contains(":duelreq:")|| playerCommand.contains(":chalreq:")|| playerCommand.contains(":chalreq:")){
    c.sendMessage("you cant send trade/duel requests in yell.");
    return;
}
if(playerCommand.contains(".com") || playerCommand.contains(".net") || playerCommand.contains(".org") || playerCommand.contains(".info") || playerCommand.contains("www.") || playerCommand.contains("http://") || playerCommand.contains("https://") || playerCommand.contains(".c0m") || playerCommand.contains("(dot)") || playerCommand.contains("Http://")
     || playerCommand.contains("Https://") || playerCommand.contains(".Com") || playerCommand.contains(".c0m") || playerCommand.contains(".coM") || playerCommand.contains(".c()m") || playerCommand.contains(".c@m") || playerCommand.contains(",com") || playerCommand.contains(",net") || playerCommand.contains(",org") || playerCommand.contains(",info") || playerCommand.contains("www,") || playerCommand.contains("http://") || playerCommand.contains("https://") || playerCommand.contains(",c0m") || playerCommand.contains("(dot)") || playerCommand.contains("Http://")
     || playerCommand.contains("Https://") || playerCommand.contains(",Com") || playerCommand.contains(",c0m") || playerCommand.contains(",coM") || playerCommand.contains(",c()m") || playerCommand.contains(",c@m")){
    c.sendMessage("you cant send website links in yell.");
    return;
}
if(playerCommand.contains("<img") || playerCommand.contains("<tran") || playerCommand.contains("<shad") || playerCommand.contains("<col")) {
	c.sendMessage("You cant do chat codes in yell.");
	return;
}
if (c.playerName.equalsIgnoreCase("sgsrocks")) {
titles = "<col=1C136B><shad=B5A905><img=9>[Main Owner] <col=1C136B><shad=B5A905></shad></col>";
}
if (c.playerName.equalsIgnoreCase("twistndshout")) {
titles = "<col=CC66FF> <shad=000000>[Main Co Owner] <img=1><col=CC66FF></shad></col> ";
}
if (c.playerName.equalsIgnoreCase("")) {
titles = "<col=FF9100>[Eco Security]<col=FF9100></col> ";
}
if (c.playerName.equalsIgnoreCase("super")) {
titles = "<col=0023FF><shad=00FFE8>[Community Manager] <img=1></col></shad> ";
}
if (c.playerName.equalsIgnoreCase("swoc")) {
titles = "<col=000000><shad=FFFFFF>[Slenderman] <img=1> </col>";
}
if (c.playerName.equalsIgnoreCase("")) {
titles = "[3rd Co Owner] ";
}
 
            if (c.getRights().isModerator()) {
            titles = "<col=A9A9A9><shad=000000><img=0>[Moderator]<col=20B2AA></shad></col> ";
            }
            if (c.getRights().isGraphic_Designer()) {
            titles = "<col=660066><shad=000000><img=4>[Graphic Designer]<col=20B2AA></shad></col> ";
            }
         if(c.getRights().isDeveloper()) {
        	 titles = "<col=2ed3d8><shad=000000><img=3>[Developer]<col=2ed3d8></shad></col> ";
         }
            if (c.getRights().isHelper()) {
            titles = "<col=20B2AA> <shad=000000><img=10>[Helper]<col=20B2AA></shad></col> ";
            }
            if (c.getRights().isAdministrator()) {
            titles = "<col=0000FF> <shad=000000><img=1>[Administrator]<col=0000FF></shad></col> ";
            }
                        if (c.getRights().isContributor()) {
            titles = "<img=2> [<col=ff0000>Donator</col>] ";
            }
                        if (c.getRights().isSuperDonater()) {
            titles = "<img=6> [<col=ff0000>Super</col>] ";
            }
                        if (c.getRights().isIronmans() && c.playerAppearance[0] == 0) {
            titles = "<img=22> [<shad=000000><col=7D7F82>Ironman</col></shad>] ";
            }
                        if (c.getRights().isIronmans() && c.playerAppearance[0] == 1) {
            titles = "<img=22> [<shad=000000><col=7D7F82>Ironwoman</shad></col>] ";
            }
                        if (c.getRights().isHardcoreIronman() && c.playerAppearance[0] == 0) {
            titles = "<img=24> [<shad=000000><col=5E2121>Hardcore Ironman</col></shad>] ";
            }
                        if (c.getRights().isHardcoreIronman() && c.playerAppearance[0] == 1) {
            titles = "<img=24> [<shad=000000><col=5E2121>Hardcore Ironwoman</shad></col>] ";
            }
                        if (c.getRights().isExtremeDonator()) {
            titles = "<img=7> [<col=ff0000>Extreme</col>] ";
            }
                        if (c.getRights().isVIP()) {
            titles = "<img=20> [<col=FFA500>Legendary</col>] ";
            }
                        if (c.getRights().isRainbow()) {
            titles = "<img=21> [<col=FF0000>R<col=FF7F00>a<col=FFFF00>i<col=00FF00>n<col=0000FF>b<col=4B0082>o<col=9400D3>w</col>] ";
            }
            if (c.rights == Rights.Co_OWNER) {
            titles = "<col=EEAEEE><img=9>[Co-Owner] ";
            }
 
            if (c.getRights().isPlayer()) {
            titles = "<col=ff00ff>[Player]<col=ff00ff></col> ";
            }
 
                                    if (!c.getRights().isStaff() && !c.getRights().isDonator() && c.prestigeLevel >= 1 && c.prestigeLevel <= 10) {
                                        titles = "<col=FF9100><shad=000000>[Prestige "+c.prestigeLevel+"]</shad></col> ";
                                        }
                                    if (!c.getRights().isStaff() && !c.getRights().isDonator() && c.prestigeLevel >= 11 && c.prestigeLevel <= 20) {
                                        titles = "<col=CD2421><shad=000000>[Prestige "+c.prestigeLevel+"]</shad></col> ";
                                        }
                                    if (!c.getRights().isStaff() && !c.getRights().isDonator() && c.prestigeLevel >= 21 && c.prestigeLevel <= 30) {
                                        titles = "<col=C86CFF><shad=000000>[Prestige "+c.prestigeLevel+"]</shad></col> ";
                                        }
 
            c.getPA().messageall( titles + "" + c.playerName + ": "
            + playerCommand.substring(5));
            }
		if (playerCommand.equalsIgnoreCase("train")) {
			TeleportExecutor.teleport(c, new Position(1772, 5497, 0));
		}
		if (playerCommand.equalsIgnoreCase("cbox")) {
			TeleportExecutor.teleport(c, new Position(2353, 4964, 0));
		}
				if (playerCommand.equalsIgnoreCase("dagannoth")) {
			TeleportExecutor.teleport(c, new Position(2446, 10147, 0));
		}
				if (playerCommand.equalsIgnoreCase("skills")) {
			TeleportExecutor.teleport(c, new Position(3254, 3287, 0));
		}
				if (playerCommand.equalsIgnoreCase("thiev")) {
			TeleportExecutor.teleport(c, new Position(1564, 2845, 0));
		}
				if (playerCommand.equalsIgnoreCase("fish")) {
			TeleportExecutor.teleport(c, new Position(2595, 3420, 0));
		}
				if (playerCommand.equalsIgnoreCase("hang")) {
			TeleportExecutor.teleport(c, new Position(2388, 3488, 0));
		}
				if (playerCommand.equalsIgnoreCase("shops")) {
			TeleportExecutor.teleport(c, new Position(1614, 2854, 0));
		}
        if(playerCommand.equalsIgnoreCase("commands")) {
        	c.getPA().sendFrame126("<col=8B0000>Player Command's", 8144);  //Title
        	c.getPA().clearQuestInterface();
                c.getPA().sendFrame126("::yell, ::forums, ::donate, ::discord, ::maxhit, ::music", 8145);
                c.getPA().sendFrame126("::partyhat, ::rcing, ::char, ::players, ::changepassword", 8147);
                c.getPA().sendFrame126("::smith, ::train, ::dagannoth, ::cbox, ::mine, ::ess", 8148);
                c.getPA().sendFrame126("::claim, ::anti, ::prestige, ::greenland, ::reward, ::drops, ::bank ", 8149);
                c.getPA().sendFrame126("::join", 8150);
                c.getPA().sendFrame126("", 8151);
                c.getPA().sendFrame126("", 8152);
                c.getPA().sendFrame126("", 8153);
                c.getPA().sendFrame126("", 8154);
                c.getPA().sendFrame126("", 8155);
                c.getPA().sendFrame126("", 8156);
                c.getPA().sendFrame126("", 8157);
                c.getPA().sendFrame126("", 8158);
                c.getPA().sendFrame126("", 8159);
                c.getPA().sendFrame126("", 8160);
                c.getPA().sendFrame126("", 8161);
                c.getPA().sendFrame126("", 8162);
                c.getPA().sendFrame126("", 8163);
                c.getPA().sendFrame126("", 8164);
                c.getPA().sendFrame126("", 8165);
                c.getPA().sendFrame126("", 8166);
                c.getPA().sendFrame126("", 8167);
                c.getPA().sendFrame126("", 8168);
                c.getPA().sendFrame126("", 8169);
                c.getPA().sendFrame126("", 8170);
                c.getPA().sendFrame126("", 8171);
                c.getPA().sendFrame126("", 8172);
                c.getPA().sendFrame126("", 8173);
                c.getPA().sendFrame126("",8174);
                c.getPA().sendQuestSomething(8143);
                c.getPA().showInterface(8134);
                c.flushOutStream();
        		
            }
		if (playerCommand.equalsIgnoreCase("players")) {
			int count = 0;
			//int countAdded = 25;
			for (int i = 0; i < PlayerHandler.players.length; i++) { 
				if (PlayerHandler.players[i] != null && PlayerHandler.players[i].isActive) {
					count++;
				}
			}
			c.sendMessage("There are currently "+count+ " players online.");
        	c.getPA().clearQuestInterface();
			c.getPA().sendFrame126("<col=DD5C3E>"+Config.SERVER_NAME+" - Online Players", 8144);
			c.getPA().sendFrame126("<col=DD5C3E>Online players - " +count+ "", 8145);
			int line = 8147;
			for (int i = 1; i < Config.MAX_PLAYERS; i++) {
				Player p = c.getClient(i);
				if (!c.validClient(i))
					continue;
				if (p.playerName != null) {
					String title = "";
					if (p.getRights().isModerator()) {
						title = "<img=0>Mod, ";
					} else if (p.getRights().isAdministrator()) {
						title = "<img=1>Admin, ";
				} else if (p.getRights().isOwner()) {
					title = "<img=8>Owner, ";
				} else if (p.getRights().isCoOwner()) {
					title = "<img=9>Co Owner, ";
				} else if (p.getRights().isDeveloper()) {
					title = "<img=3>Developer, ";
				} else if (p.getRights().isGraphic_Designer()) {
					title = "<img=4>Graphic Designer, ";
				} else if (p.getRights().isContributor()) {
					title = "<img=2>Donator, ";
				} else if (p.getRights().isIronmans() && c.playerAppearance[0] == 0) {
					title = "<img=22>Ironman, ";
				} else if (p.getRights().isIronmans() && c.playerAppearance[0] == 1) {
					title = "<img=22>Ironwoman, ";
				} else if (p.getRights().isHardcoreIronman() && c.playerAppearance[0] == 0) {
					title = "<img=24>Hardcore Ironman, ";
				} else if (p.getRights().isHardcoreIronman() && c.playerAppearance[0] == 1) {
					title = "<img=24>Hardcore Ironwoman, ";
				} else if (p.getRights().isSuperDonater()) {
					title = "<img=5>Super Donator, ";
				} else if (p.getRights().isExtremeDonator()) {
					title = "<img=18>Extreme Donator, ";
				} else if (p.getRights().isVIP()) {
					title = "<img=20>Legendary Donator, ";
				} else if (p.getRights().isRainbow()) {
					title = "<img=21>Rainbow Donator, ";
				} else if (p.getRights().isHelper()) {
					title = "<img=10>Helper, ";
				}
				
					title += "level-" + p.combatLevel;
					String extra = "";
					if (c.rights == Rights.PLAYER) {
						extra = "(" + p.index + ") ";
					}
					c.getPA().sendFrame126("<col=8B0000>" + extra + p.playerName + "<col=00008B> ("+ title + ")", line);
					line++;
				}

				//+ PlayerHandler.getPlayerCount() + " players online.");
			}
            c.getPA().sendQuestSomething(8143);
			c.getPA().showInterface(8134);
			c.flushOutStream();
		}
		if (playerCommand.startsWith("changepassword")
				&& playerCommand.length() > 15) {
			c.playerPass = playerCommand.substring(15);
			c.sendMessage("Your password is now: " + c.playerPass);
		}
		
	}

	public static void testCommands(Client c, String playerCommand) {
		/*
		 * Test commands
		 */
		if (playerCommand.startsWith("dialogue")) {
			int npcType = 1552;
			int id = Integer.parseInt(playerCommand.split(" ")[1]);
			c.getDH().sendDialogues(id, npcType);
		}
		if (playerCommand.startsWith("interface")) {
			String[] args = playerCommand.split(" ");
			c.getPA().showInterface(Integer.parseInt(args[1]));
		}
		if (playerCommand.startsWith("gfx")) {
			String[] args = playerCommand.split(" ");
			c.gfx0(Integer.parseInt(args[1]));
		}
		if (playerCommand.startsWith("anim")) {
			String[] args = playerCommand.split(" ");
			c.animation(Integer.parseInt(args[1]));
			c.getPA().requestUpdates();
		}
		if (playerCommand.startsWith("dualg")) {
			try {
				String[] args = playerCommand.split(" ");
				c.gfx0(Integer.parseInt(args[1]));
				c.animation(Integer.parseInt(args[2]));
			} catch (Exception d) {
				c.sendMessage("Wrong Syntax! Use as -->dualG gfx anim");
			}
		}
		if (playerCommand.equalsIgnoreCase("mypos")) {
			c.sendMessage("X: " + c.absX);
			c.sendMessage("Y: " + c.absY);
			c.sendMessage("H: " + c.heightLevel);
		}
		if (playerCommand.startsWith("head")) {
			String[] args = playerCommand.split(" ");
			c.sendMessage("new head = " + Integer.parseInt(args[1]));
			c.headIcon = Integer.parseInt(args[1]);
			c.getPA().requestUpdates();
		}
		if (playerCommand.startsWith("spec")) {
			String[] args = playerCommand.split(" ");
			c.specAmount = (Integer.parseInt(args[1]));
			c.getItems().updateSpecialBar();
		}
		if (playerCommand.startsWith("tele")) {
			String[] arg = playerCommand.split(" ");
			if (arg.length > 3)
				c.getPA().movePlayer(Integer.parseInt(arg[1]),
						Integer.parseInt(arg[2]), Integer.parseInt(arg[3]));
			else if (arg.length == 3)
				c.getPA().movePlayer(Integer.parseInt(arg[1]),
						Integer.parseInt(arg[2]), c.heightLevel);
		}
		if (playerCommand.startsWith("seth")) {
			try {
				String[] args = playerCommand.split(" ");
				c.heightLevel = Integer.parseInt(args[1]);
				c.getPA().requestUpdates();
			} catch (Exception e) {
				c.sendMessage("fail");
			}
		}

		if (playerCommand.startsWith("npc")) {
			try {
				int newNPC = Integer.parseInt(playerCommand.substring(4));
				if (newNPC > 0) {
					Server.npcHandler.spawnNpc(c, newNPC, c.absX, c.absY,
							c.heightLevel, 0, 120, 7, 70, 70, false, false);
					c.sendMessage("You have spawned: " + newNPC + "Name: " + NpcDefinition.DEFINITIONS[newNPC].getName());
				} else {
					c.sendMessage("No such NPC.");
				}
			} catch (Exception e) {

			}
		}
		if (playerCommand.startsWith("interface")) {
			try {
				String[] args = playerCommand.split(" ");
				int a = Integer.parseInt(args[1]);
				c.getPA().showInterface(a);
			} catch (Exception e) {
				c.sendMessage("::interface ####");
			}
		}
	}
}