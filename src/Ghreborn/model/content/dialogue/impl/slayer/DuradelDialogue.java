package Ghreborn.model.content.dialogue.impl.slayer;

import java.util.Optional;

import Ghreborn.model.content.dialogue.Dialogue;
import Ghreborn.model.content.dialogue.DialogueConstants;
import Ghreborn.model.content.dialogue.DialogueManager;
import Ghreborn.model.content.dialogue.Emotion;
import Ghreborn.model.players.skills.Skill;
import Ghreborn.model.players.skills.slayer.SlayerMaster;
import Ghreborn.model.players.skills.slayer.Task;

public class DuradelDialogue extends Dialogue {

	@Override
	public void execute() {
		switch (getNext()) {
		case 0:
			if(getPlayer().combatLevel<100){
				DialogueManager.sendNpcChat(getPlayer(), 405, Emotion.ANGRY_1, "Do not waste my time peasent.","You need a Combat level of at least 100.");
				setNext(1);
				return;
				//player.getDH().sendNpcChat2("Do not waste my time peasent.","You need a Combat level of at least 100.",402,"Duradel");
			}
			if (getPlayer().playerLevel[18] < 50) {
				DialogueManager.sendNpcChat(getPlayer(), 405, Emotion.ANGRY_1, "You must have a slayer level of at least 50 weakling.");
				setNext(1);
				return;
				//player.getDH().sendNpcChat1("You must have a slayer level of at least 50 weakling.", 490, "Duradel");
				
			}
			setNext(2);
			break;
		case 1:
			getPlayer().getPA().closeAllWindows();
			break;
		case 2:
			if (getPlayer().getSlayer().getTask().isPresent()) {
				if (getPlayer().getSlayer().getTask().get().getLevel() > getPlayer().playerLevel[Skill.SLAYER.getId()]) {
					DialogueManager.sendNpcChat(getPlayer(), 405, Emotion.CALM, "You have been assigned a task you cannot complete.",
							"What an inconvenience, i'll get to the bottom of this.",
							"In the meanwhile, i've reset your task.",
							"Talk to me or one of the others to get a new task.");
					getPlayer().getSlayer().setTask(Optional.empty());
					getPlayer().getSlayer().setTaskAmount(0);
					setNext(1);
					return;
				}
			}
			DialogueManager.sendNpcChat(getPlayer(), 405, Emotion.CALM, "What do you want?");
			setNext(3);
			break;
		case 3:
			DialogueManager.sendOption(getPlayer(), "I'd like to see the slayer interface.", "I am in need of a slayer assignment.",
					"Could you tell me where I can find my current task?", "Nothing, sorry!");
			break;
		case 4:
			getPlayer().getSlayer().handleInterface("buy");
			break;
		case 5:
			Optional<Task> task = getPlayer().getSlayer().getTask();
			Optional<SlayerMaster> master = SlayerMaster.get(getPlayer().talkingNpc);
			Optional<SlayerMaster> myMaster = SlayerMaster.get(getPlayer().getSlayer().getMaster());

			if (task.isPresent() && master.isPresent() && myMaster.isPresent()) {
				if (getPlayer().getSlayer().getMaster() != master.get().getId()) {
					if (myMaster.get().getLevel() < master.get().getLevel()) {
						DialogueManager.sendNpcChat(getPlayer(), 405, Emotion.CALM, "You have an easier task from a different master.",
								"If you cannot complete their task, you cannot start",
								"one of mine. You must finish theirs first.");
						setNext(2);
						return;
					}
					DialogueManager.sendNpcChat(getPlayer(), 405, Emotion.CALM, "I can give you an easier task but this will reset your",
							"consecutive tasks completed if you have an active task.",
							"Are you sure this is what you want to do?");
					setNext(6);
				}
			}

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
			DialogueManager.sendClientChat(getPlayer(), Emotion.CALM, "I need an assignment.");
			setNext(5);
			break;
		case DialogueConstants.OPTIONS_4_3:
			break;
		case DialogueConstants.OPTIONS_4_4:
			break;
		case DialogueConstants.OPTIONS_2_1:
			Optional<SlayerMaster> master_npc = SlayerMaster.get(getPlayer().talkingNpc);
			if (getPlayer().getSlayer().getMaster() != master_npc.get().getId() && master_npc.get().getId() != 405) {
				DialogueManager.sendNpcChat(getPlayer(), 405, Emotion.CALM, "You already seem to have an active task with someone else.");
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
