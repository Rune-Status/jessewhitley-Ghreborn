package Ghreborn.model.items;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import Ghreborn.Config;
import Ghreborn.Server;
import Ghreborn.definitions.ItemCacheDefinition;

public class Item {

	
    // Few item types for equipping
    public static int twohanded[] = { 11785, 10431, 7158, 1319, 3202, 6528, 15333, 15335, 15332};
    public static int isHelm[] = { 0000};
    
	public static boolean isPlate(int itemId) {
		if(itemId == -1)
			return false;
		String[] data = { "vesta's chainbody", "jacket", "Ladies chainbody", "Torva platebody", "top", "shirt", "platebody", "ahrims robetop","karils leathertop"," brassard", "robe top", "robetop", "platebody (t)",
				"platebody (g)", "Owner Plate", "chestplate", "Black Platebody (W)", " torso", "Torso", "hauberk", "dragon chainbody", "pernix", "morrigan" };
		String item = getItemName(itemId);
		if (item == null) {
			return false;
		}
		boolean item1 = false;
		for (String element : data) {
			if (item.endsWith(element) || item.contains(element)) {
				item1 = true;
			}
		}
		return item1;
	}
	
    public static boolean isFullHelm(int itemId) {
		if(itemId == -1)
			return false;
		String[] data = {"med helm", "coif", "Larupia hat","Kyatt hat","Owner Hat", "dharok's helm", "hood", "tnitiate helm", "coif", "helm of neitiznot", "armadyl helmet", "berserker helm", 
				"archer helm", "farseer helm", "warrior helm", "mage hat", "lizard skull"};
		String item = getItemName(itemId);
		if (item == null) {
			return false;
		}
		boolean item1 = false;
		for (String element : data) {
			if (item.endsWith(element) || item.contains(element)) {
				item1 = true;
			}
		}
		return item1;
	}

    public static boolean isFullMask(int itemId) {
		if(itemId == -1)
			return false;
		String[] data = {"Star-face","full helm","Larupia hat","Kyatt hat", "Owner Hat", "Light Helmet", "mask", "verac's helm", "guthan's helm", "karil's coif", "mask", "Mask","torag's helm", "void", "sallet", "pernix", "katagon full helm"};
		String item = getItemName(itemId);
		if (item == null) {
			return false;
		}
		boolean item1 = false;
		for (String element : data) {
			if (item.endsWith(element) || item.contains(element)) {
				item1 = true;
			}
		}
		return item1;
	}
	
    
    public static boolean playerCape(int itemId) {
		String[] data = { "Sack of presents", "Bag of Gold", "cloak", "cape", "Butterfly Wings", "Cape", "ava's", "accumulator", "tokhaar" };
		String item = getItemName(itemId);
		if (item == null) {
			return false;
		}
		boolean item1 = false;
		for (String element : data) {
			if (item.endsWith(element) || item.contains(element)) {
				item1 = true;
			}
		}
		return item1;
	}

	public static boolean playerBoots(int itemId) {
		String[] data = { "Shoes", "shoes", "boots", "Boots", "flippers",
				"Flippers", "Ragefire boots",
				"Steadfast boots", "Glaiven boots" };
		String item = getItemName(itemId);
		if (item == null) {
			return false;
		}
		boolean item1 = false;
		for (String element : data) {
			if (item.endsWith(element) || item.contains(element)) {
				item1 = true;
			}
		}
		return item1;
	}
	

	public static boolean playerGloves(int itemId) {
		String[] data = { "Gloves", "gloves", "glove", "Glove", "gauntlets",
				"Gauntlets", "vambraces", "vamb", "v'brace" };
		String item = getItemName(itemId);
		if (item == null) {
			return false;
		}
		boolean item1 = false;
		for (String element : data) {
			if (item.endsWith(element) || item.contains(element)) {
				item1 = true;
			}
		}
		return item1;
	}

