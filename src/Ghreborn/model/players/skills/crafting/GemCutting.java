package Ghreborn.model.players.skills.crafting;

import Ghreborn.Config;
import Ghreborn.event.CycleEvent;
import Ghreborn.event.CycleEventContainer;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.skills.Skill;
import Ghreborn.util.Misc;

public class GemCutting extends CraftingData {
	
	public static void cutGem(final Client c, final int itemUsed, final int usedWith) {
		if (c.playerIsCrafting == true) {
			return;
		}
		final int itemId = (itemUsed == 1755 ? usedWith : itemUsed);
		for (final cutGemData g : cutGemData.values()) {
			if (itemId == g.getUncut()) {
				if (c.playerLevel[12] < g.getLevel()) {
					c.sendMessage("You need a crafting level of "+ g.getLevel() +" to cut this gem.");
					return;
				}
				if (!c.getItems().playerHasItem(itemId)) {
					return;
				}
				c.playerIsCrafting = true;
				c.getSkilling().stop();
				c.getSkilling().setSkill(Skill.CRAFTING);
				c.getSkilling().add(new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if(c == null || c.disconnected || c.teleporting || c.isDead) {
							container.stop();
							return;
						}
						if (c.playerIsCrafting == true) {
							if (c.getItems().playerHasItem(itemId)) {
								if (Misc.random(150) == 0) {
									container.stop();
									return;
								}
								c.getItems().deleteItem(itemId, 1);
								c.getItems().addItem(g.getCut(), 1);	
								c.getPA().addSkillXP((int) g.getXP()*Config.CRAFTING_EXPERIENCE, 12);
								c.sendMessage("You cut the "+ c.getItems().getItemName(itemId).toLowerCase() +".");
								c.animation(g.getAnimation());
							} else {
								container.stop();
							}
						} else {
							container.stop();
						}
					}
					@Override
					public void stop() {
						c.playerIsCrafting = false;
					}
				}, 2);
				return;
			}
		}
	}
}

