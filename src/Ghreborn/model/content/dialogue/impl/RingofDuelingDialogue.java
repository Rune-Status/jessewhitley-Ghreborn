package Ghreborn.model.content.dialogue.impl;

import Ghreborn.model.content.dialogue.Dialogue;
import Ghreborn.model.content.dialogue.DialogueConstants;
import Ghreborn.model.content.dialogue.DialogueManager;
import Ghreborn.model.content.dialogue.Emotion;
import Ghreborn.model.transport.JewelleryTeleport;

public class RingofDuelingDialogue extends Dialogue {

	@Override
	public void execute() {
		switch (getNext()) {
		case 0:
			DialogueManager.sendOption(getPlayer(), "Duel Arena", "Clan Wars", "Castle Wars");
			break;
		
	}
	}
	@Override
		public boolean clickButton(int id) {
			
			switch(id) {
			case DialogueConstants.OPTIONS_3_1://fishing guild
				JewelleryTeleport.teleport(getPlayer(), 3367, 3267, 0);
					break;
			case DialogueConstants.OPTIONS_3_2://mining guild
				JewelleryTeleport.teleport(getPlayer(), 3368, 3163, 0);
				break;
			case DialogueConstants.OPTIONS_3_3://crafting guild
				JewelleryTeleport.teleport(getPlayer(), 2441, 3087, 0);
				break;
			}
			return false;
		}

}