	public static boolean playerShield(int itemId) {
		String[] data = { "kiteshield", "Lit bug lantern", "Unlit bug lantern", "book", "Kiteshield", "shield",
				"Shield", "Kite", "kite", "defender", "ket-xil", "Book", "Off-hand" };
		String item = getItemName(itemId);
		if (item == null) {
			return false;
		}
		boolean item1 = false;
		for (String element : data) {
			if (item.endsWith(element) || item.contains(element)) {
				item1 = true;
			}
		}
		return item1;
	}

	public static boolean playerAmulet(int itemId) {
		String[] data = { "amulet", "Amulet", "necklace", "Necklace",
				"Pendant", "pendant", "Symbol", "symbol", "scarf", "Scarf",
				"scarve", "Scarve", "stole", "Stole", "Saradomin halo" };
		String item = getItemName(itemId);
		if (item == null) {
			return false;
		}
		boolean item1 = false;
		for (String element : data) {
			if (item.endsWith(element) || item.contains(element)) {
				item1 = true;
			}
		}
		return item1;
	}

	public static boolean playerArrows(int itemId) {
		String[] data = { "Arrows", "arrows", "Arrow", "arrow", "Bolts",
				"bolts", "Bolt", "bolt" };
		String item = getItemName(itemId);
		if (item == null) {
			return false;
		}
		boolean item1 = false;
		for (String element : data) {
			if (item.endsWith(element) || item.contains(element)) {
				item1 = true;
			}
		}
		return item1;
	}

	public static boolean playerRings(int itemId) {
		String[] data = { "ring", "rings", "Ring", "Rings", };
		String item = getItemName(itemId);
		if (item == null) {
			return false;
		}
		boolean item1 = false;
		for (String element : data) {
			if (item.endsWith(element) || item.contains(element)) {
				item1 = true;
			}
		}
		return item1;
	}

	public static boolean playerHats(int itemId) {
		String[] data = { "boater", "Light Helmet", "cowl", "peg", "coif", "helm", "Coif",
				"mask", "Mask", "hat", "headband", "hood", "Hood", "Halo", "halo", "disguise",
				"cavalier", "full helm", "tiara", "helmet", "Hat", "ears",
				"partyhat", "helm(t)", "helm(g)", "beret", "facemask",
				"sallet", "hat(g)", "hat(t)", "bandana", "Helm", "mitre",
				"Mitre", "afro", "skull", "Crown", "Star-face"};
		String item = getItemName(itemId);
		if (item == null) {
			return false;
		}
		boolean item1 = false;
		for (String element : data) {
			if ((item.endsWith(element) || item.contains(element))
					&& itemId != 4214) {
				item1 = true;
			}
		}
		return item1;
	}

	public static boolean playerLegs(int itemId) {
		String[] data = { "tassets", "Ladies Platelegs", "Seth Chaps", "Light Legs", "Owner Legs", "chaps", "bottoms", "gown", "trousers",
				"platelegs", "pantaloons", "robe", "plateskirt", "legs", "leggings",
				"shorts", "Shorts", "Skirt", "greaves","Knightmare legs", "Torva Platelegs", "Priml Platelegs", "skirt", "cuisse", "Trousers", };
		String item = getItemName(itemId);
		if (item == null) {
			return false;
		}
		boolean item1 = false;
		for (String element : data) {
			if ((item.endsWith(element) || item.contains(element))
					&& !item.contains("top") && !item.contains("robe (g)")
					&& !item.contains("robe (t)")) {
				item1 = true;
			}
		}
		return item1;
	}

