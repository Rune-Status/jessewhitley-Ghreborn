package Ghreborn.model.content.dialogue.impl.slayer;

import java.util.Optional;

import Ghreborn.model.content.dialogue.Dialogue;
import Ghreborn.model.content.dialogue.DialogueConstants;
import Ghreborn.model.content.dialogue.DialogueManager;
import Ghreborn.model.content.dialogue.Emotion;
import Ghreborn.model.players.skills.Skill;
import Ghreborn.model.players.skills.slayer.SlayerMaster;
import Ghreborn.model.players.skills.slayer.Task;

public class TuraelDialogue extends Dialogue {

	@Override
	public void execute() {
		switch (getNext()) {
		case 0:
			DialogueManager.sendNpcChat(getPlayer(), 401, Emotion.CALM, "What do you want?");
			setNext(3);
			break;
		case 1:
			getPlayer().getPA().closeAllWindows();
			break;
		case 2:
			
			if (getPlayer().getSlayer().getTask().isPresent()) {
				if (getPlayer().getSlayer().getTask().get().getLevel() > getPlayer().playerLevel[Skill.SLAYER.getId()]) {
					DialogueManager.sendNpcChat(getPlayer(), 401, Emotion.CALM, "You have been assigned a task you cannot complete.",
							"What an inconvenience, i'll get to the bottom of this.",
							"In the meanwhile, i've reset your task.",
							"Talk to me or one of the others to get a new task.");
					getPlayer().getSlayer().setTask(Optional.empty());
					getPlayer().getSlayer().setTaskAmount(0);
					setNext(1);
					return;
				}
			}
			break;
		case 3:
			DialogueManager.sendOption(getPlayer(), "I'd like to see the slayer interface.", "I am in need of a slayer assignment.",
					"Could you tell me where I can find my current task?", "Nothing, sorry!");
			break;
		case 4:
			getPlayer().getSlayer().handleInterface("buy");
			break;
		case 5:
			break;
		case 6:
			DialogueManager.sendOption(getPlayer(), "Yes, I would like an easier task.", "No, I want to keep hunting on my current task.");
			break;
		}
		// TODO Auto-generated method stub

	}
	@Override
	public boolean clickButton(int id) {
		switch(id) {
		case DialogueConstants.OPTIONS_4_1:
			DialogueManager.sendClientChat(getPlayer(), Emotion.CALM, "I'd like to see the slayer interface.");
			setNext(4);
			break;
		case DialogueConstants.OPTIONS_4_2:
			if (!getPlayer().getSlayer().getTask().isPresent()) {
			getPlayer().getSlayer().createNewTask(401);
			}else {
			DialogueManager.sendNpcChat(getPlayer(), 401, Emotion.CALM, 
					"You currently have " + getPlayer().getSlayer().getTaskAmount() + " "
							+ getPlayer().getSlayer().getTask().get().getPrimaryName() + " to kill.",
					"You cannot get an easier task. You must finish this.");
			setNext(2);
			}
			break;
		case DialogueConstants.OPTIONS_4_3:
			break;
		case DialogueConstants.OPTIONS_4_4:
			break;
		case DialogueConstants.OPTIONS_2_1:
			Optional<SlayerMaster> master_npc = SlayerMaster.get(getPlayer().talkingNpc);
			if (getPlayer().getSlayer().getMaster() != master_npc.get().getId() && master_npc.get().getId() != 401) {
				DialogueManager.sendNpcChat(getPlayer(), 401, Emotion.CALM, "You already seem to have an active task with someone else.");
				setNext(2);
				return false;
			}
			getPlayer().getSlayer().createNewTask(getPlayer().talkingNpc);
			break;
		case DialogueConstants.OPTIONS_2_2:
			getPlayer().getPA().closeAllWindows();
			break;
		}
		return false;
		
	}
}
