package Ghreborn.model.players;

import Ghreborn.Server;
import Ghreborn.model.content.dailytasks.DailyTasks;
import Ghreborn.model.npcs.NPC;
import Ghreborn.model.npcs.NPCHandler;

public class DialogueHandler {

	private Client c;

	public DialogueHandler(Client client) {
		this.c = client;
	}
	private int getChatAnim(ChatEmotes ce) {
		return ChatEmotes.getAnimId(ce);
	}
	/**
	 * Handles all talking
	 * 
	 * @param dialogue
	 *            The dialogue you want to use
	 * @param npcId
	 *            The npc id that the chat will focus on during the chat
	 */
	public void sendDialogues(int dialogue, int npcId) {
		c.talkingNpc = npcId;
		switch (dialogue) {
		case 0:
			c.talkingNpc = -1;
			c.getPA().removeAllWindows();
			c.nextChat = 0;
			break;
		case 1:
			sendStatement("You found a hidden tunnel! Do you want to enter it?");
			c.dialogueAction = 1;
			c.nextChat = 2;
			break;
		case 2:
			sendOption2("Yea! I'm fearless!", "No way! That looks scary!");
			c.dialogueAction = 1;
			c.nextChat = 0;
			break;
		case 3:
			sendNpcChat4(
					"Hello!",
					"My name is Duradel and I am a master of the slayer skill.",
					"I can assign you a slayer task suitable to your combat level.",
					"Would you like a slayer task?", c.talkingNpc, "Duradel", ChatEmotes.HAPPY_JOYFUL);
			c.nextChat = 4;
			break;
		case 5:
			sendNpcChat4("Hello adventurer...",
					"My name is Kolodion, the master of this mage bank.",
					"Would you like to play a minigame in order ",
					"to earn points towards recieving magic related prizes?",
					c.talkingNpc, "Kolodion", ChatEmotes.HAPPY_JOYFUL);
			c.nextChat = 6;
			break;
		case 6:
			sendNpcChat4("The way the game works is as follows...",
					"You will be teleported to the wilderness,",
					"You must kill mages to recieve points,",
					"redeem points with the chamber guardian.", c.talkingNpc,
					"Kolodion", ChatEmotes.HAPPY_JOYFUL);
			c.nextChat = 15;
			break;
		case 11:
			sendNpcChat4(
					"Hello!",
					"My name is Duradel and I am a master of the slayer skill.",
					"I can assign you a slayer task suitable to your combat level.",
					"Would you like a slayer task?", c.talkingNpc, "Duradel", ChatEmotes.HAPPY_JOYFUL);
			c.nextChat = 12;
			break;
		case 12:
			sendOption2("Yes I would like a slayer task.",
					"No I would not like a slayer task.");
			c.dialogueAction = 5;
			break;
		case 13:
			sendNpcChat4(
					"Hello!",
					"My name is Duradel and I am a master of the slayer skill.",
					"I see I have already assigned you a task to complete.",
					"Would you like me to give you an easier task?",
					c.talkingNpc, "Duradel", ChatEmotes.HAPPY_JOYFUL);
			c.nextChat = 14;
			break;
		case 14:
			sendOption2("Yes I would like an easier task.",
					"No I would like to keep my task.");
			c.dialogueAction = 6;
			break;
		case 15:
			sendOption2("Yes I would like to play",
					"No, sounds too dangerous for me.");
			c.dialogueAction = 7;
			break;
		case 16:
			sendOption2("I would like to reset my barrows brothers.",
					"I would like to fix all my barrows");
			c.dialogueAction = 8;
			break;
		case 17:
			sendOption5("Air", "Mind", "Water", "Earth", "More");
			c.dialogueAction = 10;
			c.dialogueId = 17;
			c.teleAction = -1;
			break;
		case 18:
			sendOption5("Fire", "Body", "Cosmic", "Astral", "More");
			c.dialogueAction = 11;
			c.dialogueId = 18;
			c.teleAction = -1;
			break;
		case 19:
			sendOption5("Nature", "Law", "Death", "Blood", "More");
			c.dialogueAction = 12;
			c.dialogueId = 19;
			c.teleAction = -1;
			break;
			
		case 908:
			sendPlayerChat1("Hello there kitty!", ChatEmotes.HAPPY_JOYFUL);
			c.nextChat = 909;	
			break;
		case 909:
			sendNpcChat1("Meeeooow.", npcId, Server.npcHandler.getNpcName(npcId), ChatEmotes.DEFAULT);
			c.nextChat = 0;	
			break;
			
		case 910:
			sendOption3("Pet", "Catch Rat", "Shoo Away");
			c.dialogueAction = 222;
			c.nextChat = 0;	
			break;
		case 911:
			sendStatement("You pet your cat.");
			Server.npcHandler.startAnimation(9166, c.rememberNpcIndex);
			c.animation(9087);
			Server.npcHandler.npcs[c.rememberNpcIndex].forceChat("Meow!");
			c.nextChat = 0;	
			break;
		case 912:
			//Server.npcHandler.catchRat(c.rememberNpcIndex);
			c.getPA().removeAllWindows();
			c.nextChat = 0;	
			break;
		case 913:
			sendStatement("You shoo your cat away.");
			if(Server.npcHandler.npcs[c.rememberNpcIndex].npcType >= 761 && Server.npcHandler.npcs[c.rememberNpcIndex].npcType >= 766)
				c.ratsCaught = 0;
			Server.npcHandler.npcs[c.rememberNpcIndex].absX = 0;
			Server.npcHandler.npcs[c.rememberNpcIndex].absY = 0;
			Server.npcHandler.npcs[c.rememberNpcIndex] = null;
			c.summonId = 0;
			c.hasNpc = false;
			c.nextChat = 0;	
			break;
		case 900:
			sendNpcChat4("Hello! Would you like to enable Daily Tasks?", "These tasks can be assigned and completed", "once a day for some pretty neat rewards!", "Enable Daily Tasks?", 1909, "Daily Task Master", ChatEmotes.HAPPY_JOYFUL);
			c.nextChat = 901;
			break;
		case 901:
			sendOption2("Yes, enable daily tasks", "No, disable daily tasks.");
			c.dialogueAction = 901;
			break;
		
		case 902:
			sendNpcChat2("Please select your preference on the type", "of tasks you would like to receive!", 1909, "Daily Task Master", ChatEmotes.HAPPY_JOYFUL);
			c.nextChat = 903;
			break;
		case 903:
			sendOption2("Choose PvM Related Task", "Choose Skilling Related Task");
			c.dialogueAction = 903;
			break;
			
		case 904:
			sendNpcChat2("You have enabled daily tasks! Please", "press continue to select a task preference.", 1909, "Daily Task Master", ChatEmotes.HAPPY_JOYFUL);
			c.nextChat = 902;
			break;
		case 905:
			sendNpcChat2("You have disabled daily tasks! Talk to", "me if you change your mind.", 1909, "Daily Task Master", ChatEmotes.HAPPY_JOYFUL);
			c.nextChat = 0;
			break;
			
		case 906:
			sendNpcChat2("Great, your tasks will now be PvM related", "until you change your mind!", 1909, "Daily Task Master", ChatEmotes.HAPPY_JOYFUL);
			c.nextChat = 0;
			DailyTasks.complete(c);
			DailyTasks.assignTask(c);
			break;
		case 907:
			sendNpcChat2("Great, your tasks will now be Skilling related", "until you change your mind!", 1909, "Daily Task Master", ChatEmotes.HAPPY_JOYFUL);
			c.nextChat = 0;
			DailyTasks.complete(c);
			DailyTasks.assignTask(c);
			break;
		case 914:
			sendStatement("What spell book do u want to switch to?");
			c.nextChat = 915;
			break;
		case 915:
			sendOption3("Regular spellbook", "Ancient Magicks", "Lunar spellbook");
			c.dialogueAction = 915;
			break;
		}
	}

