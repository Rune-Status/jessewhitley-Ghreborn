package Ghreborn.model.players.skills.crafting;

import Ghreborn.Config;
import Ghreborn.event.CycleEvent;
import Ghreborn.event.CycleEventContainer;
import Ghreborn.event.CycleEventHandler;
import Ghreborn.model.items.Item;
import Ghreborn.model.players.Client;

public class GlassBlowing extends GlassData {

	private static int amount;

	public static void glassBlowing(final Client c, final int buttonId) {
		if (c.playerIsCrafting) {
			return;
		}
		for (final glassData g : glassData.values()) {
			if (buttonId == g.getButtonId(buttonId)) {
				if (c.playerLevel[12] < g.getLevelReq()) {
					c.sendMessage("You need a crafting level of " + g.getLevelReq() + " to make this.");
					c.getPA().removeAllWindows();
					return;
				}
				if (!c.getItems().playerHasItem(1775, 1)) {
					c.sendMessage("You have run out of molten glass.");
					return;
				}
				c.animation(884);
				c.getPA().removeAllWindows();
				c.playerIsCrafting = true;
				amount = g.getAmount(buttonId);
				CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if (c == null || c.disconnected || c.getSession() == null) {
							stop();
							return;
						}
						if (c.playerIsCrafting) {
							if (amount == 0) {
								container.stop();
								return;
							}
							if (!c.getItems().playerHasItem(1775, 1)) {
								c.sendMessage("You have run out of molten glass.");
								container.stop();
								return;
							}
							c.getItems().deleteItem(1775, 1);
							c.getItems().addItem(g.getNewId(), 1);
							c.sendMessage("You make a " + Item.getItemName(g.getNewId()) + ".");
							c.getPA().addSkillXP(g.getXP() * (c.getRights().isIronman() ? Config.Ironman_exp_rate : Config.CRAFTING_EXPERIENCE), 12);
							c.animation(884);
							amount--;
						} else {
							container.stop();
						}
					}
					@Override
					public void stop() {
						c.stopAnimation();
						c.playerIsCrafting = false;
					}
				}, 3);
			}
		}
	}

	public static void makeGlass(final Client c, final int itemUsed,
			final int usedWith) {
		final int blowPipeId = (itemUsed == 1785 ? usedWith : itemUsed);
		c.getPA().showInterface(11462);
		for (final glassData g : glassData.values()) {
			if (blowPipeId == g.getNewId()) {
				c.getItems().deleteItem(1759, 1);
				c.getItems().addItem(g.getNewId(), 1);
				c.getPA().addSkillXP(4, 12);
			}
		}
	}

}