package Ghreborn.model.content.dialogue.impl;

import Ghreborn.model.content.dialogue.Dialogue;
import Ghreborn.model.content.dialogue.DialogueConstants;
import Ghreborn.model.content.dialogue.DialogueManager;
import Ghreborn.model.content.dialogue.Emotion;
import Ghreborn.model.transport.JewelleryTeleport;

public class GloryDialogue extends Dialogue {

	@Override
	public void execute() {
		switch (getNext()) {
		case 0:
			DialogueManager.sendOption(getPlayer(), "Edgeville", "Karamja", "Draynor", "Al Kharid");
			break;
		
	}
	}
	@Override
		public boolean clickButton(int id) {
			
			switch(id) {
			case DialogueConstants.OPTIONS_4_1:
				JewelleryTeleport.teleport(getPlayer(), 3087, 3495, 0);
					break;
			case DialogueConstants.OPTIONS_4_2:
				JewelleryTeleport.teleport(getPlayer(), 2912, 3170, 0);
				break;
			case DialogueConstants.OPTIONS_4_3:
				JewelleryTeleport.teleport(getPlayer(), 3104, 3249, 0);
				break;
			case DialogueConstants.OPTIONS_4_4:
				JewelleryTeleport.teleport(getPlayer(), 3293, 3162, 0);
				break;
			}
			return false;
		}

}
