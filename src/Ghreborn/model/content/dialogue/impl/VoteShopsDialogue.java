package Ghreborn.model.content.dialogue.impl;

import Ghreborn.model.content.dialogue.Dialogue;
import Ghreborn.model.content.dialogue.DialogueConstants;
import Ghreborn.model.content.dialogue.DialogueManager;

public class VoteShopsDialogue extends Dialogue {

	@Override
	public void execute() {
		switch (getNext()) {
		case 0:
			DialogueManager.sendOption(getPlayer(), "Vote Shop 1", "Vote Shop 2", "Vote Shop 3", "Vote Shop 4", "Vote Shop 5");
			break;
		
	}
	}
	@Override
	public boolean clickButton(int id) {
		
		switch(id) {
		case DialogueConstants.OPTIONS_5_1://vote shop 1
			getPlayer().getShops().openShop(251);
				break;
		case DialogueConstants.OPTIONS_5_2://vote shop 2
			getPlayer().getShops().openShop(252);
			break;
		case DialogueConstants.OPTIONS_5_3://vote shop 3
			getPlayer().getShops().openShop(253);
			break;
		case DialogueConstants.OPTIONS_5_4://vote shop 4
			getPlayer().getShops().openShop(254);
			break;
		case DialogueConstants.OPTIONS_5_5://vote shop 5
			getPlayer().getShops().openShop(255);
			break;
		}
		return false;
	}

}