	public static boolean playerBody(int itemId) {
		String[] data = { "body", "Demon Plate", "Torva Platebody", "Black Platebody (W)", "Light Plate", "top", "Owner Plate", "Priest gown", "apron", "shirt",
				"platebody", "robetop", "body(g)", "body(t)",
				"Wizard robe (t)", "jacket", "body", "Body", "brassard", "blouse",
				"tunic", "leathertop", "Saradomin plate", "chainbody",
				"Platebody", "Chainbody", "Robe Top", "Robe top", "hauberk",
				"Shirt", "torso", "Torso", "chestplate", "Wizard robe (g)",
				"Snakeskin body", "Morrigan's Body" };
		String item = getItemName(itemId);
		if (item == null) {
			return false;
		}
		boolean item1 = false;
		for (String element : data) {
			if (item.endsWith(element) || item.contains(element)) {
				item1 = true;
			}
		}
		return item1;
	}
    
	public static String getItemName(int id) {
		return ItemCacheDefinition.forID(id).getName();
}
	
	public static boolean[] itemStackable = new boolean[Config.ITEM_LIMIT];
	public static boolean[] itemIsNote = new boolean[Config.ITEM_LIMIT];
	public static int[] targetSlots = new int[Config.ITEM_LIMIT];
	public static void load() {
		int counter = 0;
		int c;
		
		try {
			FileInputStream dataIn = new FileInputStream(new File("./Data/data/stackable.dat"));
			while ((c = dataIn.read()) != -1) {
				if (c == 0) {
					itemStackable[15243] = true;
					itemStackable[15263] = true;
					itemStackable[12437] = true;
					itemStackable[12434] = true;
					itemStackable[12825] = true;
					itemStackable[12435] = true;
					itemStackable[9187] = true;
					itemStackable[9188] = true;
					itemStackable[9189] = true;
					itemStackable[9190] = true;
					itemStackable[9191] = true;
					itemStackable[9192] = true;
					itemStackable[9140] = true; 
					itemStackable[9141] = true; 
					itemStackable[9142] = true; 
					itemStackable[9143] = true; 
					itemStackable[9144] = true; 
					itemStackable[9194] = true;
					itemStackable[6571] = false;
					itemStackable[9193] = true;
					itemStackable[12437] = true;
					itemStackable[11230] = true;
					itemStackable[11237] = true;
					itemIsNote[15273] = true;
					itemStackable[7219] = true;
					itemStackable[11212] = true;
					itemStackable[11232] = true;
					itemStackable[9341] = true;
					itemStackable[15273] = true;
					itemStackable[counter] = false;
				} else {
					itemStackable[counter] = true;
				}
				counter++;
			}
			dataIn.close();
		} catch (IOException e) {
			System.out.println("Critical error while loading stackabledata! Trace:");
			e.printStackTrace();
		}

		counter = 0;
		
		try {
			FileInputStream dataIn = new FileInputStream(new File("./Data/data/notes.dat"));
			while ((c = dataIn.read()) != -1) {
				if (c == 0) {
					itemIsNote[counter] = true;
				} else {
					itemIsNote[counter] = false;
				}
				counter++;
			}
			dataIn.close();
		} catch (IOException e) {
			System.out.println("Critical error while loading notedata! Trace:");
			e.printStackTrace();
		}
		
		counter = 0;
		try {
			FileInputStream dataIn = new FileInputStream(new File("./Data/data/equipment.dat"));
			while ((c = dataIn.read()) != -1) {
				targetSlots[counter++] = c;
			}
			dataIn.close();
		} catch (IOException e) {
			System.out.println("Critical error while loading notedata! Trace:");
			e.printStackTrace();
		}
	}

	protected int id;
	protected int amount;

	public Item() {
		this.id = 0;
		this.amount = 0;
	}

	public Item(int id, int amount) {
		this.id = id;
		this.amount = amount;
	}
	
	public Item(int id) {
		this.id = id;
		this.amount = 1;
	}

	public int getId() {
		return id;
	}

	public void setId(int value) {
		id = value;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int value) {
		amount = value;
	}

	public static boolean isFullBody(int itemId) {
		if(itemId == -1)
			return false;
		return ItemDefinition.forId(itemId).isPlatebody();
	}

}