package Ghreborn.model.content;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import Ghreborn.event.CycleEvent;
import Ghreborn.event.CycleEventContainer;
import Ghreborn.event.CycleEventHandler;
import Ghreborn.model.items.GameItem;
import Ghreborn.model.items.ItemAssistant;
import Ghreborn.model.players.Client;
import Ghreborn.util.Misc;

public class MoneyBag extends CycleEvent {

	/**
	 * The item Id of the Money bag
	 */
	public static final int MONEY_BAG = 23958;
	/**
	 * A map containing a List of {@link GameItem}'s that contain items relevant to their rarity.
	 */
	private static Map<Rarity, List<GameItem>> items = new HashMap<>();
	static {
		items.put(Rarity.COMMON, 
			Arrays.asList(
				new GameItem(23959, 1))
		);
		
	items.put(Rarity.UNCOMMON,
			Arrays.asList(
					new GameItem(23960, 1))
	);
		
		items.put(Rarity.RARE,
				Arrays.asList(
						new GameItem(23961, 1), new GameItem(23962, 1)));
	}
	private Client c;
	
	public MoneyBag(Client c) {
		this.c = c;
	}
	public void open() {
		if (System.currentTimeMillis() - c.lastMysteryBox < 1000) {
			return;
		}
		if (c.getItems().freeSlots() < 2) {
			c.sendMessage("You need atleast two free slots to open a Money Bag.");
			return;
		}
		if (!c.getItems().playerHasItem(MONEY_BAG)) {
			c.sendMessage("You need a Money Bag to do this.");
			return;
		}
		c.lastMysteryBox = System.currentTimeMillis();
		CycleEventHandler.getSingleton().stopEvents(this);
		CycleEventHandler.getSingleton().addEvent(this, this, 2);
	}
	@Override
	public void execute(CycleEventContainer container) {
		if (c.disconnected || Objects.isNull(c)) {
			container.stop();
			return;
		}
		int random = Misc.random(21);
		List<GameItem> itemList = random < 5 ? items.get(Rarity.COMMON) : random >= 5 && random <= 20 ? items.get(Rarity.UNCOMMON) : items.get(Rarity.RARE);
		GameItem item = Misc.getRandomItem(itemList);
		c.getItems().deleteItem(MONEY_BAG, 1);
			c.getItems().addItem(item.getId(), item.getAmount());
			c.sendMessage("<col=255>You have identified the money bag as " + ItemAssistant.getItemName(item.getId()) + "</col>.");
		
		container.stop();
	}
	/**
	 * Represents the rarity of a certain list of items
	 */
	enum Rarity {
		UNCOMMON, COMMON, RARE
	}


}
