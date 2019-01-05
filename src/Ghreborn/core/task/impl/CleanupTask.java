package Ghreborn.core.task.impl;

import Ghreborn.core.GameEngine;
import Ghreborn.core.task.Task;

/**
 * Performs garbage collection and finalization.
 * @author Graham Edgecombe
 *
 */
public class CleanupTask implements Task {

	@Override
	public void execute(GameEngine context) {
		context.submitWork(new Runnable() {
			public void run() {
				System.runFinalization();
				//System.gc();
				System.out.println("System resources cleaned.");
			}
		});
	}

}
