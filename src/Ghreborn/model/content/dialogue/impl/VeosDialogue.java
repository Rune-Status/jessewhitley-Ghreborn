package Ghreborn.model.content.dialogue.impl;

import Ghreborn.model.content.dialogue.Dialogue;
import Ghreborn.model.content.dialogue.DialogueConstants;
import Ghreborn.model.content.dialogue.DialogueManager;
import Ghreborn.model.content.dialogue.Emotion;

public class VeosDialogue extends Dialogue {

	@Override
	public boolean clickButton(int id) {
		switch(id) {
	case DialogueConstants.OPTIONS_4_1:
		DialogueManager.sendClientChat(getPlayer(), Emotion.CALM, "Can You take me somewhere?");
		setNext(3);
		break;
	case DialogueConstants.OPTIONS_4_2:
		//DialogueManager.sendClientChat(getPlayer(), Emotion.CALM, "Leather is rather weak stuff.");
		setNext(5);
		break;
	case DialogueConstants.OPTIONS_3_1:
		getPlayer().getPA().movePlayer(1824, 3690, 0);
		getPlayer().getPA().closeAllWindows();
		break;
	case DialogueConstants.OPTIONS_3_2:
		getPlayer().getPA().movePlayer(1504, 3401, 0);
		getPlayer().getPA().closeAllWindows();
		break;
		}
		
		return false;
	}
	@Override
	public void execute() {
		switch (getNext()) {
		case 0:
			DialogueManager.sendClientChat(getPlayer(), Emotion.CALM, "Hello Veos.");
			setNext(1);
			break;
		case 1:
			DialogueManager.sendNpcChat(getPlayer(), 2147, Emotion.CALM, "Hello there. What can I do for you?");
			setNext(2);
			break;
		case 2:
			DialogueManager.sendOption(getPlayer(), "Can you take me somewhere?", "Tell me more about Great Kourend.", "Tell me more about Zeah.", "Nothing.");
			break;
		case 3:
			DialogueManager.sendNpcChat(getPlayer(), 2147, Emotion.CALM, "Where would you like to go?");
			setNext(4);
			break;
		case 4:
			DialogueManager.sendOption(getPlayer(), "Travel to Piscarilius House.", "Travel to Land's End.", "Stay where you are.");
			break;
		}
		
	}

}
