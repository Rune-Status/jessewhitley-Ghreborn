package Ghreborn.model.content.dialogue.impl;

import Ghreborn.model.content.dialogue.Dialogue;
import Ghreborn.model.content.dialogue.DialogueConstants;
import Ghreborn.model.content.dialogue.DialogueManager;
import Ghreborn.model.content.dialogue.Emotion;
import Ghreborn.util.Misc;

public class ManandWomanDialogue extends Dialogue {


@Override
public void execute() {
	switch (getNext()) {
	case 0:
		DialogueManager.sendClientChat(getPlayer(), Emotion.HAPPY, "Hello, how's it going?");
		setNext(1);
		break;
	case 1:
		int random = Misc.random(12);
		if (random == 0) {
			DialogueManager.sendNpcChat(getPlayer(), getPlayer().getNpcClickIndex(), Emotion.DEFAULT, "How can I help you");
			setNext(3);
		} else if (random == 1) {
			DialogueManager.sendNpcChat(getPlayer(), getPlayer().getNpcClickIndex(), Emotion.DEFAULT, "I'm fine, how are you?");
			setNext(4);
		} else if (random == 2) {
			DialogueManager.sendNpcChat(getPlayer(), getPlayer().getNpcClickIndex(), Emotion.DEFAULT, "I'm busy right now.");
			setNext(2);
		} else if (random == 3) {
			DialogueManager.sendNpcChat(getPlayer(), getPlayer().getNpcClickIndex(), Emotion.ANGRY_1, "No, I don't want to buy anything!");
			setNext(2);
		} else if (random == 4) {
			DialogueManager.sendNpcChat(getPlayer(), getPlayer().getNpcClickIndex(), Emotion.ANNOYED, "No I don't have any spare change.");
			setNext(2);
		} else if (random == 5) {
			DialogueManager.sendNpcChat(getPlayer(), getPlayer().getNpcClickIndex(), Emotion.HAPPY, "I'm very well thank you.");
			setNext(2);
		} else if (random == 6) {
			DialogueManager.sendNpcChat(getPlayer(), getPlayer().getNpcClickIndex(), Emotion.HAPPY, "Hello there! Nice weather we've been having.");
			setNext(2);
		} else if (random == 7) {
			DialogueManager.sendNpcChat(getPlayer(), getPlayer().getNpcClickIndex(), Emotion.DEFAULT, "That is classified information.");
			setNext(2);
		} else if (random == 8) {
			DialogueManager.sendNpcChat(getPlayer(), getPlayer().getNpcClickIndex(), Emotion.ANGRY_1, "Get out of my way, I'm in a hurry!");
			setNext(2);
		} else if (random == 9) {
			DialogueManager.sendNpcChat(getPlayer(), getPlayer().getNpcClickIndex(), Emotion.HAPPY, "Hello.");
			setNext(2);
		} else if (random == 10) {
			DialogueManager.sendNpcChat(getPlayer(), getPlayer().getNpcClickIndex(), Emotion.HAPPY, "Do I know you? I'm in a hurry");
			setNext(2);
		} else if (random == 11) {
			DialogueManager.sendNpcChat(getPlayer(), getPlayer().getNpcClickIndex(), Emotion.ANNOYED, "I'm sorry I can't help you there.");
			setNext(2);
		} else if (random == 12) {
			DialogueManager.sendNpcChat(getPlayer(), getPlayer().getNpcClickIndex(), Emotion.DEFAULT, "Not too bad thanks.");
			setNext(2);
		}
		break;
	case 2:
		getPlayer().getPA().closeAllWindows();
		break;
	case 3:
		DialogueManager.sendOption(getPlayer(), "Do you wish to trade?", "I'm in search of a quest.", "I'm in search of enemies to kill.");
		break;
	case 4:
		DialogueManager.sendClientChat(getPlayer(), Emotion.HAPPY, "Very well thank you.");
		setNext(2);
		break;
	}
}
@Override
public boolean clickButton(int id) {
	switch(id) {
	case DialogueConstants.OPTIONS_3_1:
		DialogueManager.sendNpcChat(getPlayer(), getPlayer().getNpcClickIndex(), Emotion.DEFAULT, "No, I have nothing I wish to get rid of.", "If you want to do some trading, there are", "plent of shops and market stalls around though.");
		setNext(2);
		break;
	case DialogueConstants.OPTIONS_3_2:
		DialogueManager.sendNpcChat(getPlayer(), getPlayer().getNpcClickIndex(), Emotion.DEFAULT, "I'm sorry I can't help you there.");
		setNext(2);
		break;
	case DialogueConstants.OPTIONS_3_3:
		DialogueManager.sendNpcChat(getPlayer(), getPlayer().getNpcClickIndex(), Emotion.DEFAULT,"I've heard there are many fearsome creatures", "that dwell under the ground...");
		setNext(2);
		break;
	}
	return false;
}

}