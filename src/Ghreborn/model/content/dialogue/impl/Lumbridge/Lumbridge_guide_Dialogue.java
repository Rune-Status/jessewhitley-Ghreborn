package Ghreborn.model.content.dialogue.impl.Lumbridge;

import Ghreborn.model.content.dialogue.Dialogue;
import Ghreborn.model.content.dialogue.DialogueConstants;
import Ghreborn.model.content.dialogue.DialogueManager;
import Ghreborn.model.content.dialogue.Emotion;

public class Lumbridge_guide_Dialogue extends Dialogue {

	@Override
	public void execute() {
		switch(getNext()) {
		case 0:
			DialogueManager.sendNpcChat(getPlayer(), 306, Emotion.DEFAULT, "Greetings adventurer. I am Phileas the Lumbridge", "Guide. I am here to give information and directions to", "new players. Do you require any help?");
			setNext(1);
			break;
		case 1:
			DialogueManager.sendOption(getPlayer(), "Yes please.", "No, I can find things myself thank you.");
			break;
		case 2:
			DialogueManager.sendNpcChat(getPlayer(), 306, Emotion.DEFAULT, "First I must warn you to take every precaution to", "keep your Ghreborn password and PIN secure. The", "most important thing to remember is to never give your", "password to, or share your account with, anyone.");
			setNext(4);
			break;
		case 4:
			DialogueManager.sendNpcChat(getPlayer(), 306, Emotion.DEFAULT, "I have much more information to impart; what would", "you like to know about?");
			setNext(5);
			break;
		case 5:
			DialogueManager.sendOption(getPlayer(), "Where can I find a quest to go on?", "What monsters should I fight?", "Where can I make money?", "How can I heal myself?", "Where can I find a bank?");
			break;
		}
		
	}
	@Override
	public boolean clickButton(int id) {
		switch(id) {
		case DialogueConstants.OPTIONS_2_1:
			DialogueManager.sendClientChat(getPlayer(), Emotion.CALM, "Yes please.");
			setNext(2);
			break;
		case DialogueConstants.OPTIONS_2_2:
			DialogueManager.sendClientChat(getPlayer(), Emotion.CALM, "No, I can find things myself thank you.");
			setNext(3);
			break;
		}
		return false;
		
	}

}
