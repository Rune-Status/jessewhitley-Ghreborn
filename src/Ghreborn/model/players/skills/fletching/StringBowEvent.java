package Ghreborn.model.players.skills.fletching;

import Ghreborn.Config;
import Ghreborn.event.event.Event2;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.skills.Skill;

public class StringBowEvent extends Event2<Client> {

	private FletchableBow bow;

	public StringBowEvent(FletchableBow bow, Client attachment, int ticks) {
		super("skilling", attachment, ticks);
		this.bow = bow;
	}

	@Override
	public void execute() {
		if (attachment == null || attachment.disconnected || attachment.getSession() == null) {
			stop();
			return;
		}
		if (!attachment.getItems().playerHasItem(bow.getItem()) || !attachment.getItems().playerHasItem(1777)) {
			stop();
			return;
		}
		switch (bow.getProduct()) {
/*		case 853:
			if (Boundary.isIn(attachment, Boundary.SEERS_BOUNDARY)) {
				attachment.getDiaryManager().getKandarinDiary().progress(KandarinDiaryEntry.STRING_MAPLE_SHORT);
			}
			break;
		
		case 857:
			DailyTasks.increase(attachment, PossibleTasks.YEW_SHORTBOWS);
			break;
			
		case 859:
			if (Boundary.isIn(attachment, Boundary.LLETYA_BOUNDARY)) {
				attachment.getDiaryManager().getWesternDiary().progress(WesternDiaryEntry.STRING_MAGE_LONG);
			}
			break;*/
		}
		attachment.animation(bow.getAnimation());
		attachment.getItems().deleteItem2(bow.getItem(), 1);
		attachment.getItems().deleteItem2(1777, 1);
		attachment.getItems().addItem(bow.getProduct(), 1);
		attachment.getPA().addSkillXP((int) bow.getExperience() * (attachment.getRights().isIronman() ? 1 : Config.FLETCHING_EXPERIENCE), Skill.FLETCHING.getId());
	}

	@Override
	public void stop() {
		super.stop();
		if (attachment == null || attachment.disconnected || attachment.getSession() == null) {
			return;
		}
		attachment.stopAnimation();
	}

}

