package Ghreborn;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import Ghreborn.clip.Region;
import Ghreborn.clip.doors.DoorDefinition;
import Ghreborn.core.GameEngine;
import Ghreborn.core.PlayerHandler;
import Ghreborn.core.World;
import Ghreborn.core.task.impl.CleanupTask;
import Ghreborn.definitions.ItemCacheDefinition;
import Ghreborn.definitions.NPCCacheDefinition;
import Ghreborn.definitions.ObjectDef;
import Ghreborn.event.CycleEvent;
import Ghreborn.event.CycleEventContainer;
import Ghreborn.event.CycleEventHandler;
import Ghreborn.model.npcs.NPCHandler;
import Ghreborn.model.npcs.drop.NpcDropSystem;
import Ghreborn.model.objects.Doors;
import Ghreborn.model.objects.DoubleDoors;
import Ghreborn.model.players.Client;
import Ghreborn.model.region.music.MusicLoader;
import Ghreborn.util.Benchmark;
import Ghreborn.util.Misc;
import Ghreborn.util.json.EquipmentRequirementLoader;
import Ghreborn.util.json.ItemDefinitionLoader;
import Ghreborn.util.json.NpcDefinitionLoader;
import Ghreborn.util.json.NpcDropCacheLoader;
import Ghreborn.util.json.NpcDropTableLoader;
import Ghreborn.world.GlobalDropsHandler;


/**
 * 
 * @author Graham Edgecombe
 * @author Jinrake
 * Hyperion
 *
 */
public class RS2Server {

	/**
	 * Logger instance.
	 */
	private static final Logger logger = Logger.getLogger(RS2Server.class.getName());

	/**
	 * The <code>GameEngine</code> instance.
	 */
	private static final GameEngine engine = new GameEngine();

	/**
	 * Creates the server and the <code>GameEngine</code> and initializes the
	 * <code>World</code>.
	 * @throws IOException if an I/O error occurs loading the world.
	 * @throws ClassNotFoundException if a class the world loads was not found.
	 * @throws IllegalAccessException if a class loaded by the world was not accessible.
	 * @throws InstantiationException if a class loaded by the world was not created.
	 */
	public RS2Server() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		World.getWorld().init(engine);
	}

	/**
	 * Starts the <code>GameEngine</code>.
	 * @throws ExecutionException if an error occured during background loading.
	 * @throws IOException 
	 */
	public void start() throws ExecutionException {
		try {
		if(World.getWorld().getBackgroundLoader().getPendingTaskAmount() > 0) {
			logger.info("Waiting for pending background loading tasks...");
			World.getWorld().getBackgroundLoader().waitForPendingTasks();
		}
		CycleEventHandler.getSingleton();
		//GlobalDropsHandler.initialize();
		new ItemDefinitionLoader().load();
		new NpcDefinitionLoader().load();
		new NpcDropTableLoader().load();
		new NpcDropCacheLoader().load();
		NpcDropSystem.get().loadDrops();
		NpcDropSystem.get().loadRareDrops();
		DoorDefinition.load();
		try {
			new MusicLoader().load();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		new EquipmentRequirementLoader().load();
		ItemCacheDefinition.unpackConfig();
		NPCCacheDefinition.unpackConfig();
		World.getWorld().getBackgroundLoader().shutdown();
		ObjectDef.loadConfig();
        Region.load();
        Server.startMinutesCounter();
        try {
			Server.globalObjects.loadGlobalObjectFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Doors.getSingleton().load();
		DoubleDoors.getSingleton().load();
		NPCHandler.loadDefs();
		//startWinterSpawnTimer();
		engine.start();	
		logger.info("Setting up login channels...");
		Server.setupLoginChannels();
		logger.info(Config.SERVER_NAME+" accpeting incoming connections...");
		World.getWorld().submit(new CleanupTask());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * Gets the <code>GameEngine</code>.
	 * @return The game engine.
	 */
	public static GameEngine getEngine() {
		return engine;
	}
	
}
