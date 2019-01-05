package Ghreborn.model.content.dialogue.impl.slayer;

import Ghreborn.model.content.dialogue.Dialogue;
import Ghreborn.model.content.dialogue.DialogueManager;
import Ghreborn.model.content.dialogue.Emotion;

public class DuradelDialogue extends Dialogue {

	@Override
	public void execute() {
		switch (getNext()) {
		case 0:
			if(getPlayer().combatLevel<100){
				DialogueManager.sendNpcChat(getPlayer(), 405, Emotion.ANGRY_1, "Do not waste my time peasent.","You need a Combat level of at least 100.");
				setNext(1);
				//player.getDH().sendNpcChat2("Do not waste my time peasent.","You need a Combat level of at least 100.",402,"Duradel");
			}
			if (getPlayer().playerLevel[18] < 50) {
				DialogueManager.sendNpcChat(getPlayer(), 405, Emotion.ANGRY_1, "You must have a slayer level of at least 50 weakling.");
				setNext(1);
				//player.getDH().sendNpcChat1("You must have a slayer level of at least 50 weakling.", 490, "Duradel");
				
			}
			break;
		}
		// TODO Auto-generated method stub

	}

}
