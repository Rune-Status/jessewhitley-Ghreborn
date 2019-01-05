package Ghreborn.model.content.dialogue.impl.Lumbridge;

import Ghreborn.model.content.dialogue.Dialogue;
import Ghreborn.model.content.dialogue.DialogueConstants;
import Ghreborn.model.content.dialogue.DialogueManager;
import Ghreborn.model.content.dialogue.Emotion;
import Ghreborn.model.npcs.NPC;
import Ghreborn.model.npcs.NPCHandler;

public class HansDialogue extends Dialogue {

	@Override
	public void execute() {
		switch(getNext()) {
		case 0:
			DialogueManager.sendNpcChat(getPlayer(), 3077, Emotion.DEFAULT, "Hello, what are you doing here?");
			setNext(1);
			break;
		case 1:
			DialogueManager.sendOption(getPlayer(), "I'm looking for whoever is in charge of this place.", "I have come to kill everyone in this castle!", "I don't know. I'm lost. Where am I?", "Can you tell me how long I've been here?", "Nothing.");
			break;
		case 2:
			getPlayer().getPA().closeAllWindows();
			break;
		case 3:
			DialogueManager.sendNpcChat(getPlayer(), 3077, Emotion.DEFAULT, "Who, the Duke? He's in his study, on the first floor.");
			setNext(2);
			break;
		case 4:
			DialogueManager.sendNpcChat(getPlayer(), 3077, Emotion.DEFAULT, "You are in Lumbridge Castle.");
			setNext(2);
			break;
		}
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean clickButton(int id) {
		
		switch(id) {
		case DialogueConstants.OPTIONS_5_1://fishing guild
			DialogueManager.sendClientChat(getPlayer(), Emotion.CALM, "I'm looking for whoever is in charge of this place.");
			setNext(3);
				break;
		case DialogueConstants.OPTIONS_5_2://mining guild
			//final NPC npc = NPCHandler.npcs[player.getNpcClickIndex()];
			//npc.forceChat("Help! help!");
			setNext(2);
			break;
		case DialogueConstants.OPTIONS_5_3://crafting guild
			DialogueManager.sendClientChat(getPlayer(), Emotion.CALM, "I don't know. I'm lost. Where am I?");
			setNext(4);
			break;
		case DialogueConstants.OPTIONS_5_4://crafting guild
			setNext(2);
			break;
		case DialogueConstants.OPTIONS_5_5://crafting guild
			DialogueManager.sendClientChat(getPlayer(), Emotion.ANNOYED, "Nothing.");
			setNext(2);
			break;
		}
		return false;
	}


}
