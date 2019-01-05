package Ghreborn.model.content.dialogue;

import Ghreborn.definitions.NPCCacheDefinition;
import Ghreborn.model.npcs.NpcDefinition;
import Ghreborn.model.players.Client;

public class DialogueManager {
	
    public static void sendItem2(Client player, String text1, String text2, int item1, int item2) {
    	player.getPA().sendFrame126(text1, 6232);
    	player.getPA().sendFrame126(text2, 6233);
    	player.getPA().sendFrame246(6235, 170, item1);
    	player.getPA().sendFrame246(6236, 170, item2);
		player.getPA().sendChatInterface(6231);
	}
    
    public static void sendItem2zoom(Client player, String text1, String text2, int item1, int item2) {
    	player.getPA().sendFrame126(text1, 6232);
    	player.getPA().sendFrame126(text2, 6233);
    	player.getPA().sendFrame246(6235, 130, item1);
    	player.getPA().sendFrame246(6236, 100, item2);
    	player.getPA().sendChatInterface(6231);
    }
	
	public static void sendItem1(Client player, String text, int item) {
		player.getPA().sendFrame126(text, 308);
		player.getPA().sendFrame246(307, 200, item);
		player.getPA().sendChatInterface(306);
	}
	
	public static void makeItem3(Client player, int itemId1, String itemName1, int itemId2, String itemName2, int itemId3, String itemName3) {
		player.getPA().sendChatInterface(8880);
		player.getPA().sendFrame246(8883, 190, itemId1);
		player.getPA().sendFrame246(8884, 190, itemId2);
		player.getPA().sendFrame246(8885, 190, itemId3);
		player.getPA().sendFrame126(itemName1, 8889);
		player.getPA().sendFrame126(itemName2, 8893);
		player.getPA().sendFrame126(itemName3, 8897);
	}

	public static void sendInformationBox(Client player, String title, String line1, String line2, String line3, String line4) {
		player.getPA().sendFrame126(title, 6180);
		player.getPA().sendFrame126(line1, 6181);
		player.getPA().sendFrame126(line2, 6182);
		player.getPA().sendFrame126(line3, 6183);
		player.getPA().sendFrame126(line4, 6184);
		player.getPA().sendChatInterface(6179);
	}

	public static void sendNpcChat(Client player, int npcId, Emotion emotion, String... lines) {
		String npcName = NPCCacheDefinition.forID(npcId).getName().replace("_", " ");
		switch (lines.length) {
		case 1:
			player.getPA().sendFrame200(4883, emotion.getEmoteId());
			player.getPA().sendFrame126(npcName, 4884);
			player.getPA().sendFrame126(lines[0], 4885);
			player.getPA().sendFrame75(npcId, 4883);
			player.getPA().sendChatInterface(4882);
			break;
		case 2:
			player.getPA().sendFrame200(4888, emotion.getEmoteId());
			player.getPA().sendFrame126(npcName, 4889);
			player.getPA().sendFrame126(lines[0], 4890);
			player.getPA().sendFrame126(lines[1], 4891);
			player.getPA().sendFrame75(npcId, 4888);
			player.getPA().sendChatInterface(4887);
			break;
		case 3:
			player.getPA().sendFrame200(4894, emotion.getEmoteId());
			player.getPA().sendFrame126(npcName, 4895);
			player.getPA().sendFrame126(lines[0], 4896);
			player.getPA().sendFrame126(lines[1], 4897);
			player.getPA().sendFrame126(lines[2], 4898);
			player.getPA().sendFrame75(npcId, 4894);
			player.getPA().sendChatInterface(4893);
			break;
		case 4:
			player.getPA().sendFrame200(4901, emotion.getEmoteId());
			player.getPA().sendFrame126(npcName, 4902);
			player.getPA().sendFrame126(lines[0], 4903);
			player.getPA().sendFrame126(lines[1], 4904);
			player.getPA().sendFrame126(lines[2], 4905);
			player.getPA().sendFrame126(lines[3], 4906);
			player.getPA().sendFrame75(npcId, 4901);
			player.getPA().sendChatInterface(4900);
		}
	}

