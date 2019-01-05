package Ghreborn.world;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.File;
import java.util.Scanner;
import java.util.function.Predicate;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import Ghreborn.Config;
import Ghreborn.core.PlayerHandler;
import Ghreborn.model.items.GroundItem;
import Ghreborn.model.items.Item;
import Ghreborn.model.items.ItemDefinition;
import Ghreborn.model.items.ItemList;
import Ghreborn.model.players.Boundary;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.Player;
import Ghreborn.util.Misc;

/**
 * Handles ground items
 **/

public class ItemHandler {

	public List<GroundItem> items = new ArrayList<GroundItem>();
	public static final int HIDE_TICKS = 100;
	public int DropItemCount = 0;
	public int MaxListedItems = 30000;
	 // Phate: Global Item VARS
		public static int[] globalItemController =		new int[5001];
		public static int[] globalItemID =				new int[5001];
		public static int[] globalItemX =				new int[5001];
		public static int[] globalItemY =				new int[5001];
		public static int[] globalItemAmount =			new int[5001];
		public static int MaxDropItems = 100000;
		public static boolean[] globalItemStatic =		new boolean[5001];
		public static int[] DroppedItemsX = new int[MaxDropItems];
		public static int[] DroppedItemsY = new int[MaxDropItems];
		public static int[] DroppedItemsN = new int[MaxDropItems];
		public static int[] DroppedItemsH = new int[MaxDropItems];
		public static int[] DroppedItemsDDelay = new int[MaxDropItems];
		public static int[] DroppedItemsSDelay = new int[MaxDropItems];
		public static int[] DroppedItemsDropper = new int[MaxDropItems];
		public static int[] DroppedItemsDeletecount = new int[MaxDropItems];
		public static boolean[] DroppedItemsAlwaysDrop = new boolean[MaxDropItems];
		public static int[] globalItemTicks =			new int[5001];

							//process() is called evry 500 ms
	public static int MaxDropShowDelay = 120; //120 * 500 = 60000 / 1000 = 60s
	public static int SDID = 90; //90 * 500 = 45000 / 1000 = 45s
					//SDID = Standard Drop Items Delay
	public  int[] DroppedItemsID = new int[MaxDropItems];
	//public ItemList ItemList[] = new ItemList[MaxListedItems]

	public ItemHandler() {
		for (int i = 0; i <= 5000; i++) {
			globalItemController[i] =	0;
			globalItemID[i] =			0;
			globalItemX[i] =			0;
			globalItemY[i] =			0;
			globalItemAmount[i] =		0;
			globalItemTicks[i] =		0;
			globalItemStatic[i]  =	false;
		}
		for(int i = 0; i < MaxDropItems; i++) {
			ResetItem(i);
		}
		for (int i = 0; i < Config.ITEM_LIMIT; i++) {
			ItemList[i] = null;
		}
		//loadItemList("item.cfg");
		//loadItemPrices("prices.txt");
	}

	/**
	 * Adds item to list
	 **/
	public void addItem(GroundItem item) {
		items.add(item);
	}

	/**
	 * Removes item from list
	 **/
	public void removeItem(GroundItem item) {
		items.remove(item);
	}

	/**
	 * Item amount
	 **/
	public int itemAmount(String player, int itemId, int itemX, int itemY, int height) {
		for (GroundItem i : items) {
			if (i.hideTicks >= 1 && player.equalsIgnoreCase(i.getController()) || i.hideTicks < 1) {
				if (i.getId() == itemId && i.getX() == itemX && i.getY() == itemY && i.getHeight() == height) {
					return i.getAmount();
				}
			}
		}
		return 0;
	}


	/**
	 * Item exists
	 **/
	public boolean itemExists(int itemId, int itemX, int itemY, int height) {
		for (GroundItem i : items) {
			if (i.getId() == itemId && i.getX() == itemX && i.getY() == itemY
					&& i.getHeight() == height) {
				return true;
			}
		}
		return false;
	}
	
