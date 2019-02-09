package Ghreborn.model.players.skills.fletching;

import Ghreborn.Config;
import Ghreborn.event.event.Event2;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.skills.Skill;

public class StringCrossbowEvent extends Event2<Client> {

	private FletchableCrossbow crossbow;

	public StringCrossbowEvent(FletchableCrossbow b, Client attachment, int ticks) {
		super("skilling", attachment, ticks);
		this.crossbow = b;
	}

	@Override
	public void execute() {
		if (attachment == null || attachment.disconnected || attachment.getSession() == null) {
			stop();
			return;
		}
		if (!attachment.getItems().playerHasItem(crossbow.getItem()) || !attachment.getItems().playerHasItem(9438)) {
			stop();
			return;
		}
		attachment.startAnimation(crossbow.getAnimation());
		attachment.getItems().deleteItem2(crossbow.getItem(), 1);
		attachment.getItems().deleteItem2(9438, 1);
		attachment.getItems().addItem(crossbow.getProduct(), 1);
		attachment.getPA().addSkillXP((int) crossbow.getExperience() * (attachment.getRights().isIronman() ? 1 : Config.FLETCHING_EXPERIENCE), Skill.FLETCHING.getId());
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

