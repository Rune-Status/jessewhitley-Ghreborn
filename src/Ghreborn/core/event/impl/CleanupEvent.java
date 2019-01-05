package Ghreborn.core.event.impl;

import Ghreborn.core.World;
import Ghreborn.core.event.Event;
import Ghreborn.core.task.impl.CleanupTask;

/**
 * An event which runs periodically and performs tasks such as garbage
 * collection.
 * @author Graham Edgecombe
 *
 */
public class CleanupEvent extends Event {

	/**
	 * The delay in milliseconds between consecutive cleanups.
	 */
	public static final int CLEANUP_CYCLE_TIME = 300000;
	
	/**
	 * Creates the cleanup event to run every 5 minutes.
	 */
	public CleanupEvent() {
		super(CLEANUP_CYCLE_TIME);
	}

	@Override
	public void execute() {
		World.getWorld().submit(new CleanupTask());
	}

}
