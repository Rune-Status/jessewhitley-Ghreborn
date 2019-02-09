package Ghreborn.model.players;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import Ghreborn.Config;
import Ghreborn.Connection;
import Ghreborn.Server;
import Ghreborn.clip.PathChecker;
import Ghreborn.core.PlayerHandler;
import Ghreborn.event.CycleEvent;
import Ghreborn.event.CycleEventContainer;
import Ghreborn.event.CycleEventHandler;
import Ghreborn.event.Task;
import Ghreborn.model.content.clan.Clan;
import Ghreborn.model.content.dialogue.impl.StarterDialogue;
import Ghreborn.model.items.GameItem;
import Ghreborn.model.items.Item;
import Ghreborn.model.items.Item2;
import Ghreborn.model.items.ItemDefinition;
import Ghreborn.model.items.bank.BankTab;
import Ghreborn.model.npcs.NPC;
import Ghreborn.model.npcs.NPCHandler;
import Ghreborn.model.npcs.PetHandler;
import Ghreborn.model.npcs.boss.Armadyl.Armadyl;
import Ghreborn.model.npcs.boss.Bandos.Bandos;
import Ghreborn.model.npcs.boss.Cerberus.Cerberus;
import Ghreborn.model.npcs.boss.Kalphite.Kalphite;
import Ghreborn.model.npcs.boss.Kraken.Kraken;
import Ghreborn.model.npcs.boss.Saradomin.Saradomin;
import Ghreborn.model.npcs.boss.Zamorak.Zamorak;
import Ghreborn.model.npcs.boss.abyssalsire.AbyssalSireConstants;
import Ghreborn.model.npcs.boss.instances.InstancedArea;
import Ghreborn.model.npcs.boss.instances.InstancedAreaManager;
import Ghreborn.model.npcs.boss.zulrah.Zulrah;
import Ghreborn.model.players.combat.Degrade;
import Ghreborn.model.players.combat.Degrade.DegradableItem;
import Ghreborn.model.players.combat.magic.MagicData;
import Ghreborn.model.players.skills.Smelting;
import Ghreborn.model.players.skills.crafting.Enchantment;
import Ghreborn.model.sounds.Sound;
import Ghreborn.util.Misc;
import Ghreborn.util.Stream;;

public class PlayerAssistant {

	public int[] VERY_RARE_ITEMS = { 6585, 12002, 11286, 13233, 11838, 11785, 11824, 11791, 11828, 11826, 11830, 11810,
			11812, 11834, 11832, 12823, 12827, 12932, 12932, };
	public int[] EXTREMELY_RARE_ITEMS = { 12819, 11908, 12004, 13576, 11828, 11826, 11830, 11812, 11834, 11832,
			12819, };
	public int[] RARE_ITEMS = { 12807, 12806, 12007, 11907, 12927, 12922, 11822, 11820, 11818, 11816, 11824, 11812,
			6739, 6733, 6731, 6737, 6735, 11920, 12605, 11286, 12603, 10887, 4151, 6568, 6524, 6528, 11235, 7158, 10581,
			13092, 13233, 13231, 13229, 13227, 3140, 8901, 11840, 12002, 4087, 4585, 3204, 11335, 11838, 11990, };

	private Client c;

	public PlayerAssistant(Client Client) {
		this.c = Client;
	}

	public int CraftInt, Dcolor, FletchInt;

	/**
	 * MulitCombat icon
	 * 
	 * @param i1
	 *            0 = off 1 = on
	 */
	public void multiWay(int i1) {
		c.outStream.createFrame(61);
		c.outStream.writeByte(i1);
		c.updateRequired = true;
		c.setAppearanceUpdateRequired(true);
	}
	public boolean checkDisplayName(String name) {
	try {
		File list = new File("./Data/displaynames.txt");
		FileReader read = new FileReader(list);
		BufferedReader reader = new BufferedReader(read);
		String line = null;
			while ((line = reader.readLine()) != null) {
				if (line.equalsIgnoreCase(name)) {
				return true;
				}
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	

	
	public void createDisplayName(String name) {
		BufferedWriter names = null;
		try {
			names = new BufferedWriter(new FileWriter("./Data/displaynames.txt", true));
			names.write(name);
			names.newLine();
			names.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (names != null) {
				try {
					names.close();
				} catch (IOException e2) {
				}
			}
		}
	}

public boolean playerNameExists(String name) {
	try {
	File names = new File("./Data/characters/"+name+".txt");
		if (names.exists()) {
		return true;
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * Sends a custom sound
	 * @param id - the ID of the sound
	 * @param loop - how many times to loop sound
	 * @param dist - the distance from source in tiles
	 */
	public void sendSound(int id, int loop, int dist) {
		try {
			c.outStream.createFrameVarSize(25);
			c.outStream.writeByte(id);
			c.outStream.writeByte(loop);
			c.outStream.writeByte(dist);
			c.outStream.writeWord(1);
			c.updateRequired = true;
			c.appearanceUpdateRequired = true;
			c.outStream.endFrameVarSize();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	/**
	 * Produces a sound that is audible to all players within range
	 * @param emitter - The client emitting a sound
	 * @param loop - not finished yet, just keep as 1
	 */
	public static void produceMultiplayerSound(Client emitter, int id, int loop) {
		final int emitterSourceX = emitter.absX; final int emitterSourceY = emitter.absY;
		for (int j = 0; j < Server.playerHandler.players.length; j++) {
			if (Server.playerHandler.players[j] != null) {
				Client p = (Client)Server.playerHandler.players[j];
				if (p.absX >= emitterSourceX - 10 && p.absX <= emitterSourceX + 10 && p.absY >= emitterSourceY - 10 && p.absY <= emitterSourceY + 10) {
					int n = (int) Math.sqrt((p.absX - emitterSourceX)*(p.absX - emitterSourceX) + (p.absY - emitterSourceY)*(p.absY - emitterSourceY)); 
					p.getPA().sendSound(id, loop , n);
					//p.sendMessage("Sending sound "+id+", amp "+n);
				} 
			}
		}
	}
	/**
	 * Changes the main displaying sprite on an interface. The index represents the location of the new sprite in the index of the sprite array.
	 * 
	 * @param componentId the interface
	 * @param index the index in the array
	 */
	public void sendChangeSprite(int componentId, byte index) {
		if (c == null || c.getOutStream() == null) {
			return;
		}
		Stream stream = c.getOutStream();
		stream.createFrame(7);
		stream.writeDWord(componentId);
		stream.writeByte(index);
		c.flushOutStream();
	}
    public void sendInterfaceTextColor(int color, int i1) // colour changing on interface :O!
    {
    	if(c.outStream != null)
        c.getOutStream().createFrame(122);
        c.getOutStream().writeWordBigEndianA(i1); // interface
        c.getOutStream().writeWordBigEndianA(color); // colour stuff
        c.flushOutStream();
        //  sendMessage("Frame 122 tested");
    }
    public void sendQuestSomething(int id) {
		if(c.outStream != null)
        c.getOutStream().createFrame(79);
        c.getOutStream().writeWordBigEndian(id);
        c.getOutStream().writeWordA(0);
        c.flushOutStream();
    }
    public void newWelc() {
        infodia("Official server of ghreborn.com", "Type ::help and ::commands.", "<col=0000FF>Newest Update: Redid Bank system. with Tabs!.", "Support the server", "Welcome to Godzhell reborn");
    }
    public void clearQuestInterface() {
        for (int x = 0; x < QuestInterface.length; x++) {
            sendFrame126("", QuestInterface[x]);
        }
    }
    public int[] QuestInterface = {
            8145, 8147, 8148, 8149, 8150, 8151, 8152, 8153, 8154, 8155, 8156, 8157,
            8158, 8159, 8160, 8161, 8162, 8163, 8164, 8165, 8166, 8167, 8168, 8169,
            8170, 8171, 8172, 8173, 8174, 8175, 8176, 8177, 8178, 8179, 8180, 8181,
            8182, 8183, 8184, 8185, 8186, 8187, 8188, 8189, 8190, 8191, 8192, 8193,
            8194, 8195, 12174, 12175, 12176, 12177, 12178, 12179, 12180, 12181,
            12182, 12183, 12184, 12185, 12186, 12187, 12188, 12189, 12190, 12191,
            12192, 12193, 12194, 12195, 12196, 12197, 12198, 12199, 12200, 12201,
            12202, 12203, 12204, 12205, 12206, 12207, 12208, 12209, 12210, 12211,
            12212, 12213, 12214, 12215, 12216, 12217, 12218, 12219, 12220, 12221,
            12222, 12223
        };
    		public void loadQuests() {
    			sendFrame126("<col=FF7F00>Days:</col> <col=ffffff>" + c.daysPlayed + "</col><col=FF7F00> Hrs:</col> <col=ffffff>" + c.hoursPlayed
    					+ "</col><col=FF7F00> Mins:</col> <col=ffffff>" + c.minutesPlayed + "</col>", 29162);
    			sendFrame126("<col=FF7F00>Players:</col> <col=3CB71E>"+ Server.playerHandler.getPlayersOnline()+"", 29163);
    			sendFrame126("<col=3CB71E>Staff List",
    					29164);
    			sendFrame126("<col=FF7F00>Player Information:",
    					29165);
    			sendFrame126("<col=FF7F00>Prestige Points:</col> <col=ffffff>"+c.prestigePoints,
    					29166);
    			sendFrame126("<col=FF7F00>Donator Points:</col> <col=ffffff>"+c.DonatorPoints,
    					29167);
    			sendFrame126("<col=FF7F00>Vote Points:</col> <col=ffffff>"+c.votePoints,
    					29168);
    			if(c.getSlayer().getTask().isPresent()) {
    			sendFrame126("<col=FF7F00>Slayer Monster:</col> <col=ffffff>"+c.getSlayer().getTask().get().getPrimaryName()+".",
    					29169);
    			} else if(!c.getSlayer().getTask().isPresent()) {
	    			sendFrame126("<col=FF7F00>Slayer Monster:</col> <col=ffffff> None.",
	    					29169);
				}
    			sendFrame126("<col=FF7F00>Task Amount:</col> <col=ffffff>"+c.getSlayer().getTaskAmount()+".",
    					29170);
    			sendFrame126("<col=ffffff>NPC Statistics:</col> <col=FF7F00>[</col><col=ffffff>Click Here</col><col=FF7F00>]", 29177);
    		}

	public void infodia(String text, String text2, String text3, String text4, String title){//by Grey
        sendFrame126(title, 6180);
        sendFrame126(text, 6181);
        sendFrame126(text2, 6182);
        sendFrame126(text3, 6183);
        sendFrame126(text4, 6184);        
        sendChatInterface(6179);
}
	public void walkTo5(int i, int j) {
		c.newWalkCmdSteps = 0;
		if (++c.newWalkCmdSteps > 50)
			c.newWalkCmdSteps = 0;
		int k = c.getX() + i;
		k -= c.mapRegionX * 8;
		c.getNewWalkCmdX()[0] = c.getNewWalkCmdY()[0] = 0;
		int l = c.getY() + j;
		l -= c.mapRegionY * 8;

		for (int n = 0; n < c.newWalkCmdSteps; n++) {
			c.getNewWalkCmdX()[n] += k;
			c.getNewWalkCmdY()[n] += l;
		}
	}

	/*
	 * Vengeance
	 */
	public void castVeng() {
		if (c.playerLevel[6] < 94) {
			c.sendMessage("You need a magic level of 94 to cast this spell.");
			return;
		}
		if (c.playerLevel[1] < 40) {
			c.sendMessage("You need a defence level of 40 to cast this spell.");
			return;
		}
		if (!c.getItems().playerHasItem(9075, 4)
				|| !c.getItems().playerHasItem(557, 10)
				|| !c.getItems().playerHasItem(560, 2)) {
			c.sendMessage("You don't have the required runes to cast this spell.");
			return;
		}
		if (System.currentTimeMillis() - c.lastCast < 30000) {
			c.sendMessage("You can only cast vengeance every 30 seconds.");
			return;
		}
		if (c.vengOn) {
			c.sendMessage("You already have vengeance casted.");
			return;
		}
		c.animation(4410);
		c.gfx100(726);// Just use c.gfx100
		c.getItems().deleteItem2(9075, 4);
		c.getItems().deleteItem2(557, 10);// For these you need to change to
											// deleteItem(item, itemslot,
											// amount);.
		c.getItems().deleteItem2(560, 2);
		addSkillXP(112, 6);
		refreshSkill(6);
		c.vengOn = true;
		c.lastCast = System.currentTimeMillis();
	}


	public void resetAutocast() {
		c.autocastId = 0;
		c.autocasting = false;
		c.getPA().sendFrame36(108, 0);
	}

	public void sendFrame126(String s, int id) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSizeWord(126);
			c.getOutStream().writeString(s);
			c.getOutStream().writeWordA(id);
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}
	}

	public void sendLink(String s) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSizeWord(187);
			c.getOutStream().writeString(s);
		}
	}

	public void setSkillLevel(int skillNum, int currentLevel, int XP) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(134);
			c.getOutStream().writeByte(skillNum);
			c.getOutStream().writeDWord_v1(XP);
			c.getOutStream().writeByte(currentLevel);
			c.flushOutStream();
		}
	}


	public void sendFrame106(int sideIcon) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(106);
			c.getOutStream().writeByteC(sideIcon);
			c.flushOutStream();
			requestUpdates();
		}
	}

