package Ghreborn.model.content.dialogue.impl.Lumbridge;

import Ghreborn.model.content.dialogue.Dialogue;
import Ghreborn.model.content.dialogue.DialogueConstants;
import Ghreborn.model.content.dialogue.DialogueManager;
import Ghreborn.model.content.dialogue.Emotion;
/**
 * Fred The Farmer
 * @author Sgsrocks
 *
 */
public class FredTheFarmer extends Dialogue {

	@Override
	public boolean clickButton(int id) {
		switch(id) {
	case DialogueConstants.OPTIONS_3_1:
		DialogueManager.sendClientChat(getPlayer(), Emotion.CALM, "I'm looking for something to kill.");
		setNext(2);
		break;
	case DialogueConstants.OPTIONS_3_2:
		DialogueManager.sendClientChat(getPlayer(), Emotion.CALM, "I'm lost.");
		setNext(3);
		break;
	case DialogueConstants.OPTIONS_3_3:
		DialogueManager.sendClientChat(getPlayer(), Emotion.CALM, "Fred! Fred! I've seen The Thing!");
		setNext(4);
		break;
		}
		
		return false;
	}
	@Override
	public void execute() {
		switch (getNext()) {
		case 0:
			DialogueManager.sendNpcChat(getPlayer(), 732, Emotion.ANGRY_1, "What you doing on my land?");
			setNext(1);
			break;
		case 1:
			DialogueManager.sendOption(getPlayer(), "I'm looking for something to kill.", "I'm lost.", "Fred! Fred! I've seen The Thing!");
			break;
		case 2:
			DialogueManager.sendNpcChat(getPlayer(), 732, Emotion.ANGRY_1, "What, on my land? Leave my livestock alone you", "scoundrel!");
			setNext(5);
			break;
		case 3:
			DialogueManager.sendNpcChat(getPlayer(), 732, Emotion.CALM, "How can u be lost? Just follow the road east and", "south. You'll end up in Lumbridge fairly quickly.");
			setNext(6);
			break;
		case 4:
			DialogueManager.sendNpcChat(getPlayer(), 732, Emotion.CALM, "You ... you actually saw it?");
			setNext(7);
			break;
		case 7:
			DialogueManager.sendNpcChat(getPlayer(), 732, Emotion.CALM, "Run for the hills! "+getPlayer().playerName+" grab as many chickens as", "you can! We have to ...");
			setNext(8);
			break;
		case 8:
			DialogueManager.sendClientChat(getPlayer(), Emotion.CALM, "Fred!");
			setNext(9);
			break;
		case 9:
			DialogueManager.sendNpcChat(getPlayer(), 732, Emotion.CALM, "... flee! Oh, woe is me! The shapeshifter is coming!", "We're all ...");
			setNext(10);
			break;
		case 10:
			DialogueManager.sendClientChat(getPlayer(), Emotion.ANGRY_1, "FRED!");
			setNext(11);
			break;
		case 11:
			DialogueManager.sendNpcChat(getPlayer(), 732, Emotion.CALM, "... doomed, What!");
			setNext(12);
			break;
		case 12:
			DialogueManager.sendClientChat(getPlayer(), Emotion.CALM, "It's not a shapeshifter or any other kind of monster!");
			setNext(13);
			break;
		case 13:
			DialogueManager.sendNpcChat(getPlayer(), 732, Emotion.CALM, "Well then what is it boy?");
			setNext(14);
			break;
		case 14:
			DialogueManager.sendClientChat(getPlayer(), Emotion.CALM, "Well ... it's just two Penguins; Penguins disguised as a", "sheep.");
			setNext(15);
			break;
		case 15:
			DialogueManager.sendNpcChat(getPlayer(), 732, Emotion.CALM, "...");
			setNext(16);
			break;
		case 16:
			DialogueManager.sendNpcChat(getPlayer(), 732, Emotion.CALM, "Have you been out in the sun too long?");
			setNext(17);
			break;
		case 5:
		case 6:
		case 17:
			getPlayer().getPA().closeAllWindows();
			break;
		}
	}
	

}
