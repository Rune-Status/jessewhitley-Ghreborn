package Ghreborn.model.content.dialogue.impl;

import Ghreborn.Config;
import Ghreborn.model.content.dialogue.Dialogue;
import Ghreborn.model.content.dialogue.DialogueManager;
import Ghreborn.model.content.dialogue.Emotion;
import Ghreborn.model.content.dialogue.OptionDialogue;

public class ZenyteDialogue extends Dialogue {

	@Override
	public void execute() {
		switch (getNext()) {
		case 0:
			DialogueManager.sendNpcChat(getPlayer(), NPC_ID, Emotion.LAUGHING, "Greetings! I'm the Zenyte Gem Trader.",
					"What can I do for you?");
			setNext(1);
			break;
		case 1:
			getPlayer().start(new OptionDialogue("Craft Ring of suffering", p -> {
				craft(19550);
			}, "Craft Necklace of anguish", p -> {
				craft(19547);
			}, "Craft Tormented bracelet", p -> {
				craft(19544);
			}, "Craft Amulet of torture", p -> {
				craft(19553);
			}));
			end();
			break;
		}
	}

	public void craft(int itemId) {
		if (getPlayer().playerLevel[Config.MAGIC] < 90) {
			getPlayer().sendMessage("You need at least level 90 magic to craft zenyte.");
		} else {
			if (getPlayer().getItems().playerHasItem(19493)) {
				getPlayer().getItems().deleteItem2(19493, 1);
				getPlayer().getItems().addItem(itemId, 1);
				getPlayer().sendMessage("You received a crafted zenyte item.");
			} else {
				getPlayer().sendMessage("You must hava a zenyte to craft this item.");
			}
		}

		getPlayer().getPA().removeAllWindows();
	}

	public static final int NPC_ID = 526;

}