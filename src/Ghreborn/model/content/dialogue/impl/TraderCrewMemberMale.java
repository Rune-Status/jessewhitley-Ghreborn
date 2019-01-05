package Ghreborn.model.content.dialogue.impl;

import Ghreborn.Server;
import Ghreborn.model.content.dialogue.Dialogue;
import Ghreborn.model.content.dialogue.DialogueConstants;
import Ghreborn.model.content.dialogue.DialogueManager;
import Ghreborn.model.content.dialogue.Emotion;

public class TraderCrewMemberMale  extends Dialogue {

	@Override
	public void execute() {
		switch (getNext()) {
		case 0:
			DialogueManager.sendNpcChat(getPlayer(), 1331, Emotion.CALM, "Can i help you?");
			setNext(1);
			break;
		case 1:
			DialogueManager.sendOption(getPlayer(), "Yes, who are you?", "Yes, I would like to charter a ship.", "No thanks.");
			break;
		case 2:
			DialogueManager.sendNpcChat(getPlayer(), 1331, Emotion.CALM, "I'm one of Trader Stan's crew; we are all part of the", "largest fleet of trading and sailing vessels to ever sail", "the seven seas.");
			setNext(3);
			break;
		case 3:
			DialogueManager.sendNpcChat(getPlayer(), 1331, Emotion.CALM, "If you want to get to a port in a hurry then you can", "charter one of our ships to take you there. if the price", "is right...");
			setNext(4);
			break;
		case 4:
			DialogueManager.sendClientChat(getPlayer(), Emotion.CALM, "So, where exactly can I go with your ships?");
			setNext(5);
			break;
		case 5:
			DialogueManager.sendNpcChat(getPlayer(), 1331, Emotion.CALM, "We run ships from Port Phasmatys over to Port", "Tyras, stopping at Port Sarim, Catherby, Brimhaven,", "Musa Point, the Shipyard and Port Khazard.");
			setNext(6);
			break;
		case 6:
			DialogueManager.sendNpcChat(getPlayer(), 1331, Emotion.CALM, "We might dock at Mos Le`Harmless one in a while, as", "well, if you catch my meaning...");
			setNext(7);
			break;
		case 7:
			DialogueManager.sendClientChat(getPlayer(), Emotion.CALM, "Wow, that's alot of ports. I take it you have some"," exotic stuff to trade?");
			setNext(8);
			break;
		case 8:
			DialogueManager.sendNpcChat(getPlayer(), 1331, Emotion.CALM, "We certainly do! We have accest to items bought and","sold from around the world. Would you like to take a","look? Or would you like to charter a ship?");
			setNext(9);
			break;
		case 9:
			DialogueManager.sendOption(getPlayer(), "Yes, let's see what you're trading.", "Yes, I would like to charter a ship.", "Isn't it tricky to sail about in those clothes?", "No thanks.");
			break;
		case 10:
			getPlayer().getShops().openShop(109);
			break;
		case 11:
			DialogueManager.sendNpcChat(getPlayer(), 1331, Emotion.CALM, "Tricky? Tricky!");
			setNext(12);
			break;
		case 12:
			DialogueManager.sendNpcChat(getPlayer(), 1331, Emotion.CALM, "With all due credit, Trader Stan is a great employer,", "but he insists we wear the lasest in high fashion even", "when sailing.");
			setNext(13);
			break;
		case 13:
			DialogueManager.sendNpcChat(getPlayer(), 1331, Emotion.CALM, "Do you have even the slightest idea how tricky it is to", "sail in this stuff?");
			setNext(14);
			break;
		case 14:
			DialogueManager.sendNpcChat(getPlayer(), 1331, Emotion.CALM, "Some of us tried tearing it and arguing that it was too", "fragile to wear when on a boat, but he just had it", "enchanted to re-stitch itself.");
			setNext(15);
			break;
		case 15:
			DialogueManager.sendNpcChat(getPlayer(), 1331, Emotion.CALM, "It's hard to hate him when we know how much he shells", "out on this gear, but if I fall overboard because of this", "getup one more time; I'm going to quit.");
			setNext(16);
			break;
		case 16:
			DialogueManager.sendClientChat(getPlayer(), Emotion.CALM, "Wow, that's kind of harsh.");
			setNext(17);
			break;
			case 17:
				DialogueManager.sendNpcChat(getPlayer(), 1331, Emotion.CALM, "Anyway, would you like to take a look at our exotic", "wares from around the world? Or would you like to", "charter a ship?");
				setNext(18);
				break;
			case 18:
				DialogueManager.sendOption(getPlayer(), "Yes, let's see what you're trading.", "Yes, I would like to charter a ship.", "No thanks.");
				break;
			case 19:
				getPlayer().sendMessage("Ship charters have not been added yet.");
				getPlayer().getPA().closeAllWindows();
				break;
			case 20:
				getPlayer().getPA().closeAllWindows();
				break;
		}
	}
	@Override
	public boolean clickButton(int id) {
		switch(id) {
	case DialogueConstants.OPTIONS_3_1:
		if(getNext() == 1) {
		DialogueManager.sendClientChat(getPlayer(), Emotion.CALM, "Yes. who are you");
		setNext(2);
		}else {
			DialogueManager.sendClientChat(getPlayer(), Emotion.CALM, "Yes, let's see what you're trading.");
			setNext(10);
		}
		break;
	case DialogueConstants.OPTIONS_3_2:
		DialogueManager.sendClientChat(getPlayer(), Emotion.CALM, "Yes, I would like to charter a ship.");
		setNext(19);
		break;
	case DialogueConstants.OPTIONS_3_3:
		DialogueManager.sendClientChat(getPlayer(), Emotion.CALM, "No thanks.");
		setNext(20);
		break;
	case DialogueConstants.OPTIONS_4_1:
		DialogueManager.sendClientChat(getPlayer(), Emotion.CALM, "Yes, let's see what you're trading.");
		setNext(10);
		break;
	case DialogueConstants.OPTIONS_4_2:
		DialogueManager.sendClientChat(getPlayer(), Emotion.CALM, "Yes, I would like to charter a ship.");
		setNext(19);
		break;
	case DialogueConstants.OPTIONS_4_3:
		DialogueManager.sendClientChat(getPlayer(), Emotion.CALM, "Isn't it tricky to sail about in those clothes?");
		setNext(11);
		break;
	case DialogueConstants.OPTIONS_4_4:
		DialogueManager.sendClientChat(getPlayer(), Emotion.CALM, "No thanks.");
		//setNext(2);
		break;
		}
		
		return false;
	}

}
