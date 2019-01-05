package Ghreborn.model.items;


import java.util.NoSuchElementException;

/**
 * The container that represents an item definition.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class ItemDefinition {

	/**
	 * The array that contains all of the item definitions.
	 */
	public static final ItemDefinition[] DEFINITIONS = new ItemDefinition[30000];

	/**
	 * The identifier for the item.
	 */
	private final int id;

	/**
	 * The proper name of the item.
	 */
	private final String name;

	/**
	 * The description of the item.
	 */
	private final String description;

	/**
	 * The equipment slot of this item.
	 */
	private final int equipmentSlot;

	/**
	 * The flag that determines if the item is noteable.
	 */
	private final boolean noteable;

	/**
	 * The flag that determines if the item is noted.
	 */
	private final boolean noted;

	/**
	 * The flag that determines if the item is stackable.
	 */
	private final boolean stackable;

	/**
	 * The special store price of this item.
	 */
	private final int specialPrice;

	/**
	 * The general store price of this item.
	 */
	private final int generalPrice;

	/**
	 * The low alch value of this item.
	 */
	private final int lowAlchValue;

	/**
	 * The high alch value of this item.
	 */
	private final int highAlchValue;

	/**
	 * The weight value of this item.
	 */
	private final double weight;

	/**
	 * The array of bonuses for this item.
	 */
	private final int[] bonus;

	/**
	 * The flag that determines if this item is two-handed.
	 */
	private final boolean twoHanded;

	/**
	 * The flag that determines if this item is a full helmet.
	 */
	private final boolean fullHelm;

	/**
	 * The flag that determines if this item is a platebody.
	 */
	private final boolean platebody;

	/**
	 * The flag that determines if this item is tradeable.
	 */
	private final boolean tradeable;

	/**
	 * Creates a new {@link ItemDefinition}.
	 *
	 * @param id
	 *            the identifier for the item.
	 * @param name
	 *            the proper name of the item.
	 * @param description
	 *            the description of the item.
	 * @param equipmentSlot
	 *            the equipment slot of this item.
	 * @param noteable
	 *            the flag that determines if the item is noteable.
	 * @param stackable
	 *            the flag that determines if the item is stackable.
	 * @param specialPrice
	 *            the special store price of this item.
	 * @param generalPrice
	 *            the general store price of this item.
	 * @param lowAlchValue
	 *            the low alch value of this item.
	 * @param highAlchValue
	 *            the high alch value of this item.
	 * @param weight
	 *            the weight value of this item.
	 * @param bonus
	 *            the array of bonuses for this item.
	 * @param twoHanded
	 *            the flag that determines if this item is two-handed.
	 * @param fullHelm
	 *            the flag that determines if this item is a full helmet.
	 * @param platebody
	 *            the flag that determines if this item is a platebody.
	 * @param tradeable
	 *            the flag that determines if this item is tradeable.
	 */
	public ItemDefinition(int id, String name, String description, int equipmentSlot, boolean noteable, boolean noted,
			boolean stackable, int specialPrice, int generalPrice, int lowAlchValue, int highAlchValue, double weight,
			int[] bonus, boolean twoHanded, boolean fullHelm, boolean platebody, boolean tradeable) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.equipmentSlot = equipmentSlot;
		this.noteable = noteable;
		this.noted = noted;
		this.stackable = stackable;
		this.specialPrice = specialPrice;
		this.generalPrice = generalPrice;
		this.lowAlchValue = lowAlchValue;
		this.highAlchValue = highAlchValue;
		this.weight = weight;
		this.bonus = bonus;
		this.twoHanded = twoHanded;
		this.fullHelm = fullHelm;
		this.platebody = platebody;
		this.tradeable = tradeable;
	}

	public ItemDefinition(int id) {
		this(id, "Item #" + id, "", 3, false, false, false, 0, 0, 0, 0, 0, new int[14], false, false, false, false);
	}

	public static int getIdForName(String name) {
		if (name == null)
			return -1;
		for (int i = 0; i < DEFINITIONS.length; i++) {
			if (DEFINITIONS[i] == null)
				continue;
			if (DEFINITIONS[i].getName().toLowerCase().equalsIgnoreCase(name.toLowerCase()))
				return i;
		}
		return -1;
	}

	public boolean isWearable() {
		return equipmentSlot != -1;
	}

	public static ItemDefinition forId(int id) {
		if (id < 0 || id > DEFINITIONS.length || DEFINITIONS[id] == null) {
			return new ItemDefinition(id);
		}
		return DEFINITIONS[id];
	}

	// cached unnoted id
	private int unnotedId = -1;

	// much smarter way to do it, with better performance
	public int getUnnotedId() {
		if (unnotedId == -1) {

			// we start with a simple inexpensive lookup, the most common way
			// which is +1 of the item id
			int unnoted = id - 1;
			if (!ItemDefinition.DEFINITIONS[unnoted].isNoted()) {

				// cache the result so we don't have to do this in the future...
				unnotedId = unnoted;
				return unnotedId;
			}

			// if it isn't found there, we do an expensive lookup
			for (ItemDefinition it : DEFINITIONS) {
				if (it.name.equals(name) && !it.noted) {

					// cache the result so we don't have to do this in the
					// future...
					unnotedId = it.id;
					return unnotedId;
				}
			}

			// if it's still not found, throw an exception
			throw new NoSuchElementException("unnoted id not found for id: " + id);
		}

		// we already calculated the unnoted item, return the previously
		// calculated value (it never changes)
		return unnotedId;
	}

	/**
	 * Gets the identifier for the item.
	 * @param i 
	 *
	 * @return the identifier.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the proper name of the item.
	 *
	 * @return the proper name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the description of the item.
	 *
	 * @return the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Gets the equipment slot of this item.
	 *
	 * @return the equipment slot.
	 */
	public int getEquipmentSlot() {
		return equipmentSlot;
	}

	/**
	 * Determines if the item is noted or not.
	 *
	 * @return {@code true} if the item is noted, {@code false} otherwise.
	 */
	public boolean isNoted() {
		return noted;
	}

	/**
	 * Determines if the item is noteable or not.
	 *
	 * @return {@code true} if the item is noteable, {@code false} otherwise.
	 */
	public boolean isNoteable() {
		return noteable && DEFINITIONS[id + 1].isNoted();
	}

	/**
	 * Determines if the item is equipable or not.
	 * 
	 * @return {@code true} if the item is equipable, {@code false} otherwise.
	 */
	public boolean isEquipable() {
		return equipmentSlot != -1;
	}

	/**
	 * Determines if the item is stackable or not.
	 *
	 * @return {@code true} if the item is stackable, {@code false} otherwise.
	 */
	public boolean isStackable() {
		return stackable || noted;
	}

	/**
	 * Gets the special store price of this item.
	 * 
	 * @param i
	 *
	 * @return the special price.
	 */
	public int getSpecialPrice() {
		return specialPrice;
	}

	/**
	 * Gets the general store price of this item.
	 *
	 * @return the general price.
	 */
	public int getGeneralPrice() {
		return generalPrice;
	}

	/**
	 * Gets the low alch value of this item.
	 *
	 * @return the low alch value.
	 */
	public int getLowAlchValue() {
		return lowAlchValue;
	}

	/**
	 * Gets the high alch value of this item.
	 * 
	 * @param itemId
	 *
	 * @return the high alch value.
	 */
	public int getHighAlchValue() {
		return highAlchValue;
	}

	/**
	 * Gets the weight value of this item.
	 *
	 * @return the weight value.
	 */
	public double getWeight() {
		return weight;
	}

	/**
	 * Gets the array of bonuses for this item.
	 *
	 * @return the array of bonuses.
	 */
	public int[] getBonus() {
		return bonus;
	}

	/**
	 * Determines if this item is two-handed or not.
	 *
	 * @return {@code true} if this item is two-handed, {@code false} otherwise.
	 */
	public boolean isTwoHanded() {
		return twoHanded;
	}

	/**
	 * Determines if this item is a full helmet or not.
	 *
	 * @return {@code true} if this item is a full helmet, {@code false}
	 *         otherwise.
	 */
	public boolean isFullHelm() {
		return fullHelm;
	}

	/**
	 * Determines if this item is a platebody or not.
	 *
	 * @return {@code true} if this item is a platebody, {@code false}
	 *         otherwise.
	 */
	public boolean isPlatebody() {
		return platebody;
	}

	/**
	 * Determines if this item is tradeable.
	 * 
	 * @return {@code true} if this item is tradeable, {@code false} otherwise.
	 */
	public boolean isTradeable() {
		return tradeable;
	}
}