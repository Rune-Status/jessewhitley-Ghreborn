package Ghreborn;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.util.HashedWheelTimer;

import Ghreborn.core.GameEngine;
import Ghreborn.core.PlayerHandler;
import Ghreborn.core.World;
import Ghreborn.core.task.Task;
import Ghreborn.core.tick.Tick;
import Ghreborn.core.tick.TickManager;
import Ghreborn.data.ServerData;
import Ghreborn.definitions.ObjectDef;
import Ghreborn.event.CycleEventHandler;
import Ghreborn.event.TaskScheduler;
import Ghreborn.event.event.EventHandler;
import Ghreborn.model.content.clan.ClanManager;
import Ghreborn.model.minigames.FightPits;
import Ghreborn.model.multiplayer_session.MultiplayerSessionListener;
import Ghreborn.model.npcs.NPCHandler;
import Ghreborn.model.npcs.drops.DropList;
import Ghreborn.model.objects.Doors;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.Player;
import Ghreborn.net.PipelineFactory;
import Ghreborn.util.ShutDownHook;
import Ghreborn.util.flood.Flooder;
import Ghreborn.util.json.ItemDefinitionLoader;
import Ghreborn.util.json.NpcDefinitionLoader;
import Ghreborn.world.ItemHandler;
import Ghreborn.world.ObjectHandler;
import Ghreborn.world.ObjectManager;
import Ghreborn.world.ShopHandler;
import Ghreborn.world.objects.GlobalObjects;
import Ghreborn.util.Benchmark;
import Ghreborn.util.ControlPanel;

/**
 * Server.java
 *
 * @author Sanity
 * @author Graham
 * @author Blake
 * @author Ryan Lmctruck30
 *
 */

public class Server {
	
	public static boolean
	UpdateServer = false,
	shutdownClientHandler;	

	public static int
	serverlistenerPort = 43594;
	
	private static long minutesCounter;
	public static ItemHandler itemHandler = new ItemHandler();
	public static PlayerHandler playerHandler = new PlayerHandler();
    public static NPCHandler npcHandler = new NPCHandler();
	public static ShopHandler shopHandler = new ShopHandler();
	public static ObjectHandler objectHandler = new ObjectHandler();
	public static ObjectManager objectManager = new ObjectManager();
	public static FightPits fightPits = new FightPits();
	public static TickManager tickManager = new TickManager();
	//public static ClanChatHandler clanChat = new ClanChatHandler();
	public static GlobalObjects globalObjects = new GlobalObjects();
	public static DropList droplist = new DropList();
	public static ObjectDef Obj = new ObjectDef();
	/**
	 * A class that will manage game events
	 */
	static final EventHandler events = new EventHandler();

	/**
	 * Contains data which is saved between sessions.
	 */
	public static ServerData serverData = new ServerData();

	public static ControlPanel panel = new ControlPanel(true); // false if you want it off
	
	/**
	 * The flooder used to stress-test the server.
	 */
	private static Flooder flooder = new Flooder();

	/**
	 * ClanChat Added by Valiant
	 */
	public static ClanManager clanManager = new ClanManager();

	private static final TaskScheduler taskScheduler = new TaskScheduler();