	public static void sendOption(Client player, String... options) {
		if (options.length < 2) {
			return;
		}
		switch (options.length) {
		case 1:
			throw new IllegalArgumentException("1 option is not possible! (DialogueManager.java)");
		case 2:
			player.getPA().sendFrame126(options[0], 2461);
			player.getPA().sendFrame126(options[1], 2462);
			player.getPA().sendChatInterface(2459);
			break;
		case 3:
			player.getPA().sendFrame126(options[0], 2471);
			player.getPA().sendFrame126(options[1], 2472);
			player.getPA().sendFrame126(options[2], 2473);
			player.getPA().sendChatInterface(2469);
			break;
		case 4:
			player.getPA().sendFrame126(options[0], 2482);
			player.getPA().sendFrame126(options[1], 2483);
			player.getPA().sendFrame126(options[2], 2484);
			player.getPA().sendFrame126(options[3], 2485);
			player.getPA().sendChatInterface(2480);
			break;
		case 5:
			player.getPA().sendFrame126(options[0], 2494);
			player.getPA().sendFrame126(options[1], 2495);
			player.getPA().sendFrame126(options[2], 2496);
			player.getPA().sendFrame126(options[3], 2497);
			player.getPA().sendFrame126(options[4], 2498);
			player.getPA().sendChatInterface(2492);
		}
	}

	public static void sendClientChat(Client player, Emotion emotion, String... lines) {
		switch (lines.length) {
		case 1:
			player.getPA().sendFrame200(969, emotion.getEmoteId());
			player.getPA().sendFrame126(player.getUsername(), 970);
			player.getPA().sendFrame126(lines[0], 971);
			player.getPA().sendFrame185(969);
			player.getPA().sendChatInterface(968);
			break;
		case 2:
			player.getPA().sendFrame200(974, emotion.getEmoteId());
			player.getPA().sendFrame126(player.getUsername(), 975);
			player.getPA().sendFrame126(lines[0], 976);
			player.getPA().sendFrame126(lines[1], 977);
			player.getPA().sendFrame185(974);
			player.getPA().sendChatInterface(973);
			break;
		case 3:
			player.getPA().sendFrame200(980, emotion.getEmoteId());
			player.getPA().sendFrame126(player.getUsername(), 981);
			player.getPA().sendFrame126(lines[0], 982);
			player.getPA().sendFrame126(lines[1], 983);
			player.getPA().sendFrame126(lines[2], 984);
			player.getPA().sendFrame185(980);
			player.getPA().sendChatInterface(979);
			break;
		case 4:
			player.getPA().sendFrame200(987, emotion.getEmoteId());
			player.getPA().sendFrame126(player.getUsername(), 988);
			player.getPA().sendFrame126(lines[0], 989);
			player.getPA().sendFrame126(lines[1], 990);
			player.getPA().sendFrame126(lines[2], 991);
			player.getPA().sendFrame126(lines[3], 992);
			player.getPA().sendFrame185(987);
			player.getPA().sendChatInterface(986);
		}
	}

	public static void sendStatement(Client player, String... lines) {
		switch (lines.length) {
		case 1:
			player.getPA().sendFrame126(lines[0], 357);
			player.getPA().sendChatInterface(356);
			break;
		case 2:
			player.getPA().sendFrame126(lines[0], 360);
			player.getPA().sendFrame126(lines[1], 361);
			player.getPA().sendChatInterface(359);
			break;
		case 3:
			player.getPA().sendFrame126(lines[0], 364);
			player.getPA().sendFrame126(lines[1], 365);
			player.getPA().sendFrame126(lines[2], 366);
			player.getPA().sendChatInterface(363);
			break;
		case 4:
			player.getPA().sendFrame126(lines[0], 369);
			player.getPA().sendFrame126(lines[1], 370);
			player.getPA().sendFrame126(lines[2], 371);
			player.getPA().sendFrame126(lines[3], 372);
			player.getPA().sendChatInterface(368);
			break;
		case 5:
			player.getPA().sendFrame126(lines[0], 375);
			player.getPA().sendFrame126(lines[1], 376);
			player.getPA().sendFrame126(lines[2], 377);
			player.getPA().sendFrame126(lines[3], 378);
			player.getPA().sendFrame126(lines[4], 379);
			player.getPA().sendChatInterface(374);
		}
	}

