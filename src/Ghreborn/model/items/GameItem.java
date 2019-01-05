package Ghreborn.model.items;

public class GameItem {
	
	public int id;

	public int amount;

	private int slot;
	
	public boolean stackable;
	public GameItem(int id) {
		this.id = id;
		this.amount = 1;
		if (ItemDefinition.forId(id).isStackable()) {
			stackable = true;
		}
	}
	/**
	 * Constructs a new game item with an id and amount
	 * @param id the id of the item
	 * @param amount the amount of the item
	 */
	public GameItem(int id, int amount) {
		this(id);
		this.amount = amount;
	}
	
	public GameItem(int id, int amount, int slot) {
		this(id, amount);
		this.amount = amount;
		this.slot = slot;
	}
	
	/**
	 * Returns a new GameItem object with a new id and amount
	 * @param id		the item id
	 * @param amount	the item amount
	 * @return	a new game item
	 */
	public GameItem set(int id, int amount) {
		return new GameItem(id, amount);
	}
	
	/**
	 * Retries the item id for the game item 
	 * @return the item id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Retrieves the amount of the game item
	 * @return the amount
	 */
	public int getAmount() {
		return amount;
	}
	
	/**
	 * Sets the amount of the game item
	 * @param the amount
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	/**
	 * The slot the game item exists in the container
	 * @return	the slot
	 */
	public int getSlot() {
		return slot;
	}
	
	/**
	 * Determines if the item is stackable
	 * @return true if the item is stackable, false if it is not.
	 */
	public boolean isStackable() {
		return stackable;
	}

}