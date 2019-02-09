package Ghreborn.model.players.skills.crafting;

import Ghreborn.Config;
import Ghreborn.event.CycleEvent;
import Ghreborn.event.CycleEventContainer;
import Ghreborn.event.CycleEventHandler;
import Ghreborn.model.players.Client;

public class LeatherMaking extends CraftingData {

	public static void craftLeatherDialogue(final Client c, final int itemUsed, final int usedWith) {
		for (final leatherData l : leatherData.values()) {
			final int leather = (itemUsed == 1733 ? usedWith : itemUsed);
			if (leather == l.getLeather()) {
				if (l.getLeather() == 1741) {
					c.getPA().showInterface(2311);
					c.leatherType = leather;
					c.getCrafting().hideType = leather;
					c.craftDialogue = true;
					return;
				}
				String[] name = {
						"Body", "Chaps", "Bandana", "Boots", "Vamb",
				};
				if (l.getLeather() == 6289) {
					c.getPA().sendChatInterface(8938);
					c.getPA().itemOnInterface(8941, 180, 6322);
					c.getPA().itemOnInterface(8942, 180, 6324);
					c.getPA().itemOnInterface(8943, 180, 6326);
					c.getPA().itemOnInterface(8944, 180, 6328);
					c.getPA().itemOnInterface(8945, 180, 6330);
					for (int i = 0; i < name.length; i++) {
						c.getPA().sendFrame126(name[i], 8949 + (i * 4));
					}
					c.leatherType = leather;
					c.getCrafting().hideType = leather;
					c.craftDialogue = true;
					return;
				}
			}
		}
		for (final leatherDialogueData d : leatherDialogueData.values()) {
			final int leather = (itemUsed == 1733 ? usedWith : itemUsed);
			String[] name = {
					"Vamb", "Chaps", "Body",
			};
			if (leather == d.getLeather()) {
				c.getPA().sendChatInterface(8880);
				c.getPA().itemOnInterface(8883, 180, d.getVamb());
				c.getPA().itemOnInterface(8884, 180, d.getChaps());
				c.getPA().itemOnInterface(8885, 180, d.getBody());
				for (int i = 0; i < name.length; i++) {
					c.getPA().sendFrame126(name[i], 8889 + (i * 4));
				}
				c.leatherType = leather;
				c.getCrafting().hideType = leather;
				c.craftDialogue = true;
				return;
			}
		}
	}

	private static int amount;
	
	public static void craftLeather(final Client c, final int buttonId) {
		if (c.playerIsCrafting == true) {
			return;
		}
		for (final leatherData l : leatherData.values()) {

			if (buttonId == l.getButtonId(buttonId)) {
				if (c.leatherType == l.getLeather()) {
					if (c.playerLevel[12] < l.getLevel()) {
						c.sendMessage("You need a crafting level of "+ l.getLevel() +" to make this.");
						c.getPA().removeAllWindows();
						return;
					}
					if (!c.getItems().playerHasItem(1734)) {
						c.sendMessage("You need some thread to make this.");
						c.getPA().removeAllWindows();
						return;
					}
					if (!c.getItems().playerHasItem(c.leatherType, l.getHideAmount())) {
						c.getItems();
						c.getItems();
						c.sendMessage("You need "+ l.getHideAmount() +" "+ c.getItems().getItemName(c.leatherType).toLowerCase() +" to make "+ c.getItems().getItemName(l.getProduct()).toLowerCase()+".");
						c.getPA().removeAllWindows();
						return;
					}
					c.animation(1249);
					c.getPA().removeAllWindows();
					c.playerIsCrafting = true;
					amount = l.getAmount(buttonId);
					CycleEventHandler.getSingleton().addEvent(3, c, new CycleEvent() {
						@SuppressWarnings({ "unused", "null" })
						@Override
						public void execute(CycleEventContainer container) {
							if( c == null || c.disconnected || c.teleporting || c.isDead) {
								container.stop();
								return;
							}
							if (c.playerIsCrafting == true) {
								if (!c.getItems().playerHasItem(1734)) {
									c.sendMessage("You have run out of thread.");
									container.stop();
									return;
								}
								if (!c.getItems().playerHasItem(c.leatherType, l.getHideAmount())) {
									c.sendMessage("You have run out of leather.");
									container.stop();
									return;
								}
								if (amount == 0) {
									container.stop();
									return;
								}
								c.getItems().deleteItem(1734, c.getItems().getItemSlot(1734), 1);
								c.getItems().deleteItem2(c.leatherType, l.getHideAmount());
								c.getItems().addItem(l.getProduct(), 1);
								c.getItems();
								c.getItems();
								c.sendMessage("You make "+ ((c.getItems().getItemName(l.getProduct()).contains("body")) ? "a" : "some") +" "+ c.getItems().getItemName(l.getProduct()) +".");
								c.getPA().addSkillXP((int) l.getXP()*(c.getRights().isIronman() ? Config.Ironman_exp_rate : Config.CRAFTING_EXPERIENCE), 12);
								c.animation(1249);
								amount--;
								if (!c.getItems().playerHasItem(1734)) {
									c.sendMessage("You have run out of thread.");
									container.stop();
									return;
								}
								if (!c.getItems().playerHasItem(c.leatherType, l.getHideAmount())) {
									c.sendMessage("You have run out of leather.");
									container.stop();
									return;
								}
							} else {
								container.stop();
							}
						}
						@Override
						public void stop() {
							c.playerIsCrafting = false;
							c.craftDialogue = false;
						}
					}, 5);
				}
			}
		}
	}
}
