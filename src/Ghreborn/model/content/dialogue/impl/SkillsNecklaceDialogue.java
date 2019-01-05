package Ghreborn.model.content.dialogue.impl;

import Ghreborn.model.content.dialogue.Dialogue;
import Ghreborn.model.content.dialogue.DialogueConstants;
import Ghreborn.model.content.dialogue.DialogueManager;
import Ghreborn.model.content.dialogue.Emotion;
import Ghreborn.model.transport.JewelleryTeleport;

public class SkillsNecklaceDialogue extends Dialogue {

	@Override
	public void execute() {
		switch (getNext()) {
		case 0:
			DialogueManager.sendOption(getPlayer(), "Fishing Guild", "Mining Guild", "Crafting Guild", "Cooking Guild", "Woodcutting Guild");
			break;
		
	}
	}
	@Override
		public boolean clickButton(int id) {
			
			switch(id) {
			case DialogueConstants.OPTIONS_5_1://fishing guild
				JewelleryTeleport.teleport(getPlayer(), 2593, 3420, 0);
					break;
			case DialogueConstants.OPTIONS_5_2://mining guild
				JewelleryTeleport.teleport(getPlayer(), 3021, 3338, 0);
				break;
			case DialogueConstants.OPTIONS_5_3://crafting guild
				JewelleryTeleport.teleport(getPlayer(), 2933, 3291, 0);
				break;
			case DialogueConstants.OPTIONS_5_4://cooking guild
				JewelleryTeleport.teleport(getPlayer(), 3143, 3441, 0);
				break;
			case DialogueConstants.OPTIONS_5_5://woodcuttting guild
				JewelleryTeleport.teleport(getPlayer(), 1589, 3483, 0);
				break;
			}
			return false;
		}

}
