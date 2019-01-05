package Ghreborn.model.content.dialogue.impl;

import Ghreborn.model.content.dialogue.Dialogue;
import Ghreborn.model.content.dialogue.DialogueConstants;
import Ghreborn.model.content.dialogue.DialogueManager;
import Ghreborn.model.content.dialogue.Emotion;
import Ghreborn.model.content.dialogue.OptionDialogue;
/**
 * Dialogue for Ellis
 * @author Sgsrocks
 *
 */
public class EllisDialogue extends Dialogue {

	@Override
	public boolean clickButton(int id) {
		switch(id) {
	case DialogueConstants.OPTIONS_2_1:
		DialogueManager.sendClientChat(getPlayer(), Emotion.CALM, "Can i buy some leather then?");
		setNext(3);
		break;
	case DialogueConstants.OPTIONS_2_2:
		DialogueManager.sendClientChat(getPlayer(), Emotion.CALM, "Leather is rather weak stuff.");
		setNext(5);
		break;
		}
		
		return false;
	}
	@Override
	public void execute() {
		switch (getNext()) {
		case 0:
			DialogueManager.sendNpcChat(getPlayer(), 3231, Emotion.CALM, "Greetings friend. I am a manufacturer of leather.");
			setNext(1);
			break;
			
		case 1:
			DialogueManager.sendOption(getPlayer(), "Can i buy some leather then?", "Leather is rather weak stuff.");
			break;
			
		case 3:
			DialogueManager.sendNpcChat(getPlayer(), 3231, Emotion.CALM, "I make leather from animal hides. Bring me some","cowhides and one gold coin per hide, and I'll tan them", "into soft leather for you.");
			setNext(4);
			break;
		case 4:
			getPlayer().getPA().closeAllWindows();
			break;
		case 5:
			DialogueManager.sendNpcChat(getPlayer(), 3231, Emotion.CALM, "Normal leather may be quite weak, but it's very cheap.","I make it from cowhides for only 1 gp per hide, and", "it's so easy to craft that anyone can work with it.");
			setNext(6);
			break;
		case 6:
			DialogueManager.sendNpcChat(getPlayer(), 3231, Emotion.CALM, "Alternatively you could try hard leather. It's not so","easy to craft, but I only charge 3 gp per cowhide to", "prepare it, and it makes much sturdier armour.");
			setNext(7);
			break;
		case 7:
			DialogueManager.sendNpcChat(getPlayer(), 3231, Emotion.CALM, "I can also tan snake hides and dragonhides, suitable for","crafting into the highest quality armour for rangers.");
			setNext(8);
			break;
		case 8:
			DialogueManager.sendClientChat(getPlayer(), Emotion.CALM, "Thanks, I'll bear it in mind.");
			setNext(9);
			break; 
		case 9:
			getPlayer().getPA().closeAllWindows();
			break;
		}
		
	}

}