	public void reloadItems(Player player) {
		Predicate<GroundItem> visible = item -> (((player.getItems().tradeable(item.getId())
				|| item.getController().equalsIgnoreCase(player.playerName)) && player
				.distanceToPoint(item.getX(), item.getY()) <= 60)
				&& (item.hideTicks > 0 && item.getController().equalsIgnoreCase(player.playerName) || item.hideTicks == 0) && player.heightLevel == item.getHeight());
		items.stream().filter(visible).forEach(item -> player.getItems().removeGroundItem(item));
		items.stream().filter(visible).forEach(item -> player.getItems().createGroundItem(item));
	}

	public void process() {
		try {
			Iterator<GroundItem> it = items.iterator();
			while (it.hasNext()) {
				GroundItem i = it.next();
				if (i == null)
					continue;
				if(i.globalItem) {
					if(!i.initialized) {
						createGlobalItem(i);
						i.initialized = true;
					}
					continue;
				}
				
				if (i.hideTicks > 0) {
					i.hideTicks--;
				}
				if (i.hideTicks == 1) {
					i.hideTicks = 0;
					createGlobalItem(i);
					i.removeTicks = HIDE_TICKS;
				}
				if (i.removeTicks > 0) {
					i.removeTicks--;
				}
				if (i.removeTicks == 1) {
					i.removeTicks = 0;
					PlayerHandler.stream().filter(Objects::nonNull).filter(p -> p.distanceToPoint(i.getX(), i.getY()) <= 60)
							.forEach(p -> p.getItems().removeGroundItem(i.getId(), i.getX(), i.getY(), i.getAmount(), i.getHeight()));
					it.remove();
				}
			}
		} catch (Exception e) {
			System.out.println("ItemHandler" + e);
			e.printStackTrace();
		}
	}
	public void ResetItem(int ArrayID) {
		DroppedItemsID[ArrayID] = -1;
		DroppedItemsX[ArrayID] = -1;
		DroppedItemsY[ArrayID] = -1;
		DroppedItemsN[ArrayID] = -1;
		DroppedItemsH[ArrayID] = -1;
		DroppedItemsDDelay[ArrayID] = -1;
		DroppedItemsSDelay[ArrayID] = 0;
		DroppedItemsDropper[ArrayID] = -1;
		DroppedItemsDeletecount[ArrayID] = 0;
		DroppedItemsAlwaysDrop[ArrayID] = false;
	}
	/**
	 * Creates the ground item
	 **/
	public int[][] brokenBarrows = { { 4708, 4860 }, { 4710, 4866 },
			{ 4712, 4872 }, { 4714, 4878 }, { 4716, 4884 }, { 4720, 4896 },
			{ 4718, 4890 }, { 4720, 4896 }, { 4722, 4902 }, { 4732, 4932 },
			{ 4734, 4938 }, { 4736, 4944 }, { 4738, 4950 }, { 4724, 4908 },
			{ 4726, 4914 }, { 4728, 4920 }, { 4730, 4926 }, { 4745, 4956 },
			{ 4747, 4926 }, { 4749, 4968 }, { 4751, 4994 }, { 4753, 4980 },
			{ 4755, 4986 }, { 4757, 4992 }, { 4759, 4998 } };

	public void createGroundItem(Player player, int itemId, int itemX, int itemY, int height, int itemAmount, int playerId) {
		try {
		if (itemId > 0) {
			if (itemId >= 2412 && itemId <= 2414) {
				player.sendMessage("The cape vanishes as it touches the ground.");
				return;
			}
			if (!ItemDefinition.forId(itemId).isStackable() && itemAmount > 0) {
				if (itemAmount > 28)
					itemAmount = 28;
				for (int j = 0; j < itemAmount; j++) {
					player.getItems().createGroundItem(itemId, itemX, itemY, 1);
					GroundItem item = new GroundItem(itemId, itemX, itemY, height, 1, HIDE_TICKS, PlayerHandler.players[playerId].playerName);
					addItem(item);
				}
			} else {
				player.getItems().createGroundItem(itemId, itemX, itemY, itemAmount);
				GroundItem item = new GroundItem(itemId, itemX, itemY, height, itemAmount, HIDE_TICKS, PlayerHandler.players[playerId].playerName);
				addItem(item);
			}
		}
		} catch (Exception e) { e.printStackTrace(); }
	}
	

