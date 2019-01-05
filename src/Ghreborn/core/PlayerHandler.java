package Ghreborn.core;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;


import Ghreborn.Config;
import Ghreborn.Server;
import Ghreborn.model.Location;
import Ghreborn.model.multiplayer_session.MultiplayerSessionFinalizeType;
import Ghreborn.model.multiplayer_session.MultiplayerSessionType;
import Ghreborn.model.npcs.NPCHandler;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.Player;
import Ghreborn.model.players.Player;
import Ghreborn.model.players.PlayerCannon;
import Ghreborn.model.players.PlayerSave;
import Ghreborn.util.Misc;
import Ghreborn.util.Stream;

public class PlayerHandler {
	public static Object lock = new Object();
	public static Player players[] = new Player[Config.MAX_PLAYERS];
	public static String messageToAll = "";

	public static boolean updateAnnounced;
	public static boolean updateRunning;
	public static int updateSeconds;
	public static long updateStartTime;
	private boolean kickAllPlayers = false;
	static {
		for(int i = 0; i < Config.MAX_PLAYERS; i++)
			players[i] = null;
	}
	public static int playerCount;
	private static final Map<String, Player> playerByUsername = new HashMap<>();
	
	private static final Map<Long, Player> playerByHash = new HashMap<>();
	
	private static List<PlayerCannon> cannons = new ArrayList<PlayerCannon>();

	public static PlayerSave save;
	
	public static Player getPlayer(String name) {
		if (name == null)
			return null;
		return playerByUsername.get(name.toLowerCase().replaceAll("_", " "));
	}
	public static Player getPlayer(int playerId) {
        if (playerId < 0 || playerId >= players.length || players[playerId] == null) {
            return null;
        }
        return (Player) players[playerId];
    }

	public final void getDoubleEXPWeekend() {
		Calendar calendar = new GregorianCalendar();
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		Config.doubleEXPWeekend = false;
		if (day > 6) {
			Config.doubleEXPWeekend = true;
			Config.SERVER_EXP_BONUS = 2;
			return;
		} else if (day < 2) {
			Config.doubleEXPWeekend = true;
			Config.SERVER_EXP_BONUS = 2;
			return;
		}
		Config.SERVER_EXP_BONUS = 1;
	}
	public static Optional<Player> getOptionalPlayer(String name) {
		return getPlayers().stream().filter(Objects::nonNull).filter(Player -> Player.playerName.equalsIgnoreCase(name)).findFirst();
	}
	
	
	public static boolean cannonExists(int x, int y, int height) {
		for(PlayerCannon can : cannons) {
			if(can.getX() == x && can.getY() == y && can.getHeight() == height) 
				return true;
		}
		return false;
	}
	
	public static void addNewCannon(PlayerCannon cannon) {
		cannons.add(cannon);
		if(cannon.getGlobalObj() != null)
			Server.getGlobalObjects().add(cannon.getGlobalObj());
	}
	
	public static void removeCannon(PlayerCannon cannon) {
		cannons.remove(cannon);
		if(cannon.getGlobalObj() != null)
			Server.getGlobalObjects().remove(cannon.getGlobalObj());
	}
	
	public static void updateCannon(PlayerCannon cannon) {
		if(cannon.getGlobalObj() != null) {
			Server.getGlobalObjects().updateObject(cannon.getGlobalObj(), cannon.phase.getObj());
		}
	}
	
	public static PlayerCannon claimCannon(Client p) {
		for(PlayerCannon cannon : cannons) {
			if(p.playerName.equalsIgnoreCase(cannon.name))  {
				cannon.c = p;
				return cannon;
			}
		}
		return null;
	}
	
	public static void updateCannonOrientation(PlayerCannon cannon, int animationID) {
		for (Player p : Server.playerHandler.players) {
			if(p != null && p.isActive && p.heightLevel == cannon.getHeight()) {
				if(p.distanceToPoint(cannon.getX(), cannon.getY()) <= 25) {
					p.getPA().createPlayersObjectAnim(cannon.getX(), cannon.getY(), animationID, 10, -1);	
				}
			}
		}
	}

