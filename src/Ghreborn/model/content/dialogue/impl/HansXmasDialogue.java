package Ghreborn.model.content.dialogue.impl;

import Ghreborn.model.content.dialogue.Dialogue;
import Ghreborn.model.content.dialogue.DialogueConstants;
import Ghreborn.model.content.dialogue.DialogueManager;
import Ghreborn.model.content.dialogue.Emotion;
import Ghreborn.model.players.PlayerSave;

public class HansXmasDialogue extends Dialogue {

	@Override
	public void execute() {
		switch (getNext()) {
		case 0:
			DialogueManager.sendNpcChat(getPlayer(), getPlayer().getNpcClickIndex(), Emotion.HAPPY,
					"Merry Chrismas "+getPlayer().playerName+", Hope u having a good time.", "Do u want the items for this years event?");
			setNext(1);
			break;
		case 1:
			DialogueManager.sendOption(getPlayer(), "Yes Please.", "No thank you.");
			break;
		case 2:
			DialogueManager.sendNpcChat(getPlayer(), getPlayer().getNpcClickIndex(), Emotion.HAPPY, "Here you go "+getPlayer().getUsername()+".");
			setNext(3);
			break;
		case 3:
			if(!getPlayer().HasXmasItems()){
				getPlayer().getItems().addItemToBank1(1419, 1);
				getPlayer().getItems().addItemToBank1(10507, 1);
				getPlayer().getItems().addItemToBank1(6856, 1);
				getPlayer().getItems().addItemToBank1(6857, 1);
				getPlayer().getItems().addItemToBank1(6858, 1);
				getPlayer().getItems().addItemToBank1(6859, 1);
				getPlayer().getItems().addItemToBank1(6860, 1);
				getPlayer().getItems().addItemToBank1(6861, 1);
				getPlayer().getItems().addItemToBank1(6862, 1);
				getPlayer().getItems().addItemToBank1(6863, 1);
				getPlayer().getItems().addItemToBank1(6865, 1);
				getPlayer().getItems().addItemToBank1(6866, 1);
				getPlayer().getItems().addItemToBank1(6867, 1);
				getPlayer().getItems().addItemToBank1(1038, 2);
				getPlayer().getItems().addItemToBank1(1040, 2);
				getPlayer().getItems().addItemToBank1(1042, 2);
				getPlayer().getItems().addItemToBank1(1044, 2);
				getPlayer().getItems().addItemToBank1(1046, 2);
				getPlayer().getItems().addItemToBank1(1048, 2);
				getPlayer().getItems().addItemToBank1(1050, 2);
				getPlayer().getItems().addItemToBank1(962, 2);
				getPlayer().getItems().addItemToBank1(11862, 1);
				getPlayer().getItems().addItemToBank1(11863, 1);
				getPlayer().getItems().addItemToBank1(12887, 1);
				getPlayer().getItems().addItemToBank1(12888, 1);
				getPlayer().getItems().addItemToBank1(12889, 1);
				getPlayer().getItems().addItemToBank1(12890, 1);
				getPlayer().getItems().addItemToBank1(12891, 1);
				getPlayer().getItems().addItemToBank1(12892, 1);
				getPlayer().getItems().addItemToBank1(12893, 1);
				getPlayer().getItems().addItemToBank1(12894, 1);
				getPlayer().getItems().addItemToBank1(12895, 1);
				getPlayer().getItems().addItemToBank1(12896, 1);
				getPlayer().getItems().addItemToBank1(13288, 1);
				getPlayer().getItems().addItemToBank1(13343, 1);
				getPlayer().getItems().addItemToBank1(13344, 1);
				getPlayer().getItems().addItemToBank1(20832, 1);
				getPlayer().getItems().addItemToBank1(20836, 1);
				getPlayer().getItems().addItemToBank1(20834, 1);
				getPlayer().getItems().addItemToBank1(21847, 1);
				getPlayer().getItems().addItemToBank1(21849, 1);
				getPlayer().getItems().addItemToBank1(21851, 1);
				getPlayer().getItems().addItemToBank1(21853, 1);
				getPlayer().getItems().addItemToBank1(21855, 1);
				getPlayer().getItems().addItemToBank1(21857, 1);
				getPlayer().getItems().addItemToBank1(21859, 1);
				getPlayer().getItems().addItemToBank1(22713, 1);
				getPlayer().getItems().addItemToBank1(22715, 1);
				getPlayer().getItems().addItemToBank1(22717, 1);
				getPlayer().getItems().addItemToBank1(22719, 1);
				getPlayer().HasXmasItems = true;
				PlayerSave.saveGame(getPlayer());
				DialogueManager.sendClientChat(getPlayer(), Emotion.HAPPY, "Thank you.");
				setNext(4);
			}else{
				DialogueManager.sendNpcChat(getPlayer(), getPlayer().getNpcClickIndex(), Emotion.HAPPY, "Wait... You allready have them.");
				setNext(4);
			}
			break;
		case 4:
			getPlayer().getPA().closeAllWindows();
			break;
		}

	}
	@Override
	public boolean clickButton(int id) {
		
		switch(id) {
		case DialogueConstants.OPTIONS_2_1:
			DialogueManager.sendClientChat(getPlayer(), Emotion.HAPPY, "Yes Please.");
			setNext(2);
			break;
		}
		return false;
		}
}
