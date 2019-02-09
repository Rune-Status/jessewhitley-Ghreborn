package Ghreborn.model.items.item_combinations;

import java.util.List;
import java.util.Optional;

import Ghreborn.model.items.GameItem;
import Ghreborn.model.items.ItemCombination;
import Ghreborn.model.players.Client;

public class DragonPickaxe extends ItemCombination {

	public DragonPickaxe(GameItem outcome, Optional<List<GameItem>> revertedItems, GameItem[] items) {
		super(outcome, revertedItems, items);
	}

	@Override
	public void combine(Client player) {
		super.items.forEach(item -> player.getItems().deleteItem2(item.getId(), item.getAmount()));
		player.getItems().addItem(super.outcome.getId(), super.outcome.getAmount());
		player.getDH().sendItemStatement("You combined the items and created the upgraded Dragon Pickaxe.", 12797);
		//player.setCurrentCombination(Optional.empty());
		player.nextChat = -1;
	}

	@Override
	public void showDialogue(Client player) {
		player.getDH().sendStatement("The upgraded Dragon pickaxe is untradeable.", "You can revert this but you will the upgrade kit.");
	}

}
