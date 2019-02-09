package Ghreborn.model.players;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;


import Ghreborn.Config;
import Ghreborn.core.PlayerHandler;
import Ghreborn.model.content.dailytasks.DailyTasks.PossibleTasks;
import Ghreborn.model.content.dailytasks.TaskTypes;
import Ghreborn.model.content.gambling.Gambling;
import Ghreborn.model.items.GameItem;
import Ghreborn.model.items.bank.BankItem;
import Ghreborn.model.items.bank.BankTab;
import Ghreborn.model.players.skills.slayer.SlayerMaster;
import Ghreborn.model.players.skills.slayer.Task;
import Ghreborn.model.npcs.NPCDeathTracker;
import Ghreborn.util.Misc;

public class PlayerSave {

	
	public static boolean playerExists(String name) {
		File file = new File("./Data/characters/" + name + ".txt");
		return file.exists();
	}

	public static boolean playerExists2(String name) {
		File file = new File("./Data/characters2/" + name + ".txt");
		return file.exists();
	}
	/**
	 * Loads the gambling details
	 */
	public static void loadGambling() {
		try {
			File file = new File("./data/GAMBLING.txt");
			if (!file.exists()) {
				return;
			}
			BufferedReader in = new BufferedReader(new FileReader(file));
			long money = Long.parseLong(in.readLine());
			Gambling.MONEY_TRACKER = money;
			//logger.info("Gambling results " + Misc.formatText(money));
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	

	
	/**
	 * Saves the gambling details
	 */
	public static void saveGambling() {
		try {
			File file = new File("./data/GAMBLING.txt");
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			out.write(String.valueOf(Gambling.MONEY_TRACKER));
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void addItemToFile(String name, int itemId, int itemAmount) {
		if (itemId < 0 || itemAmount < 0) {
			Misc.println("Illegal operation: Item id or item amount cannot be negative.");
			return;
		}
		BankItem item = new BankItem(itemId + 1, itemAmount);
		if (!playerExists(name)) {
			Misc.println("Illegal operation: Player account does not exist, validate name.");
			return;
		}
		if (PlayerHandler.isPlayerOn(name)) {
			Misc.println("Illegal operation: Attempted to modify the account of a player online.");
			return;
		}
		File character = new File("./Data/characters/" + name + ".txt");
		List<String> lines = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(character))) {
			String line;
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
			reader.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		BankTab[] tabs = new BankTab[] { new BankTab(0), new BankTab(1), new BankTab(2), new BankTab(3), new BankTab(4),
				new BankTab(5), new BankTab(6), new BankTab(7), new BankTab(8), };
		String token, token2;
		String[] token3 = new String[3];
		int spot = 0;
		for (String line : lines) {
			if (line == null) {
				continue;
			}
			line = line.trim();
			spot = line.indexOf("=");
			if (spot == -1) {
				continue;
			}
			token = line.substring(0, spot);
			token = token.trim();
			token2 = line.substring(spot + 1);
			token2 = token2.trim();
			token3 = token2.split("\t");
			if (token.equals("bank-tab")) {
				int tabId = Integer.parseInt(token3[0]);
				int id = Integer.parseInt(token3[1]);
				int amount = Integer.parseInt(token3[2]);
				tabs[tabId].add(new BankItem(id, amount));
			}
		}
		boolean inserted = false;
		for (BankTab tab : tabs) {
			if (tab.contains(item) && tab.spaceAvailable(item)) {
				tab.add(item);
				inserted = true;
				break;
			}
		}
		if (!inserted) {
			for (BankTab tab : tabs) {
				if (tab.freeSlots() > 0) {
					tab.add(item);
					inserted = true;
					break;
				}
			}
		}
		if (!inserted) {
			Misc.println("Item could not be added to offline account, no free space in bank.");
			return;
		}
		int startIndex = Misc.indexOfPartialString(lines, "bank-tab");
		int lastIndex = Misc.lastIndexOfPartialString(lines, "bank-tab");
		if (lastIndex != startIndex && startIndex > 0 && lastIndex > 0) {
			List<String> cutout = lines.subList(startIndex, lastIndex);
			List<String> bankData = new ArrayList<>(lastIndex - startIndex);
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < Config.BANK_SIZE; j++) {
					if (j > tabs[i].size() - 1)
						break;
					BankItem bankItem = tabs[i].getItem(j);
					if (bankItem == null)
						continue;
					bankData.add("bank-tab = " + i + "\t" + bankItem.getId() + "\t" + bankItem.getAmount());
				}
			}
			lines.removeAll(cutout);
			lines.addAll(startIndex, bankData);
		}
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(character))) {
			for (String line : lines) {
				writer.write(line);
				writer.newLine();
			}
			writer.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * Tells us whether or not the specified name has the friend added.
	 * 
	 * @param name
	 * @param friend
	 * @return
	 */
	public static boolean isFriend(String name, String friend) {
		long nameLong = Misc.playerNameToInt64(friend);
		long[] friends = getFriends(name);
		if (friends != null && friends.length > 0) {
			for (int index = 0; index < friends.length; index++) {
				if (friends[index] == nameLong) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isFriend2(String name, String friend) {
		long nameLong = Misc.playerNameToInt64(friend);
		long[] friends = getFriends2(name);
		if (friends != null && friends.length > 0) {
			for (int index = 0; index < friends.length; index++) {
				if (friends[index] == nameLong) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Returns a characters friends in the form of a long array.
	 * 
	 * @param name
	 * @return
	 */
	public static long[] getFriends(String name) {
		String line = "";
		String token = "";
		String token2 = "";
		String[] token3 = new String[3];
		long[] readFriends = new long[200];
		long[] friends = null;
		int totalFriends = 0;
		int index = 0;
		File input = new File("./Data/characters/" + name + ".txt");
		if (!input.exists()) {
			return null;
		}
		try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				int spot = line.indexOf("=");
				if (spot > -1) {
					token = line.substring(0, spot);
					token = token.trim();
					token2 = line.substring(spot + 1);
					token2 = token2.trim();
					token3 = token2.split("\t");
					if (token.equals("character-friend")) {
						try {
							readFriends[index] = Long.parseLong(token2);
							totalFriends++;
						} catch (NumberFormatException nfe) {
						}
					}
				}
			}
			reader.close();
			if (totalFriends > 0) {
				friends = new long[totalFriends];
				for (int i = 0; i < totalFriends; i++) {
					friends[i] = readFriends[i];
				}
				return friends;
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return null;
	}

	public static long[] getFriends2(String name) {
		String line = "";
		String token = "";
		String token2 = "";
		String[] token3 = new String[3];
		long[] readFriends = new long[200];
		long[] friends = null;
		int totalFriends = 0;
		int index = 0;
		File input = new File("./Data/characters2/" + name + ".txt");
		if (!input.exists()) {
			return null;
		}
		try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				int spot = line.indexOf("=");
				if (spot > -1) {
					token = line.substring(0, spot);
					token = token.trim();
					token2 = line.substring(spot + 1);
					token2 = token2.trim();
					token3 = token2.split("\t");
					if (token.equals("character-friend")) {
						try {
							readFriends[index] = Long.parseLong(token2);
							totalFriends++;
						} catch (NumberFormatException nfe) {
						}
					}
				}
			}
			reader.close();
			if (totalFriends > 0) {
				friends = new long[totalFriends];
				for (int i = 0; i < totalFriends; i++) {
					friends[i] = readFriends[i];
				}
				return friends;
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return null;
	}

	/**
	 * Loading
	 **/
	public static int loadGame(Client p, String playerName, String playerPass) {
		String line = "";
		String token = "";
		String token2 = "";
		String[] token3 = new String[3];
		boolean EndOfFile = false;
		int ReadMode = 0;
		BufferedReader characterfile = null;
		boolean File1 = false;

		try {
			characterfile = new BufferedReader(new FileReader(
					"./Data/characters/" + playerName + ".txt"));
			File1 = true;
		} catch (FileNotFoundException fileex1) {
		}

		if (File1) {
			// new File ("./characters/"+playerName+".txt");
		} else {
			Misc.println(playerName + ": character file not found.");
			p.newPlayer = false;
			return 0;
		}
		try {
			line = characterfile.readLine();
		} catch (IOException ioexception) {
			Misc.println(playerName + ": error loading file.");
			return 3;
		}
		while (EndOfFile == false && line != null) {
			line = line.trim();
			int spot = line.indexOf("=");
			if (spot > -1) {
				token = line.substring(0, spot);
				token = token.trim();
				token2 = line.substring(spot + 1);
				token2 = token2.trim();
				token3 = token2.split("\t");
				switch (ReadMode) {
				case 1:
					if (token.equals("character-password")) {
						if (playerPass.equalsIgnoreCase(token2)) {
							playerPass = token2;
						} else {
							return 3;
						}
					}
					break;
				case 2:
					if (token.equals("character-height")) {
						p.heightLevel = Integer.parseInt(token2);
					} else if (token.equals("summonId")) {
						p.summonId = Integer.parseInt(token2);
					} else if (token.equals("has-npc")) {
						p.hasNpc = Boolean.parseBoolean(token2);
					} else if (token.equals("ratsCaught")) {
						p.ratsCaught = Integer.parseInt(token2);
					} else if (token.equals("character-posx")) {
						p.teleportToX = (Integer.parseInt(token2) <= 0 ? Config.START_LOCATION_X
								: Integer.parseInt(token2));
					} else if (token.equals("character-posy")) {
						p.teleportToY = (Integer.parseInt(token2) <= 0 ? Config.START_LOCATION_Y
								: Integer.parseInt(token2));
					} else if (token.equals("character-rights")) {
						Rights right = Rights.get(Integer.parseInt(token2));
						p.setRights(right);
					} else if (line.startsWith("displayName")) {
						p.displayName = token2;
					} else if (line.startsWith("player-title")) {
						p.titleId = Integer.parseInt(token2);
					} else if (token.equals("isRunning2")) {
						p.isRunning2 = Boolean.parseBoolean(token2);
					} else if (token.equals("HasXmasItems")) {
						p.HasXmasItems = Boolean.parseBoolean(token2);
					} else if (token.equals("Musicvolume")) {
						p.Musicvolume = Integer.parseInt(token2);
					} else if (token.equals("brightness")) {
						p.brightness = Integer.parseInt(token2);
					} else if (token.equals("splitchat")) {
						p.splitChat = Boolean.parseBoolean(token2);
					} else if (token.equals("chatEffects")) {
						p.chatEffects = Boolean.parseBoolean(token2);
					} else if (token.equals("mouseButton")) {
						p.mouseButton = Boolean.parseBoolean(token2);
					} else if (token.equals("acceptAid")) {
						p.acceptAid = Boolean.parseBoolean(token2);
					} else if (token.equals("arceussFavor")) {
						p.arceussFavor = Double.parseDouble(token2);
					} else if (token.equals("hosidiusFavor")) {
						p.hosidiusFavor = Double.parseDouble(token2);
					} else if (token.equals("lovakengjFavor")) {
						p.lovakengjFavor = Double.parseDouble(token2);
					} else if (token.equals("piscarilliusFavor")) {
						p.piscarilliusFavor = Double.parseDouble(token2);
					} else if (token.equals("shazienFavor")) {
						p.shazienFavor = Double.parseDouble(token2);
					} else if (token.equals("days")) {
						p.daysPlayed = Long.parseLong(token2);
					} else if (token.equals("hours")) {
						p.hoursPlayed = Long.parseLong(token2);
					} else if (token.equals("minutes")) {
						p.minutesPlayed = Long.parseLong(token2);
					} else if (token.equals("seconds")) {
						p.secondsPlayed = Double.parseDouble(token2);
					} else if (token.equals("character-uid")) {
						p.uniqueIdentifier = Long.parseLong(token2);
					} else if (token.equals("run-energy")) {
							p.runEnergy = Integer.parseInt(token2);
					} else if (token.equals("raidPoints")) {
						p.setRaidPoints(Integer.parseInt(token2));
					}else if (token.equals("raidCount")) {
						p.raidCount = (Integer.parseInt(token2));
					} else if (token.equals("resize")) {
						p.resize = Integer.parseInt(token2);
					} else if (token.equals("frameWidth")) {
						p.width = Integer.parseInt(token2);
					} else if (token.equals("frameHeight")) {
						p.height = Integer.parseInt(token2);
					} else if (token.equals("last-clan-chat")) {
						p.lastClanChat = token2;
					} else if (token.equals("onLoginClan")) {
						p.lastClanChat = token2;
					} else if (token.equals("placeholders")) {
						p.placeHolders = Boolean.parseBoolean(token2);
					} else if (token.equals("tutorial-progress")) {
						p.tutorial = Integer.parseInt(token2);
					} else if (token.equals("itemsinlootbag")) {
						p.itemsInLootBag = Integer.parseInt(token2);
					} else if (token.equals("crystal-bow-shots")) {
						p.crystalBowArrowCount = Integer.parseInt(token2);
					} else if (token.equals("skull-timer")) {
						p.skullTimer = Integer.parseInt(token2);
					} else if (token.equals("prestigeLevel")) {
						p.prestigeLevel = Integer.parseInt(token2);
					} else if (token.equals("prestigePoints")) {
						p.prestigePoints = Integer.parseInt(token2);
						p.prestigeLevel = Integer.parseInt(token2);
					} else if (token.equals("DonatorPoints")) {
						p.DonatorPoints = Integer.parseInt(token2);
					} else if (token.equals("magic-book")) {
						p.playerMagicBook = Integer.parseInt(token2);
					} else if (token.equals("brother-info")) {
						p.barrowsNpcs[Integer.parseInt(token3[0])][1] = Integer
								.parseInt(token3[1]);
					} else if (token.equals("hasCannon")) {
						if (Boolean.parseBoolean(token2)) {
							p.playerCannon = PlayerHandler.claimCannon(p);
						}
					} else if (token.equals("pouch-rune")) {
						for (int j = 0; j < token3.length; j++) {
							p.setRuneEssencePouch(j, Integer.parseInt(token3[j]));
						}
					} else if (token.equals("pouch-pure")) {
						for (int j = 0; j < token3.length; j++) {
							p.setPureEssencePouch(j, Integer.parseInt(token3[j]));
						}
					} else if (token.equals("special-amount")) {
						p.specAmount = Double.parseDouble(token2);
					} else if (token.equals("selected-coffin")) {
						p.randomCoffin = Integer.parseInt(token2);
					} else if (token.equals("barrows-killcount")) {
						p.pkPoints = Integer.parseInt(token2);
					} else if (token.equals("dailyTaskDate")) {
						p.dailyTaskDate = Integer.parseInt(token2);
					} else if (token.equals("totalDailyDone")) {
						p.totalDailyDone = Integer.parseInt(token2);
					} else if (token.equals("currentTask")) {
						if(token2 != null && token2.equals("") == false)
							p.currentTask = PossibleTasks.valueOf(token2); //Integer.parseInt(token2);
					} else if (token.equals("completedDailyTask")) {
						p.completedDailyTask = Boolean.parseBoolean(token2);
					} else if (token.equals("playerChoice")) {
						if(token2 != null && token2.equals("") == false)
							p.playerChoice = TaskTypes.valueOf(token2); //Integer.parseInt(token2);
					} else if (token.equals("dailyEnabled")) {
						p.dailyEnabled = Boolean.parseBoolean(token2);
				    } else if (token.equals("character-Giantkills")) {
						p.Giantkills = Integer.parseInt(token2);
					    } else if (token.equals("character-Ghostkills")) {
						p.Ghostkills = Integer.parseInt(token2);
					    } else if (token.equals("character-Druidkills")) {
						p.Druidkills = Integer.parseInt(token2);
					    } else if (token.equals("character-Demonkills")) {
						 p.Demonkills = Integer.parseInt(token2);
					    } else if (token.equals("character-JDemonkills")) {
						p.JDemonkills = Integer.parseInt(token2);
					    } else if (token.equals("character-Generalkills")) {
						p.Generalkills = Integer.parseInt(token2);	
						} else if (token.equals("wave-id")) {
							p.waveId = Integer.parseInt(token2);
						} else if (token.equals("wave-type")) {
							p.waveType = Integer.parseInt(token2);
						} else if (token.equals("infernowave-id")) {
							p.infernoWaveId = Integer.parseInt(token2);
						} else if (token.equals("infernowave-type")) {
							p.infernoWaveType = Integer.parseInt(token2);
						} else if (token.equals("wave-info")) {
							for (int i = 0; i < p.waveInfo.length; i++)
								p.waveInfo[i] = Integer.parseInt(token3[i]);
						} else if (token.equals("zulrah-best-time")) {
							p.setBestZulrahTime(Long.parseLong(token2));
						} else if (token.equals("toxic-staff-amount")) {
							p.setToxicStaffOfDeadCharge(Integer.parseInt(token2));
						} else if (token.equals("toxic-pipe-ammo")) {
							p.setToxicBlowpipeAmmo(Integer.parseInt(token2));
						} else if (token.equals("toxic-pipe-amount")) {
							p.setToxicBlowpipeAmmoAmount(Integer.parseInt(token2));
						} else if (token.equals("toxic-pipe-charge")) {
							p.setToxicBlowpipeCharge(Integer.parseInt(token2));
						} else if (token.equals("serpentine-helm")) {
							p.setSerpentineHelmCharge(Integer.parseInt(token2));
						} else if (token.equals("sotd-charge")) {
							p.staffOfDeadCharge = Integer.parseInt(token2);
						} else if (token.equals("trident-of-the-seas")) {
							p.setTridentCharge(Integer.parseInt(token2));
						} else if (token.equals("trident-of-the-swamp")) {
					} else if (token.equals("teleblock-length")) {
						p.teleBlockDelay = System.currentTimeMillis();
						p.teleBlockLength = Integer.parseInt(token2);
					} else if (token.equals("pc-points")) {
						p.pcPoints = Integer.parseInt(token2);
					} else if (token.equals("slayer-task")) {
						Optional<Task> task = SlayerMaster.get(token2);
						p.getSlayer().setTask(task);
					} else if (token.equals("slayer-master")) {
						p.getSlayer().setMaster(Integer.parseInt(token2));
					} else if (token.equals("slayerPoints")) {
						p.getSlayer().setPoints(Integer.parseInt(token2));
					} else if (token.equals("slayer-task-amount")) {
						p.getSlayer().setTaskAmount(Integer.parseInt(token2));
					} else if (token.equals("slayer-recipe") || token.equals("slayer-helmet")) {
						p.getSlayer().setHelmetCreatable(Boolean.parseBoolean(token2));
					} else if (token.equals("slayer-imbued-helmet")) {
						p.getSlayer().setHelmetImbuedCreatable(Boolean.parseBoolean(token2));
					} else if (token.equals("bigger-boss-tasks")) {
						p.getSlayer().setBiggerBossTasks(Boolean.parseBoolean(token2));
					} else if (token.equals("cerberus-route")) {
						p.getSlayer().setCerberusRoute(Boolean.parseBoolean(token2));
					} else if (token.equals("superior-slayer")) {
						p.getSlayer().setBiggerAndBadder(Boolean.parseBoolean(token2));
					//} else if (token.equals("slayer-tasks-completed")) {
						//p.slayerTasksCompleted = Integer.parseInt(token2);
					} else if (token.equals("magePoints")) {
						p.magePoints = Integer.parseInt(token2);
					} else if (token.equals("VotePoints")) {
						p.votePoints = Integer.parseInt(token2);
					} else if (token.equals("autoRet")) {
						p.autoRet = Integer.parseInt(token2);
					} else if (token.equals("barrowskillcount")) {
						p.barrowsKillCount = Integer.parseInt(token2);
					} else if (token.equals("flagged")) {
						p.accountFlagged = Boolean.parseBoolean(token2);
					} else if (token.equals("wave")) {
						p.waveId = Integer.parseInt(token2);
					} else if (token.equals("void")) {
						for (int j = 0; j < token3.length; j++) {
							p.voidStatus[j] = Integer.parseInt(token3[j]);
						}
					} else if (token.equals("lost-items")) {
						if (token3.length > 1) {
							for (int i = 0; i < token3.length; i += 2) {
								int itemId = Integer.parseInt(token3[i]);
								int itemAmount = Integer.parseInt(token3[i + 1]);
								p.getZulrahLostItems().add(new GameItem(itemId, itemAmount));
							}
						}
					} else if (token.equals("gwkc")) {
						p.killCount = Integer.parseInt(token2);
					} else if (token.equals("fightMode")) {
						p.fightMode = Integer.parseInt(token2);
					}
					break;
				case 3:
					if (token.equals("character-equip")) {
						p.playerEquipment[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[1]);
						p.playerEquipmentN[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[2]);
					}
					break;
				case 4:
					if (token.equals("character-look")) {
						p.playerAppearance[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[1]);
					}
					break;
				case 5:
					if (token.equals("character-skill")) {
						p.playerLevel[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[1]);
						p.playerXP[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[2]);
					}
					break;
				case 6:
					if (token.equals("character-item")) {
						p.playerItems[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[1]);
						p.playerItemsN[Integer.parseInt(token3[0])] = Integer
								.parseInt(token3[2]);
					}
					break;
				case 7:
					if (token.equals("character-bank")) {
						p.bankItems[Integer.parseInt(token3[0])] = Integer.parseInt(token3[1]);
						p.bankItemsN[Integer.parseInt(token3[0])] = Integer.parseInt(token3[2]);
						p.getBank().getBankTab()[0]
								.add(new BankItem(Integer.parseInt(token3[1]), Integer.parseInt(token3[2])));
					} else if (token.equals("bank-tab")) {
						int tabId = Integer.parseInt(token3[0]);
						int itemId = Integer.parseInt(token3[1]);
						int itemAmount = Integer.parseInt(token3[2]);
						p.getBank().getBankTab()[tabId].add(new BankItem(itemId, itemAmount));
					}
					break;
				case 8:
					if (token.equals("character-friend")) {
						p.getFriends().add(Long.parseLong(token3[0]));
					}
					break;
				case 9:
					if (token.equals("character-ignore")) {
						p.getIgnores().add(Long.parseLong(token3[0]));
					}
					break;
				case 10:
					if (token.equals("item")) {
						p.degradableItem[Integer.parseInt(token3[0])] = Integer.parseInt(token3[1]);
					} else if (token.equals("claim-state")) {
						for (int i = 0; i < token3.length; i++) {
							p.claimDegradableItem[i] = Boolean.parseBoolean(token3[i]);
						}
					}
					break;
				case 11:
					NPCDeathTracker.NPCName name = NPCDeathTracker.NPCName.get(token);
					if (name != null) {
						p.getNpcDeathTracker().getTracker().put(name, Integer.parseInt(token2));
					}
					break;
				}
			} else {
				if (line.equals("[ACCOUNT]")) {
					ReadMode = 1;
				} else if (line.equals("[CHARACTER]")) {
					ReadMode = 2;
				} else if (line.equals("[EQUIPMENT]")) {
					ReadMode = 3;
				} else if (line.equals("[LOOK]")) {
					ReadMode = 4;
				} else if (line.equals("[SKILLS]")) {
					ReadMode = 5;
				} else if (line.equals("[ITEMS]")) {
					ReadMode = 6;
				} else if (line.equals("[BANK]")) {
					ReadMode = 7;
				} else if (line.equals("[FRIENDS]")) {
					ReadMode = 8;
				} else if (line.equals("[IGNORES]")) {
					ReadMode = 9;
				} else if (line.equals("[DEGRADEABLES]")) {
					ReadMode = 10;
				} else if (line.equals("[NPC-TRACKER]")) {
					ReadMode = 11;
				} else if (line.equals("[EOF]")) {
					try {
						characterfile.close();
					} catch (IOException ioexception) {
					}
					return 1;
				}
			}
			try {
				line = characterfile.readLine();
			} catch (IOException ioexception1) {
				EndOfFile = true;
			}
		}
		try {
			characterfile.close();
		} catch (IOException ioexception) {
		}
		return 13;
	}

	/**
	 * Saving
	 **/
	public static boolean saveGame(Client p) {
		if (!p.saveFile || p.newPlayer || !p.saveCharacter) {
			// System.out.println("first");
			return false;
		}
		if (p.playerName == null
				|| PlayerHandler.players[p.index] == null) {
			// System.out.println("second");
			return false;
		}
		p.playerName = p.playerName2;
		int tbTime = (int) (p.teleBlockDelay - System.currentTimeMillis() + p.teleBlockLength);
		if (tbTime > 300000 || tbTime < 0) {
			tbTime = 0;
		}

		BufferedWriter characterfile = null;
		try {
			characterfile = new BufferedWriter(new FileWriter(
					"./Data/characters/" + p.playerName + ".txt"));

			/* ACCOUNT */
			characterfile.write("[ACCOUNT]", 0, 9);
			characterfile.newLine();
			characterfile.write("character-username = ", 0, 21);
			characterfile.write(p.playerName, 0, p.playerName.length());
			characterfile.newLine();
			characterfile.write("character-password = ", 0, 21);
			characterfile.write(p.playerPass, 0, p.playerPass.length());
			characterfile.newLine();
			characterfile.newLine();

			/* CHARACTER */
			characterfile.write("[CHARACTER]", 0, 11);
			characterfile.newLine();
			characterfile.write("character-height = ", 0, 19);
			characterfile.write(Integer.toString(p.heightLevel), 0, Integer
					.toString(p.heightLevel).length());
			characterfile.newLine();
			characterfile.write("character-posx = ", 0, 17);
			characterfile.write(Integer.toString(p.absX), 0,
					Integer.toString(p.absX).length());
			characterfile.newLine();
			characterfile.write("character-posy = ", 0, 17);
			characterfile.write(Integer.toString(p.absY), 0,
					Integer.toString(p.absY).length());
			characterfile.newLine();
			characterfile.write("character-rights = " + p.getRights().getValue());
			characterfile.newLine();

			if (p.displayName != null) {
				characterfile.write("displayName = ", 0, 14);
				characterfile.write(p.displayName, 0, p.displayName.length());
				characterfile.newLine();
			}
			characterfile.write("player-title = ", 0, 15);
			characterfile.write(Integer.toString(p.titleId), 0, Integer.toString(p.titleId).length());
			characterfile.newLine();
			characterfile.write("isRunning2 = ", 0, 13);
			characterfile.write(Boolean.toString(p.isRunning2), 0, Boolean.toString(p.isRunning2).length());
			characterfile.newLine();
			characterfile.write("HasXmasItems = ", 0, 15);
			characterfile.write(Boolean.toString(p.HasXmasItems), 0, Boolean.toString(p.HasXmasItems).length());
			characterfile.newLine();
			characterfile.write("brightness = ", 0, 13);
			characterfile.write(Integer.toString(p.brightness), 0, Integer.toString(p.brightness).length());
			characterfile.newLine();
			characterfile.write("Musicvolume = ", 0, 14);
			characterfile.write(Integer.toString(p.Musicvolume), 0, Integer.toString(p.Musicvolume).length());
			characterfile.newLine();
			characterfile.write("splitchat = ", 0, 12);
			characterfile.write(Boolean.toString(p.splitChat), 0, Boolean.toString(p.splitChat).length());
			characterfile.newLine();
			characterfile.write("chatEffects = ", 0, 14);
			characterfile.write(Boolean.toString(p.chatEffects), 0, Boolean.toString(p.chatEffects).length());
			characterfile.newLine();
			characterfile.write("mouseButton = ", 0, 14);
			characterfile.write(Boolean.toString(p.mouseButton), 0, Boolean.toString(p.mouseButton).length());
			characterfile.newLine();
			characterfile.write("acceptAid = ", 0, 12);
			characterfile.write(Boolean.toString(p.acceptAid), 0, Boolean.toString(p.acceptAid).length());
			characterfile.newLine();
			characterfile.write("character-uid = ", 0, 16);
			characterfile.write(Long.toString(p.uniqueIdentifier), 0, Long
					.toString(p.uniqueIdentifier).length());
			characterfile.newLine();			
			characterfile.write("arceussFavor = " + p.arceussFavor);
			characterfile.newLine();
			characterfile.write("hosidiusFavor = " + p.hosidiusFavor);
			characterfile.newLine();
			characterfile.write("lovakengjFavor = " + p.lovakengjFavor);
			characterfile.newLine();
			characterfile.write("piscarilliusFavor = " + p.piscarilliusFavor);
			characterfile.newLine();
			characterfile.write("shazienFavor = " + p.shazienFavor);
			characterfile.newLine();
			characterfile.write("days = " + p.daysPlayed);
			characterfile.newLine();
			characterfile.write("hours = " + p.hoursPlayed);
			characterfile.newLine();
			characterfile.write("minutes = " + p.minutesPlayed);
			characterfile.newLine();
			characterfile.write("seconds = " + p.secondsPlayed);
			characterfile.newLine();
			characterfile.write("run-energy = ", 0, 13);
			characterfile.write(Integer.toString(p.runEnergy), 0, Integer.toString(p.runEnergy).length());
			characterfile.newLine();
			characterfile.write("raidPoints = " + p.getRaidPoints());
			characterfile.newLine();
			characterfile.write("raidCount = " + p.raidCount);
			characterfile.newLine();
			characterfile.write("resize = ", 0, 9);
			characterfile.write(Integer.toString(p.resize), 0, Integer
					.toString(p.resize).length());
			characterfile.newLine();
			characterfile.write("frameWidth = ", 0, 13);
			characterfile.write(Integer.toString(p.width), 0, Integer
					.toString(p.width).length());
			characterfile.newLine();
			characterfile.write("frameHeight = ", 0, 14);
			characterfile.write(Integer.toString(p.height), 0, Integer
					.toString(p.height).length());
			characterfile.newLine();
			characterfile.write("last-clan-chat = " + p.lastClanChat);
			characterfile.newLine();
			characterfile.write("placeholders = " + p.placeHolders);
			characterfile.newLine();
			characterfile.write("has-npc = ", 0, 10);
			characterfile.write(Boolean.toString(p.hasNpc), 0, Boolean.toString(p.hasNpc).length());
			characterfile.newLine();
			characterfile.write("summonId = ", 0, 11);
			characterfile.write(Integer.toString(p.summonId), 0, Integer.toString(p.summonId).length());
			characterfile.newLine();
			characterfile.write("ratsCaught = ", 0, 13);
			characterfile.write(Integer.toString(p.ratsCaught)  , 0, Integer.toString(p.ratsCaught).length());
			characterfile.newLine();
			characterfile.write("hasCannon = " + (p.playerCannon == null ? "false" : "true"));
			characterfile.newLine();
			characterfile.write("pouch-rune = " + p.getRuneEssencePouch(0) + "\t" + p.getRuneEssencePouch(1) + "\t" + p.getRuneEssencePouch(2));
			characterfile.newLine();
			characterfile.write("pouch-pure = " + p.getPureEssencePouch(0) + "\t" + p.getPureEssencePouch(1) + "\t" + p.getPureEssencePouch(2));
			characterfile.newLine();
			characterfile.write("crystal-bow-shots = ", 0, 20);
			characterfile.write(Integer.toString(p.crystalBowArrowCount), 0,
					Integer.toString(p.crystalBowArrowCount).length());
			characterfile.newLine();
			characterfile.write("loot-bag = ");
			for (int i = 0; i < p.lootBag.length; i++) {
				if (i > 0) {
					characterfile.write(" ");
				}
				characterfile.write(+p.lootBag[i] + "," + p.amountLoot[i]);
			}
			characterfile.newLine();
			characterfile.write("skull-timer = ", 0, 14);
			characterfile.write(Integer.toString(p.skullTimer), 0, Integer
					.toString(p.skullTimer).length());
			characterfile.newLine();
			characterfile.write("magic-book = ", 0, 13);
			characterfile.write(Integer.toString(p.playerMagicBook), 0, Integer
					.toString(p.playerMagicBook).length());
			characterfile.newLine();
			for (int b = 0; b < p.barrowsNpcs.length; b++) {
				characterfile.write("brother-info = ", 0, 15);
				characterfile.write(Integer.toString(b), 0, Integer.toString(b)
						.length());
				characterfile.write("	", 0, 1);
				characterfile.write(
						p.barrowsNpcs[b][1] <= 1 ? Integer.toString(0)
								: Integer.toString(p.barrowsNpcs[b][1]), 0,
						Integer.toString(p.barrowsNpcs[b][1]).length());
				characterfile.newLine();
			}
			characterfile.write("special-amount = ", 0, 17);
			characterfile.write(Double.toString(p.specAmount), 0, Double
					.toString(p.specAmount).length());
			characterfile.newLine();
			characterfile.write("selected-coffin = ", 0, 18);
			characterfile.write(Integer.toString(p.randomCoffin), 0, Integer
					.toString(p.randomCoffin).length());
			characterfile.newLine();
			characterfile.write("barrows-killcount = ", 0, 20);
			characterfile.write(Integer.toString(p.barrowsKillCount), 0,
					Integer.toString(p.barrowsKillCount).length());
			characterfile.newLine();
			characterfile.write("dailyTaskDate = " + p.dailyTaskDate);
			characterfile.newLine();
			characterfile.write("totalDailyDone = " + p.totalDailyDone);
			characterfile.newLine();
			characterfile.write("currentTask = ", 0, 14);
			if(p.currentTask != null)
                characterfile.write(p.currentTask.name(), 0, p.currentTask.name().length());
			characterfile.newLine();
			characterfile.write("completedDailyTask = " + p.completedDailyTask);
			characterfile.newLine();
			characterfile.write("playerChoice = ", 0, 15);
			if(p.playerChoice != null)
                characterfile.write(p.playerChoice.name(), 0, p.playerChoice.name().length());
			characterfile.newLine();
			characterfile.write("dailyEnabled = " + p.dailyEnabled);
			characterfile.newLine();
			characterfile.write("character-Giantkills = ", 0, 23);
			characterfile.write(Integer.toString(p.Giantkills), 0, 
				Integer.toString(p.Giantkills).length());
			characterfile.newLine();
			characterfile.write("character-Ghostkills = ", 0, 23);
			characterfile.write(Integer.toString(p.Ghostkills), 0, 
				Integer.toString(p.Ghostkills).length());
			characterfile.newLine();
			characterfile.write("character-Druidkills = ", 0, 23);
			characterfile.write(Integer.toString(p.Druidkills), 0, 
				Integer.toString(p.Druidkills).length());
			characterfile.newLine();
			characterfile.write("character-Demonkills = ", 0, 23);
			characterfile.write(Integer.toString(p.Demonkills), 0, 
				Integer.toString(p.Demonkills).length());
			characterfile.newLine();
			characterfile.write("character-JDemonkills = ", 0, 24);
			characterfile.write(Integer.toString(p.JDemonkills), 0, 
				Integer.toString(p.JDemonkills).length());
			characterfile.newLine();
			characterfile.write("character-Generalkills = ", 0, 25);
			characterfile.write(Integer.toString(p.Generalkills), 0, 
				Integer.toString(p.Generalkills).length());
			characterfile.newLine();
			characterfile.write("wave-id = " + p.waveId);
			characterfile.newLine();
			characterfile.write("wave-type = " + p.waveType);
			characterfile.newLine();
			characterfile.write("infernowave-id = " + p.infernoWaveId);
			characterfile.newLine();
			characterfile.write("infernowave-type = " + p.infernoWaveType);
			characterfile.newLine();
			characterfile.write("wave-info = " + p.waveInfo[0] + "\t" + p.waveInfo[1] + "\t" + p.waveInfo[2]);
			characterfile.newLine();
			characterfile.write("zulrah-best-time = " + p.getBestZulrahTime());
			characterfile.newLine();
			characterfile.write("toxic-staff-ammo = " + p.getToxicStaffOfDeadCharge());
			characterfile.newLine();
			characterfile.write("toxic-pipe-ammo = " + p.getToxicBlowpipeAmmo());
			characterfile.newLine();
			characterfile.write("toxic-pipe-amount = " + p.getToxicBlowpipeAmmoAmount());
			characterfile.newLine();
			characterfile.write("toxic-pipe-charge = " + p.getToxicBlowpipeCharge());
			characterfile.newLine();
			characterfile.write("serpentine-helm = " + p.getSerpentineHelmCharge());
			characterfile.newLine();
			characterfile.write("trident-of-the-seas = " + p.getTridentCharge());
			characterfile.newLine();
			characterfile.write("trident-of-the-swamp = " + p.getToxicTridentCharge());
			characterfile.newLine();
			characterfile.write("teleblock-length = ", 0, 19);
			characterfile.write(Integer.toString(tbTime), 0,
					Integer.toString(tbTime).length());
			characterfile.newLine();
			characterfile.write("prestigeLevel = ", 0, 16);
			characterfile.write(Integer.toString(p.prestigeLevel), 0, Integer
					.toString(p.prestigeLevel).length());
			characterfile.newLine();
			characterfile.write("prestigePoints = ", 0, 17);
			characterfile.write(Integer.toString(p.prestigePoints), 0, Integer
					.toString(p.prestigePoints).length());
			characterfile.newLine();
			characterfile.write("DonatorPoints = ", 0, 16);
			characterfile.write(Integer.toString(p.DonatorPoints), 0, Integer
					.toString(p.DonatorPoints).length());
			characterfile.newLine();
			characterfile.write("pc-points = ", 0, 12);
			characterfile.write(Integer.toString(p.pcPoints), 0, Integer
					.toString(p.pcPoints).length());
			characterfile.newLine();
			if (p.getSlayer().getTask().isPresent()) {
				Task task = p.getSlayer().getTask().get();
				characterfile.write("slayer-task = " + task.getPrimaryName());
				characterfile.newLine();
				characterfile.write("slayer-task-amount = " + p.getSlayer().getTaskAmount());
				characterfile.newLine();
			}
			characterfile.write("slayer-master = " + p.getSlayer().getMaster());
			characterfile.newLine();
			characterfile.write("consecutive-tasks = " + p.getSlayer().getConsecutiveTasks());
			characterfile.newLine();
			characterfile.write("magePoints = ", 0, 13);
			characterfile.write(Integer.toString(p.magePoints), 0, Integer
					.toString(p.magePoints).length());
			characterfile.newLine();
			characterfile.write("VotePoints = ", 0, 13);
			characterfile.write(Integer.toString(p.votePoints), 0, Integer
					.toString(p.votePoints).length());
			characterfile.newLine();
			characterfile.write("autoRet = ", 0, 10);
			characterfile.write(Integer.toString(p.autoRet), 0, Integer
					.toString(p.autoRet).length());
			characterfile.newLine();
			characterfile.write("barrowskillcount = ", 0, 19);
			characterfile.write(Integer.toString(p.barrowsKillCount), 0,
					Integer.toString(p.barrowsKillCount).length());
			characterfile.newLine();
			characterfile.write("flagged = ", 0, 10);
			characterfile.write(Boolean.toString(p.accountFlagged), 0, Boolean
					.toString(p.accountFlagged).length());
			characterfile.newLine();
			characterfile.write("wave = ", 0, 7);
			characterfile.write(Integer.toString(p.waveId), 0, Integer
					.toString(p.waveId).length());
			characterfile.newLine();
			characterfile.write("gwkc = ", 0, 7);
			characterfile.write(Integer.toString(p.killCount), 0, Integer
					.toString(p.killCount).length());
			characterfile.newLine();
			characterfile.write("fightMode = ", 0, 12);
			characterfile.write(Integer.toString(p.fightMode), 0, Integer
					.toString(p.fightMode).length());
			characterfile.newLine();
			characterfile.write("lost-items = ");
			for (GameItem item : p.getZulrahLostItems()) {
				if (item == null) {
					continue;
				}
				characterfile.write(item.getId() + "\t" + item.getAmount() + "\t");
			}
			characterfile.newLine();
			characterfile.write("void = ", 0, 7);
			String toWrite = p.voidStatus[0] + "\t" + p.voidStatus[1] + "\t"
					+ p.voidStatus[2] + "\t" + p.voidStatus[3] + "\t"
					+ p.voidStatus[4];
			characterfile.write(toWrite);
			characterfile.newLine();
			characterfile.newLine();

			/* EQUIPMENT */
			characterfile.write("[EQUIPMENT]", 0, 11);
			characterfile.newLine();
			for (int i = 0; i < p.playerEquipment.length; i++) {
				characterfile.write("character-equip = ", 0, 18);
				characterfile.write(Integer.toString(i), 0, Integer.toString(i)
						.length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(p.playerEquipment[i]), 0,
						Integer.toString(p.playerEquipment[i]).length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(p.playerEquipmentN[i]), 0,
						Integer.toString(p.playerEquipmentN[i]).length());
				characterfile.write("	", 0, 1);
				characterfile.newLine();
			}
			characterfile.newLine();

			/* LOOK */
			characterfile.write("[LOOK]", 0, 6);
			characterfile.newLine();
			for (int i = 0; i < p.playerAppearance.length; i++) {
				characterfile.write("character-look = ", 0, 17);
				characterfile.write(Integer.toString(i), 0, Integer.toString(i)
						.length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(p.playerAppearance[i]), 0,
						Integer.toString(p.playerAppearance[i]).length());
				characterfile.newLine();
			}
			characterfile.newLine();

			/* SKILLS */
			characterfile.write("[SKILLS]", 0, 8);
			characterfile.newLine();
			for (int i = 0; i < p.playerLevel.length; i++) {
				characterfile.write("character-skill = ", 0, 18);
				characterfile.write(Integer.toString(i), 0, Integer.toString(i)
						.length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(p.playerLevel[i]), 0,
						Integer.toString(p.playerLevel[i]).length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(p.playerXP[i]), 0, Integer
						.toString(p.playerXP[i]).length());
				characterfile.newLine();
			}
			characterfile.newLine();

			/* ITEMS */
			characterfile.write("[ITEMS]", 0, 7);
			characterfile.newLine();
			for (int i = 0; i < p.playerItems.length; i++) {
				if (p.playerItems[i] > 0) {
					characterfile.write("character-item = ", 0, 17);
					characterfile.write(Integer.toString(i), 0, Integer
							.toString(i).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.playerItems[i]), 0,
							Integer.toString(p.playerItems[i]).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.playerItemsN[i]), 0,
							Integer.toString(p.playerItemsN[i]).length());
					characterfile.newLine();
				}
			}
			characterfile.newLine();
			/* BANK */
			characterfile.write("[BANK]", 0, 6);
			characterfile.newLine();
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < Config.BANK_SIZE; j++) {
					if (j > p.getBank().getBankTab()[i].size() - 1)
						break;
					BankItem item = p.getBank().getBankTab()[i].getItem(j);
					if (item == null)
						continue;
					characterfile.write("bank-tab = " + i + "\t" + item.getId() + "\t" + item.getAmount());
					characterfile.newLine();
				}
			}

			characterfile.newLine();
			characterfile.newLine();
			/* FRIENDS */
			characterfile.write("[FRIENDS]", 0, 9);
			characterfile.newLine();
			for (Long friend : p.getFriends().getFriends()) {
				characterfile.write("character-friend = ", 0, 19);
				characterfile.write(Long.toString(friend), 0, Long.toString(friend).length());
				characterfile.newLine();
			}
			characterfile.newLine();
			characterfile.newLine();
			characterfile.newLine();
			characterfile.write("[DEGRADEABLES]");
			characterfile.newLine();
			characterfile.write("claim-state = ");
			for (int i = 0; i < p.claimDegradableItem.length; i++) {
				characterfile.write(Boolean.toString(p.claimDegradableItem[i]));
				if (i != p.claimDegradableItem.length - 1) {
					characterfile.write("\t");
				}
			}
			characterfile.newLine();
			for (int i = 0; i < p.degradableItem.length; i++) {
				if (p.degradableItem[i] > 0) {
					characterfile.write("item = " + i + "\t" + p.degradableItem[i]);
					characterfile.newLine();
				}
			}
			characterfile.newLine();

			/* IGNORES */
			characterfile.write("[IGNORES]", 0, 9);
			characterfile.newLine();
			for (Long ignore : p.getIgnores().getIgnores()) {
				characterfile.write("character-ignore = ", 0, 19);
				characterfile.write(Long.toString(ignore), 0, Long.toString(ignore).length());
				characterfile.newLine();
			}
			characterfile.newLine();
			characterfile.write("[NPC-TRACKER]");
			characterfile.newLine();
			for (Entry<NPCDeathTracker.NPCName, Integer> entry : p.getNpcDeathTracker().getTracker().entrySet()) {
				if (entry != null) {
					if (entry.getValue() > 0) {
						characterfile.write(entry.getKey().toString() + " = " + entry.getValue());
						characterfile.newLine();
					}
				}
			}

			characterfile.newLine();
			/* EOF */
			characterfile.write("[EOF]", 0, 5);
			characterfile.newLine();
			characterfile.newLine();
			characterfile.close();
		} catch (IOException ioexception) {
			Misc.println(p.playerName + ": error writing file.");
			return false;
		}
		return true;
	}

}