	public static final GameEngine ENGINE = new GameEngine();
	private static MultiplayerSessionListener multiplayerSessionListener = new MultiplayerSessionListener();
	public static TaskScheduler getTaskScheduler() {
		return taskScheduler;
	}
	public static Flooder getFlooder() {
		return flooder;
	}
	public static ObjectDef getObjectDef() {
		return Obj;
	}
	public static DropList getDropList(){
		return droplist;
	}
	public static void main(String[] args) throws Exception {
/*		com.everythingrs.playersonline.PlayersOnline.service.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				int online = 0;
				for (Player player : PlayerHandler.players) {
					if (player != null) {
						online += 1;
					}
				}
				com.everythingrs.playersonline.PlayersOnline.insert("1yas9sbywkw3j71agw4iiicnmi251x4tx8auv1vcz8c2d0io1orma598wc2jvhvgxtmhu4k7qfr", online, false);
			}
		}, 0, 30, TimeUnit.SECONDS);*/
		new Server().run();
	}

	
	public void run() {
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
	try {
		World.getWorld();
		try {
			new RS2Server().start();
		} catch(Exception ex) {
			System.out.println("Error starting the Ghreborn...");
			ex.printStackTrace();
			System.exit(1);
		}
		Runtime.getRuntime().addShutdownHook(new ShutDownHook());
			while(true) {
				long start = System.nanoTime() / 1000000L;
				World.getWorld().submit(new Task() {
					@Override
					public void execute(GameEngine context) {
						Iterator<Tick> tickIt$ = tickManager.getTickables().iterator();
						while(tickIt$.hasNext()) {
							Tick t = tickIt$.next();
							t.cycle();
							if(!t.isRunning()) {
								tickIt$.remove();
							}
						}
					}					
				});
				events.process();
				CycleEventHandler.getSingleton().process();
				World.getWorld().submit(new Task() {
					@Override
					public void execute(GameEngine context) {
						Server.playerHandler.process();			
					}
				});
				World.getWorld().submit(new Task() {
					@Override
					public void execute(GameEngine context) {
						Server.npcHandler.process();
					}
				});
				World.getWorld().submit(new Task() {
					@Override
					public void execute(GameEngine context) {						
						Server.shopHandler.process();
					}
				});
				World.getWorld().submit(new Task() {
					@Override
					public void execute(GameEngine context) {
						Server.objectManager.process();
					}
				});
				World.getWorld().submit(new Task() {
					@Override
					public void execute(GameEngine context) {
						Benchmark.start("ItemHandler");
						Server.itemHandler.process();
					}
				});
				World.getWorld().submit(new Task() {
					@Override
					public void execute(GameEngine context) {
						Benchmark.start("ItemHandler");
						Server.globalObjects.pulse();
					}
				});
				long sleepTime = 600-(System.nanoTime() / 1000000L - start);
				if (sleepTime > 0) {
					Thread.sleep(sleepTime);
				} else {
					// The server has reached maximum load, players may now lag.
					System.out.println("[WARNING]: Server load: " + (100 + (Math.abs(sleepTime) / (Config.CYCLE_TIME / 100))) + "%!");
				}
			}		
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	static void startMinutesCounter() {
		try {
			BufferedReader minuteFile = new BufferedReader(new FileReader("./data/minutes.log"));
			Server.minutesCounter = Integer.parseInt(minuteFile.readLine());
		} catch (Exception e) {
			e.printStackTrace();
		}

		World.submit(new Tick(25) {
		    @Override public void execute() {
		        setMinutesCounter(getMinutesCounter() + 1);
                for (Player player : World.getPlayers()) {
                  if (player == null) {
                      continue;
                  }
                  player.getAllotment().doCalculations();
                  //player.getFlowers().doCalculations();
                 // player.getHerbs().doCalculations();
                  //player.getHops().doCalculations();
                  player.getBushes().doCalculations();
                  //player.getTrees().doCalculations();
                  //player.getFruitTrees().doCalculations();
                  //player.getSpecialPlantOne().doCalculations();
                  //player.getSpecialPlantTwo().doCalculations(); //lowering all player items timer
	              //ItemManager.getInstance().lowerAllItemsTimers(player);
                }
		    }
        });

	}
	public static void setupLoginChannels() {
		/**
		 * Accepting Connections
		 */
        ServerBootstrap serverBootstrap = new ServerBootstrap (new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
        serverBootstrap.setPipelineFactory (new PipelineFactory(new HashedWheelTimer()));
        //serverBootstrap.bind (new InetSocketAddress(serverlistenerPort));
		try {
	        serverBootstrap.bind (new InetSocketAddress(serverlistenerPort));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void setMinutesCounter(long minutesCounter) {
		Server.minutesCounter = minutesCounter;
		try {
			BufferedWriter minuteCounter = new BufferedWriter(new FileWriter("./data/minutes.log"));
			minuteCounter.write(Long.toString(getMinutesCounter()));
			minuteCounter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static long getMinutesCounter() {
		return minutesCounter;
	}

	public static ServerData getServerData() {
		return serverData;
	}

	public static GlobalObjects getGlobalObjects() {
		return globalObjects;
	}
	public static MultiplayerSessionListener getMultiplayerSessionListener() {
		return multiplayerSessionListener;
	}
	public static EventHandler getEventHandler() {
		return events;
	}
}
