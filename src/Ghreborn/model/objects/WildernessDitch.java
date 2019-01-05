package Ghreborn.model.objects;


import Ghreborn.event.CycleEvent;
import Ghreborn.event.CycleEventContainer;
import Ghreborn.event.CycleEventHandler;
import Ghreborn.model.players.Client;
import com.google.common.base.Stopwatch;

/**
 * Handles the wilderness ditch event.
 * @author Chris
 * @date Aug 12, 2015 8:32:41 AM
 *
 */
public class WildernessDitch {
	
	private static final int ANIMATION_ID = 6132;
	private static int OFFSET_Y = 3;
	
	/**
	 * An instance of the stopwatch to track the player's last ditch hop.
	 */
	@SuppressWarnings("unused")
	private Stopwatch stopwatch = Stopwatch.createUnstarted();
	
	/**
	 * Whether our player is in the wilderness.
	 * @param c	the player
	 * @return true if the player's absolute x positiion is 3523
	 */
	public static boolean inWilderness(Client c) {
		return c.getY() >= 3523;
	}

	/**
	 * Crosses the ditch.
	 * @param c	the player
	 * @param x	the x-coordinate to walk to
	 * @param y the y-coordinate to walk to
	 */
	public static void crossDitch(Client c, int x, int y) {
		c.resetWalkingQueue();
		c.teleportToX = x;
       	c.teleportToY = y;
		c.getPA().requestUpdates();
	}

	/**
	 * Starts the enter process for the player.
	 * @param c	the player
	 */
	public static void enter(final Client c) {
		@SuppressWarnings("unused")
		final long lastEntered = System.currentTimeMillis();
		//long time = stopwatch.elapsed(TimeUnit.MILLISECONDS);
		if (c.stopPlayerPacket) {
			return;
		}
		c.stopPlayerPacket = true;
		c.animation(ANIMATION_ID);
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				crossDitch(c, c.absX, c.absY + OFFSET_Y);
				if (c.absY <= 3523) {
					container.stop();
				} else if (c.absX <= 2998) {
					container.stop();
				}
			}

			@Override
			public void stop() {
				resetWalkIndex(c);
				c.stopPlayerPacket = false;
			}
		}, 1);
	}
	
	

	/**
	 * Starts the leave process for the player.
	 * @param c	the player
	 */
	public static void leave(final Client c) {
		@SuppressWarnings("unused")
		final long lastEntered = System.currentTimeMillis();

		if (c.stopPlayerPacket) {
			return;
		}
		c.stopPlayerPacket = true;
		c.animation(ANIMATION_ID);
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				crossDitch(c, c.absX, c.absY - OFFSET_Y);
				if (c.absY <= 3523) {
					container.stop();
				} else if (c.absX <= 2995) {
					container.stop();
				}
			}

			@Override
			public void stop() {
				resetWalkIndex(c);
				c.stopPlayerPacket = false;
			}
		}, 1);
	}
	
	/**
	 * Resets the player's walk index when the event has concluded.
	 * @param c	the player
	 */
	private static void resetWalkIndex(Client c) {
		c.isRunning2 = true;
		c.getPA().sendFrame36(173, 1);
		c.playerWalkIndex = 0x333;
		c.getPA().requestUpdates();
	}
}