	/**
	 * Shows items for everyone who is within 60 squares
	 **/
	public void createGlobalItem(GroundItem i) {
		for (Player p : PlayerHandler.players) {
			if (p != null) {
				Player person = p;
				if (!person.playerName.equalsIgnoreCase(i.getController())) {
					if (!person.getItems().tradeable(i.getId())) {
						continue;
					}
					if (person.distanceToPoint(i.getX(), i.getY()) <= 60 && person.heightLevel == i.getHeight()) {
						person.getItems().createGroundItem(i.getId(), i.getX(), i.getY(), i.getAmount());
					}
				}
			}
		}
	}

	/**
	 * Removing the ground item
	 **/

	public void removeGroundItem(Client c, int itemId, int itemX, int itemY, int height, boolean add) {
		for (GroundItem i : items) {
			if (i.getId() == itemId && i.getX() == itemX && i.getY() == itemY && i.getHeight() == height) {
				if (i.hideTicks > 0 && i.getController().equalsIgnoreCase(c.playerName)) {
					if (add) {
						if (c.getItems().addItem(i.getId(), i.getAmount())) {
							removeControllersItem(i, c, i.getId(), i.getX(), i.getY(), i.getAmount(), i.getHeight());
							break;
						}
					} else {
						removeControllersItem(i, c, i.getId(), i.getX(), i.getY(), i.getAmount(), i.getHeight());
						break;
					}
				} else if (i.hideTicks <= 0) {
					if (c.ironman && !i.getController().equalsIgnoreCase(c.playerName)) {
						c.sendMessage("You can only pick up your own drops and items as a Ironman.");
						return;
					}
					if (add) {
						if (c.getItems().addItem(i.getId(), i.getAmount())) {
							removeGlobalItem(i, i.getId(), i.getX(), i.getY(), i.getHeight(), i.getAmount());
							break;
						}
					} else {
						removeGlobalItem(i, i.getId(), i.getX(), i.getY(), i.getHeight(), i.getAmount());
						break;
					}
				}
			}
		}
	}


	/**
	 * Remove item for just the item controller (item not global yet)
	 **/

	public void removeControllersItem(GroundItem i, Player c, int itemId, int itemX, int itemY, int itemAmount, int heightLevel) {
		c.getItems().removeGroundItem(itemId, itemX, itemY, itemAmount, heightLevel);
		removeItem(i);
	}

	/**
	 * Remove item for everyone within 60 squares
	 **/

	public void removeGlobalItem(GroundItem i, int itemId, int itemX, int itemY, int height, int itemAmount) {
		for (Player p : PlayerHandler.players) {
			if (p != null) {
				Player person = p;
				if (person.distanceToPoint(itemX, itemY) <= 60 && person.heightLevel == height) {
					person.getItems().removeGroundItem(itemId, itemX, itemY, itemAmount, height);
				}
			}
		}
		removeItem(i);
	}

	/**
	 * Item List
	 **/

	public ItemList ItemList[] = new ItemList[Config.ITEM_LIMIT];

	public void newItemList(int ItemId, String ItemName,
			String ItemDescription, double ShopValue, double LowAlch,
			double HighAlch, int Bonuses[]) {
		// first, search for a free slot
		int slot = -1;
		for (int i = 0; i < 11740; i++) {
			if (ItemList[i] == null) {
				slot = i;
				break;
			}
		}

		if (slot == -1)
			return; // no free slot found
		ItemList newItemList = new ItemList(ItemId);
		newItemList.itemName = ItemName;
		newItemList.itemDescription = ItemDescription;
		newItemList.ShopValue = ShopValue;
		newItemList.LowAlch = LowAlch;
		newItemList.HighAlch = HighAlch;
		newItemList.Bonuses = Bonuses;
		ItemList[slot] = newItemList;
	}

