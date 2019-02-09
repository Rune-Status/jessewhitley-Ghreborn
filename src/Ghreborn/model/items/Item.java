package Ghreborn.model.items;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import Ghreborn.Config;
import Ghreborn.Server;
import Ghreborn.definitions.ItemCacheDefinition;

public class Item {

	
    // Few item types for equipping
    public static int twohanded[] = { 11785, 10431, 7158, 1319, 3202, 6528, 15333, 15335, 15332};
    public static int isHelm[] = { 0000};
    
	public static boolean isFullBody(int itemId) {
		if(itemId == -1)
			return false;
		String[] data = {"Platebody", "Pyromancer garb","platebody", "plate", "top", "wings", "blouse", "monk's robe", "shirt",
				"robetop", "robe top", "tabard", "jacket", "tunic", "chestplate", "leathertop", "brassard", "torso",
				"robe (g)", "robe (t)", "zamorak robe", "hauberk", "coat", "Mummy", "varrock", "decorative", "garb", "zamorak robe", "Justiciar chestguard" };
		String armour = getItemName(itemId);
		if (armour == null)
			return false;

		switch (itemId) {
			case 11899:
			case 11896:
				return true;
		}
		for (int i = 0; i < data.length; i++) {
			if (armour.contains(data[i])) {
				return true;
			}
		}
		return false;
	}

    public static boolean isFullHat(int itemId) {
		if(itemId == -1)
			return false;
		String[] data = {"splitbark helm", "mystic", "black mask", "highwayman mask", "mime mask",
				"facemask", "ironman helm", "grim reaper", "imp mask", "mage hat", "med helm", "coif",
				"Dharok's helm", "hood", "Initiate helm", "Coif", "Helm of neitiznot", "Armadyl helmet", "Berserker helm",
				"Archer helm", "Farseer helm", "Warrior helm", "Void", "reindeer", "cowl", "Void ranger helm",
				"Blacksmith's_helm", "black mask", "kandarin headgear", "Robin hood hat",
				"pith helmet", "spiny helmet", "gold helmet", "mining helmet", "camo helmet", "crab helmet",
				"3rd age full helmet", "dwarven helmet", "kandarin headgear", "camo"};
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
		String[] data = {"Star-face","helm", "helmet_imbued", "helm_(g)", "Bronze_full_helm", "Iron_full_helm",
				"Mithril_full_helm", "helm_(t)", "full_helm(g)", "full", "heraldic", "heraldic_helm", "full_helm",
				"Verac's_helm", "Guthan's_helm", "Karil's_coif", "ween_mask", "Torag's_helm", "Void", "sallet",
				"slayer helmet", "Void_ranger_helm", "Shayzien", "Graceful", "gas_mask", "serpentine_helm", "magma_helm",
				"ankou", "demon", "mummy", "head", "Banshee", "Goblin mask", "Jack_lantern_mask", "h'ween",
				"slayer helmet (i)", "rogue mask", "santa mask", "antisanta mask", "Splitbark helm", "Justiciar faceguard"};
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
		String[] data = { "kiteshield", "off-hand",  "Off-Hand","Tome of fire", "Dragonfire ward", "Lit bug lantern", "Unlit bug lantern", "book", "Kiteshield", "shield",
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
				"scarve", "Scarve", "stole", "Slenderman Tenticles", "Stole", "Saradomin halo" };
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
		String[] data = { "boater", "Justiciar faceguard", "Light Helmet", "cowl", "peg", "coif", "helm", "Coif",
				"mask", "Mask", "hat", "headband", "hood", "Hood", "Halo", "halo", "disguise",
				"cavalier", "full helm", "tiara", "helmet", "Hat", "ears",
				"partyhat", "helm(t)", "helm(g)", "beret", "facemask",
				"sallet", "hat(g)", "hat(t)", "bandana", "Helm", "mitre",
				"Mitre", "afro", "skull", "Crown", "Royal crown" ,"Star-face"};
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
		String[] data = { "tassets", "Ladies Platelegs", "Justiciar legguards","Seth Chaps", "Light Legs", "Owner Legs", "chaps", "bottoms", "gown", "trousers",
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
		String[] data = { "body", "Pyromancer garb","Demon Plate", "Justiciar chestguard", "Torva Platebody", "Black Platebody (W)", "Light Plate", "top", "Owner Plate", "Priest gown", "apron", "shirt",
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
	static {
		try {
			List<String> stackableData = Files.readAllLines(Paths.get("./Data/", "data", "stackables.dat"));
			for (String data : stackableData) {
				int id = Integer.parseInt(data.split("\t")[0]);
				boolean stackable = Boolean.parseBoolean(data.split("\t")[1]);
				itemStackable[id] = stackable;
				itemStackable[21880] = true;
				itemStackable[6646] = true;
				itemStackable[6651] = true;
				itemStackable[21930] = true;
				itemStackable[7776] = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		int counter = 0;
		int c = 0;
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
			System.out.println("Loaded " + counter + " noted configurations");
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
			System.out.println("Loaded " + counter + " equipment configurations");
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

}