	/*
	 * Information Box
	 */
	
	/*public String GetNpcName(int NpcID) {
		for (int i = 0; i < NPCHandler.maxListedNPCs; i++) {
			if (NPCHandler.NpcList[i] != null) {
				if (NPCHandler.NpcList[i].npcId == NpcID) {
					return NPCHandler.NpcList[i].npcName;
				}
			}
		}
		return "NPC Not Listed" + NpcID;
	}*/

	private void sendOption3(String s, String s1, String s2) {
		c.getPA().sendFrame126("Select an Option", 2470);
		c.getPA().sendFrame126(s, 2471);
		c.getPA().sendFrame126(s1, 2472);
		c.getPA().sendFrame126(s2, 2473);
		c.getPA().sendChatInterface(2469);
	}
	public void sendStartInfo(String text, String text1, String text2,
			String text3, String title) {
		c.getPA().sendFrame126(title, 6180);
		c.getPA().sendFrame126(text, 6181);
		c.getPA().sendFrame126(text1, 6182);
		c.getPA().sendFrame126(text2, 6183);
		c.getPA().sendFrame126(text3, 6184);
		c.getPA().sendChatInterface(6179);
	}

	/*
	 * Options
	 */

	public void sendOption2(String s, String s1) {
		c.getPA().sendFrame126("Select an Option", 2460);
		c.getPA().sendFrame126(s, 2461);
		c.getPA().sendFrame126(s1, 2462);
		c.getPA().sendChatInterface(2459);
	}