	@SuppressWarnings("resource")
	public void loadItemPrices(String filename) {
		try {
			Scanner s = new Scanner(new File("./data/cfg/" + filename));
			while (s.hasNextLine()) {
				String[] line = s.nextLine().split(" ");
				ItemList temp = getItemList(Integer.parseInt(line[0]));
				if (temp != null)
					temp.ShopValue = Integer.parseInt(line[1]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ItemList getItemList(int i) {
		for (int j = 0; j < ItemList.length; j++) {
			if (ItemList[j] != null) {
				if (ItemList[j].itemId == i) {
					return ItemList[j];
				}
			}
		}
		return null;
	}

	public boolean loadItemList(String FileName) {
		String line = "";
		String token = "";
		String token2 = "";
		String token2_2 = "";
		String[] token3 = new String[10];
		boolean EndOfFile = false;
		BufferedReader characterfile = null;
		try {
			characterfile = new BufferedReader(new FileReader("./Data/cfg/"
					+ FileName));
		} catch (FileNotFoundException fileex) {
			Misc.println(FileName + ": file not found.");
			return false;
		}
		try {
			line = characterfile.readLine();
		} catch (IOException ioexception) {
			Misc.println(FileName + ": error loading file.");
		}
		while (EndOfFile == false && line != null) {
			line = line.trim();
			int spot = line.indexOf("=");
			if (spot > -1) {
				token = line.substring(0, spot);
				token = token.trim();
				token2 = line.substring(spot + 1);
				token2 = token2.trim();
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token3 = token2_2.split("\t");
				if (token.equals("item")) {
					int[] Bonuses = new int[12];
					for (int i = 0; i < 12; i++) {
						if (token3[(6 + i)] != null) {
							Bonuses[i] = Integer.parseInt(token3[(6 + i)]);
						} else {
							break;
						}
					}
					newItemList(Integer.parseInt(token3[0]),
							token3[1].replaceAll("_", " "),
							token3[2].replaceAll("_", " "),
							Double.parseDouble(token3[4]),
							Double.parseDouble(token3[4]),
							Double.parseDouble(token3[6]), Bonuses);
				}
			} else {
				if (line.equals("[ENDOFITEMLIST]")) {
					try {
						characterfile.close();
					} catch (IOException ioexception) {
					}
					return true;
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
		return false;
	}

	public void createGroundItem(Player player, int itemId, int itemX, int itemY, int height, int itemAmount) {
		if (itemId > 0 && itemAmount > 0) {
			if (itemId >= 2412 && itemId <= 2414) {
				player.sendMessage("The cape vanishes as it touches the ground.");
				return;
			}
			if (!Item.itemStackable[itemId] && itemAmount > 0) {
				if (itemAmount > 28) {
					itemAmount = 28;
				}
				for (int j = 0; j < itemAmount; j++) {
					player.getItems().createGroundItem(itemId, itemX, itemY, 1);
					GroundItem item = new GroundItem(itemId, itemX, itemY, height, 1, HIDE_TICKS, player.playerName);
					items.add(item);
				}
			} else {
				if (itemId != 11849 && !Boundary.isIn(player, Boundary.ROOFTOP_COURSES))
					player.getItems().createGroundItem(itemId, itemX, itemY, itemAmount);
					GroundItem item = new GroundItem(itemId, itemX, itemY, height, itemAmount, HIDE_TICKS, player.playerName);
					items.add(item);
			}
		}
	}

	public GroundItem getGroundItem(int itemId, int x, int y, int height) {
		Optional<GroundItem> item = items.stream().filter(i -> i.getId() == itemId && i.getX() == x && i.getY() == y && i.getHeight() == height).findFirst();
		return item.orElse(null);
	}
}