	public void sendFrame107() {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(107);
			c.flushOutStream();
		}
	}

	public void sendFrame36(int id, int state) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(36);
			c.getOutStream().writeWordBigEndian(id);
			c.getOutStream().writeByte(state);
			c.flushOutStream();
		}
	}
	public void sendFrame185(int Frame) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(185);
			c.getOutStream().writeWordBigEndianA(Frame);
		}
	}

	public void showInterface(int interfaceid) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(97);
			c.getOutStream().writeWord(interfaceid);
			c.flushOutStream();
		}
	}

	public void sendFrame248(int MainFrame, int SubFrame) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(248);
			c.getOutStream().writeWordA(MainFrame);
			c.getOutStream().writeWord(SubFrame);
			c.flushOutStream();
		}
	}

	public void sendFrame246(int MainFrame, int SubFrame, int SubFrame2) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(246);
			c.getOutStream().writeWordBigEndian(MainFrame);
			c.getOutStream().writeWord(SubFrame);
			c.getOutStream().writeWord(SubFrame2);
			c.flushOutStream();
		}
	}

	public void sendFrame171(int MainFrame, int SubFrame) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(171);
			c.getOutStream().writeByte(MainFrame);
			c.getOutStream().writeWord(SubFrame);
			c.flushOutStream();
		}
	}

	public void sendFrame200(int MainFrame, int SubFrame) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(200);
			c.getOutStream().writeWord(MainFrame);
			c.getOutStream().writeWord(SubFrame);
			c.flushOutStream();
		}
	}

	public void sendFrame70(int i, int o, int id) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(70);
			c.getOutStream().writeWord(i);
			c.getOutStream().writeWordBigEndian(o);
			c.getOutStream().writeWordBigEndian(id);
			c.flushOutStream();
		}
	}

	public void sendFrame75(int MainFrame, int SubFrame) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(75);
			c.getOutStream().writeWordBigEndianA(MainFrame);
			c.getOutStream().writeWordBigEndianA(SubFrame);
			c.flushOutStream();
		}
	}
	public static void stopSkilling(Client c) {
		if (c.smeltAmount > 0)
			Smelting.resetSmelting(c);
	}
	public void sendChatInterface(int Frame) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(164);
			c.getOutStream().writeWordBigEndian_dup(Frame);
			c.flushOutStream();
		}
	}

	public void setPrivateMessaging(int i) { // friends and ignore list status
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(221);
			c.getOutStream().writeByte(i);
			c.flushOutStream();
		}
	}

	public void setChatOptions(int publicChat, int privateChat, int tradeBlock) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(206);
			c.getOutStream().writeByte(publicChat);
			c.getOutStream().writeByte(privateChat);
			c.getOutStream().writeByte(tradeBlock);
			c.flushOutStream();
		}
	}

	public void sendFrame87(int id, int state) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(87);
			c.getOutStream().writeWordBigEndian_dup(id);
			c.getOutStream().writeDWord_v1(state);
			c.flushOutStream();
		}
	}

	public void sendPM(long name, int rights, byte[] chatMessage) {
		c.getOutStream().createFrameVarSize(196);
		c.getOutStream().writeQWord(name);
		c.getOutStream().writeDWord(new Random().nextInt());
		c.getOutStream().writeByte(rights);
		c.getOutStream().writeBytes(chatMessage, chatMessage.length, 0);
		c.getOutStream().endFrameVarSize();

	}

	public void createPlayerHints(int type, int id) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(254);
			c.getOutStream().writeByte(type);
			c.getOutStream().writeWord(id);
			c.getOutStream().write3Byte(0);
			c.flushOutStream();
		}
	}

	public void createObjectHints(int x, int y, int height, int pos) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(254);
			c.getOutStream().writeByte(pos);
			c.getOutStream().writeWord(x);
			c.getOutStream().writeWord(y);
			c.getOutStream().writeByte(height);
			c.flushOutStream();
		}
	}

	public void loadPM(long playerName, int world) {
		if (c.getOutStream() != null && c != null) {
			if (world != 0) {
				world += 9;
			} else if (!Config.WORLD_LIST_FIX) {
				world += 1;
			}
			c.getOutStream().createFrame(50);
			c.getOutStream().writeQWord(playerName);
			c.getOutStream().writeByte(world);
			c.flushOutStream();
		}
	}

	public void removeAllWindows() {
		if (c.getOutStream() != null && c != null) {
			c.getPA().resetVariables();
			c.getOutStream().createFrame(219);
			c.flushOutStream();
		}
	}

	public void closeAllWindows() {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(219);
			c.flushOutStream();
		}
	}

	public void sendFrame34(int id, int slot, int column, int amount) {
		if (c.getOutStream() != null && c != null) {
			c.outStream.createFrameVarSizeWord(34); // init item to smith
													// screen
			c.outStream.writeWord(column); // Column Across Smith Screen
			c.outStream.writeByte(4); // Total Rows?
			c.outStream.writeDWord(slot); // Row Down The Smith Screen
			c.outStream.writeWord(id + 1); // item
			c.outStream.writeByte(amount); // how many there are?
			c.outStream.endFrameVarSizeWord();
		}
	}

	public void walkableInterface(int id) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(208);
			c.getOutStream().writeWordBigEndian_dup(id);
			c.flushOutStream();
		}
	}

	public int mapStatus = 0;

	public void sendFrame99(int state) { // used for disabling map
		if (c.getOutStream() != null && c != null) {
			if (mapStatus != state) {
				mapStatus = state;
				c.getOutStream().createFrame(99);
				c.getOutStream().writeByte(state);
				c.flushOutStream();
			}
		}
	}

	public void sendCrashFrame() { // used for crashing cheat clients
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(123);
			c.flushOutStream();
		}
	}

	/**
	 * Reseting animations for everyone
	 **/

	public void frame1() {
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			if (PlayerHandler.players[i] != null) {
				Client person = (Client) PlayerHandler.players[i];
				if (person != null) {
					if (person.getOutStream() != null && !person.disconnected) {
						if (c.distanceToPoint(person.getX(), person.getY()) <= 25) {
							person.getOutStream().createFrame(1);
							person.flushOutStream();
							person.getPA().requestUpdates();
						}
					}
				}
			}
		}
	}

	/**
	 * Creating projectile
	 **/
	public void createProjectile(int x, int y, int offX, int offY, int angle, int speed, int gfxMoving, int startHeight,
			int endHeight, int lockon, int time) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC((y - (c.getMapRegionY() * 8)) - 2);
			c.getOutStream().writeByteC((x - (c.getMapRegionX() * 8)) - 3);
			c.getOutStream().createFrame(117);
			c.getOutStream().writeByte(angle);
			c.getOutStream().writeByte(offY);
			c.getOutStream().writeByte(offX);
			c.getOutStream().writeWord(lockon);
			c.getOutStream().writeWord(gfxMoving);
			c.getOutStream().writeByte(startHeight);
			c.getOutStream().writeByte(endHeight);
			c.getOutStream().writeWord(time);
			c.getOutStream().writeWord(speed);
			c.getOutStream().writeByte(16);
			c.getOutStream().writeByte(64);
			c.flushOutStream();

		}
	}

	public void createProjectile2(int x, int y, int offX, int offY, int angle,
			int speed, int gfxMoving, int startHeight, int endHeight,
			int lockon, int time, int slope) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC((y - (c.getMapRegionY() * 8)) - 2);
			c.getOutStream().writeByteC((x - (c.getMapRegionX() * 8)) - 3);
			c.getOutStream().createFrame(117);
			c.getOutStream().writeByte(angle);
			c.getOutStream().writeByte(offY);
			c.getOutStream().writeByte(offX);
			c.getOutStream().writeWord(lockon);
			c.getOutStream().writeWord(gfxMoving);
			c.getOutStream().writeByte(startHeight);
			c.getOutStream().writeByte(endHeight);
			c.getOutStream().writeWord(time);
			c.getOutStream().writeWord(speed);
			c.getOutStream().writeByte(slope);
			c.getOutStream().writeByte(64);
			c.flushOutStream();
		}
	}

	// projectiles for everyone within 25 squares
	public void createPlayersProjectile(int x, int y, int offX, int offY, int angle, int speed, int gfxMoving,
			int startHeight, int endHeight, int lockon, int time) {
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			Client p = (Client) PlayerHandler.players[i];
			if (p != null) {
				Client person = p;
				if (person.getOutStream() != null) {
					if (person.distanceToPoint(x, y) <= 25) {
						if (p.heightLevel == c.heightLevel)
							person.getPA().createProjectile(x, y, offX, offY, angle, speed, gfxMoving, startHeight,
									endHeight, lockon, time);
					}
				}
			}
		}
	}

	public void createPlayersProjectile2(int x, int y, int offX, int offY, int angle, int speed, int gfxMoving,
			int startHeight, int endHeight, int lockon, int time, int slope) {
		// synchronized(c) {
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			Player p = PlayerHandler.players[i];
			if (p != null) {
				if (p.getOutStream() != null) {
					if (p.distanceToPoint(x, y) <= 25 && c.heightLevel == p.heightLevel) {
						p.getPA().createProjectile2(x, y, offX, offY, angle, speed, gfxMoving, startHeight, endHeight,
								lockon, time, slope);
					}
				}
			}

		}
	}

	/**
	 ** GFX
	 **/
	public void stillGfx(int id, int x, int y, int height, int time) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC(y - (c.getMapRegionY() * 8));
			c.getOutStream().writeByteC(x - (c.getMapRegionX() * 8));
			c.getOutStream().createFrame(4);
			c.getOutStream().writeByte(0);
			c.getOutStream().writeWord(id);
			c.getOutStream().writeByte(height);
			c.getOutStream().writeWord(time);
			c.flushOutStream();
		}
	}
	public void stillgfx2(int id, int Y, int X)
	{
	if (c.getOutStream() != null && c != null) {
	c.getOutStream().createFrame(85);
	c.getOutStream().writeByteC(Y - (c.mapRegionY * 8));
	c.getOutStream().writeByteC(X - (c.mapRegionX * 8));
	c.getOutStream().createFrame(4);
	c.getOutStream().writeByte(0);//Tiles away (X >> 4 + Y & 7)
	c.getOutStream().writeWord(id);//Graphic id
	c.getOutStream().writeByte(80);//height of the spell above it's basic place, i think it's written in pixels 100 pixels higher
	c.getOutStream().writeWord(0);//Time before casting the graphic
	c.flushOutStream();
	}
	}
	// creates gfx for everyone
	public void createPlayersStillGfx(int id, int x, int y, int height, int time) {
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			Player p = PlayerHandler.players[i];
			if (p != null) {
				Client person = (Client) p;
				if (person != null) {
					if (person.getOutStream() != null) {
						if (person.distanceToPoint(x, y) <= 25) {
							person.getPA().stillGfx(id, x, y, height, time);
						}
					}
				}
			}
		}
	}

	/**
	 * Objects, add and remove
	 **/
	public void object(int objectId, int objectX, int objectY, int face,
			int objectType) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC(objectY - (c.getMapRegionY() * 8));
			c.getOutStream().writeByteC(objectX - (c.getMapRegionX() * 8));
			c.getOutStream().createFrame(101);
			c.getOutStream().writeByteC((objectType << 2) + (face & 3));
			c.getOutStream().writeByte(0);

			if (objectId != -1) { // removing
				c.getOutStream().createFrame(151);
				c.getOutStream().writeByteS(0);
				c.getOutStream().writeWordBigEndian(objectId);
				c.getOutStream().writeByteS((objectType << 2) + (face & 3));
			}
			c.flushOutStream();
		}
	}
	public void useGloryCharge() {
		int[][] glory = {{11978, 11976, 5}, {11976, 1712, 4}, {1712, 1710, 3}, {1710, 1708, 2}, {1708, 1706, 1}, {1706, 1704, 1}};
		for (int i = 0; i < glory.length; i++) {
			if (c.itemUsing == glory[i][0]) {
				if (c.isOperate) {
					c.playerEquipment[c.playerAmulet] = glory[i][1];
				} else {
					c.getItems().deleteItem(glory[i][0], 1);
					c.getItems().addItem(glory[i][1], 1);
				}
				if (glory[i][2] > 1) {
					c.sendMessage("Your amulet has "+glory[i][2]+" charges left.");
				} else {
					c.sendMessage("Your amulet has "+glory[i][2]+" charge left.");
				}
			}
		}
		if(c.isOperate){
		c.getItems().updateSlot(c.playerAmulet);
		}
		c.isOperate = false;
		c.itemUsing = -1;
	}

	public void checkObjectSpawn(int objectId, int objectX, int objectY,
			int face, int objectType) {
		if (c.distanceToPoint(objectX, objectY) > 60)
			return;
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC(objectY - (c.getMapRegionY() * 8));
			c.getOutStream().writeByteC(objectX - (c.getMapRegionX() * 8));
			c.getOutStream().createFrame(101);
			c.getOutStream().writeByteC((objectType << 2) + (face & 3));
			c.getOutStream().writeByte(0);

			if (objectId != -1) { // removing
				c.getOutStream().createFrame(151);
				c.getOutStream().writeByteS(0);
				c.getOutStream().writeWordBigEndian(objectId);
				c.getOutStream().writeByteS((objectType << 2) + (face & 3));
			}
			c.flushOutStream();
		}
	}

	/**
	 * Show option, attack, trade, follow etc
	 **/
	public String optionType = "null";

	public void showOption(int i, int l, String s, int a) {
		if (c.getOutStream() != null && c != null) {
			if (!optionType.equalsIgnoreCase(s)) {
				optionType = s;
				c.getOutStream().createFrameVarSize(104);
				c.getOutStream().writeByteC(i);
				c.getOutStream().writeByteA(l);
				c.getOutStream().writeString(s);
				c.getOutStream().endFrameVarSize();
				c.flushOutStream();
			}
		}
	}

	public void openUpBank() {
		c.getPA().sendChangeSprite(58014, c.placeHolders ? (byte) 1 : (byte) 0);
		resetVariables();
		if (c.getBankPin().isLocked() && c.getBankPin().getPin().trim().length() > 0) {
			c.getBankPin().open(2);
			c.isBanking = false;
			return;
		}
		if (c.takeAsNote)
			sendFrame36(115, 1);
		else
			sendFrame36(115, 0);

		if (c.inWild() && !(c.getRights().isAdministrator())&& !(c.getRights().isOwner())&& !(c.getRights().isCoOwner())) {
			c.sendMessage("You can't bank in the wilderness!");
			return;
		}

		if (c.getBank().getBankSearch().isSearching()) {
			c.getBank().getBankSearch().reset();
		}
		c.getPA().sendFrame126("Search", 58063);
		if (c.getOutStream() != null && c != null) {
			c.isBanking = true;
			c.getItems().resetItems(5064);
			c.getItems().resetBank();
			c.getItems().resetTempItems();
			c.getOutStream().createFrame(248);
			c.getOutStream().writeWordA(5292);// ok perfect
			c.getOutStream().writeWord(5063);
			c.flushOutStream();
		}
	}

	public boolean viewingOtherBank;
	BankTab[] backupTabs = new BankTab[9];

	public void resetOtherBank() {
		for (int i = 0; i < backupTabs.length; i++)
			c.getBank().setBankTab(i, backupTabs[i]);
		viewingOtherBank = false;
		removeAllWindows();
		c.isBanking = false;
		c.getBank().setCurrentBankTab(c.getBank().getBankTab()[0]);
		c.getItems().resetBank();
		c.sendMessage("You are no longer viewing another players bank.");
	}
	public static void coinFlip(Scanner input) {
	    while (input.hasNextLine()) {
	        String line = input.nextLine();
	        Scanner console = new Scanner(line);
	        int head = 0;
	        int total = 0;
	        
	        while (console.hasNext()) {
	            total++;
	            if (console.next().toLowerCase().equals("h")) {
	                head++;
	            }
	        }
	        
	        System.out.printf("%d heads (%.1f%%)\n", head, head * 100.0 / total);
	        if (head * 100.0 / total > 50) {
	        	
	            System.out.println("You win!");
	        }
	        System.out.println();
	    }
	}
	public void openOtherBank(Player player) {
		if (player == null)
			return;

		if (c.getPA().viewingOtherBank) {
			c.getPA().resetOtherBank();
			return;
		}
		if (player.getPA().viewingOtherBank) {
			c.sendMessage("That player is viewing another players bank, please wait.");
			return;
		}
		for (int i = 0; i < backupTabs.length; i++)
			backupTabs[i] = c.getBank().getBankTab(i);
		for (int i = 0; i < player.getBank().getBankTab().length; i++)
			c.getBank().setBankTab(i, player.getBank().getBankTab(i));
		c.getBank().setCurrentBankTab(c.getBank().getBankTab(0));
		viewingOtherBank = true;
		openUpBank();
	}

	public PlayerAssistant sendConfig(int id, int value) {
		if (value <128) {
			if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(36);
			c.getOutStream().writeWordBigEndian(id);
			c.getOutStream().writeByte(value);
			c.flushOutStream();
		} else {
			if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(87);
			c.getOutStream().writeWordBigEndian_dup(id);
			c.getOutStream().writeDWord_v1(value);
			c.flushOutStream();
			}
		}
		}
		
		return this;
		}
	public void repeatAnimation(final int tickAmount, final int anim, int duration) {
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
                 int ticks = tickAmount;
			@Override
			public void execute(CycleEventContainer container) {
			if (ticks > 0) {
	                     ticks--;
                             c.animation(anim);
		         } else if (ticks == 0) {
                               this.stop();
                         }

			}
			@Override
			public void stop() {
			 c.animation(65535);
			}
		}, duration);
	}
	/**
	 * Drink AntiPosion Potions
	 * 
	 * @param itemId
	 *            The itemId
	 * @param itemSlot
	 *            The itemSlot
	 * @param newItemId
	 *            The new item After Drinking
	 * @param healType
	 *            The type of poison it heals
	 */

	public void potionPoisonHeal(int itemId, int itemSlot, int newItemId,
			int healType) {
		c.attackTimer = c.getCombat().getAttackDelay(
				c.getItems().getItemName(c.playerEquipment[c.playerWeapon])
						.toLowerCase());
		if (c.duelRule[5]) {
			c.sendMessage("Potions has been disabled in this duel!");
			return;
		}
		if (!c.isDead && c.foodDelay.elapsed(2000)) {
			if (c.getItems().playerHasItem(itemId, 1, itemSlot)) {
				c.sendMessage("You drink the "
						+ c.getItems().getItemName(itemId).toLowerCase() + ".");
				c.foodDelay.reset();
				// Actions
				if (healType == 1) {
					// Cures The Poison
				} else if (healType == 2) {
					// Cures The Poison + protects from getting poison again
				}
				c.animation(0x33D);
				c.getItems().deleteItem(itemId, itemSlot, 1);
				c.getItems().addItem(newItemId, 1);
				requestUpdates();
			}
		}
	}

	/**
	 * Magic on items
	 **/
	@SuppressWarnings("unused")
	public void magicOnItems(int slot, int itemId, int spellId) {
		if (!c.getItems().playerHasItem(itemId, 1, slot)) {
			return;
		}

		switch (spellId) {
		case 1162: // low alch
			if (System.currentTimeMillis() - c.alchDelay > 1000) {
				if (!c.getCombat().checkMagicReqs(49)) {
					break;
				}
				if (itemId == 995) {
					c.sendMessage("You can't alch coins");
					break;
				}
				c.getItems().deleteItem(itemId, slot, 1);
				c.getItems().addItem(995,
						ItemDefinition.forId(itemId).getLowAlchValue() / 3);
				c.animation(MagicData.MAGIC_SPELLS[49][2]);
				c.gfx100(MagicData.MAGIC_SPELLS[49][3]);
				c.alchDelay = System.currentTimeMillis();
				sendFrame106(6);
				addSkillXP(MagicData.MAGIC_SPELLS[49][7] * (c.getRights().isIronman() ? 4 : Config.MAGIC_EXP_RATE), 6);
				refreshSkill(6);
			}
			break;

		case 1178: // high alch
			if (System.currentTimeMillis() - c.alchDelay > 2000) {
				if (!c.getCombat().checkMagicReqs(50)) {
					break;
				}
				if (!c.getItems().playerHasItem(itemId, 1, slot) || itemId == 995 || itemId == 995
						|| c.getItems().freeSlots() < 1) {
					return;
				}
				c.getItems().deleteItem(itemId, slot, 1);
				c.getItems().addItem(995, (int) (ItemDefinition.forId(itemId).getHighAlchValue()));
				c.animation(MagicData.MAGIC_SPELLS[50][2]);
				c.gfx100(MagicData.MAGIC_SPELLS[50][3]);
				c.alchDelay = System.currentTimeMillis();
				sendFrame106(6);
				addSkillXP(MagicData.MAGIC_SPELLS[50][7] * (c.getRights().isIronman() ? 4 : Config.MAGIC_EXP_RATE), 6);
				refreshSkill(6);
			}
			break;
			
		case 1155: // Lvl-1 enchant sapphire
		case 1165: // Lvl-2 enchant emerald
		case 1176: // Lvl-3 enchant ruby
		case 1180: // Lvl-4 enchant diamond
		case 1187: // Lvl-5 enchant dragonstone
		case 6003: // Lvl-6 enchant onyx
			Enchantment.getSingleton().enchantItem(c, itemId, spellId);
			enchantBolt(spellId, itemId, 28);
			break;
		}
	}
	private final int[][] boltData = { { 1155, 879, 9, 9236 }, { 1155, 9337, 17, 9240 }, { 1165, 9335, 19, 9237 }, { 1165, 880, 29, 9238 }, { 1165, 9338, 37, 9241 },
			{ 1176, 9336, 39, 9239 }, { 1176, 9339, 59, 9242 }, { 1180, 9340, 67, 9243 }, { 1187, 9341, 78, 9244 }, { 6003, 9342, 97, 9245 } };

	public int[][] runeData = { { 1155, 555, 1, -1, -1 }, { 1165, 556, 3, -1, -1 }, { 1176, 554, 5, -1, -1 }, { 1180, 557, 10, -1, -1 }, { 1187, 555, 15, 557, 15 },
			{ 6003, 554, 20, 557, 20 } };

	private void enchantBolt(int spell, int bolt, int amount) {

		for (int[] aBoltData : boltData) {
			if (spell==aBoltData[0]) {
				if (bolt==aBoltData[1]) {
					switch (spell) {
						case 1155:
							if (!c.getCombat().checkMagicReqs(56)) {
								return;
							}
							break;
						case 1165:
							if (!c.getCombat().checkMagicReqs(57)) {
								return;
							}
							break;
						case 1176:
							if (!c.getCombat().checkMagicReqs(58)) {
								return;
							}
							break;
						case 1180:
							if (!c.getCombat().checkMagicReqs(59)) {
								return;
							}
							break;
						case 1187:
							if (!c.getCombat().checkMagicReqs(60)) {
								return;
							}
							break;
						case 6003:
							if (!c.getCombat().checkMagicReqs(61)) {
								return;
							}
							break;
					}
					if (!c.getItems().playerHasItem(aBoltData[1], amount))
						amount=c.getItems().getItemAmount(bolt);
					c.getItems().deleteItem(aBoltData[1], c.getItems().getItemSlot(aBoltData[1]), amount);
					c.getPA().addSkillXP(aBoltData[2]*amount, 6);
					c.getItems().addItem(aBoltData[3], amount);
					c.getPA().sendFrame106(6);
					return;
				}
			}
		}
	}

	/**
	 * Dieing
	 **/

	public void applyDead() {
		c.getPA().sendFrame126(":quicks:off", -1);
		c.isFullHat = Item.isFullHat(c.playerEquipment[c.playerHat]);
		c.isFullMask = Item.isFullMask(c.playerEquipment[c.playerHat]);
		c.isFullBody = Item.isFullBody(c.playerEquipment[c.playerChest]);
		c.getPA().requestUpdates();
		c.respawnTimer = 15;
		c.isDead = false;
		c.freezeTimer = 1;
		c.recoilHits = 0;
			c.sendMessage("Oh dear you are dead!");
			c.setPoisonDamage((byte) 0);
			c.setVenomDamage((byte) 0);
		if (Config.BOUNTY_HUNTER_ACTIVE) {
			c.getBH().setCurrentHunterKills(0);
			c.getBH().setCurrentRogueKills(0);
			c.getBH().updateStatisticsUI();
			c.getBH().updateTargetUI();
		}
		c.face(null);
		c.stopMovement();
/*		if (duelSession != null && duelSession.getStage().getStage() == MultiplayerSessionStage.FURTHER_INTERACTION) {
			if (!duelSession.getWinner().isPresent()) {
				c.sendMessage("You have lost the duel!");
				Client opponent = duelSession.getOther(c);
				opponent.logoutDelay.reset();
				if (!duelSession.getWinner().isPresent()) {
					duelSession.setWinner(opponent);
				}
				PlayerSave.saveGame(opponent);
			} else {
				c.sendMessage("Congratulations, you have won the duel.");
			}
			c.logoutDelay.reset();
		}*/
		PlayerSave.saveGame(c);
		c.specAmount = 10;
		c.getItems().addSpecialBar(c.playerEquipment[c.playerWeapon]);
		c.lastVeng = 0;
		c.animation(2304);
		c.vengOn = false;
		resetFollowers();
		c.attackTimer = 10;
		c.tradeResetNeeded = true;
		c.doubleHit = false;

		removeAllWindows();
		closeAllWindows();
	}
	//}

	public void resetDamageDone() {
		for (int i = 0; i < PlayerHandler.players.length; i++) {
			if (PlayerHandler.players[i] != null) {
				PlayerHandler.players[i].damageTaken[c.index] = 0;
			}
		}
	}

	public void vengMe() {
		if (System.currentTimeMillis() - c.lastVeng > 30000) {
			if (c.getItems().playerHasItem(557, 10)
					&& c.getItems().playerHasItem(9075, 4)
					&& c.getItems().playerHasItem(560, 2)) {
				c.vengOn = true;
				c.lastVeng = System.currentTimeMillis();
				c.animation(4410);
				c.gfx100(726);
				c.getItems().deleteItem(557, c.getItems().getItemSlot(557), 10);
				c.getItems().deleteItem(560, c.getItems().getItemSlot(560), 2);
				c.getItems()
						.deleteItem(9075, c.getItems().getItemSlot(9075), 4);
			} else {
				c.sendMessage("You do not have the required runes to cast this spell. (9075 for astrals)");
			}
		} else {
			c.sendMessage("You must wait 30 seconds before casting this again.");
		}
	}

	public void resetTb() {
		c.teleBlockLength = 0;
		c.teleBlockDelay = 0;
	}

	public void resetFollowers() {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				if (PlayerHandler.players[j].followId == c.index) {
					Client c = (Client) PlayerHandler.players[j];
					c.getPA().resetFollow();
				}
			}
		}
	}
	public void updatefarming(){
		c.getAllotment().updateAllotmentsStates();
	}
	public void createProjectile3(int casterY, int casterX, int offsetY, int offsetX, int gfxMoving, int StartHeight, int endHeight, int speed, int AtkIndex) {
		for (int i = 1; i < Config.MAX_PLAYERS; i++) {
			if (PlayerHandler.players[i] != null) {
				Client p = (Client) PlayerHandler.players[i];
				if (p.WithinDistance(c.absX, c.absY, p.absX, p.absY, 60)) {
					if (p.heightLevel == c.heightLevel) {
						if (PlayerHandler.players[i] != null && !PlayerHandler.players[i].disconnected) {
							p.outStream.createFrame(85);
							p.outStream.writeByteC((casterY - (p.mapRegionY * 8)) - 2);
							p.outStream.writeByteC((casterX - (p.mapRegionX * 8)) - 3);
							p.outStream.createFrame(117);
							p.outStream.writeByte(50);
							p.outStream.writeByte(offsetY);
							p.outStream.writeByte(offsetX);
							p.outStream.writeWord(AtkIndex);
							p.outStream.writeWord(gfxMoving);
							p.outStream.writeByte(StartHeight);
							p.outStream.writeByte(endHeight);
							p.outStream.writeWord(51);
							p.outStream.writeWord(speed);
							p.outStream.writeByte(16);
							p.outStream.writeByte(64);
						}
					}
				}
			}
		}
	}
	public void getDragonClawHits(Client c, int i) {
		c.clawHit[0] = i+Misc.random(10)+1;
		c.clawHit[1] = c.clawHit[0]/2;
		c.clawHit[2] = c.clawHit[1]/2;
		c.clawHit[3] = (c.clawHit[1]-c.clawHit[2]);
		c.sendMessage(""+c.clawHit[0]+","+c.clawHit[1]+","+c.clawHit[2]+","+c.clawHit[3]+".");
	}

	public void resetDragonHits(Client c) {
		for(int i = 0; i < 4; i++) {
			c.clawHit[i] = -1;
		}
		c.usingClaws = false;
	}
	public void giveLife() {
		c.getLoot().handleLootbagDeath();
		c.isDead = false;
		c.face(null);
		c.freezeTimer = 0;
		PetHandler.ownerDeath(c);
		if(c.getRights().isHardcoreIronman()) {
			c.setRights(Rights.IRONMAN);
			c.getPA().requestUpdates();
			messageall(c.playerName+" has Died as a hardcore ironman");
		}
		//c.hasInteracted = 0;
		if (!c.inDuelArena()  && !Boundary.isIn(c, Boundary.DUEL_ARENAS)
				&& !Boundary.isIn(c, Boundary.FIGHT_CAVE)
				&& !Boundary.isIn(c, Boundary.CERBERUS_BOSSROOMS)
				&& !Boundary.isIn(c, Kraken.BOUNDARY) && !Boundary.isIn(c, Zulrah.BOUNDARY)
				&& !Boundary.isIn(c, Armadyl.BOUNDARY) && !Boundary.isIn(c, Bandos.BOUNDARY)
				&& !Boundary.isIn(c, Saradomin.BOUNDARY) && !Boundary.isIn(c, Zamorak.BOUNDARY)) {
				if(!c.getRights().isOwner() && !c.getRights().isRainbow()) { 
				for (int itemId : Config.DROP_AND_DELETE_ON_DEATH) {
					if (c.getItems().isWearingItem(itemId)) {
						int slot = c.getItems().getItemSlot(itemId);
						if (slot != -1) {
							c.getItems().removeItem(itemId, slot);
						}
					}
					if (c.getItems().playerHasItem(itemId)) {
						c.getItems().deleteItem2(itemId, c.getItems().getItemAmount(itemId));
					}
				}
				ArrayList<List<GameItem>> items = c.getItems().getItemsKeptOnDeath();
				List<GameItem> lostItems = items.get(0);
				List<GameItem> keptItems = items.get(1);

				c.getItems().dropItems(lostItems);
				c.getItems().deleteAllItems();

				for (GameItem item : keptItems) {
					c.getItems().addItem(item.getId(), item.getAmount());
				}
				} else {
					c.sendMessage("You don't lose items!");
				}
		}

		
				Client killer = (Client) PlayerHandler.getPlayer(c.getKiller());
				if (c.getItems().isWearingItem(12931)
						&& c.getItems().getWornItemSlot(12931) == c.playerHat
						|| c.getItems().playerHasItem(12931)) {
					if (c.getSerpentineHelmCharge() > 0) {
						Server.itemHandler.createGroundItem(killer == null ? c : killer, 12934, c.getX(),
								c.getY(), c.heightLevel, c.getSerpentineHelmCharge(),
								killer == null ? c.index : killer.index);
						Server.itemHandler.createGroundItem(killer == null ? c : killer, 12929, c.getX(),
								c.getY(), c.heightLevel, 1, killer == null ? c.index : killer.index);
						if (c.getItems().isWearingItem(12931)
								&& c.getItems().getWornItemSlot(12931) == c.playerHat) {
							c.getItems().wearItem(-1, 0, c.playerHat);
						} else {
							c.getItems().deleteItem2(12931, 1);
						}
						c.sendMessage(
								"The serpentine helm has been dropped on the floor. You lost the helm and it's charge.");
						c.setSerpentineHelmCharge(0);
					}
				}
				if (c.getItems().isWearingItem(12904)
						&& c.getItems().getWornItemSlot(12904) == c.playerWeapon
						|| c.getItems().playerHasItem(12904)) {
					if (c.staffOfDeadCharge > 0) {
						Server.itemHandler.createGroundItem(killer == null ? c : killer, 12934, c.getX(),
								c.getY(), c.heightLevel, c.staffOfDeadCharge,
								killer == null ? c.index : killer.index);
						Server.itemHandler.createGroundItem(killer == null ? c : killer, 12904, c.getX(),
								c.getY(), c.heightLevel, 1, killer == null ? c.index : killer.index);
						if (c.getItems().isWearingItem(12904)
								&& c.getItems().getWornItemSlot(12904) == c.playerWeapon) {
							c.getItems().wearItem(-1, 0, c.playerWeapon);
						} else {
							c.getItems().deleteItem2(12904, 1);
						}
						c.sendMessage(
								"The toxic staff of the dead has been dropped on the floor. You lost the staff and its charge.");
						c.staffOfDeadCharge = 0;
					}
				}
				if (c.getItems().isWearingItem(12926)
						&& c.getItems().getWornItemSlot(12926) == c.playerWeapon
						|| c.getItems().playerHasItem(12926)) {
					if (c.getToxicBlowpipeAmmo() > 0 && c.getToxicBlowpipeAmmoAmount() > 0
							&& c.getToxicBlowpipeCharge() > 0) {
						Server.itemHandler.createGroundItem(killer == null ? c : killer, 12924, c.getX(),
								c.getY(), c.heightLevel, 1, killer == null ? c.index : killer.index);
						Server.itemHandler.createGroundItem(killer == null ? c : killer, 12934, c.getX(),
								c.getY(), c.heightLevel, c.getToxicBlowpipeCharge(),
								killer == null ? c.index : killer.index);
						Server.itemHandler.createGroundItem(killer == null ? c : killer,
								c.getToxicBlowpipeAmmo(), c.getX(), c.getY(), c.heightLevel,
								c.getToxicBlowpipeAmmoAmount(), killer == null ? c.index : killer.index);
						if (c.getItems().isWearingItem(12926)
								&& c.getItems().getWornItemSlot(12926) == c.playerWeapon) {
							c.getItems().wearItem(-1, 0, c.playerWeapon);
						} else {
							c.getItems().deleteItem2(12926, 1);
						}
						c.setToxicBlowpipeAmmo(0);
						c.setToxicBlowpipeAmmoAmount(0);
						c.setToxicBlowpipeCharge(0);
						c.sendMessage(
								"Your blowpipe has been dropped on the floor. You lost the ammo, pipe, and charge.");
					}
				}
				/*
				 * } else if (c.inPits) {
				 * Server.fightPits.removePlayerFromPits(c.playerId);
				 * c.pitsStatus = 1;
				 */
		c.getCombat().resetPrayers();
		for (int i = 0; i < 20; i++) {
			c.playerLevel[i] = getLevelForXP(c.playerXP[i]);
			c.getPA().refreshSkill(i);
		}
		if (Boundary.isIn(c, Kalphite.BOUNDARY)) {
			c.getPA().movePlayer(3092, 3494, 0);
			InstancedArea instance7 = c.getKalphite().getInstancedKalphite();
			if (instance7 != null) {
				InstancedAreaManager.getSingleton().disposeOf(c.getKalphite().getInstancedKalphite());
				c.KALPHITE_INSTANCE = false;
				c.getKalphite().stop();
			}
		}
 if (Boundary.isIn(c, Armadyl.BOUNDARY)) {
			c.getPA().movePlayer(3092, 3494, 0);
			InstancedArea instance6 = c.getArmadyl().getInstancedArmadyl();
			if (instance6 != null) {
				InstancedAreaManager.getSingleton().disposeOf(instance6);
				c.ARMADYL_INSTANCE = false;
				c.getArmadyl().stop();
			}
	} else if (Boundary.isIn(c, Boundary.RAIDS)) {
		c.getPA().movePlayer(c.getRaids().getStartLocation().getX(),c.getRaids().getStartLocation().getY(),c.getRaids().getHeight(c.getRaids().raidLeader));
		c.getRaids().currentRoom=0;
	} else if (Boundary.isIn(c, Boundary.OLM)) {
		c.getPA().movePlayer(c.getRaids().getStartLocation().getX(),c.getRaids().getStartLocation().getY(),c.getRaids().getHeight(c.getRaids().raidLeader));
		} else if (Boundary.isIn(c, Bandos.BOUNDARY)) {
			c.getPA().movePlayer(3092, 3494, 0);
			InstancedArea instance1 = c.getBandos().getInstancedBandos();
			if (instance1 != null) {
				InstancedAreaManager.getSingleton().disposeOf(instance1);
				c.BANDOS_INSTANCE = false;
				c.getBandos().stop();
			}
		} else if (Boundary.isIn(c, Saradomin.BOUNDARY)) {
			c.getPA().movePlayer(3092, 3494, 0);
			InstancedArea instance2 = c.getSaradomin().getInstancedSaradomin();
			if (instance2 != null) {
				InstancedAreaManager.getSingleton().disposeOf(instance2);
				c.SARADOMIN_INSTANCE = false;
				c.getSaradomin().stop();
			}
		} else if (Boundary.isIn(c, Zamorak.BOUNDARY)) {
			c.getPA().movePlayer(3092, 3494, 0);
			InstancedArea instance3 = c.getZamorak().getInstancedZamorak();
			if (instance3 != null) {
				InstancedAreaManager.getSingleton().disposeOf(instance3);
				c.ZAMORAK_INSTANCE = false;
				c.getZamorak().stop();
			}
		} else if (Boundary.isIn(c, Kraken.BOUNDARY)) {
			c.getPA().movePlayer(3092, 3494, 0);
			InstancedArea instance4 = c.getKraken().getInstancedKraken();
			if (instance4 != null) {
				InstancedAreaManager.getSingleton().disposeOf(c.getKraken().getInstancedKraken());
				c.getKraken().stop();
			}
		} else if (Boundary.isIn(c, Zulrah.BOUNDARY)) {
			c.getPA().movePlayer(3092, 3494, 0);
			InstancedArea instance5 = c.getZulrahEvent().getInstancedZulrah();
			if (instance5 != null) {
				InstancedAreaManager.getSingleton().disposeOf(instance5);
				c.getZulrahEvent().DISPOSE_EVENT();
				c.getZulrahLostItems().store();
				c.sendMessage("Talk to Zul-Areth to get your items back.");
			}
		} else if (Boundary.isIn(c, Boundary.FIGHT_CAVE)) {
			c.getFightCave().handleDeath();
		} else if (Boundary.isIn(c, Boundary.INFERNO)) {
			c.getInfernoMinigame().handleDeath();
	} else if (Boundary.isIn(c, Boundary.RAIDS)) {
		c.getPA().movePlayer(c.getRaids().getStartLocation().getX(),c.getRaids().getStartLocation().getY(),c.getRaids().getHeight(c.getRaids().raidLeader));
	} else if (Boundary.isIn(c, Boundary.OLM)) {
		c.getPA().movePlayer(c.getRaids().getStartLocation().getX(),c.getRaids().getStartLocation().getY(),c.getRaids().getHeight(c.getRaids().raidLeader));
	}
							// wildy
			movePlayer(Config.RESPAWN_X, Config.RESPAWN_Y, 0);
			c.isSkulled = false;
			c.skullTimer = 0;
			c.attackedPlayers.clear();

		// PlayerSaving.getSingleton().requestSave(c.index);
		PlayerSave.saveGame(c);
		c.getCombat().resetPlayerAttack();
		resetAnimation();
		c.animation(65535);
		frame1();
		resetTb();
		c.isSkulled = false;
		c.attackedPlayers.clear();
		c.headIconPk = -1;
		c.skullTimer = -1;
		c.damageTaken = new int[Config.MAX_PLAYERS];
		c.getPA().requestUpdates();
		removeAllWindows();
		c.tradeResetNeeded = true;
	}
	public void resetTzhaar() {
		c.waveId = -1;
		c.tzhaarToKill = -1;
		c.tzhaarKilled = -1;
	}
	/**
	 * Location change for digging, levers etc
	 **/

	public void changeLocation() {
		switch (c.newLocation) {
		case 1:
			sendFrame99(2);
			movePlayer(3578, 9706, -1);
			break;
		case 2:
			sendFrame99(2);
			movePlayer(3568, 9683, -1);
			break;
		case 3:
			sendFrame99(2);
			movePlayer(3557, 9703, -1);
			break;
		case 4:
			sendFrame99(2);
			movePlayer(3556, 9718, -1);
			break;
		case 5:
			sendFrame99(2);
			movePlayer(3534, 9704, -1);
			break;
		case 6:
			sendFrame99(2);
			movePlayer(3546, 9684, -1);
			break;
		}
		c.newLocation = 0;
	}

	/**
	 * Teleporting
	 **/
	public void spellTeleport(int x, int y, int height) {
		c.getPA().startTeleport(x, y, height,
				c.playerMagicBook == 1 ? "ancient" : "modern");
	}

	public void startTeleport(int x, int y, int height, String teleportType) {
		if (c.duelStatus == 5) {
			c.sendMessage("You can't teleport during a duel!");
			return;
		}
		if (c.inWild() && c.wildLevel > Config.NO_TELEPORT_WILD_LEVEL) {
			c.sendMessage("You can't teleport above level "
					+ Config.NO_TELEPORT_WILD_LEVEL + " in the wilderness.");
			return;
		}
		if (System.currentTimeMillis() - c.teleBlockDelay < c.teleBlockLength) {
			c.sendMessage("You are teleblocked and can't teleport.");
			return;
		}
		if (!c.isDead && c.teleTimer == 0 && c.respawnTimer == -6) {
			if (c.playerIndex > 0 || c.npcIndex > 0)
				c.getCombat().resetPlayerAttack();
			c.stopMovement();
			removeAllWindows();
			c.teleX = x;
			c.teleY = y;
			c.npcIndex = 0;
			c.playerIndex = 0;
			c.face(null);
			c.teleHeight = height;
			if (teleportType.equalsIgnoreCase("modern")) {
				c.animation(714);
				c.teleTimer = 11;
				c.teleGfx = 308;
				c.teleEndAnimation = 715;
			}
			if (teleportType.equalsIgnoreCase("ancient")) {
				c.animation(1979);
				c.teleGfx = 0;
				c.teleTimer = 9;
				c.teleEndAnimation = 0;
				c.gfx0(392);
			}

		}
	}

	public void startTeleport2(int x, int y, int height) {
		if (c.duelStatus == 5) {
			c.sendMessage("You can't teleport during a duel!");
			return;
		}
		if (System.currentTimeMillis() - c.teleBlockDelay < c.teleBlockLength) {
			c.sendMessage("You are teleblocked and can't teleport.");
			return;
		}
		if (!c.isDead && c.teleTimer == 0) {
			c.stopMovement();
			removeAllWindows();
			c.teleX = x;
			c.teleY = y;
			c.npcIndex = 0;
			c.playerIndex = 0;
			c.face(null);
			c.teleHeight = height;
			c.animation(714);
			c.teleTimer = 11;
			c.teleGfx = 308;
			c.teleEndAnimation = 715;

		}
	}

	public void processTeleport() {
		c.teleportToX = c.teleX;
		c.teleportToY = c.teleY;
		c.heightLevel = c.teleHeight;
		if (c.teleEndAnimation > 0) {
			c.animation(c.teleEndAnimation);
		}
	}

	public void movePlayer(int x, int y, int h) {
		if (c.getHouse() != null && c.inConstruction() && !(x >= 16 && x <= 55 && y >= 16 && y <= 55)) {
			c.getHouse().leave(c);
		}
		c.resetWalkingQueue();
		c.teleportToX = x;
		c.teleportToY = y;
		c.heightLevel = h;
		requestUpdates();
		
		final Client client = c;
		
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				if (!client.disconnected)
					Server.itemHandler.reloadItems(client);
				container.stop();				
			}

			@Override
			public void stop() {}
			
		}, 2);
	}

	/**
	 * Following
	 **/

	/*
	 * public void Player() { if(Server.playerHandler.players[c.followId] ==
	 * null || Server.playerHandler.players[c.followId].isDead) {
	 * c.getPA().resetFollow(); return; } if(c.freezeTimer > 0) { return; } int
	 * otherX = Server.playerHandler.players[c.followId].getX(); int otherY =
	 * Server.playerHandler.players[c.followId].getY(); boolean withinDistance =
	 * c.goodDistance(otherX, otherY, c.getX(), c.getY(), 2); boolean
	 * hallyDistance = c.goodDistance(otherX, otherY, c.getX(), c.getY(), 2);
	 * boolean bowDistance = c.goodDistance(otherX, otherY, c.getX(), c.getY(),
	 * 6); boolean rangeWeaponDistance = c.goodDistance(otherX, otherY,
	 * c.getX(), c.getY(), 2); boolean sameSpot = (c.absX == otherX && c.absY ==
	 * otherY); if(!c.goodDistance(otherX, otherY, c.getX(), c.getY(), 25)) {
	 * c.followId = 0; c.getPA().resetFollow(); return; }
	 * c.face(c.followId+32768); if ((c.usingBow || c.mageFollow ||
	 * c.autocastId > 0 && (c.npcIndex > 0 || c.playerIndex > 0)) && bowDistance
	 * && !sameSpot) { c.stopMovement(); return; } if (c.usingRangeWeapon &&
	 * rangeWeaponDistance && !sameSpot && (c.npcIndex > 0 || c.playerIndex >
	 * 0)) { c.stopMovement(); return; } if(c.goodDistance(otherX, otherY,
	 * c.getX(), c.getY(), 1) && !sameSpot) { return; }
	 * c.outStream.createFrame(174); boolean followPlayer = c.followId > 0; if
	 * (c.freezeTimer <= 0) if (followPlayer) c.outStream.writeWord(c.followId);
	 * else c.outStream.writeWord(c.followId2); else c.outStream.writeWord(0);
	 * 
	 * if (followPlayer) c.outStream.writeByte(1); else
	 * c.outStream.writeByte(0); if (c.usingBow && c.playerIndex > 0)
	 * c.followDistance = 5; else if (c.usingRangeWeapon && c.playerIndex > 0)
	 * c.followDistance = 3; else if (c.spellId > 0 && c.playerIndex > 0)
	 * c.followDistance = 5; else c.followDistance = 1;
	 * c.outStream.writeWord(c.followDistance); }
	 */

	/**
	 * Following
	 **/

	public void followPlayer() {
		if (c.followId > PlayerHandler.players.length) {
			c.followId = 0;
			return;
		}
		if (PlayerHandler.players[c.followId] == null) {
			c.followId = 0;
			return;
		}
		if (PlayerHandler.players[c.followId].isDead) {
			c.followId = 0;
			return;
		}
		if (c.freezeTimer > 0) {
			return;
		}
		/*if (Boundary.isIn(c, Boundary.DUEL_ARENAS)) {
			DuelSession session = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
					MultiplayerSessionType.DUEL);
			if (!Objects.isNull(session)) {
				if (session.getRules().contains(Rule.NO_MOVEMENT)) {
					c.followId = 0;
					return;
				}
			}
		}*/
		if (inPitsWait()) {
			c.followId = 0;
		}

		if (c.isDead || c.playerLevel[3] <= 0)
			return;
		/*if (!c.lastSpear.elapsed(4000)) {
			c.sendMessage("You are stunned, you cannot move.");
			c.followId = 0;
			return;
		}*/
		int otherX = PlayerHandler.players[c.followId].getX();
		int otherY = PlayerHandler.players[c.followId].getY();

		boolean sameSpot = (c.absX == otherX && c.absY == otherY);

		boolean hallyDistance = c.goodDistance(otherX, otherY, c.getX(), c.getY(), 2);
		boolean withinDistance = c.goodDistance(otherX, otherY, c.getX(), c.getY(), 2);
		boolean rangeWeaponDistance = c.goodDistance(otherX, otherY, c.getX(), c.getY(), 6);
		boolean bowDistance = c.goodDistance(otherX, otherY, c.getX(), c.getY(), 6);

		if (!c.goodDistance(otherX, otherY, c.getX(), c.getY(), 25)) {
			c.followId = 0;
			return;
		}

		c.face(PlayerHandler.players[c.followId]);

		boolean projectile = c.usingBow || c.mageFollow || c.autocastId > 0 || c.usingRangeWeapon
				|| c.usingOtherRangeWeapons;
		if (!projectile || projectile && (PathChecker.isProjectilePathClear(c.absX, c.absY,
				c.heightLevel, otherX, otherY)
				|| PathChecker.isProjectilePathClear(otherX, otherY, c.heightLevel, c.absX, c.absY))) {
			if (c.goodDistance(otherX, otherY, c.getX(), c.getY(), 1)) {
				if (otherX != c.getX() || otherY != c.getY()) {
					return;
				}
			}

			if ((c.usingBow || c.mageFollow || (c.playerIndex > 0 && c.autocastId > 0))
					&& bowDistance && !sameSpot) {
				return;
			}

			if (c.getCombat().usingHally() && hallyDistance && !sameSpot) {
				return;
			}

			if (c.usingRangeWeapon && rangeWeaponDistance && !sameSpot) {
				return;
			}
		}

		final int x = c.absX;
		final int y = c.absY;
		final int z = c.heightLevel;
		final int x2 = otherX;
		final int y2 = otherY;
		double lowDist = 9999;
		int lowX = 0;
		int lowY = 0;
		int x3 = x2;
		int y3 = y2 - 1;

		int[][] nonDiags = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

		for (int k = 0; k < 4; k++) {
			x3 = otherX + nonDiags[k][0];
			y3 = otherY + nonDiags[k][1];
			if (Misc.distance(x, y, x3, y3) < lowDist) {
				if (!projectile && PathChecker.isMeleePathClear(x3, y3, z, x2, y2)
						|| projectile && PathChecker.isProjectilePathClear(x3, y3, z, x2, y2)) {
					if (PathFinder.getPathFinder().accessable(c, x3, y3)) {
						lowDist = Misc.distance(x, y, x3, y3);
						lowX = x3;
						lowY = y3;
					}
				}
			}
		}

		if (lowX != 0 && lowY != 0) {
			PathFinder.getPathFinder().findRoute(c, lowX, lowY, true, 18, 18);
		} else {
			PathFinder.getPathFinder().findRoute(c, otherX, otherY, true, 18, 18);
		}
	}
    public void showMap() {
    	int pos = ((c.absX/ 64) - 46) + (((c.absY) / 64) - 49) * 6;
		sendFrame36(106, pos);  //idk haven't used PI in a while...
		showInterface(5392);// is it openInterface? not sure
	}


	public void followNpc() {
		if (NPCHandler.npcs[c.followId2] == null || NPCHandler.npcs[c.followId2].isDead) {
			c.followId2 = 0;
			return;
		}
		if (c.freezeTimer > 0) {
			return;
		}
		if (c.isDead || c.playerLevel[3] <= 0)
			return;
		int otherX = NPCHandler.npcs[c.followId2].getX();
		int otherY = NPCHandler.npcs[c.followId2].getY();

		NPC npc = NPCHandler.npcs[c.followId2];
		double distance = npc.getDistance(c.getX(), c.getY());

		boolean withinDistance = distance <= 2;
		boolean hallyDistance = distance <= 2;
		boolean bowDistance = distance <= 8;
		boolean rangeWeaponDistance = distance <= 4;
		boolean sameSpot = c.absX == otherX && c.absY == otherY;

		if (!c.goodDistance(otherX, otherY, c.getX(), c.getY(), 25)) {
			c.followId2 = 0;
			return;
		}

		c.face(npc);

		if (distance <= 1) {
			if (!npc.insideOf(c.getX(), c.getY())) {
				return;
			}
		}

		boolean projectile = c.usingBow || c.mageFollow || (c.npcIndex > 0 && c.autocastId > 0)
				|| c.usingRangeWeapon || c.usingOtherRangeWeapons;

		if (projectile && npc.npcType == AbyssalSireConstants.SLEEPING_NPC_ID) {
			return;
		}

		if (c.getCombat().usingHally() && hallyDistance && !sameSpot) {
			return;
		}

		if (!npc.insideOf(c.getX(), c.getY())) {
			if (!projectile || projectile && (PathChecker.isProjectilePathClear(c.getX(), c.getY(),
					c.heightLevel, npc.getX(), npc.getY())
					|| PathChecker.isProjectilePathClear(npc.getX(), npc.getY(), c.heightLevel, c.getX(),
							c.getY()))) {

				if ((c.usingBow || c.mageFollow || (c.npcIndex > 0 && c.autocastId > 0))
						&& bowDistance && !sameSpot) {
					return;
				}

				if (c.usingRangeWeapon && rangeWeaponDistance && !sameSpot) {
					return;
				}
			}
		}

		boolean isWaterNPC = npc.npcType == 2042 || npc.npcType == 2043 || npc.npcType == 2044 || npc.npcType == 6656
				|| npc.npcType == 6640 || npc.npcType == 494 || npc.npcType == 496 || npc.npcType == 493;

		if (isWaterNPC) {
			if (c.isRunning && !withinDistance) {
				if (otherY > c.getY() && otherX == c.getX()) {
					// walkTo(0, getMove(c.getY(), otherY - 1) +
					// getMove(c.getY(),
					// otherY - 1));
					playerWalk(otherX, otherY - 1);
				} else if (otherY < c.getY() && otherX == c.getX()) {
					// walkTo(0, getMove(c.getY(), otherY + 1) +
					// getMove(c.getY(),
					// otherY + 1));
					playerWalk(otherX, otherY + 1);
				} else if (otherX > c.getX() && otherY == c.getY()) {
					// walkTo(getMove(c.getX(), otherX - 1) + getMove(c.getX(),
					// otherX - 1), 0);
					playerWalk(otherX - 1, otherY);
				} else if (otherX < c.getX() && otherY == c.getY()) {
					// walkTo(getMove(c.getX(), otherX + 1) + getMove(c.getX(),
					// otherX + 1), 0);
					playerWalk(otherX + 1, otherY);
				} else if (otherX < c.getX() && otherY < c.getY()) {
					// walkTo(getMove(c.getX(), otherX + 1) + getMove(c.getX(),
					// otherX + 1), getMove(c.getY(), otherY + 1) +
					// getMove(c.getY(), otherY + 1));
					playerWalk(otherX + 1, otherY + 1);
				} else if (otherX > c.getX() && otherY > c.getY()) {
					// walkTo(getMove(c.getX(), otherX - 1) + getMove(c.getX(),
					// otherX - 1), getMove(c.getY(), otherY - 1) +
					// getMove(c.getY(), otherY - 1));
					playerWalk(otherX - 1, otherY - 1);
				} else if (otherX < c.getX() && otherY > c.getY()) {
					// walkTo(getMove(c.getX(), otherX + 1) + getMove(c.getX(),
					// otherX + 1), getMove(c.getY(), otherY - 1) +
					// getMove(c.getY(), otherY - 1));
					playerWalk(otherX + 1, otherY - 1);
				} else if (otherX > c.getX() && otherY < c.getY()) {
					// walkTo(getMove(c.getX(), otherX + 1) + getMove(c.getX(),
					// otherX + 1), getMove(c.getY(), otherY - 1) +
					// getMove(c.getY(), otherY - 1));
					playerWalk(otherX + 1, otherY - 1);
				}
			} else {
				if (otherY > c.getY() && otherX == c.getX()) {
					// walkTo(0, getMove(c.getY(), otherY - 1));
					playerWalk(otherX, otherY - 1);
				} else if (otherY < c.getY() && otherX == c.getX()) {
					// walkTo(0, getMove(c.getY(), otherY + 1));
					playerWalk(otherX, otherY + 1);
				} else if (otherX > c.getX() && otherY == c.getY()) {
					// walkTo(getMove(c.getX(), otherX - 1), 0);
					playerWalk(otherX - 1, otherY);
				} else if (otherX < c.getX() && otherY == c.getY()) {
					// walkTo(getMove(c.getX(), otherX + 1), 0);
					playerWalk(otherX + 1, otherY);
				} else if (otherX < c.getX() && otherY < c.getY()) {
					// walkTo(getMove(c.getX(), otherX + 1), getMove(c.getY(),
					// otherY + 1));
					playerWalk(otherX + 1, otherY + 1);
				} else if (otherX > c.getX() && otherY > c.getY()) {
					// walkTo(getMove(c.getX(), otherX - 1), getMove(c.getY(),
					// otherY - 1));
					playerWalk(otherX - 1, otherY - 1);
				} else if (otherX < c.getX() && otherY > c.getY()) {
					// walkTo(getMove(c.getX(), otherX + 1), getMove(c.getY(),
					// otherY - 1));
					playerWalk(otherX + 1, otherY - 1);
				} else if (otherX > c.getX() && otherY < c.getY()) {
					// walkTo(getMove(c.getX(), otherX - 1), getMove(c.getY(),
					// otherY + 1));
					playerWalk(otherX - 1, otherY + 1);
				}
			}
		} else {

			final int x = c.absX;
			final int y = c.absY;
			final int z = c.heightLevel;
			final int x2 = otherX;
			final int y2 = otherY;
			double lowDist = 9999;
			int lowX = 0;
			int lowY = 0;
			int x3 = x2;
			int y3 = y2 - 1;
			final int loop = npc.getSize();

			for (int k = 0; k < 4; k++) {
				for (int i = 0; i < loop - (k == 0 ? 1 : 0); i++) {
					if (k == 0) {
						x3++;
					} else if (k == 1) {
						if (i == 0) {
							x3++;
						}
						y3++;
					} else if (k == 2) {
						if (i == 0) {
							y3++;
						}
						x3--;
					} else if (k == 3) {
						if (i == 0) {
							x3--;
						}
						y3--;
					}

					if (Misc.distance(x, y, x3, y3) < lowDist) {
						if (!projectile && PathChecker.isMeleePathClear(x3, y3, z, x2, y2)
								|| projectile && PathChecker.isProjectilePathClear(x3, y3, z, x2, y2)) {
							if (PathFinder.getPathFinder().accessable(c, x3, y3)) {
								lowDist = Misc.distance(x, y, x3, y3);
								lowX = x3;
								lowY = y3;
							}
						}
					}
				}
			}

			if (lowX != 0 && lowY != 0) {
				PathFinder.getPathFinder().findRoute(c, lowX, lowY, true, 18, 18);
			} else {
				PathFinder.getPathFinder().findRoute(c, npc.absX, npc.absY, true, 18, 18);
			}

		}
	}
	public int getRunningMove(int i, int j) {
		if (j - i > 2)
			return 2;
		else if (j - i < -2)
			return -2;
		else
			return j - i;
	}
	
    public void playerWalk(int x, int y) {
        PathFinder.getPathFinder().findRoute(c, x, y, true, 1, 1);
}

	public void resetFollow() {
		c.followId = 0;
		c.followId2 = 0;
		c.mageFollow = false;
	}
	public static void playSound(Client c, int i1, int i2, int i3) {
	c.outStream.createFrame(174);
	c.outStream.writeWord(i1);
	c.outStream.writeByte(i2);
	c.outStream.writeWord(i3);
}
	public void messageall(String message){
	for (int i = 0; i < Server.playerHandler.players.length; i++) {
	if (Server.playerHandler.players[i] != null) {
	Client c2 = (Client)Server.playerHandler.players[i];
	c2.sendMessage(message);
	}
	}
	}
    public void playMusic(int i) {
    	if (c.outStream != null && c != null && i != -1) {
        c.outStream.createFrame(74);
        c.outStream.writeWordBigEndian(i);
    	}
    }
	public void walkTo(int x, int y) {
		PathFinder.getPathFinder().findRoute(c, x, y, true, 1, 1);
	}

	public void walkTo2(int i, int j) {
		if (c.freezeDelay > 0)
			return;
		c.newWalkCmdSteps = 0;
		if (++c.newWalkCmdSteps > 50)
			c.newWalkCmdSteps = 0;
		int k = c.getX() + i;
		k -= c.mapRegionX * 8;
		c.getNewWalkCmdX()[0] = c.getNewWalkCmdY()[0] = 0;
		int l = c.getY() + j;
		l -= c.mapRegionY * 8;

		for (int n = 0; n < c.newWalkCmdSteps; n++) {
			c.getNewWalkCmdX()[n] += k;
			c.getNewWalkCmdY()[n] += l;
		}
	}

	public void stopDiagonal(int otherX, int otherY) {
		if (c.freezeDelay > 0)
			return;
		c.newWalkCmdSteps = 1;
		int xMove = otherX - c.getX();
		int yMove = 0;
		if (xMove == 0)
			yMove = otherY - c.getY();
		/*
		 * if (!clipHor) { yMove = 0; } else if (!clipVer) { xMove = 0; }
		 */

		int k = c.getX() + xMove;
		k -= c.mapRegionX * 8;
		c.getNewWalkCmdX()[0] = c.getNewWalkCmdY()[0] = 0;
		int l = c.getY() + yMove;
		l -= c.mapRegionY * 8;

		for (int n = 0; n < c.newWalkCmdSteps; n++) {
			c.getNewWalkCmdX()[n] += k;
			c.getNewWalkCmdY()[n] += l;
		}

	}

	public void walkToCheck(int i, int j) {
		if (c.freezeDelay > 0)
			return;
		c.newWalkCmdSteps = 0;
		if (++c.newWalkCmdSteps > 50)
			c.newWalkCmdSteps = 0;
		int k = c.getX() + i;
		k -= c.mapRegionX * 8;
		c.getNewWalkCmdX()[0] = c.getNewWalkCmdY()[0] = 0;
		int l = c.getY() + j;
		l -= c.mapRegionY * 8;

		for (int n = 0; n < c.newWalkCmdSteps; n++) {
			c.getNewWalkCmdX()[n] += k;
			c.getNewWalkCmdY()[n] += l;
		}
	}

	public int getMove(int place1, int place2) {
		if ((place1 - place2) == 0) {
			return 0;
		} else if ((place1 - place2) < 0) {
			return 1;
		} else if ((place1 - place2) > 0) {
			return -1;
		}
		return 0;
	}

	public boolean fullVeracs() {
		return c.playerEquipment[c.playerHat] == 4753
				&& c.playerEquipment[c.playerChest] == 4757
				&& c.playerEquipment[c.playerLegs] == 4759
				&& c.playerEquipment[c.playerWeapon] == 4755;
	}

	public boolean fullGuthans() {
		return c.playerEquipment[c.playerHat] == 4724
				&& c.playerEquipment[c.playerChest] == 4728
				&& c.playerEquipment[c.playerLegs] == 4730
				&& c.playerEquipment[c.playerWeapon] == 4726;
	}

	/**
	 * reseting animation
	 **/
	public void resetAnimation() {
		c.getCombat().getPlayerAnimIndex(
				c.getItems().getItemName(c.playerEquipment[c.playerWeapon])
						.toLowerCase());
		c.animation(c.playerStandIndex);
		requestUpdates();
	}

	public void requestUpdates() {
		c.updateRequired = true;
		c.setAppearanceUpdateRequired(true);
	}

	public void handleAlt(int id) {
		if (!c.getItems().playerHasItem(id)) {
			c.getItems().addItem(id, 1);
		}
	}

	public void levelUp(int skill) {
		switch (skill) {
		case 0:
			sendFrame126("Congratulations, you just advanced an attack level!",
					6248);
			sendFrame126("Your attack level is now "
					+ getLevelForXP(c.playerXP[skill]) + ".", 6249);
			c.sendMessage("Congratulations, you just advanced an attack level.");
			sendChatInterface(6247);
			break;

		case 1:
			sendFrame126("Congratulations, you just advanced a defence level!",
					6254);
			sendFrame126("Your defence level is now "
					+ getLevelForXP(c.playerXP[skill]) + ".", 6255);
			c.sendMessage("Congratulations, you just advanced a defence level.");
			sendChatInterface(6253);
			break;

		case 2:
			sendFrame126(
					"Congratulations, you just advanced a strength level!",
					6207);
			sendFrame126("Your strength level is now "
					+ getLevelForXP(c.playerXP[skill]) + ".", 6208);
			c.sendMessage("Congratulations, you just advanced a strength level.");
			sendChatInterface(6206);
			break;

		case 3:
			sendFrame126(
					"Congratulations, you just advanced a hitpoints level!",
					6217);
			sendFrame126("Your hitpoints level is now "
					+ getLevelForXP(c.playerXP[skill]) + ".", 6218);
			c.sendMessage("Congratulations, you just advanced a hitpoints level.");
			sendChatInterface(6216);
			break;

		case 4:
			sendFrame126("Congratulations, you just advanced a ranged level!",
					5453);
			sendFrame126("Your ranged level is now "
					+ getLevelForXP(c.playerXP[skill]) + ".", 6114);
			c.sendMessage("Congratulations, you just advanced a ranging level.");
			sendChatInterface(4443);
			break;

		case 5:
			sendFrame126("Congratulations, you just advanced a prayer level!",
					6243);
			sendFrame126("Your prayer level is now "
					+ getLevelForXP(c.playerXP[skill]) + ".", 6244);
			c.sendMessage("Congratulations, you just advanced a prayer level.");
			sendChatInterface(6242);
			break;

		case 6:
			sendFrame126("Congratulations, you just advanced a magic level!",
					6212);
			sendFrame126("Your magic level is now "
					+ getLevelForXP(c.playerXP[skill]) + ".", 6213);
			c.sendMessage("Congratulations, you just advanced a magic level.");
			sendChatInterface(6211);
			break;

		case 7:
			sendFrame126("Congratulations, you just advanced a cooking level!",
					6227);
			sendFrame126("Your cooking level is now "
					+ getLevelForXP(c.playerXP[skill]) + ".", 6228);
			c.sendMessage("Congratulations, you just advanced a cooking level.");
			sendChatInterface(6226);
			break;

		case 8:
			sendFrame126(
					"Congratulations, you just advanced a woodcutting level!",
					4273);
			sendFrame126("Your woodcutting level is now "
					+ getLevelForXP(c.playerXP[skill]) + ".", 4274);
			c.sendMessage("Congratulations, you just advanced a woodcutting level.");
			sendChatInterface(4272);
			break;

		case 9:
			sendFrame126(
					"Congratulations, you just advanced a fletching level!",
					6232);
			sendFrame126("Your fletching level is now "
					+ getLevelForXP(c.playerXP[skill]) + ".", 6233);
			c.sendMessage("Congratulations, you just advanced a fletching level.");
			sendChatInterface(6231);
			break;

		case 10:
			sendFrame126("Congratulations, you just advanced a fishing level!",
					6259);
			sendFrame126("Your fishing level is now "
					+ getLevelForXP(c.playerXP[skill]) + ".", 6260);
			c.sendMessage("Congratulations, you just advanced a fishing level.");
			sendChatInterface(6258);
			break;

		case 11:
			sendFrame126(
					"Congratulations, you just advanced a fire making level!",
					4283);
			sendFrame126("Your firemaking level is now "
					+ getLevelForXP(c.playerXP[skill]) + ".", 4284);
			c.sendMessage("Congratulations, you just advanced a fire making level.");
			sendChatInterface(4282);
			break;

		case 12:
			sendFrame126(
					"Congratulations, you just advanced a crafting level!",
					6264);
			sendFrame126("Your crafting level is now "
					+ getLevelForXP(c.playerXP[skill]) + ".", 6265);
			c.sendMessage("Congratulations, you just advanced a crafting level.");
			sendChatInterface(6263);
			break;

		case 13:
			sendFrame126(
					"Congratulations, you just advanced a smithing level!",
					6222);
			sendFrame126("Your smithing level is now "
					+ getLevelForXP(c.playerXP[skill]) + ".", 6223);
			c.sendMessage("Congratulations, you just advanced a smithing level.");
			sendChatInterface(6221);
			break;

		case 14:
			sendFrame126("Congratulations, you just advanced a mining level!",
					4417);
			sendFrame126("Your mining level is now "
					+ getLevelForXP(c.playerXP[skill]) + ".", 4438);
			c.sendMessage("Congratulations, you just advanced a mining level.");
			sendChatInterface(4416);
			break;

		case 15:
			sendFrame126(
					"Congratulations, you just advanced a herblore level!",
					6238);
			sendFrame126("Your herblore level is now "
					+ getLevelForXP(c.playerXP[skill]) + ".", 6239);
			c.sendMessage("Congratulations, you just advanced a herblore level.");
			sendChatInterface(6237);
			break;

		case 16:
			sendFrame126("Congratulations, you just advanced a agility level!",
					4278);
			sendFrame126("Your agility level is now "
					+ getLevelForXP(c.playerXP[skill]) + ".", 4279);
			c.sendMessage("Congratulations, you just advanced an agility level.");
			sendChatInterface(4277);
			break;

		case 17:
			sendFrame126(
					"Congratulations, you just advanced a thieving level!",
					4263);
			sendFrame126("Your theiving level is now "
					+ getLevelForXP(c.playerXP[skill]) + ".", 4264);
			c.sendMessage("Congratulations, you just advanced a thieving level.");
			sendChatInterface(4261);
			break;

		case 18:
			sendFrame126("Congratulations, you just advanced a slayer level!",
					12123);
			sendFrame126("Your slayer level is now "
					+ getLevelForXP(c.playerXP[skill]) + ".", 12124);
			c.sendMessage("Congratulations, you just advanced a slayer level.");
			sendChatInterface(12122);
			break;

		case 20:
			sendFrame126(
					"Congratulations, you just advanced a runecrafting level!",
					4268);
			sendFrame126("Your runecrafting level is now "
					+ getLevelForXP(c.playerXP[skill]) + ".", 4269);
			c.sendMessage("Congratulations, you just advanced a runecrafting level.");
			sendChatInterface(4267);
			break;
		}
		c.dialogueAction = 0;
		c.nextChat = 0;
		sendFrame126("Click here to continue", 358);
	}

	public void refreshSkill(int i) {
		switch (i) {
		case 0:
			sendFrame126("" + c.playerLevel[0] + "", 4004);
			sendFrame126("" + getLevelForXP(c.playerXP[0]) + "", 4005);
			sendFrame126("" + c.playerXP[0] + "", 4044);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[0]) + 1)
					+ "", 4045);
			break;

		case 1:
			sendFrame126("" + c.playerLevel[1] + "", 4008);
			sendFrame126("" + getLevelForXP(c.playerXP[1]) + "", 4009);
			sendFrame126("" + c.playerXP[1] + "", 4056);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[1]) + 1)
					+ "", 4057);
			break;

		case 2:
			sendFrame126("" + c.playerLevel[2] + "", 4006);
			sendFrame126("" + getLevelForXP(c.playerXP[2]) + "", 4007);
			sendFrame126("" + c.playerXP[2] + "", 4050);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[2]) + 1)
					+ "", 4051);
			break;

		case 3:
			sendFrame126("" + c.playerLevel[3] + "", 4016);
			sendFrame126("" + getLevelForXP(c.playerXP[3]) + "", 4017);
			sendFrame126("" + c.playerXP[3] + "", 4080);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[3]) + 1)
					+ "", 4081);
			break;

		case 4:
			sendFrame126("" + c.playerLevel[4] + "", 4010);
			sendFrame126("" + getLevelForXP(c.playerXP[4]) + "", 4011);
			sendFrame126("" + c.playerXP[4] + "", 4062);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[4]) + 1)
					+ "", 4063);
			break;

		case 5:
			sendFrame126("" + c.playerLevel[5] + "", 4012);
			sendFrame126("" + getLevelForXP(c.playerXP[5]) + "", 4013);
			sendFrame126("" + c.playerXP[5] + "", 4068);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[5]) + 1)
					+ "", 4069);
			sendFrame126("" + c.playerLevel[5] + "/"
					+ getLevelForXP(c.playerXP[5]) + "", 687);// Prayer frame
			break;

		case 6:
			sendFrame126("" + c.playerLevel[6] + "", 4014);
			sendFrame126("" + getLevelForXP(c.playerXP[6]) + "", 4015);
			sendFrame126("" + c.playerXP[6] + "", 4074);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[6]) + 1)
					+ "", 4075);
			break;

		case 7:
			sendFrame126("" + c.playerLevel[7] + "", 4034);
			sendFrame126("" + getLevelForXP(c.playerXP[7]) + "", 4035);
			sendFrame126("" + c.playerXP[7] + "", 4134);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[7]) + 1)
					+ "", 4135);
			break;

		case 8:
			sendFrame126("" + c.playerLevel[8] + "", 4038);
			sendFrame126("" + getLevelForXP(c.playerXP[8]) + "", 4039);
			sendFrame126("" + c.playerXP[8] + "", 4146);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[8]) + 1)
					+ "", 4147);
			break;

		case 9:
			sendFrame126("" + c.playerLevel[9] + "", 4026);
			sendFrame126("" + getLevelForXP(c.playerXP[9]) + "", 4027);
			sendFrame126("" + c.playerXP[9] + "", 4110);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[9]) + 1)
					+ "", 4111);
			break;

		case 10:
			sendFrame126("" + c.playerLevel[10] + "", 4032);
			sendFrame126("" + getLevelForXP(c.playerXP[10]) + "", 4033);
			sendFrame126("" + c.playerXP[10] + "", 4128);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[10]) + 1)
					+ "", 4129);
			break;

		case 11:
			sendFrame126("" + c.playerLevel[11] + "", 4036);
			sendFrame126("" + getLevelForXP(c.playerXP[11]) + "", 4037);
			sendFrame126("" + c.playerXP[11] + "", 4140);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[11]) + 1)
					+ "", 4141);
			break;

		case 12:
			sendFrame126("" + c.playerLevel[12] + "", 4024);
			sendFrame126("" + getLevelForXP(c.playerXP[12]) + "", 4025);
			sendFrame126("" + c.playerXP[12] + "", 4104);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[12]) + 1)
					+ "", 4105);
			break;

		case 13:
			sendFrame126("" + c.playerLevel[13] + "", 4030);
			sendFrame126("" + getLevelForXP(c.playerXP[13]) + "", 4031);
			sendFrame126("" + c.playerXP[13] + "", 4122);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[13]) + 1)
					+ "", 4123);
			break;

		case 14:
			sendFrame126("" + c.playerLevel[14] + "", 4028);
			sendFrame126("" + getLevelForXP(c.playerXP[14]) + "", 4029);
			sendFrame126("" + c.playerXP[14] + "", 4116);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[14]) + 1)
					+ "", 4117);
			break;

		case 15:
			sendFrame126("" + c.playerLevel[15] + "", 4020);
			sendFrame126("" + getLevelForXP(c.playerXP[15]) + "", 4021);
			sendFrame126("" + c.playerXP[15] + "", 4092);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[15]) + 1)
					+ "", 4093);
			break;

		case 16:
			sendFrame126("" + c.playerLevel[16] + "", 4018);
			sendFrame126("" + getLevelForXP(c.playerXP[16]) + "", 4019);
			sendFrame126("" + c.playerXP[16] + "", 4086);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[16]) + 1)
					+ "", 4087);
			break;

		case 17:
			sendFrame126("" + c.playerLevel[17] + "", 4022);
			sendFrame126("" + getLevelForXP(c.playerXP[17]) + "", 4023);
			sendFrame126("" + c.playerXP[17] + "", 4098);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[17]) + 1)
					+ "", 4099);
			break;

		case 18:
			sendFrame126("" + c.playerLevel[18] + "", 12166);
			sendFrame126("" + getLevelForXP(c.playerXP[18]) + "", 12167);
			sendFrame126("" + c.playerXP[18] + "", 12171);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[18]) + 1)
					+ "", 12172);
			break;

		case 19:
			sendFrame126("" + c.playerLevel[19] + "", 13926);
			sendFrame126("" + getLevelForXP(c.playerXP[19]) + "", 13927);
			sendFrame126("" + c.playerXP[19] + "", 13921);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[19]) + 1)
					+ "", 13922);
			break;

		case 20:
			sendFrame126("" + c.playerLevel[20] + "", 4152);
			sendFrame126("" + getLevelForXP(c.playerXP[20]) + "", 4153);
			sendFrame126("" + c.playerXP[20] + "", 4157);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[20]) + 1)
					+ "", 4159);
			break;
			
		}
	}

	public int getXPForLevel(int level) {
		int points = 0;
		int output = 0;

		for (int lvl = 1; lvl <= level; lvl++) {
			points += Math.floor((double) lvl + 300.0
					* Math.pow(2.0, (double) lvl / 7.0));
			if (lvl >= level)
				return output;
			output = (int) Math.floor(points / 4);
		}
		return 0;
	}

	public int getLevelForXP(int exp) {
		int points = 0;
		int output = 0;
		if (exp > 13034430)
			return 99;
		for (int lvl = 1; lvl <= 99; lvl++) {
			points += Math.floor((double) lvl + 300.0
					* Math.pow(2.0, (double) lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if (output >= exp) {
				return lvl;
			}
		}
		return 0;
	}
	
	public double getAgilityRunRestore() {
		return 2260 - (c.playerLevel[16] * 10);
	}

	public boolean addSkillXP(int amount, int skill){
		if (amount+c.playerXP[skill] < 0 || c.playerXP[skill] > 200000000) {
			if(c.playerXP[skill] > 200000000) {
				c.playerXP[skill] = 200000000;
			}
			return false;
		}
		if (c.playerEquipment[c.playerRing] == 24171) {
			amount *= Config.SERVER_EXP_BONUS * 2;
		} else {
			amount *= Config.SERVER_EXP_BONUS;
		}
		if (c.playerEquipment[c.playerRing] == 24172) {
			amount *= Config.SERVER_EXP_BONUS * 3;
		} else {
			amount *= Config.SERVER_EXP_BONUS;
		}
		if (c.playerEquipment[c.playerRing] == 24173) {
			amount *= Config.SERVER_EXP_BONUS * 4;
		} else {
			amount *= Config.SERVER_EXP_BONUS;
		}
		if(c.getRights().isRainbow()) {
			amount *= Config.SERVER_EXP_BONUS * 2;
		} else {
			amount *= Config.SERVER_EXP_BONUS;
		
		}
		if (c.playerEquipment[c.playerAmulet] == 295 || c.playerEquipment[c.playerRing] == 23910) {
			amount *= Config.SERVER_EXP_BONUS * 2;
		} else {
			amount *= Config.SERVER_EXP_BONUS;
		}
		int oldLevel = getLevelForXP(c.playerXP[skill]);
		c.playerXP[skill] += amount;
		if (oldLevel < getLevelForXP(c.playerXP[skill])) {
			if (c.playerLevel[skill] < c.getLevelForXP(c.playerXP[skill]) && skill != 3 && skill != 5)
				c.playerLevel[skill] = c.getLevelForXP(c.playerXP[skill]);
			levelUp(skill);
			//c.sendSound(Sound.LEVELUP, 100);
			c.gfx100(199);
			requestUpdates();
		}
		setSkillLevel(skill, c.playerLevel[skill], c.playerXP[skill]);
		refreshSkill(skill);
		return true;
	}

	public void resetBarrows() {
		c.barrowsNpcs[0][1] = 0;
		c.barrowsNpcs[1][1] = 0;
		c.barrowsNpcs[2][1] = 0;
		c.barrowsNpcs[3][1] = 0;
		c.barrowsNpcs[4][1] = 0;
		c.barrowsNpcs[5][1] = 0;
		c.barrowsKillCount = 0;
		c.randomCoffin = Misc.random(3) + 1;
	}

	public static int Barrows[] = { 4708, 4710, 4712, 4714, 4716, 4718, 4720,
			4722, 4724, 4726, 4728, 4730, 4732, 4734, 4736, 4738, 4745, 4747,
			4749, 4751, 4753, 4755, 4757, 4759 };
	public static int Runes[] = { 4740, 558, 560, 565 };
	public static int Pots[] = {};

	public int randomBarrows() {
		return Barrows[(int) (Math.random() * Barrows.length)];
	}

	public int randomRunes() {
		return Runes[(int) (Math.random() * Runes.length)];
	}

	public int randomPots() {
		return Pots[(int) (Math.random() * Pots.length)];
	}
	public boolean wearingCape(int cape) {
		int capes[] = {
		9747, 9748, 9750, 9751, 
		9753, 9754, 9756, 9757, 
		9759, 9760, 9762, 9763,
		9765, 9766, 9768, 9769,
		9771, 9772, 9774, 9775,
		9777, 9778, 9780, 9781,
		9783, 9784, 9786, 9787,
		9789, 9790, 9792, 9793,
		9795, 9796, 9798, 9799,
		9801, 9802, 9804, 9805,
		9807, 9808, 9810, 9811,
		10662, 9813 
		};
		for(int i = 0; i < capes.length; i++) {
		if(capes[i] == cape) {
		return true;
		}
		}
		return false;
		}

		public int skillcapeGfx(int cape) {
		int capeGfx[][] = {
		{9747, 823}, {9748, 823},
		{9750, 828}, {9751, 828},
		{9753, 824}, {9754, 824},
		{9756, 832}, {9757, 832},
		{9759, 829}, {9760, 829},
		{9762, 813}, {9763, 813},
		{9765, 817}, {9766, 817},
		{9768, 833}, {9769, 833},
		{9771, 830}, {9772, 830},
		{9774, 835}, {9775, 835},
		{9777, 826}, {9778, 826},
		{9780, 818}, {9781, 818},
		{9783, 812}, {9784, 812},
		{9786, 827}, {9787, 827},
		{9789, 820}, {9790, 820},
		{9792, 814}, {9793, 814},
		{9795, 815}, {9796, 815},
		{9798, 819}, {9799, 819},
		{9801, 821}, {9802, 821},
		{9804, 831}, {9805, 831},
		{9807, 822}, {9808, 822},
		{9810, 825}, {9811, 825},
		{10662, 816}, {9813, 816}
		};
		for(int i = 0; i < capeGfx.length; i++) {
		if(capeGfx[i][0] == cape) {
		return capeGfx[i][1];
		}
		}
		return -1;
		}

		public int skillcapeEmote(int cape) {
		int capeEmote[][] = {
		{9747, 4959}, {9748, 4959},
		{9750, 4981}, {9751, 4981},
		{9753, 4961}, {9754, 4961},
		{9756, 4973}, {9757, 4973},
		{9759, 4979}, {9760, 4979},
		{9762, 4939}, {9763, 4939},
		{9765, 4947}, {9766, 4947},
		{9768, 4971}, {9769, 4971},
		{9771, 4977}, {9772, 4977},
		{9774, 4969}, {9775, 4969},
		{9777, 4965}, {9778, 4965},
		{9780, 4949}, {9781, 4949},
		{9783, 4937}, {9784, 4937},
		{9786, 4967}, {9787, 4967},
		{9789, 4953}, {9790, 4953},
		{9792, 4941}, {9793, 4941},
		{9795, 4943}, {9796, 4943},
		{9798, 4951}, {9799, 4951},
		{9801, 4955}, {9802, 4955},
		{9804, 4975}, {9805, 4975},
		{9807, 4957}, {9808, 4957},
		{9810, 4963}, {9811, 4963},
		{10662, 4945}, {9813, 4945}
		};
		for(int i = 0; i < capeEmote.length; i++) {
		if(capeEmote[i][0] == cape) {
		return capeEmote[i][1];
		}
		}
		return -1;
		}
	/**
	 * Show an arrow icon on the selected c.
	 * 
	 * @Param i - Either 0 or 1; 1 is arrow, 0 is none.
	 * @Param j - The player/Npc that the arrow will be displayed above.
	 * @Param k - Keep this set as 0
	 * @Param l - Keep this set as 0
	 */
	public void drawHeadicon(int i, int j, int k, int l) {
		c.outStream.createFrame(254);
		c.outStream.writeByte(i);

		if (i == 1 || i == 10) {
			c.outStream.writeWord(j);
			c.outStream.writeWord(k);
			c.outStream.writeByte(l);
		} else {
			c.outStream.writeWord(k);
			c.outStream.writeWord(l);
			c.outStream.writeByte(j);
		}
	}

	public int getNpcId(int id) {
		for (int i = 0; i < NPCHandler.maxNPCs; i++) {
			if (NPCHandler.npcs[i] != null) {
				if (NPCHandler.npcs[i].index == id) {
					return i;
				}
			}
		}
		return -1;
	}

	public void removeObject(int x, int y) {
		object(-1, x, x, 10, 10);
	}

	private void objectToRemove(int X, int Y) {
		object(-1, X, Y, 10, 10);
	}

	private void objectToRemove2(int X, int Y) {
		object(-1, X, Y, -1, 0);
	}

	public void removeObjects() {
		objectToRemove(2638, 4688);
		objectToRemove2(2635, 4693);
		objectToRemove2(2634, 4693);
		objectToRemove2(3234, 3207);
		objectToRemove2(3233, 3207);
		objectToRemove2(3235, 3199);
		objectToRemove(2387, 3488);
		objectToRemove2(2463, 3176);
		objectToRemove2(2467, 3178);
	}
	

	public void handleGlory(int gloryId) {
		c.getDH().sendOption4("Edgeville", "Al Kharid", "Karamja", "Mage Bank");
		c.usingGlory = true;
	}

	public void resetVariables() {
		//c.getFishing().resetFishing();
		c.getCrafting().resetCrafting();
		c.usingGlory = false;
		c.smeltInterface = false;
		c.smeltType = 0;
		c.smeltAmount = 0;
		c.woodcut[0] = c.woodcut[1] = c.woodcut[2] = 0;
		c.mining[0] = c.mining[1] = c.mining[2] = 0;
	}

	public boolean inPitsWait() {
		return c.getX() <= 2404 && c.getX() >= 2394 && c.getY() <= 5175
				&& c.getY() >= 5169;
	}

	public void castleWarsObjects() {
		object(-1, 2373, 3119, -3, 10);
		object(-1, 2372, 3119, -3, 10);
	}

	public int antiFire() {
		int toReturn = 0;
		if (c.antiFirePot)
			toReturn++;
		if (c.playerEquipment[c.playerShield] == 1540 || c.prayerActive[12]
				|| c.playerEquipment[c.playerShield] == 11284)
			toReturn++;
		return toReturn;
	}

	public boolean checkForFlags() {
		int[][] itemsToCheck = { { 995, 100000000 }, { 35, 5 }, { 667, 5 },
				{ 2402, 5 }, { 746, 5 }, { 4151, 150 }, { 565, 100000 },
				{ 560, 100000 }, { 555, 300000 }, { 4827, 10 } };
		for (int j = 0; j < itemsToCheck.length; j++) {
			if (itemsToCheck[j][1] < c.getItems().getTotalCount(
					itemsToCheck[j][0]))
				return true;
		}
		return false;
	}

	public void addStarter() {
		c.start(new StarterDialogue());
	}

	public int getWearingAmount() {
		int count = 0;
		for (int j = 0; j < c.playerEquipment.length; j++) {
			if (c.playerEquipment[j] > 0)
				count++;
		}
		return count;
	}

	public void useOperate(int itemId) {
		Optional<DegradableItem> d = DegradableItem.forId(itemId);
		if (d.isPresent()) {
			Degrade.checkPercentage(c, itemId);
			return;
		}
		switch (itemId) {
		case 1712:
		case 1710:
		case 1708:
		case 1706:
			handleGlory(itemId);
			break;
		case 11283:
		case 11284:
			if (c.playerIndex > 0) {
				c.getCombat().handleDfs(c);
			} else if (c.npcIndex > 0) {
				c.getCombat().handleDfsNPC(c);
			}
			break;
		}
	}
	
	public void ditchJump(final Client c, final int x, final int y) {
		  c.getPA().walkTo(x ,y);
		  c.isRunning = false;
		  c.playerWalkIndex = 6132;
		  c.getPA().requestUpdates();
	 }
	 
	 public void resetDitchJump(final Client c) {
	  c.isRunning = true;
	  c.getPA().sendFrame36(173, 1);
	  c.getCombat().getPlayerAnimIndex(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
	  c.getPA().requestUpdates();
	 }

	public void getSpeared(int otherX, int otherY) {
		int x = c.absX - otherX;
		int y = c.absY - otherY;
		if (x > 0)
			x = 1;
		else if (x < 0)
			x = -1;
		if (y > 0)
			y = 1;
		else if (y < 0)
			y = -1;
		moveCheck(x, y);
	}

	public void moveCheck(int xMove, int yMove) {
		movePlayer(c.absX + xMove, c.absY + yMove, c.heightLevel);
	}

	public int findKiller() {
		int killer = c.index;
		int damage = 0;
		for (int j = 0; j < Config.MAX_PLAYERS; j++) {
			if (PlayerHandler.players[j] == null)
				continue;
			if (j == c.index)
				continue;
			if (c.goodDistance(c.absX, c.absY, PlayerHandler.players[j].absX,
					PlayerHandler.players[j].absY, 40)
					|| c.goodDistance(c.absX, c.absY + 9400,
							PlayerHandler.players[j].absX,
							PlayerHandler.players[j].absY, 40)
					|| c.goodDistance(c.absX, c.absY,
							PlayerHandler.players[j].absX,
							PlayerHandler.players[j].absY + 9400, 40))
				if (c.damageTaken[j] > damage) {
					damage = c.damageTaken[j];
					killer = j;
				}
		}
		return killer;
	}


	public void appendPoison(int damage) {
		if (System.currentTimeMillis() - c.lastPoisonSip > c.poisonImmune) {
			c.sendMessage("You have been poisoned.");
			c.poisonDamage = (byte) damage;
		}
	}

	public boolean checkForPlayer(int x, int y) {
		for (Player p : PlayerHandler.players) {
			if (p != null) {
				if (p.getX() == x && p.getY() == y)
					return true;
			}
		}
		return false;
	}

	public void checkPouch(int i) {
		if (i < 0)
			return;
		c.sendMessage("This pouch has " + c.pouches[i] + " rune ess in it.");
	}

	public void fillPouch(int i) {
		if (i < 0)
			return;
		int toAdd = c.POUCH_SIZE[i] - c.pouches[i];
		if (toAdd > c.getItems().getItemAmount(1436)) {
			toAdd = c.getItems().getItemAmount(1436);
		}
		if (toAdd > c.POUCH_SIZE[i] - c.pouches[i])
			toAdd = c.POUCH_SIZE[i] - c.pouches[i];
		if (toAdd > 0) {
			c.getItems().deleteItem(1436, toAdd);
			c.pouches[i] += toAdd;
		}
	}

	public void emptyPouch(int i) {
		if (i < 0)
			return;
		int toAdd = c.pouches[i];
		if (toAdd > c.getItems().freeSlots()) {
			toAdd = c.getItems().freeSlots();
		}
		if (toAdd > 0) {
			c.getItems().addItem(1436, toAdd);
			c.pouches[i] -= toAdd;
		}
	}

	public void fixAllBarrows() {
		int totalCost = 0;
		int cashAmount = c.getItems().getItemAmount(995);
		for (int j = 0; j < c.playerItems.length; j++) {
			boolean breakOut = false;
			for (int i = 0; i < c.getItems().brokenBarrows.length; i++) {
				if (c.playerItems[j] - 1 == c.getItems().brokenBarrows[i][1]) {
					if (totalCost + 80000 > cashAmount) {
						breakOut = true;
						c.sendMessage("You have run out of money.");
						break;
					} else {
						totalCost += 80000;
					}
					c.playerItems[j] = c.getItems().brokenBarrows[i][0] + 1;
				}
			}
			if (breakOut)
				break;
		}
		if (totalCost > 0)
			c.getItems().deleteItem(995, c.getItems().getItemSlot(995),
					totalCost);
	}
	 int givePet = Misc.random(200);

	public  void TzTokLottery() {
	    if (c.getItems().playerHasItem(6570)) {
	        c.getItems().deleteItem2(6570, 1);
	        if (givePet == 17) {
	            c.getItems().addItem(13225, 1);
	            c.sendMessage("TzTok-Jad noticed your sacrifice. You have been awarded x1 Jad Pet", 255);
	        } else {
	            c.sendMessage("Your sacrifice was not worth it. No pet for you", 255);
	        }
	    } else {
	    	c.sendMessage("You need an fire cape to do this.");
	    }
	}
	public void movePlayer(int x, int y) {
		movePlayer(x, y, c.heightLevel);
	}
	public void handleLoginText() {
	       c.getPA().sendFrame126("<col=3CB71E>Train", 181);
	        c.getPA().sendFrame126("<col=000000>Cape", 178);
	        c.getPA().sendFrame126("<col=3CB71E>Skillz", 175);
	        c.getPA().sendFrame126("<col=DD5C3E>Edge", 177);
	        c.getPA().sendFrame126("<col=3CB71E>Fish", 185);
	        c.getPA().sendFrame126("<col=ffff00>Hang", 186);
	        c.getPA().sendFrame126("<col=3CB71E>Thiev", 182);
	        c.getPA().sendFrame126("<col=3CB71E>Barrw", 173);
	        c.getPA().sendFrame126("<col=3CB71E>Shops", 179);
	        c.getPA().sendFrame126("<col=DD5C3E>WC", 187);
	        c.getPA().sendFrame126("<col=3CB71E>Naay", 176);
	        c.getPA().sendFrame126("", 13371);
	        c.getPA().sendFrame126("", 13372);
	        c.getPA().sendFrame126("", 13373);
	        c.getPA().sendFrame126("", 13374);
	        c.getPA().sendFrame126("", 13376);
	        c.getPA().sendFrame126("", 13378);
	        c.getPA().sendFrame126("", 13380);
	        c.getPA().sendFrame126("", 13381);
	        c.getPA().sendFrame126("", 13382);
	        c.getPA().sendFrame126("", 11102);
	        c.getPA().sendFrame126("", 13379);
	        c.getPA().sendFrame126("", 13377);
	        c.getPA().sendFrame126("", 13375);
	        c.getPA().sendFrame126("", 11103);

	        //bank name
	        c.getPA().sendFrame126("The Bank Of GodzHell Reborn", 5383);
	        //logout message
	        c.getPA().sendFrame126("<col=ffffff>When You Have Finished Playing.</col>", 2450);
	        c.getPA().sendFrame126("<col=DD5C3E>Godzhell Reborn</col><col=ffffff>, Use The</col>", 2451);
	        c.getPA().sendFrame126("<col=ffffff>Button Below To Log Out Safely.</col>", 2452); 
	        //home name
	        c.getPA().sendFrame126("<col=3CB71E>Home</col>", 180);
	        
	        
			//Main teleport interface//
			c.getPA().sendFrame126("<col=3CB71E>- Dungeon Teleports -</col>", 33337);
			c.getPA().sendFrame126("Edgeville Dungeon", 33338); 
			c.getPA().sendFrame126(" Brimhaven Dungeon", 33339);
			c.getPA().sendFrame126(" Relekka Dungeon", 33340);
			c.getPA().sendFrame126(" Taverly Dungeon", 33341);
			c.getPA().sendFrame126("<col=3CB71E>- Monster Teleports -", 33342);
			c.getPA().sendFrame126("Rock Crabs", 33343);
			c.getPA().sendFrame126("Slayer Tower", 33344);
			c.getPA().sendFrame126("Skeletal Wyverns", 33345);
			c.getPA().sendFrame126("Smoke Devils", 33346);
			c.getPA().sendFrame126("Ancient Cavern", 33347);
			c.getPA().sendFrame126("<col=3CB71E>- Boss Teleports -", 33348);
			c.getPA().sendFrame126("General Graardor (Bandos)", 33349);
			c.getPA().sendFrame126("Commander Zilyana (Sara)", 33350);
			c.getPA().sendFrame126("K'ril Tsutsaroth (Zamorak)", 33351);
			c.getPA().sendFrame126("Kree'arra (Armadyl)", 33352);
			c.getPA().sendFrame126("Dagannoth Kings", 33353);
			c.getPA().sendFrame126("Barrelchest <col=DD5C3E>(WILD)", 33354);
			c.getPA().sendFrame126("King Black Dragon <col=DD5C3E>(WILD)", 33355);
			c.getPA().sendFrame126("Kraken", 33356);
			c.getPA().sendFrame126("Corporeal Beast", 33357);
			c.getPA().sendFrame126("Lizardman Shaman", 33358);
			c.getPA().sendFrame126("Cerberus", 33359);
			c.getPA().sendFrame126("Giant Mole <col=DD5C3E>(WILD)", 33360);
			c.getPA().sendFrame126("Demonic Gorillas", 33361);
			c.getPA().sendFrame126("<col=DD5C3E>- Wildy Boss Teleports -", 33362);
			c.getPA().sendFrame126("Callisto", 33363);	
			c.getPA().sendFrame126("Venenatis (MULTI)", 33364);
			c.getPA().sendFrame126("Vet'ion (MULTI)", 33365);
			c.getPA().sendFrame126("Crazy Archaeologist", 33366);
			c.getPA().sendFrame126("Chaos Fanatic", 33367);
			c.getPA().sendFrame126("Chaos Elemental", 33368);
			c.getPA().sendFrame126("<col=DD5C3E>- Wilderness Teleports -", 33369);
			c.getPA().sendFrame126("Mage Bank (SAFE)", 33370);
			c.getPA().sendFrame126("Obelisks [44]", 33371);
			c.getPA().sendFrame126("Resource Area [54]", 33372);
			c.getPA().sendFrame126("East Dragons [20]", 33373);
			c.getPA().sendFrame126("Hill Giants (MULTI) [14]", 33374);
			c.getPA().sendFrame126("West Dragons [10]", 33375);
			c.getPA().sendFrame126("Lava Dragons (MULTI) [43]", 33376);	
			c.getPA().sendFrame126("<col=3CB71E>- Skilling Teleports -", 33377);
			c.getPA().sendFrame126("Click here to open interface", 33378);
			c.getPA().sendFrame126("<col=3CB71E>- Minigame Teleports -", 33379);
			c.getPA().sendFrame126("Barrows", 33380);
			c.getPA().sendFrame126("Duel Arena", 33381);
			c.getPA().sendFrame126("Fight Caves", 33382);
			c.getPA().sendFrame126("Fishing Tournament", 33383);
			c.getPA().sendFrame126("Warrior's Guild", 33384);
			c.getPA().sendFrame126("Pest Control", 33385);
			c.getPA().sendFrame126("<col=3CB71E>- Donator Teleports -", 33386);
			c.getPA().sendFrame126("Donator Zone", 33387);
			c.getPA().sendFrame126("Anglerfish Fishing", 33388);
			c.getPA().sendFrame126("Sand Crabs", 33389);
			c.getPA().sendFrame126("Tormented Demons", 33390);
			c.getPA().sendFrame126("<col=3CB71E>- New Boss Teleports -", 33391);
			c.getPA().sendFrame126("Zul-Andra", 33392);
			c.getPA().sendFrame126("Skotizo", 33393);
			c.getPA().sendFrame126("Kalphite Queen", 33394);
			c.getPA().sendFrame126("<col=3CB71E>- New Monster Teleports -", 33395);
			c.getPA().sendFrame126("Rune and Addy Dragons", 33396);
			c.getPA().sendFrame126("Inferno", 33397);
			c.getPA().sendFrame126("Catacombs of Kourend", 33398);
			c.getPA().sendFrame126("Wyvern cave", 33399);
			c.getPA().sendFrame126("Karuulm slayer dungeon", 33400);
			c.getPA().sendFrame126("Vorkath", 33401);
			c.getPA().sendFrame126("Kalphite Lair", 33402);
			c.getPA().sendFrame126("", 33403);
	}

	public void handleWeaponStyle() {
		if (c.fightMode == 0) {
			c.getPA().sendFrame36(43, c.fightMode);
		} else if (c.fightMode == 1) {
			c.getPA().sendFrame36(43, 3);
		} else if (c.fightMode == 2) {
			c.getPA().sendFrame36(43, 1);
		} else if (c.fightMode == 3) {
			c.getPA().sendFrame36(43, 2);
		}
	}

	public void removeAllItems() {
		for (int i = 0; i < c.playerItems.length; i++) {
			c.playerItems[i] = 0;
		}
		for (int i = 0; i < c.playerItemsN.length; i++) {
			c.playerItemsN[i] = 0;
		}
		c.getItems().resetItems(3214);
	}
	public void sendUpdateItems(int inventoryId, Item[] items) {
		c.outStream.createFrameVarSizeWord(53);
		c.outStream.writeWord(inventoryId);
		c.outStream.writeWord(items.length);
		for(int i = 0; i < items.length; i++) {
			if(c.playerItemsN[i] > 254) {
				c.outStream.writeByte(255);
				c.outStream.writeDWord_v2(c.playerItemsN[i]);
			} else {
				c.outStream.writeByte(c.playerItemsN[i]);
			}
			if(c.playerItems[i] > 0) {
				c.outStream.writeWordBigEndianA(c.playerItems[i] + 1);
			} else {
				c.outStream.writeWordBigEndianA(0);
			}
		}
		c.outStream.endFrameVarSizeWord();
		c.flushOutStream();
	}
	public void sendScreenFade(String text, int state, int seconds) {
		if (c == null || c.getOutStream() == null) {
			return;
		}
		if (seconds < 1 && state != 0) {
			throw new IllegalArgumentException("The amount of seconds cannot be less than one.");
		}
		c.getOutStream().createFrameVarSize(9);
		c.getOutStream().writeString(text);
		c.getOutStream().writeByte(state);
		c.getOutStream().writeByte(seconds);
		c.getOutStream().endFrameVarSize();
	}
	public void createPlayersObjectAnim(int X, int Y, int animationID, int tileObjectType, int orientation) {
		try {
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC(Y - (c.mapRegionY * 8));
			c.getOutStream().writeByteC(X - (c.mapRegionX * 8));
			int x = 0;
			int y = 0;
			c.getOutStream().createFrame(160);
			c.getOutStream().writeByteS(((x & 7) << 4) + (y & 7));// tiles
																		// away
																		// -
																		// could
																		// just
																		// send
																		// 0
			c.getOutStream().writeByteS((tileObjectType << 2) + (orientation & 3));
			c.getOutStream().writeWordA(animationID);// animation id
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public boolean wearingDharok(Player c) {
		return (c.playerEquipment[c.playerWeapon] == 4718 && c.playerEquipment[c.playerAmulet] == 12851
				&& c.playerEquipment[c.playerHat] == 4716 && c.playerEquipment[c.playerChest] == 4720
				&& c.playerEquipment[c.playerLegs] == 4722);
	}

	public boolean wearingVerac(Player c) {
		return (c.playerEquipment[c.playerWeapon] == 4755 && c.playerEquipment[c.playerAmulet] == 12851
				&& c.playerEquipment[c.playerHat] == 4753 && c.playerEquipment[c.playerChest] == 4757
				&& c.playerEquipment[c.playerLegs] == 4759);
	}

	public boolean wearingAhrim(Player c) {
		return (c.playerEquipment[c.playerWeapon] == 4710 && c.playerEquipment[c.playerAmulet] == 12851
				&& c.playerEquipment[c.playerHat] == 4708 && c.playerEquipment[c.playerChest] == 4712
				&& c.playerEquipment[c.playerLegs] == 4714);
	}

	public boolean wearingTorag(Player c) {
		return (c.playerEquipment[c.playerWeapon] == 4747 && c.playerEquipment[c.playerAmulet] == 12851
				&& c.playerEquipment[c.playerHat] == 4745 && c.playerEquipment[c.playerChest] == 4749
				&& c.playerEquipment[c.playerLegs] == 4751);
	}

	public boolean wearingGuthan(Player c) {
		return (c.playerEquipment[c.playerWeapon] == 4726 && c.playerEquipment[c.playerAmulet] == 12851
				&& c.playerEquipment[c.playerHat] == 4724 && c.playerEquipment[c.playerChest] == 4728
				&& c.playerEquipment[c.playerLegs] == 4730 && c.playerEquipment[c.playerWeapon] == 23994 
				&& c.playerEquipment[c.playerHat] == 23993 && c.playerEquipment[c.playerChest] == 23995
				&& c.playerEquipment[c.playerLegs] == 23996);
	}

	public boolean wearingKaril(Player c) {
		return (c.playerEquipment[c.playerWeapon] == 4734 && c.playerEquipment[c.playerAmulet] == 12851
				&& c.playerEquipment[c.playerHat] == 4732 && c.playerEquipment[c.playerChest] == 4736
				&& c.playerEquipment[c.playerLegs] == 4738);
	}

	public boolean corpSpear() {
		return (c.playerEquipment[c.playerWeapon] == 11824
				);
	}

	public boolean Keris() {
		return (c.playerEquipment[c.playerWeapon] == 10581);
	}
	public void shakeScreen(int verticleAmount, int verticleSpeed, int horizontalAmount, int horizontalSpeed) {
		if (c != null && c.getOutStream() != null) {
			c.outStream.createFrame(35);
			c.outStream.writeByte(verticleAmount);
			c.outStream.writeByte(verticleSpeed);
			c.outStream.writeByte(horizontalAmount);
			c.outStream.writeByte(horizontalSpeed);
		}
	}

	public void resetShaking() {
		shakeScreen(1, 1, 1, 1);
	}
	/**
	 * Open bank
	 **/
	public void sendFrame34a(int frame, int item, int slot, int amount) {
		c.outStream.createFrameVarSizeWord(34);
		c.outStream.writeWord(frame);
		c.outStream.writeByte(slot);
		c.outStream.writeWord(item + 1);
		c.outStream.writeByte(255);
		c.outStream.writeDWord(amount);
		c.outStream.endFrameVarSizeWord();
	}
	public boolean addSkillXP(double amount, int skill) {
		return addSkillXP((int) amount, skill);
	}
	public int backupItems[] = new int[Config.BANK_SIZE];
	public int backupItemsN[] = new int[Config.BANK_SIZE];



		public void sendFriend(long friend, int world) {
			c.getOutStream().createFrame(50);
			c.getOutStream().writeQWord(friend);
			c.getOutStream().writeByte(world);

		}
		public int totalLevel() {
			int total = 0;
			for (int i = 0; i <= 20; i++) {
				total += getLevelForXP(c.playerXP[i]);
			}
			return total;
		}

		public int xpTotal() {
			int xp = 0;
			for (int i = 0; i <= 20; i++) {
				xp += c.playerXP[i];
			}
			return xp;
		}
		public void sendStatement(String s) {
			sendFrame126(s, 357);
			sendFrame126("Click here to continue", 358);
			sendChatInterface(356);
		}
		public void itemOnInterface(int interfaceChild, int zoom, int itemId) {
			if (c.getOutStream() != null && c != null) {
				c.getOutStream().createFrame(246);
				c.getOutStream().writeWordBigEndian(interfaceChild);
				c.getOutStream().writeWord(zoom);
				c.getOutStream().writeWord(itemId);
				c.flushOutStream();
			}
		}
		public void openItemsKeptOnDeath() {
			ArrayList<List<GameItem>> items = c.getItems().getItemsKeptOnDeath();

			List<GameItem> lostItems = items.get(0);
			List<GameItem> keptItems = items.get(1);

			int keptCount = c.getItems().getKeptItemsCount();
			if (keptCount > keptItems.size()) {
				keptCount = keptItems.size();
			}

			for (int i = 0; i < 4; i++) {
				int id = keptCount > i ? keptItems.get(i).getId() : -1;
				sendFrame34a(10494, id, i, 1);
			}

			for (int i = 0; i < 64; i++) {
				int id = lostItems.size() > i ? lostItems.get(i).getId() : -1;
				int amount = lostItems.size() > i ? lostItems.get(i).getAmount() : 1;
				sendFrame34a(10600, id, i, amount);
			}

			sendFrame126("Ghreborn - Items Kept on Death", 17103);
			sendKeptItemsInformation();

			showInterface(17100);
		}
		public void sendKeptItemsInformation() {
			for (int i = 17109; i < 17131; i++) {
				sendFrame126("", i);
			}

			int count = c.getItems().getKeptItemsCount();

			sendFrame126("Items you will keep on death:", 17104);
			sendFrame126("Items you will lose on death:", 17105);
			sendFrame126("Player Information", 17106);
			sendFrame126("Max items kept on death:", 17107);
			sendFrame126("~ " + count + " ~", 17108);
			sendFrame126("The normal amount of", 17111);
			sendFrame126("items kept is three.", 17112);

			if (count < 2) {
				sendFrame126("You're marked with a", 17111);
				sendFrame126("<col=DD5C3E>skull. @lre@This reduces the", 17112);
				sendFrame126("items you keep from", 17113);
				sendFrame126("three to zero!", 17114);
				if (count > 0) {
					sendFrame126("However, you also have", 17115);
					sendFrame126("the <col=DD5C3E>Protect @lre@Items prayer", 17116);
					sendFrame126("active, which saves you", 17117);
					sendFrame126("one extra item!", 17118);
				}
			} else if (count < 4) {
				sendFrame126("You have no factors", 17111);
				sendFrame126("affecting the items you", 17112);
				sendFrame126("keep.", 17113);
			} else {
				sendFrame126("You have the <col=DD5C3E>Protect", 17111);
				sendFrame126("<col=DD5C3E>Item @lre@prayer active,", 17112);
				sendFrame126("which saves you one", 17113);
				sendFrame126("extra item!", 17114);
			}
		}
		public void createPlayersProjectile(int x, int y, int offX, int offY,
				int angle, int speed, int gfxMoving, int startHeight,
				int endHeight, int lockon, int time, int slope, int lol) {
			for (int i = 0; i < Config.MAX_PLAYERS; i++) {
				Client p = (Client) PlayerHandler.players[i];
				if (p != null) {
					Client person = (Client) p;
					if (person != null) {
						if (person.getOutStream() != null) {
							if (person.distanceToPoint(x, y) <= 25) {
								if (p.heightLevel == c.heightLevel)
									person.getPA().createProjectile(x, y, offX,
											offY, angle, speed, gfxMoving,
											startHeight, endHeight, lockon, time,
											slope, lol);
							}
						}
					}
				}
			}
		}
		public void createProjectile(int x, int y, int offX, int offY, int angle,
				int speed, int gfxMoving, int startHeight, int endHeight,
				int lockon, int time, int slope, int lol) {

			if (c.getOutStream() != null && c != null) {
				c.getOutStream().createFrame(85);
				c.getOutStream().writeByteC((y - (c.getMapRegionY() * 8)) - 2);
				c.getOutStream().writeByteC((x - (c.getMapRegionX() * 8)) - 3);
				c.getOutStream().createFrame(117);
				c.getOutStream().writeByte(angle);
				c.getOutStream().writeByte(offY);
				c.getOutStream().writeByte(offX);
				c.getOutStream().writeWord(lockon);
				c.getOutStream().writeWord(gfxMoving);
				c.getOutStream().writeByte(startHeight);
				c.getOutStream().writeByte(endHeight);
				c.getOutStream().writeWord(time);
				c.getOutStream().writeWord(speed);
				c.getOutStream().writeByte(slope);
				c.getOutStream().writeByte(lol);
				c.flushOutStream();
			}
		}
		private static int amount;
		public void startFillvial(Client c){
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if (c == null || c.disconnected || c.getSession() == null) {
						stop();
						return;
					}
					if (!c.playerIsCrafting) {
						if (amount == 0) {
							container.stop();
							return;
						}
						if (!c.getItems().playerHasItem(229, 1)) {
							//c.sendMessage("You have run out of molten glass.");
							container.stop();
							return;
						}
						c.getItems().deleteItem(229, 1);
						c.getItems().addItem(228, 1);
						//c.sendMessage("You fill the " + Item.getItemName(229) + ".");
						//c.getPA().addSkillXP(g.getXP() * Config.CRAFTING_EXPERIENCE, 12);
						//c.animation(884);
						amount--;
					} else {
						container.stop();
					}
				}
				@Override
				public void stop() {
					c.stopAnimation();
					//c.playerIsCrafting = false;
				}
			}, 3);
			}
		public boolean salveAmulet() {
			return (c.playerEquipment[c.playerAmulet] == 4081);
		}
		public void sendString(final String s, final int id) {
			if (c.getOutStream() != null && c != null) {
				c.getOutStream().createFrameVarSizeWord(126);
				c.getOutStream().writeString(s);
				c.getOutStream().writeWordA(id);
				c.getOutStream().endFrameVarSizeWord();
				c.flushOutStream();
			}

		}
		public void stillCamera(int x, int y, int height, int speed, int angle) {
			c.outStream.createFrame(177);
			c.outStream.writeByte(x / 64);
			c.outStream.writeByte(y / 64);
			c.outStream.writeWord(height);
			c.outStream.writeByte(speed);
			c.outStream.writeByte(angle);
		}

		public void spinCamera(int i1, int i2, int i3, int i4, int i5) {
			c.outStream.createFrame(166);
			c.outStream.writeByte(i1);
			c.outStream.writeByte(i2);
			c.outStream.writeWord(i3);
			c.outStream.writeByte(i4);
			c.outStream.writeByte(i5);
		}

		public void resetCamera() {
			c.outStream.createFrame(107);
			c.updateRequired = true;
			c.appearanceUpdateRequired = true;
		}
	    public void sendPlayerObjectAnimation(Client player, int x, int y, int animation, int type, int orientation, int height) {
            if (player == null)
                return;
            // if (p.getPosition().isViewableFrom(position)) {
            player.getOutStream().createFrame(85);
            player.getOutStream().writeByteC(y - (player.mapRegionY * 8));
            player.getOutStream().writeByteC(x - (player.mapRegionX * 8));
            player.getOutStream().createFrame(160);
            player.getOutStream().writeByteS(((0 & 7) << 4) + (0 & 7));
            player.getOutStream().writeByteS((type << 2) + (orientation & 3));
            player.getOutStream().writeWordA(animation);
            // }
        }
		public void addEaster() {
				c.getItems().addItemToBank(1961, 5);
				c.getItems().addItemToBank(21214, 1);
				c.getItems().addItemToBank(4565, 1);
				c.getItems().addItemToBank(4566, 1);
				c.getItems().addItemToBank(7927, 1);
				c.getItems().addItemToBank(11021, 1);
				c.getItems().addItemToBank(11020, 1);
				c.getItems().addItemToBank(11022, 1);
				c.getItems().addItemToBank(11019, 1);
				c.getItems().addItemToBank(1037, 1);
				c.getItems().addItemToBank(13182, 1);
				c.getItems().addItemToBank(13663, 1);
				c.getItems().addItemToBank(13664, 1);
				c.getItems().addItemToBank(13665, 1);
				c.sendMessage("Some easter items has been added to your bank.");
				
			
		
		}
		/**
		 * 
		 * @return: Gets players Clan
		 */
		public Clan getClan() {
			if (Server.clanManager.clanExists(c.playerName)) {
				return Server.clanManager.getClan(c.playerName);
			}
			return null;
		}

		/*
		 * public void sendClan(String name, String message, String clan, int
		 * rights) { c.outStream.createFrameVarSizeWord(217);
		 * c.outStream.writeString(name);
		 * c.outStream.writeString(Misc.formatPlayerName(message));
		 * c.outStream.writeString(clan); c.outStream.writeWord(rights);
		 * c.outStream.endFrameVarSize(); }
		 */

		public void sendClan(String name, String message, String clan, int rights) {
			c.outStream.createFrameVarSizeWord(217);
			c.outStream.writeString(name);
			c.outStream.writeString(Misc.formatPlayerName(message));
			c.outStream.writeString(clan);
			c.outStream.writeWord(rights);
			c.outStream.endFrameVarSize();
		}

		public void clearClanChat() {
			c.clanId = -1;
			c.getPA().sendFrame126("Talking in: ", 18139);
			c.getPA().sendFrame126("Owner: ", 18140);
			for (int j = 18144; j < 18244; j++) {
				c.getPA().sendFrame126("", j);
			}
		}

		/**
		 * Sets the clan information for the player's clan.
		 */
		public void setClanData() {
			boolean exists = Server.clanManager.clanExists(c.playerName);
			if (!exists || c.clan == null) {
				sendFrame126("Join chat", 18135);
				sendFrame126("Talking in: Not in chat", 18139);
				sendFrame126("Owner: None", 18140);
			}
			if (!exists) {
				sendFrame126("Chat Disabled", 18306);
				String title = "";
				for (int id = 18307; id < 18317; id += 3) {
					if (id == 18307) {
						title = "Anyone";
					} else if (id == 18310) {
						title = "Anyone";
					} else if (id == 18313) {
						title = "General+";
					} else if (id == 18316) {
						title = "Only Me";
					}
					sendFrame126(title, id + 2);
				}
				for (int index = 0; index < 100; index++) {
					sendFrame126("", 18323 + index);
				}
				for (int index = 0; index < 100; index++) {
					sendFrame126("", 18424 + index);
				}
				return;
			}
			Clan clan = Server.clanManager.getClan(c.playerName);
			sendFrame126(clan.getTitle(), 18306);
			String title = "";
			for (int id = 18307; id < 18317; id += 3) {
				if (id == 18307) {
					title = clan.getRankTitle(clan.whoCanJoin)
							+ (clan.whoCanJoin > Clan.Rank.ANYONE && clan.whoCanJoin < Clan.Rank.OWNER ? "+" : "");
				} else if (id == 18310) {
					title = clan.getRankTitle(clan.whoCanTalk)
							+ (clan.whoCanTalk > Clan.Rank.ANYONE && clan.whoCanTalk < Clan.Rank.OWNER ? "+" : "");
				} else if (id == 18313) {
					title = clan.getRankTitle(clan.whoCanKick)
							+ (clan.whoCanKick > Clan.Rank.ANYONE && clan.whoCanKick < Clan.Rank.OWNER ? "+" : "");
				} else if (id == 18316) {
					title = clan.getRankTitle(clan.whoCanBan)
							+ (clan.whoCanBan > Clan.Rank.ANYONE && clan.whoCanBan < Clan.Rank.OWNER ? "+" : "");
				}
				sendFrame126(title, id + 2);
			}
			if (clan.rankedMembers != null) {
				for (int index = 0; index < 100; index++) {
					if (index < clan.rankedMembers.size()) {
						sendFrame126("<clan=" + clan.ranks.get(index) + ">" + clan.rankedMembers.get(index), 18323 + index);
					} else {
						sendFrame126("", 18323 + index);
					}
				}
			}
			if (clan.bannedMembers != null) {
				for (int index = 0; index < 100; index++) {
					if (index < clan.bannedMembers.size()) {
						sendFrame126(clan.bannedMembers.get(index), 18424 + index);
					} else {
						sendFrame126("", 18424 + index);
					}
				}
			}
		}
		public void itemOnInterface(int item, int amount, int frame, int slot) {
			c.outStream.createFrameVarSizeWord(34);
			c.outStream.writeWord(frame);
			c.outStream.writeByte(slot);
			c.outStream.writeWord(item + 1);
			c.outStream.writeByte(255);
			c.outStream.writeDWord(amount);
			c.outStream.endFrameVarSizeWord();
		}
		public void movePlayer(Coordinate coord) {
			movePlayer(coord.getX(), coord.getY(), coord.getH());
		}
		public void sendFrame34P2(int item, int slot, int frame, int amount) {
			c.outStream.createFrameVarSizeWord(34);
			c.outStream.writeWord(frame);
			c.outStream.writeByte(slot);
			c.outStream.writeWord(item + 1);
			c.outStream.writeByte(255);
			c.outStream.writeDWord(amount);
			c.outStream.endFrameVarSizeWord();
		}
		public static void noteItems(Client player, int item) {
			ItemDefinition definition = ItemDefinition.forId(item);
			ItemDefinition notedDefinition = ItemDefinition.forId(item + 1);
			if (definition == null || notedDefinition == null
					|| !notedDefinition.getName().contains(definition.getName())) {
				player.sendMessage("You cannot note this item, it is unnotable.");
				return;
			}
			if (!player.getItems().playerHasItem(item, 1)) {
				return;
			}
			for (int index = 0; index < player.playerItems.length; index++) {
				int amount = player.playerItemsN[index];
				if (player.playerItems[index] == item + 1 && amount > 0) {
					player.getItems().deleteItem(item, index, amount);
					player.getItems().addItem(item + 1, amount);
				}
			}
			player.getDH().sendStatement("You note all your " + definition.getName() + ".");
			player.nextChat = -1;
		}











}