	public void sendOption4(String s, String s1, String s2, String s3) {
		c.getPA().sendFrame126("Select an Option", 2481);
		c.getPA().sendFrame126(s, 2482);
		c.getPA().sendFrame126(s1, 2483);
		c.getPA().sendFrame126(s2, 2484);
		c.getPA().sendFrame126(s3, 2485);
		c.getPA().sendChatInterface(2480);
	}

	public void sendOption5(String s, String s1, String s2, String s3, String s4) {
		c.getPA().sendFrame126("Select an Option", 2493);
		c.getPA().sendFrame126(s, 2494);
		c.getPA().sendFrame126(s1, 2495);
		c.getPA().sendFrame126(s2, 2496);
		c.getPA().sendFrame126(s3, 2497);
		c.getPA().sendFrame126(s4, 2498);
		c.getPA().sendChatInterface(2492);
	}

	/*
	 * Statements
	 */
	/**
	 * Statements.
	 */
	public void sendStatement(String[] lines) {
		switch (lines.length) {
			case 1 :
				sendStatement(lines[0]);
				break;
			case 2 :
				sendStatement(lines[0], lines[1]);
				break;
			case 3 :
				sendStatement(lines[0], lines[1], lines[2]);
				break;
			case 4 :
				sendStatement(lines[0], lines[1], lines[2], lines[3]);
				break;
			case 5 :
				sendStatement(lines[0], lines[1], lines[2], lines[3], lines[4]);
				break;
		}
	}

	public void sendStatement(String line1) {
		c.getPA().sendFrame126(line1, 357);
		c.getPA().sendChatInterface(356);
	}


	public void sendStatement(String line1, String line2) {
		c.getPA().sendFrame126(line1, 360);
		c.getPA().sendFrame126(line2, 361);
		c.getPA().sendChatInterface(359);
	}

	public void sendStatement(String line1, String line2, String line3) {
		c.getPA().sendFrame126(line1, 364);
		c.getPA().sendFrame126(line2, 365);
		c.getPA().sendFrame126(line3, 366);
		c.getPA().sendChatInterface(363);
	}

	public void sendStatement(String line1, String line2, String line3, String line4) {
		c.getPA().sendFrame126(line1, 369);
		c.getPA().sendFrame126(line2, 370);
		c.getPA().sendFrame126(line3, 371);
		c.getPA().sendFrame126(line4, 372);
		c.getPA().sendChatInterface(368);
	}

	public void sendStatement(String line1, String line2, String line3, String line4, String line5) {
		c.getPA().sendFrame126(line1, 375);
		c.getPA().sendFrame126(line2, 376);
		c.getPA().sendFrame126(line3, 377);
		c.getPA().sendFrame126(line4, 378);
		c.getPA().sendFrame126(line5, 379);
		c.getPA().sendChatInterface(374);
	}

	/*
	 * Npc Chatting
	 */

	private void sendNpcChat1(String s, int ChatNpc, String name, ChatEmotes ce) {
		c.getPA().sendFrame200(4883, getChatAnim(ce));
		c.getPA().sendFrame126(name, 4884);
		c.getPA().sendFrame126(s, 4885);
		c.getPA().sendFrame75(ChatNpc, 4883);
		c.getPA().sendChatInterface(4882);
	}
	
	private void sendNpcChat2(String s, String s1, int ChatNpc, String name, ChatEmotes ce) {
		c.getPA().sendFrame200(4888, getChatAnim(ce));
		c.getPA().sendFrame126(name, 4889);
		c.getPA().sendFrame126(s, 4890);
		c.getPA().sendFrame126(s1, 4891);
		c.getPA().sendFrame75(ChatNpc, 4888);
		c.getPA().sendChatInterface(4887);
	}

