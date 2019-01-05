package Ghreborn.model.players.skills.agility.impl;

import Ghreborn.event.CycleEvent;
import Ghreborn.event.CycleEventContainer;
import Ghreborn.event.CycleEventHandler;
import Ghreborn.model.players.Client;

/**
 * 
 * @author Micheal / 01053
 *
 */

public class GnomeAgility {

	/**
	 * Variables
	 */

	private boolean WALKING_LOG, CLIMB_NET, CLIMB_BRANCH, WALKING_ROPE, BRANCH_DESCEND, CLIMB_RAIL, CRAWL_PIPE;

	public boolean PERFORMING_ACTION;

	private static boolean AGILITY_WALKING = false;

	private int clicked = 0;

	/**
	 * Sets the player to a walking state for when using certain obstacles.
	 * 
	 * @param player
	 */

	private void SET_ANIMATION(final Client c, final int walkAnimation, final int x, final int y) {
		c.isRunning = false;
		c.getPA().sendFrame36(173, 0);
		c.playerWalkIndex = walkAnimation;
		c.getPA().requestUpdates();
		c.getPA().walkTo5(x, y);
		AGILITY_WALKING = true;
	}

	/**
	 * Resets the agility walking animation after they've completed certain
	 * obstacles
	 * 
	 * @param player
	 */

	private static void RESET_ANIMATION(final Client c) {
		c.isRunning = true;
		c.getPA().sendFrame36(173, 1);
		c.playerWalkIndex = 0x333;
		c.getPA().requestUpdates();
		AGILITY_WALKING = false;
	}

	/**
	 * 
	 * @param player
	 * @param objectType
	 * 
	 */

