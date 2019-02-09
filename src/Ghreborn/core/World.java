package Ghreborn.core;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import Ghreborn.Config;
import Ghreborn.Connection;
import Ghreborn.Server;
import Ghreborn.core.event.Event;
import Ghreborn.core.event.EventManager;
import Ghreborn.core.event.impl.CleanupEvent;
import Ghreborn.core.event.impl.SavingEvent;
import Ghreborn.core.task.Task;
import Ghreborn.core.tick.Tick;
import Ghreborn.model.items.Item;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.Player;
import Ghreborn.model.players.PlayerSave;
import Ghreborn.util.BlockingExecutorService;


/**
 * Holds data global to the game world.
 * @author Graham Edgecombe
 *
 */
public class World {
	
	/**
	 * Logging class.
	 */
	
	private final static Player[] players = new Player[Config.MAX_PLAYERS];
	
	private static final Logger logger = Logger.getLogger(World.class.getName());
	
	/**
	 * World instance.
	 */
	private static final World world = new World();
	
	/**
	 * Gets the world instance.
	 * @return The world instance.
	 */
	public static World getWorld() {
		return world;
	}
	
	/**
	 * An executor service which handles background loading tasks.
	 */
	private BlockingExecutorService backgroundLoader = new BlockingExecutorService(Executors.newSingleThreadExecutor());

	/**
	 * The game engine.
	 */
	private GameEngine engine;
	
	/**
	 * The event manager.
	 */
	private EventManager eventManager;
		
	
	/**
	 * Creates the world and begins background loading tasks.
	 */
	
	public World() {
		backgroundLoader.submit(new Callable<Object>() {
			@Override
			public Object call() throws Exception {
				Connection.initialize();
				return null;
			}
		});

	}
	
	/**
	 * Gets the background loader.
	 * @return The background loader.
	 */
	public BlockingExecutorService getBackgroundLoader() {
		return backgroundLoader;
	}
	
	/**
	 * Initialises the world: loading configuration and registering global
	 * events.
	 * @param engine The engine processing this world's tasks.
	 * @throws IOException if an I/O error occurs loading configuration.
	 * @throws ClassNotFoundException if a class loaded through reflection was not found.
	 * @throws IllegalAccessException if a class could not be accessed.
	 * @throws InstantiationException if a class could not be created.
	 * @throws IllegalStateException if the world is already initialised.
	 */
	public void init(GameEngine engine) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		if(this.engine != null) {
			throw new IllegalStateException("The world has already been initialised.");
		} else {
			this.engine = engine;
			this.eventManager = new EventManager(engine);
			this.register();
		}
	}
	
	/**
	 * Registers global events such as updating.
	 */
	private void register() {
		submit(new SavingEvent());
		submit(new CleanupEvent());
		//submit(new ActionProcessor());
	}
	
	/**
	 * Submits a new event.
	 * @param event The event to submit.
	 */
	public void submit(Event event) {
		this.eventManager.submit(event);
	}
	
	/**
	 * Submits a new Tick
	 */	
	public static void submit(Tick tick) {
		Server.tickManager.submit(tick);
	}
	
	/**
	 * Submits a new task.
	 * @param task The task to submit.
	 */
	public void submit(Task task) {
		this.engine.pushTask(task);
	}
	
	/**
	 * Gets the game engine.
	 * @return The game engine.
	 */
	public GameEngine getEngine() {
		return engine;
	}
	
	/**
	 * Handles an exception in any of the pools.
	 * @param t The exception.
	 */
	public void handleError(Throwable t) {
		logger.severe("An error occurred in an executor service! The server will be halted immediately.");
		t.printStackTrace();
		try {
			for(int i = 0; i < PlayerHandler.players.length; i++) {
				if (PlayerHandler.players[i] != null) {
					PlayerSave.saveGame((Client)PlayerHandler.players[i]);
				}	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.exit(1);
	}

	public static Player[] getPlayers() {
		return PlayerHandler.players;
	}

	
}