	public static void sendTimedNpcChat(Client player, int npcId, Emotion emotion, String... lines) {
		String npcName = NpcDefinition.DEFINITIONS[npcId].getName();
		switch (lines.length) {
		case 2:
			player.getPA().sendFrame200(12379, emotion.getEmoteId());
			player.getPA().sendFrame126(npcName, 12380);
			player.getPA().sendFrame126(lines[0], 12381);
			player.getPA().sendFrame126(lines[1], 12382);
			player.getPA().sendFrame75(npcId, 12379);
			player.getPA().sendChatInterface(12378);
			break;
		case 3:
			player.getPA().sendFrame200(12384, emotion.getEmoteId());
			player.getPA().sendFrame126(npcName, 12385);
			player.getPA().sendFrame126(lines[0], 12386);
			player.getPA().sendFrame126(lines[1], 12387);
			player.getPA().sendFrame126(lines[2], 12388);
			player.getPA().sendFrame75(npcId, 12384);
			player.getPA().sendChatInterface(12383);
			break;
		case 4:
			player.getPA().sendFrame200(11892, emotion.getEmoteId());
			player.getPA().sendFrame126(npcName, 11893);
			player.getPA().sendFrame126(lines[0], 11894);
			player.getPA().sendFrame126(lines[1], 11895);
			player.getPA().sendFrame126(lines[2], 11896);
			player.getPA().sendFrame126(lines[3], 11897);
			player.getPA().sendFrame75(npcId, 11892);
			player.getPA().sendChatInterface(11891);
		}
	}

	public static void sendTimedClientChat(Client player, Emotion emotion, String... lines) {
		switch (lines.length) {
		case 1:
			player.getPA().sendFrame200(12774, emotion.getEmoteId());
			player.getPA().sendFrame126(player.getUsername(), 12775);
			player.getPA().sendFrame126(lines[0], 12776);
			player.getPA().sendFrame185(12774);
			player.getPA().sendChatInterface(12773);
			break;
		case 2:
			player.getPA().sendFrame200(12778, emotion.getEmoteId());
			player.getPA().sendFrame126(player.getUsername(), 12779);
			player.getPA().sendFrame126(lines[0], 12780);
			player.getPA().sendFrame126(lines[1], 12781);
			player.getPA().sendFrame185(12778);
			player.getPA().sendChatInterface(12777);
			break;
		case 3:
			player.getPA().sendFrame200(12783, emotion.getEmoteId());
			player.getPA().sendFrame126(player.getUsername(), 12784);
			player.getPA().sendFrame126(lines[0], 12785);
			player.getPA().sendFrame126(lines[1], 12786);
			player.getPA().sendFrame126(lines[2], 12787);
			player.getPA().sendFrame185(12783);
			player.getPA().sendChatInterface(12782);
			break;
		case 4:
			player.getPA().sendFrame200(11885, emotion.getEmoteId());
			player.getPA().sendFrame126(player.getUsername(), 11886);
			player.getPA().sendFrame126(lines[0], 11887);
			player.getPA().sendFrame126(lines[1], 11888);
			player.getPA().sendFrame126(lines[2], 11889);
			player.getPA().sendFrame126(lines[3], 11890);
			player.getPA().sendFrame185(11885);
			player.getPA().sendChatInterface(11884);
		}
	}

	public static void sendTimedStatement(Client player, String... lines) {
		switch (lines.length) {
		case 1:
			player.getPA().sendFrame126(lines[0], 12789);
			player.getPA().sendChatInterface(12788);
			break;
		case 2:
			player.getPA().sendFrame126(lines[0], 12791);
			player.getPA().sendFrame126(lines[1], 12792);
			player.getPA().sendChatInterface(12790);
			break;
		case 3:
			player.getPA().sendFrame126(lines[0], 12794);
			player.getPA().sendFrame126(lines[1], 12795);
			player.getPA().sendFrame126(lines[2], 12796);
			player.getPA().sendChatInterface(12793);
			break;
		case 4:
			player.getPA().sendFrame126(lines[0], 12798);
			player.getPA().sendFrame126(lines[1], 12799);
			player.getPA().sendFrame126(lines[2], 12800);
			player.getPA().sendFrame126(lines[3], 12801);
			player.getPA().sendChatInterface(12797);
			break;
		case 5:
			player.getPA().sendFrame126(lines[0], 12803);
			player.getPA().sendFrame126(lines[1], 12804);
			player.getPA().sendFrame126(lines[2], 12805);
			player.getPA().sendFrame126(lines[3], 12806);
			player.getPA().sendFrame126(lines[4], 12807);
			player.getPA().sendChatInterface(12802);
		}
	}
}