	private void sendNpcChat3(String s, String s1, String s2, int ChatNpc, String name, ChatEmotes ce) {
		c.getPA().sendFrame200(4894, getChatAnim(ce));
		c.getPA().sendFrame126(name, 4895);
		c.getPA().sendFrame126(s, 4896);
		c.getPA().sendFrame126(s1, 4897);
		c.getPA().sendFrame126(s2, 4898);
		c.getPA().sendFrame75(ChatNpc, 4894);
		c.getPA().sendChatInterface(4893);
	}
	
	private void sendNpcChat4(String s, String s1, String s2, String s3, int ChatNpc, String name, ChatEmotes ce) {
		c.getPA().sendFrame200(4901, getChatAnim(ce));
		c.getPA().sendFrame126(name, 4902);
		c.getPA().sendFrame126(s, 4903);
		c.getPA().sendFrame126(s1, 4904);
		c.getPA().sendFrame126(s2, 4905);
		c.getPA().sendFrame126(s3, 4906);
		c.getPA().sendFrame75(ChatNpc, 4901);
		c.getPA().sendChatInterface(4900);
	}
	
	/*
	 * Player Chatting Back
	 */
	
	private void sendPlayerChat1(String s, ChatEmotes ce) {
		c.getPA().sendFrame200(969, getChatAnim(ce));
		c.getPA().sendFrame126(c.playerName, 970);
		c.getPA().sendFrame126(s, 971);
		c.getPA().sendFrame185(969);
		c.getPA().sendChatInterface(968);
	}
	
	private void sendPlayerChat2(String s, String s1, ChatEmotes ce) {
		c.getPA().sendFrame200(974, getChatAnim(ce));
		c.getPA().sendFrame126(c.playerName, 975);
		c.getPA().sendFrame126(s, 976);
		c.getPA().sendFrame126(s1, 977);
		c.getPA().sendFrame185(974);
		c.getPA().sendChatInterface(973);
	}
	
	private void sendPlayerChat3(String s, String s1, String s2, ChatEmotes ce) {
		c.getPA().sendFrame200(980, getChatAnim(ce));
		c.getPA().sendFrame126(c.playerName, 981);
		c.getPA().sendFrame126(s, 982);
		c.getPA().sendFrame126(s1, 983);
		c.getPA().sendFrame126(s2, 984);
		c.getPA().sendFrame185(980);
		c.getPA().sendChatInterface(979);
	}
	
	private void sendPlayerChat4(String s, String s1, String s2, String s3, ChatEmotes ce) {
		c.getPA().sendFrame200(987, getChatAnim(ce));
		c.getPA().sendFrame126(c.playerName, 988);
		c.getPA().sendFrame126(s, 989);
		c.getPA().sendFrame126(s1, 990);
		c.getPA().sendFrame126(s2, 991);
		c.getPA().sendFrame126(s3, 992);
		c.getPA().sendFrame185(987);
		c.getPA().sendChatInterface(986);
	}
	private static enum ChatEmotes {
		HAPPY_JOYFUL(588),
		CALM_TALK1(589),
		CALM_TALK2(590),
		DEFAULT(591),
		EVIL1(592),
		EVIL2(593),
		EVIL3(594),
		ANNOYED(595),
		DISTRESSED(596),
		DISTRESSED2(597),
		ALMOST_CRYING(598),
		BOWS_HEAD_SAD(598),
		DRUNK_LEFT(600),
		DRUNK_RIGHT(601),
		NOT_INTERESTED(602),
		SLEEPY(603),
		PLAIN_EVIL(604),
		LAUGH1(605),
		LAUGH2(606),
		LAUGH3(607),
		LAUGH4(608),
		EVIL_LAUGH(609),
		SAD(610),
		MORE_SAD(611),
		ON_ONE_HAND(612),
		NEARLY_CRYING(613),
		ANGRY1(614),
		ANGRY2(615),
		ANGRY3(616),
		ANGRY4(617);
		
		private ChatEmotes(int animId) {
		this.animId = animId;
		}
		
		private int animId;
		
		private static int getAnimId(ChatEmotes ce) {
			return ce.animId;
		}
	}
	/*
	 * Player Chating Back
	 */
	public void sendItemStatement(String text, int item) {
		c.getPA().sendFrame126(text, 308);
		c.getPA().sendFrame246(307, 200, item);
		c.getPA().sendChatInterface(306);
	}

}
