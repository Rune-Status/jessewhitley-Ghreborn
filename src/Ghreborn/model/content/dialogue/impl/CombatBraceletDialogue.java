package Ghreborn.model.content.dialogue.impl;

import Ghreborn.model.content.dialogue.Dialogue;
import Ghreborn.model.content.dialogue.DialogueConstants;
import Ghreborn.model.content.dialogue.DialogueManager;
import Ghreborn.model.content.dialogue.Emotion;
import Ghreborn.model.transport.JewelleryTeleport;

public class CombatBraceletDialogue extends Dialogue {

	@Override
	public void execute() {
		switch (getNext()) {
		case 0:
			DialogueManager.sendOption(getPlayer(), "Warrior's Guild", "Champion's Guild", "Edgeville Monastery", "Ranging Guild");
			break;
		
	}
	}
	@Override
		public boolean clickButton(int id) {
			
			switch(id) {
			case DialogueConstants.OPTIONS_4_1://fishing guild
				JewelleryTeleport.teleport(getPlayer(), 2881, 3546, 0);
					break;
			case DialogueConstants.OPTIONS_4_2://mining guild
				JewelleryTeleport.teleport(getPlayer(), 3191, 3366, 0);
				break;
			case DialogueConstants.OPTIONS_4_3://crafting guild
				JewelleryTeleport.teleport(getPlayer(), 3052, 3481, 0);
				break;
			case DialogueConstants.OPTIONS_4_4://cooking guild
				JewelleryTeleport.teleport(getPlayer(), 2656, 3440, 0);
				break;
			}
			return false;
		}

}
