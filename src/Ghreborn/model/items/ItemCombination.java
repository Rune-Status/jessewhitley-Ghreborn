package Ghreborn.model.items;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import Ghreborn.model.players.Client;

/**
 * 
 * @author Jason MacKeigan
 * @date Nov 21, 2014, 12:38:16 PM
 */
public abstract class ItemCombination {
	/**
	 * List of items that are required in the combination
	 */
	protected List<GameItem> items;

	/**
	 * The item received when the items are combined
	 */
	protected GameItem outcome;

	/**
	 * The item that can be reverted from the combination, if possible.
	 */
	protected Optional<List<GameItem>> revertedItems;

	/**
	 * Creates a new item combination
	 * 
	 * @param items the game items required
	 */
	public ItemCombination(GameItem outcome, Optional<List<GameItem>> revertedItems, GameItem... items) {
		this.items = Arrays.asList(items);
		this.outcome = outcome;
		this.revertedItems = revertedItems;
	}

	/**
	 * Combines all of the items to create the outcome
	 * 
	 * @param Client the Client combining the items
	 */
	public abstract void combine(Client Client);

	/**
	 * Shows the initial dialogue with basic information about the combination
	 * 
	 * @param Client the Client
	 */
	public abstract void showDialogue(Client Client);

	/**
	 * Reverts the outcome, if possible, back to the items used to combine it
	 * 
	 * @param Client the Client requesting reversion
	 */
	public void revert(Client Client) {
		if (!revertedItems.isPresent()) {
			return;
		}
		if (Client.getItems().freeSlots() < revertedItems.get().size()) {
			Client.getDH().sendStatement("You need atleast " + revertedItems.get().size() + "" + " inventory slots to do this.");
			Client.nextChat = -1;
			return;
		}
		Client.getItems().deleteItem2(outcome.getId(), outcome.getAmount());
		revertedItems.get().forEach(item -> Client.getItems().addItem(item.getId(), item.getAmount()));
		Client.getDH().sendStatement("The " + ItemAssistant.getItemName(outcome.getId()) + " has been split up.", "You have received some of the item(s) used in the making of",
				"this item.");
		Client.nextChat = -1;
	}

	/**
	 * Sends the confirmation page to the Client so they can choose to cancel it
	 * 
	 * @param Client the Client
	 */
	public void sendCombineConfirmation(Client Client) {
		//Client.getDH().sendOption2("Combine items?", "Yes", "Cancel");
	}

	/**
	 * Sends the confirmation page to the Client so thay revert the item, or cancel the decision.
	 * 
	 * @param Client the Client
	 */
	public void sendRevertConfirmation(Client Client) {
		//Client.getDH().sendOption2("Revert item?", "Yes", "Cancel");
	}

	/**
	 * Determines if the Client has all of the items required for the combination.
	 * 
	 * @param Client the Client making the combination
	 * @return true if they have all of the items, otherwise false
	 */
	public boolean isCombinable(Client Client) {
		Optional<GameItem> unavailableItem = items.stream().filter(i -> !Client.getItems().playerHasItem(i.getId(), i.getAmount())).findFirst();
		return !unavailableItem.isPresent();
	}

	/**
	 * Determines if the items used match that required in the combination
	 * 
	 * @param item1 the first item
	 * @param item2 the second item
	 * @return true if all items match, otherwise false
	 */
	public boolean itemsMatch(GameItem item1, GameItem item2) {
		return items.stream().filter(itemValuesMatch(item1).or(itemValuesMatch(item2))).count() == 2;
	}

	private static final Predicate<GameItem> itemValuesMatch(GameItem item) {
		return i -> i.getId() == item.getId() && i.getAmount() <= item.getAmount();
	}

	/**
	 * List of the required items
	 * 
	 * @return the items
	 */
	public List<GameItem> getItems() {
		return items;
	}

	/**
	 * Determines if the item is revertable or not
	 * 
	 * @return
	 */
	public boolean isRevertable() {
		return revertedItems.isPresent() ? true : false;
	}

	/**
	 * Attempts to retrieve the revertable item, if it exists
	 * 
	 * @return the revertable item
	 */
	public Optional<List<GameItem>> getRevertItems() {
		return revertedItems.isPresent() ? revertedItems : Optional.empty();
	}

	/**
	 * The item that we receive for combining the items
	 * 
	 * @return the item
	 */
	public GameItem getOutcome() {
		return outcome;
	}

}