	public boolean newPlayerClient(Client client1) {
		int slot = -1;
		for (int i = 1; i < Config.MAX_PLAYERS; i++) {
			if ((players[i] == null) || players[i].disconnected) {
				slot = i;
				break;
			}
		}
		if (slot == -1)
			return false;
		client1.handler = this;
		client1.index = slot;
		players[slot] = client1;
		players[slot].isActive = true;
		players[slot].connectedFrom = ((InetSocketAddress) client1.getSession().getRemoteAddress()).getAddress().getHostAddress();
		//Server.getLoginLogHandler().logLogin(players[slot], "Login");
		// faster referencing
		playerByUsername.put(client1.playerName.toLowerCase().replaceAll("_", " "), client1);
		playerByHash.put(client1.getUsernameHash(), client1);
		playerCount++;
		return true;
	}
	public void destruct() {
		for(int i = 0; i < Config.MAX_PLAYERS; i++) {
			if(players[i] == null) 
				continue;
			players[i].destruct();
			players[i] = null;
		}
	}

	public static int getPlayerCount() {
		return playerCount;
	}



	public static boolean isPlayerOn(String playerName) {
		// synchronized (PlayerHandler.players) {
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			if (players[i] != null) {
				if (players[i].playerName.equalsIgnoreCase(playerName)) {
					//LOGGER.log(Level.WARNING, "Possible nullity for player " + playerName +"!");
					//LOGGER.log(Level.WARNING, "disconnected = " + players[i].disconnected + ", properLogout = " + players[i].properLogout);
					//LOGGER.log(Level.WARNING, "isActive = " + players[i].isActive);

					return true;
				}
			}
		}
		return false;
		// }
	}
	/**
	 * Create an int array of the specified length, containing all values
	 * between 0 and length once at random positions.
	 * 
	 * @param length
	 *            The size of the array.
	 * @return The randomly shuffled array.
	 */
	private int[] shuffledList(int length) {
		int[] array = new int[length];
		for (int i = 0; i < array.length; i++) {
			array[i] = i;
		}
		Random rand = new Random();
		for (int i = 0; i < array.length; i++) {
			int index = rand.nextInt(i + 1);
			int a = array[index];
			array[index] = array[i];
			array[i] = a;
		}
		return array;
	}
	public void process() {
		try {
			synchronized (lock) {
				if (kickAllPlayers) {
					for (int i = 1; i < Config.MAX_PLAYERS; i++) {
						if (players[i] != null) {
							players[i].disconnected = true;
						}
					}
				}
				getDoubleEXPWeekend();
				int[] randomOrder = shuffledList(Config.MAX_PLAYERS);
			for (int i = 0; i < Config.MAX_PLAYERS; i++) {
				if (players[randomOrder[i]] == null || !players[randomOrder[i]].isActive || !players[randomOrder[i]].initialized)
					continue;
				try {
					Player player = (Player) players[randomOrder[i]];
					if (players[randomOrder[i]].disconnected
							&& (players[randomOrder[i]].logoutDelay.elapsed(90000) || players[randomOrder[i]].properLogout || kickAllPlayers)) {
						if (Server.getMultiplayerSessionListener().inSession(player, MultiplayerSessionType.TRADE)) {
							Server.getMultiplayerSessionListener().finish(player, MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
						}
						Client o = (Client) PlayerHandler.players[randomOrder[i]];
						if (PlayerSave.saveGame(o)) {
							System.out.println("Game saved for player " + players[randomOrder[i]].playerName);
						} else {
							System.out.println("Could not save for " + players[randomOrder[i]].playerName);
						}
						playerByUsername.remove(o.playerName.toLowerCase().replaceAll("_", " "));
						playerByHash.remove(o.getUsernameHash());
						removePlayer(players[randomOrder[i]]);
						players[randomOrder[i]] = null;
						continue;
					}
					//players[randomOrder[i]].processQueuedPackets();
					players[randomOrder[i]].process();
					players[randomOrder[i]].postProcessing();
					players[randomOrder[i]].getNextPlayerMovement();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			for (int i = 0; i < Config.MAX_PLAYERS; i++) {
				if (players[randomOrder[i]] == null || !players[randomOrder[i]].isActive || !players[randomOrder[i]].initialized)
					continue;
				try {
					players[randomOrder[i]].update();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			for (int i = 0; i < Config.MAX_PLAYERS; i++) {
				if (players[randomOrder[i]] == null || !players[randomOrder[i]].isActive || !players[randomOrder[i]].initialized)
					continue;
				try {
					players[randomOrder[i]].onReset();
					
					players[randomOrder[i]].preProcessing();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (updateRunning && !updateAnnounced) {
				updateAnnounced = true;
				Server.UpdateServer = true;
			}
			if (updateRunning && (System.currentTimeMillis() - updateStartTime > (updateSeconds * 1000))) {
				kickAllPlayers = true;
			}
			}
			} catch (Exception e) {
				System.out.println("PlayerHandler" + e);
				e.printStackTrace();
			}
	}
	
	
	public void updateNPC(Player plr, Stream str) {
		// synchronized(plr) {
		updateBlock.currentOffset = 0;

		str.createFrameVarSizeWord(65);
		str.initBitAccess();

		str.writeBits(8, plr.npcListSize);
		int size = plr.npcListSize;
		plr.npcListSize = 0;
		for (int i = 0; i < size; i++) {
			if (plr.rebuildNPCList == false && plr.withinDistance(plr.npcList[i]) == true && !plr.npcList[i].teleporting) {
				plr.npcList[i].updateNPCMovement(str);
				plr.npcList[i].appendNPCUpdateBlock(updateBlock);
				plr.npcList[plr.npcListSize++] = plr.npcList[i];
			} else {
				int id = plr.npcList[i].index;
				plr.npcInListBitmap[id >> 3] &= ~(1 << (id & 7));
				str.writeBits(1, 1);
				str.writeBits(2, 3);
			}
		}

		for (int i = 0; i < NPCHandler.maxNPCs; i++) {
			if (NPCHandler.npcs[i] != null) {
				int id = NPCHandler.npcs[i].index;
				if (plr.rebuildNPCList == false && (plr.npcInListBitmap[id >> 3] & (1 << (id & 7))) != 0) {

				} else if (plr.withinDistance(NPCHandler.npcs[i]) == false) {

				} else {
					plr.addNewNPC(NPCHandler.npcs[i], str, updateBlock);
				}
			}
		}

		plr.rebuildNPCList = false;

		if (updateBlock.currentOffset > 0) {
			str.writeBits(14, 16383);
			str.finishBitAccess();
			str.writeBytes(updateBlock.buffer, updateBlock.currentOffset, 0);
		} else {
			str.finishBitAccess();
		}
		str.endFrameVarSizeWord();
		// }
	}

	private Stream updateBlock = new Stream(new byte[Config.BUFFER_SIZE]);
	
	public void updatePlayer(Player plr, Stream str) {
		try {
		// synchronized(plr) {
		updateBlock.currentOffset = 0;
		if (updateRunning && !updateAnnounced) {
			str.createFrame(114);
			str.writeWordBigEndian(updateSeconds * 50 / 30);
		}
		plr.updateThisPlayerMovement(str);
		boolean saveChatTextUpdate = plr.isChatTextUpdateRequired();
		plr.setChatTextUpdateRequired(false);
		plr.appendPlayerUpdateBlock(updateBlock);
		plr.setChatTextUpdateRequired(saveChatTextUpdate);
		str.writeBits(8, plr.playerListSize);
		int size = plr.playerListSize;
		if (size >= 250)
			size = 250;
		plr.playerListSize = 0;
		for (int i = 0; i < size; i++) {
			if (!plr.didTeleport && !plr.playerList[i].didTeleport && plr.withinDistance(plr.playerList[i])) {
				plr.playerList[i].updatePlayerMovement(str);
				plr.playerList[i].appendPlayerUpdateBlock(updateBlock);
				plr.playerList[plr.playerListSize++] = plr.playerList[i];
			} else {
				int id = plr.playerList[i].index;
				plr.playerInListBitmap[id >> 3] &= ~(1 << (id & 7));
				str.writeBits(1, 1);
				str.writeBits(2, 3);
			}
		}

		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			if (players[i] == null || !players[i].isActive || players[i] == plr)
				continue;
			int id = players[i].index;
			if ((plr.playerInListBitmap[id >> 3] & (1 << (id & 7))) != 0)
				continue;
			if (!plr.withinDistance(players[i]))
				continue;
			plr.addNewPlayer(players[i], str, updateBlock);
		}

		if (updateBlock.currentOffset > 0) {
			str.writeBits(11, 2047);
			str.finishBitAccess();
			str.writeBytes(updateBlock.buffer, updateBlock.currentOffset, 0);
		} else
			str.finishBitAccess();

		str.endFrameVarSizeWord();
		// }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void removePlayer(Player plr) {
		plr.destruct();
	}

	public static ArrayList<Player> playersWithin(Location loc, int distance) {
		ArrayList<Player> list = new ArrayList<Player>();
		for (Player plr : players) {
			if (plr != null && Math.abs(plr.getX() - loc.getX()) <= distance
					&& Math.abs(plr.getY() - loc.getY()) <= distance) {
				list.add((Player) plr);
			}
		}
		return list;
	}
	public static void executeGlobalMessage(String message) {
		Player[] clients = (Player[]) new Player[players.length];
		System.arraycopy(players, 0, clients, 0, players.length);
		Arrays.asList(clients).stream().filter(Objects::nonNull).forEach(player -> player.sendMessage(message));
	}
	
	public static void executeBroadcast(String message) {
		System.out.println("Created new broadcast: " + message);
		Player[] clients = (Player[]) new Player[players.length];
		System.arraycopy(players, 0, clients, 0, players.length);
		Arrays.asList(clients).stream().filter(Objects::nonNull).forEach(player -> player.getPA().sendFrame126(message, 3755));
	}
	public static List<Player> getPlayers() {
		Player[] clients = new Player[players.length];
		System.arraycopy(players, 0, clients, 0, players.length);
		return Arrays.asList(clients).stream().filter(Objects::nonNull).collect(Collectors.toList());
	}

	public static List<Player> getPlayerList() {
		return Arrays.asList(players);
	}

	public static java.util.stream.Stream<Player> stream() {
		return Arrays.stream(players);
	}

	// lmao this is the wrong one nayway

	public static Player getPlayerByLongName(long name) {
		return playerByHash.get(name);
	}
	public static int getPlayersOnline() {
	int count = 0;
	//int countAdded = 25;
		for (int i = 0; i < PlayerHandler.players.length; i++) { 
			if (PlayerHandler.players[i] != null && PlayerHandler.players[i].isActive) {
				count++;
			}
		}
		return count;
	}
	public static java.util.stream.Stream<Player> nonNullStream() {
		return Arrays.stream(players).filter(Objects::nonNull);
	}
	public static void messageAllStaff(String text, boolean message) {
		for (Player p : PlayerHandler.players) {
			Client o = (Client) p;
			if (o != null) {
				if (o.getRights().isStaff()) {
					if (message) {
						o.sendMessage(text);
					} else {
						o.sendMessage("Alert##Ticket Center##" + text + "##Help");
					}
				}
			}
		}
	}

	public static Client getPlayerByName(String playerName) {
		for(int i = 0; i < Config.MAX_PLAYERS; i++) {
			if(players[i] != null){
				if(players[i].playerName.equalsIgnoreCase(playerName)) {
					return (Client) players[i];
				}
			}
		}
		return null;
	}


	
}
