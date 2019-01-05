package Ghreborn.model.players.skills.farming;

import Ghreborn.model.players.Client;
import Ghreborn.model.players.Player;

public class Farming {

	public static boolean prepareCrop(Client player, int item, int id, int x, int y) {
		// plant pot
		//if (player.getSeedling().fillPotWithSoil(item, x, y)) {
			//return true;
		//}
		// allotments
		//if (player.getAllotment().curePlant(x, y, item)) {
			//return true;
		//}false
		//if (player.getAllotment().putCompost(x, y, item)) {
			//return true;
		//}
		if (player.getAllotment().plantSeed(x, y, item)) {
			return true;
		}
		if (player.getAllotment().clearPatch(x, y, item)) {
			return true;
		}
		if (player.getBushes().clearPatch(x, y, item)) {
			return true;
		}
		/*if (item >= 3422 && item <= 3428 && id == 4090) {
			player.getInventory().removeItem(new Item(item));
			player.getInventory().addItem(new Item(item + 8));
			player.getUpdateFlags().sendAnimation(832);
			player.getActionSender().sendMessage("You put the olive oil on the fire, and turn it into sacred oil.");
			return true;
		}*/
		if (item <= 5340 && item > 5332) {
			if (player.getAllotment().waterPatch(x, y, item)) {
				return true;
			}
		}
		return false;
	}

	public static boolean inspectObject(Player player, int x, int y) {
		// allotments
		if (player.getAllotment().inspect(x, y)) {
			return true;
		}
		return false;
	}

	public static boolean guide(Player player, int x, int y) {
		// allotments
		return false;
	}

	public static boolean harvest(Player player, int x, int y) {
		// allotments
		if (player.getAllotment().harvest(x, y)) {
			return true;
		}
		return false;
	}
}
