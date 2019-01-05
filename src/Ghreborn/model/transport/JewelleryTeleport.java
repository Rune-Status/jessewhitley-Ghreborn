package Ghreborn.model.transport;

import Ghreborn.model.content.teleport.Position;
import Ghreborn.model.content.teleport.TeleportExecutor;
import Ghreborn.model.players.Client;

public class JewelleryTeleport {

	public enum Jewellery {
		GLORY(new int[]{11978, 11976, 1712, 1710, 1708, 1706, 1704}),
		DUELLING(new int[]{2552, 2554, 2556, 2558, 2560, 2562, 2564, 2566, -1}),
		GAMES(new int[]{3853, 3855, 3857, 3859, 3861, 3863, 3865, 3867, -1}),
		SKILLS(new int[]{11968, 11970, 11105, 11107, 11109, 11111, 11113}),
		COMBAT(new int[]{11972, 11974, 11118, 11120, 11122, 11124, 11126});

		int[] ids;

		private Jewellery(int[] ids) {
			this.ids = ids;
		}
		
		public int[] getIds() {
			return ids;
		}
	}

	public static void replaceItem(Client player) {
		player.getItems().deleteItem2(player.getClickItem(), 1);
			int item = findNextJewellery(player.getClickItem());
			if (item > 0) {
				player.getItems().addItem(item, 1);
			}
	}

	public static void teleport(Client player, int x, int y, int h) {
		player.getPA().removeAllWindows();
		if (!player.getItems().playerHasItem(player.getClickItem())) {
			return;
		}
		TeleportExecutor.teleport(player, new Position(x, y, h));
		replaceItem(player);
	}

	public static int findNextJewellery(int id) {
		boolean getNext = false;
		for (Jewellery jewellery : Jewellery.values()) {
			for (int i : jewellery.getIds()) {
				if (getNext) {
					return i;
				}
				if (id == i) {
					getNext = true;
				}
			}
		}
		return 0;
	}

}