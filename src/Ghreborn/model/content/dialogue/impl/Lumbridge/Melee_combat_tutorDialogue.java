package Ghreborn.model.content.dialogue.impl.Lumbridge;

import Ghreborn.model.content.dialogue.Dialogue;
import Ghreborn.model.content.dialogue.DialogueConstants;
import Ghreborn.model.content.dialogue.DialogueManager;
import Ghreborn.model.content.dialogue.Emotion;

public class Melee_combat_tutorDialogue extends Dialogue {
	@Override
	public void execute() {
		switch(getNext()) {
		case 0:
			DialogueManager.sendOption(getPlayer(), "Can you tell me about different weapon types i can use?", "Please tell me about skillcapes.", "Bye.");
			break;
		case 1:
			DialogueManager.sendNpcChat(getPlayer(), 3216, Emotion.DEFAULT, "Well let me see now...There are stabbing type weapons.", "such as daggers, then you have swords which are", "slashimg maces that have great crushing abilities, battle", "axes which are powerful and spears which can be good");
			setNext(2);
			break;
		case 2:
			DialogueManager.sendNpcChat(getPlayer(), 3216, Emotion.DEFAULT, "for Defence and many forms of Attack.");
			setNext(3);
			break;
		case 3:
			DialogueManager.sendNpcChat(getPlayer(), 3216, Emotion.DEFAULT, "It depends a lot on how you want to fight. Experiment", "and find out what is best for you. Never be scared to", "try out new weapon; you never know, you might like", "it! Why, I tried all of them for a while and settled on");
			setNext(4);
			break;
		case 4:
			DialogueManager.sendNpcChat(getPlayer(), 3216, Emotion.DEFAULT, "this rather good sword!");
			setNext(5);
			break;
		case 5:
			DialogueManager.sendOption(getPlayer(), "I'd like a training sword and shield.", "Bye.");
			break;
		case 6:
			getPlayer().getPA().closeAllWindows();
			break;
		case 7:
			DialogueManager.sendNpcChat(getPlayer(), 3216, Emotion.DEFAULT, "Of course. Skillcapes are a symbol of achievement. Only", "people who have mastered a skill and reached level 99", "can get their hands on them and gain the benefits they", "carry.");
			setNext(8);
			break;
		case 8:
			DialogueManager.sendNpcChat(getPlayer(), 3216, Emotion.DEFAULT, "The Cape of Defence will act as ring of life, saving you", "from combat if your hitpoints became low. Is there", "something else I can help you with, perhaps?");
			setNext(0);
			break;
		}
	}
		@Override
		public boolean clickButton(int id) {
			switch(id) {
			case DialogueConstants.OPTIONS_3_1:
				DialogueManager.sendClientChat(getPlayer(), Emotion.CALM, "Can you tell me about different weapon types I can", "use?");
				setNext(1);
				break;
			case DialogueConstants.OPTIONS_3_2:
				DialogueManager.sendClientChat(getPlayer(), Emotion.CALM, "Can you tell me about skillcapes?");
				setNext(7);
				break;
			case DialogueConstants.OPTIONS_3_3:
				DialogueManager.sendClientChat(getPlayer(), Emotion.CALM, "Bye.");
				setNext(6);
				break;
			case DialogueConstants.OPTIONS_2_1:
				if(getPlayer().getItems().playerHasItem(9703) 
						&& getPlayer().getItems().bankContains(9703)
						&& getPlayer().getItems().playerHasEquipped(9703)
						&& !getPlayer().getItems().playerHasItem(9704) 
						&& !getPlayer().getItems().bankContains(9704)
						&& !getPlayer().getItems().playerHasEquipped(9704)){
					//getPlayer().getItems().addItem(9703, 1);
					getPlayer().getItems().addItem(9704, 1);
					DialogueManager.sendNpcChat(getPlayer(), 3216, Emotion.DEFAULT, "you have the sword, heres the shield.");
					setNext(6);
				} else if(!getPlayer().getItems().playerHasItem(9703) 
							&& !getPlayer().getItems().bankContains(9703)
							&& !getPlayer().getItems().playerHasEquipped(9703)
							&& getPlayer().getItems().playerHasItem(9704) 
							&& getPlayer().getItems().bankContains(9704)
							&& getPlayer().getItems().playerHasEquipped(9704)){
						getPlayer().getItems().addItem(9703, 1);
						//getPlayer().getItems().addItem(9704, 1);
						DialogueManager.sendNpcChat(getPlayer(), 3216, Emotion.DEFAULT, "you have the shield, heres the sword.");
						setNext(6);
				}else if(!getPlayer().getItems().playerHasItem(9703) 
						&& !getPlayer().getItems().bankContains(9703)
						&& !getPlayer().getItems().playerHasEquipped(9703)
						&& !getPlayer().getItems().playerHasItem(9704) 
						&& !getPlayer().getItems().bankContains(9704)
						&& !getPlayer().getItems().playerHasEquipped(9704)){
					getPlayer().getItems().addItem(9703, 1);
					getPlayer().getItems().addItem(9704, 1);
					DialogueManager.sendNpcChat(getPlayer(), 3216, Emotion.DEFAULT, "Here you go.");
					setNext(6);
				} else {
					DialogueManager.sendNpcChat(getPlayer(), 3216, Emotion.DEFAULT, "You allready have them.");
					setNext(6);
				}
				break;
			case DialogueConstants.OPTIONS_2_2:
				DialogueManager.sendClientChat(getPlayer(), Emotion.CALM, "Bye.");
				setNext(6);
				break;
			}
			return false;

	}

}