	public void agilityCourse(final Client c, final int objectType) {
		switch (objectType) {

		/**
		 * Walk across log
		 */

		case 23145:
			if (clicked == 0 && c.absY == 3436) {
				clicked = 1;
				return;
			} else if (clicked == 1 && c.absY == 3436) {
				if (WALKING_LOG) {
					c.sendMessage("You've already used the log and must complete the course.");
					return;
				}
				//if (c.stopPlayerPacket) {
					//return;
				//}
				c.stopPlayerPacket = true;
				while (c.absX != 2474 && c.absY != 3436) {
					c.getPA().walkTo5(2474 - c.absX, 3436 - c.absY);
				}
				SET_ANIMATION(c, 762, 0, -7);
				if (AGILITY_WALKING && c.absY == 3436) {
					WALKING_LOG = true;
					PERFORMING_ACTION = true;
				}
				CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if(c == null || c.disconnected || c.isDead) {
							container.stop();
							return;
						}
						c.stopPlayerPacket = false;
						container.stop();
						if (c.absX == 2474 && c.absY == 3429) {
							c.getPA().addSkillXP(10500, c.playerAgility);
							clicked = 0;
							container.stop();
						}
					}

					@Override
					public void stop() {
						if (c.absY == 3436) {
							RESET_ANIMATION(c);
							PERFORMING_ACTION = false;
							WALKING_LOG = false;
							clicked = 0;
						} else
							RESET_ANIMATION(c);
						PERFORMING_ACTION = false;
					}
				}, 7);
			}
			break;

		/**
		 * Climb net
		 */

		case 23134:
			if (CLIMB_NET) {
				c.sendMessage("You've already used the net you must complete the course!");
				return;
			}
			CLIMB_NET = true;
			c.animation(828);
			c.getPA().movePlayer(c.absX, 3424, 1);
			c.getPA().addSkillXP(4500, c.playerAgility);
			CLIMB_NET = true;
			break;

		/**
		 * Tree branch up
		 */

		case 23559:
			if (CLIMB_BRANCH) {
				c.sendMessage("You've already used the branch you must complete the course!");
				return;
			}
			CLIMB_BRANCH = true;
			c.animation(828);
			c.getPA().movePlayer(2473, 3420, 2);
			c.getPA().addSkillXP(4500, c.playerAgility);
			break;

		/**
		 * Balance rope
		 */

		case 23557:
			if (WALKING_ROPE) {
				c.sendMessage("You've already used the walking rope you must complete the course!");
				return;
			}
			if (clicked == 0 && c.absY == 3420 && c.heightLevel == 2) {
				clicked = 1;
				return;
			} else if (clicked == 1 && c.absY == 3420 && c.heightLevel == 2) {
				while (c.absX != 2477 && c.absY != 3420) {
					c.getPA().walkTo(2477 - c.absX, 3420 - c.absY);
				}
				SET_ANIMATION(c, 762, 6, 0);
				PERFORMING_ACTION = true;
				if (AGILITY_WALKING && c.absY == 3420 && c.heightLevel == 2) {
					WALKING_ROPE = true;
					PERFORMING_ACTION = true;
				}
				CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if(c == null || c.disconnected || c.isDead) {
							container.stop();
							return;
						}
						WALKING_ROPE = true;
						if (c.absX == 2483 && c.absY == 3420) {
							c.getPA().addSkillXP(10500, c.playerAgility);
							clicked = 0;
							container.stop();
						}
					}

					@Override
					public void stop() {
						if (c.absY == 3420 && c.heightLevel == 2) {
							RESET_ANIMATION(c);
							PERFORMING_ACTION = false;
							clicked = 0;
						} else
							RESET_ANIMATION(c);
						PERFORMING_ACTION = false;
					}
				}, 7);
			}
			break;

		/**
		 * Tree branch down
		 */

		case 23560:
		case 23561:
			if (BRANCH_DESCEND) {
				c.sendMessage("You've already used the net you must complete the course!");
				return;
			}
			BRANCH_DESCEND = true;
			c.animation(828);
			c.getPA().movePlayer(c.absX, c.absY, 0);
			c.getPA().addSkillXP(4500, c.playerAgility);
			break;

		/**
		 * Climb railing
		 */

		case 23135:
			if (CLIMB_RAIL) {
				c.sendMessage("You've already used the net you must complete the course!");
				return;
			}
			CLIMB_RAIL = true;
			PERFORMING_ACTION = true;
			c.animation(828);
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if(c == null || c.disconnected || c.isDead) {
						container.stop();
						return;
					}
					if(c == null || c.disconnected || c.isDead) {
						container.stop();
						return;
					}
					c.getPA().movePlayer(c.absX, 3427, 0);
					container.stop();
				}

				@Override
				public void stop() {
					c.turnPlayerTo(c.absX, 3426);
					c.getPA().addSkillXP(4500, c.playerAgility);
					PERFORMING_ACTION = false;
				}
			}, 1);
			break;

		/**
		 * Crawl through the pipe.
		 */

		case 23139:
		case 23138:
			if (CRAWL_PIPE) {
				return;
			}
			if (clicked == 0 && c.absY == 3430) {
				clicked = 1;
				return;
			} else if (clicked == 1 && c.absY == 3430) {

				while (c.absX != 2484 && c.absY != c.objectY - 1) {
					c.getPA().walkTo(2484 - c.absX, (c.objectY - 1) - c.absY);
				}
				c.animation(749);
				SET_ANIMATION(c, 844, 0, 7);
				if (AGILITY_WALKING && c.absY == 3430) {
					PERFORMING_ACTION = true;
					CRAWL_PIPE = true;
				}
				CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if(c == null || c.disconnected || c.isDead) {
							container.stop();
							return;
						}
						if (c.absY == 3437) {
							c.animation(748);
							RESET_ANIMATION(c);
							if (WALKING_LOG && CLIMB_NET && CLIMB_BRANCH && WALKING_ROPE && BRANCH_DESCEND && CLIMB_RAIL) {
								c.getPA().addSkillXP(70000, c.playerAgility);
		                       // Achievements.increase(c, AchievementType.AGILITY, 1);
								c.sendMessage("You have completed the full gnome agility course.");
								c.getItems().addItem(995, 35_000);
								c.sendMessage("You receive 35,000 coins for completing the course!");
							} else {
								c.getPA().addSkillXP(250, c.playerAgility);
								c.sendMessage("You must complete the course to get full experience from the crawl pipe.");
							}
							clicked = 0;
							container.stop();
						}
					}

					@Override
					public void stop() {
						RESET_ANIMATION(c);
						PERFORMING_ACTION = false;
						WALKING_LOG = false;
						CLIMB_NET = false;
						CLIMB_BRANCH = false;
						WALKING_ROPE = false;
						BRANCH_DESCEND = false;
						CLIMB_RAIL = false;
						CRAWL_PIPE = false;
						clicked = 0;
					}
				}, 1);
				break;
			}
		}
	}
}
