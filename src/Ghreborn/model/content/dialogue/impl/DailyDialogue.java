package Ghreborn.model.content.dialogue.impl;

import Ghreborn.model.content.dialogue.Dialogue;
import Ghreborn.model.content.dialogue.DialogueConstants;
import Ghreborn.model.content.dialogue.DialogueManager;
import Ghreborn.model.content.dialogue.Emotion;

public class DailyDialogue extends Dialogue {

	@Override
	public void execute() {
		switch (getNext()) {
		case 0:
			DialogueManager.sendNpcChat(getPlayer(), 1909, Emotion.CALM, "Hello! Would you like to enable daily tasks?", "These tasks can be assigned and completed", "Once a day for some pretty neat rewards!", "Do you want to enable daily tasks?");
			setNext(1);
			break;
		case 1:
			DialogueManager.sendOption(getPlayer(), "Yes, enable daily tasks", "No, disable daily tasks.");
			break;
		case 2:
			DialogueManager.sendNpcChat(getPlayer(), 1909, Emotion.CALM, "Please select your preference on the type", "of tasks you would like to receive!");
			setNext(3);
			break;
		}
		// TODO Auto-generated method stub
		
	}
	public boolean clickButton(int id) {
		
	switch(id) {
	case DialogueConstants.OPTIONS_2_1:
		if(getNext() == 1) {
			getPlayer().dailyEnabled = true;
			setNext(2);
			System.out.println("this one");
		}else{
			System.out.println("that one");
		}
		break;
	}
		return false;
	}

}
